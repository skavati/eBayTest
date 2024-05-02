package common.toolbox.appium;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.xml.sax.SAXException;

import common.baselib.BaseMethods;
import common.constants.GlobalConstants;
import common.toolbox.selenium.Action;
import common.toolbox.selenium.Element;
import common.toolbox.selenium.Waits;
import common.utilities.FileUtils;
import common.utilities.StringUtils;
import common.utilities.XMLUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.android.nativekey.PressesKey;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.appium.java_client.windows.PressesKeyCode;
import io.appium.java_client.windows.WindowsDriver;

public class TestAppium {
	private static AppiumDriver<MobileElement> driver;
	private static Logger LOGGER = LogManager.getLogger(TestAppium.class);
	private static final String ANDROID = "Android";
	private static final String PLATFORM_NAME = "platformName";
	private static final String PLATFORM_VERSION = "platformVersion";
	private static final String DEVICE_NAME = "deviceName";
	private static final String XPATH_LOGON = "//*[@text='Logon to mobile banking']";
	private static final String PW = "cat012";
	private static final String XPATH_OK = "//*[@text='OK']";
	private static final CharSequence CAN_75759616 = "75759616";
	private static final String ID_CAN = null;
	private static final String XPATH_LOGOUT = ".//button[@id='btnLogOut']";
	private static final String XPATH_SKIP = "//*[@text='Skip']";
	private static final String XPATH_X = ".//button[@class='pull-right visible-xs']";
	private static final String XPATH_ACCT_TILE = ".//div[@class='clearfix account-tiles']";
	private static final String BUNDLE_ID = "bundleId";
	private static final String ID_VERIFY = "Verify";
	private static final String END = "===============end====================";
	private static final String SETTINGS_APP_SUCCESS = "Settings app launched successfully.";
	private static final String IPHONE_8 = " Compass's iPhone 8";
	private static final String PREFERENCES = "com.apple.Preferences";
	private static final String INCLUDE_NON_MODEL = "settings[includeNonModalElements]";
	private static final String SIMPLE_VISIBLE_CHECK = "settings[simpleIsVisibleCheck]";
	private static final String AUTOMATION_NAME = "automationName";
	private static final String XCUITEST = "XCUITest";
	private static final String ID_SETTINGS = "Settings";
	private static final String ID_TRUST = "Trust";
	private static final String XPATH_DELETE_APP = "//XCUIElementTypeStaticText[@label='Delete App' or @label='Delete Apps']";
	private static final String XPATH_VERIFY_APP = "Verify App";
	private static final String XPATH_XCUI_TYPE_BUTTON = "XCUIElementTypeButton";
	private static final String ID_TELSTRA07BB = "Telstra07BB";
	private static final String CSS_CLOSE_BUTTON = ".close-button";
	private static final String XPATH_CONTINUE = ".//button[text()='Continue']";
	private static final String UDID_CDA = "cda56cd570f01e6426fb72c515a42fc24c8f48b3";
	private static final String URL = "http://10.148.32.126:4724/wd/hub";
	private static final String BOM = "Bank of Melb.";

	static Element element = new Element();

	public static AppiumDriver getDriver() {
		return driver;
	}

	public static void setDriver(AppiumDriver driver) {
		TestAppium.driver = driver;
	}

	// Launch app using Appium
	@SuppressWarnings("rawtypes")
	// @Test
	// Testing appium driver
	public static void Test_Android_driver() throws MalformedURLException {
		// Capabilities
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(PLATFORM_NAME, ANDROID);
		capabilities.setCapability(PLATFORM_VERSION, "6.0");
		capabilities.setCapability(DEVICE_NAME, "samsung SM-N920I");
		capabilities.setCapability("browser", "chrome");

		AndroidDriver<MobileElement> driver = new AndroidDriver(new URL("http://10.148.44.73:8080/wd/hub"),
				capabilities);
		driver.get("https://e2eibank.stgeorge.com.au/mb/homeloan.html");
		setDriver(driver);
		LOGGER.info("Title: " + driver.getTitle());

		HashMap<String, String> encoding = new HashMap<String, String>();
		encoding.put("encoding", "UTF-8");

		String logFileContents = (String) ((AndroidDriver) driver).executeScript("hpe-wd: downloadLogs", encoding);
		LOGGER.info("logFileContents: " + logFileContents);

	}

