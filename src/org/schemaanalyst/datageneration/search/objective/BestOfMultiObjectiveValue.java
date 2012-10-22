package org.schemaanalyst.datageneration.search.objective;

public class BestOfMultiObjectiveValue extends MultiObjectiveValue {

	public BestOfMultiObjectiveValue() {
		super();
	}
	
	public BestOfMultiObjectiveValue(String description) {
		super(description);
	}	
	
	protected void computeValue() {
		ObjectiveValue best = ObjectiveValue.worstObjectiveValue(description);
		for (ObjectiveValue objVal : objVals) {
			if (objVal.betterThan(best)) {
				best = objVal;
			}
		}
		setValue(best);		
		computeValue = false;
	}
	
	public void appendToStringBuilder(StringBuilder sb, String indent) {
		if (computeValue) {
			computeValue();
		}		
		
		super.appendToStringBuilder(sb, indent);
		
		sb.append(" [Best: "); 
		sb.append(value);
		sb.append("]");
		
		appendObjectiveValuesToStringBuilder(sb, indent+"\t");		
	}	
}