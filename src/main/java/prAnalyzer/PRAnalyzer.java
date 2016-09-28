package prAnalyzer;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestFileDetail;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import bot.Bot;

public class PRAnalyzer {
	
	private GHRepository repo;
	private List<GHPullRequest> pullRequests;
	private HashMap<GHPullRequest, List<GHPullRequestFileDetail>> filesDetails;
	private int pullRequestIndex = -1;
	
	public PRAnalyzer(String repoName){
		try {
			GitHub github = GitHub.connect();
		
			repo = github.getRepository(repoName);
			
			pullRequests = repo.getPullRequests(GHIssueState.OPEN);
			filesDetails = new HashMap<GHPullRequest, List<GHPullRequestFileDetail>>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void Start(Bot bot){
		while(true){
			try {
				System.out.println("Fetching PRs.");
				pullRequests = repo.getPullRequests(GHIssueState.OPEN);
				int cpt = 0;
				for(GHPullRequest PR : pullRequests){
					if (new Date().getTime() - PR.getCreatedAt().getTime() <= 10000) {
						System.out.println("	New PR detected : adding bot comment");
						pullRequestIndex = cpt;
						String msg = "------This is an automatic message------\r\n\r\n" ;
						msg += bot.BuildMessage(this);
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
			
		int count = 0;
		if(fileDetail.getFilename().endsWith(".java")){
			m = Regex.GetMethodAddedPattern().matcher(fileDetail.getPatch()); 
			while(m.find()) { 		
				count++;
			}
		}
		
		return count;
	}
	
	public String GetNewMethodPrototype(int fileIndex, int methodIndex){
		Matcher m;
		
		GHPullRequestFileDetail fileDetail = GetFiles(pullRequests.get(pullRequestIndex)).get(fileIndex);
		
		int count = 0;
		if(fileDetail.getFilename().endsWith(".java")){
			m = Regex.GetMethodAddedPattern().matcher(fileDetail.getPatch()); 					
			
			while(m.find()) { 		
				if(count == methodIndex){
					return m.group().replaceAll("(\\+ *)|( *\\{)", "");
				}
				count++;
			}
		}
		
		return "Error";
	}
	
	public int GetNumberOfDeletedMethodInFile(int fileIndex){
		Matcher m;
		
		GHPullRequestFileDetail fileDetail = GetFiles(pullRequests.get(pullRequestIndex)).get(fileIndex);
		
		int count = 0;
		if(fileDetail.getFilename().endsWith(".java")){
			m = Regex.GetMethodDeletedPattern().matcher(fileDetail.getPatch()); 	
			while(m.find()) { 		
				count++;
			}
		}
		
		return count;
	}
	
	public String GetDeletedMethodPrototype(int fileIndex, int methodIndex){
		Matcher m;
		
		GHPullRequestFileDetail fileDetail = GetFiles(pullRequests.get(pullRequestIndex)).get(fileIndex);
		
		if(fileDetail.getFilename().endsWith(".java")){
			m = Regex.GetMethodDeletedPattern().matcher(fileDetail.getPatch()); 					
			int count = 0;
			while(m.find()) { 	
				if(count == methodIndex){
					return m.group().replaceAll("(\\+ *)|( *\\{)", "");
				}
				count++;
			}
		}
		
		return "Error";
	}
	
	public int GetNumberOfNewTestInFile(int fileIndex){
		Matcher m;
		
		GHPullRequestFileDetail fileDetail = GetFiles(pullRequests.get(pullRequestIndex)).get(fileIndex);
			
		int count = 0;
		if(fileDetail.getFilename().endsWith(".java")){
			m = Regex.GetTestAddedPattern().matcher(fileDetail.getPatch()); 
			while(m.find()) { 		
				count++;
			}
		}
		
		return count;
	}
	
	public String GetNewTestPrototype(int fileIndex, int methodIndex){
		Matcher m;
		
		GHPullRequestFileDetail fileDetail = GetFiles(pullRequests.get(pullRequestIndex)).get(fileIndex);
		
		int count = 0;
		if(fileDetail.getFilename().endsWith(".java")){
			m = Regex.GetTestAddedPattern().matcher(fileDetail.getPatch()); 					
			
			while(m.find()) { 		
				if(count == methodIndex){
					return m.group().replaceAll("(\\+ *)|( *\\{)|(@Test)|(\r)|(\n)", "");
				}
				count++;
			}
		}
		
		return "Error";
	}
	
	public int GetNumberOfDeletedTestInFile(int fileIndex){
		Matcher m;
		
		GHPullRequestFileDetail fileDetail = GetFiles(pullRequests.get(pullRequestIndex)).get(fileIndex);
		
		int count = 0;
		if(fileDetail.getFilename().endsWith(".java")){
			m = Regex.GetTestDeletedPattern().matcher(fileDetail.getPatch()); 	
			while(m.find()) { 		
				count++;
			}
		}
		
		return count;
	}
	
	public String GetDeletedTestPrototype(int fileIndex, int methodIndex){
		Matcher m;
		
		GHPullRequestFileDetail fileDetail = GetFiles(pullRequests.get(pullRequestIndex)).get(fileIndex);
		
		if(fileDetail.getFilename().endsWith(".java")){
			m = Regex.GetTestDeletedPattern().matcher(fileDetail.getPatch()); 					
			int count = 0;
			while(m.find()) { 	
				if(count == methodIndex){
					return m.group().replaceAll("(\\+ *)|( *\\{)|(@Test)|(\r)|(\n)", "");
				}
				count++;
			}
		}
		
		return "Error";
	}
	
	public int GetNumberOfNewCommentsInFile(int fileIndex){
		Matcher m;
		
		GHPullRequestFileDetail fileDetail = GetFiles(pullRequests.get(pullRequestIndex)).get(fileIndex);
		
		int count = 0;
		if(fileDetail.getFilename().endsWith(".java")){
			m = Regex.GetCommentAddedPattern().matcher(fileDetail.getPatch()); 	
			while(m.find()) { 		
				count++;
			}
		}
		
		return count;
	}
	
	public int GetNumberOfDeletedCommentsInFile(int fileIndex){
		Matcher m;
		
		GHPullRequestFileDetail fileDetail = GetFiles(pullRequests.get(pullRequestIndex)).get(fileIndex);
		
		int count = 0;
		if(fileDetail.getFilename().endsWith(".java")){
			m = Regex.GetCommentAddedPattern().matcher(fileDetail.getPatch()); 	
			while(m.find()) { 		
				count++;
			}
		}
		
		return count;
	}
	
	public int GetNumberOfModifiedMethodsInFile(int fileIndex) {
		Matcher m;
		
		GHPullRequestFileDetail fileDetail = GetFiles(pullRequests.get(pullRequestIndex)).get(fileIndex);
			
		int count = 0;
		if(fileDetail.getFilename().endsWith(".java")){
			m = Regex.GetMethodModifiedPattern().matcher(fileDetail.getPatch()); 
			while(m.find()) {
				String method = m.group();
				String[] methodLines = method.split("\n");
				for(String l: methodLines) {
				    if(l.startsWith("+") || l.startsWith("-")) {
				    	count++;
				    	break;
				    }
				}
			}
		}
		
		return count;
	}
	
	public String GetModifiedMethodPrototype(int fileIndex, int methodIndex){
		Matcher m;
		
		GHPullRequestFileDetail fileDetail = GetFiles(pullRequests.get(pullRequestIndex)).get(fileIndex);
		
		int count = 0;
		if(fileDetail.getFilename().endsWith(".java")){
			m = Regex.GetMethodModifiedPattern().matcher(fileDetail.getPatch()); 					
			
			while(m.find()) { 	
				
				boolean isAModifiedMethod = false;
				String method = m.group();
				String[] methodLines = method.split("\n");
				for(String l: methodLines) {
				    if(l.startsWith("+") || l.startsWith("-")) {
				    	isAModifiedMethod = true;
				    	break;
				    }
				}
				
				if(count == methodIndex && isAModifiedMethod){
					return methodLines[1].replaceAll("(\\+ *)|( *\\{)", "");
				}
				if(isAModifiedMethod) count++;
			}
		}
		
		return "Error";
	}
	
	public void Nothing(){
		//Nothing.	
	}
	
}
