package common.toolbox.autoit;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;

import javax.swing.JOptionPane;


import org.apache.log4j.*;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.winium.WiniumDriver;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

import autoitx4java.AutoItX;
import common.baselib.BaseMethods;
import common.toolbox.selenium.Action;
import common.toolbox.selenium.JavaScript;
import common.toolbox.sikuli.Sikuli;
import common.toolbox.winium.Winium;
import common.utilities.FileUtils;
import common.utilities.SystemUtils;

import com.hp.lft.sdk.te.Keys;
import com.jacob.com.LibraryLoader;

public class AutoItX4Java {
	private static final Logger LOGGER = LogManager.getLogger(AutoItX4Java.class);
	// to get AutoItx instance
	public static AutoItX getAutoItx() {
		final String JACOB_DLL_TO_USE = System
				.getProperty("sun.arch.data.model").contains("32")
						? "jacob-1.18-x86.dll"
						: "jacob-1.18-x64.dll";
		try {
			File file = new File(
					FileUtils.getWorkSpacePath() + "\\Common\\tools\\autoit",
					JACOB_DLL_TO_USE);
			System.setProperty(LibraryLoader.JACOB_DLL_PATH,
					file.getAbsolutePath());
			AutoItX x = new AutoItX();
			return x;


		} catch (Exception e) {

		}
		return null;
	}
	// to get relative x and y coordinates
	// based on target window control size width
	public static int getRelativeXCoordinate(AutoItX x, Integer posX,
			Integer controlWidth, String title, String text, String controlID) {
		return (int) ((1.0 * posX / controlWidth)
				* x.controlGetPosWidth(title, text, controlID));
	}

	// to get relative x and y coordinates
	// based on target window control size height
	public static int getRelativeYCoordinate(AutoItX x, Integer posY,
			Integer controlHeight, String title, String text,
			String controlID) {
		return (int) ((1.0 * posY / controlHeight)
				* x.controlGetPosHeight(title, text, controlID));
	}
	// *********************************** Below are testing purpose only
	// ************************
	
	// @Test
	public void test_Calculator() throws Exception {
		// TODO Auto-generated method stub

		// Declare instance driver for FireFox class
		WebDriver driver = BaseMethods.getDriver("ie");
		// Choose the 'JACOB' dll based on the JVM bit version.

		final String JACOB_DLL_TO_USE = System
				.getProperty("sun.arch.data.model").contains("32") ?

						"jacob-1.18-x86.dll" : "jacob-1.18-x64.dll";

		LOGGER.info("jacob file to use: " + JACOB_DLL_TO_USE);

		File file = new File(
				System.getProperty("user.home")
						+ "\\.jenkins\\workspace\\Common\\tools\\autoit",
				JACOB_DLL_TO_USE);
		System.setProperty(LibraryLoader.JACOB_DLL_PATH,
				file.getAbsolutePath());
		LOGGER.info("jacob path: " + file.getAbsolutePath());
		// Open url
		driver.get("http://www.seleniumhq.org/download/");

		// Click on Browse button
		driver.findElement(By.xpath(".//a[@href='https://goo.gl/dR7Lg2']"))
				.click();

		// Declare Object for AutoIt class
		AutoItX x = new AutoItX();

		// Here File Upload window get active.
		x.winWaitActive("Downloads - Internet Explorer", "");
		x.sleep(500);
		x.mouseClick("left", 978, 27);
		
	}

