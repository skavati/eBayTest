package common.toolbox.leanft;

import com.hp.lft.sdk.GeneralLeanFtException;
import com.hp.lft.sdk.mobile.Label;
import com.hp.lft.sdk.web.Button;
import com.hp.lft.sdk.web.WebElement;

public class WaitMethods {
	// to wait for an object until it exists
	public static void waitForElement(WebElement webElement, Integer delay)
			throws GeneralLeanFtException, InterruptedException {
		for (int i = 0; i < 60; i++) {
			//Waits.waitInSeconds(1);.exists(1) means waits for 1 sec
			if (webElement.exists(1) || (delay == i)) {
				break;
			}
		}
	}

	// to wait for an object until it exists or 10 secs elapsed
	public static void waitForElement(WebElement webElement) throws GeneralLeanFtException, InterruptedException {
		waitForElement(webElement, 10);
	}
	
	// to wait for button until it exists
		public static void waitForButton(Button button, Integer delay)
				throws GeneralLeanFtException, InterruptedException {
			for (int i = 0; i < 60; i++) {
				//Waits.waitInSeconds(1);
				if (button.exists(1) || (delay == i)) {
					break;
				}
			}
		}
		
		// to wait for button until it exists or 10 secs elapsed
		public static void waitForButton(Button button) throws GeneralLeanFtException, InterruptedException {
			waitForElement(button, 10);
		}
		
		// to wait for label until it exists
				public static void waitForLabel(Label label, Integer delay)
						throws GeneralLeanFtException, InterruptedException {
					for (int i = 0; i < 60; i++) {
						//Waits.waitInSeconds(1);
						if (label.exists(1) || (delay == i)) {
							break;
						}
					}
				}
				
				// to wait for label until it exists or 10 secs elapsed
				public static void waitForLabel(Label label) throws GeneralLeanFtException, InterruptedException {
					waitForLabel(label, 10);
				}
}
