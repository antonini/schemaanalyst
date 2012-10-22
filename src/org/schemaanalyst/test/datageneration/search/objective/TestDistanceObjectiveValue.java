package org.schemaanalyst.test.datageneration.search.objective;

import java.math.BigDecimal;

import org.junit.Test;
import static org.junit.Assert.assertNull;

import org.schemaanalyst.datageneration.search.objective.DistanceObjectiveValue;
import org.schemaanalyst.datageneration.search.objective.ObjectiveValue;

import static org.schemaanalyst.test.junit.BigDecimalAssert.assertEquals;

public class TestDistanceObjectiveValue {

	@Test
	public void testSetValueUsingDistance() {
		
		ObjectiveValue objVal = new ObjectiveValue("vanilla objective value");
		objVal.normalizeAndSetValue(BigDecimal.TEN);  
		
		DistanceObjectiveValue distObjVal = new DistanceObjectiveValue("distance objective value");
		distObjVal.setValueUsingDistance(10);
		
		assertEquals("Objective values should be the same", distObjVal.getValue(), objVal.getValue());
		assertEquals("Distance should be 10", distObjVal.getDistance(), BigDecimal.TEN);
		
		distObjVal.setValueUsingDistance(10.0);		
		assertEquals("Objective values should be the same", distObjVal.getValue(), objVal.getValue());
		
		distObjVal.setValue(1);
		assertNull("Distance should be null", distObjVal.getDistance());
		assertEquals("Objective value should be 1", distObjVal.getValue(), BigDecimal.ONE);
	}
}