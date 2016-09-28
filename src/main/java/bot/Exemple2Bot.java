package bot;

import prAnalyzer.PRAnalyzer;

public class Exemple2Bot extends Bot {
	
	public Exemple2Bot(String repoName) {
		super(repoName);
	}

	@Override
	public String BuildMessage(PRAnalyzer analyzer) {
		String msg = "";
		int numberOfFileToDisplay = 5;
		int numberOfFileDisplayed = 0;
		int numberOfFileNotDisplayed = 0;
		msg += "Java file(s) modified or added :\r\n";
		

		for(int i = 0; i < analyzer.GetNumberOfFiles(); i++){
			String fileName = analyzer.GetFileName(i);
			if(fileName.endsWith(".java")){
				if(numberOfFileDisplayed < numberOfFileToDisplay){
					msg += "    " + fileName + "\r\n";
					numberOfFileDisplayed++;
				}else{
					numberOfFileNotDisplayed++;
				}
			}
		}

		if(numberOfFileToDisplay < numberOfFileNotDisplayed){
			msg += "And " + (numberOfFileNotDisplayed-numberOfFileToDisplay) + " other file.\r\n";
		}
		
		return msg;
	}
}
