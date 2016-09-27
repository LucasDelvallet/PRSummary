package main;

import prAnalyzer.PRAnalyzer;

public class Main {

	public static void main(String[] args) {
		PRAnalyzer analyzer = new PRAnalyzer("structurizr/java");
		
		int indexPR = 0;
		int indexFile = 6; //34 pour un fichier avec des nouveaux tests; 4 pour tester les m�thodes abstraites pour les m�thodes modifi�es vu que �a marche pas actuellement
		int indexNewMethod = 0;
		
		System.out.println("Nombre de pull request 					: " + analyzer.GetNumberOfPullRequests());
		System.out.println("Nombre de fichier de la PR d'index 0			: " + analyzer.GetNumberOfFiles(indexPR));
		System.out.println("Nom du fichier d'index 1 				: " + analyzer.GetFileName(indexPR, indexFile));
		System.out.println("Nombre de nouvelle méthode				: " + analyzer.GetNumberOfNewMethodInFile(indexPR, indexFile));
		System.out.println("Nom de la nouvelle méthode d'index 0			: " + analyzer.GetNewMethodPrototype(indexPR, indexFile, indexNewMethod));
		System.out.println("Nombre de méthode supprimée				: " + analyzer.GetNumberOfDeletedMethodInFile(indexPR, indexFile));
		System.out.println("Nombre de commentaire ajouté				: " + analyzer.GetNumberOfNewCommentsInFile(indexPR, indexFile));
		System.out.println("Nombre de commentaire supprimé				: " + analyzer.GetNumberOfDeletedCommentsInFile(indexPR, indexFile));

		System.out.println("Nombre de nouveaux test					: " + analyzer.GetNumberOfNewTestInFile(indexPR, indexFile));
		System.out.println("Nom du nouveau test d'index 0				: " + analyzer.GetNewTestPrototype(indexPR, indexFile, indexNewMethod));
		System.out.println("Nombre de test supprimé					: " + analyzer.GetNumberOfDeletedTestInFile(indexPR, indexFile));
		
		System.out.println("Nombre de méthodes modifiées				: " + analyzer.GetNumberOfModifiedMethodsInFile(indexPR, indexFile));
		System.out.println("Nom de la méthode modifiée d'index 0			: " + analyzer.GetModifiedMethodPrototype(indexPR, indexFile, 0));
	
	
		
		
		System.out.println("============ EXEMPLE CONCRET ============");
		System.out.println("===> Exemple 1 : ");
		
		int nbNewMethode = 0;
		int nbNewTestMethode = 0;
		for(int i = 0; i < analyzer.GetNumberOfFiles(indexPR); i++){
			System.out.print(".");
			nbNewMethode += analyzer.GetNumberOfNewMethodInFile(indexPR, i);
			nbNewTestMethode += analyzer.GetNumberOfNewTestInFile(indexPR, i);
		}
		System.out.println("");
		
		System.out.println("Nombre de nouvelles méthodes, hors test, ajoutée : " + (nbNewMethode - nbNewTestMethode));
		System.out.println("Nombre de nouvelles méthodes de test ajoutée : " + nbNewTestMethode);
		if((nbNewMethode - nbNewTestMethode) > nbNewTestMethode){
			System.out.println("Il n'y a a priori pas assez de tests pour les nouvelles méthodes.");
		}
		
		
		
		
		
	
	}
}