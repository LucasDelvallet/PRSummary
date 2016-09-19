package main;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestFileDetail;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;

public class Main {

	public static void main(String[] args) {
		try {
			Pattern methodAdded = Pattern.compile("\\+[ ]*[a-zA-Z]* [a-zA-Z]* [a-zA-Z]+ [a-zA-Z]+ *\\([a-zA-Z, ]*\\) *\\{");
			Pattern methodDeleted = Pattern.compile("\\-[ ]*[a-zA-Z]* [a-zA-Z]* [a-zA-Z]+ [a-zA-Z]+ *\\([a-zA-Z, ]*\\) *\\{");
			Pattern commentAdded = Pattern.compile("\\+[ ]*(/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/)|\\+[ ]*(//.*)");
			Matcher m;
			
			GitHub github = GitHub.connect();
			
			GHRepository repo = github.getRepository("structurizr/java");
			
			for(GHPullRequest PR : repo.getPullRequests(GHIssueState.OPEN)){
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
						m = methodAdded.matcher(filesDetail.getPatch());
						int count = 0;
						while (m.find())
						    count++;
						System.out.println("			Nombre de commentaires ajoutés : " + count);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}

//	Regex pour chopper différents trucs (pour les foutre dans des string il faut doubler les \\:
//
//	Les méthodes ajoutées ou supprimées :
//		\+[ ]*[a-zA-Z]* [a-zA-Z]* [a-zA-Z]+ [a-zA-Z]+ *\([a-zA-Z, ]*\) *\{
//		\-[ ]*[a-zA-Z]* [a-zA-Z]* [a-zA-Z]+ [a-zA-Z]+ *\([a-zA-Z, ]*\) *\{
//	
//	Détecter les commentaires ajoutés ou supprimés :
//		\+[ ]*(/\*([^*]|[\r\n]|(\*+([^*/]|[\r\n])))*\*+/)|\+[ ]*(//.*)
//		\-[ ]*(/\*([^*]|[\r\n]|(\*+([^*/]|[\r\n])))*\*+/)|\-[ ]*(//.*)
//