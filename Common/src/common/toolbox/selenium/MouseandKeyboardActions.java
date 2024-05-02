package common.toolbox.selenium;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import common.baselib.BaseMethods;
import common.utilities.StringUtils;

public class MouseandKeyboardActions {
	
	private BaseMethods baseMethods; 
		

		
	public  void pageScroll(String loc) throws Exception{	
		int pageScroll=250;
		if(loc.equalsIgnoreCase("UP")) pageScroll=-250;
		JavascriptExecutor jse = (JavascriptExecutor)baseMethods.getDriver();	
		jse.executeScript("scroll(0, "+pageScroll+");");
		
	}	
	
	
	
	public  void pressKeyBoardEnter() throws Exception{
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_ENTER);
	
	}	
	
	public  static void mouseOverElement(WebElement WebElement, WebDriver webdriver) {

		try {

			// create Actions object
			Actions act = new Actions(webdriver);
			act.moveToElement(WebElement).build().perform();

		} catch (NoSuchElementException e) {

			System.out.println(e.getMessage());

		}

	}
}
