package common.toolbox.selenium;

import java.util.NoSuchElementException;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class ComboBox {
	// Eg: to search by a value eg: address then select it in a combobox
	// eg: Update mailing address
	public static void searchAndSelect(WebDriver driver, WebElement comboBoxElement, String value) throws Exception {
		try {
			JavaScript.scrollToView(driver, comboBoxElement);
			Waits.waitInSeconds(1);
			comboBoxElement.click();
			Waits.waitInSeconds(1);
			comboBoxElement.sendKeys(Keys.HOME,Keys.SHIFT,Keys.END,Keys.DELETE);
			Waits.waitInSeconds(1);
			// Enter value and highlight to select value from wheel as expected
			//comboBoxElement.sendKeys(value);
			Action.sendKeys(driver, comboBoxElement, value);
			Waits.waitInSeconds(1);
			Action.sendKeys(driver, comboBoxElement, Keys.ARROW_DOWN);
			Waits.waitInMilliSeconds(1);
			Action.sendKeys(driver, comboBoxElement, Keys.ENTER);
			Waits.waitInMilliSeconds(1);
			Action.sendKeys(driver, comboBoxElement, Keys.TAB);
		    Waits.waitInMilliSeconds(1);
		    
		    Integer i=0;
			while (!comboBoxElement.getAttribute("value").contentEquals(value)) {
				Action.sendKeys(driver, comboBoxElement, Keys.ARROW_DOWN);
				Waits.waitInMilliSeconds(1);
				Action.sendKeys(driver, comboBoxElement, Keys.ENTER);
				Waits.waitInMilliSeconds(1);
				Action.sendKeys(driver, comboBoxElement, Keys.TAB);
			    Waits.waitInMilliSeconds(1);
			    if (comboBoxElement.getAttribute("value").contentEquals(value)) {
			    	 	break;
			    }else {
			    	comboBoxElement.sendKeys(Keys.HOME,Keys.SHIFT,Keys.END,Keys.DELETE);
					Waits.waitInSeconds(1);
					Action.sendKeys(driver, comboBoxElement, value);
					Waits.waitInSeconds(1);
			    }
			    i++;
			    if (i>20) {
			    	break;
			    }
			}
			Waits.waitInSeconds(1);
		} catch (Exception e) {
			throw new Exception("Unable to search and select a value:" + value);

		}

	}
}