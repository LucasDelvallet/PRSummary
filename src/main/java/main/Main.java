package main;

import java.io.File;

import com.github.gumtreediff.tree.ITree;

import bot.Exemple1Bot;
import gumtree.spoon.AstComparator;
import gumtree.spoon.diff.DiffImpl;
import spoonBot.Exemple1SpoonBot;
import spoonPRAnalyser.SpoonPRAnalyzer;

public class Main {

	public static void main(String[] args) {
		
		
		//Exemple1Bot botty = new Exemple1Bot("LucasDelvallet/PRSummary");
		//botty.Start();
		
		//Exemple1SpoonBot botbotbibot = new Exemple1SpoonBot("LucasDelvallet/PRSummary");
		//botbotbibot.Start();
		

		long tStart = System.currentTimeMillis();
		
		SpoonPRAnalyzer analyzer = new SpoonPRAnalyzer("INRIA/Spoon");
		//SpoonPRAnalyzer analyzer = new SpoonPRAnalyzer("LucasDelvallet/PRSummary");
		analyzer.StartAnalysisOfRepo();		
		
		long tEnd = System.currentTimeMillis();
		long tDelta = tEnd - tStart;
		double elapsedSeconds = tDelta / 1000.0;
		
		if(elapsedSeconds > 60 )
			System.out.println("Minutes �coul�es : " + (elapsedSeconds/60f));
		else 
			System.out.println("Secondes �coul�es : " + elapsedSeconds);
		
		//SpoonPRAnalyzer analyzer = new SpoonPRAnalyzer("INRIA/spoon");
		
		//int fileIndex = 0, indexMethod = 0;
		
		//System.out.println("Nombre de pull request 					: " + analyzer.GetNumberOfPullRequests());
		//System.out.println("Nom du fichier d'index 1 				: " + analyzer.GetFileName(fileIndex));
		//System.out.println("Nombre de nouvelle méthode				: " + analyzer.GetNumberOfNewMethodInFile(fileIndex));
		//System.out.println("Nom de la nouvelle méthode d'index 0			: " + analyzer.GetNewMethodPrototype(fileIndex, indexMethod));
		//System.out.println("Nombre de méthode supprimée				: " + analyzer.GetNumberOfDeletedMethodInFile(fileIndex));
		//System.out.println("Nombre de commentaire ajouté				: " + analyzer.GetNumberOfNewCommentsInFile(fileIndex));
		//System.out.println("Nombre de commentaire supprimé				: " + analyzer.GetNumberOfDeletedCommentsInFile(fileIndex));
		
		//System.out.println("Nombre de nouveaux test					: " + analyzer.GetNumberOfNewTestInFile(fileIndex));
		//System.out.println("Nom du nouveau test d'index 0				: " + analyzer.GetNewTestPrototype(fileIndex, indexMethod));
		//System.out.println("Nombre de test supprimé					: " + analyzer.GetNumberOfDeletedTestInFile(fileIndex));
		
		//System.out.println("Nombre de méthodes modifiées				: " + analyzer.GetNumberOfModifiedMethodsInFile(fileIndex));
		//System.out.println("Nom de la méthode modifiée d'index 0			: " + analyzer.GetModifiedMethodPrototype(fileIndex, 0));
		//System.out.println("Nom de la méthode modifiée d'index 1			: " + analyzer.GetModifiedMethodPrototype(fileIndex, 1));	
		
		
		/*PRAnalyzer analyzer = new PRAnalyzer("LucasDelvallet/PRSummary");
		
		int indexPR = 0;
		int indexFile = 6; //34 pour un fichier avec des nouveaux tests; 4 pour tester les méthodes abstraites pour les méthodes modifiées vu que ça marche pas actuellement
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
	
	
		
		
		System.out.println("\r\n============ EXEMPLE CONCRET ============");
		System.out.println("\r\n===> Exemple 1 : ");
		int numberOfFileToDisplay = 5;
		int numberOfFileDisplayed = 0;
		int numberOfFileNotDisplayed = 0;
		System.out.println("Fichier(s) java modifié(s) :");
		

		for(int i = 0; i < analyzer.GetNumberOfFiles(indexPR); i++){
			String fileName = analyzer.GetFileName(indexPR, i);
			if(fileName.endsWith(".java")){
				if(numberOfFileDisplayed < numberOfFileToDisplay){
					System.out.println("    " + fileName);
					numberOfFileDisplayed++;
				}else{
					numberOfFileNotDisplayed++;
				}
			}
		}

		if(numberOfFileToDisplay < numberOfFileNotDisplayed){
			System.out.println("Et " + (numberOfFileNotDisplayed-numberOfFileToDisplay) + " autre(s) fichier(s)." );
		}
		
		
		System.out.println("\r\n===> Exemple 2 : ");
		
		int nbNewMethod = 0;
		int nbNewTestMethod = 0;
		for(int i = 0; i < analyzer.GetNumberOfFiles(indexPR); i++){
			nbNewMethod += analyzer.GetNumberOfNewMethodInFile(indexPR, i);
			nbNewTestMethod += analyzer.GetNumberOfNewTestInFile(indexPR, i);
		}
		
		System.out.println("Nombre de nouvelles méthodes, hors test, ajoutée : " + (nbNewMethod - nbNewTestMethod));
		System.out.println("Nombre de test ajouté : " + nbNewTestMethod);
		if((nbNewMethod - nbNewTestMethod) > nbNewTestMethod){
			System.out.println("A priori il n'y a pas assez de tests pour les nouvelles méthodes.");
		}*/
	}
}