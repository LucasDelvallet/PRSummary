package main;

import prAnalyzer.PRAnalyzer;

public class Main {

	public static void main(String[] args) {
		PRAnalyzer analyzer = new PRAnalyzer("structurizr/java");
		
		int indexPR = 0;
		int indexFile = 34;
		int indexNewMethod = 0;
		
		System.out.println("Nombre de pull request 					: " + analyzer.getNumberOfPullRequests());
		System.out.println("Nombre de fichier de la PR d'index 0			: " + analyzer.getNumberOfFiles(indexPR));
		System.out.println("Nom du fichier d'index 1 				: " + analyzer.getFileName(indexPR, indexFile));
		System.out.println("Nombre de nouvelle méthode				: " + analyzer.getNumberOfNewMethodInFile(indexPR, indexFile));
		System.out.println("Nom de la nouvelle méthode d'index 0			: " + analyzer.getNewMethodPrototype(indexPR, indexFile, indexNewMethod));
		System.out.println("Nombre de méthode supprimée				: " + analyzer.getNumberOfDeletedMethodInFile(indexPR, indexFile));
		System.out.println("Nombre de commentaire ajouté				: " + analyzer.getNumberOfNewComments(indexPR, indexFile));
		System.out.println("Nombre de commentaire supprimé				: " + analyzer.getNumberOfDeletedComments(indexPR, indexFile));

		System.out.println("Nombre de nouveaux test					: " + analyzer.getNumberOfNewTestMethodInFile(indexPR, indexFile));
		System.out.println("Nom du nouveau test d'index 0				: " + analyzer.getNewTestMethodPrototype(indexPR, indexFile, indexNewMethod));
		System.out.println("Nombre de test supprimé					: " + analyzer.getNumberOfDeletedTestMethodInFile(indexPR, indexFile));

	}
}