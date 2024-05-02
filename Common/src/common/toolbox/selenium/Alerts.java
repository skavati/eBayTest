package common.toolbox.selenium;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;

import common.baselib.BaseMethods;

public class Alerts {

	
	public void alertOk(WebDriver driver) throws Exception {
		driver.switchTo().alert().accept();

	}

	public String getAlertText(WebDriver driver) {
		return driver.switchTo().alert().getText();
	}

	
	public void alertCancel(WebDriver driver) throws Exception {
		driver.switchTo().alert().dismiss();
	}

	
	public void alertSendKeys(WebDriver driver, String text) throws Exception {
		Alert alert1 = driver.switchTo().alert();
		alert1.sendKeys(text);
	}

	// verify if alert with text exists
	public static boolean isAlertPresent(WebDriver driver, String text, Integer timeOutInSec) {
		try {
			for (int i = 0; i < timeOutInSec; i++) {
				Waits.waitInSeconds(1);
				if (driver.switchTo().alert().getText().equalsIgnoreCase(text)) {
					return true;
				}
			}

		} catch (Exception e) {
			return false;
		}
		return false;
	}

}