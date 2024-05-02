package common.utilities;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.JFrame;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellNotAvailableException;
import com.profesorfalken.jpowershell.PowerShellResponse;

import common.toolbox.leanft.LeanFT;
import common.toolbox.sikuli.Sikuli;

//import javax.swing.JFrame;

public class SystemUtils {
	// private String processName;

	private static final Logger LOGGER = LogManager.getLogger(SystemUtils.class);

	public static void killProcessByName(String processName) throws Exception {
		if (isRunning(processName)) {
			getRuntime().exec("taskkill /F /IM " + processName);
		}
	}

	public static void killProcessById(String processId) throws Exception {
		if (isRunning(processId)) {
			getRuntime().exec("Taskkill /F /PID " + processId);
		}
	}

	public static boolean isRunning(String processName) throws Exception {
		Process listTasksProcess = getRuntime().exec("tasklist");
		BufferedReader tasksListReader = new BufferedReader(new InputStreamReader(listTasksProcess.getInputStream()));

		String tasksLine;

		while ((tasksLine = tasksListReader.readLine()) != null) {
			if (tasksLine.contains(processName)) {
				return true;
			}
		}

		return false;
	}

	// to get process id from process name
	// note: if multiple processes are present then it would return Array List
	// eg: pid for iexplore = [14578, 15485,12548]
	public static ArrayList<String> getProcessId(String processName) throws Exception {
		Process listTasksProcess = getRuntime().exec("tasklist /FI \"IMAGENAME eq " + processName + "\"");
		BufferedReader tasksListReader = new BufferedReader(new InputStreamReader(listTasksProcess.getInputStream()));

		String tasksLine;
		ArrayList<String> list = new ArrayList<String>();
		String pid = "";
		while ((tasksLine = tasksListReader.readLine()) != null) {
			if (tasksLine.contains(processName)) {
				pid = StringUtils.ExtractBetweenTags(tasksLine, processName, "Console", false).trim();
				list.add(pid);
			}
		}
		return list;
	}

	// to get latest process id when same processes are running multiple times
	// simultaneously
	public static String getCurrentProcessId(String processName) throws Exception {
		ArrayList<String> list = getProcessId(processName);
		return list.get(list.size() - 1);
	}

	public static Runtime getRuntime() {
		return Runtime.getRuntime();
	}

	public static String user_home() {
		return System.getProperty("user.home");

	}

	public static String user_dir() {
		return System.getProperty("user.dir");

	}

	// to get machine name
	public static String getComputerName() {
		Map<String, String> env = System.getenv();
		if (env.containsKey("COMPUTERNAME"))
			return env.get("COMPUTERNAME");
		else if (env.containsKey("HOSTNAME"))
			return env.get("HOSTNAME");
		else
			return "Unknown Computer";
	}

