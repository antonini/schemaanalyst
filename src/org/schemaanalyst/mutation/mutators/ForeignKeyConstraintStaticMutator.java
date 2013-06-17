/*
 */
package org.schemaanalyst.mutation.mutators;

import java.util.ArrayList;
import java.util.List;

import org.schemaanalyst.representation.Column;
import org.schemaanalyst.representation.ForeignKeyConstraint;
import org.schemaanalyst.representation.Schema;
import org.schemaanalyst.representation.Table;

/**
 * Produces mutants containing new foreign key constraints for each column where
 * a reference column in another table can be located of the same type.
 * 
 * @author chris
 */
public class ForeignKeyConstraintStaticMutator extends Mutator {

    @Override
    public void produceMutants(Table table, List<Schema> mutants) {
        // Ignore columns already in foreign keys
        List<Column> columns = table.getColumns();
        for (ForeignKeyConstraint fkey : table.getForeignKeyConstraints()) {
            columns.removeAll(fkey.getColumns());
        }
        
        for (Column localColumn : table.getColumns()) {
            List<Column> refColumns = findColumns(localColumn);
            for (Column foreignColumn : refColumns) {
                produceMutant(localColumn, foreignColumn, mutants);
            }
        }
    }
    
    private void produceMutant(Column localColumn, Column foreignColumn, List<Schema> mutants) {
        Schema mutant = localColumn.getTable().getSchema().duplicate();
        Column dupLocalColumn = mutant.getTable(localColumn.getTable().getName()).getColumn(localColumn.getName());
        Column dupForeignColumn = mutant.getTable(foreignColumn.getTable().getName()).getColumn(foreignColumn.getName());
        dupLocalColumn.setForeignKey(dupForeignColumn.getTable(), foreignColumn);
        mutants.add(mutant);
    }

    private List<Column> findColumns(Column localColumn) {
        List<Column> foreignColumns = new ArrayList<>();
        
        // Ignore localColumn's table
        Schema schema = localColumn.getTable().getSchema();
        List<Table> foreignTables = new ArrayList<>(schema.getTables());
        foreignTables.remove(localColumn.getTable());
        
        // Find reference columns
        for (Table foreignTable : foreignTables) {
            for (Column foreignColumn : foreignTable.getColumns()) {
                if (foreignColumn.getType().getClass().equals(localColumn.getType().getClass())) {
                    foreignColumns.add(foreignColumn);
                }
            }
        }
        return foreignColumns;
    }
}