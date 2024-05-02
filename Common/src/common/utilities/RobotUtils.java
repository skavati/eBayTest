package common.utilities;

import java.awt.AWTException;
import java.awt.Robot;

public class RobotUtils {
	// to scroll mouse wheel up or down
	public static void mouseWheel(Integer wheelAmount) throws AWTException {
		Robot bot = new Robot();
		bot.setAutoDelay(100);
		bot.mouseWheel(wheelAmount);
	}
}
