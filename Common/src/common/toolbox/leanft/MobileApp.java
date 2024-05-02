package common.toolbox.leanft;

import java.net.URL;
import java.util.HashMap;


import org.apache.log4j.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.hp.lft.report.ReportException;
import com.hp.lft.report.Reporter;
import com.hp.lft.sdk.GeneralLeanFtException;
import com.hp.lft.sdk.SDK;
import com.hp.lft.sdk.SwipeDirection;
import com.hp.lft.sdk.mobile.Application;
import com.hp.lft.sdk.mobile.ApplicationDescription;
import com.hp.lft.sdk.mobile.Button;
import com.hp.lft.sdk.mobile.ButtonDescription;
import com.hp.lft.sdk.mobile.Device;
import com.hp.lft.sdk.mobile.EditField;
import com.hp.lft.sdk.mobile.EditFieldDescription;
import com.hp.lft.sdk.mobile.Label;
import com.hp.lft.sdk.mobile.LabelDescription;
import com.hp.lft.sdk.mobile.MobileLab;
import com.hp.lft.sdk.mobile.UiObject;
import com.hp.lft.sdk.mobile.UiObjectDescription;
import com.hp.lft.sdk.mobile.WebView;
import com.hp.lft.sdk.mobile.WebViewDescription;
import com.hp.lft.sdk.web.Page;
import com.hp.lft.sdk.web.PageDescription;
import com.hp.lft.sdk.web.WebElement;
import com.hp.lft.sdk.web.WebElementDescription;
import com.hp.lft.sdk.web.XPathDescription;

import common.constants.GlobalConstants;
import common.toolbox.selenium.Waits;
import common.utilities.ExcelUtils;
import common.utilities.StringUtils;
import common.utilities.XMLUtils;
//import io.appium.java_client.PressesKeyCode;
import io.appium.java_client.android.AndroidDriver;

public class MobileApp {
	private static final Logger LOGGER = LogManager.getLogger(MobileApp.class);
	private static Device device;

	public static Device getDevice() {
		return device;
	}

	public static void setDevice(Device device) {
		MobileApp.device = device;
	}

	private static Application app;
	private static WebView webView;
	private static Page page;
	private static String counter;
	private static String currentTestSuite;

	
	private static WebDriver driver;

	public static WebView getWebView() {
		return webView;
	}

	public static void setWebView(WebView webView) {
		MobileApp.webView = webView;
	}

	public static Application getApp() {
		return app;
	}

	public static void setApp(Application app) {
		MobileApp.app = app;
	}

	public static Page getPage() {
		return page;
	}

	public static void setPage(Page page) {
		MobileApp.page = page;
	}

	public static WebDriver getDriver() {
		return driver;
	}

	public static void setDriver(WebDriver driver) {
		MobileApp.driver = driver;
	}

	public static String getCounter() {
		return counter;
	}

	public static void setCounter(String counter) {
		MobileApp.counter = counter;
	}

	public static String getCurrentTestSuite() {
		return currentTestSuite;
	}

	public static void setCurrentTestSuite(String currentTestSuite) {
		MobileApp.currentTestSuite = currentTestSuite;
	}

	// to get mobile deivce using device id
	public static Device getMobileDevice() {
		try {
			String deviceID;

			if (currentTestSuite == null) {
				// when running scenario as cucumber feature
				// use below
				deviceID = ExcelUtils.getCellData(GlobalConstants.TEST_DATA_MOBILE_WEB_FILE, "DeviceList", "1", "deviceid");
			} else {
				// below is when running from Suite level
				Integer deviceList = ExcelUtils.GetExcelSheet(GlobalConstants.TEST_DATA_MOBILE_WEB_FILE, "DeviceList")
						.getLastRowNum();
				// below line is to run test suites serially
				if (deviceList == 1) {
					deviceID = ExcelUtils.getCellData(GlobalConstants.TEST_DATA_MOBILE_WEB_FILE, "DeviceList", "1",
							"deviceid");
				} else {
					// below line is to run test suites parallel
					String suiteNo = StringUtils.GetNumericString(currentTestSuite);
					deviceID = ExcelUtils.getCellData(GlobalConstants.TEST_DATA_MOBILE_WEB_FILE, "DeviceList", suiteNo,
							"deviceid");
				}

			}
			device = MobileLab.lockDeviceById(deviceID);
			LOGGER.info("Device " + deviceID + " is being locked");
			setDevice(device);
			return device;
		} catch (Exception e) {
			
			LOGGER.info(e);
		}
		return null;

	}

