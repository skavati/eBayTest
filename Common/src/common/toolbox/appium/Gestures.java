package common.toolbox.appium;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import org.apache.log4j.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import common.toolbox.selenium.Dropdown;
import common.toolbox.selenium.Element;
import common.toolbox.selenium.Waits;
import common.utilities.ImageUtils;
import common.utilities.StringUtils;
import common.utilities.XMLUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;

public class Gestures {
	private static final Logger LOGGER = LogManager.getLogger(Gestures.class);
	private static final String DIRECTION = "direction";
	private static final String ELEMENT = "element";
	private static final String TO_VISIBLE = "to visible";
	private static final String MOBILE_SCROLL = "mobile:scroll";
	private static final String MOBILE_TAP = "mobile:tap";
	private static final String MOBILE_DOUBLE_TAP = "mobile:double tap";
	private static final String RIGHT = "right";

	public static void tapOnElement_TouchAction(AppiumDriver<?> driver, WebElement element) {
		int x = element.getLocation().getX();
		int y = element.getLocation().getY();
		new TouchAction(driver).tap(PointOption.point(x, y)).perform();
	}

	public static void swipeLeftToRight(AppiumDriver<?> driver) {
		Dimension size = driver.manage().window().getSize();
		int endx = (int) (size.width * 0.8);
		int startx = (int) (size.width * 0.20);
		int starty = size.height / 2;
		// java-client: 6.0.0
		new TouchAction(driver).press(PointOption.point(startx, starty))
				.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1))).moveTo(PointOption.point(endx, starty))
				.release().perform();

	}

	public static void swipeRightToLeft(AppiumDriver<?> driver) {
		Dimension size = driver.manage().window().getSize();
		int startx = (int) (size.width * 0.95);
		int endx = (int) (size.width * 0.05);
		int starty = size.height / 2;

		// java-client:6.0.0
		new TouchAction(driver).press(PointOption.point(startx, starty))
				.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1))).moveTo(PointOption.point(endx, starty))
				.release().perform();

	}

	public static void swipeElementRight(AppiumDriver<?> driver, org.openqa.selenium.WebElement element) {
		// center point of element
		int startX = element.getLocation().getX() + (element.getSize().getWidth() / 2);
		int startY = element.getLocation().getY();
		// end point (x) of element
		Dimension size = driver.manage().window().getSize();
		int endX = (int) (size.width * 0.80);

		// java-client:6.0.0
		new TouchAction(driver).press(PointOption.point(startX, startY))
				.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1))).moveTo(PointOption.point(endX, startY))
				.release().perform();
	}

	// to swipe element up
	public static void swipeElementUp(AppiumDriver<?> driver, org.openqa.selenium.remote.RemoteWebElement element) {
		// center point of element
		int startX = element.getLocation().getX() + (element.getSize().getWidth() / 2);
		int startY = element.getLocation().getY();
		// end point (x) of element
		Dimension size = driver.manage().window().getSize();
		int endY = (int) (size.height * 0.30);// Note: 0.40 or 0.50 not working

		// java-client:6.0.0
		new TouchAction(driver).press(PointOption.point(startX, startY))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(100))).moveTo(PointOption.point(startX, endY))
				.release().perform();
	}

	public static void scrollDownToElement(AppiumDriver<?> driver, By locator) {
		org.openqa.selenium.JavascriptExecutor js = driver;
		HashMap<String, String> scrollObject = new HashMap<>();
		scrollObject.put(DIRECTION, "down");
		scrollObject.put(ELEMENT, ((RemoteWebElement) driver.findElement(locator)).getId());
		scrollObject.put(TO_VISIBLE, "true");
		js.executeScript(MOBILE_SCROLL, scrollObject);
	}

	public static void scrollDownToElement(AppiumDriver<?> driver,
			org.openqa.selenium.remote.RemoteWebElement element) {
		org.openqa.selenium.JavascriptExecutor js = driver;
		HashMap<String, String> scrollObject = new HashMap<>();
		scrollObject.put(DIRECTION, "down");
		// Alternatively
		scrollObject.put(ELEMENT, (element).getId());
		scrollObject.put(TO_VISIBLE, "true");
		js.executeScript(MOBILE_SCROLL, scrollObject);
	}

	public static void scrollUpToElement(AppiumDriver<?> driver, By locator) {
		org.openqa.selenium.JavascriptExecutor js = driver;
		HashMap<String, String> scrollObject = new HashMap<>();
		scrollObject.put(DIRECTION, "up");
		// below also working

		// Alternatively to scroll up to element until its visible
		scrollObject.put(ELEMENT, ((RemoteWebElement) driver.findElement(locator)).getId());
		scrollObject.put(TO_VISIBLE, "true");
		js.executeScript(MOBILE_SCROLL, scrollObject);
	}

	public static void scrollUpToElement(AppiumDriver<?> driver, org.openqa.selenium.WebElement element) {
		org.openqa.selenium.JavascriptExecutor js = driver;
		HashMap<String, String> scrollObject = new HashMap<>();
		scrollObject.put(DIRECTION, "up");

		// Alternatively
		scrollObject.put(ELEMENT, ((RemoteWebElement) element).getId());
		scrollObject.put(TO_VISIBLE, "true");
		js.executeScript(MOBILE_SCROLL, scrollObject);
	}

	// swipe up,down,left,right using iOS element
	public static void swipeToDirection_iOS_XCTest(AppiumDriver<?> driver, RemoteWebElement element, String direction) {
		try {
			JavascriptExecutor js = driver;
			HashMap<String, String> swipeObject = new HashMap<>();
			if (direction.equalsIgnoreCase("down")) {
				swipeObject.put(DIRECTION, "down");
			} else if (direction.equalsIgnoreCase("up")) {
				swipeObject.put(DIRECTION, "up");
			} else if (direction.equalsIgnoreCase("left")) {
				swipeObject.put(DIRECTION, "left");
			} else if (direction.equalsIgnoreCase(RIGHT)) {
				swipeObject.put(DIRECTION, RIGHT);
			}
			swipeObject.put(ELEMENT, element.getId());
			js.executeScript("mobile:swipe", swipeObject);

		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	// swipe up,down,left,right on iOS "Native" element using TouchAction class
	// until element is visible on screen
	public static void swipeToElement(AppiumDriver<?> driver, RemoteWebElement element, String direction) {
		try {
			// get screen size
			Dimension size = driver.manage().window().getSize();
			int startX = (int) (size.width * 0.50);
			int startY = (int) (size.height * 0.50);
			int endX = startX;
			int endY = startY;

			if (direction.equalsIgnoreCase("down")) {
				endY = (int) (size.height * 0.60);
			} else if (direction.equalsIgnoreCase("up")) {
				endY = (int) (size.height * 0.40);
			} else if (direction.equalsIgnoreCase("left")) {
				endX = (int) (size.width * 0.40);
			} else if (direction.equalsIgnoreCase(RIGHT)) {
				endX = (int) (size.width * 0.60);
			}

			while (element.isDisplayed()== false || element.getAttribute("visible").contentEquals("false")) {
				new TouchAction(driver).press(PointOption.point(startX, startY))
						.waitAction(WaitOptions.waitOptions(Duration.ofMillis(100)))
						.moveTo(PointOption.point(endX, endY)).release().perform();
			}
			Waits.waitInMilliSeconds(500);

		} catch (Exception e) {
			e.getStackTrace();
		}
	}

// swipe up or down when visisble attribute is always false for some native ios elements
	public static void swipeToElement_WhenVisibilityAlwaysFalse(AppiumDriver<?> driver, RemoteWebElement element,
			String direction) {
		try {
			// get screen size
			Dimension size = driver.manage().window().getSize();
			int startX = (int) (size.width * 0.50);
			int startY = (int) (size.height * 0.50);

			if (direction.equalsIgnoreCase("down")) {
				int endY = (int) (size.height * 0.60);
			} else if (direction.equalsIgnoreCase("up")) {
				int endY = (int) (size.height * 0.40);
			} else if (direction.equalsIgnoreCase("left")) {
				int endX = (int) (size.width * 0.40);
			} else if (direction.equalsIgnoreCase(RIGHT)) {
				int endX = (int) (size.width * 0.60);
			}
			// scroll down until target element location lesser than screen height
			int y = element.getLocation().getY();
			while (y > size.getHeight()) {
				int endY = (int) (size.height * 0.46);
				new TouchAction(driver).press(PointOption.point(startX, startY))
						.waitAction(WaitOptions.waitOptions(Duration.ofMillis(100)))
						.moveTo(PointOption.point(startX, endY)).release().perform();
				y = element.getLocation().getY();
			}

			Thread.sleep(500);

		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	// swipe up,down,left,right on iOS "Native" element using TouchAction class
	// until element is visible on screen
	// by extracting text of each scrolled image
	public static void swipeToElement_OCR(AppiumDriver<?> driver, String direction, String text) {
		try {
			// get screen size
			Dimension size = driver.manage().window().getSize();
			int startX = (int) (size.width * 0.50);
			int startY = (int) (size.height * 0.50);
			int endX = startX;
			int endY = startY;

			if (direction.equalsIgnoreCase("down")) {
				endY = (int) (size.height * 0.60);
			} else if (direction.equalsIgnoreCase("up")) {
				endY = (int) (size.height * 0.40);
			} else if (direction.equalsIgnoreCase("left")) {
				endX = (int) (size.width * 0.40);
			} else if (direction.equalsIgnoreCase(RIGHT)) {
				endX = (int) (size.width * 0.60);
			}

			String timeStamp = StringUtils.getCurrentTimeStamp("ddMMyyHHmmssSSS");
			String file = common.utilities.FileUtils.findFullPath(System.getProperty("user.dir"), "config.xml");
			String screenShotPath = XMLUtils.getNodeText(file, "ScreenShotPath");
			String newPath;
			// if xpath contains {user.home} then use below code
			if (screenShotPath.contains("{")) {
				newPath = StringUtils.replaceWithReflection(screenShotPath, "common.utilities.SystemUtils", "{", "}");
			} else {
				newPath = screenShotPath;
			}
			// to create all parent directories if not already exist
			common.utilities.FileUtils.createDirectories(newPath);
			String imgPath = newPath + "\\" + timeStamp + ".png";
			File scrFile = (driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(imgPath));
			LOGGER.info("Imgage path:" + imgPath);
			int i = 0;
			while (!ImageUtils.getImgText(imgPath).contains(text)) {
				LOGGER.info("Imgage text:" + ImageUtils.getImgText(imgPath));
				if (i >= 5) {
					break;
				}
				new TouchAction(driver).press(PointOption.point(startX, startY))
						.waitAction(WaitOptions.waitOptions(Duration.ofMillis(100)))
						.moveTo(PointOption.point(endX, endY)).release().perform();
				timeStamp = StringUtils.getCurrentTimeStamp("ddMMyyHHmmssSSS");
				imgPath = newPath + "\\" + timeStamp + ".png";
				scrFile = (driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(scrFile, new File(imgPath));
				LOGGER.info("Imgage path:" + imgPath);
				i++;
			}

		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	// Tap on element
	public static void tapOnElement_iOS(AppiumDriver<?> driver, RemoteWebElement element) {
		try {
			org.openqa.selenium.JavascriptExecutor js = driver;
			Map<String, Object> params = new HashMap<>();
			int centerx = element.getLocation().getX() + (element.getSize().getWidth() / 2);
			int centery = element.getLocation().getY() + (element.getSize().getHeight() / 2);
			LOGGER.info("Tapping at x:" + centerx);
			LOGGER.info("Tapping at y:" + centery);
			params.put("x", centerx);
			params.put("y", centery);
			params.put(ELEMENT, (element).getId());
			js.executeScript(MOBILE_TAP, params);
		} catch (Exception e) {
			e.getStackTrace();

		}
	}

	public static void tapOnElement_iOS(AppiumDriver<?> driver, RemoteWebElement element, Integer x, Integer y) {
		try {
			org.openqa.selenium.JavascriptExecutor js = driver;
			Map<String, Object> params = new HashMap<>();
			params.put("x", x);
			params.put("y", y);
			params.put(ELEMENT, (element).getId());
			js.executeScript(MOBILE_TAP, params);
			Thread.sleep(500);
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	// Tap on element using element parameter
	// Note: Dont use x and y parameter
	public static void doubleTapOnElement_iOS(AppiumDriver<?> driver, RemoteWebElement element) {
		try {
			org.openqa.selenium.JavascriptExecutor js = driver;
			Map<String, Object> params = new HashMap<>();
			params.put(ELEMENT, (element).getId());
			js.executeScript(MOBILE_DOUBLE_TAP, params);
			Thread.sleep(500);
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	// Tap on device screen using x,y coordinates only
// Note: Dont use element parameter
	public static void doubleTapOnElement_iOS(AppiumDriver<?> driver, Integer x, Integer y) {
		try {
			org.openqa.selenium.JavascriptExecutor js = driver;
			Map<String, Object> params = new HashMap<>();
			params.put("x", x);
			params.put("y", y);
			js.executeScript(MOBILE_DOUBLE_TAP, params);
			Thread.sleep(500);
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	// to get element location by x coordinate
	public static int getElementLocationByX(RemoteWebElement element) {
		return element.getLocation().getX() + (element.getSize().getWidth() / 2);
	}

	// to get element location by Y coordinate
	public static void searchElementLocationByY_IOS(AppiumDriver<?> driver, RemoteWebElement element,
			Integer deviceViewPortY, Integer startY) {
		Map<String, Object> params = new HashMap<>();
		JavascriptExecutor js = driver;
		Integer centreX = getElementLocationByX(element);
		Integer elementHeight = element.getSize().getHeight();
		LOGGER.info("Tapping x coordinate: " + centreX);
		for (int j = startY; j < deviceViewPortY; j = j + elementHeight) {
			params.put("x", centreX);
			params.put("y", j);
			params.put(ELEMENT, (element).getId());
			LOGGER.info("Searing for y coordinate: " + j);
			Waits.waitInSeconds(2);
			js.executeScript(MOBILE_TAP, params);
		}
	}

	// https://github.com/appium/appium/issues/3896
	public static void clickElement(WebElement webViewElement, AppiumDriver driver) throws InterruptedException {

		// We need to compare the view size of web and native.
		// First we gather [Information] of web view.
		// [Information] - Element coordinates of element and web view height
		// and width.
		JavascriptExecutor js = driver;
		// Web View Dimensions
		int screenWebViewWidth = ((Long) js.executeScript("return window.innerWidth || document.body.clientWidth"))
				.intValue();
		LOGGER.info("Screen webview width value calculated is :" + screenWebViewWidth);
		int screenWebViewHeight = ((Long) js.executeScript("return window.innerHeight || document.body.clientHeight"))
				.intValue();
		LOGGER.info("Screen webview height value calculated is :" + screenWebViewHeight);

		// Element Coordinates in WebView
		int elementwebViewX = webViewElement.getLocation().getX();
		int elementwebViewY = webViewElement.getLocation().getY();
		LOGGER.info("Element webviewX :" + elementwebViewX);
		LOGGER.info("Element webviewY :" + elementwebViewY);
		// Switching to Native view to use the native supported methods
		Driver.switchToNativeView(driver);
		double screenWidth = driver.manage().window().getSize().getWidth();
		double screenHeight = driver.manage().window().getSize().getHeight();
		// Service URL bar height is %
		// From the webview coordinates we will be calculating the native view
		// coordinates using the width and height.
		double elementNativeViewX = ((elementwebViewX * screenWidth) / screenWebViewWidth);
		double elementNativeViewY = ((elementwebViewY * screenHeight) / screenWebViewHeight);

		LOGGER.info("Element native view Y coordinate calculated as :" + elementNativeViewY);
		LOGGER.info("Element native view X: " + elementNativeViewX);
		tapOnCoordinates(elementNativeViewX, elementNativeViewY, driver);
		// Switching back to Web View
		Driver.switchToWebView(driver);
	}

	public static void tapOnCoordinates(final double x, final double y, AppiumDriver driver) {
		JavascriptExecutor executor = driver;
		Map map = new HashMap();
		map.put("tapCount", (double) 1);
		map.put("touchCount", (double) 1);
		map.put("duration", 0.5);
		map.put("x", (double) x);
		map.put("y", (double) y);
		executor.executeScript(MOBILE_TAP, map);
	}

		public static void dragAndDrop(AppiumDriver<?> driver, org.openqa.selenium.WebElement element1,
			org.openqa.selenium.WebElement element2) {
		// center point of element
		int startX = element1.getLocation().getX() + (element1.getSize().getWidth() / 2);
		int startY = element1.getLocation().getY() + (element1.getSize().getHeight() / 2);

		int endX = element2.getLocation().getX() + (element2.getSize().getWidth() / 2);
		int endY = element2.getLocation().getY() + (element2.getSize().getHeight() / 2);

		// java-client:6.0.0
		new TouchAction(driver).longPress(PointOption.point(startX, startY))
				.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1))).moveTo(PointOption.point(endX, endY))
				.release().perform();
	}

	public static void doubleTapTouchAction(AppiumDriver<?> driver, Integer x, Integer y) {
		new TouchAction(driver).press(PointOption.point(x, y)).release().perform().press(PointOption.point(x, y))
				.release().perform();

	}
	// select from dropdown list
	// Here input txt can be full txt or partial txt
	public static void selectFromDropDown(AppiumDriver driver, RemoteWebElement element, String txt,
			String defaultSelection, Boolean isPartialTxt) throws Exception {
		if (Driver.getPlatformName().equalsIgnoreCase("android")) {
			Dropdown.SelectElementByText(element, txt);
		} else {
			Gestures.swipeToElement(driver, element, "up");
			Gestures.tapOnElement_TouchAction(driver, element);
			RemoteWebElement pickerWheel = (RemoteWebElement) driver
					.findElementByClassName("XCUIElementTypePickerWheel");
			if (isPartialTxt) {
				@SuppressWarnings("unchecked")
				List<WebElement> accts = pickerWheel.findElements(By.className("XCUIElementTypeOther"));
				// LOGGER.info("From acct size:"+accts.size());
				outerloop: for (int i = 0; i < accts.size(); i++) {
					if (i == 0) {
						// Note:
						// on first tap, use below code to scroll up to 'Default selection'
						// Had to recreate 'PickerWheel' and get x and y coordinates
						// due to picker wheel tap skipping few dropdown elements on first tap
						pickerWheel = (RemoteWebElement) driver.findElementByClassName("XCUIElementTypePickerWheel");
						int x = pickerWheel.getLocation().getX() + pickerWheel.getSize().getWidth() / 2;
						int y = pickerWheel.getLocation().getY() + pickerWheel.getSize().getHeight() / 2
								+ element.getSize().getHeight() / 2;
						new TouchAction(driver).tap(PointOption.point(x, y)).perform();
						// use below code to get text of first tapped selection
						// scroll up and down the wheel to get text of first tapped selection
						pickerWheel = (RemoteWebElement) driver.findElementByClassName("XCUIElementTypePickerWheel");
						x = pickerWheel.getLocation().getX() + pickerWheel.getSize().getWidth() / 2;
						y = pickerWheel.getLocation().getY() + pickerWheel.getSize().getHeight() / 2
								+ element.getSize().getHeight() / 2;
						new TouchAction(driver).tap(PointOption.point(x, y)).perform();

						pickerWheel = (RemoteWebElement) driver.findElementByClassName("XCUIElementTypePickerWheel");
						x = pickerWheel.getLocation().getX() + pickerWheel.getSize().getWidth() / 2;
						y = pickerWheel.getLocation().getY() + pickerWheel.getSize().getHeight() / 2
								- element.getSize().getHeight() / 2;
						new TouchAction(driver).tap(PointOption.point(x, y)).perform();
						if (pickerWheel.getText().contains(txt)) {
							break outerloop;
						}
						while (!pickerWheel.getText().contentEquals(defaultSelection)) {
							pickerWheel = (RemoteWebElement) driver
									.findElementByClassName("XCUIElementTypePickerWheel");
							x = pickerWheel.getLocation().getX() + pickerWheel.getSize().getWidth() / 2;
							y = pickerWheel.getLocation().getY() + pickerWheel.getSize().getHeight() / 2
									- element.getSize().getHeight() / 2;

							new TouchAction(driver).tap(PointOption.point(x, y)).perform();
							if (pickerWheel.getText().contains(txt)) {
								break outerloop;
							}
						}

					}

					pickerWheel = (RemoteWebElement) driver.findElementByClassName("XCUIElementTypePickerWheel");
					int x = pickerWheel.getLocation().getX() + pickerWheel.getSize().getWidth() / 2;
					int y = pickerWheel.getLocation().getY() + pickerWheel.getSize().getHeight() / 2
							+ element.getSize().getHeight() / 2;

					new TouchAction(driver).tap(PointOption.point(x, y)).perform();
					LOGGER.info("Picker wheel text while scroll up:" + pickerWheel.getText());
					if (pickerWheel.getText().contains(txt)) {
						LOGGER.info("Target account found from dropdown while scrolling down");
						break;
					}
				}
			} else {
				pickerWheel.sendKeys(txt);
			}
			driver.findElementById("Done").click();
		}
	}

	public static void selectFromDropDown(AppiumDriver driver, RemoteWebElement element, String txt) throws Exception {
		selectFromDropDown(driver, element, txt, "", false);
	}
}
