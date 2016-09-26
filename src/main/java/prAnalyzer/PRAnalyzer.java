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

	static private Pattern methodAdded = Pattern.compile("\\+[ ]*[a-zA-Z<>]* [a-zA-Z<>]* [a-zA-Z<>]+ [a-zA-Z<>_]+ *\\([a-zA-Z_, <>]*\\) *[a-zA-Z]* [a-zA-Z]* *\\{");
	static private Pattern methodDeleted = Pattern.compile("\\-[ ]*[a-zA-Z<>]* [a-zA-Z<>]* [a-zA-Z<>]+ [a-zA-Z<>_]+ *\\([a-zA-Z_, <>]*\\) *[a-zA-Z]* [a-zA-Z]* *\\{");
	static private Pattern commentAdded = Pattern.compile("\\+[ ]*(/\\*([^*]|[\r\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/)|\\+[ ]*(//.*)");
	static private Pattern commentDeleted = Pattern.compile("\\-[ ]*(/\\*([^*]|[\r\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/)|\\-[ ]*(//.*)");
	static private Pattern testMethodAdded = Pattern.compile("\\+ *@Test *[\r\n]*\\+ *[a-zA-Z<>]* [a-zA-Z<>]* [a-zA-Z<>]+ [a-zA-Z<>_]+ *\\([a-zA-Z_, <>]*\\) *[a-zA-Z]* [a-zA-Z]* *\\{");
	static private Pattern testMethodDeleted = Pattern.compile("\\- *@Test *[\r\n]*\\- *[a-zA-Z<>]* [a-zA-Z<>]* [a-zA-Z<>]+ [a-zA-Z<>_]+ *\\([a-zA-Z_, <>]*\\) *[a-zA-Z]* [a-zA-Z]* *\\{");
	
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
	
	public int getNumberOfPullRequests(){
		return pullRequests.size();
	}
	
	public int getNumberOfFiles(int pullRequestIndex){
		return pullRequests.get(pullRequestIndex).listFiles().asList().size();
	}
	
	public String getFileName(int pullRequestIndex, int fileIndex){
		return pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex).getFilename();
	}
	
	public int getNumberOfNewMethodInFile(int pullRequestIndex, int fileIndex){
		Matcher m;
		
		GHPullRequestFileDetail fileDetail = pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex);
			
		int count = 0;
		if(fileDetail.getFilename().endsWith(".java")){
			m = methodAdded.matcher(fileDetail.getPatch()); 
			while(m.find()) { 		
				count++;
			}
		}
		
		return count;
	}
	
	public String getNewMethodPrototype(int pullRequestIndex, int fileIndex, int methodIndex){
		Matcher m;
		
		GHPullRequestFileDetail fileDetail = pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex);
		
		int count = 0;
		if(fileDetail.getFilename().endsWith(".java")){
			m = methodAdded.matcher(fileDetail.getPatch()); 					
			
			while(m.find()) { 		
				if(count == methodIndex){
					return m.group().replaceAll("(\\+ *)|( *\\{)", "");
				}
				count++;
			}
		}
		
		return "Error";
	}
	
	public int getNumberOfDeletedMethodInFile(int pullRequestIndex, int fileIndex){
		Matcher m;
		
		GHPullRequestFileDetail fileDetail = pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex);
		
		int count = 0;
		if(fileDetail.getFilename().endsWith(".java")){
			m = methodDeleted.matcher(fileDetail.getPatch()); 	
			while(m.find()) { 		
				count++;
			}
		}
		
		return count;
	}
	
	public String getDeletedMethodPrototype(int pullRequestIndex, int fileIndex, int methodIndex){
		Matcher m;
		
		GHPullRequestFileDetail fileDetail = pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex);
		
		if(fileDetail.getFilename().endsWith(".java")){
			m = methodDeleted.matcher(fileDetail.getPatch()); 					
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
	
	public int getNumberOfNewTestMethodInFile(int pullRequestIndex, int fileIndex){
		Matcher m;
		
		GHPullRequestFileDetail fileDetail = pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex);
			
		int count = 0;
		if(fileDetail.getFilename().endsWith(".java")){
			m = testMethodAdded.matcher(fileDetail.getPatch()); 
			while(m.find()) { 		
				count++;
			}
		}
		
		return count;
	}
	
	public String getNewTestMethodPrototype(int pullRequestIndex, int fileIndex, int methodIndex){
		Matcher m;
		
		GHPullRequestFileDetail fileDetail = pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex);
		
		int count = 0;
		if(fileDetail.getFilename().endsWith(".java")){
			m = testMethodAdded.matcher(fileDetail.getPatch()); 					
			
			while(m.find()) { 		
				if(count == methodIndex){
					return m.group().replaceAll("(\\+ *)|( *\\{)|(@Test)|(\r)|(\n)", "");
				}
				count++;
			}
		}
		
		return "Error";
	}
	
	public int getNumberOfDeletedTestMethodInFile(int pullRequestIndex, int fileIndex){
		Matcher m;
		
		GHPullRequestFileDetail fileDetail = pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex);
		
		int count = 0;
		if(fileDetail.getFilename().endsWith(".java")){
			m = testMethodDeleted.matcher(fileDetail.getPatch()); 	
			while(m.find()) { 		
				count++;
			}
		}
		
		return count;
	}
	
	public String getDeletedTestMethodPrototype(int pullRequestIndex, int fileIndex, int methodIndex){
		Matcher m;
		
		GHPullRequestFileDetail fileDetail = pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex);
		
		if(fileDetail.getFilename().endsWith(".java")){
			m = testMethodDeleted.matcher(fileDetail.getPatch()); 					
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
	
	public int getNumberOfNewComments(int pullRequestIndex, int fileIndex){
		Matcher m;
		
		GHPullRequestFileDetail fileDetail = pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex);
		
		int count = 0;
		if(fileDetail.getFilename().endsWith(".java")){
			m = commentAdded.matcher(fileDetail.getPatch()); 	
			while(m.find()) { 		
				count++;
			}
		}
		
		return count;
	}
	
	public int getNumberOfDeletedComments(int pullRequestIndex, int fileIndex){
		Matcher m;
		
		GHPullRequestFileDetail fileDetail = pullRequests.get(pullRequestIndex).listFiles().asList().get(fileIndex);
		
		int count = 0;
		if(fileDetail.getFilename().endsWith(".java")){
			m = commentDeleted.matcher(fileDetail.getPatch()); 	
			while(m.find()) { 		
				count++;
			}
		}
		
		return count;
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
