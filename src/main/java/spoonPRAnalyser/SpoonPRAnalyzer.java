package spoonPRAnalyser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestFileDetail;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import gumtree.spoon.AstComparator;
import gumtree.spoon.diff.Diff;
import gumtree.spoon.diff.operations.*;
import spoon.reflect.declaration.*;
import spoon.support.reflect.code.*;
import spoon.support.reflect.declaration.*;
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
	
	private List<Operation> getActions(int fileIndex) {
		GHPullRequestFileDetail fileDetail = GetFiles(pullRequests.get(pullRequestIndex)).get(fileIndex);

		String beforeUrl = "https://raw.githubusercontent.com/" + repo.getFullName() + "/" + GetTargetBranchName() + "/" + fileDetail.getFilename(); 
		String afterUrl = fileDetail.getRawUrl().toString();
		
		InputStream before, after;
		File fileBefore = null, fileAfter = null;
		try {
			fileBefore = File.createTempFile("before", ".java");
			fileBefore.deleteOnExit();
			fileAfter = File.createTempFile("after", ".java");
			fileAfter.deleteOnExit();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {			
			if(!fileDetail.getStatus().equals("added")) {
				before = new URL(beforeUrl).openStream();
				after = new URL(afterUrl).openStream();
				
				FileOutputStream out = new FileOutputStream(fileBefore);
			    IOUtils.copy(before, out);
			    
			    FileOutputStream out2 = new FileOutputStream(fileAfter);
			    IOUtils.copy(after, out2);
			} else {
				after = new URL(afterUrl).openStream();
				
				FileOutputStream out = new FileOutputStream(fileBefore);
			    IOUtils.copy(new ByteArrayInputStream(" ".getBytes(StandardCharsets.UTF_8)), out);
			    
			    FileOutputStream out2 = new FileOutputStream(fileAfter);
			    IOUtils.copy(after, out2);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		AstComparator diff = new AstComparator();
		Diff differences = null;
		try {
			differences = diff.compare(fileBefore, fileAfter);
			fileBefore.delete();
			fileAfter.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return differences.getRootOperations();
	}
	
	public int GetNumberOfNewMethodInFile(int fileIndex){		
		int count = 0;		
		List<Operation> actions = getActions(fileIndex);		
		
		for(Operation a : actions) {
			if(a.getNode() instanceof CtMethodImpl && a instanceof InsertOperation) count++;
			if(a.getNode() instanceof CtClassImpl && a instanceof InsertOperation) {
				Set<CtMethodImpl> c = ((CtClassImpl)a.getNode()).getAllMethods();
				count += c.size();
			}
		}
		
		return count;
	}
	
	public String GetNewMethodPrototype(int fileIndex, int methodIndex){
		int count = 0;	
		List<Operation> actions = getActions(fileIndex);		
		
		for(Operation a : actions) {
			
			if(a.getNode() instanceof CtMethodImpl && a instanceof InsertOperation) {
				if(count == methodIndex) {
					CtMethodImpl method = (CtMethodImpl)a.getNode();
					return method.getSignature();
				}
				count++;
			}
			
			if(a.getNode() instanceof CtClassImpl && a instanceof InsertOperation) {
				Set<CtMethodImpl> c = ((CtClassImpl)a.getNode()).getAllMethods();
				
				for (CtMethodImpl method : c) {
					if(count == methodIndex) {
						return method.getSignature();
					}
					count++;
				}
			}
		}
		
		return "Error";
	}
	
	public int GetNumberOfDeletedMethodInFile(int fileIndex){
		int count = 0;		
		List<Operation> actions = getActions(fileIndex);		
		
		for(Operation a : actions) {
			if(a.getNode() instanceof CtMethodImpl && a instanceof DeleteOperation) count++;
			if(a.getNode() instanceof CtClassImpl && a instanceof DeleteOperation) {
				Set c = ((CtClassImpl)a.getNode()).getAllMethods();
				count += c.size();
			}
		}
		
		return count;
	}
	
	public String GetDeletedMethodPrototype(int fileIndex, int methodIndex){
		int count = 0;	
		List<Operation> actions = getActions(fileIndex);		
		
		for(Operation a : actions) {
			CtElement test = a.getNode();
			if(a.getNode() instanceof CtMethodImpl && a instanceof DeleteOperation) {
				if(count == methodIndex) {
					CtMethodImpl method = (CtMethodImpl)a.getNode();
					return method.getSimpleName();
				}
				count++;
			}
			if(a.getNode() instanceof CtClassImpl && a instanceof DeleteOperation) {
				Set<CtMethodImpl> c = ((CtClassImpl)a.getNode()).getAllMethods();
				
				for (CtMethodImpl method : c) {
					if(count == methodIndex) {
						return method.getSignature();
					}
					count++;
				}
			}
		}
		
		return "Error";
	}
	
	public int GetNumberOfNewTestInFile(int fileIndex){
		int count = 0;		
		List<Operation> actions = getActions(fileIndex);		
		
		for(Operation a : actions) {

			if(a.getNode() instanceof CtMethodImpl && a instanceof InsertOperation) {
				List<CtAnnotation<? extends Annotation>> annotations = a.getNode().getAnnotations();
				
				for(CtAnnotation<? extends Annotation> annotation : annotations) {
					if(annotation.getSignature().contains("Test") || annotation.getSignature().contains("test")) count++;
				}
			}
			
			if(a.getNode() instanceof CtClassImpl && a instanceof InsertOperation) {
				Set<CtMethodImpl> methods = ((CtClassImpl)a.getNode()).getAllMethods();
				for (CtMethodImpl method : methods) {
					List<CtAnnotation<? extends Annotation>> annotations = method.getAnnotations();

					for(CtAnnotation<? extends Annotation> annotation : annotations) {
						if(annotation.getSignature().contains("Test") || annotation.getSignature().contains("test")) count++;
					}
				}
			}
		}
		
		return count;
	}
	
	public String GetNewTestPrototype(int fileIndex, int methodIndex){
		int count = 0;		
		List<Operation> actions = getActions(fileIndex);		
		
		for(Operation a : actions) {
			if(a.getNode() instanceof CtMethodImpl && a instanceof InsertOperation) {
				List<CtAnnotation<? extends Annotation>> annotations = a.getNode().getAnnotations();
				
				for(CtAnnotation<? extends Annotation> annotation : annotations) {
					if(annotation.getSignature().contains("Test") || annotation.getSignature().contains("test")) {
						if(count == methodIndex) {
							CtMethodImpl method = (CtMethodImpl)a.getNode();
							return method.getSimpleName();
						}
						count++;
					}
				}
			}
			
			if(a.getNode() instanceof CtClassImpl && a instanceof InsertOperation) {
				Set<CtMethodImpl> methods = ((CtClassImpl)a.getNode()).getAllMethods();
				for (CtMethodImpl method : methods) {
					List<CtAnnotation<? extends Annotation>> annotations = method.getAnnotations();

					for(CtAnnotation<? extends Annotation> annotation : annotations) {
						if(annotation.getSignature().contains("Test") || annotation.getSignature().contains("test")) {
							if(count == methodIndex) {
								CtMethodImpl m = (CtMethodImpl)a.getNode();
								return method.getSimpleName();
							}
							count++;
						}
					}
				}
			}
		}
		
		return "Error";
	}
	
	public int GetNumberOfDeletedTestInFile(int fileIndex){
		int count = 0;		
		List<Operation> actions = getActions(fileIndex);		
		
		for(Operation a : actions) {
			if(a.getNode() instanceof CtMethodImpl && a instanceof DeleteOperation) {
				List<CtAnnotation<? extends Annotation>> annotations = a.getNode().getAnnotations();
				
				for(CtAnnotation<? extends Annotation> annotation : annotations) {
					if(annotation.getSignature().contains("Test") || annotation.getSignature().contains("test")) count++;
				}
			}
			
			if(a.getNode() instanceof CtClassImpl && a instanceof DeleteOperation) {
				Set<CtMethodImpl> methods = ((CtClassImpl)a.getNode()).getAllMethods();
				for (CtMethodImpl method : methods) {
					List<CtAnnotation<? extends Annotation>> annotations = method.getAnnotations();

					for(CtAnnotation<? extends Annotation> annotation : annotations) {
						if(annotation.getSignature().contains("Test") || annotation.getSignature().contains("test")) count++;
					}
				}
			}
		}
		
		return count;
	}
	
	public String GetDeletedTestPrototype(int fileIndex, int methodIndex){
		int count = 0;		
		List<Operation> actions = getActions(fileIndex);		
		
		for(Operation a : actions) {
			if(a.getNode() instanceof CtMethodImpl && a instanceof DeleteOperation) {
				List<CtAnnotation<? extends Annotation>> annotations = a.getNode().getAnnotations();
				
				for(CtAnnotation<? extends Annotation> annotation : annotations) {
					if(annotation.getSignature().contains("Test") || annotation.getSignature().contains("test")) {
						if(count == methodIndex) {
							CtMethodImpl method = (CtMethodImpl)a.getNode();
							return method.getSimpleName();
						}
						count++;
					}
				}
			}
			
			if(a.getNode() instanceof CtClassImpl && a instanceof DeleteOperation) {
				Set<CtMethodImpl> methods = ((CtClassImpl)a.getNode()).getAllMethods();
				for (CtMethodImpl method : methods) {
					List<CtAnnotation<? extends Annotation>> annotations = method.getAnnotations();

					for(CtAnnotation<? extends Annotation> annotation : annotations) {
						if(annotation.getSignature().contains("Test") || annotation.getSignature().contains("test")) {
							if(count == methodIndex) {
								CtMethodImpl m = (CtMethodImpl)a.getNode();
								return method.getSimpleName();
							}
							count++;
						}
					}
				}
			}
			
		}
		
		return "Error";
	}
	
	public int GetNumberOfNewCommentsInFile(int fileIndex){
		int count = 0;		
		List<Operation> actions = getActions(fileIndex);		
		
		for(Operation a : actions) {
			if(a instanceof InsertOperation) {
				if(a.getNode() != null) {					
					if(a.getNode().getComments().size() > 0) 
						count++;
				}
			}
		}
		
		return count;
	}
	
	public int GetNumberOfDeletedCommentsInFile(int fileIndex){
		int count = 0;		
		List<Operation> actions = getActions(fileIndex);		
		
		for(Operation a : actions) {
			if(a instanceof DeleteOperation) {
				if(a.getNode() != null)
					if(a.getNode().getComments().size() > 0) 
						count++;	
			}
		}
		
		return count;
	}
	
	public int GetNumberOfModifiedMethodsInFile(int fileIndex) {
		List<Operation> actions = getActions(fileIndex);
		List<String> modifiedMethods = new ArrayList<String>();
		
		for(Operation a : actions) {
			CtElement parent = a.getNode();
			
			while(!(parent instanceof CtMethodImpl)) {
				if(parent == null) break;
				parent = parent.getParent();				
			}
			
			if(parent == null) continue;	
			
			if(parent instanceof CtMethodImpl) {
				if(modifiedMethods.indexOf(((CtMethodImpl)parent).getSignature()) == -1) {
					modifiedMethods.add(((CtMethodImpl)parent).getSignature());
				}
			}
		}
		
		return modifiedMethods.size();
	}
	
	public String GetModifiedMethodPrototype(int fileIndex, int methodIndex){
		List<Operation> actions = getActions(fileIndex);
		List<String> modifiedMethods = new ArrayList<String>();
		
		for(Operation a : actions) {
 			CtElement parent = a.getNode();
			
			while(!(parent instanceof CtMethodImpl)) {
					if(parent == null) break;
					parent = parent.getParent();
			}
			
			if(parent == null) continue;			
			
			if(parent instanceof CtMethodImpl) {
				if(modifiedMethods.indexOf(((CtMethodImpl)parent).getSignature()) == -1) {
					modifiedMethods.add(((CtMethodImpl)parent).getSignature());
					if(modifiedMethods.size()-1 == methodIndex) return modifiedMethods.get(modifiedMethods.size()-1);
				}
			}
		}
		
		return "Error";
	}
	
}
