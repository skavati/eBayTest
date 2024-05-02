package common.constants;

import com.aventstack.extentreports.ExtentTest;

import common.utilities.FileUtils;
import common.utilities.XMLUtils;

public class GlobalConstants {
	private GlobalConstants() {
		throw new IllegalStateException("GlobalConstants class");
	}

	public static final String BROWSER = "ie";
	private static final String TEST_DATA_FILE = "TestDataFile";
	public static final ExtentTest TEST = null;
	public static final String IMAGE_PATH = System.getProperty("user.home") + "\\ScreenShots";
	public static final String USER_DIR = System.getProperty("user.dir");
	public static final String SIKULI_IMAGE_PATH = USER_DIR + "\\SikuliImages";
	// Excel related constants
	public static final String TEST_DATA_MOBILE_WEB_FILE = "";
	public static final String LOGIN_SHEET = "Login";
	public static final String LOGIN_EXT_SHEET = "Login_ext";
	public static final String LOGIN_MB_SHEET = "Login"; 
	public static final String MY_ACCOUNTS_SHEET = "MyAccounts";
	public static final String TRANSFERS_PAYMENTS_BPAY_SHEET = "TransfersPayments&Bpay";
	public static final String MANAGE_MY_ACCOUNTS_SHEET = "ManageMyAccounts";
	public static final String PRODUCTS_TOOLS_CALCULATORS_SHEET = "ProductsTools&Calculators";
	public static final String MY_DETAILS_SETTINGS_SHEET = "MyDetails&Settings";
	public static final String SECURITY_SMS_EMAIL_ALERTS_SHEET = "SecuritySMS&EmailAlerts";
	public static final String CAN_RETRIEVAL_SHEET = "CANRetrieval";
	public static final String PASS_RESET_SHEET = "PWDReset";
	public static final String SAFI_SHEET = "SAFI";
	public static final String SERVICE_STATION_SHEET = "ServiceStation";
	public static final String CLOSED_ACTS_ESTMT_SHEET = "ClosedActsEStmts";
	public static final String PAYMENTS_MB_SHEET = "Payments";
	public static final String INVITATIONS_MB_SHEET = "Invitations";
	public static final String CASH_MB_SHEET = "Cash";
	public static final String MESSAGES_MB_SHEET = "Messages";
	public static final String SERVICES_MB_SHEET = "Services";
	public static final boolean DEBUGMODE = true;
	public static final String CAPTURE_EMAIL_SHEET = "CaptureEmail_registration";
	public static final String DEEP_LINK_HOME_URL = USER_DIR + "\\src\\test\\resources\\fasthelp.htm";

}
