package common.toolbox.twin;

import static org.ebayopensource.twin.Criteria.type;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.log4j.*;
import org.ebayopensource.twin.Application;
import org.ebayopensource.twin.Criteria;
import org.ebayopensource.twin.Element;
import org.ebayopensource.twin.TwinException;
import org.ebayopensource.twin.element.Pane;
import org.ebayopensource.twin.element.Text;
import org.ebayopensource.twin.element.Window;
import org.junit.Test;

import common.toolbox.selenium.Waits;

public class CalculatorTest {
	private static final Logger LOGGER = LogManager.getLogger(CalculatorTest.class);
	static final String RC_URL = "http://127.0.0.1:4444";
	static final String APPLICATION = "calculator";

	private Map<Integer, String> calcNumPad_ObjectRepository;
	private Map<String, String> calcOperPad_ObjectRepository;
	{
		// Object Repository for Calculator Numbers
		calcNumPad_ObjectRepository = new HashMap<Integer, String>();
		calcNumPad_ObjectRepository.put(0, "130");
		calcNumPad_ObjectRepository.put(1, "131");
		calcNumPad_ObjectRepository.put(2, "132");
		calcNumPad_ObjectRepository.put(3, "133");
		calcNumPad_ObjectRepository.put(4, "134");
		calcNumPad_ObjectRepository.put(5, "135");
		calcNumPad_ObjectRepository.put(6, "136");
		calcNumPad_ObjectRepository.put(7, "137");
		calcNumPad_ObjectRepository.put(8, "138");
		calcNumPad_ObjectRepository.put(9, "139");

		// Object Repository for Calculator Operators
		calcOperPad_ObjectRepository = new HashMap<String, String>();
		calcOperPad_ObjectRepository.put("+", "93");
		calcOperPad_ObjectRepository.put("-", "94");
		calcOperPad_ObjectRepository.put("*", "92");
		calcOperPad_ObjectRepository.put("/", "91");
		calcOperPad_ObjectRepository.put("=", "121");
		calcOperPad_ObjectRepository.put("clear", "81");
	}

	private static Pane pane = null;
	private static Criteria criteria = null;
	private static Window window = null;

	// Keypad Panel
	private static Element keyPadPanel = null;
	private static String SCREENSHOT_DIR = System.getProperty("user.dir") + "/Screenshots/";

	public static void main(String[] args) throws TwinException, IOException, InterruptedException {
		Application app = null;

		try {
			app = launchApplication();
			CalculatorTest ct = new CalculatorTest();

			// Clear Screenshots folder
			clearScreenshotsFolder();
			Thread.sleep(5000);
			// Perform Addition
			LOGGER.info("Addition of 1,3 - Actual Results: " + ct.add(1, 3) + ",  Expected Results: 4");
			window.getScreenshot().save(new File(SCREENSHOT_DIR + "Add_1_and_3.png"));

			LOGGER.info("Addition of 17,3 - Actual Results: " + ct.add(17, 3) + ",  Expected Results: 20");
			window.getScreenshot().save(new File(SCREENSHOT_DIR + "Add_17_and_3.png"));

			// Perform Subtraction
			LOGGER.info("Subtraction of 5,1 - Actual Results: " + ct.subtraction(5, 1) + ",  Expected Results: 4");
			window.getScreenshot().save(new File(SCREENSHOT_DIR + "Subtraction_5_and_1.png"));

			LOGGER.info("Subtraction of 90,7 - Actual Results: " + ct.subtraction(90, 7) + ",  Expected Results: 83");
			window.getScreenshot().save(new File(SCREENSHOT_DIR + "Subtraction_90_and_7.png"));

			// Perform Multiplication
			LOGGER.info(
					"Multiplication of 8,2 - Actual Results: " + ct.multiplication(8, 2) + ",  Expected Results: 16");
			window.getScreenshot().save(new File(SCREENSHOT_DIR + "Multiplication_8_and_2.png"));

			LOGGER.info(
					"Multiplication of 15,4 - Actual Results: " + ct.multiplication(15, 4) + ",  Expected Results: 60");
			window.getScreenshot().save(new File(SCREENSHOT_DIR + "Multiplication_15_and_4.png"));

			// Perform Division
			LOGGER.info("Division of 9,3 - Actual Results: " + ct.division(9, 3) + ",  Expected Results: 3");
			window.getScreenshot().save(new File(SCREENSHOT_DIR + "Division_9_and_3.png"));

			LOGGER.info("Division of 100,2 - Actual Results: " + ct.division(100, 2) + ",  Expected Results: 50");
			window.getScreenshot().save(new File(SCREENSHOT_DIR + "Division_100_and_2.png"));

		} finally {
			// Close the application
			if (app != null) {
				app.close();
			}
		}
	}

