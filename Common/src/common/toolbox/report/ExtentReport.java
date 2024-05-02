package common.toolbox.report;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.gherkin.model.Scenario;
import com.aventstack.extentreports.gherkin.model.When;

import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;


import common.utilities.FileUtils;
import common.utilities.StringUtils;
import common.utilities.XMLUtils;


public class ExtentReport {
	// To generate test results report in html format
	private static final Logger LOGGER = LogManager.getLogger(ExtentReport.class);
	private static ExtentReports extent;
	private static String reportNo;
	private static String configPath;
	public static String getReportNo() {
		return reportNo;
	}

	public static void setReportNo(String reportNo) {
		ExtentReport.reportNo = reportNo;
	}

	public static String getConfigPath() {
		return configPath;
	}

	public static void setConfigPath(String configPath) {
		ExtentReport.configPath = configPath;
	}

	// to generate test results using 3.1.1
	public static ExtentReports createInstance(String file) throws IOException {
		ExtentSparkReporter htmlReporter = new ExtentSparkReporter(file);
		String fileName = Paths.get(file).getFileName().toString();
		  htmlReporter.config().setTheme(Theme.STANDARD);
		  htmlReporter.config().setDocumentTitle(fileName);
		  htmlReporter.config().setEncoding("utf-8");
		  htmlReporter.config().setReportName(fileName);
		//htmlReporter.loadXMLConfig("./src/test/resources/html-config.xml");
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		/*
		 * extent.setSystemInfo("Environment", "QA"); extent.setSystemInfo("OS",
		 * "WINDOWS 7"); extent.setSystemInfo("Title", "Automation Report");
		 * extent.setSystemInfo("Name", "Regression");
		 */
		
		return extent;
	}

	public static ExtentReports getExtentReport() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, IOException, ParserConfigurationException{
		String xmlPath = null;
		if (extent == null) {
			xmlPath = XMLUtils.getNodeText(configPath, "ExtentReportFile");
			LOGGER.info("ReportNo:"+reportNo);
			// Below condition useful for parallel testing
			if (reportNo != null) {
				// to get path excluding .html
				String xmlPath1 = FilenameUtils.removeExtension(xmlPath);
				xmlPath = xmlPath1 + reportNo + ".html";
				LOGGER.info("Report Path:"+xmlPath);
				
				}
			// if xpath contains {user.home} then use below code
			if (xmlPath.contains("{")) {
				// to replace {user_home} with C:\Users\C70032
				String newPath = StringUtils.replaceWithReflection(xmlPath,
						"common.utilities.SystemUtils", "{", "}");
				// to replace eg: {timeStamp} with HHmm eg: 1520
				if (newPath.contains("{")) {
					newPath = StringUtils.replaceWithReflection(newPath,
							"common.utilities.SystemUtils", "{", "}");
				}
			
				//FileUtils.createFile(newPath);
				//newPath=newPath+"\\report"+Thread.currentThread().getId()+".html";
				extent = createInstance(newPath);
			} else {
				// to create all parent directories if not already exist
				//FileUtils.createFile(xmlPath);
				extent = createInstance(xmlPath);
			}
		}
		
		return extent;
	}

	// ******* TESTING PURPOSE ONLY***
	public Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}
	@Test
	public void test_extentReport() throws IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InterruptedException {
		// ExtentReports report = getExtentReport();

		ExtentReports report = createInstance(
				"C:\\Users\\C70032\\TestReport\\test.html");
		// gherkin classes

		// feature
		ExtentTest feature = extent.createTest("Transactions");
		
		// scenario
		// imgPath = BaseMethods.getSnap(getWebDriver());
		ExtentTest scenario = feature.createNode(Scenario.class,
				"Verify acct details and acct to acct transfer");
		scenario.assignCategory("catogery");

		
		// ExtentTest scenarioNo = scenario.createNode(Scenario.class, "No: 1");

		// createNode(Scenario.class, "Jeff returns a faulty microwave");
		
		long startTime = System.currentTimeMillis();
		Thread.sleep(5000);
		// wait for activity here
		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime) / 1000;
		
		ExtentTest step = scenario.createNode(When.class, "step passed",
				"step duration:" + duration + " seconds");

		

		// count 2

		

		report.flush();

	}

	// @Test
	public void test_Extractbetweentags() {
		
	}

}
