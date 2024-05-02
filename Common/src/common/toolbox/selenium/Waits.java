package common.toolbox.selenium;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;

public class Waits {
	public static void waitInSeconds(int inSecs) {
		try {
			long now = Instant.now().getEpochSecond();
			long elapsedTime = now+inSecs;
			while (now < elapsedTime) {
				now = Instant.now().getEpochSecond();
			}
		} catch (Exception e) {

		}
	}

	public static void waitInMilliSeconds(int milliSecs) {
		try {
			long now = Instant.now().toEpochMilli();
			long elapsedTime = now + milliSecs;
			while (now < elapsedTime) {
				now = Instant.now().toEpochMilli();
			}
		} catch (Exception e) {

		}
	}

	

	
	public static void waitForElementToBeVisible(WebDriver driver, WebElement element, int waitInSec) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			WebDriverWait wait = new WebDriverWait(driver, waitInSec);
			wait.until(ExpectedConditions.visibilityOf(element));
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		} catch (Exception e) {

		}
	}

	public static void waitForElementToBeVisible(WebDriver driver, WebElement element) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.visibilityOf(element));
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	public static void waitForElementToBeClickable(WebDriver driver, WebElement element, Integer WaitInSec) {

		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		WebDriverWait wait = new WebDriverWait(driver, WaitInSec);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

	}

	public static void appiumWaitForElementToBeVisible(AppiumDriver driver, WebElement element) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	public static void waitForElementsToBeVisible(WebDriver driver, List<WebElement> element) {
		WebDriverWait wait = new WebDriverWait(driver, 60);
		

		wait.until(ExpectedConditions.visibilityOfAllElements(element));
	}

	

	public static void waitForElementsToBeVisible(WebDriver driver, List<WebElement> element,int timeInSec) {
		WebDriverWait wait = new WebDriverWait(driver, timeInSec);
		

		wait.until(ExpectedConditions.visibilityOfAllElements(element));
	}

	// Added @14/03/2018for waiting for URL to match with the expected one for
	// external link features

	public static void waitForURLContains(WebDriver driver, String expectedUrl) {
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.urlContains(expectedUrl));
	}

	// Added @19/1/2018 for waiting the till invisibility of element:
	public static void waitForElementToBeinvisibil(WebDriver driver, WebElement element, int waitInSec) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		WebDriverWait wait = new WebDriverWait(driver, waitInSec);
		wait.until(ExpectedConditions.invisibilityOfElementLocated((By) element));

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	public static void waitForElementToBeinvisibil(WebDriver driver, By by, int waitInSec) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		WebDriverWait wait = new WebDriverWait(driver, waitInSec);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(by));

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	public static boolean waitForFrame(WebDriver driver, String strLogicalName, int inFrameIndex, int inTime) {
		boolean blResult = false;
		int inElapsedTime = 0;

		while (inElapsedTime <= inTime && !(blResult)) {

			try {
				driver.switchTo().frame(inFrameIndex);
				blResult = true;
				break;
			} catch (Exception e) {
			}

			inElapsedTime++;
		}

		return blResult;
	}

	public static boolean waitForFrame(WebDriver driver, String strLogicalName, String strFrameIDorName, int inTime) {
		boolean blResult = false;
		int inElapsedTime = 0;

		while (inElapsedTime <= inTime && !(blResult)) {

			try {
				driver.switchTo().frame(strFrameIDorName);
				blResult = true;
				break;
			} catch (Exception e) {
			}

			inElapsedTime++;
		}

		return blResult;
	}

	// to set implicit wait in seconds
	public static void setImplicitWait(WebDriver driver, Integer delay) {
		driver.manage().timeouts().implicitlyWait(delay, TimeUnit.SECONDS);
	}

	// For stale element: trying attempts to click on element if not available
	// first time
	public static boolean retryStaleElementClick(WebElement element, Integer attemptNo) {
		boolean result = false;
		int attempts = 0;
		while (attempts < attemptNo) {
			try {
				element.click();
				result = true;
				break;
			} catch (StaleElementReferenceException e) {
				waitInSeconds(1);
			}
			attempts++;
		}
		return result;
	}

	public static boolean retryStaleElementClick(WebElement element) {
		return retryStaleElementClick(element, 3);
	}

	public static void waitForWindowTitle(WebDriver driver, String title, int waitForSec) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		WebDriverWait wait = new WebDriverWait(driver, waitForSec);
		wait.until(ExpectedConditions.titleContains(title));
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

}
