package main;

import prAnalyzer.PRAnalyzer;

public class Main {

	public static void main(String[] args) {
		PRAnalyzer analyzer = new PRAnalyzer("structurizr/java");
		
		int indexPR = 0;
		int indexFile = 6; //34 pour un fichier avec des nouveaux tests; 4 pour tester les mÈthodes abstraites pour les mÈthodes modifiÈes vu que Áa marche pas actuellement
		int indexNewMethod = 0;
		
		System.out.println("Nombre de pull request 					: " + analyzer.getNumberOfPullRequests());
		System.out.println("Nombre de fichier de la PR d'index 0			: " + analyzer.getNumberOfFiles(indexPR));
		System.out.println("Nom du fichier d'index 1 				: " + analyzer.getFileName(indexPR, indexFile));
		System.out.println("Nombre de nouvelle m√©thode				: " + analyzer.getNumberOfNewMethodInFile(indexPR, indexFile));
		System.out.println("Nom de la nouvelle m√©thode d'index 0			: " + analyzer.getNewMethodPrototype(indexPR, indexFile, indexNewMethod));
		System.out.println("Nombre de m√©thode supprim√©e				: " + analyzer.getNumberOfDeletedMethodInFile(indexPR, indexFile));
		System.out.println("Nombre de commentaire ajout√©				: " + analyzer.getNumberOfNewComments(indexPR, indexFile));
		System.out.println("Nombre de commentaire supprim√©				: " + analyzer.getNumberOfDeletedComments(indexPR, indexFile));

		System.out.println("Nombre de nouveaux test					: " + analyzer.getNumberOfNewTestMethodInFile(indexPR, indexFile));
		System.out.println("Nom du nouveau test d'index 0				: " + analyzer.getNewTestMethodPrototype(indexPR, indexFile, indexNewMethod));
		System.out.println("Nombre de test supprim√©				: " + analyzer.getNumberOfDeletedTestMethodInFile(indexPR, indexFile));
		
		System.out.println("Nombre de m√©thodes modifiÈes				: " + analyzer.getNumberOfModifiedMethods(indexPR, indexFile));
		System.out.println("Nom de la m√©thode modifiÈe d'index 0			: " + analyzer.getModifiedMethodPrototype(indexPR, indexFile, 0));
	}
}