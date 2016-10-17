package main;

import spoonPRAnalyser.SpoonPRAnalyzer;

public class Main {

	public static void main(String[] args) {

		if (args[1].equals("Analysis")) {
			SpoonPRAnalyzer analyzer = new SpoonPRAnalyzer(args[0]);
			analyzer.StartAnalysisOfRepo();
			System.exit(0);
		}

		if (args[1].equals("Bot")) {
			SpoonPRAnalyzer analyzer = new SpoonPRAnalyzer(args[0]);
			analyzer.StartBot();
			System.exit(0);
		}

		System.exit(1);
	}
}