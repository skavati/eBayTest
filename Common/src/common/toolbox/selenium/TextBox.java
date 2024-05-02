package common.toolbox.selenium;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TextBox {
	
	
			
		public static boolean enterValue(String strLogicalName,String strLocatorType,String strLocatorValue,String strValue,WebDriver driver){
			boolean blResult=false;
			WebElement element = Object.getElement(driver, strLocatorType, strLocatorValue);
			if(element!=null){
				if(element.isDisplayed()){
					if(element.isEnabled()){
						try{
							element.sendKeys(strValue);
							blResult=true;
								
						}
						catch(Exception e){
							}
						
					}
					else{
					}
				}
				else{
					}
			}
			else{
				}
			return blResult;
		}
		
		public static boolean verifyValue(String strLogicalName,WebElement element,String strValue,WebDriver driver){
			boolean blResult=false;
			if(element!=null){
				if(element.isDisplayed()){
					if(element.isEnabled()){
						try{
							String textValue=element.getAttribute("value");
							if(textValue.equalsIgnoreCase(strValue)){
								blResult=true;
								}
						}
						catch(Exception e){
							}
						
					}
					else{
					}
				}
				else{
					}
			}
			else{
				}
			return blResult;
		}
		
		public static boolean enterPasswordValue(String strLogicalName,String strLocatorType,String strLocatorValue,String strValue,WebDriver driver){
			boolean blResult=false;
			WebElement element = Object.getElement(driver, strLocatorType, strLocatorValue);
			if(element!=null){
				if(element.isDisplayed()){
					if(element.isEnabled()){
						try{
							element.sendKeys(strValue);
							blResult=true;
							
						}
						catch(Exception e){
							}
						
					}
					else{
						}
				}
				else{
					}
			}
			else{
				}
			return blResult;
		}
		
		public static boolean clearValue(String strLogicalName,String strLocatorType,String strLocatorValue ,WebDriver driver){
			boolean blResult=false;
			WebElement element = Object.getElement(driver, strLocatorType, strLocatorValue);
			if(element!=null){
				if(element.isDisplayed()){
					if(element.isEnabled()){
						try{
							element.clear();
							blResult=true;
								
						}
						catch(Exception e){
							}
						
					}
					else{
						}
				}
				else{
					}
			}
			else{
			}
			return blResult;
		}
		
		public static boolean JSEnterValue(String strLogicalName,WebDriver driver,WebElement element,String strValue){
			boolean blResult=false;
			if(element!=null){
				if(element.isDisplayed()){
					if(element.isEnabled()){
						try{
							JavascriptExecutor javascriptExecutor=((JavascriptExecutor)driver);
							javascriptExecutor.executeScript("arguments[0].value=arguments[1]", element,strValue);
							element.sendKeys(strValue);
							blResult=true;
								
						}
						catch(Exception e){
							}
						
					}
					else{
						}
				}
				else{
					}
			}
			else{
			}
			return blResult;
		}
		
	


}
