package common.toolbox.selenium;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

public class Action {
	// to send keys using Selenium actions
	public static void sendKeys(WebDriver driver, WebElement element,
			Keys keys) {
		Actions action = new Actions(driver);
		action.sendKeys(element, keys).build().perform();
	}

	// to enter text using action class
	public static void sendKeys(WebDriver driver, WebElement element,
			String txt) {
		Actions action = new Actions(driver);
		action.moveToElement(element).sendKeys(txt).build().perform();

	}

	public static void keyInWithCoordinates(WebDriver driver,
			WebElement element, String value, Integer x, Integer y) {
		Actions action = new Actions(driver);
		action.moveToElement(element, x, y).sendKeys(value).build().perform();
	}

	// to double click on webelemet
	public static void doubleClick(WebDriver driver, WebElement element) {
		Actions action = new Actions(driver);
		action.moveToElement(element).doubleClick().build().perform();

	}

	// to click on web elemet
	public static void click(WebDriver driver, WebElement element) {
		try {
			Actions action = new Actions(driver);
			action.moveToElement(element).build().perform();
			action.click(element).build().perform();
		} catch (StaleElementReferenceException e) {
		}
	}

	// Move to the element using Selenium actions
	public static void moveToElement(WebDriver driver, WebElement element) {
		Actions action = new Actions(driver);
		action.moveToElement(element).build().perform();
	}

	// Right Click using Selenium actions
	public static void rightClick(WebDriver driver, WebElement element) {
		Actions action = new Actions(driver);
		action.contextClick(element).perform();
		}

	// drag and drop using Selenium actions
	public static void dragAndDrop(WebDriver driver, WebElement fromElement,
			WebElement toElement) {
		Actions action = new Actions(driver);
		org.openqa.selenium.interactions.Action dragDrop = action
				.dragAndDrop(fromElement, toElement).build();
		dragDrop.perform();
	}

	// drag and drop by certain pixels (x,y)
	public static void dragAndDropBy(WebDriver driver, WebElement element,
			Integer x, Integer y) {
		Actions action = new Actions(driver);
		action.dragAndDropBy(element, x, y).build().perform();

	}
	// Actions builder = new Actions(driver);
	// builder.moveToElement(knownElement, 10, 25).click().build().perform();
	public static void clickWithCoordinates(WebDriver driver,
			WebElement element, Integer x, Integer y) {
		Actions action = new Actions(driver);
		action.moveToElement(element, x, y).click().build().perform();
	}

	// ********************** testing purpose only ************************

}