	// to get screen width as string
	public static Integer getScreenWidth() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		return width;
	}

	// to get screen height as string
	public static Integer getScreenHeight() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = (int) screenSize.getHeight();
		return height;
	}

	// to get command line output in string format

	// to get command line output in string format
	public static String execCmd(String cmd) throws java.io.IOException {
		java.util.Scanner s = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream())
				.useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	// to run powershell command and get output as string
	// parameter: command: eg: "powershell.exe -file
	// C:\\Users\\c70032.CLIENT\\eclipse-workspace\\MBankWeb2JAR\\src\\main\\resources\\MBResource\\powershell\\RunMBankWeb.ps1";
	public static String runPowerShell(String command) throws IOException {
		String line;
		try {
			// Executing the command
			Process powerShellProcess = Runtime.getRuntime().exec(command);
			// Getting the results
			powerShellProcess.getOutputStream().close();
			LOGGER.info("Standard Output:");
			BufferedReader stdout = new BufferedReader(new InputStreamReader(powerShellProcess.getInputStream()));
			while ((line = stdout.readLine()) != null) {
				LOGGER.info(line);
			}
			line = stdout.readLine();
			if ((line) != null) {
				stdout.close();
				return line;
			} else {
				LOGGER.info("Standard Error:");
				BufferedReader stderr = new BufferedReader(new InputStreamReader(powerShellProcess.getErrorStream()));
				while ((line = stderr.readLine()) != null) {
					LOGGER.info(line);
					return line;
				}
				stderr.close();
			}
			// LOGGER.info/("Done");

		} catch (Exception e) {
			e.getMessage();
		}
		return null;
	}

	// to run powershell using Jpowershell
	public static void runJPowerShell(String commandOrFile) {
		PowerShell powerShell = null;
		PowerShellResponse response = null;
		try {
			// Creates PowerShell session
			powerShell = PowerShell.openSession();
			// Execute a command or powershell file in PowerShell session
			if (commandOrFile.endsWith(".ps1")) {
				// Increase timeout to give enough time to the script to finish
				Map<String, String> config = new HashMap<String, String>();
				config.put("maxWait", "80000");
				// Execute script
				powerShell.configuration(config).executeScript(commandOrFile);
			} else {
				// Execute command
				powerShell.executeCommand(commandOrFile);
			}

		} catch (PowerShellNotAvailableException ex) {

		} finally {
			// Always close PowerShell session to free resources.
			if (powerShell != null)
				powerShell.close();
		}
	}

	// to run powershell using Jpowershell and return console ouptut
	public static String getPowerShellOutput(String commandOrFile) {
		PowerShell powerShell = null;
		PowerShellResponse response = null;
		String output = null;
		try {
			// Creates PowerShell session
			powerShell = PowerShell.openSession();
			// Execute a command or powershell file in PowerShell session
			if (commandOrFile.endsWith(".ps1")) {
				// Increase timeout to give enough time to the script to finish
				Map<String, String> config = new HashMap<String, String>();
				config.put("maxWait", "80000");
				// Execute script
				response = powerShell.configuration(config).executeScript(commandOrFile);
			} else {
				// Execute command
				response = powerShell.executeCommand(commandOrFile);
			}
			output = response.getCommandOutput();
			return output;
		} catch (PowerShellNotAvailableException ex) {

		} finally {
			// Always close PowerShell session to free resources.
			if (powerShell != null)
				powerShell.close();
		}
		return output;
	}

	// to get Today's name
	public static String getTodayName() {
		LocalDate date = LocalDate.now();
		DayOfWeek dow = date.getDayOfWeek();
		String dayName = dow.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
		return dayName;
	}

	// to get Today's no
	public static Integer getTodayNo() {
		LocalDate date = LocalDate.now();
		DayOfWeek dow = date.getDayOfWeek();
		String dayNo = dow.getDisplayName(TextStyle.NARROW_STANDALONE, Locale.ENGLISH);
		return Integer.valueOf(dayNo);
	}

	// get dateand time stamp based on input format
	// eg: ddMMyy or ddMMYY or ddMMyyHHmmss
	public static String getDateTimeStamp(String dateTimeFormat) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		DateFormat dateFormat = new SimpleDateFormat(dateTimeFormat);
		return dateFormat.format(timestamp);
	}

	// to get relative x and y coordinates
	public static int getRelativeXCoordinate(Integer x, Integer screenWidth) {
		return (int) ((1.0 * x / screenWidth) * getScreenWidth());
	}

	// to get relative y coordinate
	public static int getRelativeYCoordinate(Integer y, Integer screenHeight) {
		return (int) ((1.0 * y / screenHeight) * getScreenHeight());
	}

	// get time duration an activity took
	public static long getTimeDurationInSeconds(long startTime) {
		long endTime = System.currentTimeMillis();
		return (endTime - startTime) / 1000;
	}

	// timeStamp as variable in config file
	// which will parsed as below method using java reflection call
	public static String timeStamp() {
		return getDateTimeStamp("HHmm");

	}

	// to get new date after adding or subtracting no.of days from given date
	// dd/MM/yyyy format
	public static String getNewDate(String date, int daysToAdd, String outputFormat) throws ParseException {

		// Get date from the label
		Date inputDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(date);

		// Format the date
		DateFormat dateFormat = new SimpleDateFormat(outputFormat);
		String outputDate = dateFormat.format(inputDate);

		// Add a days to Calendar
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateFormat.parse(outputDate));
		cal.add(Calendar.DATE, daysToAdd);
		String newDate = dateFormat.format(cal.getTime());
		return newDate;

	}

	// To establish connection to remote servers via SSH client java library
	public static Session getRemoteSession(String host, String user, String password) {
		try {
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			return session;
		} catch (Exception e) {
			LOGGER.info(e);
		}
		return null;
	}

	// To establish remote channel using java SSH client
	public static Channel getRemoteChannel(String host, String user, String password) {
		try {
			Session session = getRemoteSession(host, user, password);
			if (session != null) {
				Channel channel = session.openChannel("exec");
				return channel;
			}
		} catch (Exception e) {
			LOGGER.info(e);
		}
		return null;
	}

	// To execute remote command using java SSH client
	public static void executeRemoteCommand(String host, String user, String password, String cmd) {
		try {
			Channel channel = getRemoteChannel(host, user, password);
			if (channel != null) {
				((ChannelExec) channel).setCommand(cmd);
				channel.connect();
			}
		} catch (Exception e) {
			LOGGER.info(e);
		}
	}

	// to get month name from date

	public static String getMonthName(int num) {
		String month = "wrong";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		if (num >= 0 && num <= 12) {
			month = months[num - 1];
		}
		return month;

	}

	public static Integer getMonthNumber(String monthName) {
		try {
			Integer month = Month.valueOf(monthName.toUpperCase()).getValue();
			return month;
		} catch (Exception e) {
			LOGGER.info(e);
		}
		return null;
	}
	
	public static int getDayInt(Date date) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
	    return Integer.parseInt(dateFormat.format(date));
	}
	public static int getMonthInt(Date date) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
	    return Integer.parseInt(dateFormat.format(date));
	}
	
	public static int getYearInt(Date date) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
	    return Integer.parseInt(dateFormat.format(date));
	}
	// eg format = dd/MM/yyyy or dd-MM-yyyy
	//date=02/10/2222
	public static Integer getMonthNumber(String date, String format) throws ParseException {		
		Date date1=new SimpleDateFormat(format).parse(date);  
		Integer month = getMonthInt(date1);
		return month;
	}
	
	public static Integer getYearNumber(String date, String format) throws ParseException {		
		Date date1=new SimpleDateFormat(format).parse(date);  
		Integer month = getYearInt(date1);
		return month;
	}
	
	public static Integer getDayNumber(String date, String format) throws ParseException {		
		Date date1=new SimpleDateFormat(format).parse(date);  
		Integer month = getDayInt(date1);
		return month;
	}
	
	// inputDate = 30/09/2022 , format = "dd-MMM-yyyy", "MM dd, yyyy","E, MMM dd yyyy", "E, MMM dd yyyy HH:mm:ss", "dd-MMM-yyyy HH:mm:ss"
	public static String getFormattedDate(String inputDate, String format) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
	    SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy");
	    Date date = format1.parse(inputDate);
	   return format2.format(date);  
	}
	
	public static String getLastDayOfTheMonth(String date,String format) {
        String lastDayOfTheMonth = "";

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try{
        java.util.Date dt= formatter.parse(date);
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(dt);  

        calendar.add(Calendar.MONTH, 1);  
        calendar.set(Calendar.DAY_OF_MONTH, 1);  
        calendar.add(Calendar.DATE, -1);  

        java.util.Date lastDay = calendar.getTime();  

        lastDayOfTheMonth = formatter.format(lastDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lastDayOfTheMonth;
    }

	// ************** below are testing purpose only **********
	// @Test
	public void test_screensize() {
		JFrame frame = new JFrame("Untitled - Notepad");
		// get the screen size as a java dimension
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// get 2/3 of the height, and 2/3 of the width
		int height = screenSize.height * 3 / 3;
		int width = screenSize.width * 3 / 3;

		// set the jframe height and width
		frame.setPreferredSize(new Dimension(width, height));
		frame.toFront();
		frame.requestFocus();
		frame.setAlwaysOnTop(true);
	}

	// @Test
	public void test_machine_name() {

		LOGGER.info("Resolution : " + getScreenWidth() + "x" + getScreenHeight());

	}

	// @Test
	public void test_get_cmd_output() throws IOException, InterruptedException {
		// String cmd = "for /f \"tokens=2 USEBACKQ\" %f IN (`tasklist /NH /FI
		// \"WINDOWTITLE eq This page can’t be displayed - Internet
		// Explorer*\"`) Do set ourPID=%f";
		// String cmd = "for /F \"tokens=1,2\" %i in ('tasklist') do (if \"%i\"
		// equ \"notepad.exe\" (set x=%j)echo %x%";
		// String cmd = "for /f \"tokens=21 USEBACKQ\" %f IN (`tasklist /NH /FI
		// \"WINDOWTITLE eq how to get own process*\"`) Do set ourPID=%f";
		// String cmd = "tasklist /FI \"IMAGENAME eq iexplore.exe\" /FI
		// \"WINDOWTITLE eq Downloads*\"";
		String process = "IEDriverServer.exe";
		String cmd = "tasklist /FI \"IMAGENAME eq " + process + "\"";

		LOGGER.info("cmd : " + cmd);
		String result = execCmd(cmd);
		String pid = StringUtils.ExtractBetweenTags(result, "iexplore.exe", "Console", false).trim();
		LOGGER.info("result : " + pid);
		// StringUtils.ExtractBetweenTags(result, "iexplore.exe", "Console",
		// false);
		// execCmd("Taskkill /F /PID "+pid);
	}

	// @Test
	public void test_getpid() throws Exception {
		// String pid = SystemUtils.getCurrentProcessId("IEDriverServer.exe");
		// LOGGER.info("current crhome pid: "+pid);
		// SystemUtils.killProcessById(pid);
		// LeanFT.startRuntimeEngine();
		String cmd = "dir /b/s " + System.getProperty("user.dir") + "\\src\\*config.xml";
		LOGGER.info("cmd input : " + cmd);
		String result = execCmd(cmd);
		LOGGER.info("cmd output : " + result);

	}

	// @Test
	public void test_day_of_week() throws IOException {

		LocalDate date = LocalDate.now();
		DayOfWeek dow = date.getDayOfWeek();
		LOGGER.info("Enum = " + dow);

		String dayName = dow.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
		LOGGER.info("FULL = " + dayName);

		dayName = dow.getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH);
		LOGGER.info("FULL_STANDALONE = " + dayName);

		dayName = dow.getDisplayName(TextStyle.NARROW, Locale.ENGLISH);
		LOGGER.info("NARROW = " + dayName);

		dayName = dow.getDisplayName(TextStyle.NARROW_STANDALONE, Locale.ENGLISH);
		LOGGER.info("NARROW_STANDALONE = " + dayName);

		dayName = dow.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
		LOGGER.info("SHORT = " + dayName);

		dayName = dow.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.ENGLISH);
		LOGGER.info("SHORT_STANDALONE = " + dayName);
	}

	// test powershell
	// @Test
	public void test_powershell() {
		String dateStamp = SystemUtils.getDateTimeStamp("ddMMyy");
		String filePath = "\\\\entpublic.infau.wbcau.westpac.com.au\\Enterprise\\Nishant\\test.txt";
		String text = dateStamp + "_1";
		String command = "$file = " + StringUtils.quote(filePath) + "\n" + "$text = " + StringUtils.quote(text) + "\n"
				+ "$text | Set-Content $file";

		// String command = StringUtils.quote(text)+" | Set-Content
		// "+StringUtils.quote(filePath);
		runJPowerShell(command);
		// LOGGER.info(o);
	}
	
	

	@Test
	public void Test_getYearNumber() throws ParseException, IOException {
		String today = SystemUtils.getDateTimeStamp("dd/MM/yyyy");
		Integer monthNo=getMonthNumber(today, "dd/MM/yyyy")+1;
		LOGGER.info("month name :"+getMonthName(monthNo).toUpperCase());
	}

}