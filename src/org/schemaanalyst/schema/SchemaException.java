package org.schemaanalyst.schema;

/**
 * An Exception class thrown when a schema has been carelessly constructed (e.g. number of foreign key columns in the 
 * original and reference table do not match).
 * @author Phil McMinn
 * 
 */
public class SchemaException extends RuntimeException {
	
	private static final long serialVersionUID = -1938527440146240474L;

	/**
	 * Constructs the exception.
	 * @param message A message explaining the problem.
	 */
	public SchemaException(String message) {
		super(message);
	}
}