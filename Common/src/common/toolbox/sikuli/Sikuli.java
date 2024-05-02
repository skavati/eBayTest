package common.toolbox.sikuli;

import java.util.NoSuchElementException;


import org.apache.log4j.*;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

public class Sikuli {
	static Screen screen = new Screen();
	private static final Logger LOGGER = LogManager.getLogger(Sikuli.class);
	public static boolean isElementExists(String elementPath){
		Pattern element = new Pattern(elementPath);
		if (screen.exists(element) != null){
			return true;
			}	else{
				return false;
			}		
	}
	// to wait for sikuli image element
		public static void waitForElement(String elementPath) throws FindFailed {
			try {
				Pattern element = new Pattern(elementPath);				
				screen.wait(element);				
			} catch (

			NoSuchElementException e) {
				LOGGER.info(e.getMessage());
			}
		}
	// to click on sikuli image element
	public static void clickElement(String elementPath) throws FindFailed {
		try {
			Pattern element = new Pattern(elementPath);				
			screen.wait(element);
			screen.click(element);			
			
		} catch (

		NoSuchElementException e) {
			LOGGER.info(e.getMessage());
		}
	}

	// to double click on sikuli image element
	public static void doubleClickElement(String elementPath) throws FindFailed {
		try {
			Pattern element = new Pattern(elementPath);
			screen.wait(element);
			screen.doubleClick(element);
		} catch (

		NoSuchElementException e) {
			LOGGER.info(e.getMessage());
		}
	}

	// to type on sikuli image element
	public static void typeElement(String elementPath, String text) throws FindFailed {
		try {
			Pattern element = new Pattern(elementPath);
			screen.wait(element);
			screen.type(element, text);
		} catch (

		NoSuchElementException e) {
			LOGGER.info(e.getMessage());
		}
	}

	// to right click on sikuli image element
	public static void rightClickElement(String elementPath) throws FindFailed {
		try {
			Pattern element = new Pattern(elementPath);
			screen.wait(element);
			screen.rightClick(element);
		} catch (

		NoSuchElementException e) {
			LOGGER.info(e.getMessage());
		}
	}
}
