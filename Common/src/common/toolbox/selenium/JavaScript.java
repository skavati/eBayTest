package common.toolbox.selenium;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JavaScript {
	// to find elment by id using javascript
	public static WebElement findElementById(WebDriver driver, String id) {
		WebElement element = null;
		JavascriptExecutor js = (JavascriptExecutor) driver;
		element = (WebElement) js.executeScript("return document.getElementById('" + id + "');", element);
		return element;
	}

	// to find elment by xpath using javascript
	public static WebElement findElementByXpath(WebDriver driver, String xpath) {		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement element = (WebElement) js.executeScript(
				"return document.evaluate( '"+xpath+"' ,document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null ).singleNodeValue;");
		return element;
	}

	// to click on element using JavaScript
	public static void clickElement(WebDriver driver, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", element);
	}

	// To keyin on web element using javascript
	public static void KeyInElement(WebDriver driver, WebElement element, String value) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].value=" + value + ";", element);
	}

	// scrolltoview using javascript
	public static void scrollToView(WebDriver driver, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", element);

	}
	
	// scroll by up or down
	//jse.executeScript("window.scrollBy(0,-250)", "");
	public static void scrollBy(WebDriver driver,Integer scrollByNo) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		//js.executeScript("window.scrollBy(0,"+scrollByNo+")", "");
		js.executeScript("scroll(0, "+scrollByNo+");");
		

	}

	// To make element visible
	public static void makeElementVisibile(WebDriver driver, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		((JavascriptExecutor) driver)
				.executeScript("arguments[0].style.height='auto'; arguments[0].style.visibility='visible';", element);

	}
	
	// To highlight any webelement
		public static void highlightElement(WebDriver driver, WebElement element) throws InterruptedException {
			for (int i = 0; i < 2; i++) {
				JavascriptExecutor js = (JavascriptExecutor) driver;
				// js.executeScript("arguments[0].setAttribute('style',
				// arguments[1]);", element, "color: yellow; border: 2px solid
				// yellow;");
				js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
						"color: red; border: 2px dotted blue;");
				Thread.sleep(500);
				js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
				// Waits.waitInSeconds(1);
			}
		}

		// Generating Alert Using Javascript Executor
		public static void generateAlert(WebDriver driver, String msg) throws InterruptedException {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("alert('" + msg + "');");
			Waits.waitInSeconds(1);
			driver.switchTo().alert().accept();

		}

}
