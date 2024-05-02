package common.toolbox.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import common.toolbox.selenium.Waits;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class ExtractCode {
    private final static String SMS_POPUP_CLASSPATH = "C:\\Your\\PATH\\in\\here\\";
    private final static String SMS_POPUP_APP_NAME = "SMSPopup.apk";
    private final static String SMS_POPUP_PACKAGE_NAME = "net.everythingandroid.smspopup";
    private final static String DEVICE_ID = "your_device_id";

    private final static String ADB_INSTALL_COMMAND = "adb -s %s install -r %s";
    private final static String ADB_UNINSTALL_COMMAND = "adb -s %s uninstall %s";
    private final static String ADB_START_ACTIVITY = "adb -s %s shell am start -n net.everythingandroid.smspopup/.ui.SmsPopupConfigActivity";
    private final static String ADB_GO_BACK = "adb -s %s shell input keyevent 4";

    private final static String TYPE_SEARCH_TEXT_STARTSWITH = "textStartsWith";
    private final static String TYPE_SEARCH_TEXT_CONTAINS = "textContains";
    private final static String TYPE_SEARCH_TEXT = "text";

    private WebDriverWait wait60;
    private Wait<WebDriver> fluentWait30;
    private AppiumDriver localDriver;

    public ExtractCode(AppiumDriver driver) {
        localDriver = driver;
        wait60 = new WebDriverWait(driver, 60);
        fluentWait30 = new FluentWait<WebDriver>(driver).withTimeout(30, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
    }

    public void installSMSAPK() throws Exception {
        
        this.uninstallSMSAPK();

        runADBCommand(String.format(ADB_INSTALL_COMMAND, DEVICE_ID, new File(SMS_POPUP_CLASSPATH, SMS_POPUP_APP_NAME).getAbsolutePath()));
        Thread.sleep(6000); //--> This can be replaced by checking if package is already installed. In slower devices this sleep may not be enough
    }

    public void uninstallSMSAPK() throws Exception {
        runADBCommand(String.format(ADB_UNINSTALL_COMMAND, DEVICE_ID, SMS_POPUP_PACKAGE_NAME));
        Waits.waitInSeconds(2);
    }

    public void configureSMSAPK() throws Exception {

        runADBCommand(String.format(ADB_START_ACTIVITY, DEVICE_ID));
        waitForIDwithText("android:id/button1","Accept",TYPE_SEARCH_TEXT).click();
        runADBCommand(String.format(ADB_GO_BACK, DEVICE_ID));
    }

    public String returnSMS() throws Exception {
        String SMS;

        waitForID("net.everythingandroid.smspopup:id/popupMessageMainlayout");
        SMS = waitForID("net.everythingandroid.smspopup:id/messageTextView").getText();
        runADBCommand(String.format(ADB_GO_BACK, DEVICE_ID));
        return SMS;
    }



    

    private void runADBCommand(String command) throws Exception {
        Runtime.getRuntime().exec(command);
    }

    
    private WebElement waitForIDwithText(String id, String value, String typeSearch) throws Exception {
        try {
            String query = "new UiSelector().resourceId(\"" + id + "\")." + typeSearch + "(\"" + value + "\")";
            return fluentWait30.until(webDriver -> ((AndroidDriver) localDriver).findElementByAndroidUIAutomator(query));
        } catch (Exception ex) {
            throw new Exception("The following id was not found: "+id);
        }
    }

    
    private WebElement waitForID(String id) throws Exception {
        try {
            return wait60.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
        } catch (Exception ex) {
            throw new Exception("The following id was not found: "+id);
        }
    }
    
    
    
}
