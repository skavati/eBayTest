package common.toolbox.selenium;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Set;


import org.apache.log4j.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import common.baselib.BaseMethods;

public class Window {
	private static final Logger LOGGER = LogManager.getLogger(Window.class);
	private BaseMethods baseMethods;

	private static String homeWindow = null;

	public static boolean focusWindow(String strLogicalName, WebDriver driver, String strWindowHandle) {
		boolean blResult = false;

		try {
			driver.switchTo().window(strWindowHandle);
			blResult = true;
		} catch (Exception e) {
		}
		return blResult;
	}

	
	public void switchToWindow(int windownumber) throws Exception {
		homeWindow = baseMethods.getDriver().getWindowHandle();
		String[] windowHandles = baseMethods.getDriver().getWindowHandles()
				.toArray(new String[baseMethods.getDriver().getWindowHandles().size()]);
		LOGGER.info("%%%%%%%%%%%%" + windowHandles.length);
		baseMethods.getDriver().switchTo().window(windowHandles[windownumber]);
		BaseMethods.maximizeWinDow();

	}

	
	public void switchToOriginalWindow() {
		baseMethods.getDriver().switchTo().window(homeWindow);
	}

	public static void SwitchWindow(WebDriver driver, String Title, int WindowCount, int WaitTimeInSec)
			throws InterruptedException {
		try {
			// ..... METHOD 1 .......
			String BaseWindow = driver.getWindowHandle();

			int count = 0;
			while ((driver.getWindowHandles().size()) != WindowCount) {
				count++;
				Waits.waitInSeconds(1);

				if ((count == WaitTimeInSec)) {
					LOGGER.info("Window with title '" + Title + "' still not found even after  '" + WaitTimeInSec
							+ "' seconds");
					break;
				}

				if (driver.getWindowHandles().size() == WindowCount)
					break;
			}

			Set<String> handles = driver.getWindowHandles();

			for (String handle : handles)
			{
				LOGGER.info("Window " + WindowCount + " title: " + driver.getTitle());
				
				//if (handle.equals(BaseWindow)) {
					if (driver.switchTo().window(handle).getTitle().contains(Title)) {
						break;

				//	}

				}
			}

		}

		catch (NoSuchElementException e) {

		}
	}
	////This method is to switch to window using the window/page title. The inputs are the driver, the window title, locator of the element to be clicked
	public static void SwitchtoWindowusingPagetitle(WebDriver driver, String windowtext, String locator)
			throws InterruptedException

	{
		try {
			String parentWindowId = driver.getWindowHandle();
			driver.findElement(By.xpath(locator)).click();

			try {
				for (String windowId : driver.getWindowHandles()) {
					String currentwindowtext = driver.switchTo().window(windowId).getTitle();

					if (currentwindowtext.contains(windowtext)) {

						// Assert.assertTrue(driver.getTitle().contains(windowtext));
						LOGGER.info("Window " + windowtext + " is opened successfully");
						driver.close();
						break;
					}

				}
			} finally {
				// Switch to the parent browser window
				driver.switchTo().window(parentWindowId);

			}

		} catch (NoSuchElementException e) {
		}
	}

	//This method is to switch to window using the window/page title. The inputs are the driver, the window title, locator of the element to be clicked,
	// content in the page to be checked and the locator for the content
	public static void SwitchtoWindowusingPagetitle(WebDriver driver, String windowtext, String locator,  String content, String contentlocator)
			throws Exception

	{
		try {
			String parentWindowId = driver.getWindowHandle();
			driver.findElement(By.xpath(locator)).click();

			try {
				for (String windowId : driver.getWindowHandles()) {
					String currentwindowtext = driver.switchTo().window(windowId).getTitle();

					if (currentwindowtext.contains(windowtext)) 
					{
						//Assert.assertTrue(driver.getTitle().contains(windowtext));
						LOGGER.info("Window '" + windowtext + "' is opened successfully");
						WebElement windowobject = driver.findElement(By.xpath(contentlocator));
						Waits.waitInSeconds(2);
						//Assert.assertEquals(windowobject.getAttribute("value"), content);
						LOGGER.info("The popup window contains the text : "+windowobject.getAttribute("value"));
						driver.close();
						break;
					}

				}
			} finally {
				// Switch to the parent browser window
				driver.switchTo().window(parentWindowId);

			}

		} catch (NoSuchElementException e) {
		}
	}

	public static void SwitchtoWindowusingcurrenturl(WebDriver driver, String windowtext, String linkname, String locator)
			throws InterruptedException

	{
		try {
			String parentWindowId = driver.getWindowHandle();
			driver.findElement(By.xpath(locator)).click();
			LOGGER.info("Customer has clicked "+linkname);

			try {
				for (String windowId : driver.getWindowHandles()) {
					String currentwindowtext = driver.switchTo().window(windowId).getCurrentUrl();

					if (currentwindowtext.contains(windowtext)) {

						// Assert.assertTrue(driver.getPageSource().contains(windowtext));
						LOGGER.info("Window " + windowtext + " is opened successfully");
						LOGGER.info("The url of the window opened is: " + currentwindowtext);
						driver.close();
						break;
					}

				}
			} finally {
				// Switch to the parent browser window
				driver.switchTo().window(parentWindowId);
			}

		} catch (NoSuchElementException e) {
		}
	}

	public void SwitchWindow(WebDriver driver, String Title, int WindowCount) throws InterruptedException {
		SwitchWindow(driver, Title, WindowCount, 10);
	}

	// to switch to latest window open
	public static void SwitchToLatestWindow(WebDriver driver) {
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
			LOGGER.info("window title "+driver.getTitle());
		}

	}

	// to switch to latest window open
	public static void SwitchToLatestWindow(WebDriver driver, int windowCount, int waitTimeInSec)
			throws InterruptedException {
		int count = 0;
		while (!((driver.getWindowHandles().size()) == windowCount)) {
			count++;
			Waits.waitInSeconds(1);
			if (count == waitTimeInSec) {
				break;
			}
			if (driver.getWindowHandles().size() == windowCount)
				break;
		}

		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}

	}

	// method overloading
	public static void SwitchToLatestWindow(WebDriver driver, int windowCount) throws InterruptedException{
		SwitchToLatestWindow(driver, windowCount, 30);
	}
//Added method @18/4/2017 for Switch back to Parent Window:
	public static void switchToParentWindow(WebDriver driver,String Parenwindow)
	{
		//Method for switch back to parent windos
		for(String windowHandle  : driver.getWindowHandles())
		{
			if(!windowHandle.equals(Parenwindow))
			{
				driver.switchTo().window(windowHandle);
				driver.close(); //closing child window
				driver.switchTo().window(Parenwindow); //cntrl to parent window
			}
		}	
	}
	
}
