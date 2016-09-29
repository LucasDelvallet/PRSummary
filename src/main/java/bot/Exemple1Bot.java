package bot;

import prAnalyzer.PRAnalyzer;

public class Exemple1Bot extends Bot {

	public Exemple1Bot(String repoName) {
		super(repoName);
	}

	@Override
	public String BuildMessage(PRAnalyzer analyzer) {
		String msg = "";
		
		int nbNewMethod = 0;
		int nbNewTestMethod = 0;
		for(int i = 0; i < analyzer.GetNumberOfFiles(); i++){
			nbNewMethod += analyzer.GetNumberOfNewMethodInFile(i);
			nbNewTestMethod += analyzer.GetNumberOfNewTestInFile(i);
		}
		
		int nbCommit = analyzer.GetNumberOfCommit();
		if(nbCommit > 1){
			msg += "You have pushed more than one commit. When you finish editing, squash your commits into one.\r\n\r\n";
		}
		
		msg += "Number of new method, except test : " + (nbNewMethod - nbNewTestMethod) + "\r\n";
		msg += "Number of new test : " + nbNewTestMethod + "\r\n";
		
		if((nbNewMethod - nbNewTestMethod) > nbNewTestMethod){
			msg += "There's less new tests than new method. It may be a problem." + "\r\n\r\n";
		}
		
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
