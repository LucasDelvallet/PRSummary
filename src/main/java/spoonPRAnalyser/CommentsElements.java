package spoonPRAnalyser;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import gumtree.spoon.diff.operations.DeleteOperation;
import gumtree.spoon.diff.operations.InsertOperation;
import gumtree.spoon.diff.operations.Operation;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtElement;
import spoon.support.reflect.declaration.CtClassImpl;
import spoon.support.reflect.declaration.CtMethodImpl;

public final class CommentsElements {
	private CommentsElements() {}
	
	public static int GetNumberOfNewCommentsInFile(List<Operation> actions) {
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

	public static int GetNumberOfDeletedCommentsInFile(List<Operation> actions) {
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

}
