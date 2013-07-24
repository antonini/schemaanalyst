package paper.icst2013;

import org.schemaanalyst.sqlrepresentation.Schema;

import originalcasestudy.BankAccount;
import originalcasestudy.BookTown;
import originalcasestudy.Cloc;
import originalcasestudy.CoffeeOrders;
import originalcasestudy.CustomerOrder;
import originalcasestudy.DellStore;
import originalcasestudy.Employee;
import originalcasestudy.Examination;
import originalcasestudy.Flights;
import originalcasestudy.FrenchTowns;
import originalcasestudy.Inventory;
import originalcasestudy.Iso3166;
import originalcasestudy.JWhoisServer;
import originalcasestudy.NistDML181;
import originalcasestudy.NistDML182;
import originalcasestudy.NistDML183;
import originalcasestudy.NistWeather;
import originalcasestudy.NistXTS748;
import originalcasestudy.NistXTS749;
import originalcasestudy.Person;
import originalcasestudy.Products;
import originalcasestudy.RiskIt;
import originalcasestudy.StudentResidence;
import originalcasestudy.UnixUsage;
import originalcasestudy.Usda;
import paper.util.SchemaStatsTable;

public class LatexSchemaStatsTable extends SchemaStatsTable {

    public static Schema[] schemas = {
        new BankAccount(),
        new BookTown(),
        //		new BooleanExample(),
        new Cloc(),
        new CoffeeOrders(),
        new CustomerOrder(),
        new DellStore(),
        new Employee(),
        new Examination(),
        new Flights(),
        new FrenchTowns(),
        new Inventory(),
        //new ITrust(),
        new Iso3166(),
        new JWhoisServer(),
        new NistDML181(),
        new NistDML182(),
        new NistDML183(),
        new NistWeather(),
        new NistXTS748(),
        new NistXTS749(),
        new Person(),
        new Products(),
        new RiskIt(),
        new StudentResidence(),
        new UnixUsage(),
        new Usda(), //		new World()
    };

    public LatexSchemaStatsTable() {
        super(" & ", " \\\\\n");
    }

    @Override
    protected void writeHeader(StringBuffer table) {
        table.append("%!TEX root=../../icst13-schemaanalyst.tex\n");
    }

    @Override
    protected void writeFooter(StringBuffer table,
            int totalNumTables, int totalNumColumns, // int totalUniqueColumnTypes,
            int totalNumChecks, int totalNumForeignKeys, int totalNumNotNulls,
            int totalNumPrimaryKeys, int totalNumUniques) {
        table.append("\\midrule \n");

        writeRow(table, "{\\bf Total}", totalNumTables, totalNumColumns, // totalUniqueColumnTypes, 
                totalNumChecks, totalNumForeignKeys, totalNumNotNulls, totalNumPrimaryKeys, totalNumUniques);
    }

    public static void main(String[] args) {
        LatexSchemaStatsTable table = new LatexSchemaStatsTable();
        System.out.println(table.write(schemas));
    }
}
