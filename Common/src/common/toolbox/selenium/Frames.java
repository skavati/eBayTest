package common.toolbox.selenium;

import java.util.NoSuchElementException;


import org.apache.log4j.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import common.baselib.BaseMethods;

public class Frames {
	private static final Logger LOGGER = LogManager.getLogger(Frames.class);
	private BaseMethods baseMethods; 
	
		
	public  void switchToPageComeOutoFFrame(){
		baseMethods.getDriver().switchTo().defaultContent();
	}
	
	public  void SwitchFrame(WebDriver driver, String frameValue, String frameType, int waitTimeInSec) {
		try {
			driver.switchTo().defaultContent();
			switch (frameType.toLowerCase()) {
			case "id":
				BaseMethods.waitForElement(driver, By.id(frameValue), waitTimeInSec);
				driver.switchTo().frame(frameValue);
				break;

			case "default":
				driver.switchTo().defaultContent();
				break;

			case "name":
				BaseMethods.waitForElement(driver, By.name(frameValue), waitTimeInSec);
				driver.switchTo().frame(frameValue);
				break;

			default:
				LOGGER.info("Sorry, invalid selection of ''ByType''.");
				break;
			}

		} catch (NoSuchElementException e) {

			LOGGER.info(e.getMessage());
		}
	}

	public  void SwitchFrame(WebDriver driver, String FrameValue) {
		SwitchFrame(driver, FrameValue, "ID", 30);
	}

}
