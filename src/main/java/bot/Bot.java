package bot;

import prAnalyzer.PRAnalyzer;

public abstract class Bot{

	private String repoName = "";
	
	public Bot(String repoName){
		this.repoName = repoName;
	}
	
	public void Start(){
		PRAnalyzer analyzer = new PRAnalyzer(repoName);
		analyzer.Start(this);
	}

	public abstract String BuildMessage(PRAnalyzer analyzer);
	
}