	private static void clearScreenshotsFolder() throws IOException  {
		for (File f : new File(SCREENSHOT_DIR).listFiles()) {
			if (f.isFile() && f.exists()) {
				Files.delete(Paths.get(SCREENSHOT_DIR));
			}
		}
	}

	private static Application launchApplication() throws MalformedURLException {
		// URL of the remote control, connect to rc
		Application app = new Application(new URL(RC_URL));

		// launch application 'calculator' on remote control.
		app.open(APPLICATION, null);

		window = app.getWindow();
		pane = window.getChild(type(Pane.class));
		// criteria = type(Pane.class).and(class());

		// Keypad Panel
		keyPadPanel = pane.getChildren(criteria).get(1);

		return app;
	}

	// Perform 'Addition'.
	public int add(int a, int b) throws InterruptedException {
		performOperation("clear");

		clickNumber(a);
		performOperation("+");
		clickNumber(b);
		performOperation("=");

		return Integer.parseInt(getResults().trim());
	}

	// Perform 'Subtraction'.
	public int subtraction(int a, int b) throws InterruptedException {
		performOperation("clear");

		clickNumber(a);
		performOperation("-");
		clickNumber(b);
		performOperation("=");

		return Integer.parseInt(getResults().trim());
	}

	// Perform 'Multiplication'.
	public int multiplication(int a, int b) throws InterruptedException {
		performOperation("clear");

		clickNumber(a);
		performOperation("*");
		clickNumber(b);
		performOperation("=");

		return Integer.parseInt(getResults().trim());
	}

	// Perform 'Division'.
	public int division(int a, int b) throws NumberFormatException, InterruptedException {
		performOperation("clear");
		clickNumber(a);
		performOperation("/");
		clickNumber(b);
		performOperation("=");

		return Integer.parseInt(getResults().trim());
	}

	// Click the Number in the calculator application.
	private void clickNumber(int number) throws InterruptedException {
		String sNumber = String.valueOf(number);
		for (int i = 0; i < sNumber.length(); i++) {
			String id_ = calcNumPad_ObjectRepository.get(Character.digit(sNumber.charAt(i), 10));
			// keyPadPanel.getChildren(id(id_)).get(0).click();
			Waits.waitInSeconds(1);
		}
	}

	// Perform operations.
	private void performOperation(String controlID) throws InterruptedException {
		String id_ = calcOperPad_ObjectRepository.get(controlID);
		// keyPadPanel.getChildren(id(id_)).get(0).click();

		Waits.waitInSeconds(1);
	}

	// Fetch the results after performing the operations
	private String getResults() throws InterruptedException {
		Waits.waitInSeconds(1);
		Element resultPanel = pane.getChildren(criteria).get(0);
		String result = (resultPanel.getChildren().get(2)).getName();

		return result;
	}

	// ***************** testing purpose only *****************
	@Test
	public void test_calculator() throws MalformedURLException {
		Application app = new Application(new URL("http://127.0.0.1:4445"));

		// launch application 'calculator' on remote control.
		app.open("calculator", null);
		window = app.getWindow();
		pane = window.getChild(type(Pane.class));
		// criteria = type(Pane.class).and(id("158"));
		// Keypad Panel
		// Element child = pane.getChild(id("137"));
		// child.click();
		// keyPad.click();
		// keyPad.sendKeys("50");
	}
}