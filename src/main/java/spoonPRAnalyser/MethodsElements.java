package spoonPRAnalyser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import gumtree.spoon.diff.operations.DeleteOperation;
import gumtree.spoon.diff.operations.InsertOperation;
import gumtree.spoon.diff.operations.Operation;
import gumtree.spoon.diff.operations.UpdateOperation;
import spoon.reflect.declaration.CtElement;
import spoon.support.reflect.declaration.CtClassImpl;
import spoon.support.reflect.declaration.CtMethodImpl;

public final class MethodsElements {
	private MethodsElements() {}
	
	public static int GetNumberOfNewMethodInFile(List<Operation> actions) {
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
	
	public static String GetNewMethodPrototype(List<Operation> actions, int methodIndex) {
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

	public static int GetNumberOfDeletedMethodInFile(List<Operation> actions) {
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

	public static String GetDeletedMethodPrototype(List<Operation> actions, int methodIndex) {
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

	public static int GetNumberOfModifiedMethodsInFile(List<Operation> actions) {
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

	public static String GetModifiedMethodPrototype(List<Operation> actions, int methodIndex) {
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