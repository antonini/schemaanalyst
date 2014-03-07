package org.schemaanalyst.sqlparser;

import gudusoft.gsqlparser.nodes.*;

/**
 * <p>
 * An SQLParseException caused by an unsupported SQL feature encountered when 
 * mapping the General SQL Parser representation to the SchemaAnalyst 
 * representation.
 * </p>
 */
@SuppressWarnings("serial")
public class UnsupportedSQLException extends SQLParseException {

    public UnsupportedSQLException(String message) {
        super(message);
    }

    public UnsupportedSQLException(String message, TParseTreeNode node) {
        super(message + " at \"" + node + "\", input line " + node.getLineNo() + ", column " + node.getColumnNo());
    }

    public UnsupportedSQLException(TConstraint constraint) {
        this("Unsupported SQL constraint [GSP EConstraintType: " + constraint.getConstraint_type() + "]", constraint);
    }

    public UnsupportedSQLException(TExpression expression) {
        this("Unsupported SQL expression [GSP EExpressionType: " + expression.getExpressionType() + "]", expression);
    }

    public UnsupportedSQLException(TTypeName dataType, TParseTreeNode node) {
        this("Unsupported SQL datatype [GSP EDataType: " + dataType.getDataType() + "]", node);
    }

    public UnsupportedSQLException(TAlterTableOption alterTableOption) {
        this("Unsupported SQL alter table option [GSP EAlterTableOptionType: " + alterTableOption.getOptionType() + "]", alterTableOption);
    }
}
