package casestudy;

import org.schemaanalyst.schema.Column;
import org.schemaanalyst.schema.RelationalCheckPredicate;
import org.schemaanalyst.schema.Schema;
import org.schemaanalyst.schema.Table;
import org.schemaanalyst.schema.columntype.CharColumnType;
import org.schemaanalyst.schema.columntype.DecimalColumnType;
import org.schemaanalyst.schema.columntype.IntegerColumnType;
import org.schemaanalyst.schema.columntype.NumericColumnType;

public class NistXTS749 extends Schema {

    static final long serialVersionUID = -4880081864543689479L;
	
	@SuppressWarnings("unused")
	public NistXTS749() {
		super("NistXTS749");
		
		/*
		  
		  CREATE TABLE STAFF 
		  (
		  SALARY  INTEGER,
		  EMPNAME CHAR(20),
		  GRADE   DECIMAL,
		  EMPNUM  CHAR(3) PRIMARY KEY NOT NULL
		  );
		  
		*/

		Table staffTable = createTable( "STAFF");

		Column salary = staffTable.addColumn("SALARY" , new IntegerColumnType());
		Column empname = staffTable.addColumn("EMPNAME" , new CharColumnType(20));
		Column grade = staffTable.addColumn("GRADE" , new DecimalColumnType(300));
		Column empnum = staffTable.addColumn("EMPNUM" , new CharColumnType(3));

		empnum.setPrimaryKey();
		empnum.setNotNull();

		/*

		  CREATE TABLE TEST12649
		  (
		  TNUM1 NUMERIC(5) NOT NULL,
		  TNUM2 NUMERIC(5) NOT NULL,
		  TCHAR CHAR(3),
		  CONSTRAINT CND12649A PRIMARY KEY(TNUM1, TNUM2),
		  CONSTRAINT CND12649B CHECK(TNUM2 > 0),
		  CONSTRAINT CND12649C FOREIGN KEY(TCHAR)
		  REFERENCES STAFF(EMPNUM)
		  );

		 */

		Table test12649Table = createTable( "TEST12649");

		Column tnum1 = test12649Table .addColumn("TNUM1" , new NumericColumnType(5));
		Column tnum2 = test12649Table .addColumn("TNUM2" , new NumericColumnType(5));
		
		tnum1.setNotNull();
		tnum2.setNotNull();

		Column tchar = test12649Table .addColumn("TCHAR" , new CharColumnType(3));
		
		test12649Table.setPrimaryKeyConstraint(tnum1, tnum2);
		
		test12649Table.addCheckConstraint(new RelationalCheckPredicate(tnum2, ">", 0));
		
		test12649Table.addForeignKeyConstraint(staffTable, tchar, empnum);		
	}
}