package org.eclipse.swt.graphics;

import java.util.*;

/**
 * @noreference This class is not intended to be referenced by clients.
 */
public final class RegionLog {

	public enum OpType {
		ADD, INTERSECT, SUBTRACT, TRANSLATE
	}

	public record Operation(OpType type, Object executionObject) {
	}

	private final List<Operation> op = new LinkedList<>();


	public void translate(Point p) {
		op.add(new Operation(OpType.TRANSLATE, p));
	}

	public void intersect(Object ob) {
		op.add(new Operation(OpType.INTERSECT, ob));
	}

	public void add(Object ob) {
		op.add(new Operation(OpType.ADD, ob));
	}

	public void subtract(Object ob) {
		op.add(new Operation(OpType.SUBTRACT, ob));
	}

	public List<Operation> getOperations(){
		return op;
	}

	public int getNumberOfOperations() {
		return op.size();
	}

	public static RegionLog getLog(Region r) {
		return r.getLog();
	}


}