	// test notepad
	// @Test
	public void test_Notepad() throws Exception {
		final String JACOB_DLL_TO_USE = System
				.getProperty("sun.arch.data.model").contains("32")
						? "jacob-1.18-x86.dll"
						: "jacob-1.18-x64.dll";
		LOGGER.info("jacob file to use: " + JACOB_DLL_TO_USE);
		File file = new File(
				System.getProperty("user.home")
						+ "\\.jenkins\\workspace\\Common\\tools\\autoit",
				JACOB_DLL_TO_USE);
		System.setProperty(LibraryLoader.JACOB_DLL_PATH,
				file.getAbsolutePath());
		// LibraryLoader.loadJacobLibrary();
		// LOGGER.info("jacob path: "+file.getAbsolutePath());
		// Declare Object for AutoIt class
		String dateStamp = SystemUtils.getDateTimeStamp("ddMMyy");
		// String filePath =
		// "\\\\entpublic.infau.wbcau.westpac.com.au\\Enterprise\\Nishant\\Test.txt";
		String filePath = "C:\\Users\\C70032\\Automation\\temp\\Test.txt";
		// LOGGER.info("filePath\n:"+filePath);
		// Runtime.getRuntime().exec("cmd /c start "+filePath);
		// Waits.waitInSeconds(2);
		String text = dateStamp + "_1";
		AutoItX x = new AutoItX();
		String notepad = "Test.txt - Notepad";
		// String testString = "this is a test.";
		// if notepad is hidden then use AutoItX.SW_RESOTRE
		// x.run("notepad.exe", "c:\\", AutoItX.SW_RESTORE);
		x.winActivate(notepad);
		x.winWaitActive(notepad);
		x.ControlSetText(notepad, "", "15", "");
		// x.send(testString);
		// x.wait(2000);
		Thread.sleep(500);
		x.send(text);
		// Assert.assertTrue(x.winExists(notepad, testString));
		Thread.sleep(500);
		x.winClose(notepad);
		// x.controlClick(title, text, controlID)

		// x.winActivate("Notepad");
		// x.wait(2000);
		Thread.sleep(500);
		x.controlClick("Notepad", "&Save", "");
		// x.mouseClick("Save", 1, 10);
	}

	// to test file download
	@Test
	public void test_download_file() throws Exception {

		AutoItX x = getAutoItx();
		String txt = "Downloads - Internet Explorer provided by St.George Banking Group";
		if(x==null) {
			throw new Exception("x is null");
		}
		x.winActivate(txt);
		x.winWaitActive(txt);
		String[] posX = {"1920,1082", "1080,951"};
		int X = 0;
		for (int i = 0; i < (posX.length); i++) {
			String[] X1 = posX[i].split(",");
			if (X1[0].contentEquals(
					String.valueOf(SystemUtils.getScreenWidth()))) {
				X = Integer.valueOf(X1[1]);
				break;
			}
		}
		int Y = 0;
		String[] posY = {"1080,75", "800,72"};
		for (int i = 0; i < (posY.length); i++) {
			String[] Y1 = posY[i].split(",");
			if (Y1[0].contentEquals(
					String.valueOf(SystemUtils.getScreenHeight()))) {
				Y = Integer.valueOf(Y1[1]);
				break;
			}
		}

		
		LOGGER.info("Relative posX:" + X);
		LOGGER.info("Relative posY:" + Y);
		x.controlClick(txt, "", "[CLASS:DirectUIHWND;INSTANCE:1]", "Left", 1, X,
				Y);

		LOGGER.info("control pos width:" + x.controlGetPosWidth(txt, "",
				"[CLASS:DirectUIHWND;INSTANCE:1]"));
		LOGGER.info("control pos height:" + x.controlGetPosHeight(txt,
				"", "[CLASS:DirectUIHWND;INSTANCE:1]"));
		// to click keyboard enter as AutoIt is only highlighting targeted
		// element but not clicking on it
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_UP);
		robot.delay(1000);
		robot.keyPress(KeyEvent.VK_DOWN);
		robot.delay(1000);
		robot.keyPress(KeyEvent.VK_DOWN);
		robot.delay(1000);
		robot.keyPress(KeyEvent.VK_ENTER);

		

		

		// below is using sikuli
		

	}

	// to test notepad
	// @Test
	public void test_saveFile() {
		final String JACOB_DLL_TO_USE = System
				.getProperty("sun.arch.data.model").contains("32")
						? "jacob-1.18-x86.dll"
						: "jacob-1.18-x64.dll";
		LOGGER.info("jacob file to use: " + JACOB_DLL_TO_USE);
		File file = new File(
				FileUtils.getWorkSpacePath() + "\\Common\\tools\\autoit",
				JACOB_DLL_TO_USE);
		System.setProperty(LibraryLoader.JACOB_DLL_PATH,
				file.getAbsolutePath());

		AutoItX x = new AutoItX();
		String txt = "Date and Time";
		x.winActivate(txt);
		x.winWaitActive(txt);
		// to click on "Cancel"
		// x.controlClick(txt, "Cancel", "[CLASS:Button;INSTANCE:2]");
		// to click on combo box
		// x.controlClick(txt, "", "[CLASS:ComboBox;INSTANCE:2]");
		// to rename file
		

		

		// x.controlClick(txt, "", "126");
		x.winClose(txt);

	}
}
