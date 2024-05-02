package common.baselib;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.xml.sax.SAXException;

import common.constants.GlobalConstants;
import common.toolbox.selenium.Object;
import common.toolbox.selenium.Waits;
import common.utilities.StringUtils;
import common.utilities.SystemUtils;
import common.utilities.XMLUtils;
import io.appium.java_client.AppiumDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class BaseMethods {
	private static final Logger LOGGER = LogManager.getLogger(BaseMethods.class);
	static WebDriver driver = null;
	private static final String CONFIG_XML = "config.xml";
	private static final String NO_SANDBOX = "--no-sandbox";
	private static final String BROWSER_BINARY = "BrowserBinary";
	private static final String SYSTEM_UTILS = "common.utilities.SystemUtils";
	private static final String DISABLE_GPU = "--disable-gpu";
	private static final String HEADLESS_CONFIG = "Headless";
	private static final String HEADLESS = "headless";
	private static final String LOGGER_HEADLESS_MODE = "Chrome running on 'Headless mode'";

	public static ChromeDriverService getChromeDriverService() {
		String driverDir = common.utilities.FileUtils.findFullPath(GlobalConstants.USER_DIR, "chromedriver106");
		LOGGER.info("Chrome Driver directory:" + driverDir);
		ChromeDriverService service = new ChromeDriverService.Builder()
				.usingDriverExecutable(new File(driverDir + "\\chromedriver.exe")).usingAnyFreePort().withSilent(true)
				.withVerbose(false).build();
		return service;
	}

	// to get ie driver as service
	public static InternetExplorerDriverService getIEDriverService() {
		InternetExplorerDriverService service = new InternetExplorerDriverService.Builder()
				.usingDriverExecutable(new File(
						common.utilities.FileUtils.findFullPath(GlobalConstants.USER_DIR, "IEDriverServer.exe")))
				.usingAnyFreePort().withSilent(true).build();
		return service;
	}

	@SuppressWarnings("deprecation")
	public static WebDriver getDriver(String browserName)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParserConfigurationException, IOException {

		try {
			
			switch (browserName.toLowerCase()) {
			case "ie":
				InternetExplorerOptions options = new InternetExplorerOptions();
				options.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				options.setCapability("requireWindowFocus", true);
				// other capabilities

				String ieDriverPath = common.utilities.FileUtils.findFullPath(GlobalConstants.USER_DIR, CONFIG_XML);
				System.setProperty("webdriver.ie.driver", ieDriverPath);
				driver = new InternetExplorerDriver(getIEDriverService(), options);
                break;

			case "chrome":
				String driverDir = common.utilities.FileUtils.findFullPath(GlobalConstants.USER_DIR, "chromedriver104");
				System.setProperty("webdriver.chrome.driver",driverDir+"\\chromedriver.exe");
				ChromeOptions chromeOptions = new ChromeOptions();
				String configPath = common.utilities.FileUtils.findFullPath(GlobalConstants.USER_DIR, CONFIG_XML);
				String useBrowserStack = XMLUtils.getNodeText(configPath, "UseBrowserStack");
				String userName = XMLUtils.getNodeText(configPath, "UserName");
				String automateKey = XMLUtils.getNodeText(configPath, "AutomateKey");
				
				if (useBrowserStack.equalsIgnoreCase("true")) {
					DesiredCapabilities caps = new DesiredCapabilities();					
					//chromeOptions.addArguments("incognito");  // ChromeOptions for starting chrome in incognito mode
					caps.setCapability("chrome.switches", Arrays.asList("--incognito"));
					caps.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
					// other capability declarations
					caps.setCapability("browser", "Chrome");
					caps.setCapability("browser_version", "latest");
					caps.setCapability("os", "Windows");
					caps.setCapability("os_version", "10");
					String URL = "https://" + userName + ":" + automateKey + "@hub-cloud.browserstack.com/wd/hub";
					driver = new RemoteWebDriver(new URL(URL), caps);
				} else {
					// to maximize browser window
					//chromeOptions.addArguments("start-maximized");
					// below line is to lauch brwoser with target url without
					// black screen with address bar just "data"
					//chromeOptions.addArguments(NO_SANDBOX);
					//chromeOptions.addArguments("--single-process");
					// to set binary path of chrome
					String binaryPath = XMLUtils.getNodeText(configPath, BROWSER_BINARY);
					if (binaryPath.contains("{")) {
						binaryPath = StringUtils.replaceWithReflection(binaryPath, SYSTEM_UTILS, "{", "}");
					}
					//chromeOptions.setBinary(binaryPath);
					 DesiredCapabilities capabilities = new DesiredCapabilities();
					 capabilities.setCapability("chrome.switches", Arrays.asList("--incognito"));						
					// below line optional
					capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
					// to stop logging driver info on console
					capabilities.setCapability("chrome.verbose", false);
					capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
					driver = new ChromeDriver(capabilities);
				}

				break;

			case "firefox":
				String driverPath = common.utilities.FileUtils.findFullPath(GlobalConstants.USER_DIR,
						"geckodriver.exe");
				LOGGER.info("Firefox Driver path:" + driverPath);
				System.setProperty("webdriver.gecko.driver", driverPath);
				driver = new FirefoxDriver();
				break;
			default:
				break;
			}
		} catch (NoSuchElementException e) {
			LOGGER.info(e.getMessage());
		}
		return driver;

	}

	public static WebDriver getChromeDriver(ChromeDriverService service)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParserConfigurationException, SAXException, IOException {
		try {
			ChromeOptions chromeOptions = new ChromeOptions();
			//chromeOptions.addArguments(DISABLE_GPU);
			// to suppress console logs
			//chromeOptions.addArguments("--log-level=3");
			//chromeOptions.addArguments("--silent");
			// below line is to launch browser with target url without black
			// screen with address bar just "data"
			//chromeOptions.addArguments(NO_SANDBOX);
			String configPath = common.utilities.FileUtils.findFullPath(GlobalConstants.USER_DIR, CONFIG_XML);
			String headless = XMLUtils.getNodeText(configPath, HEADLESS_CONFIG);
			if (headless.equalsIgnoreCase("true")) {
				//chromeOptions.addArguments(HEADLESS);
				LOGGER.info(LOGGER_HEADLESS_MODE);
			}
			// optional
			// to set binary path of chrome
			String binaryPath = XMLUtils.getNodeText(configPath, BROWSER_BINARY);
			if (binaryPath.contains("{")) {
				binaryPath = StringUtils.replaceWithReflection(binaryPath, SYSTEM_UTILS, "{", "}");
			}
			try {
			chromeOptions.setBinary(binaryPath);
			}catch(Exception e) {
				
			}
			
			DesiredCapabilities capabilities = new DesiredCapabilities();
		    capabilities.setCapability("chrome.switches", Arrays.asList("--incognito"));
			//System.setProperty("webdriver.chrome.logfile", "C:\\chromedriver.log");
			//System.setProperty("webdriver.chrome.verboseLogging", "true");
			//LoggingPreferences logPrefs = new LoggingPreferences();
			//LOGGER.info("now setting log levels for desktop......");
			//logPrefs.enable(LogType.BROWSER, Level.INFO);
			//chromeOptions.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
			chromeOptions.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
			//chromeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			chromeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			driver = new ChromeDriver(service, chromeOptions);
		} catch (NoSuchElementException e) {
			LOGGER.info(e.getMessage());
		}
		return driver;

	}

	// to reutrn driver when Chrome Driver Service is used
	// Note: this method uses device emulation using device metrics
	// eg: width, heith, pixel ratio etc.

	@SuppressWarnings({ "unchecked", "deprecation" })
	public static WebDriver getDriver_Using_DeviceMetrics(ChromeDriverService service, String deviceWidth,
			String deviceHeight) throws Exception {
		try {
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.addArguments(NO_SANDBOX);
			chromeOptions.addArguments(DISABLE_GPU);
			String configPath = common.utilities.FileUtils.findFullPath(GlobalConstants.USER_DIR, CONFIG_XML);
			String headless = XMLUtils.getNodeText(configPath, HEADLESS_CONFIG);
			if (headless.equalsIgnoreCase("true")) {
				chromeOptions.addArguments(HEADLESS);
				LOGGER.info(LOGGER_HEADLESS_MODE);
			}

			String binaryPath = XMLUtils.getNodeText(configPath, BROWSER_BINARY);
			if (binaryPath.contains("{")) {
				binaryPath = StringUtils.replaceWithReflection(binaryPath, SYSTEM_UTILS, "{", "}");
			}
			chromeOptions.setBinary(binaryPath);
			// ******** Using Device Metrics***********
			HashMap deviceMetrics = new HashMap();
			deviceMetrics.put("width", Integer.valueOf(deviceWidth));
			deviceMetrics.put("height", Integer.valueOf(deviceHeight));
			// pixel ratio seems optional so commented for now
			HashMap mobileEmulation = new HashMap();
			mobileEmulation.put("deviceMetrics", deviceMetrics);
			// below key (userAgent) seems optional
			// code working with or without
			mobileEmulation.put("userAgent",
					"Mozilla/5.0 (Linux; Android 4.2.1; en-us; Nexus 5 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19");
			chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
			chromeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			chromeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			// ******** End Using Device Metrics***********
			driver = new ChromeDriver(service, chromeOptions);

		} catch (NoSuchElementException e) {
			LOGGER.info(e.getMessage());
		}
		return driver;

	}

	// to reutrn driver when Chrome Driver Service is used
		// Note: this method uses chrome on emulated device
		// user below devices as reference
		@SuppressWarnings("unchecked")
		public static WebDriver getDriver_On_EmulatedDevice(ChromeDriverService service, String deviceName)
				throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParserConfigurationException, SAXException, IOException {
			try {
				ChromeOptions chromeOptions = new ChromeOptions();
				String configPath = common.utilities.FileUtils.findFullPath(GlobalConstants.USER_DIR, CONFIG_XML);
				String headless = XMLUtils.getNodeText(configPath, HEADLESS_CONFIG);
				if (headless.equalsIgnoreCase("true")) {
					chromeOptions.addArguments(HEADLESS);
					LOGGER.info(LOGGER_HEADLESS_MODE);
				}
				chromeOptions.addArguments(NO_SANDBOX);
				chromeOptions.addArguments(DISABLE_GPU);
				// ******** Using Mobile Emulation Dictionary ***********
				HashMap mobileEmulation = new HashMap();
				mobileEmulation.put("deviceName", deviceName);
				chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
				chromeOptions.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
				chromeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				chromeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
				//--disable-dev-shm-usage
				chromeOptions.setCapability(CapabilityType.OVERLAPPING_CHECK_DISABLED, true);
				LoggingPreferences logPrefs = new LoggingPreferences();
				LOGGER.info("now setting log levels......");
				logPrefs.enable(LogType.BROWSER, Level.INFO);
				chromeOptions.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
				// ******** End Using Mobile Emulation Dictionary ***********
				String useSeleniumBox = XMLUtils.getNodeText(configPath, "UseSeleniumBox");
				if ((useSeleniumBox != null) && (useSeleniumBox.equalsIgnoreCase("true"))) {
					LOGGER.info("Tests running on SeleniumBox");
					// When running on SeleniumBox (grid)
					
					/*	chromeOptions.setCapability("e34:per_test_timeout_ms", 120000);*/
					ChromeOptions options = new ChromeOptions();
					options.setExperimentalOption("mobileEmulation", mobileEmulation);
					options.setCapability(CapabilityType.BROWSER_VERSION, "91");
					if (System.getProperty("token") != null) {
						LOGGER.info("Selenium Box token:"+System.getProperty("token"));
						options.setCapability("e34:token", System.getProperty("token"));
					}else {
						String token = XMLUtils.getNodeText(configPath, "HeadspinToken");
						options.setCapability("e34:token", token);
					}
					
					String seleniumBoxUrl = XMLUtils.getNodeText(configPath, "SeleniumBoxUrl");
					try {
						 driver = new RemoteWebDriver(new URL(seleniumBoxUrl), options);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				} else {
					LOGGER.info("Tests running on local machine");
					String binaryPath = XMLUtils.getNodeText(configPath, BROWSER_BINARY);
					if (binaryPath.contains("{")) {
						binaryPath = StringUtils.replaceWithReflection(binaryPath, SYSTEM_UTILS, "{", "}");
					}
					chromeOptions.setBinary(binaryPath);
					driver = new ChromeDriver(service, chromeOptions);				
				}
				Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
				String v = cap.getVersion();
				LOGGER.info("Chrome version: " + v);
			} catch (NoSuchElementException e) {
				LOGGER.info(e.getMessage());
			}
			return driver;
		}
	public static void InstantiateBrowser(String browserName, String url)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParserConfigurationException, SAXException, IOException {
		driver = getDriver(browserName);
		Waits.waitInSeconds(2);
		driver.get(url);
	}

	public void closeBrowser() {
		driver.close();
	}

	public static WebElement GetElement(WebDriver driver, final By by, int timeoutInSeconds) {

		if (timeoutInSeconds > 0) {
			WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
			wait.until(ExpectedConditions.presenceOfElementLocated(by));
		}
		return driver.findElement(by);
	}

	public static WebElement GetElement(WebDriver driver, By by) {
		return GetElement(driver, by, 10);
	}

	public static List<WebElement> GetElements(WebDriver driver, final By by, int timeoutInSeconds) {
		if (timeoutInSeconds > 0) {
			WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
		}
		return driver.findElements(by);
	}

	public static String getXPATHValue(String sKey) throws IOException {
		FileInputStream inputStream = new FileInputStream("src\\or\\OR.properties");
		Properties pro = new Properties();
		pro.load(inputStream);
		return pro.getProperty(sKey).trim();
	}

	public static ExpectedCondition<Boolean> presenceOfAllElementsLocatedBy(final By locator) {
		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(final WebDriver driver) {
				return !driver.findElements(locator).isEmpty();
			}
		};
	}

	public static ExpectedCondition<Boolean> pageToLoad() {
		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(final WebDriver webDriver) {
				return ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete");
			}
		};
	}

	public static ExpectedCondition<String> appearingOfWindowAndSwitchToIt(final String title) {
		return new ExpectedCondition<String>() {
			@Override
			public String apply(final WebDriver driver) {
				final String initialHandle = driver.getWindowHandle();
				for (final String handle : driver.getWindowHandles()) {
					if (needToSwitch(initialHandle, handle)) {
						driver.switchTo().window(handle);
						if (driver.getTitle().equals(title)) {
							return handle;
						}
					}
				}
				driver.switchTo().window(initialHandle);
				return null;
			}
		};
	}

	private static boolean needToSwitch(String initialHandle, String handle) {
		if (handle.isEmpty()) {
			return false;
		}
		return !initialHandle.equals(handle);
	}

	public WebElement getParentElement(WebElement element) {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return arguments[0].parentNode", element);
	}

	public int getTableHeaderColumnIndex(String columnName, List<WebElement> allHeaders) {
		int rowIndex = -1;
		for (int iterator = 0; iterator < allHeaders.size(); iterator++) {
			if (columnName.equals(allHeaders.get(iterator).getText().trim())) {
				rowIndex = iterator;
			}
		}
		return rowIndex;
	}

	public final WebElement findElementByNoThrow(final By locator) {
		try {
			return getDriver().findElement(locator);
		} catch (WebDriverException ignored) {
			LOGGER.info("[INFO] Element not found... Ignoring exception");
			return null;
		}
	}

	public static WebDriver getDriver() {
		return driver;
	}

	public static void setDriver(WebDriver driver) {
		BaseMethods.driver = driver;
	}

	public static void implicitWaitMethod() {
		BaseMethods.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	

	public static void maximizeWinDow() throws Exception {
		BaseMethods.driver.manage().window().maximize();
	}

	public static void elementTimeOut(String time) {
		BaseMethods.driver.manage().timeouts().implicitlyWait(Integer.parseInt(time), TimeUnit.SECONDS);
	}

	public static void pageLoadTimeOut(String time) {
		BaseMethods.driver.manage().timeouts().pageLoadTimeout(Integer.parseInt(time), TimeUnit.SECONDS);
	}

	public static void openURL(String url) {
		BaseMethods.driver.get(url);
	}

	public static void handleCertificateError() {
		if (BaseMethods.driver instanceof InternetExplorerDriver) {
			BaseMethods.driver.navigate().to("javascript:document.getElementById('overridelink').click()");
		}
	}

	public static void close() {
		try {
			BaseMethods.driver.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	public static int countOfElements(String locator) throws Exception {
		By byLocator = Object.elementLocator(locator);
		return BaseMethods.driver.findElements(byLocator).size();
	}

	// to click on web element
	public static void ClickElement(WebElement webElement) {
		webElement.click();

	}

	// To double click on Web Element
	public static void DoubleClickElement(WebElement webElement) {
		try {
			// create Actions object
			Actions act = new Actions(BaseMethods.driver);
			// Use DoubleClick method to double click on any element
			act.doubleClick(webElement).build().perform();
		} catch (NoSuchElementException e) {
			LOGGER.info(e.getMessage());
		}
	}

	// To keyin on Web Element
	public static void KeyInElement(WebElement webElement, String value) {
		try {
			webElement.sendKeys(value);
		} catch (Exception e) {
			LOGGER.info("Unable to key in value " + value + " on web element");
		}
	}

	// To take screen shot of page
	public static void captureScreenShot(WebDriver driver, String obj) throws IOException {
		File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshotFile, new File(obj));
	}

	public static String GetTimeStampValue() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyHHmm");
		String systime = dateFormat.format(timestamp);
		return systime;
	}

	public static int randInt(int min, int max) throws NoSuchAlgorithmException {
		// Usually this can be a field rather than a method variable
		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		Random random = SecureRandom.getInstanceStrong();
		int randomNum = random.nextInt((max - min) + 1) + min;
		return randomNum;
	}


	public static void waitForElement(WebDriver driver, final By by, int timeoutInSeconds) {

		WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	// over load function to have default timeoutinSeconds=10
	public static void waitForElement(WebDriver driver, By by) {
		waitForElement(driver, by, 10);
	}

	public static File takeScreenShots(String picture) {
		try {
			File temp = ((TakesScreenshot) BaseMethods.driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(temp,
					new File(GlobalConstants.USER_DIR + "/target/screenshots/" + File.separator + picture + ".jpeg"));
			return temp;

		} catch (IOException e) {
			LOGGER.error(e);

		}
		return null;
	}

	public static boolean isElementExists(By by) {
		boolean isExists = true;
		try {
			BaseMethods.driver.findElement(by);
		} catch (NoSuchElementException e) {
			isExists = false;
		}
		return isExists;
	}

	public static List<WebElement> selectRadioButton(String locator) throws Exception {
		By byLocator = Object.elementLocator(locator);
		List<WebElement> radioButtonList = BaseMethods.driver.findElements(byLocator);
		return radioButtonList;
	}

	public static String getSnap(WebDriver driver)
			throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParserConfigurationException, SAXException {
		String timeStamp = StringUtils.getCurrentTimeStamp("ddMMyyHHmmssSSS");
		String file = common.utilities.FileUtils.findFullPath(GlobalConstants.USER_DIR, CONFIG_XML);
		String screenShotPath = XMLUtils.getNodeText(file, "ScreenShotPath");
		String takeFullScreenShot = XMLUtils.getNodeText(file, "TakeFullScreenShot");
		String newPath;
		// if xpath contains {user.home} then use below code
		if (screenShotPath.contains("{")) {
			newPath = StringUtils.replaceWithReflection(screenShotPath, SYSTEM_UTILS, "{", "}");
		} else {
			newPath = screenShotPath;
		}
		// to create all parent directories if not already exist
		common.utilities.FileUtils.createDirectories(newPath);
		String snapPath = newPath + "\\" + timeStamp + ".png";

		// 3rd method : useful to view Jenkins report
		if (takeFullScreenShot.toLowerCase().contentEquals("true")) {
			// take the screenshot of the entire results page and save it to a
			// png file
			Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(0))
					.takeScreenshot(driver);
			ImageIO.write(screenshot.getImage(), "PNG", new File(snapPath));
		} else {
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(snapPath));
		}
		String snapPath1 = "file:///" + snapPath;
		return snapPath1;
	}

	// To highlight any webelement
	public static void highlightElement(WebDriver driver, WebElement element) throws InterruptedException {
		for (int i = 0; i < 2; i++) {
			JavascriptExecutor js = (JavascriptExecutor) driver;

			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
					"color: red; border: 2px dotted yellow;");
			Thread.sleep(100);

		}
	}

	public static void highlightElement(AppiumDriver driver, WebElement element) throws InterruptedException {
		for (int i = 0; i < 2; i++) {
			JavascriptExecutor js = driver;
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
					"color: red; border: 2px dotted yellow;");
			Thread.sleep(100);

		}
	}

	// Generating Alert Using Javascript Executor
	public static void generateAlert(WebDriver driver, String msg) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("alert('" + msg + "');");
		Waits.waitInSeconds(1);
		driver.switchTo().alert().accept();

	}

	// ******* testing purpose only *************

	// @Test
	public void test_killprocess() throws Exception {
		SystemUtils.killProcessByName("IEDriverServer.exe");
	}

}
