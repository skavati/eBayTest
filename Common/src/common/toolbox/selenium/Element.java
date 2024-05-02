package common.toolbox.selenium;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import common.utilities.StringUtils;
import io.appium.java_client.AppiumDriver;

public class Element {
	public static final long DEFAULT_TIMEOUT_SECONDS = 30;
	private static long timeout = DEFAULT_TIMEOUT_SECONDS;
	private static final Logger LOGGER = LogManager.getLogger(Element.class);

	public static boolean verify(String strLogicalName, String strLocatorType, String strLocatorValue, String strValue,
			WebDriver driver) throws Exception {
		boolean blResult = false;
		WebElement element = Object.getElement(driver, strLocatorType, strLocatorValue);
		if (element != null) {
			if (element.isDisplayed()) {
				if (element.isEnabled()) {
					try {
						String textValue = element.getText();
						if (textValue.equalsIgnoreCase(strValue)) {
							blResult = true;
						}
					} catch (Exception e) {
					}
				} else {
				}
			} else {
			}
		} else {
		}
		return blResult;
	}

	

	
	public static boolean verify(WebDriver driver, String strLocatorType, String strLocatorValue) throws Exception {
		boolean blResult = false;

		WebElement element = Object.getElement(driver, strLocatorType, strLocatorValue);

		if (!(element == null)) {
			if (element.isDisplayed()) {
				try {
					blResult = true;
					highlightElement(driver, strLocatorType, strLocatorValue);

				} catch (Exception e) {
				}
			} else {
			}
		} else {
		}

		return blResult;
	}

	public static boolean notVerify(WebDriver driver, String strLocatorType, String strLocatorValue) throws Exception {
		boolean blResult = false;

		WebElement element = Object.getElement(driver, strLocatorType, strLocatorValue);

		if (element == null) {
			blResult = true;
		} else {
		}

		return blResult;
	}

