package org.schemaanalyst.representation.datatype;

public class TimeDataType extends DataType {

	private static final long serialVersionUID = 2067286896633355675L;

	public void accept(DataTypeVisitor typeVisitor) {
		typeVisitor.visit(this);
	}

}