	// to get mobile deivce using device id
	public static Device getCurrentMobileDevice() {
		try {
			String fileName = System.getProperty("user.dir") + "\\config.xml";
			String deviceId = XMLUtils.getNodeText(fileName, "CurrentDeviceID");
			device = MobileLab.lockDeviceById(deviceId);
			setDevice(device);
			return device;
		} catch (Exception e) {
			
			LOGGER.info(e);
		}
		return null;

	}

	public static Application getMobileApp(String appId, Boolean packaged) throws Exception {

		try {
			Application app1;
			device = getMobileDevice();
			// Describe the AUT.
			
			// LeanFT 12.54
			if (device==null) {
				throw new Exception("device is null");
			}
			Application app = device.describe(Application.class,
					new ApplicationDescription.Builder().identifier(appId).packaged(packaged).build());
			setApp(app);
			// to initialize web view as well
			getMobileWebView();
			getMobileWebPage();
			return app;
		} catch (GeneralLeanFtException e) {
			
			LOGGER.info(e);
		}
		return null;

	}

	// to get random app from app list
	public static Application getRandomMobileApp() throws Exception {

		try {
			String[] list = null;
			String appId = null;
			device = getMobileDevice();
			if (device==null) {
				throw new Exception("device is null");
			}
			if (device.getOSType().equalsIgnoreCase("android")) {
				list = new String[] { "org.stgeorge.bank.test", "org.banksa.bank.test", "org.bom.bank.test" };
				appId = StringUtils.getRandom(list);
			} else if (device.getOSType().equalsIgnoreCase("ios")) {
				if (device.getModel().toLowerCase().trim().contains("ipad")) {
					list = new String[] { "au.com.stgeorge.ConsultHD", "au.com.banksa.ConsultHD",
							"au.com.bom.ConsultHD" };
					appId = StringUtils.getRandom(list);
				} else {
					list = new String[] { "au.com.stgeorge.ConsultTest", "au.com.stgeorge.ConsultBSATest",
							"au.com.stgeorge.ConsultBOMTest" };
					appId = StringUtils.getRandom(list);
				}
			}
			Application app = device.describe(Application.class,
					new ApplicationDescription.Builder().identifier(appId).packaged(true).build());

			setApp(app);
			// to initialize web view as well
			getMobileWebView();
			getMobileWebPage();
			return app;
		} catch (GeneralLeanFtException e) {
			
			LOGGER.info(e);
		}
		return null;

	}

	// to get WebView reference from app
	public static WebView getMobileWebView() {

		try {
			WebView webview = app.describe(WebView.class,
					new WebViewDescription.Builder().className("WebView").mobileCenterIndex(0).build());
			setWebView(webview);
			return webview;
		} catch (GeneralLeanFtException e) {
			
			LOGGER.info(e);
		}
		return null;

	}

	// to get mobile web page reference using Object properties
	public static Page getMobileWebPage() {// (HashMap<String, Object> objProp)
											// {

		try {
			Page webPage = MobileApp.getWebView().describe(Page.class, new PageDescription.Builder().build());
			setPage(webPage);

			return webPage;
		} catch (GeneralLeanFtException e) {
			
			LOGGER.info(e);
		}
		return null;

	}

	// to get "Mobile Ui Object" reference using object properties through
	// HashMap
	public static UiObject findUiObject(HashMap<String, Object> objProp) {

		try {
			UiObject uiObj = app.describe(UiObject.class, new UiObjectDescription.Builder().className("View")
					.mobileCenterIndex((Integer) objProp.get("mobileCenterIndex")).build());
			return uiObj;
		} catch (GeneralLeanFtException e) {
			
			LOGGER.info(e);
		}
		return null;

	}

