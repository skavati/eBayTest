package common.toolbox.selenium;


import org.apache.log4j.*;
import org.openqa.selenium.By;

import common.baselib.BaseMethods;

public class Browser {
	private static final Logger LOGGER = LogManager.getLogger(Browser.class);
	private static BaseMethods baseMethods; 
	
	public static final String BODY_TAG="xpath=//body";
	
		
	public static String getText(String locator) throws Exception{
		By byLocator=Object.elementLocator(locator);
		//if(isElementExists(byLocator)){
			LOGGER.info(locator+" From web page:"+baseMethods.getDriver().findElement(byLocator).getText());
			return baseMethods.getDriver().findElement(byLocator).getText();
		//}
		//else{
		//	verifyMethod("Unable to find the element using locator :"+locator , false);
		//	verifyResult();
			//return null;
		//}	
		
	}
	
	public String getCurrentURL() throws Exception{
		return baseMethods.getDriver().getCurrentUrl();
	}
		
	public void maximizeWinDow() throws Exception{
		baseMethods.getDriver().manage().window().maximize();		
	}
	
		
	public String getTitle() throws Exception{
		return baseMethods.getDriver().getTitle();
	}
	
		
	public String getPageSource() throws Exception{
		return baseMethods.getDriver().getPageSource();
	}
	
		
	public boolean isTextPresent(String text) throws Exception{
		String pageSource=getText(BODY_TAG);
		LOGGER.info(text+" isTextPresent:"+pageSource.contains(text));
		LOGGER.info("**********************");
		LOGGER.info(baseMethods.getDriver().findElement(By.xpath("//body")).getText());
		LOGGER.info("**********************");
		return pageSource.contains(text);		
	}
		
	public void browserRefresh() throws Exception{
		baseMethods.getDriver().navigate().refresh();
	}
	
		
	public void browserForward() throws Exception{
		baseMethods.getDriver().navigate().forward();
	}
	
		
	public void browserBackward() throws Exception{
		baseMethods.getDriver().navigate().back();
	}
	

}
