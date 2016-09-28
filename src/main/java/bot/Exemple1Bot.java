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
		
		msg += "Nombre de nouvelles méthodes, hors test, ajoutée : " + (nbNewMethod - nbNewTestMethod) + "\r\n";
		msg += "Nombre de test ajouté : " + nbNewTestMethod + "\r\n";
		if((nbNewMethod - nbNewTestMethod) > nbNewTestMethod){
			msg += "A priori il n'y a pas assez de tests pour les nouvelles méthodes." + "\r\n";
		}
		
		return msg;
	}

}
