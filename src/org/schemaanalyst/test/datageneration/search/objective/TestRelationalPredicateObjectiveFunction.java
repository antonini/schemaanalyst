package org.schemaanalyst.test.datageneration.search.objective;

import org.junit.Test;

import org.schemaanalyst.data.Data;
import org.schemaanalyst.data.NumericValue;
import org.schemaanalyst.datageneration.search.objective.MultiObjectiveValue;
import org.schemaanalyst.datageneration.search.objective.ObjectiveValue;
import org.schemaanalyst.datageneration.search.objective.SumOfMultiObjectiveValue;
import org.schemaanalyst.datageneration.search.objective.constraint.RelationalCheckPredicateObjectiveFunction;
import org.schemaanalyst.datageneration.search.objective.predicate.ValueObjectiveFunction;
import org.schemaanalyst.logic.RelationalOperator;
import org.schemaanalyst.schema.RelationalCheckPredicate;
import org.schemaanalyst.test.mock.OneColumnMockDatabase;

import static org.schemaanalyst.test.junit.ObjectiveValueAssert.assertEquivalent;

public class TestRelationalPredicateObjectiveFunction {

	OneColumnMockDatabase database;
	Data data;
	
	boolean satisfy;
	RelationalOperator op;
	int value;
	
	RelationalCheckPredicateObjectiveFunction objFun;
	
	public TestRelationalPredicateObjectiveFunction() {
		database = new OneColumnMockDatabase();
		data = database.createData(2);					
	}
	
	void setupRelational(boolean satisfy, String operator, int value) {
		this.satisfy = satisfy;
		this.op = RelationalOperator.operator(operator);
		this.value = value;
		
		RelationalCheckPredicate relationalPredicate = new RelationalCheckPredicate(database.column, operator, value); 
		objFun = new RelationalCheckPredicateObjectiveFunction(relationalPredicate, database.table, null, "", satisfy, false);
	}
	
	void test(Integer... values) {
		database.setDataValues(values);
		MultiObjectiveValue expected = new SumOfMultiObjectiveValue("Top level");

		if (!satisfy) {
			op = op.inverse();
		}
		for (Integer compareValue : values) {
			NumericValue numericalValue = null;
			if (compareValue != null) {
				numericalValue = new NumericValue(compareValue);
			}
			
			expected.add(ValueObjectiveFunction.compute(numericalValue, op, new NumericValue(value)));
    	}	
		
		ObjectiveValue actual = objFun.evaluate(data);
		assertEquivalent(expected, actual);
	}
	
	@Test 
	public void testSatisfy_AllTrue() {
		setupRelational(true, ">", 0);
		test(10, 10);
	}
	
	@Test 
	public void testSatisfy_NoneTrue() {
		setupRelational(true, ">", 0);
		test(-10, -10);
	}	
	
	@Test 
	public void testSatisfy_OneTrue() {
		setupRelational(true, ">", 0);
		test(10, -10);
	}		
	
	@Test 
	public void testSatisfy_OneNull() {
		setupRelational(true, ">", 0);
		test(null, 10);	
	}		
	
	@Test 
	public void testSatisfy_TwoNulls() {
		setupRelational(true, ">", 0);
		test(null, null);
	}		

	@Test 
	public void testNegate_AllTrue() {
		setupRelational(false, ">", 0);
		test(10, 10);
	}		
	
	@Test 
	public void testNegate_NoneTrue() {
		setupRelational(false, ">", 0);
		test(-10, -10);
	}	
	
	@Test 
	public void testNegate_OneTrue() {
		setupRelational(false, ">", 0);
		test(10, -10);
	}		
	
	@Test 
	public void testNegate_OneNull() {
		setupRelational(false, ">", 0);
		test(null, -10);
	}		
	
	@Test 
	public void testNegate_TwoNulls() {
		setupRelational(false, ">", 0);
		test(null, null);	
	}	
}