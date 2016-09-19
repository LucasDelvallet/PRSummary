package main;

import java.io.IOException;

import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestFileDetail;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;

public class Main {

	public static void main(String[] args) {
		try {
			GitHub github = GitHub.connect();
			
			GHRepository repo = github.getRepository("structurizr/java");
			
			for(GHPullRequest PR : repo.getPullRequests(GHIssueState.OPEN)){
				System.out.println("Titre : " + PR.getTitle());
				

				System.out.println("	Liste des fichiers d'extension .java modifié/ajouté/supprimé : ");
				for(GHPullRequestFileDetail filesDetail : PR.listFiles()){
					String fileName = filesDetail.getFilename();
					if(fileName.endsWith(".java")){
						System.out.println("		Nom du fichier : " + fileName);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