	// launch application
	public static void restartApp(String scenario) {
		try {

			String appID = ExcelUtils.getCellData(GlobalConstants.TEST_DATA_MOBILE_WEB_FILE, "Login", scenario, "appId");
			Boolean packaged = ExcelUtils.getBooleanCellData(GlobalConstants.TEST_DATA_MOBILE_WEB_FILE, "Login", scenario,
					"packaged");

			app = getMobileApp(appID, packaged);
			if (app==null) {
				throw new Exception("app is null");
			}
			app.restart();

		} catch (Exception e) {
			
			LOGGER.info(e);
		}

	}

	// launch application
	// parameters: sheet,scenario
	public static void restartApp(String sheet, String scenario) {
		try {
			Integer lastRowNo = ExcelUtils.GetExcelSheet(GlobalConstants.TEST_DATA_MOBILE_WEB_FILE, "DeviceList")
					.getLastRowNum();
			if (lastRowNo == 1) {
				String appID = ExcelUtils.getCellData(GlobalConstants.TEST_DATA_MOBILE_WEB_FILE, sheet, scenario, "appId");
				app = getMobileApp(appID, true);
			} else {
				app = getRandomMobileApp();
			}
			// String appID =
			// ExcelUtils.getCellData(GlobalConstants.TEST_DATA_MOBILE_WEB_FILE,
			// sheet, scenario, "appId");
			
			
			// app = getMobileApp(appID, packaged);
			if (app==null) {
				throw new Exception("app is null");
			}
			app.restart();

			// app.launch();
			// app.install();
			device.openViewer();

		} catch (Exception e) {
			
			LOGGER.info(e);
		}

	}

	// launch application
	// parameters: sheet,scenario
	public static void restartApp(String sheet, String scenario, Integer scenarioNo) {
		try {
			Integer lastRowNo = ExcelUtils.GetExcelSheet(GlobalConstants.TEST_DATA_MOBILE_WEB_FILE, "DeviceList")
					.getLastRowNum();
			// if one deivce is listed then get app id from excel sheet itself
			// else get random app
			if (lastRowNo == 1) {
				String appID = ExcelUtils.getRepeatedCellData(GlobalConstants.TEST_DATA_MOBILE_WEB_FILE, sheet, scenario,
						scenarioNo, "appId");
				app = getMobileApp(appID, true);
			} else {
				app = getRandomMobileApp();
			}
			if (app==null) {
				throw new Exception("app is null");
			}
			app.restart();
			// app.launch();

		} catch (Exception e) {
			
			LOGGER.info(e);
		}

	}

	// launch app using appium driver

	public static WebDriver getMobileDriver(String scenario, String sheet) throws Exception {

		try {
			String appName = null;
			String appID = ExcelUtils.getCellData(GlobalConstants.TEST_DATA_MOBILE_WEB_FILE, sheet, scenario, "appId");
			Boolean packaged = ExcelUtils.getBooleanCellData(GlobalConstants.TEST_DATA_MOBILE_WEB_FILE, sheet, scenario,
					"packaged");
			app = getMobileApp(appID, packaged);

			if (device.getOSType().equalsIgnoreCase("android")) {
				// Capabilites:
				DesiredCapabilities capabilities = new DesiredCapabilities();
				// capabilities.setCapability("platformName", "Android");
				capabilities.setCapability("udid", "0540d2550a23221d");
				// capabilities.setCapability("platformVersion", "&gt;5.0.1");
				// capabilities.setCapability("deviceName", "samsung GT-1930C");

				

				capabilities.setCapability("userName", "sarath.kavati@stgeorge.com.au");
				capabilities.setCapability("appPackage", "org.stgeorge.bank.test");
				capabilities.setCapability("appActivity",
						"org.stgeorge.bank.test.activity.FirstTimeExperienceActivity");
				// driver = new WebDriver(new URL("http://10.148.17.32:8080/wd/hub"),
				// capabilities);
				LOGGER.info("driver" + driver.getTitle());
				setDriver(driver);
			} else if (device.getOSType().equalsIgnoreCase("ios")) {
				DesiredCapabilities capabilities = new DesiredCapabilities();
				// capabilities.setCapability("platformName", "iOS");
				capabilities.setCapability("udid", "8651d37b05acea7764701e8fbb0087ea702d0937");
				
				capabilities.setCapability("userName", "sarath.kavati@stgeorge.com.au");
				capabilities.setCapability("bundleId", "com.hp.advantage");
				// driver = new IOSDriver(new URL("http://10.148.44.109:8080/wd/hub"),
				// capabilities);
				setDriver(driver);
			} else if (device.getOSType().equalsIgnoreCase("windowsmobile")) {
				// to be implemented
			}

			return driver;
		} catch (GeneralLeanFtException e) {
			
			LOGGER.info(e);
		}
		return null;

	}

