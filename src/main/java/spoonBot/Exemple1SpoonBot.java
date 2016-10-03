package spoonBot;

import spoonPRAnalyser.SpoonPRAnalyzer;

public class Exemple1SpoonBot extends SpoonBot {

	public Exemple1SpoonBot(String repoName) {
		super(repoName);
	}

	@Override
	public String BuildMessage(SpoonPRAnalyzer analyzer) {
		String msg = "";
		
		int nbNewMethod = 0;
		for(int i = 0; i < analyzer.GetNumberOfJavaFiles(); i++){
			analyzer.getActions(i);
			nbNewMethod += analyzer.GetNumberOfNewMethodInFile();
		}
		
		int nbCommit = analyzer.GetNumberOfCommit();
		if(nbCommit > 1){
			msg += "You have pushed more than one commit. When you finish editing, squash your commits into one.\r\n\r\n";
		}
		
		msg += "Number of new method, except test : " + (nbNewMethod) + "\r\n";

		if(!(analyzer.GetSourceBranchName().startsWith("fix/") || analyzer.GetSourceBranchName().startsWith("feature/") || analyzer.GetSourceBranchName().startsWith("translate/"))){
			msg += "Your branch name should start with one of fix/, feature/, translate/ prefixes. Name, your branches correctly next time, please.\r\n\r\n";
		}
		
		if(!analyzer.GetTargetBranchName().equals("staging")){
			msg += "Your PR should always target the 'staging' branch. Which is not the case. Closing this branch.\r\n\r\n";
			analyzer.ClosePR();
		}
		

		
		return msg;
	}

}
