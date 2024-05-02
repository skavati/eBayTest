package common.toolbox.selenium;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;


public class Link {

	public static boolean click(String strLogicalName, String strLocatorType, String strLocatorValue, WebDriver driver)
			throws Exception {
		boolean blResult = false;
		WebElement element = Object.getElement(driver, strLocatorType, strLocatorValue);
		if (element != null) {
			if (element.isDisplayed()) {
				if (element.isEnabled()) {
					try {
						element.click();
						blResult = true;
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

	public static boolean JSClick(String strLogicalName, String strLocatorType, String strLocatorValue,
			WebDriver driver) throws Exception {
		boolean blResult = false;
		WebElement element = Object.getElement(driver, strLocatorType, strLocatorValue);
		if (element != null) {
			if (element.isDisplayed()) {
				if (element.isEnabled()) {
					try {
						((JavascriptExecutor) driver).executeScript("arguments[0].click", element);
						blResult = true;
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


	public static boolean verify(String strLogicalName, WebElement element, String strValue, WebDriver driver)
			throws Exception {
		boolean blResult = false;
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

	public static boolean verify(String strLogicalName, WebElement element, WebDriver driver) throws Exception {

		boolean blResult = false;
		if (element != null) {
			if (element.isDisplayed()) {
				try {
					blResult = true;
				} catch (Exception e) {
				}
			} else {
			}
		} else {
		}
		return blResult;
	}

	public static boolean mouseDown(String strLogicalName, WebElement element, int inWait, WebDriver driver)
			throws Exception {
		boolean blResult = false;
		if (element != null) {
			if (element.isDisplayed()) {
				if (element.isEnabled()) {
					try {
						Actions actions = new Actions(driver);
						actions.click(element).build().perform();
						try {
							Waits.waitInSeconds(inWait);
						} catch (Exception e) {
						}
						blResult = true;
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

}
