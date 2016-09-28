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
		
		msg += "Number of new method, except test : " + (nbNewMethod - nbNewTestMethod) + "\r\n";
		msg += "Number of new test : " + nbNewTestMethod + "\r\n";
		if((nbNewMethod - nbNewTestMethod) > nbNewTestMethod){
			msg += "There's less new tests than new method. It may be a problem." + "\r\n";
		}
		
		return msg;
	}

}
