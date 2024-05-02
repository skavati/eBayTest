package common.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AccessController;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedAction;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.log4j.*;
import org.json.JSONException;
import org.json.JSONString;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import common.constants.GlobalConstants;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;

public class StringUtils {
	private static final Logger LOGGER = LogManager.getLogger(StringUtils.class);

	// to compare two strings with wild card (single character)
	public static boolean equals(String s1, String s2, char wildcard) {
		if (s1.length() != s2.length())
			return false;

		for (int i = 0; i < s1.length(); i++) {
			char c1 = s1.charAt(i), c2 = s2.charAt(i);
			if (c1 != wildcard && c2 != wildcard && c1 != c2)
				return false;
		}
		return true;
	}

	// ************ way 2 ***********************

	// Solultion:

	// ************ way 3 ***********************

	// ************ way 4 ***********************

	public static boolean match(String text, String pattern) {
		return text.matches(pattern.replace("?", ".?").replace("*", ".*?"));
	}

	public static boolean matches(String pattern, String text) {
		// add sentinel so don't need to worry about *'s at end of pattern
		text += '\0';
		pattern += '\0';

		int N = pattern.length();

		boolean[] states = new boolean[N + 1];
		boolean[] old = new boolean[N + 1];
		old[0] = true;

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			states = new boolean[N + 1]; // initialized to false
			for (int j = 0; j < N; j++) {
				char p = pattern.charAt(j);

				// hack to handle *'s that match 0 characters
				if (old[j] && (p == '*'))
					old[j + 1] = true;

				if (old[j] && (p == c))
					states[j + 1] = true;
				if (old[j] && (p == '.'))
					states[j + 1] = true;
				if (old[j] && (p == '*'))
					states[j] = true;
				if (old[j] && (p == '*'))
					states[j + 1] = true;
			}
			old = states;
		}
		return states[N];
	}

	// To get numeric string from a given string
	public static String GetNumericString(String text) {
		return text.replaceAll("[^\\d.]", "");
	}

	// To get non numeric string from a given string
	public static String GetNonNumericString(String text) {
		return text.replaceAll("[0-9]", "");
	}

	public static long getRandomNumber(long first, long last) throws Exception {
		Random r = SecureRandom.getInstanceStrong();
		long number = first + ((long) (r.nextDouble() * (last - first)));
		return number;
	}

	public static String getRandomString(int length) {
		try {
			char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
			StringBuilder sb = new StringBuilder();
			Random random = SecureRandom.getInstanceStrong();
			for (int i = 0; i < length; i++) {
				char c = chars[random.nextInt(chars.length)];
				sb.append(c);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.getStackTrace();
		}
		return null;
	}

	public static String getRandomDOB(int start, int last) throws Exception {
		String dOBDay;
		String dOBMonth;
		long day = getRandomNumber(1L, 28L);
		long month = getRandomNumber(1L, 12L);
		long year = getRandomNumber(1970L, 1990L);

		if (day < 10)
			dOBDay = 0 + "" + day;
		else
			dOBDay = "" + day;

		if (month < 10)
			dOBMonth = 0 + "" + month;
		else
			dOBMonth = "" + month;

		return dOBDay + "/" + dOBMonth + "/" + year;
	}

	public static String getMonthNumber(String month) {

		if (("January".contains(month)))
			return "01";
		else if (("February".contains(month)))
			return "02";
		else if (("March".contains(month)))
			return "03";
		else if (("April".contains(month)))
			return "04";
		else if (("May".contains(month)))
			return "05";
		else if (("June".contains(month)))
			return "06";
		else if (("July".contains(month)))
			return "07";
		else if (("August".contains(month)))
			return "08";
		else if (("September".contains(month)))
			return "09";
		else if (("October".contains(month)))
			return "10";
		else if (("November".contains(month)))
			return "11";
		else if (("December".contains(month)))
			return "12";
		else
			return null;
	}

	public static String getMonthName(int month) {

		String[] monthNames = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		return monthNames[month];
	}

	// to get current timestamp based on format
	// format examples: ddMMyyHHmm ,yyyy mm dd hh MM ss,mm/dd/yyyy :HH mm
	public static String getCurrentTimeStamp(String format) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(timestamp);
	}

	public static Timestamp getCurrentTimeStamp1() {
		java.util.Date date = new java.util.Date();
		return (new Timestamp(date.getTime()));
	}

	public static String thousandsFormatter(String amount) {
		Double d = Double.parseDouble(amount);
		String nf = NumberFormat.getInstance().format(d);
		return nf;
	}

	// to get day part in string from a given date
	// examples of parameters : date = 15/04/2016 ,pattern=dd/MM/yyy

	public static Integer getDayFromDate(Date date, String pattern) throws ParseException {
		Calendar myCal = new GregorianCalendar();
		myCal.setTime(date);
		return myCal.get(Calendar.DAY_OF_MONTH);

	}

	// to get month part in string from a given date
	// examples of parameters : date = 15/04/2016 ,pattern=dd/MM/yyy

	public static Integer getMonthFromDate(Date date, String pattern) throws ParseException {
		Calendar myCal = new GregorianCalendar();
		myCal.setTime(date);
		int m = myCal.get(Calendar.MONTH);
		return (m + 1);
	}

	// to get year part in string from a given date
	// examples of parameters : date = 15/04/2016 ,pattern=dd/MM/yyy

	public static Integer getYearFromDate(Date date, String pattern) throws ParseException {
		Calendar myCal = new GregorianCalendar();
		myCal.setTime(date);
		return myCal.get(Calendar.YEAR);

	}

	// to get text between two substrings from a given string
	public static String ExtractBetweenTags(String str, String startStr, String endStr, boolean isTagsInclude) {
		Integer startIndex = str.indexOf(startStr);
		Integer endIndex = str.indexOf(endStr);
		if (isTagsInclude) {
			String subStr = str.substring(startIndex, endIndex + endStr.length());
			return subStr;
		} else {
			String subStr = str.substring(startIndex + startStr.length(), endIndex);
			return subStr;
		}

	}

	public static Object reflectionCall(final Object aninstance, final String classname, final String amethodname,
			final Class[] parameterTypes, final Object[] parameters) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Object res = null;
		try {
			Class<?> aclass;
			if (aninstance == null) {
				aclass = Class.forName(classname);
			} else {
				aclass = aninstance.getClass();
			}
			final Method amethod = aclass.getDeclaredMethod(amethodname, parameterTypes);
			AccessController.doPrivileged(new PrivilegedAction<Object>() {
				public Object run() {
					amethod.setAccessible(true);
					return null; // nothing to return
				}
			});
			res = amethod.invoke(aninstance, parameters);
		} catch (final ClassNotFoundException e) {
		}
		return res;

	}

	// Replace any method name as string between tags with method returned value
	// eg:
	public static String replaceWithReflection(String inputStr, String className, String startSymbol, String endSymbol)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String subStrExc = StringUtils.ExtractBetweenTags(inputStr, startSymbol, endSymbol, false);
		String subStrInc = StringUtils.ExtractBetweenTags(inputStr, startSymbol, endSymbol, true);
		String method = subStrExc;
		Object returnedValue = StringUtils.reflectionCall(null, className, method, null, null);
		String outputStr = inputStr.replace(subStrInc, (String) returnedValue);
		return outputStr;

	}

	// using method overloading
	public static String replaceWithReflection(String inputStr, String className)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String subStrExc = StringUtils.ExtractBetweenTags(inputStr, "|<", ">|", false);
		String subStrInc = StringUtils.ExtractBetweenTags(inputStr, "|<", ">|", true);
		String method = subStrExc;
		Object returnedValue = StringUtils.reflectionCall(null, className, method, null, null);
		String outputStr = inputStr.replace(subStrInc, (String) returnedValue);
		return outputStr;

	}

	// to get random item from list
	public static String getRandom(String[] list) throws NoSuchAlgorithmException {
		Random random = SecureRandom.getInstanceStrong();
		return list[random.nextInt(list.length)];
	}

	// to convert a double to string with two decimal format + zero as leading
	// if it is fraction
	// + adding trailing zero + rounding to two decimals + thousand separator

	// Examples:
	// to match on screen values of Ibank\Mbank web pages
	// input: ".23" output: "0.23" (adding leading zero)
	// input: ".2" output: "0.20" (adding trailing zero)
	// input: "2785.2" output: "2,785.20" (thousand seperator)
	// input: "25894.256" output: "25,894.26" (rounded)

	public static String getDecimalFormat(double decimalNo) {
		NumberFormat df = new DecimalFormat("#,##0.00");
		return df.format(decimalNo);

	}

	public static String getDecimalFormatWithoutTrailingZeros(double decimalNo) {
		NumberFormat df = new DecimalFormat("#,##0");
		return df.format(decimalNo);

	}

	// to convert a string to double
	public static double getStringToDouble(String str) {
		return Double.parseDouble(str.replaceAll(",", ""));
	}

	// to convert a string of number to a straing of certain no.of digits by
	// leading zeros
	// eg: input: 5486 : required no of digits: 9
	// output: 000005486
	public static String getFormattedString(Integer no, Integer noOfDigits) {
		return String.format("%0" + noOfDigits + "d", no);
	}

	// to remove any white spaces and tab characters
	public static String removeWhiteSpace(String str) {
		return str.replaceAll("\\s", "");
	}

	public static String addLinebreaks(String input, int maxLineLength) {
		StringTokenizer tok = new StringTokenizer(input, " ");
		StringBuilder output = new StringBuilder(input.length());
		int lineLen = 0;
		while (tok.hasMoreTokens()) {
			String word = tok.nextToken() + " ";

			if (lineLen + word.length() > maxLineLength) {
				output.append("\n");
				lineLen = 0;
			}
			output.append(word);
			lineLen += word.length();
		}
		return output.toString();
	}

	// to generate random session key ( example: push token)
	public static String generateSessionKey(int length) throws NoSuchAlgorithmException {
		String alphabet = new String("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
		int n = alphabet.length();

		String result = "";
		Random r = SecureRandom.getInstanceStrong();

		for (int i = 0; i < length; i++)
			result = result + alphabet.charAt(r.nextInt(n));

		return result;
	}

	// to create a file and write sring to it
	public void stringToFile(String text, String fileName) {
		try {
			File file = new File(fileName);
			// if file doesnt exists, then create it
			if (!file.exists()) {
				if (!file.createNewFile()) {
					throw new IllegalArgumentException("file not created");
				}
			}
			try (FileWriter fw = new FileWriter(file.getAbsoluteFile())) {
				try (BufferedWriter bw = new BufferedWriter(fw)) {
					bw.write(text);
				}
			}
			// logger.info("Done writing to " + fileName); //For testing
		} catch (IOException e) {
			LOGGER.info("Error: " + e);
		}
	}

	// to quote given string
	public static String quote(String s) {
		return new StringBuilder().append('\'').append(s).append('\'').toString();
	}

	public static String getJSONValue(String jSONString, String field) {
		return jSONString.substring(jSONString.indexOf(field), jSONString.indexOf("\n", jSONString.indexOf(field)))
				.replace(field + "\": \"", "").replace("\"", "").replace(",", "");
	}

	// To replace target string with new string in an input string content
	public static String replace(String str, String target, String replacement) {
		int targetLength = target.length();
		if (targetLength == 0) {
			return str;
		}
		int idx2 = str.indexOf(target);
		if (idx2 < 0) {
			return str;
		}
		StringBuilder buffer = new StringBuilder(targetLength > replacement.length() ? str.length() : str.length() * 2);
		int idx1 = 0;
		do {
			buffer.append(str, idx1, idx2);
			buffer.append(replacement);
			idx1 = idx2 + targetLength;
			idx2 = str.indexOf(target, idx1);
		} while (idx2 > 0);
		buffer.append(str, idx1, str.length());
		return buffer.toString();
	}

	public static String getStringBetweenTwoChars(String input, String startChar, String endChar) {
		try {
			int start = input.indexOf(startChar);
			if (start != -1) {
				int end = input.indexOf(endChar, start + startChar.length());
				if (end != -1) {
					return input.substring(start + startChar.length(), end);
				}
			}
		} catch (Exception e) {
			LOGGER.info(e);
		}
		return input;
	}

	public static String getStringBetweenTwoCharsInAFile(String filePath, String startChar, String endChar) {
		String content = "";
		try {
			Path path = Paths.get(filePath);
			Charset charset = StandardCharsets.UTF_8;
			try {
				content = new String(Files.readAllBytes(path), charset);
			} catch (IOException e) {
				LOGGER.info(e);
			}
		} catch (Exception e) {
			LOGGER.info(e);
		}
		return getStringBetweenTwoChars(content, startChar, endChar);
	}

	// To convert json string to pretty string format
	public static String toPrettyFormat(String jsonString) {
		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(jsonString).getAsJsonObject();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = gson.toJson(json);

		return prettyJson;
	}

	private static void downloadFile(CloudBlobDirectory directory, String inputPath) {
		try {
			for (ListBlobItem blob : directory.listBlobs()) {
				// System.out.println("URI of blob is: " + blob.getUri());
				if (blob.getUri().toString().endsWith("/")) {
					String[] arr = blob.getUri().toString().split("/");
					String directoryname = arr[arr.length - 1];
					CloudBlobDirectory directory1 = directory.getDirectoryReference(directoryname);
					downloadFile(directory1, inputPath);
				} else {
					CloudBlockBlob cloudBlob = (CloudBlockBlob) blob;
					File file = new File(inputPath + "\\" + cloudBlob.getName());
					// System.out.println("Blob Name is: " + cloudBlob.getName());
					file.mkdirs();
					// cloudBlob.downloadToFile(file.getAbsolutePath());

					// cloudBlob.download(new FileOutputStream(inputPath+"\\"+
					// cloudBlob.getName()));

				}
			}
		} catch (StorageException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	
	public static int getLastDigits(int src, int numberOfLastDigits){
	    String in = Integer.toString(src);
	    String lastDigits = in.substring(in.length() - numberOfLastDigits);
	    String out = in.length() < numberOfLastDigits ? in : lastDigits;
	    return Integer.parseInt(out);
	}
	
	public static String getRandomNumber(int min, int max) {
	    Random foo = new Random();
	    int randomNumber = foo.nextInt((max + 1) - min) + min;
	    return String.valueOf(randomNumber);
	}

	// ************** testing purpose only ***********

	// @Test
	public void test_getRandom() throws Exception {

		String exp = "Congratulation*You now have*";
		String act = "Congratulations!You now have created business acct";
		if (matches(exp, act)) {
			LOGGER.info("matched");
		} else {
			LOGGER.info("not matched");
		}

	}

	// @Test
	public void test_getNumericString() {
		String str = "Never share this code.Your Secure Code is 234567 for your request to retrieve CAN.";
		LOGGER.info("Numeric string:" + GetNumericString(str));
	}

	// @Test
	public void TestAzureBlobStorage() throws IOException {
		String inputPath = "C:\\Users\\SarathKavati\\Automation\\Temp";

		try {
			// Include the following imports to use blob APIs.

			// Retrieve storage account from connection-string.
			CloudStorageAccount storageAccount = CloudStorageAccount.parse(
					"DefaultEndpointsProtocol=https;AccountName=creuatdocspipelineblob;AccountKey=3znXIgkm6qWolaZSfRqm5ytNdj4NMiNl/NGEXGdAEJfhR6qYs26vgrqRzkkZNerDtm3PjZv61o+rhY+vNLEx4Q==;EndpointSuffix=core.windows.net");
			// Retrieve storage account from connection-string.

			// Create the blob client.
			CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

			// Retrieve reference to a previously created container.
			CloudBlobContainer container = blobClient.getContainerReference("creuatdocumentspipeline-documents");
			Iterable<ListBlobItem> blobs = container.listBlobs();
			for (ListBlobItem blob : container.listBlobs()) {
				// System.out.println("URI of blob is: " + blob.getUri());

				if (blob.getUri().toString().endsWith("/")) {
					String[] arr = blob.getUri().toString().split("/");
					String directoryname = arr[arr.length - 1];
					CloudBlobDirectory directory = container.getDirectoryReference(directoryname);
					downloadFile(directory, inputPath);

				} else {
					CloudBlockBlob cloudBlob = (CloudBlockBlob) blob;
					File file = new File(inputPath + "\\" + cloudBlob.getName());
					// file.mkdirs();
					// cloudBlob.downloadToFile(file.getAbsolutePath());
					// cloudBlob.download(new FileOutputStream(inputPath + "\\" +
					// cloudBlob.getName()));
				}

			}

		} catch (Exception e) {
			// Output the stack trace.
			e.printStackTrace();
		}
	}

	@Test
	public void downloadBlob() {

		try {
			String inputPath = "C:\\Users\\SarathKavati\\Automation\\Temp";
			CloudStorageAccount storageAccount = CloudStorageAccount.parse(
					"DefaultEndpointsProtocol=https;AccountName=creuatdocspipelineblob;AccountKey=3znXIgkm6qWolaZSfRqm5ytNdj4NMiNl/NGEXGdAEJfhR6qYs26vgrqRzkkZNerDtm3PjZv61o+rhY+vNLEx4Q==;EndpointSuffix=core.windows.net");

			// Create the service client object for credentialed access to the Blob service.
			CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

			// Retrieve a reference to a container.
			CloudBlobContainer container = blobClient.getContainerReference("creuatdocumentspipeline-documents");
			String predictions = "927235b4-54e8-4ea7-82f8-d600ac4211f3\\Predictions";
			//File file = new File(inputPath + "\\" + predictions);
			//file.mkdir();
			FileUtils.createDirectories(inputPath + "\\" + predictions);
			CloudBlob blob1 = container
					.getBlockBlobReference(predictions + "\\927235b4-54e8-4ea7-82f8-d600ac4211f3.json");

			blob1.download(new FileOutputStream(inputPath + "\\" + blob1.getName()));

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (StorageException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
