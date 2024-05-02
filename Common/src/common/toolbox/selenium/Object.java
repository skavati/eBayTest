package common.toolbox.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class Object{
	

		public static WebElement getElement(WebDriver driver,String strLocatorType,String strLocatorValue){
				WebElement element=null;
				try{
					if(strLocatorType.equalsIgnoreCase("ID")){
						element=driver.findElement(By.id(strLocatorValue));
					}
					else if(strLocatorType.equalsIgnoreCase("NAME")){
						element=driver.findElement(By.name(strLocatorValue));
					}
					else if(strLocatorType.equalsIgnoreCase("XPATH")){
						element=driver.findElement(By.xpath(strLocatorValue));
					}
					else if(strLocatorType.equalsIgnoreCase("CSS")){
						element=driver.findElement(By.cssSelector(strLocatorValue));
					}
					else if(strLocatorType.equalsIgnoreCase("CLASS")){
						element=driver.findElement(By.className(strLocatorValue));
					}
				}
				catch(Exception e){}
				
				return element;
		}
		
		
		public static By elementLocator(String locator) throws Exception{

			if (locator.startsWith("xpath"))
				return By.xpath(locator.substring(6));
			else
			if (locator.startsWith("css"))
				return By.cssSelector(locator.substring(4));
			else
			if(locator.startsWith("id"))
				return By.id(locator.substring(3));
			else
			if(locator.startsWith("class"))
				return By.className(locator.substring(6));
			else
			if(locator.startsWith("name"))
				return By.name(locator.substring(5));
			else
			if(locator.startsWith("tag"))
				return By.tagName(locator.substring(4));
			else
			if(locator.startsWith("link"))
				return By.linkText(locator.substring(5));
			else
				 throw new IllegalArgumentException("Invalid locator format: " + locator);
			}
		
		
	


}
