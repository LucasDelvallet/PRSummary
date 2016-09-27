package prAnalyzer;

import java.util.regex.Pattern;

public class Regex {
	
	static private Pattern methodAdded = Pattern.compile("\\+[ \\t]*[\\w<>\\[\\] ]+ [\\w<>_]+ *\\([\\w_, <>\\[\\]]*\\) *[\\w]* [\\w]* *\\{");
	static private Pattern methodDeleted = Pattern.compile("\\-[ ]*[\\w<>]* [\\w<>]* [\\w<>\\[\\]]+ [\\w<>_]+ *\\([\\w_, <>\\[\\]]*\\) *[\\w]* [\\w]* *\\{");
	static private Pattern commentAdded = Pattern.compile("\\+[ ]*(/\\*([^*]|[\r\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/)|\\+[ ]*(//.*)");
	static private Pattern commentDeleted = Pattern.compile("\\-[ ]*(/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/)|\\-[ ]*(//.*)");
	static private Pattern testAdded = Pattern.compile("\\+ *@Test *[\\r\\n]*\\+ *[\\w<>]* [\\w<>]* [\\w<>\\[\\]]+ [\\w<>_]+ *\\([\\w_, <>\\[\\]]*\\) *[\\w]* [\\w]* *\\{");
	static private Pattern testDeleted = Pattern.compile("\\- *@Test *[\\r\\n]*\\- *[\\w<>]* [\\w<>]* [\\w<>\\[\\]]+ [\\w<>_]+ *\\([\\w_, <>\\[\\]]*\\) *[\\w]* [\\w]* *\\{");
	static private Pattern methodModified = Pattern.compile("[\\r\\n]+[^\\+\\-] *((public|private|protected|static|final|native|synchronized|abstract|transient)+\\s)+[\\$_\\w\\<\\>\\[\\]]*\\s+[\\$_\\w]+\\([^\\)]*\\)?\\s*\\{?[^\\}]*\\}?");

	static Pattern GetMethodAddedPattern(){
		return methodAdded;
	}
	
	static Pattern GetMethodDeletedPattern(){
		return methodDeleted;
	}
	
	static Pattern GetCommentAddedPattern(){
		return commentAdded;
	}
	
	static Pattern GetCommentDeletedPattern(){
		return commentDeleted;
	}
	
	static Pattern GetTestAddedPattern(){
		return testAdded;
	}
	
	static Pattern GetTestDeletedPattern(){
		return testDeleted;
	}

	static Pattern GetMethodModifiedPattern(){
		return methodModified;
	}
	
}
