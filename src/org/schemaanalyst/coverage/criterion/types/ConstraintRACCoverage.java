package org.schemaanalyst.coverage.criterion.types;

import org.schemaanalyst.coverage.criterion.Criterion;
import org.schemaanalyst.coverage.criterion.Predicate;
import org.schemaanalyst.coverage.criterion.requirements.CheckConstraintRequirementsGenerator;
import org.schemaanalyst.coverage.criterion.requirements.MultiColumnConstraintRACRequirementsGenerator;
import org.schemaanalyst.coverage.criterion.requirements.NotNullConstraintRequirementsGenerator;
import org.schemaanalyst.coverage.criterion.requirements.RequirementsGenerator;
import org.schemaanalyst.sqlrepresentation.Schema;
import org.schemaanalyst.sqlrepresentation.Table;
import org.schemaanalyst.sqlrepresentation.constraint.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phil on 31/01/2014.
 */
public class ConstraintRACCoverage extends Criterion {

    @Override
    public List<Predicate> generateRequirements(Schema schema, Table table) {
        List<Predicate> requirements = new ArrayList<>();

        if (schema.hasPrimaryKeyConstraint(table)) {
            PrimaryKeyConstraint primaryKeyConstraint = schema.getPrimaryKeyConstraint(table);
            RequirementsGenerator generator = new MultiColumnConstraintRACRequirementsGenerator(schema, table, primaryKeyConstraint);
            requirements.addAll(generator.generateRequirements());
        }

        for (UniqueConstraint uniqueConstraint : schema.getUniqueConstraints(table)) {
            RequirementsGenerator generator = new MultiColumnConstraintRACRequirementsGenerator(schema, table, uniqueConstraint);
            requirements.addAll(generator.generateRequirements());
        }

        for (ForeignKeyConstraint foreignKeyConstraint : schema.getForeignKeyConstraints(table)) {
            RequirementsGenerator generator = new MultiColumnConstraintRACRequirementsGenerator(schema, table, foreignKeyConstraint);
            requirements.addAll(generator.generateRequirements());
        }

        for (CheckConstraint checkConstraint : schema.getCheckConstraints(table)) {
            RequirementsGenerator generator = new CheckConstraintRequirementsGenerator(schema, table, checkConstraint);
            requirements.addAll(generator.generateRequirements());
        }

        for (NotNullConstraint notNullConstraint : schema.getNotNullConstraints(table)) {
            RequirementsGenerator generator = new NotNullConstraintRequirementsGenerator(schema, table, notNullConstraint);
            requirements.addAll(generator.generateRequirements());
        }

        return requirements;
    }
}