	// Android test until it gets to landing page
	// @Test
	public static void test_android_Landing_Page() {

		try {
			AppiumDriver driver = driver = getAppiumDriver();
			if (driver == null) {
				throw new Exception("driver null");
			}
			// ********************** wait for Logon button
			// ****************************
			click_Logon_button(driver);
			// ********************** End wait for Logon button
			// ****************************

			new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_LOGON)));
			driver.findElement(By.id("org.stgeorge.bank:id/can_number_ET")).sendKeys("72882752");
			driver.findElement(By.id("org.stgeorge.bank:id/security_number_ET")).sendKeys("2468");
			driver.findElement(By.id("org.stgeorge.bank:id/internet_password_ET")).sendKeys(PW);
			driver.navigate().back();// to clear device keyboard hiding "logon
										// to mobile banking button"
			driver.findElement(By.xpath(XPATH_LOGON)).click(); // failing
																// for
																// Moto
																// G5

			// If dialog "Service unavailable" pops up then click ok then make
			// sure wifi is
			// poiting to "MobileDev"
			if (element.isElementPresent(driver, By.xpath("//*[@text='Service unavailable']"), 3)) {
				driver.findElement(By.xpath(XPATH_OK)).click();
				// close app and quit driver before changing wifi
				driver.closeApp();

				driver.quit();

				driver = getAppiumDriver();
				click_Logon_button(driver);
				new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_LOGON)));
				if (driver != null) {
					driver.findElement(By.id(ID_CAN)).sendKeys("74202664");
					driver.findElement(By.id("org.stgeorge.bank.test:id/security_number_ET")).sendKeys("2468");
					driver.findElement(By.id("org.stgeorge.bank.test:id/internet_password_ET")).sendKeys(PW);
					driver.findElement(By.id("org.stgeorge.bank.test:id/login_Button")).click();
				}

			}

			// if login details are invalid then change CAN
			if (element.isElementPresent(driver, By.xpath(XPATH_OK), 3)) {
				if (driver != null) {
					driver.findElement(By.xpath(XPATH_OK)).click();
					// try with UAT user
					driver.findElement(By.id(ID_CAN)).clear();
					driver.findElement(By.id(ID_CAN)).sendKeys(CAN_75759616);

					driver.findElement(By.id("org.stgeorge.bank.test:id/security_number_ET")).sendKeys("2468");
					driver.findElement(By.id("org.stgeorge.bank.test:id/internet_password_ET")).sendKeys(PW);
					driver.findElement(By.xpath(XPATH_LOGON)).click();

					if (element.isElementPresent(driver, By.xpath(XPATH_SKIP), 5)) {
						driver.findElement(By.xpath(XPATH_SKIP)).click();
					}
					Driver.switchToWebView(driver);
					Waits.waitInSeconds(2);
					waitForLandingPage(driver);
					driver.findElement(By.xpath(XPATH_LOGOUT)).click();
					Driver.switchToNativeView(driver);
					new WebDriverWait(driver, 10)
							.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='Done']")));
					driver.findElement(By.xpath("//*[@text='Done']")).click();
					new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_LOGON)));
					if (element.isElementPresent(driver, By.xpath("//*[@text='IGNORE']"), 2)) {
						driver.findElement(By.xpath("//*[@text='IGNORE']")).click();
					}
					// change environment to E2E
					driver.findElement(
							By.xpath("//*[@class='android.widget.ImageView' and ./following-sibling::*[@text='More']]"))
							.click();

					new WebDriverWait(driver, 5)
							.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='Environments']")));
					driver.findElement(By.xpath("//*[@text='Environments']")).click();
					driver.findElement(By.xpath("//*[@text='E2E TEST']")).click();
					driver.findElement(By.xpath(XPATH_OK)).click();
					Waits.waitInSeconds(1);
					driver.findElement(By.xpath(
							"//*[@" + "class='android.widget.ImageView' and ./following-sibling::*[@text='Home']]"))
							.click();

					click_Logon_button(driver);
					new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_LOGON)));
					driver.findElementByAccessibilityId("Login with card or access number").sendKeys("74202664");
					driver.findElementByAccessibilityId("Security number").sendKeys("2468");
					driver.findElementByAccessibilityId("Internet password").sendKeys(PW);
					driver.findElement(By.xpath(XPATH_LOGON)).click();
					if (element.isElementPresent(driver, By.xpath(XPATH_SKIP), 5)) {
						driver.findElement(By.xpath(XPATH_SKIP)).click();
					}

				}
				if (element.isElementPresent(driver, By.xpath(XPATH_SKIP), 5) && (driver != null)) {
					driver.findElement(By.xpath(XPATH_SKIP)).click();
				}
				// ********************** wait for Landing page after login
				// ****************************
				Driver.switchToWebView(driver);
				waitForLandingPage(driver);
			}
			// ********************** End wait for Landing page after login
			// ************************
			if (driver != null) {
				org.openqa.selenium.WebElement element = driver
						.findElement(By.xpath(".//div[@class='account-row']//small[contains(text(),'0353')]"));
				common.toolbox.selenium.JavaScript.scrollToView(driver, element);
				element.click();
				new WebDriverWait(driver, 5)
						.until(ExpectedConditions.elementToBeClickable(By.xpath(".//span[text()='Details']")));
				driver.findElement(By.xpath(".//span[text()='Details']")).click();
				driver.findElement(By.xpath(".//*[@class='pull-right icon-x']")).click();
				driver.findElement(By.xpath(".//*[@id='message-btn']")).click();
				new WebDriverWait(driver, 10)
						.until(ExpectedConditions.elementToBeClickable(By.xpath(".//ul[@class='list-group']//li[1]")));
				driver.findElement(By.xpath(".//ul[@class='list-group']//li[1]")).click();
				new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_X)));
				driver.findElement(By.xpath(XPATH_X)).click();

				// Services >> Expese splitter
				new WebDriverWait(driver, 10).until(ExpectedConditions
						.elementToBeClickable(By.xpath(".//span[@class='icon icon-service-bell text-muted']")));
				driver.findElement(By.xpath(".//span[@class='icon icon-service-bell text-muted']")).click();
				driver.findElement(By.xpath(".//a[@aria-label='Expense Splitter']")).click();
				// class="icon-arrow-a-left"
				new WebDriverWait(driver, 30)
						.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@class='icon-arrow-a-left']")));
				driver.findElement(By.xpath(".//*[@class='icon-arrow-a-left']")).click();
				driver.findElement(By.xpath(XPATH_X)).click();
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	public static AppiumDriver getAppiumDriver() {
		try {
			// Capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(PLATFORM_NAME, ANDROID);
			capabilities.setCapability(DEVICE_NAME, "pixel4");
			capabilities.setCapability("appPackage", "org.stgeorge.bank");
			capabilities.setCapability("appActivity", "org.stgeorge.bank.activity.FirstTimeExperienceActivity");
			capabilities.setCapability("recreateChromeDriverSessions", true);

			// MC 2.51

			capabilities.setCapability("udid", "ce11160b53f1571404");// S8+
			AndroidDriver driver = new AndroidDriver(new URL("http://192.168.2.54:4723/wd/hub"), capabilities);

			// MC 2.01
			LOGGER.info("Driver launched successfully.");
			return driver;
		} catch (Exception e) {
			LOGGER.info(e);
		}
		return null;
	}

	public static void click_Logon_button(AppiumDriver driver) throws InterruptedException {
		for (int i = 0; i < 20; i++) {
			if (element.isElementPresent(driver, By.xpath(XPATH_LOGON), 1)) {
				driver.findElement(By.xpath(XPATH_LOGON)).click();
				break;
			} else {
				if (element.isElementPresent(driver, By.xpath("//*[@text='Ignore' or @text='IGNORE']"), 0)) {
					driver.findElement(By.xpath("//*[@text='Ignore' or @text='IGNORE']")).click();
				}
				if (element.isElementPresent(driver, By.xpath("//*[@text='Explore our new features']"), 0)) {
					driver.findElement(By.xpath("//*[@text='Explore our new features']")).click();
					common.toolbox.appium.Gestures.swipeRightToLeft(driver);
					common.toolbox.appium.Gestures.swipeRightToLeft(driver);
					common.toolbox.appium.Gestures.swipeRightToLeft(driver);
					driver.findElement(By.xpath("//*[@text='Get started']")).click();
				}
			}
		}
	}

	public static void waitForLandingPage(AppiumDriver driver) {
		try {
			for (int i = 0; i < 30; i++) {
				if (element.isElementPresent(driver, By.xpath(XPATH_CONTINUE), 1)) {
					driver.findElementByXPath(".//div[@class='checkbox']//label[@for='accept-terms']").click();
					driver.findElementByXPath(XPATH_CONTINUE).click();
				}
				if (element.isElementPresent(driver, By.cssSelector(CSS_CLOSE_BUTTON), 0)) {
					driver.findElement(By.cssSelector(CSS_CLOSE_BUTTON)).click();
					break;
				}
				if (element.isElementPresent(driver, By.xpath(XPATH_ACCT_TILE), 0)) {
					Waits.waitInSeconds(2);
					if (driver.findElement(By.xpath(XPATH_ACCT_TILE)).isDisplayed()) {
						break;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.info(e);
		}
	}

	// Change Wifi on Android
	// @Test
	public static void update_Wifi_Android() {
		try {
			// Capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(PLATFORM_NAME, ANDROID);
			capabilities.setCapability(DEVICE_NAME, "Galaxy S8+");
			capabilities.setCapability("appPackage", "com.android.settings");
			capabilities.setCapability("appActivity", "com.android.settings.wifi.WifiSettings");
			// MC 2.51
			capabilities.setCapability("udid", "ce0317138284a8240c");// S7
			driver = new AndroidDriver(new URL("http://10.0.0.2:4723/wd/hub"), capabilities);
			LOGGER.info("Settings launched successfully.");
			// MC 2.01
			new WebDriverWait(driver, 10).until(
					ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='Wi-Fi' or @text='WI-FI']")));
			Gestures.scrollDownToElement(driver, By.xpath("//*[@text='MOBILEDEV@wsmart']"));
			// driver.findElement(By.xpath("//*[@text='MOBILEDEV@wsmart']")).click();
			driver.findElement(By.xpath("//*[@text='Telstra07BB']")).click();
			// to click on "CONNECT"
			if (element.isElementPresent(driver, By.id("button1"), 3)) {
				driver.findElement(By.id("button1")).click();
			}
			Waits.waitInSeconds(2);
			driver.closeApp();
			Waits.waitInSeconds(2);
			driver.quit();
			Waits.waitInSeconds(2);
			LOGGER.info("Wifi updated.");
		} catch (Exception e) {
			LOGGER.info(e);
			LOGGER.info("Test failed.");
		}
	}

	// @Test
	// Testing iOS app
	public static void test_iOS_driver() {
		try {
			// Capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(PLATFORM_NAME, "iOS");
			capabilities.setCapability(PLATFORM_VERSION, "11.4");
			capabilities.setCapability(DEVICE_NAME, "iPhone 8");
			capabilities.setCapability(BUNDLE_ID, "au.com.stgeorge.ConsultBOMTestEnterprise");
			capabilities.setCapability(MobileCapabilityType.UDID, UDID_CDA);
			capabilities.setCapability("startIWDP", true);
			capabilities.setCapability("webkitResponseTimeout", "70000");
			capabilities.setCapability("userName", "sarath.kavati@stgeorge.com.au");
			driver = new IOSDriver(new URL("http://192.168.2.54:4723/wd/hub"), capabilities);
			LOGGER.info("iOS driver launched successfully.");
			driver.findElement(By.xpath("//XCUIElementTypeButton[@label='Logon']")).click();
			RemoteWebElement can = (RemoteWebElement) driver.findElementByClassName("XCUIElementTypeTextField");
			can.click();
			can.sendKeys(CAN_75759616);
			org.openqa.selenium.WebElement secNo = driver.findElement(By.xpath("//*[@value='Security number']"));

			secNo.sendKeys("2468");
			org.openqa.selenium.WebElement pwd = driver.findElement(By.xpath("//*[@value='Password']"));

			pwd.sendKeys(PW);
			driver.findElement(By.xpath("//XCUIElementTypeButton[@label='Logon to mobile banking']")).click();

			if (element.isElementPresent(driver, MobileBy.AccessibilityId("OK"), 3)) {
				driver.findElement(MobileBy.AccessibilityId("OK")).click();
				// try with UAT user
				driver.findElement(By.xpath("//*[@placeholder='Card / access number']")).sendKeys(CAN_75759616);
				driver.findElement(By.xpath("//*[@placeholder='Security number']")).sendKeys("2468");
				driver.findElement(By.xpath("//*[@placeholder='Password']")).sendKeys(PW);
				driver.findElement(MobileBy.AccessibilityId("Logon to mobile banking")).click();
			}
			for (int i = 0; i < 120; i++) {
				Waits.waitInSeconds(1);
				LOGGER.info("current context:" + driver.getContext());
				if (driver.getContext().equalsIgnoreCase("native_app")) {
					if (driver.getCapabilities().getCapability(PLATFORM_NAME).toString().equalsIgnoreCase("ios")) {

						if (element.isElementPresent(driver, MobileBy.AccessibilityId("Skip"), 0)) {
							driver.findElement(MobileBy.AccessibilityId("Skip")).click();
						}
					} else {
						if (element.isElementPresent(driver, By.xpath(XPATH_SKIP), 0)) {
							driver.findElement(By.xpath(XPATH_SKIP)).click();
						}
					}
				}
				Driver.switchToWebView(driver);
				if (element.isElementPresent(driver, By.xpath(XPATH_CONTINUE), 0)) {
					driver.findElementByXPath(".//div[@class='checkbox']//label[@for='accept-terms']").click();
					driver.findElementByXPath(XPATH_CONTINUE).click();
				}
				if (element.isElementPresent(driver, By.xpath(XPATH_ACCT_TILE), 0)
						&& element.isElementPresent(driver, By.cssSelector(CSS_CLOSE_BUTTON), 3)) {
					// first check if black screen appears as this screen takes
					// couple of seconds to
					// pop up after terms and conditions ticked and continue
					driver.findElement(By.cssSelector(CSS_CLOSE_BUTTON)).click();

				}

				if (element.isElementPresent(driver, By.xpath(XPATH_LOGOUT), 0)) {
					WebElement btnLogout = driver.findElementByXPath(XPATH_LOGOUT);
					if (btnLogout.isDisplayed()) {
						break;
					}
				}
			}

			// to click on logout button
			driver.findElementByXPath(XPATH_LOGOUT).click();

		} catch (Exception e) {
			LOGGER.info(e);
			LOGGER.info("test failed.");
		} finally {
			LOGGER.info(END);
		}
	}

	// To clear hisotry and website data
	// @Test
	public void clearData() {
		try {
			// Capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(DEVICE_NAME, IPHONE_8);
			capabilities.setCapability(PLATFORM_NAME, "iOS");
			capabilities.setCapability(BUNDLE_ID, PREFERENCES);
			capabilities.setCapability(MobileCapabilityType.UDID, UDID_CDA);
			capabilities.setCapability(IOSMobileCapabilityType.WDA_LOCAL_PORT, "8101");
			// to get all elements in pagesource
			capabilities.setCapability(INCLUDE_NON_MODEL, true);
			capabilities.setCapability(SIMPLE_VISIBLE_CHECK, true);
			// dismiss alert
			capabilities.setCapability(AUTOMATION_NAME, XCUITEST);
			AppiumDriver driver = new IOSDriver(new URL("http://10.148.32.123:4724/wd/hub"), capabilities);
			LOGGER.info(SETTINGS_APP_SUCCESS);
			if (element.isElementPresent(driver, MobileBy.AccessibilityId("Back"), 1)) {
				driver.findElement(MobileBy.AccessibilityId("Back")).click();
			}

			RemoteWebElement safari = (RemoteWebElement) driver.findElement(MobileBy.AccessibilityId("Safari"));
			Gestures.swipeToElement_WhenVisibilityAlwaysFalse(driver, safari, "up");
			Waits.waitInSeconds(1);
			safari.click();

			RemoteWebElement mnuClearHistoryAndWebsiteData = (RemoteWebElement) driver
					.findElement(MobileBy.AccessibilityId("Clear History and Website Data"));
			Gestures.swipeToElement_WhenVisibilityAlwaysFalse(driver, safari, "up");
			Waits.waitInSeconds(1);
			mnuClearHistoryAndWebsiteData.click();
			RemoteWebElement mnuClearHistoryAndData = (RemoteWebElement) driver
					.findElement(MobileBy.AccessibilityId("Clear History and Data"));
			Gestures.swipeToElement_WhenVisibilityAlwaysFalse(driver, safari, "up");
			Waits.waitInSeconds(1);
			mnuClearHistoryAndData.click();

			driver.findElementById(ID_SETTINGS).click();
			LOGGER.info("Clear history and data successfully done");
		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			LOGGER.info(END);
		}
	}

	// Change Environment on iOS
	// @Test
	public void test_Change_Env_iOS() {
		try {
			// Capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(DEVICE_NAME, IPHONE_8);
			capabilities.setCapability(PLATFORM_NAME, "iOS");
			capabilities.setCapability(BUNDLE_ID, PREFERENCES);
			capabilities.setCapability(MobileCapabilityType.UDID, UDID_CDA);
			capabilities.setCapability(IOSMobileCapabilityType.WDA_LOCAL_PORT, "8101");
			// to get all elements in pagesource
			capabilities.setCapability(INCLUDE_NON_MODEL, true);
			capabilities.setCapability(SIMPLE_VISIBLE_CHECK, true);
			capabilities.setCapability(AUTOMATION_NAME, XCUITEST);

			AppiumDriver driver = new IOSDriver(new URL(URL), capabilities);
			LOGGER.info(SETTINGS_APP_SUCCESS);
			// LOGGER.info("page source"+driver.getPageSource());
			// "BankSA", "St.George", "Bank of Melb."
			RemoteWebElement lblSTG = (RemoteWebElement) driver.findElement(MobileBy.AccessibilityId("BankSA"));
			if (lblSTG.getAttribute("visible").contentEquals("true")) {
				Gestures.swipeElementRight(driver, lblSTG);
			} else {
				// Move to the top of the list
				// Now move down to element
				Gestures.swipeToElement_WhenVisibilityAlwaysFalse(driver, lblSTG, "up");
				// swipe on element
				Waits.waitInSeconds(1);
				lblSTG.click();
			}

			if (element.isElementPresent(driver, By.id("MOBILE BANKING ENVIRONMENT"), 3)) {
				RemoteWebElement env = (RemoteWebElement) driver
						.findElementByXPath("//XCUIElementTypeButton[contains(@label,'Mobile Banking Environment')]");
				env.click();
			}

			// driver.findElement(MobileBy.AccessibilityId("CUSTOM")).click();
			driver.findElement(MobileBy.AccessibilityId("PROD")).click();
			// back button
			// LOGGER.info("page source"+driver.getPageSource());
			/*
			 * if (element.isElementPresent(driver, MobileBy.AccessibilityId(BOM), 1)) {
			 * driver.findElement(MobileBy.AccessibilityId(BOM)).click(); }
			 */
			driver.findElement(MobileBy.className("XCUIElementTypeButton")).click();
			Waits.waitInSeconds(1);
			String xpath = "//XCUIElementTypeStaticText[@name='Bank Service']/following::XCUIElementTypeTextField";
			driver.findElementByXPath(xpath).clear();
			driver.findElementByXPath(xpath).sendKeys("https://ibanking.banksa.com.au/");
			driver.findElementById("return").click();
			driver.findElementById(ID_SETTINGS).click();
			LOGGER.info("Environment has been changed successfully");
		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			LOGGER.info(END);
		}
	}

	// Change Environment on iOS
	// @Test
	public void test_Clear_App_Cache_iOS() {
		try {
			// Capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(DEVICE_NAME, IPHONE_8);
			capabilities.setCapability(PLATFORM_NAME, "iOS");
			capabilities.setCapability(BUNDLE_ID, PREFERENCES);
			capabilities.setCapability(MobileCapabilityType.UDID, UDID_CDA);
			capabilities.setCapability(IOSMobileCapabilityType.WDA_LOCAL_PORT, "8101");
			// to get all elements in pagesource
			capabilities.setCapability(INCLUDE_NON_MODEL, true);
			capabilities.setCapability(SIMPLE_VISIBLE_CHECK, true);
			capabilities.setCapability(AUTOMATION_NAME, XCUITEST);

			AppiumDriver driver = new IOSDriver(new URL(URL), capabilities);
			LOGGER.info(SETTINGS_APP_SUCCESS);
			// LOGGER.info("page source"+driver.getPageSource());
			// "BankSA", "St.George", "Bank of Melb."
			RemoteWebElement lblSTG = (RemoteWebElement) driver.findElement(MobileBy.AccessibilityId("St.George"));
			if (lblSTG.getAttribute("visible").contentEquals("true")) {
				Gestures.swipeElementRight(driver, lblSTG);
			} else {
				// Move to the top of the list
				// Now move down to element
				Gestures.swipeToElement_WhenVisibilityAlwaysFalse(driver, lblSTG, "up");
				// swipe on element
				Waits.waitInSeconds(1);
				lblSTG.click();
			}

			// Waits.waitInSeconds(3);
			// LOGGER.info("page source"+driver.getPageSource());
			// driver.findElement(MobileBy.AccessibilityId("Mobile Data")).click();
			LOGGER.info("App cache has been cleared successfully");
		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			LOGGER.info(END);
		}
	}

// Change Wi-Fi 
	// @Test
	public void Change_WiFi_iOS() {
		try {
			// Capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(DEVICE_NAME, IPHONE_8);
			capabilities.setCapability(PLATFORM_NAME, "iOS");
			capabilities.setCapability(BUNDLE_ID, PREFERENCES);
			capabilities.setCapability(MobileCapabilityType.UDID, UDID_CDA);
			capabilities.setCapability(IOSMobileCapabilityType.WDA_LOCAL_PORT, "8100");
			// to get all elements in pagesource
			capabilities.setCapability(INCLUDE_NON_MODEL, true);
			capabilities.setCapability(SIMPLE_VISIBLE_CHECK, true);
			capabilities.setCapability(AUTOMATION_NAME, XCUITEST);
			driver = new IOSDriver(new URL(URL), capabilities);
			LOGGER.info(SETTINGS_APP_SUCCESS);

			if (!element.isElementPresent(driver, By.id(ID_TELSTRA07BB), 2)) {
				RemoteWebElement wifi = (RemoteWebElement) driver.findElement(By.id("Wi-Fi"));
				wifi.click();
				Waits.waitInSeconds(2); //

				element.isElementPresent(driver, By.id(ID_TELSTRA07BB), 120);
				driver.findElementById(ID_TELSTRA07BB).click();
				element.isElementPresent(driver, By.id("checkmark"), 120);
				driver.findElementById(ID_SETTINGS).click();
				LOGGER.info("Wifi has been changed successfully");
			}

		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			LOGGER.info(END);
		}
	}

	// @Test
	public static void Get_SMS_Code_iOS() {
		try {
			// Capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(DEVICE_NAME, IPHONE_8);
			capabilities.setCapability(PLATFORM_NAME, "iOS");
			capabilities.setCapability(BUNDLE_ID, PREFERENCES);
			capabilities.setCapability(MobileCapabilityType.UDID, UDID_CDA);
			capabilities.setCapability(IOSMobileCapabilityType.WDA_LOCAL_PORT, "8100");
			// to get all elements in pagesource
			capabilities.setCapability(INCLUDE_NON_MODEL, true);
			capabilities.setCapability(SIMPLE_VISIBLE_CHECK, true);
			capabilities.setCapability(AUTOMATION_NAME, XCUITEST);
			AppiumDriver driver = new IOSDriver(new URL(URL), capabilities);
			LOGGER.info(SETTINGS_APP_SUCCESS);
			Waits.waitInSeconds(3);
			Dimension size = driver.manage().window().getSize();
			int startx = (int) (size.width * 0.05);
			int endx = (int) (size.width * 0.05);
			int starty = (int) (size.height * 0.05);
			int endy = (int) (size.height * 0.6);

			// java-client:6.0.0
			new TouchAction(driver).press(PointOption.point(startx, starty))
					.waitAction(WaitOptions.waitOptions(java.time.Duration.ofSeconds(1)))
					.moveTo(PointOption.point(endx, endy))
					.waitAction(WaitOptions.waitOptions(java.time.Duration.ofSeconds(1))).release().perform();
			LOGGER.info("page source" + driver.getPageSource());
			Waits.waitInSeconds(2);

		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			LOGGER.info(END);
		}
	}

	// @Test
	public void Get_SMS_Code_Android() {
		try {
			String keystore = "C:\\Users\\l083125\\Automation\\GitRepo\\compassautomation\\MBank\\src\\test\\resources\\MBResource\\certificate\\keystore";
			System.setProperty("javax.net.ssl.trustStore", keystore);
			// Capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(PLATFORM_NAME, ANDROID);
			/*
			 * capabilities.setCapability(DEVICE_NAME, "Pixel 4");
			 * capabilities.setCapability(MobileCapabilityType.UDID, "99021FFAZ00AUR");
			 */
			capabilities.setCapability(DEVICE_NAME, "S9");
			capabilities.setCapability(MobileCapabilityType.UDID, "23bc414b16037ece");
			capabilities.setCapability("headspin:appiumVersion", "1.20.2");
			// capabilities.setCapability("headspin:appiumVersion", "1.17.0");
			capabilities.setCapability("headspin:useAppiumUnlock", true);
			capabilities.setCapability("headspin:autoDownloadChromedriver", true);
			capabilities.setCapability("headspin:controlLock", true);
			driver = new AndroidDriver(new URL(
					"https://hswestpac-au-syd-0-proxy-1-lin.srv.westpac.com.au:7001/v0/8f64ecdfe2554bda94aa7f12b28b4154/wd/hub"),
					capabilities);
			// driver = new AndroidDriver(new URL("http://10.148.32.126:4723/wd/hub"),
			// capabilities);
			LOGGER.info("Android driver launched successfully");

			((PressesKey) driver).pressKey(new KeyEvent(AndroidKey.HOME));
			// driver.sendKeyEvent(AndroidKeyCode.HOME);
			// LOGGER.info("Page source:"+driver.getPageSource());
			Waits.waitInSeconds(1);
			WebElement msg = driver.findElementByXPath("//android.widget.TextView[@text='Messages']");
			msg.click();
			Waits.waitInSeconds(1);
			// LOGGER.info("Page source:"+driver.getPageSource());
			/*
			 * WebElement sms = driver.findElementById(
			 * "com.google.android.apps.messaging:id/suggestion_container");
			 * LOGGER.info("SMS: " + sms.getAttribute("content-desc"));
			 */
			// WebElement sms =
			// driver.findElementById("com.samsung.android.messaging:id/text_content");
			WebElement sms = driver.findElementByXPath(
					"//android.widget.TextView[@resource-id='com.samsung.android.messaging:id/text_content' and contains(@text,'Code')]");
			LOGGER.info("SMS: " + sms.getAttribute("text"));
			String secureCode = StringUtils.GetNumericString(sms.getAttribute("text"));
			LOGGER.info("Secure code:" + secureCode.substring(0, 6));
			Waits.waitInSeconds(1);
			driver.navigate().back();
		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			LOGGER.info(END);
		}
	}

// Trust Dev profile
	// @Test
	public void trust_Dev_profile() {
		try {
			// Capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(DEVICE_NAME, IPHONE_8);
			capabilities.setCapability(PLATFORM_NAME, "iOS");
			capabilities.setCapability(BUNDLE_ID, PREFERENCES);
			capabilities.setCapability(MobileCapabilityType.UDID, UDID_CDA);
			capabilities.setCapability(IOSMobileCapabilityType.WDA_LOCAL_PORT, "8101");
			// to get all elements in pagesource
			capabilities.setCapability(INCLUDE_NON_MODEL, true);
			capabilities.setCapability(SIMPLE_VISIBLE_CHECK, true);
			capabilities.setCapability(AUTOMATION_NAME, XCUITEST);

			AppiumDriver driver = new IOSDriver(new URL(URL), capabilities);
			LOGGER.info(SETTINGS_APP_SUCCESS);
			// Software (OS) update alert
			if (element.isElementPresent(driver, By.id("Later"), 0)) {
				driver.findElementById("Later").click();
			}
			if (element.isElementPresent(driver, By.id("Remind Me Later"), 0)) {
				driver.findElementById("Remind Me Later").click();
			}
			// Apple ID verification
			if (element.isElementPresent(driver, By.id("Not Now"), 0)) {
				driver.findElementById("Not Now").click();
			}
			RemoteWebElement general = (RemoteWebElement) driver.findElement(By.id("General"));
			general.click();
			if (element.isElementPresent(driver, By.id("Profiles & Device Management"), 3)) {
				RemoteWebElement profiles = (RemoteWebElement) driver
						.findElement(By.id("Profiles & Device Management"));
				Gestures.swipeToElement_WhenVisibilityAlwaysFalse(driver, profiles, "up");
				profiles.click();
			}

			if (element.isElementPresent(driver, By.id("Apple Development: compasstrain@gmail.com"), 2)) {
				RemoteWebElement profiles = (RemoteWebElement) driver
						.findElement(By.id("Apple Development: compasstrain@gmail.com"));
				Gestures.swipeToElement_WhenVisibilityAlwaysFalse(driver, profiles, "up");
				profiles.click();
				if (element.isElementPresent(driver, By.id("Trust “Apple Development: compasstrain@gmail.com”"), 3)) {
					profiles = (RemoteWebElement) driver
							.findElement(By.id("Trust “Apple Development: compasstrain@gmail.com”"));
					Gestures.swipeToElement_WhenVisibilityAlwaysFalse(driver, profiles, "up");
					profiles.click();
					// dialog
					if (element.isElementPresent(driver, By.id(ID_TRUST), 3)) {
						RemoteWebElement trust = (RemoteWebElement) driver.findElement(By.id(ID_TRUST));
						Gestures.swipeToElement_WhenVisibilityAlwaysFalse(driver, trust, "up");
						trust.click();
					}
				}

				if (element.isElementPresent(driver, By.xpath(XPATH_DELETE_APP), 2)) {
					if (element.isElementPresent(driver, By.id(XPATH_VERIFY_APP), 1)) {
						driver.findElement(By.id(XPATH_VERIFY_APP)).click();
						Waits.waitInSeconds(1);
						driver.findElement(By.id(ID_VERIFY)).click();
					}
					if (element.isElementPresent(driver, By.className(XPATH_XCUI_TYPE_BUTTON), 40)) {
						driver.findElement(By.className(XPATH_XCUI_TYPE_BUTTON)).click();
					}
				}
			}

			if (element.isElementPresent(driver, By.id("Apple Development: sarath.kavati@gmail.com"), 2)) {
				RemoteWebElement profiles = (RemoteWebElement) driver
						.findElement(By.id("Apple Development: sarath.kavati@gmail.com"));
				Gestures.swipeToElement_WhenVisibilityAlwaysFalse(driver, profiles, "up");
				profiles.click();
				if (element.isElementPresent(driver, By.id("Trust “Apple Development: sarath.kavati@gmail.com”"), 3)) {
					profiles = (RemoteWebElement) driver
							.findElement(By.id("Trust “Apple Development: sarath.kavati@gmail.com”"));
					Gestures.swipeToElement_WhenVisibilityAlwaysFalse(driver, profiles, "up");
					profiles.click();
					// dialog
					if (element.isElementPresent(driver, By.id(ID_TRUST), 3)) {
						RemoteWebElement trust = (RemoteWebElement) driver.findElement(By.id(ID_TRUST));
						Gestures.swipeToElement_WhenVisibilityAlwaysFalse(driver, trust, "up");
						trust.click();
					}
				}

				if (element.isElementPresent(driver, By.xpath(XPATH_DELETE_APP), 3)) {
					if (element.isElementPresent(driver, By.id(XPATH_VERIFY_APP), 1)) {
						driver.findElement(By.id(XPATH_VERIFY_APP)).click();
						driver.findElement(By.id(ID_VERIFY)).click();
					}
					driver.findElement(By.className(XPATH_XCUI_TYPE_BUTTON)).click();
				}
			}

			if (element.isElementPresent(driver, By.id("Apple Development: testcompass5@gmail.com"), 3)) {
				RemoteWebElement profiles = (RemoteWebElement) driver
						.findElement(By.id("Apple Development: testcompass5@gmail.com"));
				Gestures.swipeToElement_WhenVisibilityAlwaysFalse(driver, profiles, "up");
				profiles.click();
				if (element.isElementPresent(driver, By.id("Trust “Apple Development: testcompass5@gmail.com”"), 3)) {
					profiles = (RemoteWebElement) driver
							.findElement(By.id("Trust “Apple Development: testcompass5@gmail.com”"));
					Gestures.swipeToElement_WhenVisibilityAlwaysFalse(driver, profiles, "up");
					profiles.click();
					// dialog
					if (element.isElementPresent(driver, By.id(ID_TRUST), 3)) {
						RemoteWebElement trust = (RemoteWebElement) driver.findElement(By.id(ID_TRUST));
						Gestures.swipeToElement_WhenVisibilityAlwaysFalse(driver, trust, "up");
						trust.click();
					}

				}
				// Back
				if (element.isElementPresent(driver, By.xpath(XPATH_DELETE_APP), 3)) {
					if (element.isElementPresent(driver, By.id(XPATH_VERIFY_APP), 1)) {
						driver.findElement(By.id(XPATH_VERIFY_APP)).click();
						driver.findElement(By.id(ID_VERIFY)).click();
					}
					driver.findElement(By.className(XPATH_XCUI_TYPE_BUTTON)).click();
				}
			}

			LOGGER.info("Dev profiles have been trusted successfully");
		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			LOGGER.info(END);
		}
	}

	// Bluetooth settings ON
	// @Test
	public void Turn_Bluetooth_On() {
		try {
			// Capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(DEVICE_NAME, IPHONE_8);
			capabilities.setCapability(PLATFORM_NAME, "iOS");
			capabilities.setCapability(BUNDLE_ID, PREFERENCES);
			capabilities.setCapability(MobileCapabilityType.UDID, UDID_CDA);
			capabilities.setCapability(IOSMobileCapabilityType.WDA_LOCAL_PORT, "8101");
			// to get all elements in pagesource
			capabilities.setCapability(INCLUDE_NON_MODEL, true);
			capabilities.setCapability(SIMPLE_VISIBLE_CHECK, true);
			capabilities.setCapability(AUTOMATION_NAME, XCUITEST);

			AppiumDriver driver = new IOSDriver(new URL(URL), capabilities);
			LOGGER.info(SETTINGS_APP_SUCCESS);
			// Software (OS) update alert
			if (element.isElementPresent(driver, By.id("Later"), 0)) {
				driver.findElementById("Later").click();
			}
			if (element.isElementPresent(driver, By.id("Remind Me Later"), 0)) {
				driver.findElementById("Remind Me Later").click();
			}
			// Apple ID verification
			if (element.isElementPresent(driver, By.id("Not Now"), 0)) {
				driver.findElementById("Not Now").click();
			}

			if (element.isElementPresent(driver, By.id("Dismiss"), 0)) {
				driver.findElementById("Dismiss").click();
			}

			if (element.isElementPresent(driver, By.id("Ignore"), 0)) {
				driver.findElementById("Ignore").click();
			}
			Waits.waitInSeconds(2);
			// To turn on 'Personal Hotspot'

			/*
			 * RemoteWebElement perstonalHotspot = (RemoteWebElement)
			 * driver.findElement(By.id("Personal Hotspot")); perstonalHotspot.click();
			 * Waits.waitInSeconds(2);
			 * LOGGER.info("Personal Hotspot settings launched successfully");
			 * 
			 * RemoteWebElement allowOthersToJoin = (RemoteWebElement)
			 * driver.findElement(By.id("Allow Others to Join")); allowOthersToJoin.click();
			 * Waits.waitInSeconds(1); /*
			 * 
			 * 
			 * RemoteWebElement wifiPwd = (RemoteWebElement)
			 * driver.findElement(By.id("Wi-Fi Password")); wifiPwd.click();
			 * Waits.waitInSeconds(3); RemoteWebElement pwd = (RemoteWebElement)
			 * driver.findElement(By.className("XCUIElementTypeTextField")); pwd.clear();
			 * //Gestures.tapOnCoordinates(370, 121, driver); Waits.waitInSeconds(1);
			 * pwd.sendKeys("compass1");
			 * 
			 * // LOGGER.info("Page source:\n"+driver.getPageSource());
			 * 
			 * Waits.waitInSeconds(1); driver.findElement(By.id("Done")).click();
			 */

			// LOGGER.info("Personal Hotspot settings launched successfully");

			RemoteWebElement bluetooth = (RemoteWebElement) driver.findElement(By.id("Bluetooth"));
			bluetooth.click();
			Waits.waitInSeconds(1);
			LOGGER.info("Bluetooth settings launched successfully");
			Waits.waitInSeconds(2);
			// LOGGER.info("Page source:\n" + driver.getPageSource());
			// To turn ON bluetooth

			/*
			 * RemoteWebElement btnBluetoothSwitch = (RemoteWebElement) driver
			 * .findElement(By.xpath("//XCUIElementTypeSwitch[@value='0']"));
			 * btnBluetoothSwitch.click(); LOGGER.info("Bluetooth is 'ON' successfully");
			 */

			// To turn OFF bluetooth
			/*
			 * RemoteWebElement btnBluetoothSwitch = (RemoteWebElement)
			 * driver.findElement(By.xpath("//XCUIElementTypeSwitch[@value='1']"));
			 * btnBluetoothSwitch.click(); LOGGER.info("Bluetooth is 'OFF' successfully");
			 */

			Waits.waitInSeconds(7);

			RemoteWebElement btnPair = (RemoteWebElement) driver.findElement(By.id("Pair"));
			btnPair.click();

			// More Info

			/*
			 * RemoteWebElement btnMoreInfo = (RemoteWebElement)
			 * driver.findElement(By.id("More Info")); btnMoreInfo.click();
			 * Waits.waitInSeconds(1); RemoteWebElement btnForgetThisDevice =
			 * (RemoteWebElement) driver.findElement(By.id("Forget This Device"));
			 * btnForgetThisDevice.click(); Waits.waitInSeconds(2); RemoteWebElement
			 * btnForgetDevice = (RemoteWebElement)
			 * driver.findElement(By.id("Forget Device")); btnForgetDevice.click();
			 * Waits.waitInSeconds(2);
			 */

			/*
			 * RemoteWebElement lblMacBookPro = (RemoteWebElement)
			 * driver.findElement(By.id("MacBook Pro")); lblMacBookPro.click();
			 * Waits.waitInSeconds(2); RemoteWebElement btnOK = (RemoteWebElement)
			 * driver.findElement(By.id("OK")); btnOK.click();
			 */

			Waits.waitInSeconds(3);
			// LOGGER.info("Page source:\n"+driver.getPageSource());
			LOGGER.info("iPhone 8 is paired with macbook");
		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			LOGGER.info(END);
		}
	}

	// launch Home screen on iphone
	// @Test
	public static void Launch_Home_iOS() {
		try {
			// Capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(DEVICE_NAME, "iPhone8");
			capabilities.setCapability(PLATFORM_NAME, "iOS");
			capabilities.setCapability(BUNDLE_ID, "com.apple.Home");
			capabilities.setCapability(MobileCapabilityType.UDID, UDID_CDA);
			capabilities.setCapability(IOSMobileCapabilityType.WDA_LOCAL_PORT, "8100");
			// to get all elements in pagesource
			capabilities.setCapability(INCLUDE_NON_MODEL, true);
			capabilities.setCapability(SIMPLE_VISIBLE_CHECK, true);
			// dismiss alert
			capabilities.setCapability("autoAcceptAlerts", true);

			capabilities.setCapability(AUTOMATION_NAME, XCUITEST);
			AppiumDriver driver = new IOSDriver(new URL("http://10.252.131.229:4724/wd/hub"), capabilities);
			LOGGER.info("Home app launched successfully.");
			driver.runAppInBackground(java.time.Duration.ofSeconds(-1));
			Waits.waitInSeconds(2);
			Gestures.swipeLeftToRight(driver);
			Waits.waitInSeconds(3);
			Gestures.swipeLeftToRight(driver);
			Waits.waitInSeconds(3);

		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			LOGGER.info(END);
		}
	}

	// @Test
	// test sample app UICatalog (Hybrid app)
	public static void Test_iOS_UICatalog() {
		try {
			// Capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();

			capabilities.setCapability(PLATFORM_NAME, "iOS");
			capabilities.setCapability(DEVICE_NAME, "iPhone7plus");
			// For Simulators
			capabilities.setCapability(PLATFORM_VERSION, "11.2");
			capabilities.setCapability(BUNDLE_ID, "au.com.stg.UICatalog");

			capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
			capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
			capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 50000);

			capabilities.setCapability("startIWDP", true);// not working
			capabilities.setCapability("webkitResponseTimeout", "70000");
			capabilities.setCapability("udid", "1dfe2cc3e94bba45b2f680cf2b9f1f7859ebc4bf");

			driver = new IOSDriver(new URL("http://10.0.0.8:4723/wd/hub"), capabilities);
			LOGGER.info("Driver launched successfully.");
			MobileElement btnWebView = (MobileElement) driver.findElementByAccessibilityId("Web View");
			Gestures.scrollDownToElement(driver, btnWebView);
			btnWebView.click();
			Waits.waitInSeconds(2);
			@SuppressWarnings("unchecked")
			Set<String> availableContexts = driver.getContextHandles();
			LOGGER.info("Context length: " + availableContexts.size());
			for (String context : availableContexts) {
				LOGGER.info("Context Name is " + context);
				if (context.contains("WEBVIEW")) {
					LOGGER.info("Webview context Name is " + context);
					driver.context(context);
					break;
				}
			}

			Waits.waitInSeconds(2);
			driver.findElementById("ac-ls-continue").click();
			Waits.waitInSeconds(2);
			WebElement btnshoppingBag = driver
					.findElement(By.xpath("//ul[@class='ac-gn-header']//a[@class='ac-gn-link ac-gn-link-bag']"));
			btnshoppingBag.click();
			Waits.waitInSeconds(2);
			driver.findElementByXPath("//a[contains(text(),'Sign in')]").click();
			Waits.waitInSeconds(2);
			WebElement element = driver.findElementByXPath("//input[@name='login-appleId']//parent::*");
			LOGGER.info("Screen width: " + driver.manage().window().getSize().getWidth());
			LOGGER.info("Screen height: " + driver.manage().window().getSize().getHeight());

			org.openqa.selenium.JavascriptExecutor js = driver;
			int width = ((Long) js.executeScript("return window.innerWidth || document.body.clientWidth")).intValue();
			int height = ((Long) js.executeScript("return window.innerHeight || document.body.clientHeight"))
					.intValue();
			LOGGER.info("Width of screen: " + width);
			LOGGER.info("Height of screen: " + height);
			Map<String, Object> params = new HashMap<>();
			int x = element.getLocation().getX() + (element.getSize().getWidth() / 2);
			LOGGER.info("Location x: " + x);
			int y = element.getLocation().getY();
			LOGGER.info("Location y: " + y);
			LOGGER.info("Element width : " + element.getSize().getWidth());
			LOGGER.info("Element height : " + element.getSize().getHeight());
			LOGGER.info("page width : " + driver.findElementById("page").getSize().getWidth());
			LOGGER.info("page height : " + driver.findElementById("page").getSize().getHeight());
			params.put("x", 200);

			element.getLocation().getY();
			element.getSize().getHeight();
			element.getSize().getHeight();
			js.executeScript("arguments[0].focus();", element);
			if (element.equals(driver.switchTo().activeElement())) {
				// driver.getKeyboard().sendKeys("compasstrain@gmail.com");

			}

		} catch (Exception e) {
			LOGGER.info(e);
			LOGGER.info("test failed.");
		} finally {
			LOGGER.info(END);
		}
	}

	// delete app data
	// @Test
	public void test_Delete_App_Data_iOS() {
		try {
			// Capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(DEVICE_NAME, IPHONE_8);
			capabilities.setCapability(PLATFORM_NAME, "iOS");
			capabilities.setCapability(BUNDLE_ID, PREFERENCES);
			capabilities.setCapability(MobileCapabilityType.UDID, UDID_CDA);
			capabilities.setCapability(IOSMobileCapabilityType.WDA_LOCAL_PORT, "8101");
			// to get all elements in pagesource
			capabilities.setCapability(INCLUDE_NON_MODEL, true);
			capabilities.setCapability(SIMPLE_VISIBLE_CHECK, true);

			capabilities.setCapability(AUTOMATION_NAME, XCUITEST);

			AppiumDriver driver = new IOSDriver(new URL("http://10.148.32.123:4724/wd/hub"), capabilities);
			LOGGER.info(SETTINGS_APP_SUCCESS);

			RemoteWebElement lbl_General = (RemoteWebElement) driver.findElement(MobileBy.AccessibilityId("General"));
			Gestures.swipeToElement_WhenVisibilityAlwaysFalse(driver, lbl_General, "up");
			Waits.waitInSeconds(1);
			lbl_General.click();
			// click on 'iPhone Storage'
			RemoteWebElement lbliPhoneStorage = (RemoteWebElement) driver
					.findElement(MobileBy.AccessibilityId("iPhone Storage"));
			Gestures.swipeToElement_WhenVisibilityAlwaysFalse(driver, lbliPhoneStorage, "up");
			Waits.waitInSeconds(1);
			lbliPhoneStorage.click();
			// Waits.waitInSeconds(5);
			// LOGGER.info("page source"+driver.getPageSource());
			// click on 'Safari'
			element.isElementPresent(driver, By.xpath("//XCUIElementTypeStaticText[@label='Safari']"), 10);
			RemoteWebElement lblSafari = (RemoteWebElement) driver
					.findElementByXPath("//XCUIElementTypeStaticText[@label='Safari']");
			Gestures.swipeToElement_WhenVisibilityAlwaysFalse(driver, lblSafari, "up");
			Waits.waitInSeconds(1);
			// Gestures.tapOnElement_TouchAction(driver, lblSafari);
			lblSafari.click();
			RemoteWebElement lblWebsiteData = (RemoteWebElement) driver
					.findElement(MobileBy.AccessibilityId("Website Data"));
			Gestures.swipeToElement_WhenVisibilityAlwaysFalse(driver, lblWebsiteData, "up");
			Waits.waitInSeconds(1);
			lblWebsiteData.click(); // Edit
			RemoteWebElement btnEdit = (RemoteWebElement) driver.findElement(MobileBy.AccessibilityId("Edit"));
			btnEdit.click();
			// Waits.waitInSeconds(3);
			// LOGGER.info("page source"+driver.getPageSource());
			// XCUIElementTypeSearchField
			element.isElementPresent(driver, MobileBy.AccessibilityId("Show All Sites"), 3);
			RemoteWebElement lblShowAllSites = (RemoteWebElement) driver
					.findElement(MobileBy.AccessibilityId("Show All Sites"));

			Gestures.swipeToElement_WhenVisibilityAlwaysFalse(driver, lblShowAllSites, "up");
			Waits.waitInSeconds(1);
			lblShowAllSites.click();
			Waits.waitInSeconds(3);
			// LOGGER.info("page source"+driver.getPageSource());
			// Delete bankofmelbourne.com.au
			RemoteWebElement btnDelete = (RemoteWebElement) driver
					.findElement(MobileBy.AccessibilityId("Delete banksa.com.au"));
			Gestures.swipeToElement_WhenVisibilityAlwaysFalse(driver, btnDelete, "up");
			Waits.waitInSeconds(1);
			btnDelete.click();

			Waits.waitInSeconds(1);
			RemoteWebElement btnDelete1 = (RemoteWebElement) driver.findElement(MobileBy.AccessibilityId("Delete"));
			btnDelete1.click();
			Waits.waitInSeconds(1);

			RemoteWebElement btnDone = (RemoteWebElement) driver.findElement(MobileBy.AccessibilityId("Done"));
			btnDone.click();

			LOGGER.info("App data has been deleted successfully");
		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			LOGGER.info(END);
		}
	}

	// to install ios app from appcenter

	// @Test
	public void test_Install_app() {
		try {
			// Capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(DEVICE_NAME, IPHONE_8);
			capabilities.setCapability(PLATFORM_NAME, "iOS");
			capabilities.setCapability(MobileCapabilityType.UDID, UDID_CDA);
			capabilities.setCapability(IOSMobileCapabilityType.WDA_LOCAL_PORT, "8101");
			// to get all elements in pagesource
			capabilities.setCapability(INCLUDE_NON_MODEL, true);
			capabilities.setCapability(SIMPLE_VISIBLE_CHECK, true);
			capabilities.setCapability(AUTOMATION_NAME, XCUITEST);
			capabilities.setCapability("startIWDP", true);
			capabilities.setCapability("webkitResponseTimeout", "70000");
			capabilities.setCapability("browserName", "Safari");
			AppiumDriver driver = new IOSDriver(new URL("http://10.148.32.123:4724/wd/hub"), capabilities);
			driver.get("https://appcenter.ms/apps");
			LOGGER.info("Appcenter launched successfully");
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Driver.switchToNativeView(driver);
			LOGGER.info("Driver switched to native");
			if (element.isElementPresent(driver, By.id("Sign in with email"), 3)) {
				driver.findElementById("Sign in with email").click();
				// Waits.waitInSeconds(2);
				// LOGGER.info("Page source"+driver.getPageSource());
				driver.findElementById("Email or username").sendKeys("mobile@westpac.com.au");
				driver.findElementById("Password").sendKeys("Westpac1");
				driver.findElementById("Keep me signed in").click();
				RemoteWebElement btnSignIn = (RemoteWebElement) driver
						.findElementByXPath("//XCUIElementTypeButton[@label='Sign in']");
				// Gestures.tapOnElement_TouchAction(driver, btnSignIn);
				btnSignIn.click();
			}

			RemoteWebElement lblSTG = (RemoteWebElement) driver.findElementById("BSA iPhone Banking CI Test");
			Gestures.swipeToElement_WhenVisibilityAlwaysFalse(driver, lblSTG, "up");
			Gestures.tapOnElement_TouchAction(driver, lblSTG);
			driver.findElementByClassName("XCUIElementTypeLink").click();
			// Waits.waitInSeconds(2);
			// LOGGER.info("Page source"+driver.getPageSource());
			/*
			 * if (element.isElementPresent(driver, By.id("Add Device"), 3)) {
			 * LOGGER.info("Button 'Add Device' found"); RemoteWebElement btnAddDevice =
			 * (RemoteWebElement) driver.findElementById("Add Device");
			 * Gestures.tapOnElement_TouchAction(driver, btnAddDevice);
			 * 
			 * if (element.isElementPresent(driver, By.id("Allow"), 3)) { LOGGER.
			 * info("Dialog 'This website is trying to download a configuration profile...' found"
			 * ); RemoteWebElement btnAllow = (RemoteWebElement)
			 * driver.findElementById("Allow"); Gestures.tapOnElement_TouchAction(driver,
			 * btnAllow); Waits.waitInSeconds(1); driver.findElementById("Close").click(); }
			 * }
			 */
			if (element.isElementPresent(driver, By.id("I'm good"), 3)) {
				RemoteWebElement btnIamGood = (RemoteWebElement) driver.findElementById("I'm good");
				Gestures.tapOnElement_TouchAction(driver, btnIamGood);
				RemoteWebElement btnOK = (RemoteWebElement) driver.findElementById("OK");
				Gestures.tapOnElement_TouchAction(driver, btnOK);
			}
			RemoteWebElement btnInstall = (RemoteWebElement) driver.findElementById("INSTALL");
			Gestures.tapOnElement_TouchAction(driver, btnInstall);
			RemoteWebElement btnInstall1 = (RemoteWebElement) driver.findElementById("Install");
			Gestures.tapOnElement_TouchAction(driver, btnInstall1);
			LOGGER.info("App installed successfully");
		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			LOGGER.info(END);
		}
	}

	// @Test
	public void test_Uninstall_app() {
		try {
			// Capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(DEVICE_NAME, IPHONE_8);
			capabilities.setCapability(PLATFORM_NAME, "iOS");
			capabilities.setCapability(BUNDLE_ID, PREFERENCES);
			capabilities.setCapability(MobileCapabilityType.UDID, UDID_CDA);
			capabilities.setCapability(IOSMobileCapabilityType.WDA_LOCAL_PORT, "8101");
			// to get all elements in pagesource
			capabilities.setCapability(INCLUDE_NON_MODEL, true);
			capabilities.setCapability(SIMPLE_VISIBLE_CHECK, true);

			capabilities.setCapability(AUTOMATION_NAME, XCUITEST);

			AppiumDriver driver = new IOSDriver(new URL(URL), capabilities);

			// driver.removeApp("au.com.stg1.WebDriverAgentRunner");
			driver.removeApp("au.com.stgeorge.ConsultBSATestEnterprise");
			LOGGER.info("App has been deleted successfully");
		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			LOGGER.info(END);
		}
	}

// to change wifi on iphone		
	// @Test
	public void test_change_wifi_iOS() {
		try {
			// Capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(DEVICE_NAME, IPHONE_8);
			capabilities.setCapability(PLATFORM_NAME, "iOS");
			capabilities.setCapability(BUNDLE_ID, PREFERENCES);
			capabilities.setCapability(MobileCapabilityType.UDID, UDID_CDA);
			capabilities.setCapability(IOSMobileCapabilityType.WDA_LOCAL_PORT, "8101");
			// to get all elements in pagesource
			capabilities.setCapability(INCLUDE_NON_MODEL, true);
			capabilities.setCapability(SIMPLE_VISIBLE_CHECK, true);

			capabilities.setCapability(AUTOMATION_NAME, XCUITEST);

			AppiumDriver driver = new IOSDriver(new URL("http://10.252.131.229:4724/wd/hub"), capabilities);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			LOGGER.info("App has been deleted successfully");
			driver.findElementById("Wi-Fi").click();
			// LOGGER.info("page source"+driver.getPageSource());
			driver.findElementById("Telstra07BB").click();
		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			LOGGER.info(END);
		}
	}

	public static void main(String[] args) {
		try {
			// Waits.waitInMilliSeconds(10000);
		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			LOGGER.info(END);
		}
	}

	// to run basic test on headspin
	@SuppressWarnings("unchecked")
	// @Test
	public void test_Headspin_Android() {
		try {
			// Capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("deviceName", "SM-G960F");
			capabilities.setCapability("platformName", "Android");
			capabilities.setCapability("autoAcceptAlerts", true);
			capabilities.setCapability("udid", "23bc414b16037ece");
			capabilities.setCapability("automationName", "UiAutomator2");
			capabilities.setCapability("appPackage", "com.android.settings");
			capabilities.setCapability("appActivity", "com.android.settings.Settings");
			capabilities.setCapability("headspin:useAppiumUnlock", true);
			// capabilities.setCapability("get_server_logs", true);

			// 10.27.30.10
			LOGGER.info("***** Before android app launch *******");
			driver = new AndroidDriver(new URL(
					"https://hswestpac-au-syd-0-proxy-1-lin.srv.westpac.com.au:7001/v0/71408590175846e481c172365cd54fab/wd/hub"),
					capabilities);
			LOGGER.info("Android driver launched successfully");
			LOGGER.info("Driver session id:" + driver.getSessionId());
			Waits.waitInSeconds(1);
		} catch (Exception e) {
			LOGGER.info("Error while launching app:/n" + e);
		} finally {
			// LogEntries entries = driver.manage().logs().get("server");
			LogEntries entries = driver.manage().logs().get("logcat");
			LOGGER.info("======== APPIUM SERVER LOGS ========");
			for (LogEntry entry : entries) {
				LOGGER.info(new Date(entry.getTimestamp()) + " " + entry.getMessage());
			}
			LOGGER.info("================");
			LOGGER.info(END);
		}
	}

	// to run basic test on headspin
	@SuppressWarnings("unchecked")
	// @Test
	public void test_Headspin_iOS() {
		try {
			// Capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("deviceName", "iPhone 8 Plus");
			capabilities.setCapability("platformName", "iOS");
			capabilities.setCapability("udid", "adedde991c38354085e11ed13827a286d20de0ab");
			capabilities.setCapability("automationName", "XCUITest");
			capabilities.setCapability("bundleId", "com.apple.Preferences");
			LOGGER.info("Before app launch");
			driver = new IOSDriver(new URL(
					"https://hswestpac-au-syd-0-proxy-2-mac.srv.westpac.com.au:7002/v0/71408590175846e481c172365cd54fab/wd/hub"),
					capabilities);
			LOGGER.info("iOS driver launched successfully");
			Waits.waitInSeconds(1);

		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			LOGGER.info(END);
		}
	}

	@BeforeClass
	public static void setUp() {
		// System.setProperty("javax.net.ssl.trustStore",
		// "C:\\Users\\l083125\\Automation\\GitRepos\\compassautomation\\MBank\\src\\test\\resources\\MBResource\\certificate\\keystore");
		// System.setProperty("javax.net.ssl.trustStorePassword", "compass");
	}

	// @Test
	public void test_SeleniumBox() {
		try {
			System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\l083125\\Automation\\keystore\\seleniumBox.ks");
			System.setProperty("javax.net.ssl.keyStorePassword", "compass");
			ChromeOptions options = new ChromeOptions();
			// FirefoxOptions options = new FirefoxOptions();
			options.setCapability(CapabilityType.BROWSER_VERSION, "91");
			options.setCapability("e34:token", "3e10481e-4f48-4a");
			// options.setCapability("e34:video", true);
			// options.setCapability("e34:acceptInsecureCerts", true);
			// RemoteWebDriver driver = new RemoteWebDriver(new
			// URL("https://seleniumgrid.tst.srv.westpac.com.au/wd/hub/"), options);
			// http://au20a9wp2093.infau.wbcau.westpac.com.au:4444/wd/hub
			RemoteWebDriver driver = new RemoteWebDriver(new URL("https://seleniumgrid.tst.srv.westpac.com.au/wd/hub/"),
					options);
			// RemoteWebDriver driver = new RemoteWebDriver(new
			// URL("http://au20a9wp2093.infau.wbcau.westpac.com.au:4444/wd/hub/"), options);

			driver.get("https://ibank2test.stgeorge.com.au/ibank/");
			LOGGER.info("Driver launched successfully");
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			// driver.findElementById("logonButton").click();
			LOGGER.info("login button clicked");
			// LOGGER.info("Error message on screen:\n"
			// + driver.findElementByXPath("//*[@class='ico summary
			// ico-error']/span").getText());
			LOGGER.info("session id:" + driver.getSessionId().toString());

		} catch (Exception e) {
			LOGGER.error(e);
		} finally {
			LOGGER.info(END);
		}
	}

	// @Test
	public void test_Calculator_AlreadyOpen() {
		try {

			DesiredCapabilities desktopCapabilities = new DesiredCapabilities();
			desktopCapabilities.setCapability("platformName", "Windows");
			desktopCapabilities.setCapability("deviceName", "WindowsPC");
			desktopCapabilities.setCapability("platformVersion", 10);
			desktopCapabilities.setCapability("app", "Root");
			WindowsDriver desktopSession = new WindowsDriver(new URL("http://127.0.0.1:4723"), desktopCapabilities);

			WebElement BHWebElement = desktopSession.findElementByName("Calculator");
			String CalcWinHandleStr = BHWebElement.getAttribute("NativeWindowHandle");
			int CalcWinHandleInt = Integer.parseInt(CalcWinHandleStr);
			String CalcWinHandleHex = Integer.toHexString(CalcWinHandleInt);

			DesiredCapabilities CalcCapabilities = new DesiredCapabilities();
			CalcCapabilities.setCapability("platformName", "Windows");
			CalcCapabilities.setCapability("deviceName", "WindowsPC");
			CalcCapabilities.setCapability("appTopLevelWindow", CalcWinHandleHex);
			LOGGER.info("Calc Handle is: " + CalcWinHandleHex);

			WindowsDriver calcSession = new WindowsDriver(new URL("http://127.0.0.1:4723"), CalcCapabilities);

			calcSession.findElementByName("One").click();
			calcSession.findElementByName("Plus").click();
			calcSession.findElementByName("Seven").click();
			calcSession.findElementByName("Equals").click();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void test_CalculatorLaunch() {
		try {

			DesiredCapabilities desktopCapabilities = new DesiredCapabilities();
			desktopCapabilities.setCapability("platformName", "Windows");
			desktopCapabilities.setCapability("deviceName", "WindowsPC");

			desktopCapabilities.setCapability("platformVersion", 10);
			desktopCapabilities.setCapability("app", "Microsoft.WindowsCalculator_8wekyb3d8bbwe!App");
			WindowsDriver desktopSession = new WindowsDriver(new URL("http://127.0.0.1:4723"), desktopCapabilities);
			desktopSession.findElementByName("One").click();
			desktopSession.findElementByName("Plus").click();
			desktopSession.findElementByName("Seven").click();
			desktopSession.findElementByName("Equals").click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void test_DownloadFile() {
		try {

			DesiredCapabilities desktopCapabilities = new DesiredCapabilities();
			desktopCapabilities.setCapability("platformName", "Windows");
			desktopCapabilities.setCapability("deviceName", "WindowsPC");
			desktopCapabilities.setCapability("platformVersion", 10);
			desktopCapabilities.setCapability("app", "Root");
			WindowsDriver driver = new WindowsDriver(new URL("http://127.0.0.1:4723"), desktopCapabilities);

			WebElement parent = driver.findElementByName("Google Chrome - 1 running window");
			parent.click();
			/*
			 * String CalcWinHandleStr = BHWebElement.getAttribute("NativeWindowHandle");
			 * int CalcWinHandleInt = Integer.parseInt(CalcWinHandleStr); String
			 * CalcWinHandleHex = Integer.toHexString(CalcWinHandleInt);
			 * 
			 * DesiredCapabilities cap = new DesiredCapabilities();
			 * cap.setCapability("platformName", "Windows"); cap.setCapability("deviceName",
			 * "WindowsPC"); cap.setCapability("appTopLevelWindow", CalcWinHandleHex);
			 * LOGGER.info("popup window Handle is: " + CalcWinHandleHex);
			 * 
			 * WindowsDriver calcSession = new WindowsDriver(new
			 * URL("http://127.0.0.1:4723"), cap);
			 */

			driver.findElementByName("Discard").click();
			// calcSession.findElementByName("Keep").click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void test_UberEats_Android() {
		try {
			// Capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(PLATFORM_NAME, ANDROID);
			capabilities.setCapability(DEVICE_NAME, "Pixel 4");
			capabilities.setCapability(MobileCapabilityType.UDID, "99021FFAZ00AUR");
			capabilities.setCapability("appPackage", "com.ubercab.eats");
			capabilities.setCapability("appActivity", "com.ubercab.eats.core.activity.LauncherActivity");
			// capabilities.setCapability("chromedriverExecutable",
			// "/Users/Anirudh/Documents/Appium/chromedriver/v94/chromedriver");
			// capabilities.setCapability("automationName", "UiAutomator2");
			driver = new AndroidDriver(new URL("http://10.148.32.126:4723/wd/hub"), capabilities);
			LOGGER.info("Android driver launched successfully");
			// LOGGER.info("Page source:"+driver.getPageSource());
			Set<String> availableContexts = driver.getContextHandles();

			for (String context : availableContexts) {
				LOGGER.info("context: " + context);
			}

			driver.findElementById("com.ubercab.eats:id/mobile_text_field").click();
			Waits.waitInSeconds(1);
			// LOGGER.info("Page source:"+driver.getPageSource());
			availableContexts = driver.getContextHandles();

			for (String context : availableContexts) {
				LOGGER.info("context: " + context);
			}
			// driver.context("WEBVIEW_chrome");

			// LOGGER.info("Page source:"+driver.getPageSource());

			driver.findElementByClassName("android.widget.EditText").sendKeys("0411226279");
			driver.findElementByClassName("android.widget.Button").click();
			Waits.waitInSeconds(30);
			for (String context : availableContexts) {
				LOGGER.info("context: " + context);
			}
			LOGGER.info("Page source:" + driver.getPageSource());

		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			LOGGER.info(END);
		}
	}

	// @Test
	public void test_Stgeorge_Store_Android() {
		try {
			// Capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(PLATFORM_NAME, ANDROID);
			capabilities.setCapability(DEVICE_NAME, "Pixel 4");
			capabilities.setCapability(MobileCapabilityType.UDID, "99021FFAZ00AUR");
			capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "chrome");

			// capabilities.setCapability("appPackage", "org.stgeorge.bank");
			// capabilities.setCapability("appActivity",
			// "org.stgeorge.bank.activity.FirstTimeExperienceActivity");
			capabilities.setCapability("chromedriverExecutable",
					"/Users/Anirudh/Documents/Appium/chromedriver/v94/chromedriver");
			driver = new AndroidDriver(new URL("http://10.148.32.126:4723/wd/hub"), capabilities);
			LOGGER.info("Android driver launched successfully");
			driver.get("https://ibank2test.stgeorge.com.au/mb/");
			Waits.waitInSeconds(2);
			Set<String> availableContexts = driver.getContextHandles();
			for (String context : availableContexts) {
				LOGGER.info("context: " + context);
			}
			driver.context("CHROMIUM");

			// LOGGER.info("Page source:"+driver.getPageSource());

			driver.findElementByXPath("//*[text()='Logon']").click();

		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			LOGGER.info(END);
		}
	}

	// @Test
	public void test_Wikipedia_Android() {
		try {
			String keystore = "C:\\Users\\l083125\\Automation\\GitRepo\\compassautomation\\MBank\\src\\test\\resources\\MBResource\\certificate\\keystore";
			System.setProperty("javax.net.ssl.trustStore", keystore);
			// Capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(PLATFORM_NAME, ANDROID);
			/*
			 * capabilities.setCapability(DEVICE_NAME, "Samsung S9");
			 * capabilities.setCapability(MobileCapabilityType.UDID, "23bc414b16037ece");
			 */
			capabilities.setCapability(DEVICE_NAME, "S10+");
			capabilities.setCapability(MobileCapabilityType.UDID, "R58N11W0WYY");
			capabilities.setCapability("appPackage", "org.wikipedia.alpha");
			capabilities.setCapability("appActivity", "org.wikipedia.main.MainActivity");
			capabilities.setCapability("headspin:appiumVersion", "1.20.2");
			capabilities.setCapability("noReset", true);
			// capabilities.setCapability("headspin:appiumVersion", "1.17.0");
			capabilities.setCapability("headspin:useAppiumUnlock", true);
			capabilities.setCapability("headspin:autoDownloadChromedriver", true);
			capabilities.setCapability("headspin:controlLock", true);
			driver = new AndroidDriver(new URL(
					"https://hswestpac-au-syd-0-proxy-1-lin.srv.westpac.com.au:7001/v0/3bc4a0d0678b4112ac68579e6a404403/wd/hub"),
					capabilities);
			LOGGER.info("Android driver launched successfully");

			Waits.waitInSeconds(1);

			// LOGGER.info("Page source" + driver.getPageSource());
			driver.findElementByXPath("//*[@text='Search Wikipedia']").click();
			Waits.waitInSeconds(1);
			// LOGGER.info("Page source" + driver.getPageSource());

			driver.findElementById("org.wikipedia.alpha:id/search_src_text").sendKeys("Appium");

			// ((PressesKey) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
			Waits.waitInSeconds(2);
			// LOGGER.info("Page source" + driver.getPageSource());

			/*
			 * Set<String> availableContexts = driver.getContextHandles(); for (String
			 * context : availableContexts) { LOGGER.info("context: " + context); }
			 */
			// driver.context("WEBVIEW_org.wikipedia.alpha");
			// Waits.waitInSeconds(1);
			driver.findElementByXPath("//android.widget.TextView[@text='Appium']").click();
			Waits.waitInSeconds(2);

			Set<String> availableContexts = driver.getContextHandles();
			for (String context : availableContexts) {
				LOGGER.info("context: " + context);
			}

			driver.context("WEBVIEW_org.wikipedia.alpha");
			// LOGGER.info("Page source" + driver.getPageSource());
			driver.findElementByXPath("//a[@title='Open-source software']").click();

			// LOGGER.info("Page source" + driver.getPageSource());

		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			LOGGER.info(END);
		}
	}

	// @Test
	public void test_Westpac_Android() {
		try {
			String keystore = "C:\\Users\\l083125\\Automation\\GitRepo\\compassautomation\\MBank\\src\\test\\resources\\MBResource\\certificate\\keystore";
			System.setProperty("javax.net.ssl.trustStore", keystore);
			// Capabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(PLATFORM_NAME, ANDROID);

			capabilities.setCapability(DEVICE_NAME, "S20");
			capabilities.setCapability(MobileCapabilityType.UDID, "RF8NA01ZWGL");

			capabilities.setCapability("appPackage", "org.westpac.bank.debug");
			capabilities.setCapability("appActivity", "au.com.westpac.banking.universal.android.MainActivity");
			capabilities.setCapability("headspin:appiumVersion", "1.20.2");
			capabilities.setCapability("noReset", true);
			// capabilities.setCapability("headspin:appiumVersion", "1.17.0");
			capabilities.setCapability("headspin:useAppiumUnlock", true);
			capabilities.setCapability("headspin:autoDownloadChromedriver", true);
			capabilities.setCapability("headspin:controlLock", true);
			driver = new AndroidDriver(new URL(
					"https://hswestpac-au-syd-0-proxy-1-lin.srv.westpac.com.au:7001/v0/3bc4a0d0678b4112ac68579e6a404403/wd/hub"),
					capabilities);
			LOGGER.info("Android driver launched successfully");

			Waits.waitInSeconds(1);

			// LOGGER.info("Page source" + driver.getPageSource());
			driver.findElement(By.xpath("//android.widget.EditText[@content-desc='Secure text field, Password']"))
					.sendKeys("intbk1");
			driver.findElement(By.id("org.westpac.bank.debug:id/button_main")).click();
			// Waits.waitInSeconds(5);
			// LOGGER.info("Page source" + driver.getPageSource());

			Element.isElementPresent(driver, By.xpath("//android.widget.TextView[@text='Profile']"), 10);
			driver.findElement(By.xpath("//android.widget.TextView[@text='Profile']")).click();
			// Waits.waitInSeconds(2);

			Element.isElementPresent(driver, By.xpath("//android.widget.TextView[@text='Documents']"), 5);
			driver.findElement(By.xpath("//android.widget.TextView[@text='Documents']")).click();
			Element.isElementPresent(driver, By.xpath("//android.widget.TextView[@text='Statements']"), 5);
			driver.findElement(By.xpath("//android.widget.TextView[@text='Statements']")).click();
			Waits.waitInSeconds(5);

			// driver.context("WEBVIEW_org.westpac.bank.debug");
			// LOGGER.info("Page source" + driver.getPageSource());
			// File file = new File("C:\\Users\\l083125\\Automation\\temp\\pagesource.log");
			// FileUtils.writeToFile("C:\\Users\\l083125\\Automation\\temp\\pagesource.txt",
			// driver.getPageSource().toString());
			// WebDriverWait wait = new WebDriverWait(driver, 30);
			// wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='button-back']")));
			// driver.findElement(By.xpath("//a[@class='button-back']")).click();

			((PressesKey) driver).pressKey(new KeyEvent(AndroidKey.BACK));
			Waits.waitInSeconds(1);
			Set<String> availableContexts = driver.getContextHandles();
			for (String context : availableContexts) {
				LOGGER.info("context: " + context);
			}

			driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']")).click();
			Waits.waitInSeconds(1);
			driver.findElement(By.xpath("//android.widget.TextView[@text='Sign out']")).click();
			Waits.waitInSeconds(1);
			FileUtils.writeToFile("C:\\Users\\l083125\\Automation\\temp\\pagesource.txt",
					driver.getPageSource().toString());

			driver.findElement(By.xpath("//android.widget.Button[@text='Done']")).click();
		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			LOGGER.info(END);
		}
	}

	// @Test
	public void testCredablWebUI() {
		try {
			ChromeDriverService service = new ChromeDriverService.Builder()
					.usingDriverExecutable(new File("C:\\Users\\SarathKavati\\Automation\\Temp\\chromedriver.exe"))
					.usingAnyFreePort().withSilent(true).withVerbose(false).build();
			ChromeOptions chromeOptions = new ChromeOptions();

			// optional
			// to set binary path of chrome
			String binaryPath = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe";
			chromeOptions.setBinary(binaryPath);
			chromeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			chromeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			WebDriver webdriver = new ChromeDriver(service, chromeOptions);
			webdriver.get("https://cre-uat-document-internal-web.azurewebsites.net");
			// Element.isElementPresent(webdriver,
			// By.xpath("//*[@class='upload-btn-text']"),30);
			// webdriver.findElement(By.xpath("//*[@class='upload-btn-text']")).click();

		} catch (Exception e) {
			LOGGER.info(e);
		} finally {

			LOGGER.info(END);
		}
	}

	@Test
	public void test_UploadDocument() {
		try {
			// Runtime.getRuntime().exec(
			// "C:\\Users\\SarathKavati\\Automation\\GitRepo\\Credabl.Documents.Internal.Web.Test\\UITest\\src\\test\\resources\\UITestResource\\Windows
			// Application Driver\\WinAppDriver.exe");

			Desktop desktop = Desktop.getDesktop();

			desktop.open(new File(
					"C:\\Users\\SarathKavati\\Automation\\GitRepo\\Credabl.Documents.Internal.Web.Test\\UITest\\src\\test\\resources\\UITestResource\\Windows Application Driver\\WinAppDriver.exe"));

			Waits.waitInSeconds(2);

			DesiredCapabilities desktopCapabilities = new DesiredCapabilities();
			desktopCapabilities.setCapability("platformName", "Windows");
			desktopCapabilities.setCapability("deviceName", "WindowsPC");
			desktopCapabilities.setCapability("platformVersion", 10);
			desktopCapabilities.setCapability("app", "Root");
			WindowsDriver driver = new WindowsDriver(new URL("http://127.0.0.1:4723"), desktopCapabilities);
			WebElement editBar = driver.findElementByAccessibilityId("1148");
			editBar.sendKeys(
					"C:\\Users\\SarathKavati\\Automation\\GitRepo\\Credabl.Documents.Internal.Web.Test\\UITest\\src\\test\\resources\\UITestResource\\documents\\Certified - Drivers Licence"+Keys.ENTER);
			Waits.waitInSeconds(1);
			editBar.sendKeys(Keys.ENTER);
			Waits.waitInSeconds(2);
			//editBar.sendKeys(Keys.HOME,Keys.SHIFT,Keys.END);
			//Waits.waitInSeconds(2);
			
			//editBar = driver.findElementByAccessibilityId("1148");
			//editBar.sendKeys("Certified ID - Medicare and Passport - Carlos_CAB001");
			Action.sendKeys(driver, editBar, "Certified - Drivers Licence"+Keys.ENTER);
			Waits.waitInSeconds(1);
			//Action.sendKeys(driver, editBar, Keys.ENTER);
		
			// driver.findElementByAccessibilityId("1136").sendKeys(Keys.TAB);
			// WebElement btnOpen = driver.findElementByAccessibilityId("1");
			// btnOpen.sendKeys(Keys.ENTER);
			Waits.waitInSeconds(2);
			// Action.doubleClick(driver, btnOpen);
			// editBar.sendKeys(Keys.ENTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}