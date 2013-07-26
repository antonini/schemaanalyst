package org.schemaanalyst.datageneration.search.objective.data;

import java.util.List;

import org.schemaanalyst.data.Data;
import org.schemaanalyst.data.Row;
import org.schemaanalyst.datageneration.search.objective.MultiObjectiveValue;
import org.schemaanalyst.datageneration.search.objective.ObjectiveFunction;
import org.schemaanalyst.datageneration.search.objective.ObjectiveValue;
import org.schemaanalyst.datageneration.search.objective.SumOfMultiObjectiveValue;
import org.schemaanalyst.datageneration.search.objective.row.ExpressionObjectiveFunctionFactory;
import org.schemaanalyst.sqlrepresentation.Table;
import org.schemaanalyst.sqlrepresentation.expression.Expression;

public class ExpressionObjectiveFunction extends ObjectiveFunction<Data> {

    protected Expression expression;
    protected Table table;
    protected String description;
    protected boolean goalIsToSatisfy, allowNull;

    public ExpressionObjectiveFunction(Expression expression,
            Table table,
            String description,
            boolean goalIsToSatisfy,
            boolean allowNull) {
        this.expression = expression;
        this.table = table;
        this.description = description;
        this.goalIsToSatisfy = goalIsToSatisfy;
        this.allowNull = allowNull;
    }

    @Override
    public ObjectiveValue evaluate(Data data) {

        MultiObjectiveValue objVal = new SumOfMultiObjectiveValue(description);
        ExpressionObjectiveFunctionFactory factory =
                new ExpressionObjectiveFunctionFactory(expression,
                goalIsToSatisfy,
                allowNull);
        ObjectiveFunction<Row> objFun = factory.create();
        
        List<Row> rows = data.getRows(table);
        for (Row row : rows) {
            objVal.add(objFun.evaluate(row));
        }

        return objVal;
    }
}