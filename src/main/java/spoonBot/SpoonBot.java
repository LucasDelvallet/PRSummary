package spoonBot;

import spoonPRAnalyser.SpoonPRAnalyzer;

public abstract class SpoonBot{

	private String repoName = "";
	
	public SpoonBot(String repoName){
		this.repoName = repoName;
	}
	
	public void Start(){
		SpoonPRAnalyzer analyzer = new SpoonPRAnalyzer(repoName);
		analyzer.Start(this);
	}

	public abstract String BuildMessage(SpoonPRAnalyzer analyzer);
	
}
