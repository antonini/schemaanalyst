package org.schemaanalyst.mutation.analysis.executor;

import org.schemaanalyst.data.generation.DataGenerator;
import org.schemaanalyst.data.generation.DataGeneratorFactory;
import org.schemaanalyst.dbms.DBMS;
import org.schemaanalyst.dbms.DBMSFactory;
import org.schemaanalyst.dbms.DatabaseInteractor;
import org.schemaanalyst.mutation.Mutant;
import org.schemaanalyst.mutation.analysis.executor.technique.AnalysisResult;
import org.schemaanalyst.mutation.analysis.executor.technique.Technique;
import org.schemaanalyst.mutation.analysis.executor.technique.TechniqueFactory;
import org.schemaanalyst.mutation.pipeline.MutationPipeline;
import org.schemaanalyst.mutation.pipeline.MutationPipelineFactory;
import org.schemaanalyst.sqlrepresentation.Schema;
import org.schemaanalyst.sqlwriter.SQLWriter;
import org.schemaanalyst.testgeneration.TestSuite;
import org.schemaanalyst.testgeneration.TestSuiteGenerator;
import org.schemaanalyst.testgeneration.coveragecriterion.CoverageCriterionFactory;
import org.schemaanalyst.util.csv.CSVFileWriter;
import org.schemaanalyst.util.csv.CSVResult;
import org.schemaanalyst.util.runner.Parameter;
import org.schemaanalyst.util.runner.RequiredParameters;
import org.schemaanalyst.util.runner.Runner;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * An alternative implementation of mutation analysis, using the
 * {@link org.schemaanalyst.mutation.analysis.technique.Original} technique,
 * inserting data from a {@link TestSuite}.
 *
 * @author Chris J. Wright
 */
@RequiredParameters("casestudy criterion")
public class MutationAnalysis extends Runner {

    /**
     * The name of the schema to use.
     */
    @Parameter("The name of the schema to use.")
    /**
     * The name of the schema to use.
     */
    protected String casestudy;
    /**
     * The coverage criterion to use to generate data.
     */
    @Parameter("The coverage criterion to use to generate data.")
    protected String criterion;
    /**
     * The maximum fitness evaluations when generating data.
     */
    @Parameter("The maximum fitness evaluations when generating data.")
    protected int maxevaluations = 100000;
    /**
     * The random seed.
     */
    @Parameter("The random seed.")
    protected long randomseed = 0;
    /**
     * Whether to re-use test cases.
     */
    @Parameter("Whether to re-use test cases.")
    protected boolean reuse = false;
    /**
     * The mutation pipeline to use to generate mutants.
     */
    @Parameter(value = "The mutation pipeline to use to generate mutants.",
            choicesMethod = "org.schemaanalyst.mutation.pipeline.MutationPipelineFactory.getPipelineChoices")
    protected String mutationPipeline = "AllOperatorsWithRemovers";
    /**
     * Whether to print live mutants.
     */
    @Parameter("Whether to print live mutants.")
    protected boolean printLive = false;
    /**
     * Which mutation analysis technique to use.
     */
    @Parameter("Which mutation analysis technique to use.")
    protected String technique = "original";
    /**
     * The instantiated schema.
     */
    protected Schema schema;
    /**
     * The instantiated DBMS.
     */
    protected DBMS dbms;
    /**
     * The writer for the DBMS.
     */
    protected SQLWriter sqlWriter;
    /**
     * The interactor for the DBMS.
     */
    protected DatabaseInteractor databaseInteractor;

    @Override
    protected void task() {
        // Instantiate fields from parameters
        instantiateParameters();

        // Start timing
        long startTime = System.currentTimeMillis();
        
        // Generate test suite and mutants, apply mutation analysis technique
        TestSuite suite = generateTestSuite();
        List<Mutant<Schema>> mutants = generateMutants();
        Technique mutTechnique = instantiateTechnique(schema, mutants, suite, dbms, databaseInteractor);
        AnalysisResult analysisResult = mutTechnique.analyse();

        // Stop timing
        long timeTaken = System.currentTimeMillis() - startTime;
        
        // Write results
        CSVResult result = new CSVResult();
        result.addValue("dbms", databaseConfiguration.getDbms());
        result.addValue("casestudy", casestudy);
        result.addValue("criterion", criterion);
        result.addValue("reuse", reuse);
        result.addValue("tests", suite.getNumTestCases());
        result.addValue("inserts", suite.getNumInserts());
        result.addValue("mutationpipeline", mutationPipeline.replaceAll(",", "|"));
        result.addValue("scorenumerator", analysisResult.getKilled().size());
        result.addValue("scoredenominator", mutants.size());
        result.addValue("timetaken", timeTaken);
        result.addValue("technique", technique);
        new CSVFileWriter(locationsConfiguration.getResultsDir() + File.separator + "newmutationanalysis.dat").write(result);

        if (printLive) {
            for (Mutant<Schema> mutant : analysisResult.getLive()) {
                System.out.println("Alive: " + mutant.getSimpleDescription() + " (" + mutant.getDescription() + ")");
            }
        }
    }

    /**
     * Instantiates the DBMS class, SQL writer and interactor.
     */
    private void instantiateParameters() {
        // Get the required DBMS class, writer and interactor
        dbms = DBMSFactory.instantiate(databaseConfiguration.getDbms());
        sqlWriter = dbms.getSQLWriter();
        databaseInteractor = dbms.getDatabaseInteractor(casestudy, databaseConfiguration, locationsConfiguration);

        // Get the required schema class
        try {
            schema = (Schema) Class.forName(casestudy).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private Technique instantiateTechnique(Schema schema, List<Mutant<Schema>> mutants, TestSuite testSuite, DBMS dbms, DatabaseInteractor databaseInteractor) {
        return TechniqueFactory.instantiate(technique, schema, mutants, testSuite, dbms, databaseInteractor);
    }

    /**
     * Generates the test suite according to the algorithm and criterion.
     *
     * @return The test suite
     */
    private TestSuite generateTestSuite() {
        // Initialise test case generator
        final DataGenerator dataGenerator = DataGeneratorFactory.instantiate("avsDefaults", 0L , 100000);

        TestSuiteGenerator generator = new TestSuiteGenerator(
                schema,
                CoverageCriterionFactory.instantiate(criterion),
                dbms.getValueFactory(),
                dataGenerator
        );
        // Generate suite and return
        return generator.generate();
    }

    /**
     * Generates mutants of the instantiated schema using the named pipeline.
     *
     * @return The mutants
     */
    private List<Mutant<Schema>> generateMutants() {
        MutationPipeline<Schema> pipeline;
        try {
            pipeline = MutationPipelineFactory.<Schema>instantiate(mutationPipeline, schema, databaseConfiguration.getDbms());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
        return pipeline.mutate();
    }

    @Override
    protected void validateParameters() {
        // Do nothing
    }

    public static void main(String[] args) {
        new MutationAnalysis().run(args);
    }

}