	@SuppressWarnings("rawtypes")
	// @Test
	// Testing appium driver
	public void TestMobileDriver() throws Exception {
		// IOSDriver d = null;
		// LeanFT.startSDK();
		// LeanFT.startReport();
		// LOGGER.info("Reporter,SDK started");

		// Capabilites:
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformName", "Android");
		// capabilities.setCapability("udid", "ce11160b53f1571404");
		capabilities.setCapability("udid", "4d6090fc");
		// capabilities.setCapability("platformVersion", "&gt;5.0.1");
		// capabilities.setCapability("deviceName", "LGE Nexus 5");

		// ios

		// capabilities.setCapability("PlatformName", "iOS");
		// capabilities.setCapability("udid",
		// "0afc37b6152eeabefcd4f733d1686289bf0a14c5");
		// capabilities.setCapability("deviceName", "c45915s iPhone");
		
		// capabilities.setCapability("autoLaunch", true);
		// capabilities.setCapability("locationContextEnabled", true);
		capabilities.setCapability("userName", "user@mc.com");
		// android

		
		capabilities.setCapability("appPackage", "org.stgeorge.bank.test");
		capabilities.setCapability("appActivity", "org.stgeorge.bank.test.activity.FirstTimeExperienceActivity");
		capabilities.setCapability("noReset", true);
		

		// ios

		// capabilities.setCapability("bundleId",
		// "au.com.stgeorge.ConsultTest");
		// http://127.0.0.1:8080
		// 10.148.44.74
		driver = new AndroidDriver(new URL("http://10.148.44.74:8080/wd/hub"), capabilities);
		LOGGER.info("driver" + driver.getTitle());

		// ios
		
		// Thread.sleep(5000);
		// Reporter.generateReport();
		// Waits.waitInSeconds(2);
		// SDK.cleanup();
	}

	// @Test
	// Testing LeanFT 14.01 (hpmc 2.51)
	public void Test_LeanFT_14_01() throws ReportException, InterruptedException {

		try {
			LeanFT.startSDK();
			LeanFT.startReport();
			LOGGER.info("Reporter,SDK started");

			// Packaged app
			Device device = MobileLab.lockDeviceById("ce11160b53f1571404");
			Application app = device.describe(Application.class,
					new ApplicationDescription.Builder().identifier("org.stgeorge.bank.test").packaged(true).build());
			app.restart();

			LOGGER.info("app description:" + app.getDescription());
			Label lbl_exitsetup = app.describe(Label.class,
					new LabelDescription.Builder().text("Exit setup").className("Label").build());
			if (lbl_exitsetup.exists(5)) {
				lbl_exitsetup.tap();

			}
			Button btn_shelfMenu = app.describe(Button.class,
					new ButtonDescription.Builder().resourceId("drawerHookBtn").build());
			btn_shelfMenu.highlight();
			// btn_shelfMenu.tap();

			EditField can = app.describe(EditField.class,
					new EditFieldDescription.Builder().resourceId("nr_sl_etxt_cardnumber").build());

			can.tap();
			can.setText("73117211");
			EditField secNo = app.describe(EditField.class, new EditFieldDescription.Builder()
					.accessibilityId("edit box Security number").className("Input").build());
			secNo.setText("2468");
			EditField pwd = app.describe(EditField.class, new EditFieldDescription.Builder()
					.accessibilityId("edit box Internet password").className("Input").build());
			pwd.setText("cat012");

			Button login = app.describe(Button.class,
					new ButtonDescription.Builder().text("Login").className("Button").build());
			login.tap();

			// terms and conditions check box
			Page page_terms = app
					.describe(WebView.class,
							new WebViewDescription.Builder().className("WebView").mobileCenterIndex(0).build())
					.describe(Page.class, new PageDescription());
			page_terms.describe(WebElement.class, new XPathDescription(
					"//SECTION[@id=\"termsTC_section_screen\"]/DIV[2]/FORM[@role=\"form\"][1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/LABEL[1]/SPAN[1]"))
					.click();

			// continue btn
			com.hp.lft.sdk.web.Button btn_continue = app.describe(com.hp.lft.sdk.web.Button.class,
					new com.hp.lft.sdk.web.ButtonDescription.Builder().buttonType("button").tagName("BUTTON")
							.name("Continue").build());
			btn_continue.click();

		} catch (Exception e) {

		} finally {
			Reporter.generateReport();
			Waits.waitInSeconds(2);
			SDK.cleanup();
		}

	}

