package org.schemaanalyst.data;

import java.io.Serializable;

import org.schemaanalyst.schema.Operand;
import org.schemaanalyst.schema.OperandVisitor;
import org.schemaanalyst.util.Duplicable;

public abstract class Value implements Operand, Serializable, Duplicable<Value>, Comparable<Value> {
	
	private static final long serialVersionUID = -5756271284942346822L;
	
	public void increment() {
	}
	
	public void decrement() {
	}
	
	public void accept(OperandVisitor visitor) {
		visitor.visit(this);
	}

	public abstract void accept(ValueVisitor visitor);
	
	public abstract Value duplicate();
	
	public abstract int compareTo(Value v);
	
	public static Integer compareTo3VL(Value v1, Value v2) {
		return null;
	}
	
	public static Boolean equals3VL(Value v1, Value v2) {
		if (v1 == null || v2 == null) {
			return null;
		} else {
			return v1.equals(v2);
		}		
	}
}