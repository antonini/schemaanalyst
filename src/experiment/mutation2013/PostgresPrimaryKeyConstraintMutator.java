/*
 */
package experiment.mutation2013;

import java.util.List;
import org.schemaanalyst.mutation.mutators.Mutator;
import org.schemaanalyst.representation.Column;
import org.schemaanalyst.representation.Constraint;
import org.schemaanalyst.representation.ForeignKeyConstraint;
import org.schemaanalyst.representation.PrimaryKeyConstraint;
import org.schemaanalyst.representation.Schema;
import org.schemaanalyst.representation.Table;

/**
 *
 * @author chris
 */
public class PostgresPrimaryKeyConstraintMutator extends Mutator {

    public void produceMutants(Table table, List<Schema> mutants) {
        // get hold of original primaryKey
        PrimaryKeyConstraint primaryKeyConstraint = table.getPrimaryKeyConstraint();

        // check foreign keys
        if (!involvedInForeignKey(table)) {
            // add column
            for (Column column : table.getColumns()) {
                if (primaryKeyConstraint == null || !primaryKeyConstraint.involvesColumn(column)) {
                    mutants.add(makeMutant(table, column, null));
                }
            }

            // remove columns		
            if (primaryKeyConstraint != null) {
                for (Column column : primaryKeyConstraint.getColumns()) {
                    mutants.add(makeMutant(table, null, column));
                }

                // replace columns
                for (Column column : table.getColumns()) {
                    for (Column primaryKeyColumn : primaryKeyConstraint.getColumns()) {
                        if (!primaryKeyConstraint.involvesColumn(column)) {
                            mutants.add(makeMutant(table, column, primaryKeyColumn));
                        }
                    }
                }
            }
        }
    }

    private Schema makeMutant(Table table, Column addColumn, Column removeColumn) {
        Schema mutant = table.getSchema().duplicate();
        String description = "Mutant with primary key column \"";
        if (addColumn == null) {
            if (removeColumn != null) {
                description += removeColumn + "\" removed";
            }
        } else {
            if (removeColumn == null) {
                description += addColumn + "\" added";
            } else {
                description += removeColumn + "\" replaced with \"" + addColumn + "\"";
            }
        }
        description += " on table \"" + table + "\"";
        mutant.addComment(description);
        mutant.addComment("(Primary Key, 3)");
        mutant.addComment("table="+table);

        Table mutantTable = mutant.getTable(table.getName());
        PrimaryKeyConstraint mutantPrimaryKeyConstraint = mutantTable.getPrimaryKeyConstraint();

        // add column to the mutant's table's primary key
        if (addColumn != null) {
            Column column = mutantTable.getColumn(addColumn.getName());
            if (mutantPrimaryKeyConstraint == null) {
                mutantTable.setPrimaryKeyConstraint(column);
            } else {
                mutantPrimaryKeyConstraint.addColumn(column);
            }
        }

        // remove column to the mutant's table's primary key
        if (removeColumn != null) {
            Column column = mutantTable.getColumn(removeColumn.getName());
            mutantPrimaryKeyConstraint.removeColumn(column);
            if (mutantPrimaryKeyConstraint.getNumColumns() == 0) {
                mutantTable.removePrimaryKeyConstraint();
            }
        }

        return mutant;
    }

    private boolean involvedInForeignKey(Table table) {
        for (Constraint constraint : table.getSchema().getConstraints()) {
            if (constraint instanceof ForeignKeyConstraint) {
                ForeignKeyConstraint key = (ForeignKeyConstraint) constraint;
                if (table == key.getReferenceTable()) {
                    return true;
                }
            }
        }
        return false;
//        List<Table> connectedTables = table.getConnectedTables();
//        if (connectedTables == null || !connectedTables.isEmpty()) {
//            return true;
//        }
//        System.out.println("Mutating: "+table.getName());
//        return false;
    }
}
