package casestudy;

import org.schemaanalyst.representation.Column;
import org.schemaanalyst.representation.Schema;
import org.schemaanalyst.representation.Table;
import org.schemaanalyst.representation.datatype.IntDataType;
import org.schemaanalyst.representation.datatype.NumericDataType;
import org.schemaanalyst.representation.datatype.VarCharDataType;

public class NistDML181 extends Schema {

    static final long serialVersionUID = 430698852888568914L;
	
	@SuppressWarnings("unused")
	public NistDML181() {
		super("NistDML181");
		
		/*
		  
		  CREATE TABLE LONG_NAMED_PEOPLE 
		  (
		  FIRSTNAME VARCHAR (373),
		  LASTNAME VARCHAR (373),
		  AGE INT,
		  PRIMARY KEY (FIRSTNAME, LASTNAME)
		  );

		*/

		Table longNamedPeopleTable = createTable( "LONG_NAMED_PEOPLE");

		Column firstName = longNamedPeopleTable .addColumn("FIRSTNAME" , new VarCharDataType(373));
		Column lastName = longNamedPeopleTable .addColumn("LASTNAME" , new VarCharDataType(373));
		Column age = longNamedPeopleTable .addColumn("AGE" , new IntDataType());
		
		longNamedPeopleTable.setPrimaryKeyConstraint(firstName, lastName);

		/*

		  CREATE TABLE ORDERS 
		  (
		  FIRSTNAME VARCHAR (373),
		  LASTNAME VARCHAR (373),
		  TITLE VARCHAR (80),
		  COST NUMERIC(5,2),
		  FOREIGN KEY (FIRSTNAME, LASTNAME)
		  REFERENCES LONG_NAMED_PEOPLE
		  );

		 */

		Table ordersTable = createTable( "ORDERS");
		
		Column firstNameOrders = ordersTable .addColumn("FIRSTNAME" , new VarCharDataType(373));
		Column lastNameOrders = ordersTable .addColumn("LASTNAME" , new VarCharDataType(373));
		Column title = ordersTable .addColumn("TITLE" , new VarCharDataType(80));
		Column cost = ordersTable .addColumn("COST" , new NumericDataType(5,2));

		ordersTable.addForeignKeyConstraint(longNamedPeopleTable, firstNameOrders, lastNameOrders, firstName, lastName);
	}
}
