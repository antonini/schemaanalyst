package org.schemaanalyst.javawriter;

import java.util.ArrayList;
import java.util.List;

import org.schemaanalyst.sqlrepresentation.datatype.DataType;
import org.schemaanalyst.sqlrepresentation.datatype.DataTypeCategoryVisitor;
import org.schemaanalyst.sqlrepresentation.datatype.LengthLimited;
import org.schemaanalyst.sqlrepresentation.datatype.PrecisionedAndScaled;
import org.schemaanalyst.sqlrepresentation.datatype.Signed;

public class DataTypeJavaWriter {

	protected JavaWriter codeWriter;
	
	public DataTypeJavaWriter(JavaWriter codeWriter) {
		this.codeWriter = codeWriter;
	}
	
	public String writeConstruction(DataType dataType) {
		
		class WriterDataTypeCategoryVisitor implements DataTypeCategoryVisitor {

			List<String> params;
			
			List<String> getParams(DataType type) {
				params = new ArrayList<>();
				type.accept(this);
				return params;
			}
			
			public void visit(DataType type) {
				// no params for standard types -- do nothing
			}

			public void visit(LengthLimited type) {
				Integer length = type.getLength();
				if (length != null) {
					params.add(type.getLength().toString());
				}
			}

			public void visit(PrecisionedAndScaled type) {
				Integer precision = type.getPrecision(); 
				if (precision != null) {
					params.add(precision.toString());
					Integer scale = type.getScale();
					if (scale != null) {
						params.add(scale.toString());
					}
				}
			}

			public void visit(Signed type) {
				boolean isSigned = type.isSigned();
				if (!isSigned) {
					params.add("false");
				}
			}
		}
		
		List<String> params = new WriterDataTypeCategoryVisitor().getParams(dataType); 
		return codeWriter.writeConstruction(dataType, params);
	}	
}