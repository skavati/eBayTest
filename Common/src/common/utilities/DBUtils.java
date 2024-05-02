package common.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTransientException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.*;
import org.junit.Test;

import com.azure.core.credential.AzureNamedKeyCredential;
import com.azure.data.tables.TableClient;
import com.azure.data.tables.TableServiceClient;
import com.azure.data.tables.TableServiceClientBuilder;
import com.azure.data.tables.models.ListEntitiesOptions;
import com.azure.data.tables.models.TableEntity;

public class DBUtils {
	private static final Logger LOGGER = LogManager.getLogger(DBUtils.class);
	private static Connection con = null;
	private static Statement stmt = null;

	// This method is use for Create connection to Data base:
	public static Connection createDbConnection(String dbType, String dbSever, String portNumber, String dbName,
			String userName, String passWord) throws ClassNotFoundException {
		String url = "";
		LOGGER.info("**** connecting to database ****");
		try {
			if (dbType.equalsIgnoreCase("db2")) {
				Class.forName("com.ibm.db2.jcc.DB2Driver");
				url = "jdbc:" + dbType + "://" + dbSever + ":" + portNumber + "/" + dbName;
				LOGGER.info("DataBase url is : " + url);
			} else if (dbType.equalsIgnoreCase("sqlserver")) {
				Class.forName("com.mysql.jdbc.Driver");
				url = "jdbc:" + dbType + "://" + dbSever + ":" + portNumber + ";" + dbName;
				LOGGER.info("DataBase url is : " + url);
			}
			LOGGER.info("driver loaded");
			con = DriverManager.getConnection(url, userName, passWord);
		} catch (SQLException ex) {
			LOGGER.info("Problem connecting to database");
		}
		return con;

	}

	// This method is use for Get Result Set:
	public static ResultSet getResultSet(Connection dbConnection, String queryString) throws SQLException {
		ResultSet rs = null;
		try {
			// Execute Statement:
			stmt = dbConnection.createStatement();
			ResultSet result = stmt.executeQuery(queryString);
			rs = result;

		} catch (Exception e) {
			LOGGER.info(e);
		}
		return rs;
	}

	// This method use for the Get Column Value:
	public static String getColumnValue(ResultSet rs, String columnName) {
		String columnvalue = null;
		try {
			while (rs.next()) {
				columnvalue = rs.getString(columnName);
				LOGGER.info(columnvalue);
				break;
			}

		} catch (Exception e) {
			LOGGER.info(e);
		}
		return columnvalue;

	}

	// This method is for Close the connection :
	public static void closeConnection(Connection dbConnection) throws SQLException {
		try {
			dbConnection.close();
			LOGGER.info("connection closed");
		} catch (Exception e) {
			LOGGER.info(e);
		}
	}

	// This method is use for Non Select Select Statement :
	public static void executeNonSelectQuery(Connection dbConnection, String queryString)
			throws ClassNotFoundException, SQLException {
		try {
			// Create Statement:
			stmt = dbConnection.createStatement();
			// Execute Statement:
			stmt.execute(queryString);
			LOGGER.info("Query Executed Successfully");
		} catch (SQLTransientException ex) {
			LOGGER.info("Problem executing query");

		}

	}

	// This method is use for Select Select Statement :
	public static void executeSelectQuery(Connection dbConnection, String queryString) {
		try {
			// Create Statement:
			stmt = dbConnection.createStatement();
			// Execute Statement:
			stmt.executeQuery(queryString);
			LOGGER.info("Query Executed Successfully");
		} catch (SQLException ex) {
			LOGGER.info("Problem executing query");
		}

	}

	// Below update Query working for Extent Transaction: Added @29/3/2018
	public static void executeUpdateStment(String date, String id) throws ClassNotFoundException, SQLException {
		Connection dbConnection = createDbConnection("sqlserver", "VIRNTDBD047", "1437",
				"DatabaseName=TransactionHistory", "TranHistService", "TranHistService");
		// Create Statement:
		stmt = dbConnection.createStatement();
		stmt.execute("update [dbo].[transaction] set postingdate='" + date + "'where id=" + id + "");

		ResultSet result = stmt.executeQuery("select postingdate from [dbo].[transaction] where id=" + id + "");
		LOGGER.info(getColumnValue(result, "postingdate"));
	}

	// Testing purpose only: for vp1 47 dbserver and 49 for uat:
	// @Test
	public void test_Method() throws SQLException, ClassNotFoundException, ParseException, InterruptedException {
		Connection dbConnection = createDbConnection("sqlserver", "VIRNTDBD047", "1437",
				"DatabaseName=TransactionHistory", "TranHistService", "TranHistService");
		// Create Statement:
		stmt = dbConnection.createStatement();
		// Execute Statement: for One time updated Message date value in Codes table:
		// stmt.execute("update [dbo].[Codes] set Message='23/03/2015' where
		// Category='MinSupportedDate' AND Code='DDA'");
		// stmt.executeQuery("select message from dbo.Codes where
		// category='MinSupportedDate' AND code='DDA'");

		// stmt.execute("update [dbo].[transaction] set postingdate='2015-03-27'where
		// id=640483");

		ResultSet result = stmt.executeQuery("select postingdate from [dbo].[transaction] where id=640483");
		LOGGER.info(getColumnValue(result, "postingdate"));

		String modified_date = "";

	}

	// @Test
	public void test_DB2() throws ClassNotFoundException, SQLException {
		Connection dbConnection = createDbConnection("db2", "SBDUXLH90", "51100", "DDCMP01", "ddbcmpc", "char0n");
		// Create Statement:
		String qry = "select CD_MESSAGE from dbo.releasecodes where CD_CODE ='ExtendTransactionHistorySwitch' and CD_RELEASE='18E3' and CD_ORIGINBANK='STG'";
		ResultSet result = getResultSet(dbConnection, qry);
		if (result != null) {
			LOGGER.info("Tran.Hist.Switch vlaue: " + getColumnValue(result, "CD_MESSAGE"));
		}
	}

	@Test
	public void test_AzureTables() {
		try {

			String connectionString = "DefaultEndpointsProtocol=https;AccountName=creuatdocspipelineblob;AccountKey=3znXIgkm6qWolaZSfRqm5ytNdj4NMiNl/NGEXGdAEJfhR6qYs26vgrqRzkkZNerDtm3PjZv61o+rhY+vNLEx4Q==;EndpointSuffix=core.windows.net";
			// Create a TableServiceClient with a connection string.
			TableServiceClient tableServiceClient = new TableServiceClientBuilder().connectionString(connectionString)
					.buildClient();
			// Loop through a collection of table names.
			//tableServiceClient.listTables().forEach(tableItem -> System.out.printf("\n"+tableItem.getName()));
			TableClient tableClient = tableServiceClient.getTableClient("crestgdocumentspipelinemessage");
			//tableClient.deleteEntity("22-07", "aaa8838a-6352-4b5e-9e17-bafb36e7157e");
			tableClient.deleteTable();
		} catch (Exception e) {
			// Output the stack trace.
			e.printStackTrace();
		}
	}

}
