package prAnalyzer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestFileDetail;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

public class PRAnalyzer {
	

	private GHRepository repo;
	private List<GHPullRequest> pullRequests;
	private HashMap<GHPullRequest, List<GHPullRequestFileDetail>> filesDetails;
	
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
	
	private List<GHPullRequestFileDetail> GetFiles(GHPullRequest PR){
		if(!filesDetails.containsKey(PR)){
			filesDetails.put(PR, PR.listFiles().asList());
		}
		return filesDetails.get(PR);
	}
	
	public int GetNumberOfPullRequests(){
		return pullRequests.size();
	}
	
	public int GetNumberOfFiles(int pullRequestIndex){
		return GetFiles(pullRequests.get(pullRequestIndex)).size();
	}
	
	public String GetFileName(int pullRequestIndex, int fileIndex){
		return GetFiles(pullRequests.get(pullRequestIndex)).get(fileIndex).getFilename();
	}
	
	public int GetNumberOfNewMethodInFile(int pullRequestIndex, int fileIndex){
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
	
	public String GetNewMethodPrototype(int pullRequestIndex, int fileIndex, int methodIndex){
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
	
	public int GetNumberOfDeletedMethodInFile(int pullRequestIndex, int fileIndex){
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
	
	public String GetDeletedMethodPrototype(int pullRequestIndex, int fileIndex, int methodIndex){
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
	
	public int GetNumberOfNewTestInFile(int pullRequestIndex, int fileIndex){
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
	
	public String GetNewTestPrototype(int pullRequestIndex, int fileIndex, int methodIndex){
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
	
	public int GetNumberOfDeletedTestInFile(int pullRequestIndex, int fileIndex){
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
	
	public String GetDeletedTestPrototype(int pullRequestIndex, int fileIndex, int methodIndex){
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
	
	public int GetNumberOfNewCommentsInFile(int pullRequestIndex, int fileIndex){
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
	
	public int GetNumberOfDeletedCommentsInFile(int pullRequestIndex, int fileIndex){
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
	
	public int GetNumberOfModifiedMethodsInFile(int pullRequestIndex, int fileIndex) {
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
	
	public String GetModifiedMethodPrototype(int pullRequestIndex, int fileIndex, int methodIndex){
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
