package org.schemaanalyst.database.sqlite;

import org.schemaanalyst.database.Database;
import org.schemaanalyst.databaseinteraction.DatabaseInteractor;

public class SQLite extends Database {

    private SQLiteDatabaseInteractor databaseInteraction = new SQLiteDatabaseInteractor();

    public SQLite() {
	sqlWriter = new SQLiteSQLWriter();
    }

    public DatabaseInteractor getDatabaseInteraction() {
		return databaseInteraction;
    }

}