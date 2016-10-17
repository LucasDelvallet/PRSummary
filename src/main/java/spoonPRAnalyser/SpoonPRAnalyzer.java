package spoonPRAnalyser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.kohsuke.github.GHCompare;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestFileDetail;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import gumtree.spoon.AstComparator;
import gumtree.spoon.diff.Diff;
import gumtree.spoon.diff.operations.*;
import spoon.reflect.declaration.*;
import spoon.support.reflect.declaration.*;

public class SpoonPRAnalyzer {

	private GHRepository repo;
	private List<GHPullRequest> pullRequests;
	private HashMap<GHPullRequest, List<GHPullRequestFileDetail>> filesDetails;
	private int pullRequestIndex = 0;

	private List<Operation> actions;

	public SpoonPRAnalyzer(String repoName) {
		try {
			GitHub github = GitHub.connect();

			repo = github.getRepository(repoName);

			pullRequests = repo.getPullRequests(GHIssueState.ALL);
			filesDetails = new HashMap<GHPullRequest, List<GHPullRequestFileDetail>>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void StartBot() {
		while (true) {
			try {
				System.out.println("Fetching PRs.");
				pullRequests = repo.getPullRequests(GHIssueState.OPEN);
				int cpt = 0;
				for (GHPullRequest PR : pullRequests) {
					pullRequestIndex = cpt;
					if (new Date().getTime() - PR.getCreatedAt().getTime() <= 10000) {
						System.out.println("	New PR detected : adding bot comment");
						String msg = "------This is an automatic message------\r\n\r\n";

						int nbFiles = GetNumberOfJavaFiles();
						int nbNewMethod = 0;
						int nbDeletedMethod = 0;
						int nbModifiedMethod = 0;
						int nbNewTest = 0;
						int nbDeletedTest = 0;

						int i = 0;
						for (GHPullRequestFileDetail a : GetFiles(pullRequests.get(pullRequestIndex))) {
							if (GetFiles(pullRequests.get(pullRequestIndex)) == null)
								continue;
							if (a.getFilename().endsWith(".java")) {
								getActions(i);
								nbNewMethod += GetNumberOfNewMethodInFile();
								nbDeletedMethod += GetNumberOfDeletedMethodInFile();
								nbModifiedMethod += GetNumberOfModifiedMethodsInFile();
								nbNewTest += GetNumberOfNewTestInFile();
								nbDeletedTest += GetNumberOfDeletedTestInFile();
							}
						}

						msg += "    Number of java files modified/deleted/added : " + nbFiles + "\r\n";
						msg += "    Number of method added : " + nbNewMethod + "\r\n";
						msg += "    Number of method deleted : " + nbDeletedMethod + "\r\n";
						msg += "    Number of method modified : " + nbModifiedMethod + "\r\n";
						msg += "    Number of test added : " + nbNewTest + "\r\n";
						msg += "    Number of test deleted : " + nbDeletedTest + "\r\n";

						PR.comment(msg);
					} else {
						System.out.println("	Old PR detected. Nothing to do.");
					}
					cpt++;
				}

				System.out.println("Waiting 10 secs.");
				Thread.sleep(10000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void StartAnalysisOfRepo() {
		long tStart = System.currentTimeMillis();

		int cpt = 0;
		int nbFilesTotal = 0;
		int nbNewMethodTotal = 0;
		int nbDeletedMethodTotal = 0;
		int nbModifiedMethodTotal = 0;
		int nbNewTestTotal = 0;
		int nbDeletedTestTotal = 0;

		for (GHPullRequest PR : pullRequests) {
			pullRequestIndex = cpt;

			System.out.println("==== PR n° : " + (cpt + 1) + "/" + pullRequests.size());

			int nbFiles = GetNumberOfJavaFiles();
			int nbNewMethod = 0;
			int nbDeletedMethod = 0;
			int nbModifiedMethod = 0;
			int nbNewTest = 0;
			int nbDeletedTest = 0;

			int i = 0;
			for (GHPullRequestFileDetail a : GetFiles(pullRequests.get(pullRequestIndex))) {
				if (GetFiles(pullRequests.get(pullRequestIndex)) == null)
					continue;
				if (a.getFilename().endsWith(".java")) {
					getActions(i);
					nbNewMethod += GetNumberOfNewMethodInFile();
					nbDeletedMethod += GetNumberOfDeletedMethodInFile();
					nbModifiedMethod += GetNumberOfModifiedMethodsInFile();
					nbNewTest += GetNumberOfNewTestInFile();
					nbDeletedTest += GetNumberOfDeletedTestInFile();
				}
			}

			System.out.println("    Number of java files modified/deleted/added : " + nbFiles);
			System.out.println("    Number of method added : " + nbNewMethod);
			System.out.println("    Number of method deleted : " + nbDeletedMethod);
			System.out.println("    Number of method modified : " + nbModifiedMethod);
			System.out.println("    Number of test added : " + nbNewTest);
			System.out.println("    Number of test deleted : " + nbDeletedTest);

			nbFilesTotal += nbFiles;
			nbNewMethodTotal += nbNewMethod;
			nbDeletedMethodTotal += nbDeletedMethod;
			nbModifiedMethodTotal += nbModifiedMethod;
			nbNewTestTotal += nbNewTest;
			nbDeletedTestTotal += nbDeletedTest;

			System.out.println("---- Total");
			System.out.println("    Total number of java files modified/deleted/added : " + nbFilesTotal
					+ "  Average : " + (float) nbFilesTotal / ((float) cpt + 1));
			System.out.println("    Total number of method added : " + nbNewMethodTotal + "  Average : "
					+ (float) nbNewMethodTotal / ((float) cpt + 1));
			System.out.println("    Total number of method deleted : " + nbDeletedMethodTotal + "  Average : "
					+ (float) nbDeletedMethodTotal / ((float) cpt + 1));
			System.out.println("    Total number of method modified : " + nbModifiedMethodTotal + "  Average : "
					+ (float) nbModifiedMethodTotal / ((float) cpt + 1));
			System.out.println("    Total number of test added : " + nbNewTestTotal + "  Average : "
					+ (float) nbNewTestTotal / ((float) cpt + 1));
			System.out.println("    Total number of test deleted : " + nbDeletedTestTotal + "  Average : "
					+ (float) nbDeletedTestTotal / ((float) cpt + 1));
			System.out.println("");
			cpt++;
		}

		long tEnd = System.currentTimeMillis();
		long tDelta = tEnd - tStart;
		double elapsedSeconds = tDelta / 1000.0;

		if (elapsedSeconds > 60)
			System.out.println("Minutes elapsed : " + (elapsedSeconds / 60f));
		else
			System.out.println("Seconds elapsed : " + elapsedSeconds);
	}

	private List<GHPullRequestFileDetail> GetFiles(GHPullRequest PR) {
		if (!filesDetails.containsKey(PR)) {
			filesDetails.put(PR, PR.listFiles().asList());
		}
		return filesDetails.get(PR);
	}

	public void ClosePR() {
		try {
			if (pullRequests.get(pullRequestIndex).getState() != GHIssueState.CLOSED) {
				pullRequests.get(pullRequestIndex).close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String GetSourceBranchName() {
		return pullRequests.get(pullRequestIndex).getHead().getLabel().split(":")[1];
	}

	public String GetTargetBranchName() {
		return pullRequests.get(pullRequestIndex).getBase().getLabel().split(":")[1];
	}

	public int GetNumberOfCommit() {
		return pullRequests.get(pullRequestIndex).listCommits().asList().size();
	}

	public int GetNumberOfPullRequests() {
		return pullRequests.size();
	}

	public int GetNumberOfJavaFiles() {
		int cpt = 0;
		for (GHPullRequestFileDetail a : GetFiles(pullRequests.get(pullRequestIndex))) {
			if (a.getFilename().endsWith(".java")) {
				cpt++;
			}
		}
		return cpt;
	}

	public String GetFileName(int fileIndex) {
		int cpt = 0;
		for (GHPullRequestFileDetail a : GetFiles(pullRequests.get(pullRequestIndex))) {
			if (a.getFilename().endsWith(".java")) {
				if (cpt == fileIndex) {
					return a.getFilename();
				}

				cpt++;
			}
		}
		return "Error";
	}

	public void getActions(int fileIndex) {
		GHPullRequestFileDetail fileDetail = null;
		int cpt = 0;
		for (GHPullRequestFileDetail a : GetFiles(pullRequests.get(pullRequestIndex))) {
			if (a.getFilename().endsWith(".java")) {
				if (cpt == fileIndex) {
					fileDetail = a;
				}

				cpt++;
			}
		}

		String mergeSHA = "";
		GHCompare comp;

		try {
			comp = repo.getCompare(pullRequests.get(pullRequestIndex).getBase().getSha(),
					pullRequests.get(pullRequestIndex).getHead().getSha());
			mergeSHA = comp.getMergeBaseCommit().getSHA1();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		String beforeUrl = "https://raw.githubusercontent.com/" + repo.getFullName() + "/" + mergeSHA + "/"
				+ fileDetail.getFilename();
		String afterUrl = fileDetail.getRawUrl().toString();

		InputStream before, after;
		File fileBefore = null, fileAfter = null;
		try {
			fileBefore = File.createTempFile("before", ".java");
			fileBefore.deleteOnExit();
			fileAfter = File.createTempFile("after", ".java");
			fileAfter.deleteOnExit();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			if (!fileDetail.getStatus().equals("added")) {
				before = new URL(beforeUrl).openStream();
				after = new URL(afterUrl).openStream();

				FileOutputStream out = new FileOutputStream(fileBefore);
				IOUtils.copy(before, out);

				FileOutputStream out2 = new FileOutputStream(fileAfter);
				IOUtils.copy(after, out2);
			} else {
				after = new URL(afterUrl).openStream();

				FileOutputStream out = new FileOutputStream(fileBefore);
				IOUtils.copy(new ByteArrayInputStream(" ".getBytes(StandardCharsets.UTF_8)), out);

				FileOutputStream out2 = new FileOutputStream(fileAfter);
				IOUtils.copy(after, out2);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		AstComparator diff = new AstComparator();
		Diff differences = null;
		try {
			differences = diff.compare(fileBefore, fileAfter);
			fileBefore.delete();
			fileAfter.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}

		actions = differences.getRootOperations();
	}

	public int GetNumberOfNewMethodInFile() {
		int count = 0;

		for (Operation a : actions) {
			if (a.getNode() instanceof CtMethodImpl && a instanceof InsertOperation)
				count++;
			if (a.getNode() instanceof CtClassImpl && a instanceof InsertOperation) {
				Set<CtMethodImpl> c = ((CtClassImpl) a.getNode()).getAllMethods();
				count += c.size();
			}
		}

		return count;
	}

	public String GetNewMethodPrototype(int methodIndex) {
		int count = 0;

		for (Operation a : actions) {

			if (a.getNode() instanceof CtMethodImpl && a instanceof InsertOperation) {
				if (count == methodIndex) {
					CtMethodImpl method = (CtMethodImpl) a.getNode();
					return method.getSignature();
				}
				count++;
			}

			if (a.getNode() instanceof CtClassImpl && a instanceof InsertOperation) {
				Set<CtMethodImpl> c = ((CtClassImpl) a.getNode()).getAllMethods();

				for (CtMethodImpl method : c) {
					if (count == methodIndex) {
						return method.getSignature();
					}
					count++;
				}
			}
		}

		return "Error";
	}

	public int GetNumberOfDeletedMethodInFile() {
		int count = 0;

		for (Operation a : actions) {
			if (a.getNode() instanceof CtMethodImpl && a instanceof DeleteOperation)
				count++;
			if (a.getNode() instanceof CtClassImpl && a instanceof DeleteOperation) {
				Set c = ((CtClassImpl) a.getNode()).getAllMethods();
				count += c.size();
			}
		}

		return count;
	}

	public String GetDeletedMethodPrototype(int methodIndex) {
		int count = 0;

		for (Operation a : actions) {
			CtElement test = a.getNode();
			if (a.getNode() instanceof CtMethodImpl && a instanceof DeleteOperation) {
				if (count == methodIndex) {
					CtMethodImpl method = (CtMethodImpl) a.getNode();
					return method.getSimpleName();
				}
				count++;
			}
			if (a.getNode() instanceof CtClassImpl && a instanceof DeleteOperation) {
				Set<CtMethodImpl> c = ((CtClassImpl) a.getNode()).getAllMethods();

				for (CtMethodImpl method : c) {
					if (count == methodIndex) {
						return method.getSignature();
					}
					count++;
				}
			}
		}

		return "Error";
	}

	public int GetNumberOfNewTestInFile() {
		int count = 0;

		for (Operation a : actions) {

			if (a.getNode() instanceof CtMethodImpl && a instanceof InsertOperation) {
				List<CtAnnotation<? extends Annotation>> annotations = a.getNode().getAnnotations();

				for (CtAnnotation<? extends Annotation> annotation : annotations) {
					if (annotation.getSignature().contains("Test") || annotation.getSignature().contains("test"))
						count++;
				}
			}

			if (a.getNode() instanceof CtClassImpl && a instanceof InsertOperation) {
				Set<CtMethodImpl> methods = ((CtClassImpl) a.getNode()).getAllMethods();
				for (CtMethodImpl method : methods) {
					List<CtAnnotation<? extends Annotation>> annotations = method.getAnnotations();

					for (CtAnnotation<? extends Annotation> annotation : annotations) {
						if (annotation.getSignature().contains("Test") || annotation.getSignature().contains("test"))
							count++;
					}
				}
			}
		}

		return count;
	}

	public String GetNewTestPrototype(int methodIndex) {
		int count = 0;

		for (Operation a : actions) {
			if (a.getNode() instanceof CtMethodImpl && a instanceof InsertOperation) {
				List<CtAnnotation<? extends Annotation>> annotations = a.getNode().getAnnotations();

				for (CtAnnotation<? extends Annotation> annotation : annotations) {
					if (annotation.getSignature().contains("Test") || annotation.getSignature().contains("test")) {
						if (count == methodIndex) {
							CtMethodImpl method = (CtMethodImpl) a.getNode();
							return method.getSimpleName();
						}
						count++;
					}
				}
			}

			if (a.getNode() instanceof CtClassImpl && a instanceof InsertOperation) {
				Set<CtMethodImpl> methods = ((CtClassImpl) a.getNode()).getAllMethods();
				for (CtMethodImpl method : methods) {
					List<CtAnnotation<? extends Annotation>> annotations = method.getAnnotations();

					for (CtAnnotation<? extends Annotation> annotation : annotations) {
						if (annotation.getSignature().contains("Test") || annotation.getSignature().contains("test")) {
							if (count == methodIndex) {
								CtMethodImpl m = (CtMethodImpl) a.getNode();
								return method.getSimpleName();
							}
							count++;
						}
					}
				}
			}
		}

		return "Error";
	}

	public int GetNumberOfDeletedTestInFile() {
		int count = 0;

		for (Operation a : actions) {
			if (a.getNode() instanceof CtMethodImpl && a instanceof DeleteOperation) {
				List<CtAnnotation<? extends Annotation>> annotations = a.getNode().getAnnotations();

				for (CtAnnotation<? extends Annotation> annotation : annotations) {
					if (annotation.getSignature().contains("Test") || annotation.getSignature().contains("test"))
						count++;
				}
			}

			if (a.getNode() instanceof CtClassImpl && a instanceof DeleteOperation) {
				Set<CtMethodImpl> methods = ((CtClassImpl) a.getNode()).getAllMethods();
				for (CtMethodImpl method : methods) {
					List<CtAnnotation<? extends Annotation>> annotations = method.getAnnotations();

					for (CtAnnotation<? extends Annotation> annotation : annotations) {
						if (annotation.getSignature().contains("Test") || annotation.getSignature().contains("test"))
							count++;
					}
				}
			}
		}

		return count;
	}

	public String GetDeletedTestPrototype(int methodIndex) {
		int count = 0;

		for (Operation a : actions) {
			if (a.getNode() instanceof CtMethodImpl && a instanceof DeleteOperation) {
				List<CtAnnotation<? extends Annotation>> annotations = a.getNode().getAnnotations();

				for (CtAnnotation<? extends Annotation> annotation : annotations) {
					if (annotation.getSignature().contains("Test") || annotation.getSignature().contains("test")) {
						if (count == methodIndex) {
							CtMethodImpl method = (CtMethodImpl) a.getNode();
							return method.getSimpleName();
						}
						count++;
					}
				}
			}

			if (a.getNode() instanceof CtClassImpl && a instanceof DeleteOperation) {
				Set<CtMethodImpl> methods = ((CtClassImpl) a.getNode()).getAllMethods();
				for (CtMethodImpl method : methods) {
					List<CtAnnotation<? extends Annotation>> annotations = method.getAnnotations();

					for (CtAnnotation<? extends Annotation> annotation : annotations) {
						if (annotation.getSignature().contains("Test") || annotation.getSignature().contains("test")) {
							if (count == methodIndex) {
								CtMethodImpl m = (CtMethodImpl) a.getNode();
								return method.getSimpleName();
							}
							count++;
						}
					}
				}
			}

		}

		return "Error";
	}

	public int GetNumberOfNewCommentsInFile() {
		int count = 0;

		for (Operation a : actions) {
			if (a instanceof InsertOperation) {
				if (a.getNode() != null) {
					if (a.getNode().getComments().size() > 0)
						count++;
				}
			}
		}

		return count;
	}

	public int GetNumberOfDeletedCommentsInFile() {
		int count = 0;

		for (Operation a : actions) {
			if (a instanceof DeleteOperation) {
				if (a.getNode() != null)
					if (a.getNode().getComments().size() > 0)
						count++;
			}
		}

		return count;
	}

	public int GetNumberOfModifiedMethodsInFile() {
		List<String> modifiedMethods = new ArrayList<String>();

		for (Operation a : actions) {
			if (!(a instanceof UpdateOperation))
				continue;

			CtElement parent = a.getNode();

			while (!(parent instanceof CtMethodImpl)) {
				if (parent == null)
					break;
				parent = parent.getParent();
			}

			if (parent == null)
				continue;

			if (parent instanceof CtMethodImpl) {
				if (modifiedMethods.indexOf(((CtMethodImpl) parent).getSignature()) == -1) {
					modifiedMethods.add(((CtMethodImpl) parent).getSignature());
				}
			}
		}

		return modifiedMethods.size();
	}

	public String GetModifiedMethodPrototype(int methodIndex) {
		List<String> modifiedMethods = new ArrayList<String>();

		for (Operation a : actions) {
			if (!(a instanceof UpdateOperation))
				continue;

			CtElement parent = a.getNode();

			while (!(parent instanceof CtMethodImpl)) {
				if (parent == null)
					break;
				parent = parent.getParent();
			}

			if (parent == null)
				continue;

			if (parent instanceof CtMethodImpl) {
				if (modifiedMethods.indexOf(((CtMethodImpl) parent).getSignature()) == -1) {
					modifiedMethods.add(((CtMethodImpl) parent).getSignature());
					if (modifiedMethods.size() - 1 == methodIndex)
						return modifiedMethods.get(modifiedMethods.size() - 1);
				}
			}
		}

		return "Error";
	}

}
