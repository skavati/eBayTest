package common.toolbox.appium;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;

import common.utilities.CSVUtils;
import common.utilities.FileUtils;
import common.utilities.StringUtils;
import common.utilities.XMLUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class Driver {
	private static String currentTestSuite;
	private static AppiumDriver<?> appiumDriver;
	private static String appPackage;
	static String deviceNo;
	static String port;
	static String wdaPort;
	static String webkitDebugProxyPort;
	private static String deviceType;
	private static String platformName;
	private static final String PLATFORM_NAME = "platformName";
	private static final String DEVICE_LIST = "DeviceList";
	private static final String DEVICE_ID = "deviceid";
	private static final String PLATFORM_VERSION = PLATFORM_NAME;
	private static final String EXCEL_DEVICE_TYPE = "DeviceType";
	private static final String EXCEL_DEVICE_NAME = "DeviceName";
	private static final String ANDROID = "android";
	private static final String CASE_STORE = "store";
	private static final String IPHONE = "iphone";
	private static final String APP_ACTIVITY_FIRST_TIME = "org.stgeorge.bank.activity.FirstTimeExperienceActivity";
	private static final String HEADSPIN_URL = "HeadspinURL";

	private static final Logger LOGGER = LogManager.getLogger(Driver.class);
	private static final String BROWSER = "browser";

	public static String getPlatformName() {
		return platformName;
	}

	public static void setPlatformName(String platformName) {
		Driver.platformName = platformName;
	}

	public static String getCurrentTestSuite() {
		return currentTestSuite;
	}

	public static void setCurrentTestSuite(String currentTestSuite) {
		Driver.currentTestSuite = currentTestSuite;
		LOGGER.info("Current suite name: " + currentTestSuite);
	}

	// launch app using appium driver
	public static AppiumDriver getAppiumDriver() {
		return appiumDriver;
	}

	public static void setDriver(AppiumDriver<?> driver) {
		Driver.appiumDriver = driver;
	}

	public static String getAppPackage() {
		return appPackage;
	}

	public static void setAppPackage(String appPackage) {
		Driver.appPackage = appPackage;
	}

	public static String getDeviceNo() {
		return deviceNo;
	}

	public static void setDeviceNo(String deviceNo) {
		Driver.deviceNo = deviceNo;
	}

	public static String getPort() {
		return port;
	}

	public static void setPort(String port) {
		Driver.port = port;
	}

	public static String getWdaPort() {
		return wdaPort;
	}

	public static void setWdaPort(String wdaPort) {
		Driver.wdaPort = wdaPort;
	}

	public static String getWebkitDebugProxyPort() {
		return webkitDebugProxyPort;
	}

	public static void setWebkitDebugProxyPort(String webkitDebugProxyPort) {
		Driver.webkitDebugProxyPort = webkitDebugProxyPort;
	}

	public static String getDeviceType() {
		return deviceType;
	}

	public static void setDeviceType(String deviceType) {
		Driver.deviceType = deviceType;
	}

	private static AppiumDriverLocalService service;
	private static AppiumServiceBuilder builder;
	private static DesiredCapabilities cap;

	@SuppressWarnings("unchecked")
	public static AppiumDriver<MobileElement> getMobileDriver(String sheet, String scenario)
			throws MalformedURLException {
		try {
			AppiumDriver<?> driver = null;
			String configPath = FileUtils.findFullPath(System.getProperty("user.dir"), "config.xml");
			String filename = XMLUtils.getNodeText(configPath, "TestDataDir");
			if (filename.contains("{")) {
				filename = StringUtils.replaceWithReflection(filename,
						"common.utilities.SystemUtils", "{", "}");
			}
			String filePath = filename;
			// getting data from config.xml			
			String userName = XMLUtils.getNodeText(configPath, "UserName");
			String automateKey = XMLUtils.getNodeText(configPath, "AutomateKey");			
			
			String platform;
			String platformVersion;
			String deviceType;
			String deviceName;			
			if (currentTestSuite == null) {
				// when running scenario as cucumber feature
				// use below
				//platform = ExcelUtils.getCellData(filePath, DEVICE_LIST, "1", PLATFORM_NAME);
				 platform = CSVUtils.getCSVData(filePath + "\\DeviceList.csv", "1",
						1, "Platform");
				platformVersion = CSVUtils.getCSVData(filePath + "\\DeviceList.csv", "1",
						1, "OSVersion");		
				deviceType = CSVUtils.getCSVData(filePath + "\\DeviceList.csv", "1",
						1, "DeviceType");
				deviceName = CSVUtils.getCSVData(filePath + "\\DeviceList.csv", "1",
						1, "DeviceName");
			} else {
				// below is when running from Suite level
				Integer deviceList = CSVUtils.getRepeatedCSVCellCount(filePath + "\\ DeviceList.csv", "No");
				// below line is to run test suites serially
				if (deviceList == 1) {
					 platform = CSVUtils.getCSVData(filePath + "\\DeviceList.csv", scenario,
								1, "Platform");
						platformVersion = CSVUtils.getCSVData(filePath + "\\DeviceList.csv", scenario,
								1, "OSVersion");		
						deviceType = CSVUtils.getCSVData(filePath + "\\DeviceList.csv", scenario,
								1, "DeviceType");
						deviceName = CSVUtils.getCSVData(filePath + "\\DeviceList.csv", scenario,
								1, "DeviceName");			
				} else {
					// below line is to run test suites parallel
					String suiteNo = StringUtils.GetNumericString(currentTestSuite);
					platform =  CSVUtils.getCSVData(filePath + "\\ DeviceList.csv", scenario,
							Integer.valueOf(suiteNo), "Platform");
					platformVersion = CSVUtils.getCSVData(filePath + "\\ DeviceList.csv", scenario,
							Integer.valueOf(suiteNo), "OSVersion");
					deviceType = CSVUtils.getCSVData(filePath + "\\ DeviceList.csv", scenario,
							Integer.valueOf(suiteNo), "DeviceType");
					deviceName = CSVUtils.getCSVData(filePath + "\\ DeviceList.csv", scenario,
							Integer.valueOf(suiteNo), "deviceName");
					}
			}
			setDeviceType(deviceType);
			setPlatformName(platform);
			
			// To start Appium server
			if (platform.equalsIgnoreCase(ANDROID)) {				
				DesiredCapabilities capabilities = new DesiredCapabilities();
				capabilities.setCapability(PLATFORM_NAME, "Android");
				capabilities.setCapability("device", deviceName);
				capabilities.setCapability("os_version", platformVersion);
				//uiautomator2
				capabilities.setCapability("automationName", "uiautomator2");
				capabilities.setCapability("video", true);
				capabilities.setCapability("interactiveDebugging ", true);
				capabilities.setCapability("app", "bs://d7c9b6c680ce8a1190e34dcab20a70eab8600977");									
					driver = new AndroidDriver(new URL("https://" + userName +":"+ automateKey + "@hub-cloud.browserstack.com/wd/hub"),
							capabilities);				
				setDriver(driver);				
				LOGGER.info("Android app launched successfully.");	
			} else {
				DesiredCapabilities capabilities = new DesiredCapabilities();
				capabilities.setCapability(PLATFORM_NAME, "iOS");
				capabilities.setCapability(PLATFORM_VERSION, platformVersion);
				capabilities.setCapability("deviceName", deviceName);			
				
				//capabilities.setCapability("startIWDP", true);
				//capabilities.setCapability("webkitResponseTimeout", "70000");
				//capabilities.setCapability("webkitDebugProxyPort", webkitDebugProxyPort);			
				

				//if (launchType.equalsIgnoreCase(BROWSER)) {
				//	capabilities.setCapability(MobileCapabilityType.AUTO_WEBVIEW, true);
				//	capabilities.setCapability("browserName", "Safari");
				//}
				
				driver = new IOSDriver(new URL("https://" + userName +":"+ automateKey + "@hub-cloud.browserstack.com/wd/hub"),
						capabilities);		
				
				setDriver(driver);				
				LOGGER.info("iOS app launched successfully.");
				
			}
			LOGGER.info("Driver session id:"+driver.getSessionId());
			return (AppiumDriver<MobileElement>) driver;
		} catch (Exception e) {

			LOGGER.info(e);
		}
		return null;

	}

	// to switch to Webview
	public static void switchToWebView(AppiumDriver<?> driver) {
		@SuppressWarnings("unchecked")
		Set<String> availableContexts = driver.getContextHandles();
		for (String context : availableContexts) {
			//LOGGER.info("Context:"+context);
			// for ios use below
			if (driver.getCapabilities().getCapability(PLATFORM_NAME).toString().equalsIgnoreCase("mac")
					|| (driver.getCapabilities().getCapability(PLATFORM_NAME).toString().equalsIgnoreCase("ios"))) {
				if (context.contains("WEBVIEW")) {
					driver.context(context);
					break;
				}
				// for android use below
			} else {
				if (context.contains("WEBVIEW_" + appPackage)) {
					driver.context(context);
					break;
				}
			}
		}
	}

	// to switch back to Native app
	public static void switchToNativeView(AppiumDriver<?> driver) {
		driver.context("NATIVE_APP");
	}

	// to start appium server via AppiumDriverLocalService
	public static void startServer(String iPAddress, Integer port) {

		HashMap<String, String> environment = new HashMap<>();
		environment.put("PATH", "/usr/local/bin:" + System.getenv("PATH"));
		service = AppiumDriverLocalService.buildService(
				new AppiumServiceBuilder().usingDriverExecutable(new File("/usr/local/Cellar/node/11.13.0/bin/node"))
						.withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/appium.js")).withLogFile(new File(System.getProperty("user.dir")+"\\target\\Appium_Server_Logs.txt"))
						.withIPAddress(iPAddress).usingPort(port).withEnvironment(environment));
		service.start();

	}

	public void stopServer() {
		service.stop();
	}

	public static boolean checkIfServerIsRunnning(int port) {

		boolean isServerRunning = false;
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(port);
			serverSocket.close();
		} catch (IOException e) {
			// If control comes here, then it means that the port is in use
			isServerRunning = true;
		}
		return isServerRunning;
	}

}
