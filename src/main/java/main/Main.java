package main;

import java.io.IOException;

import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

public class Main {

	public static void main(String[] args) {
		System.out.println("Hello World !");
		
		try {
			GitHub github = GitHub.connect();
			
			GHRepository repo = github.getRepository("structurizr/java");
			
			for(GHPullRequest PR : repo.getPullRequests(GHIssueState.OPEN)){
				System.out.println("Titre : " + PR.getTitle());
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
