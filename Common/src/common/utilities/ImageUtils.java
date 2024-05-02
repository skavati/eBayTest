package common.utilities;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.junit.Test;

import common.constants.GlobalConstants;
import common.toolbox.selenium.Waits;
import net.sourceforge.tess4j.*;

import java.io.*;
public class ImageUtils {

	public static void captureScreen(String fileName) throws Exception {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle screenRectangle = new Rectangle(screenSize);		
		Robot robot = new Robot();
		BufferedImage image = robot.createScreenCapture(screenRectangle);
		ImageIO.write(image, "png", new File(fileName));
	}

	// to capture desktop and return path of the snapshot
	public static String getDesktopSnapshot() throws Exception {
		String snapPath = GlobalConstants.IMAGE_PATH + "\\" + StringUtils.getCurrentTimeStamp("ddMMyyHHmmssSSS")
				+ ".png";
		captureScreen(snapPath);
		Waits.waitInSeconds(1);
		return snapPath;
	}

	public static void captureScreen_SelectedArea(String fileName, int x, int y, int width, int height)
			throws Exception {

		// Define an area of size 500*400 starting at coordinates (10,50)
		Rectangle screenRectangle = new Rectangle(x, y, width, height);
		Robot robot = new Robot();
		BufferedImage image = robot.createScreenCapture(screenRectangle);
		ImageIO.write(image, "png", new File(fileName));
	}

	// to capture desktop and return path of the snapshot
	public static String getDesktopSnapshot_SelectedArea(int x, int y, int width, int height) throws Exception {
		String snapPath = GlobalConstants.IMAGE_PATH + "\\" + StringUtils.getCurrentTimeStamp("ddMMyyHHmmssSSS")
				+ ".png";
		captureScreen_SelectedArea(snapPath, x, y, width, height);
		Waits.waitInSeconds(1);
		return snapPath;
	}
	
	// TO get text from a screenshot
	public static String getImgText(String imageLocation) {
	      ITesseract instance = (ITesseract) new Tesseract();
	      try 
	      {
	         String imgText = instance.doOCR(new File(imageLocation));
	         return imgText;
	      } 
	      catch (TesseractException e) 
	      {
	         e.getMessage();
	         return "Error while reading image";
	      }
	   }

	// ************* below are testing purpose only ***
	@Test
	public void test_capturescreen() throws Exception {
		//WiniumDriver driver = Winium.getWiniumDriver();
		
		String path = "C:\\Users\\C70032\\ScreenShots\\230519164223367.png";
		//LOGGER.info("Text from image: "+getImgText(path));
		if (getImgText(path).contains("440 086 725")){
			//LOGGER.info("Acct.no found");
		}
		
	}
}
