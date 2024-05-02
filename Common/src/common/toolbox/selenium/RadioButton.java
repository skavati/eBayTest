package common.toolbox.selenium;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class RadioButton {
	
		public static boolean select(String strLogicalName,String strLocatorType,String strLocatorValue,WebDriver driver){
			boolean blResult=false;
			WebElement element = Object.getElement(driver, strLocatorType, strLocatorValue);
			if(element!=null){
				if(element.isDisplayed()){
					if(element.isEnabled()){
						try{
							element.click();
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
		
		public static boolean JSSelect(String strLogicalName,String strLocatorType,String strLocatorValue,WebDriver driver){
			boolean blResult=false;
			WebElement element = Object.getElement(driver, strLocatorType, strLocatorValue);
			if(element!=null){
				if(element.isDisplayed()){
					if(element.isEnabled()){
						try{
							JavascriptExecutor javaScriptExecutor=((JavascriptExecutor)driver);
							javaScriptExecutor.executeScript("arguments[0].click",element);
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

		
		public static boolean verify(String strLogicalName,String strLocatorType,String strLocatorValue,WebDriver driver){		
			boolean blResult=false;
			WebElement element = Object.getElement(driver, strLocatorType, strLocatorValue);
			if(element!=null){
				if(element.isDisplayed()){
					try{
						element.click();
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
			return blResult;
		}
		
		public static boolean isSelected(String strLogicalName,String strLocatorType,String strLocatorValue,WebDriver driver){
			boolean blResult = false;
			WebElement element = Object.getElement(driver, strLocatorType, strLocatorValue);
			if(element!=null){
				if (element.isDisplayed()) {
					try {
						if (element.isSelected()) {
							blResult = true;
						}
					} catch (Exception e) {
					}
				} else {
					}
			}
			else{
			}
			return blResult;
		}
		
		public static boolean isEnabled(String strLocatorType,String strLocatorValue, String strLogicalName,WebDriver driver){
			boolean blResult=false;
			WebElement element = Object.getElement(driver, strLocatorType, strLocatorValue);
			if(element!=null){
				if(element.isDisplayed()){
					if(element.isEnabled()){
						try{
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
