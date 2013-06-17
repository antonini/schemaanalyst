package org.schemaanalyst.representation.datatype;

public class DateTimeDataType extends DataType {

	private static final long serialVersionUID = -4863979851522497316L;

	public void accept(DataTypeVisitor typeVisitor) {
		typeVisitor.visit(this);
	}
}