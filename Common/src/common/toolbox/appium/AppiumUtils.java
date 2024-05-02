package common.toolbox.appium;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.log4j.*;
import org.junit.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;

import com.google.common.collect.ImmutableMap;

import common.toolbox.selenium.Action;
import common.toolbox.selenium.Waits;
import common.utilities.FileUtils;
import common.utilities.StringUtils;
import common.utilities.XMLUtils;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.android.nativekey.PressesKey;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.windows.WindowsDriver;

public class AppiumUtils {
	private static final Logger LOGGER = LogManager.getLogger(AppiumUtils.class);

	public static String getSecureCode() {
		/*
		 * String configPath = FileUtils.findFullPath(System.getProperty("user.dir"),
		 * "config.xml"); //String port = XMLUtils.getNodeText(configPath, "Port");
		 * String port = "4723"; String udid = XMLUtils.getNodeText(configPath, "UDID");
		 * String iPAddress = XMLUtils.getNodeText(configPath, "IPAddress"); String
		 * secureCode = ""; try { DesiredCapabilities capabilities = new
		 * DesiredCapabilities(); capabilities.setCapability("platformName", "Android");
		 * capabilities.setCapability("deviceName", "Pixel 4");
		 * capabilities.setCapability("noReset", true);
		 * capabilities.setCapability(MobileCapabilityType.UDID, udid);
		 * LOGGER.info("udid: " + udid); LOGGER.info("ipaddress: " + iPAddress);
		 * LOGGER.info("port: " + port); // Use below line to stop getting //
		 * error:org.openqa.selenium.UnsupportedCommandException: // unknown command:
		 * Cannot call non W3C standard command while in W3C mode
		 * capabilities.setCapability("appium:chromeOptions", ImmutableMap.of("w3c",
		 * false)); AndroidDriver driver = null; driver = new AndroidDriver(new
		 * URL("http://" + iPAddress + ":" + port + "/wd/hub"), capabilities);
		 * LOGGER.info("Android driver launched successfully");
		 * 
		 * // Below block working but with inconsistent
		 * 
		 * Waits.waitInSeconds(1); ((AndroidDriver) driver).openNotifications();
		 * LOGGER.info("Notification open successfully"); Waits.waitInSeconds(2);
		 * //LOGGER.info("Page source" + driver.getPageSource()); WebElement sms =
		 * driver.findElementByXPath("//android.widget.Button[contains(@text,'Copy')]");
		 * LOGGER.info("SMS: " + sms.getText()); secureCode =
		 * StringUtils.GetNumericString(sms.getText()); LOGGER.info("Secure code:" +
		 * secureCode); Waits.waitInSeconds(1); driver.navigate().back();
		 * 
		 * // Below code will click on 'Messages' icon on home screen and get sms code
		 * // to go to home screen use below line ((PressesKey) driver).pressKey(new
		 * KeyEvent(AndroidKey.HOME)); Waits.waitInSeconds(1); WebElement msg =
		 * driver.findElementByXPath("//android.widget.TextView[@text='Messages']");
		 * msg.click(); WebElement sms = driver.findElementById(
		 * "com.google.android.apps.messaging:id/suggestion_container");
		 * LOGGER.info("SMS: " + sms.getAttribute("content-desc")); secureCode =
		 * StringUtils.GetNumericString(sms.getAttribute("content-desc"));
		 * LOGGER.info("Secure code:" + secureCode); Waits.waitInSeconds(1);
		 * driver.navigate().back();
		 * 
		 * } catch (Exception e) { LOGGER.info("Failed to launch android driver" + e); }
		 */
		try {
			String configPath = FileUtils.findFullPath(System.getProperty("user.dir"), "config.xml");
			String headspinToken = XMLUtils.getNodeText(configPath, "HeadspinToken");
			String headspinKeystore = FileUtils.findFullPath(System.getProperty("user.dir"), "headspin");
			System.setProperty("javax.net.ssl.trustStore", headspinKeystore);
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("platformName", "android");
			capabilities.setCapability("deviceName", "Samsung S9");
			capabilities.setCapability(MobileCapabilityType.UDID, "23bc414b16037ece");
			capabilities.setCapability("headspin:appiumVersion", "1.20.2");
			capabilities.setCapability("headspin:useAppiumUnlock", true);
			capabilities.setCapability("headspin:controlLock", true);
			AndroidDriver driver = new AndroidDriver(new URL(
					"https://hswestpac-au-syd-0-proxy-1-lin.srv.westpac.com.au:7001/v0/" + headspinToken + "/wd/hub"),
					capabilities);
			LOGGER.info("Android driver launched successfully");
			((PressesKey) driver).pressKey(new KeyEvent(AndroidKey.HOME));
			Waits.waitInSeconds(1);
			WebElement msg = driver.findElementByXPath("//android.widget.TextView[@text='Messages']");
			msg.click();
			Waits.waitInSeconds(1);
			WebElement sms = driver.findElementByXPath(
					"//android.widget.TextView[@resource-id='com.samsung.android.messaging:id/text_content' and contains(@text,'Code')]");
			LOGGER.info("SMS: " + sms.getAttribute("text"));
			String secureCode = StringUtils.GetNumericString(sms.getAttribute("text")).substring(0, 6);
			LOGGER.info("Secure code:" + secureCode);
			Waits.waitInSeconds(1);
			driver.navigate().back();
			return secureCode;
		} catch (Exception e) {
			LOGGER.info(e);
		}
		return null;

	}

	// To upload a PDF or image document 
	public static void uploadDocument(String winAppDriverPath,String docFullPath) {
		try {
			File file = new File(docFullPath);
			Desktop desktop = Desktop.getDesktop();
			desktop.open(new File(winAppDriverPath));
			Waits.waitInSeconds(3);
			DesiredCapabilities desktopCapabilities = new DesiredCapabilities();
			desktopCapabilities.setCapability("platformName", "Windows");
			desktopCapabilities.setCapability("deviceName", "WindowsPC");
			desktopCapabilities.setCapability("platformVersion", 10);
			desktopCapabilities.setCapability("app", "Root");
			WindowsDriver driver = new WindowsDriver(new URL("http://127.0.0.1:4723"), desktopCapabilities);
			WebElement editBar = driver.findElementByAccessibilityId("1148");
			editBar.sendKeys(FileUtils.getParentDirectoryPath(docFullPath));
			Waits.waitInSeconds(1);
			editBar.sendKeys(Keys.ENTER);
			Waits.waitInSeconds(2);
			Action.sendKeys(driver, editBar, file.getName()+Keys.ENTER);
			Waits.waitInSeconds(2);		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
