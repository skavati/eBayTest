package common.toolbox.leanft;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.*;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.hp.lft.report.CaptureLevel;
import com.hp.lft.report.ModifiableReportConfiguration;
import com.hp.lft.report.ReportConfigurationFactory;
import com.hp.lft.report.ReportLevel;
import com.hp.lft.report.*;
//import com.hp.lft.report.ModifiableReportConfiguration;
//import com.hp.lft.report.ReportConfigurationFactory;
//import com.hp.lft.report.ReportLevel;
import com.hp.lft.report.ReportException;
import com.hp.lft.sdk.ModifiableSDKConfiguration;
import com.hp.lft.sdk.SDK;

import common.constants.GlobalConstants;
import common.toolbox.sikuli.Sikuli;
import common.toolbox.winium.Winium;
import common.utilities.FileUtils;
import common.utilities.StringUtils;
import common.utilities.SystemUtils;
import common.utilities.XMLUtils;

public class LeanFT {
	private static final Logger LOGGER = LogManager.getLogger(LeanFT.class);
	public static ModifiableReportConfiguration getReportConfiguration() throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ParserConfigurationException, SAXException {

		ModifiableReportConfiguration reportConfig = ReportConfigurationFactory.createDefaultReportConfiguration();
		//reportConfig.setTargetDirectory(GlobalConstants.leanFTReportPath); // The
																			// folder
		//String configPath= FileUtils.findFullPath(System.getProperty("user.dir"), "config.xml");
		String configPath= System.getProperty("user.dir")+"\\config.xml";
		LOGGER.info("config file path: "+configPath);
		String xmlPath = XMLUtils.getNodeText(configPath, "LeanFTReportPath");
		// eg: if xpath contains {user.home} then use below code
		if (xmlPath.contains("{")) {
			String newPath = StringUtils.replaceWithReflection(xmlPath, "common.utilities.SystemUtils", "{", "}");
			// to create all parent directories if not already exist
			FileUtils.createDirectories(newPath);
			reportConfig.setTargetDirectory(newPath);
		} else {
			// to create all parent directories if not already exist
			FileUtils.createDirectories(xmlPath);
			reportConfig.setTargetDirectory(xmlPath);
		}																	// must
		// exist under C:\
		// reportConfig.setReportFolder("LeanFTReport");
		reportConfig.setTitle("Compass Test Results");
		reportConfig.setDescription("Test Report Configuration settings");
		reportConfig.setReportLevel(ReportLevel.All);
		// Only takes screenshot on error
		reportConfig.setSnapshotsLevel(CaptureLevel.OnError);
		// to keep existing results
		reportConfig.setOverrideExisting(true);
		return reportConfig;
	}

	// Initialize SDK configuration
	public static void startSDK() {
		try {
			// http://leanft-help.saas.hpe.com/en/12.52/HelpCenter/Content/HowTo/CustomFrameworks.htm
			// SDK.Init(new SdkConfiguration());
			// initial setup for your custom implementation
			ModifiableSDKConfiguration config = new ModifiableSDKConfiguration();
			config.setServerAddress(new URI("ws://localhost:5095"));
			SDK.init(config);
		} catch (Exception e) {
			
			LOGGER.info(e);
		}

	}

	// Initialize Report configuration
	public static void startReport() {
		try {
			Reporter.init(getReportConfiguration());
		} catch (Exception e) {
			
			LOGGER.info(e);
		}
	}

	// to start LeanFT Runtime Engine
	public static void startRuntimeEngine() throws Exception {
		try {
			if (!SystemUtils.isRunning("LFTRuntime.exe")) {
				SystemUtils.getRuntime().exec("LFTRuntime.exe");
				Integer i = 0;
				LOGGER.info("Waiting for LeanFT Runtime engine...");
				Thread.sleep(5000);
				
				LOGGER.info("\"LFTRuntime\" started.");
			} else {
				LOGGER.info("\"LFTRuntime\" is running.");
			}
		} catch (IOException e) {
			
			LOGGER.info(e);
		} catch (Exception e) {
			
			LOGGER.info(e);
		}
	}
	
	
	// ************* testing purpose only ************************
	@Test
	public void test_engine() throws Exception{
		startRuntimeEngine();
	}
}
