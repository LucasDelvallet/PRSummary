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

public final class TestsElements {
	private TestsElements() {}
	
	public static int GetNumberOfNewTestInFile(List<Operation> actions) {
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

	public static String GetNewTestPrototype(List<Operation> actions, int methodIndex) {
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

	public static int GetNumberOfDeletedTestInFile(List<Operation> actions) {
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

	public static String GetDeletedTestPrototype(List<Operation> actions, int methodIndex) {
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

}
