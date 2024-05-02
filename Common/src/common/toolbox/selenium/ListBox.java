package common.toolbox.selenium;
 
import java.util.Random;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class ListBox {
	

		public static boolean selectItemByValue(String strLocatorType,String strLocatorValue,String strValue,WebDriver driver){
			boolean blResult=false;
			WebElement element = Object.getElement(driver, strLocatorType, strLocatorValue);
			if(element!=null){
				if(element.isDisplayed()){
					if(element.isEnabled()){
						try{
							Select sel = new Select(element);
							sel.selectByVisibleText(strValue);
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
		
		public static boolean selectItems(String strLogicalName,String strLocatorType,String strLocatorValue,String strItems,WebDriver driver){
			boolean blResult=false;
			WebElement element = Object.getElement(driver, strLocatorType, strLocatorValue);
			if(element!=null){
				if(element.isDisplayed()){
					if(element.isEnabled()){
						try{
							Select sel = new Select(element);
							element.sendKeys(Keys.CONTROL);
							String[] arrItems=strItems.split("\\;");
							for(int inItem=0;inItem<=arrItems.length-1;inItem++){
								sel.selectByVisibleText(arrItems[inItem]);
							}
							
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
		
		public static boolean selectItemByIndex(String strLogicalName,String strLocatorType, String strLocatorValue,int itemIndex,WebDriver driver){
			boolean blResult=false;
			WebElement element = Object.getElement(driver, strLocatorType, strLocatorValue);
			if(element!=null){
				if(element.isDisplayed()){
					if(element.isEnabled()){
						try{
							Select sel = new Select(element);
							sel.selectByIndex(itemIndex);
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
		
		public static boolean selectFirstItem(String strLogicalName,String strLocatorType,String strLocatorValue,WebDriver driver){
			boolean blResult=false;
			WebElement element = Object.getElement(driver, strLocatorType, strLocatorValue);
			if(element!=null){
				if(element.isDisplayed()){
					if(element.isEnabled()){
						try{
							Select sel = new Select(element);
							sel.selectByIndex(1);
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
		
		public static boolean selectLastItem(String strLocatorType,String strLocatorValue,WebDriver driver){
			boolean blResult=false;
			WebElement element = Object.getElement(driver, strLocatorType, strLocatorValue);
			//int size =0;
			if(element!=null){
				if(element.isDisplayed()){
					if(element.isEnabled()){
						try{
							Select sel = new Select(element);
							sel.selectByIndex(sel.getOptions().size());
							blResult=true;
							}
						catch(Exception e){
							}
						
					}
					
				}
			}
			
			return blResult;
		}
		
		public static boolean selectRandomItem(String strLogicalName,String strLocatorType,String strLocatorValue,WebDriver driver){
			boolean blResult=false;
			WebElement element = Object.getElement(driver, strLocatorType, strLocatorValue);
			int size =0;
			int index = 0;
			if(element!=null){
				if(element.isDisplayed()){
					if(element.isEnabled()){
						try{
							Select sel = new Select(element);
							index=getRandomIndex(sel.getOptions().size());
							sel.selectByIndex(index);
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

		public static boolean isEnabled(String strLocatorType,String strLocatorValue, String strLogicalName,WebDriver driver){
	        boolean blResult=false;
	        WebElement element = Object.getElement(driver, strLocatorType, strLocatorValue);
	        if(element!=null){
				if(element.isDisplayed()){
					if(element.isEnabled()){
						try{
							blResult = true;
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
		
		public static boolean isDisabled(String strLocatorType,String strLocatorValue , String strLogicalName,WebDriver driver){
	        boolean blResult=false;
	        WebElement element = Object.getElement(driver, strLocatorType, strLocatorValue);
	        if(element!=null){
				if(element.isDisplayed()){
					if(!element.isEnabled()){
						try{
							blResult = true;
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
		
		public static int getRandomIndex(int size){
			int random =new Random().nextInt(size+1);
			if(random==0){random++;}
			return random;
		}
		
		public static boolean deselectAll(String strLocatorType,String strLocatorValue, String strLogicalName,WebDriver driver){
	        boolean blResult=false;
	        WebElement element = Object.getElement(driver, strLocatorType, strLocatorValue);
	        if(element!=null){
				if(element.isDisplayed()){
					if(element.isEnabled()){
						try{
							Select sel = new Select(element);
							sel.deselectAll();
							blResult = true;
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
