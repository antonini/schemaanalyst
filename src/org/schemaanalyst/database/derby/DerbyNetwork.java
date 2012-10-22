package org.schemaanalyst.database.derby;

import org.schemaanalyst.database.Database;
import org.schemaanalyst.databaseinteraction.DatabaseInteractor;
import org.schemaanalyst.sqlwriter.SQLWriter;

public class DerbyNetwork extends Database {

    private SQLWriter sqlWriter = new DerbySQLWriter();

    private DerbyDatabaseInteractor databaseInteraction = new DerbyNetworkDatabaseInteractor();

	public SQLWriter getSQLWriter() {
		return sqlWriter;
	}

	public DatabaseInteractor getDatabaseInteraction() {
		return databaseInteraction;
	}

}