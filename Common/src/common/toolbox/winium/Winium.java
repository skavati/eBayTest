package common.toolbox.winium;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;
import org.sikuli.script.Pattern;

import com.jacob.com.LibraryLoader;

import autoitx4java.AutoItX;
import common.constants.GlobalConstants;
import common.toolbox.selenium.Action;
import common.toolbox.selenium.Waits;
import common.toolbox.sikuli.Sikuli;
import common.utilities.SystemUtils;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import winium.elements.desktop.DesktopElement;

public class Winium {
	static WiniumDriver driver;
	private static final String processWinium = "Winium.Desktop.Driver.exe";
	public static WiniumDriver getWiniumDriver(String appPath, String remoteMachine) throws Exception {
		try {
			if (!SystemUtils.isRunning(processWinium)) {
				String winiumEXEpath =  common.utilities.FileUtils.findFullPath(System.getProperty("user.dir"), processWinium);				
				Runtime.getRuntime().exec(winiumEXEpath);
			}
			DesktopOptions options = new DesktopOptions();
			if ((appPath != null)) {
				options.setApplicationPath(appPath);
			} else {
				options.setDebugConnectToRunningApp(true);
			}
			driver = new WiniumDriver(new URL("http://" + remoteMachine + ":9999"), options);
		} catch (Exception e) {
			

		}
		return driver;
	}

	// method overloading
	// appPath = "C:\\Windows\\System32\\help.exe" (its a dummy program in windows)
	public static WiniumDriver getWiniumDriver(String remoteMachine) throws Exception {
		return getWiniumDriver(null, remoteMachine);
	}

	// method overloading
	public static WiniumDriver getWiniumDriver() throws Exception {
		return getWiniumDriver(null, InetAddress.getLocalHost().getHostAddress());
	}

	// To set focus on window for given window title
	// Parameters:
	// appName:Name of app on taskbar
	// windowTitle: title of window to be focused
	public static void setWindowFocus(WiniumDriver driver, String appName, String windowtitle) throws Exception {
		try {
			driver.findElement(By.name(appName)).click();
			// for multi window use below
			if (!windowtitle.contentEquals("")) {
				driver.findElement(By.name(windowtitle)).click();
			}

		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	// method overloading for single window focus
	public static void setWindowFocus(WiniumDriver driver, String appName) throws Exception {
		setWindowFocus(driver, appName, "");
	}

	public static void clickElement(String appName) throws Exception {
		try {
			WiniumDriver driver = getWiniumDriver();
			driver.findElement(By.name(appName)).click();
		} catch (Exception e) {
		} finally {
			SystemUtils.killProcessByName(processWinium);
			SystemUtils.killProcessByName("WerFault.exe");
		}
	}

	public static void clickElement(WiniumDriver driver, String appName) throws Exception {
		driver.findElement(By.name(appName)).click();
	}

	// to verify if element exists
	public static boolean isElementExists(WiniumDriver driver, String appName) throws Exception {
		return (driver.findElements(By.name(appName)).isEmpty()) ;		
		
	}

	// ************** testing purpose only ************
	// @Test
	public void test_Calculator() throws Exception {
		// calculator test
		WiniumDriver driver1 = Winium.getWiniumDriver("C:\\Windows\\system32\\calc.exe", "localhost");
		Waits.waitInSeconds(1);
		WebElement wnd = driver1.findElement(org.openqa.selenium.By.className("CalcFrame"));
		Waits.waitInSeconds(1);
		wnd.findElement(org.openqa.selenium.By.name("5")).click();
		Waits.waitInSeconds(1);
		wnd.findElement(By.name("Multiply")).click();
		Waits.waitInSeconds(1);
		wnd.findElement(By.name("3")).click();
		Waits.waitInSeconds(1);
		wnd.findElement(By.name("Equals")).click();
		Waits.waitInSeconds(1);
		WebElement result = wnd.findElement(By.id("150"));
		Action.moveToElement(driver1, result);
		Waits.waitInSeconds(1);
		wnd.findElement(By.name("Close")).click();
	}

	

	
	// to test file download
		//@Test
		public void test_download_file()   {
			final String JACOB_DLL_TO_USE = System.getProperty("sun.arch.data.model").contains("32") ?
			          "jacob-1.18-x86.dll" : "jacob-1.18-x64.dll";		
					
			File file = new File(System.getProperty("user.home") + "\\.jenkins\\workspace\\Common\\tools\\autoit", JACOB_DLL_TO_USE);
			System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());
			
			AutoItX x = new AutoItX();		
			String txt = "Downloads - Internet Explorer provided by St.George Banking Group";      
	        x.winActivate(txt);
	        x.winWaitActive(txt);        
	     
	        //1139, 73
	        Integer X = SystemUtils.getRelativeXCoordinate(1139,1920);
	        Integer Y = SystemUtils.getRelativeYCoordinate(73,1080);
	          x.controlClick(txt, "", "[CLASS:DirectUIHWND;INSTANCE:1]", "Left", 1,X,Y);
	          
	       
		}
		
		// to click on wifi
		//@Test
		public void test_wifi() throws AWTException {
			final String JACOB_DLL_TO_USE = System.getProperty("sun.arch.data.model").contains("32") ?
			          "jacob-1.18-x86.dll" : "jacob-1.18-x64.dll";		
					
			File file = new File(System.getProperty("user.home") + "\\.jenkins\\workspace\\Common\\tools\\autoit", JACOB_DLL_TO_USE);
			System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());
			
			AutoItX x = new AutoItX();
			Integer X = SystemUtils.getRelativeXCoordinate(77,1920);
	        Integer Y = SystemUtils.getRelativeYCoordinate(21,1080);
	         	
	        
			x.controlClick("", "User Promoted Notification Area", "1504","Left", 1, X ,Y);
			
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_TAB);
			robot.delay(1000);	
			robot.keyPress(KeyEvent.VK_TAB);
					
			robot.delay(1000);	
			robot.mousePress(1);
			robot.delay(1000);	
			robot.mouseRelease(1);
				
			
		}
		
		//@Test
		public void test_downloadfile() throws Exception {
			WiniumDriver driver1 = Winium.getWiniumDriver();
			Waits.waitInSeconds(1);
			Waits.waitInSeconds(1);
			Actions act=new Actions(driver1);
			act.moveToElement(driver1.findElement(By.name("Notification"))).click().build().perform();
			Waits.waitInSeconds(1);
			act.moveToElement(driver1.findElement(By.name("Cancel"))).click().build().perform();
		}
		
		@Test
		public void test_calculator_Appium() throws MalformedURLException {
			// Launch Notepad
			DesiredCapabilities appCapabilities = new DesiredCapabilities();			
			WindowsDriver<WindowsElement> driver1 = new WindowsDriver(new URL("http://127.0.0.1:4723"), appCapabilities);

			// Use the session to control the app
			driver1.findElementByClassName("Edit").sendKeys("This is some text");
		}
}