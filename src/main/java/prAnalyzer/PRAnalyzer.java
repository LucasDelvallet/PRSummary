package prAnalyzer;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestFileDetail;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

public class PRAnalyzer {
	

	private GHRepository repo;
	private List<GHPullRequest> pullRequests;
	
	public PRAnalyzer(String repoName){
		try {
			GitHub github = GitHub.connect();
		
			repo = github.getRepository(repoName);
			
			pullRequests = repo.getPullRequests(GHIssueState.OPEN);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int GetNumberOfPullRequests(){
		return pullRequests.size();
	}
	
	public int GetNumberOfFiles(int pullRequestIndex){
		return pullRequests.get(pullRequestIndex).listFiles().asList().size();
	}
	
	public String GetFileName(int pullRequestIndex, int fileIndex){
		return pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex).getFilename();
	}
	
	public int GetNumberOfNewMethodInFile(int pullRequestIndex, int fileIndex){
		Matcher m;
		
		GHPullRequestFileDetail fileDetail = pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex);
			
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
		
		GHPullRequestFileDetail fileDetail = pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex);
		
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
		
		GHPullRequestFileDetail fileDetail = pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex);
		
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
		
		GHPullRequestFileDetail fileDetail = pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex);
		
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
		
		GHPullRequestFileDetail fileDetail = pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex);
			
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
		
		GHPullRequestFileDetail fileDetail = pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex);
		
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
		
		GHPullRequestFileDetail fileDetail = pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex);
		
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
		
		GHPullRequestFileDetail fileDetail = pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex);
		
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
		
		GHPullRequestFileDetail fileDetail = pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex);
		
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
		
		GHPullRequestFileDetail fileDetail = pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex);
		
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
		
		GHPullRequestFileDetail fileDetail = pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex);
			
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
		
		GHPullRequestFileDetail fileDetail = pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex);
		
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
	
	/*
	public void temp(){
		
		Matcher m;
		
		for(GHPullRequest PR : pullRequests){
			System.out.println("Titre : " + PR.getTitle());				

			System.out.println("	Liste des fichiers d'extension .java modifiés/ajoutés/supprimés : ");
			for(GHPullRequestFileDetail filesDetail : PR.listFiles()){
				String fileName = filesDetail.getFilename();
				if(fileName.endsWith(".java")){
					System.out.println("		Nom du fichier : " + fileName);
					
					m = methodAdded.matcher(filesDetail.getPatch()); 					//On donne le pattern et le string
					System.out.println("			Méthodes ajoutées/modifiées : "); 	//On indique ce qu'on va afficher
					while(m.find()) { 													//On trouve la prochaine chaine qui satisfait le pattern
						String prototype = m.group().replaceAll("(\\+ *)|( *\\{)", ""); //On retire les caractères inutiles qui ne font pas parti du prototype de la méthode
						System.out.println("				" + prototype); 			//On affiche la méthode.
					}
					
					//On recommence pour les méthodes supprimées
					m = methodDeleted.matcher(filesDetail.getPatch());
					System.out.println("			Méthodes supprimées : ");
					while(m.find()) {
						String prototype = m.group().replaceAll("(\\- *)|( *\\{)", "");
						System.out.println("				" + prototype);
					}
					
					//On compte le nombre de commentaires ajoutés et on le montre
					m = commentAdded.matcher(filesDetail.getPatch());
					int count = 0;
					while (m.find())
					    count++;
					System.out.println("			Nombre de commentaires ajoutés : " + count);
				}
			}
		}
	}
	*/
	
	
}