	// @Test
	public void Test_iOS_Settings() throws ReportException, InterruptedException {

		try {
			LeanFT.startSDK();

			LOGGER.info("Reporter,SDK started");

			// Packaged app
			Device device = MobileLab.lockDeviceById("bb00385e2d10bad504ac26da2ed635f74224c13b");
			Application app = device.describe(Application.class, new ApplicationDescription.Builder()
					.identifier("au.com.stgeorge.ConsultTest").packaged(true).build());
			app.restart();
			Application app_settings = device.describe(Application.class,
					new ApplicationDescription.Builder().identifier("MC.Settings").packaged(false).build());

			app_settings.restart();
			
			
			
			for (int i = 0; i < 10; i = i + 1) {
				
				Label bank = app_settings.describe(Label.class,
						new LabelDescription.Builder().accessibilityId("St.George").className("Label").build());
				if (bank.exists(1)) {
					// bank.highlight();
					bank.tap();
					break;
				} else {
					device.swipe(SwipeDirection.UP);
				}
			}

			UiObject env_list = app_settings.describe(UiObject.class, new UiObjectDescription.Builder()
					.accessibilityId("Environment Plist").className("TableCell").mobileCenterIndex(0).build());
			if (env_list.exists(1)) {
				env_list.tap();
			}
			UiObject env = app_settings.describe(UiObject.class, new UiObjectDescription.Builder()
					.accessibilityId("STG Compass").className("TableCell").mobileCenterIndex(0).build());
			if (env.exists(1)) {
				env.tap();
				Button btn_back = app_settings.describe(Button.class,
						new ButtonDescription.Builder().accessibilityId("Back").className("Button").build());
				btn_back.tap();
				btn_back.tap();
				device.home();
				app.restart();
			}
		} catch (Exception e) {
			LOGGER.info(e);
		} finally {

			
		}

	}

	// @Test
	public void Test_iOS_Services() throws ReportException, InterruptedException {

		try {
			LeanFT.startSDK();
			LOGGER.info("Reporter,SDK started");
			// Packaged app
			Device device = MobileLab.lockDeviceById("05174aa282193bc045bf2a4ce3be7e23756a0fa2");
			Application app = device.describe(Application.class, new ApplicationDescription.Builder()
					.identifier("au.com.stgeorge.ConsultTestEnterprise").packaged(true).build());
			app.restart();
			// enter credentials
			EditField can = app.describe(EditField.class,
					new EditFieldDescription.Builder().className("Input").mobileCenterIndex(0).build());
			can.setText("72229419");
			can.tap();
			app.describe(EditField.class,
					new EditFieldDescription.Builder().className("Input").mobileCenterIndex(1).build()).setText("2468");
			can.tap();
			app.describe(EditField.class,
					new EditFieldDescription.Builder().className("Input").mobileCenterIndex(2).build())
					.setText("cat012");

			Page page = app
					.describe(WebView.class,
							new WebViewDescription.Builder().className("WebView").mobileCenterIndex(0).build())
					.describe(Page.class, new PageDescription());
			// to open services and close it in a loop
			for (int i = 0; i < 10; i = i + 1) {
				WebElement services = page.describe(WebElement.class,
						new WebElementDescription.Builder().className("icon icon-service-bell text-muted").build());
				Button close = page.describe(Button.class,
						new ButtonDescription.Builder().className("pull-right visible-xs").build());
				if (services.exists(1)) {
					services.click();
					close.tap();
				} else {
					LOGGER.info("Services button couldn't be found after " + i + "th iteration");
					break;
				}
			}

			

		} catch (Exception e) {
			LOGGER.info(e);
		} finally {

			
		}

	}

}
