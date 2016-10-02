package spoonPRAnalyser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestFileDetail;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import gumtree.spoon.AstComparator;
import gumtree.spoon.diff.Diff;
import spoonBot.SpoonBot;

public class SpoonPRAnalyzer {
	
	/*AstComparator diff = new AstComparator();
	File fl = new File("src/test/resources/examples/test3/CommandLine1.java");
	File fr = new File("src/test/resources/examples/test3/CommandLine2.java");*/

	private GHRepository repo;
	private List<GHPullRequest> pullRequests;
	private HashMap<GHPullRequest, List<GHPullRequestFileDetail>> filesDetails;
	private int pullRequestIndex = 0;
	
	public SpoonPRAnalyzer(String repoName){
		try {
			GitHub github = GitHub.connect();
		
			repo = github.getRepository(repoName);
			
			pullRequests = repo.getPullRequests(GHIssueState.OPEN);
			filesDetails = new HashMap<GHPullRequest, List<GHPullRequestFileDetail>>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void Start(SpoonBot spoonBot){
		while(true){
			try {
				System.out.println("Fetching PRs.");
				pullRequests = repo.getPullRequests(GHIssueState.OPEN);
				int cpt = 0;
				for(GHPullRequest PR : pullRequests){
					pullRequestIndex = cpt;
					if (new Date().getTime() - PR.getCreatedAt().getTime() <= 10000) {
						System.out.println("	New PR detected : adding bot comment");
						String msg = "------This is an automatic message------\r\n\r\n" ;
						msg += spoonBot.BuildMessage(this);
						PR.comment(msg);
					}else{
						System.out.println("	Old PR detected. Nothing to do.");
					}
					cpt++;
				}
				
				System.out.println("Waiting 10 secs.");
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	


	private List<GHPullRequestFileDetail> GetFiles(GHPullRequest PR){
		if(!filesDetails.containsKey(PR)){
			filesDetails.put(PR, PR.listFiles().asList());
		}
		return filesDetails.get(PR);
	}
	
	public void ClosePR(){
		try {
			if(pullRequests.get(pullRequestIndex).getState() != GHIssueState.CLOSED){
				pullRequests.get(pullRequestIndex).close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String GetSourceBranchName(){
		return pullRequests.get(pullRequestIndex).getHead().getLabel().split(":")[1];
	}
	
	public String GetTargetBranchName(){
		return pullRequests.get(pullRequestIndex).getBase().getLabel().split(":")[1];
	}
	
	public int GetNumberOfCommit(){
		return pullRequests.get(pullRequestIndex).listCommits().asList().size();
	}
	
	public int GetNumberOfPullRequests(){
		return pullRequests.size();
	}
	
	public int GetNumberOfFiles(){
		return GetFiles(pullRequests.get(pullRequestIndex)).size();
	}
	
	public String GetFileName(int fileIndex){
		return GetFiles(pullRequests.get(pullRequestIndex)).get(fileIndex).getFilename();
	}
	
	public int GetNumberOfNewMethodInFile(int fileIndex){
		Matcher m;
		
		GHPullRequestFileDetail fileDetail = GetFiles(pullRequests.get(pullRequestIndex)).get(fileIndex);

		String beforeUrl = "https://raw.githubusercontent.com/" + repo.getFullName() + "/" + GetTargetBranchName() + "/" + fileDetail.getFilename(); 
		String afterUrl = fileDetail.getRawUrl().toString();
		
		InputStream before, after;
		File fileBefore = null, fileAfter = null;
		try {
			fileBefore = File.createTempFile("before", ".java");
			//fileBefore.deleteOnExit();
			fileAfter = File.createTempFile("after", ".java");
			//fileAfter.deleteOnExit();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {			
			before = new URL(beforeUrl).openStream();
			after = new URL(afterUrl).openStream();
			
			FileOutputStream out = new FileOutputStream(fileBefore);
		    IOUtils.copy(before, out);
		    
		    FileOutputStream out2 = new FileOutputStream(fileAfter);
		    IOUtils.copy(after, out2);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int count = 0;
		
		AstComparator diff = new AstComparator();
		Diff difference = null;
		try {
			difference = diff.compare(fileBefore, fileAfter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return count;
	}
	
	
}