	public static boolean highlightElement(WebDriver driver, String strLocatorType, String strLocatorValue)
			throws Exception {
		boolean blResult = true;
		WebElement element = Object.getElement(driver, strLocatorType, strLocatorValue);

		for (int i = 0; i <= 5; i++) {

			// String originalStyle = element.getAttribute("style");
			JavascriptExecutor js = (JavascriptExecutor) driver;

			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
					"background: grey; border: 2px solid grey;");

			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");

		}
		return blResult;
	}

	public static boolean dragDropElement(String strFromLogicalName, String strToLogicalName, WebDriver driver,
			String strFromLocatorType, String strFromLocatorValue, String strToLocatorType, String strToLocatorValue) {
		boolean blResult = false;
		try {
			WebElement fromElement = Object.getElement(driver, strFromLocatorType, strFromLocatorValue);
			WebElement toElement = Object.getElement(driver, strToLocatorType, strToLocatorValue);

			Actions action = new Actions(driver);
			Action dragDrop = action.clickAndHold(fromElement).moveToElement(toElement).release(toElement).build();
			dragDrop.perform();

		} catch (Exception e) {
		}
		return blResult;

	}

	public static boolean isElementPresent(WebDriver driver, By by, Integer delay)
			throws org.openqa.selenium.NoSuchWindowException {
		boolean isPresent = true;
		try {
			// LOGGER.info("Fucntion :isElementPresent:Before
			// 'implicitWait'");
			driver.manage().timeouts().implicitlyWait(delay, TimeUnit.SECONDS);
			// search for elements and check if list is empty
			// LOGGER.info("Fucntion :isElementPresent:Before
			// findElements");
			if (driver.findElements(by).size() == 0) {
				// LOGGER.info("Fucntion :isElementPresent:Element
				// size='0'");
				isPresent = false;
			}
		} catch (WebDriverException e) {
			// LOGGER.info(e);
			isPresent = false;
		}
		return isPresent;
	}

	// method overloading
	public static boolean isElementPresent(WebDriver driver, By by) {
		// to set wait to 0 sec otherwise driver may already be using
		// AjaxElementLocatorFactory delay of 10 sec from page factory
		return isElementPresent(driver, by, 0);
	}

	public static boolean isElementDisappear(WebDriver driver, By by, Integer delayInSec) {
		boolean isDisappear = false;
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		for (int i = 0; i < delayInSec; i++) {
			// search for elements and check if list is empty
			if (driver.findElements(by).size() == 0) {
				return true;
			}
		}
		return isDisappear;
	}

	// method overloading for isElementDisappear
	public static boolean isElementDisappear(WebDriver driver, By by) {
		return isElementDisappear(driver, by, 0);
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public static boolean isElementPresentAfterPageLoad(WebDriver driver, By by) throws InterruptedException {
		boolean isPresent = true;
		waitForLoad(driver);
		// search for elements and check if list is empty
		if (driver.findElements(by).isEmpty()) {
			isPresent = false;
		}

		// rise back implicitly wait time
		driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
		return isPresent;
	}

	public static void waitForLoad(WebDriver driver) {
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver wd) {
				// this will tell if page is loaded
				return "complete".equals(((JavascriptExecutor) wd).executeScript("return document.readyState"));
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		// wait for page complete
		// wait.until(pageLoadCondition);
		// lower implicitly wait time
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
	}

	public static Boolean IsPageReady(WebDriver driver) {
		Boolean pageReady = false;
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// Initially bellow given if condition will check ready state of page.
		if (js.executeScript("return document.readyState").toString().equals("complete")) {
			LOGGER.info("Page Is loaded.");
			pageReady = true;
			return pageReady;
		}
		// This loop will rotate for 25 times to check If page Is ready after
		// every 1 second.
		// You can replace your value with 25 If you wants to Increase or
		// decrease wait time.
		for (int i = 0; i < 25; i++) {
			try {
				Waits.waitInSeconds(1);
			} catch (Exception e) {
			}
			// To check page ready state.
			if (js.executeScript("return document.readyState").toString().equals("complete")) {
				pageReady = true;
				return pageReady;
			}
		}
		return pageReady;
	}

	public static Boolean waitForJStoLoad(WebDriver driver) {

		WebDriverWait wait = new WebDriverWait(driver, 30);
		final JavascriptExecutor js = (JavascriptExecutor) driver;

		// wait for jQuery to load
		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					return ((Long) js.executeScript("return jQuery.active") == 0);
				} catch (Exception e) {
					return true;
				}
			}
		};

		// wait for Javascript to load
		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return js.executeScript("return document.readyState").toString().equals("complete");
			}
		};

		// return wait.until(jQueryLoad) && wait.until(jsLoad);
		return false;
	}

	// Enter value character by character
	public void TypeInField(WebElement element, String value) throws InterruptedException {
		String val = value;
		element.clear();

		for (int i = 0; i < val.length(); i++) {
			char c = val.charAt(i);
			String s = new StringBuilder().append(c).toString();
			element.sendKeys(s);
			Thread.sleep(100);
		}
	}

	// To retry click on element
	// Helps when first click didn't work
	public static void retryClick(WebDriver driver, WebElement element, Integer retryNo) {
		int attempt = 0;
		boolean staleElement = true;
		while (staleElement && attempt < retryNo) {
			try {
				attempt++;
				JavaScript.clickElement(driver, element);

				staleElement = false;
			} catch (Exception e) {
				staleElement = true;
			}
		}
		LOGGER.info("Element clicked in an attempt of " + attempt);
	}

	// method overload retryClick
	public static void retryClick(WebDriver driver, WebElement element) {
		retryClick(driver, element, 2);
	}

	// To retry sendKeys on textbox
	// Helps when first sendKeys didn't work
	public static void retrySendKeys(WebDriver driver, WebElement textBox, String value, Integer retryNo) {
		boolean staleElement = true;
		int attempt = 0;
		while (!textBox.getAttribute("value").contains(value)) {
			try {
				common.toolbox.selenium.Action.click(driver, textBox);
				textBox.clear();
				common.toolbox.selenium.Action.sendKeys(driver, textBox, value);
				attempt++;
				if (attempt == retryNo) {
					break;
				}
			} catch (Exception e) {

			}
		}
		LOGGER.info("Value entered in an attempt of " + attempt);
	}

	// method overload for retrysendKeys
	public static void retrySendKeys(WebDriver driver, WebElement element, String value) {
		retrySendKeys(driver, element, value, 2);
	}

	// To retry select text from dropDown
	// Helps when first selection from dropdown didnt work
	public static void retrySelect(WebDriver driver, WebElement dropDown, String value, Integer retryNo) {
		boolean staleElement = true;
		int attempt = 0;
		Select select = new Select(dropDown);
		while (!select.getFirstSelectedOption().getText().trim().contains(value)) {
			try {
				Dropdown.SelectElementByText(dropDown, value);
				attempt++;
				if (attempt == retryNo) {
					break;
				}
			} catch (Exception e) {

			}
		}
		LOGGER.info("Value selected from dropdown in an attempt of " + attempt);
	}

	// method overload for retrysendKeys
	public static void retrySelect(WebDriver driver, WebElement dropDown, String value) {
		retrySelect(driver, dropDown, value, 2);
	}

	// wait until context found or time elapsed
	public static void waitForContext(AppiumDriver<?> driver, Integer waitInSec) {
		for (int i = 0; i < waitInSec; i++) {
			try {
				try {
					if (driver.getContextHandles().size() > 0) {
						LOGGER.info("Context found.");
						break;

					}
				} catch (WebDriverException e) {

				}
			} catch (NoSuchWindowException e) {

			}
		}

	}

	// To verify if all child list items matched under single ancestor\parent
	// works well for all tiles and its child elements
	public static boolean verifyListItems(WebDriver driver, String parentXpath, String[] childXpaths) {
		try {
			Waits.waitInSeconds(2);
			Boolean found = false;
			List<WebElement> parents = driver.findElements(By.xpath(parentXpath));
			for (WebElement parent : parents) {
				for (String childXpath : childXpaths) {
					Integer childSize = parent.findElements(By.xpath(childXpath)).size();
					if (childSize > 0) {
						found = true;
					}else {
						found = false;
						// go to next parent or tile
						break;
					}
				}
				if (found) {
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
}
