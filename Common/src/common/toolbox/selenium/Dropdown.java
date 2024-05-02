package common.toolbox.selenium;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;

import com.google.common.base.Predicate;

import common.baselib.BaseMethods;
import common.utilities.StringUtils;

public class Dropdown {

	private static BaseMethods baseMethods;

	// to select an element from given list of selection box
	public static void SelectElementByValue(WebElement WebElement, String Value) {
		try {
			Select select = new Select(WebElement);
			select.selectByValue(Value);
		} catch (NoSuchElementException e) {
			System.out.println("Unable to select value " + Value);

		}

	}

	// to select an element by index
	public static void SelectElementByIndex(WebElement WebElement, String index) {
		try {
			Select select = new Select(WebElement);
			select.selectByIndex(Integer.parseInt(index));
		} catch (NoSuchElementException e) {
			System.out.println("Unable to select by index " + index);

		}

	}

	// Below code works for full text or partial text of dropdown elements
	public static void SelectElementByText(WebElement WebElement, String text) throws Exception {
		Select select = new Select(WebElement);		
		List<WebElement> elements = select.getOptions();
		int i = 0;
		text=text.toLowerCase();
		for (WebElement element : elements) {
			String str_lowercase_text = element.getText().trim().toLowerCase();
			if (str_lowercase_text.contains(text) || element.getText().trim().equalsIgnoreCase(text)) {
				select.selectByIndex(i);				
				break;
			}
			i++;
		}
	}
	
	
	public static void SelectElementByExactText(WebElement WebElement, String text) throws Exception {
		Select select = new Select(WebElement);		
		List<WebElement> elements = select.getOptions();
		int i = 0;
		text=text.toLowerCase();
		for (WebElement element : elements) {
			String str_lowercase_text = element.getText().trim().toLowerCase();
			if (element.getText().trim().equalsIgnoreCase(text)) {
				select.selectByIndex(i);				
				break;
			}
			i++;
		}
	}

	

	
	
	public static String getSelectedOptionFromDropdown(WebElement element) throws Exception {
		Select dropdown = new Select(element);
		return dropdown.getFirstSelectedOption().getText();
	}
	
	// to get selected element text from list of dropdown or checkbox or radio buttons
	// Here get elements with same tagName or className etc..
	public String getSelectedElementText(List<WebElement> elements) {
	   for (WebElement element : elements) {
	      if (element.isSelected()) {
	           return element.getText();
	      }
	   }
	   return null;  
	}	

}
