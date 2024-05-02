package common.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.AgeFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.w3c.dom.Document;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

public class FileUtils {
	private static final Logger LOGGER = LogManager.getLogger(FileUtils.class);

	public static void main(String[] args) throws IOException {
		// Create File In D: Driver.
		String testFile = "C:\\temp.txt";
		File FC = new File(testFile);// Created object of java File class.
		if (!FC.createNewFile()) {
			throw new IllegalArgumentException("file is not created");
		}

		// Writing In to file.
		// Create Object of java FileWriter and BufferedWriter class.
		FileWriter fw = new FileWriter(testFile);
		try (BufferedWriter bw = new BufferedWriter(fw)) {
			bw.write("This Is First Line."); // Writing In To File.
			bw.newLine();// To write next string on new line.
			bw.write("This Is Second Line."); // Writing In To File.

			// Reading from file.
			// Create Object of java FileReader and BufferedReader class.
			FileReader fR = new FileReader(testFile);
			try (BufferedReader bR = new BufferedReader(fR)) {
				String content = "";
				// Loop to read all lines one by one from file and print It.
				while ((content = bR.readLine()) != null) {
					LOGGER.info(content);
				}

			}
		}

	}

	// write some text to file
	public static void writeToFile(String fileFullPath, String inputText) {
		try {
			// create file including all parent folders if not exists
			createFile(fileFullPath);
			// write text to file
			FileWriter fw = new FileWriter(fileFullPath);
			try (BufferedWriter bw = new BufferedWriter(fw)) {
				// clear any exising text
				bw.flush();
				bw.write(inputText);
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	// write some text to file with out creating file or directories
	public static void writeToFileWithoutCreatingFile(String fileFullPath, String inputText) {
		try {
			// write text to file
			try (FileWriter fw = new FileWriter(fileFullPath)) {
				try (BufferedWriter bw = new BufferedWriter(fw)) {
					// clear any exising text
					bw.flush();
					bw.write(inputText);
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	public static void writeToFileUsingFileWriter(String fileFullPath, String inputText) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileFullPath);
			fw.write(inputText);
		} catch (IOException ex) {
			LOGGER.error(ex);
		} finally {
			IOUtils.closeQuietly(fw);
		}
	}

	// To compare two text files
	public void compareTextFiles(Scanner s1, Scanner s2) {

		int count1 = 0;
		int count2 = 0;

		// while we have lines left in both files, compare and
		// print the lines that don't match
		while (s1.hasNextLine() && s2.hasNextLine()) {
			String line1 = s1.nextLine().trim();
			count1++;
			String line2 = s2.nextLine().trim();
			count2++;
			if (!line1.equalsIgnoreCase(line2)) {
				LOGGER.info("Line " + count1 + " differs.");
				LOGGER.info("< " + line1);
				LOGGER.info("> " + line2);
			} else {
				LOGGER.info("Line " + count1 + " matches.");
			}
		}

		// any leftover lines in file1 count as differences
		while (s1.hasNextLine()) {
			String line1 = s1.nextLine();
			count1++;
			LOGGER.info("Line " + count1 + " differs.");
			LOGGER.info("< " + line1);
			LOGGER.info("> ");
		}

		// any leftover lines in file2 count as differences
		while (s2.hasNextLine()) {
			String line2 = s2.nextLine();
			count2++;
			LOGGER.info("Line " + count2 + " differs.");
			LOGGER.info("< ");
			LOGGER.info("> " + line2);
		}
	}

	// To create a parent directory if not exists
	public static void CreateDirectory(String path) {

		File theDir = new File(path);
		// if the directory does not exist, create it
		if (!theDir.exists()) {
			LOGGER.info("creating directory: " + path);
			boolean result = false;
			try {
				theDir.mkdir();
				result = true;
			}

			catch (SecurityException se) {
				// handle it
				LOGGER.info(se);
			}
			if (result) {
				LOGGER.info("DIR created");
			}

		}

		else {
			LOGGER.info("DIR already exists");

		}
	}

	// to create all parent directories if not exists
	public static void createDirectories(String DirPath) {
		Path pathToFile;
		File file;
		try {
			pathToFile = Paths.get(DirPath);
			file = new File(DirPath);
			if (!file.exists()) {
				Files.createDirectories(pathToFile);
			}
		} catch (IOException | IllegalArgumentException | SecurityException e) {

			LOGGER.info(e);
		}
	}

	// to create file after creating all parent directories
	public static void createFile(String filePath) {
		File file = new File(filePath);
		createDirectories(file.getParent());
		try {
			if (file.createNewFile()) {
				LOGGER.info("File created");
			}

		} catch (IOException e) {
			LOGGER.info(e);
		}
	}

	// myFile should only be created using this method to ensure thread safety
	public static synchronized File getFile(String fullpath) throws IOException {
		File file = new File(fullpath);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			if (file.createNewFile()) {
				LOGGER.info("File created");
			}
		}
		return file;
	}

	// to read whole text file
	public static StringBuilder readFile(String path) {
		// Assumes that a file article.rss is available on the SD card
		File file = new File(path);
		StringBuilder builder = new StringBuilder();
		if (!file.exists()) {
			throw new RuntimeException("File not found");
		}
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					LOGGER.info(e);
				}
			}
		}

		return builder;
	}

	// to read txt file
	public static String readFileUsingScanner(String pathname) throws IOException {

		File file = new File(pathname);
		StringBuilder fileContents = new StringBuilder((int) file.length());
		Scanner scanner = new Scanner(file);
		String lineSeparator = System.getProperty("line.separator");

		try {
			while (scanner.hasNextLine()) {
				fileContents.append(scanner.nextLine() + lineSeparator);
			}
			return fileContents.toString();
		} finally {
			scanner.close();
		}
	}

	// To verify if some text exists with in a file
	public static boolean verifyTextExists(String filePath, String txt) {
		int index1 = FileUtils.readFile(filePath).indexOf(txt);
		if (index1 > -1) {
			return true;
		} else
			return false;

	}

	public static String projectDir() {
		return System.getProperty("user.dir");
	}

	// To read PDF file and get string of text inside
	public static String getPDFToString(String filePath) {
		try {
			PDDocument document = null;
			document = PDDocument.load(new File(filePath));
			document.getClass();
			if (!document.isEncrypted()) {
				PDFTextStripperByArea stripper = new PDFTextStripperByArea();
				stripper.setSortByPosition(true);
				PDFTextStripper Tstripper = new PDFTextStripper();
				String str = Tstripper.getText(document);
				// LOGGER.info("PDF content:\n\n" + str);
				return str;
			}
		} catch (Exception e) {
			LOGGER.info(e);
		}
		return null;

	}

	// @Test

	public static void DeleteFileFolder(String path) throws IOException {

		File file = new File(path);
		if (file.exists()) {
			do {
				delete(file);
			} while (file.exists());
		}

	}

	private static void delete(File file) throws IOException {
		if (file.isDirectory()) {
			String fileList[] = file.list();
			if (fileList.length == 0) {
				Files.delete(file.toPath());
			} else {
				int size = fileList.length;
				for (int i = 0; i < size; i++) {
					String fileName = fileList[i];
					String fullPath = file.getPath() + "/" + fileName;
					File fileOrFolder = new File(fullPath);
					delete(fileOrFolder);
				}
			}
		} else {
			Files.delete(file.toPath());
		}
	}

	// to add a key and value to existing properties file
	public static void setPropertiesFile_KeepExisting(String file, String[] keys, String[] values) {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			Properties properties = new Properties();
			in = new FileInputStream(file);
			properties.load(in);

			for (int i = 0; i < keys.length; i++) {
				properties.setProperty(keys[i], values[i]);
			}
			out = new FileOutputStream(file);
			properties.store(out, null);
		} catch (IOException io) {
			LOGGER.error(io);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					LOGGER.info(e);
				}
			}

		}
	}

	// to add a key and value to existing properties file and clear existing
	// values
	public static void setPropertiesFile_ClearExisting(String file, String[] keys, String[] values) {
		FileOutputStream out = null;
		try {
			Properties properties = new Properties();
			try (FileInputStream in = new FileInputStream(file)) {
				out = new FileOutputStream(file);
				properties.load(in);
				for (int i = 0; i < keys.length; i++) {
					properties.setProperty(keys[i], values[i]);
				}
				properties.store(out, null);
			}
		} catch (IOException io) {
			LOGGER.error(io);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					LOGGER.info(e);
				}
			}

		}
	}

	// to get all keys in properties file
	public static void clearPropertiesFile(String file) {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			Properties properties = new Properties();
			in = new FileInputStream(file);
			properties.load(in);
			Set<Object> keys = properties.keySet();
			keys.clear();
			out = new FileOutputStream(file);
			properties.store(out, null);
		} catch (IOException io) {
			LOGGER.error(io);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					LOGGER.info(e);
				}
			}

		}
	}

	// to create a sample Json file
	// Ref: https://www.mkyong.com/java/json-simple-example-read-and-write-json/
	// @Test
	@SuppressWarnings("unchecked")
	public void createJSONFile() {
		JSONObject obj = new JSONObject();
		obj.put("name", "mkyong.com");
		obj.put("age", new Integer(100));

		JSONArray list = new JSONArray();
		list.add("msg 1");
		list.add("msg 2");
		list.add("msg 3");

		obj.put("messages", list);

		try (FileWriter file = new FileWriter("f:\\test.json")) {

			file.write(obj.toJSONString());
			file.flush();

		} catch (IOException e) {
			LOGGER.info(e);
		}

		System.out.print(obj);

	}

	// to get Json object into string format
	public static String getJSONObjectToString(String file) {
		JSONParser parser = new JSONParser();

		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			return jsonObject.toJSONString();
		} catch (IOException | ParseException e) {
			LOGGER.info(e);
		}
		return null;

	}

	// to get Json value from key
	// parameters: file=file full path, key=json key
	// example key:
	// successResponseSegment.customerDataDrivenActual[].loanDetails.LVR
	// Here successResponseSegment,loanDetails are JSONOBJECTS
	// customerDataDrivenActual[] is JSONARRAY
	// LVR is targeted node
	public static String getJsonNodeValue(String file, String nodeName) throws IOException {
		// using org.json library
		String nodeValue = null;
		try {
			// to get whole json into string
			String jsonString = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
			org.json.JSONObject parent = new org.json.JSONObject(jsonString);

			// Iterate through
			String[] node = nodeName.split(Pattern.quote("."));
			for (int i = 0; i < node.length - 1; i++) {
				if (!(node[i].contains("[]"))) {
					parent = (org.json.JSONObject) parent.get(node[i]);
				} else {
					org.json.JSONArray jArray = new org.json.JSONArray(
							parent.get(node[i].replace("[]", "")).toString());
					// LOGGER.info("array node: " + node[i].replace("[]",
					// ""));
					for (int j = 0; j < jArray.length(); j++) {
						org.json.JSONObject jo = jArray.getJSONObject(j);
						// LOGGER.info("array object: " + jo.toString());
						// if jo doesn't contain jsonobject then continue to
						// next iteration
						if (jo.toString().contains(node[i + 1])) {
							parent = (org.json.JSONObject) jo.get(node[i + 1]);
							// LOGGER.info("parent string: " +
							// parent.toString());
							break;
						} else {
							continue;
						}
					}
					i = i + 1;
				}
			}

			nodeValue = parent.get(node[node.length - 1]).toString();
		} catch (JSONException e) {
			// LOGGER.info(e);
		}

		return nodeValue;

	}

	// Using jackson library
	// example nodeName = Data[0].Fields[5].Name
	public static String getJsonNodeValueUsingJackson(String file, String nodeName) throws IOException {
		// using org.json library
		String nodeValue = null;
		try {
			// to get whole json into string
			String jsonString = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
			JsonNode rootNode = new ObjectMapper().readTree(new StringReader(jsonString));
			// Replace '.' with '/', replace '[' with '/', replace ']' with ''
			nodeName = StringUtils.replace(nodeName, ".", "/");
			nodeName = StringUtils.replace(nodeName, "[", "/");
			nodeName = StringUtils.replace(nodeName, "]", "");
			JsonNode node = rootNode.at("/" + nodeName);
			nodeValue = node.asText();
		} catch (JSONException e) {
			LOGGER.info(e);
		}

		return nodeValue;

	}

	// to update json value
	// Below method is not working for all json files.
	// Method needs fine tuning...
	// ****************** DON'T USE IT ******************
	public static void updateJson(org.json.JSONObject obj, String keyString, String newValue) throws Exception {
		// JSONObject json = new JSONObject();
		// get the keys of json object
		Iterator iterator = obj.keys();
		String key = null;
		while (iterator.hasNext()) {
			key = (String) iterator.next();
			// if the key is a string, then update the value
			if ((obj.optJSONArray(key) == null) && (obj.optJSONObject(key) == null)) {
				if ((key.equals(keyString))) {
					// put new value
					obj.put(key, newValue);
					// return obj;
				}
			}

			// if it's jsonobject
			if (obj.optJSONObject(key) != null) {
				updateJson(obj.getJSONObject(key), keyString, newValue);
			}

			// if it's jsonarray
			if (obj.optJSONArray(key) != null) {
				org.json.JSONArray jArray = obj.getJSONArray(key);
				for (int i = 0; i < jArray.length(); i++) {
					// jArray.get(i).toString()
					org.json.JSONObject json = new org.json.JSONObject(jArray.get(i).toString());
					updateJson(json, keyString, newValue);
				}
			}
		}
		// return obj;
	}

	// to get Json Node value for a specified Json file eg: Test data for MBank
	// and
	// IBank in json files
	// eg: Paymentns.json, Services.json etc...
	// Note: Here JSON structure:
	// Root Json Object >> Json Object >> Json Array Object >> Node
	public static String getJsonNodeValue(String file, String parentObjectName, String childArrayName, String keyNode,
			Integer keyNodeRepeatNo) throws Exception {
		String targetNodeValue = "";
		try {
			// to get whole json into string
			String jsonString = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
			org.json.JSONObject root = new org.json.JSONObject(jsonString);
			org.json.JSONObject parentObj = root.getJSONObject(parentObjectName);
			org.json.JSONArray childArray = parentObj.getJSONArray(childArrayName);

			int j = 0;
			for (int i = 0; i < childArray.length(); i++) {
				// jArray.get(i).toString()
				org.json.JSONObject json = new org.json.JSONObject(childArray.get(i).toString());
				// get the keys of json object
				Iterator iterator = json.keys();
				String key = null;
				while (iterator.hasNext()) {
					key = (String) iterator.next();
					// if the key is a string, then update the value
					if (key.equals(keyNode)) {
						j = j + 1;
						if (j == keyNodeRepeatNo) {
							// to get target node value
							targetNodeValue = json.get(key).toString();
							return targetNodeValue;
						}
					}
				}
			}
		} catch (Exception e) {
		}
		return targetNodeValue;
	}

	// to update targeted Json Node value for a specified Json file eg: Test
	// data
	// for MBank and IBank in json files
	// eg: Payments.json, Services.json etc...
	// Note: Here JSON structure:
	// Root Json Object >> Json Object >> Json Array Object >> Node
	public static void updateJsonNodeValue(String file, String parentObjectName, String childArrayName, String keyNode,
			Integer keyNodeRepeatNo, Object newValue) throws Exception {
		try {
			// to get whole json into string
			String jsonString = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
			org.json.JSONObject root = new org.json.JSONObject(jsonString);
			org.json.JSONObject parentObj = root.getJSONObject(parentObjectName);
			org.json.JSONArray childArray = parentObj.getJSONArray(childArrayName);

			int j = 0;
			outerloop: for (int i = 0; i < childArray.length(); i++) {
				// jArray.get(i).toString()
				org.json.JSONObject json = new org.json.JSONObject(childArray.get(i).toString());
				// get the keys of json object
				Iterator iterator = json.keys();
				String key = null;
				while (iterator.hasNext()) {
					key = (String) iterator.next();
					if (key.equals(keyNode)) {
						j = j + 1;
						// to check if keyNode repeated to match keyNodeRepeatNo
						if (j == keyNodeRepeatNo) {
							String target = json.toString();
							// put new value
							json.put(key, newValue);
							String replacement = json.toString();
							String newRootStr = root.toString().replace(target, replacement);
							try (FileWriter file1 = new FileWriter(file)) {
								// to get updated json string in pretty format
								String prettyJsonStr = getPrettyJson(newRootStr);
								file1.write(prettyJsonStr);
							}
							break outerloop;
						}
					}
				}
			}
		} catch (Exception e) {
		}
	}

	// to get json string in pretty format
	public static String getPrettyJson(String inputStr) {
		String prettyStr = "";
		try {
			Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(inputStr).getAsJsonObject();
			prettyStr = gson.toJson(je);
		} catch (Exception e) {
		}
		return prettyStr;
	}

	public static File find(String path, String fName) {
		File f = new File(path);
		if (fName.equalsIgnoreCase(f.getName())) {
			return f;
		}
		if (f.isDirectory()) {
			for (String aChild : f.list()) {
				File ff = find(path + File.separator + aChild, fName);
				if (ff != null)
					return ff;
			}

		}
		return null;
	}

	// to get full path of a file
	public static String findFullPath(String relativepath, String fileName) {
		File file = find(relativepath, fileName);
		if (file != null) {
			return file.getAbsolutePath();
		}
		return "";

	}

	public static String searchFile(String name, File file) {
		File[] list = file.listFiles();
		if (list != null) {
			for (File fil : list) {
				String path = null;
				if (fil.isDirectory()) {
					path = searchFile(name, fil);
					if (path != null) {
						return path;
					}
				} else if (fil.getName().contains(name)) {
					path = fil.getAbsolutePath();
					if (path != null) {
						return path;
					}
				}
			}
		}
		return null; // nothing found
	}

	// to generate list of features to be run in an order
	public static void setFeatureRunOrder(String project) throws IOException {
		try {
			// first clean directory FeatureOrder_IBank
			String featureOrderdir = XMLUtils.getNodeText(System.getProperty("user.dir") + "\\config.xml",
					"FeatureOrderPath");
			// FileUtils.cleanDirectory(new File(featureOrderdir));
			File file1 = new File(System.getProperty("user.dir") + "\\FeatureList_" + project + ".xlsx");
			String fileName = file1.getAbsolutePath();
			XSSFSheet sheet = ExcelUtils.GetExcelSheet(fileName, "List");
			Integer ColNo1 = ExcelUtils.GetCellNo(sheet.getRow(0), "FeatureFile");
			Integer ColNo2 = ExcelUtils.GetCellNo(sheet.getRow(0), "RunOrder");
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				// to get feature file name from excel from each row
				String fileName1 = sheet.getRow(i).getCell(ColNo1).toString();
				int RunOrder = Double.valueOf(sheet.getRow(i).getCell(ColNo2).toString()).intValue();
				// to get full path of a given feature file from excel
				String path = FileUtils.findFullPath(
						System.getProperty("user.dir") + "\\src\\test\\resources\\features",
						sheet.getRow(i).getCell(ColNo1).toString());
				// LOGGER.info("file path:\n" + path);
				File file = new File(path);
				// to create new file in FeatureOrder directory
				// eg: orginal file: Transfers_ib.feature
				// new file: 4_Transfers_ib.feature (here 4 is RunOrder in Excel
				// sheet)
				FileUtils.copyDirectory(file,
						new File(featureOrderdir + "\\" + String.valueOf(RunOrder) + "_" + fileName1));
			}

		} catch (Exception e) {

		}

	}

	// copy one file from one location to another with same or different name
	public static void copyDirectory(File sourceLocation, File targetLocation) throws IOException {

		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				targetLocation.mkdir();
			}

			String[] children = sourceLocation.list();
			for (int i = 0; i < children.length; i++) {
				copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
			}
		} else {

			try (InputStream in = new FileInputStream(sourceLocation)) {
				try (OutputStream out = new FileOutputStream(targetLocation)) {

					// Copy the bits from instream to outstream
					byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}

				}
			}
		}
	}

	// to clean directory ( delete files and sub directories)
	public static void cleanDirectory(File dir) throws IOException {
		for (File file : dir.listFiles()) {
			if (file.isDirectory())
				cleanDirectory(file);
			Files.delete(dir.toPath());
		}
	}

	// to delete only files but not sub directories
	public static void cleanDirectoryButKeepSubDirectories(File dir) throws IOException {
		for (File file : dir.listFiles()) {
			if (!file.isDirectory())
				Files.delete(dir.toPath());
		}
	}

	// to copy a file from JAR file to local drive
	// parameters: sourceFile from jar file: eg:config/config.xml
	// destinationFile: eg:"temp4Auto/resources/config"
	// destination file will be copied into parent folder of jar file location
	// **** This is working *****
	public void copyFileFromJAR(String sourceFile, String destinationFile) throws FileNotFoundException, IOException {
		File file = new File(destinationFile);
		createDirectories(file.getParent());
		InputStream fis = getClass().getClassLoader().getResourceAsStream(sourceFile);
		try (FileOutputStream fos = new FileOutputStream(destinationFile);) {
			byte[] buf = new byte[2048];
			int r;
			while (-1 != (r = fis.read(buf))) {
				fos.write(buf, 0, r);
			}
		}
	}

	public static void copyJarResourceToFolder(JarURLConnection jarConnection, File destDir) {

		try {

			JarFile jarFile = jarConnection.getJarFile();
			for (Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements();) {

				JarEntry jarEntry = e.nextElement();
				String jarEntryName = jarEntry.getName();
				LOGGER.info("jarEntryName: " + jarEntryName);
				String jarConnectionEntryName = jarConnection.getEntryName();
				LOGGER.info("jarConnectionEntryName: " + jarConnectionEntryName);

				if (jarEntryName.startsWith(jarConnectionEntryName)) {
					String filename = jarEntryName.startsWith(jarConnectionEntryName)
							? jarEntryName.substring(jarConnectionEntryName.length())
							: jarEntryName;
					File currentFile = new File(destDir, filename);

					if (jarEntry.isDirectory()) {
						currentFile.mkdirs();
					} else {
						InputStream is = jarFile.getInputStream(jarEntry);
						OutputStream out = org.apache.commons.io.FileUtils.openOutputStream(currentFile);
						org.apache.commons.io.IOUtils.copy(is, out);
						is.close();
						out.close();
					}
				}

				if (jarEntryName.startsWith("config")) {
					File currentFile = new File(destDir, "config.xml");
					if (jarEntry.isDirectory()) {
						currentFile.mkdirs();
					} else {
						InputStream is = jarFile.getInputStream(jarEntry);
						OutputStream out = org.apache.commons.io.FileUtils.openOutputStream(currentFile);
						org.apache.commons.io.IOUtils.copy(is, out);
						is.close();
						out.close();
					}
				}
			}
		} catch (IOException e) {
			LOGGER.info(e);
		}
	}

	// **** This is working *****
	public static JarFile jarForClass(Class<?> clazz, JarFile defaultJar) {
		String path = "/" + clazz.getName().replace('.', '/') + ".class";
		URL jarUrl = clazz.getResource(path);
		if (jarUrl == null) {
			return defaultJar;
		}

		String url = jarUrl.toString();
		int bang = url.indexOf("!");
		String JAR_URI_PREFIX = "jar:file:";
		if (url.startsWith(JAR_URI_PREFIX) && bang != -1) {
			try {
				return new JarFile(url.substring(JAR_URI_PREFIX.length(), bang));
			} catch (IOException e) {
				throw new IllegalStateException("Error loading jar file.", e);
			}
		} else {
			return defaultJar;
		}
	}

	// **** This is working *****
	public static void copyResourcesToDirectory(JarFile fromJar, String jarDir, String destDir) throws IOException {
		for (Enumeration<JarEntry> entries = fromJar.entries(); entries.hasMoreElements();) {
			JarEntry entry = entries.nextElement();
			if (entry.getName().startsWith(jarDir + "/") && !entry.isDirectory()) {
				File dest = new File(destDir + "/" + entry.getName().substring(jarDir.length() + 1));
				File parent = dest.getParentFile();
				if (parent != null) {
					parent.mkdirs();
				}

				try {
					try (FileOutputStream out = new FileOutputStream(dest)) {
						try (InputStream in = fromJar.getInputStream(entry)) {
							byte[] buffer = new byte[8 * 1024];

							int s = 0;
							while ((s = in.read(buffer)) > 0) {
								out.write(buffer, 0, s);
							}
						}
					}
				} catch (IOException e) {
					throw new IOException("Could not copy asset from jar file", e);
				}
			}
		}

	}

	public static String getParentDirectoryPath(String inputPath) {
		File file  = new File(inputPath);
		return file.getParentFile().getAbsolutePath();
	}

	public static String getWorkSpacePath() {
		File file = new File(System.getProperty("user.dir"));
		return file.getParent().toString();
	}

	public static String getParentDirectoryFullPath(String fileOrDirFullPath, String parentDirName) {
		File file = new File(fileOrDirFullPath);
		File parent = file.getParentFile();
		while (!parent.getName().contentEquals(parentDirName)) {
			parent = parent.getParentFile();
			if (parent.getName().contentEquals(parentDirName)) {
				return parent.getAbsolutePath();
			}
		}
		return parent.getAbsolutePath();
	}

	public static String[] findFileNameList(String directoryPath, String fileSuffix) {
		File dir = new File(directoryPath);
		FilenameFilter filefilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				if (lowercaseName.endsWith(fileSuffix)) {
					return true;
				} else {
					return false;
				}
			}
		};

		// List of all files with suffix
		return dir.list(filefilter);
	}

	public static File getTheNewestFile(String filePath, String ext) {
		File theNewestFile = null;
		File dir = new File(filePath);
		FileFilter fileFilter = new WildcardFileFilter("*." + ext);
		File[] files = dir.listFiles(fileFilter);

		if (files.length > 0) {

			Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
			theNewestFile = files[0];
		}

		return theNewestFile;
	}

	public static void deleteFiles(String directoryPath, String fileSuffix) {
		try {
			String[] fileNameList = findFileNameList(directoryPath, fileSuffix);
			for (String fileName : fileNameList) {
				File file = new File(directoryPath + "\\" + fileName);
				Files.delete(file.toPath());
			}
		} catch (Exception e) {

		}
	}

	// To get list of files before cut off date
	public static File[] getFilesBeforeCutoffDate(File directory, int year, int month, int day, int hour, int minute,
			int second) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(year, month - 1, day, hour, minute, second); // January 15th, 2008
		Date cutoffDate = cal.getTime();
		FileFilter fileFilter = new AgeFileFilter(cutoffDate);
		directory.listFiles();
		File[] files = directory.listFiles(fileFilter);
		return files;
	}

	// to get list of files after cut off date
	public static File[] getFilesAfterCutoffDate(File directory, int year, int month, int day, int hour, int minute,
			int second) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(year, month - 1, day, hour, minute, second); // January 15th, 2008
		Date cutoffDate = cal.getTime();
		FileFilter fileFilter = new AgeFileFilter(cutoffDate, false);
		directory.listFiles();
		File[] files = directory.listFiles(fileFilter);
		return files;
	}

	// get current date time
	public static LocalDateTime getCurrentDateTime() {
		LocalDateTime now = LocalDateTime.now();
		return now;
	}

	// get current year
	public static int getCurrentYear() {
		LocalDateTime now = LocalDateTime.now();
		return now.getYear();
	}

	// get current month
	public static int getCurrentMonth() {
		LocalDateTime now = LocalDateTime.now();
		return now.getMonthValue();
	}

	// get current hour
	public static int getCurrentHour() {
		LocalDateTime now = LocalDateTime.now();
		return now.getHour();
	}

	// To update any file by replacing a target string with new replcement string
	// File can be text file, doc or html file
	// Note: This target will be replaced all occurrences
	public static void replaceFileText(String filePath, String target, String replacement) {
		try {
			Path path = Paths.get(filePath);
			Charset charset = StandardCharsets.UTF_8;
			String content = "";
			try {
				content = new String(Files.readAllBytes(path), charset);
			} catch (IOException e) {
				LOGGER.info(e);
			}
			content = StringUtils.replace(content, target, replacement);
			Files.write(path, content.getBytes(charset));
		} catch (Exception e) {
			LOGGER.info(e);
		}
	}

	// To download a blob such as "png, pdf, json" files from Azure blob storage
	// inputPath = path of C: drive where these files will be downloaded to
	// connectionString= Blob storage connection string
	// containerName = name of container eg: "creuatdocumentspipeline-documents"
	// blobName = Full path of blob(file) after contrainer
	// eg:"927235b4-54e8-4ea7-82f8-d600ac4211f3\\Predictions\\927235b4-54e8-4ea7-82f8-d600ac4211f3.json"
	public static void downloadAzureStorageBlob(String inputPath, String connectionString, String containerName,
			String blobName) {
		try {
			CloudStorageAccount storageAccount = CloudStorageAccount.parse(connectionString);
			// Create the service client object for credentialed access to the Blob service.
			CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
			// Retrieve a reference to a container.
			CloudBlobContainer container = blobClient.getContainerReference(containerName);
			CloudBlob blob = container.getBlockBlobReference(blobName);
			blob.download(new FileOutputStream(inputPath + "\\" + blob.getName()));
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

	// to verify if blob exists in blob storage
	public static boolean isBlobPresent(String connectionString, String containerName, String blobName) {
		try {
			CloudStorageAccount storageAccount = CloudStorageAccount.parse(connectionString);
			// Create the service client object for credentialed access to the Blob service.
			CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
			// Retrieve a reference to a container.
			CloudBlobContainer container = blobClient.getContainerReference(containerName);
			/*
			 * for (ListBlobItem blob : container.listBlobs()) {
			 * LOGGER.info("blob.getUri().toString()"+blob.getUri().toString()); if
			 * (blob.getUri().toString().contains(blobName)) { return true; } }
			 */
			
			CloudBlob blob = container.getBlockBlobReference(blobName);
			return blob.exists();
		} catch (URISyntaxException e) {
			//e.printStackTrace();
		} catch (InvalidKeyException e) {
			//e.printStackTrace();
		} catch (StorageException e) {
			//e.printStackTrace();
		}
		return false;
	}
	
	// To get nth file in a given folder
	public static String getNthFile(String parentDir,Integer n) {
		File folder = new File(parentDir);
		File[] listOfFiles = folder.listFiles();
		return listOfFiles[n-1].getName();
	}

	// ********** Below methods are for testing purpose only ***********

	// @Test
	public void test_relativepath() throws IOException {
		// String filePath = findFullPath(System.getProperty("user.dir"),
		// "Element.java");
		Path path = Paths.get(System.getProperty("user.dir") + "\\src\\..\\Element.java");
		Path absolutePath = path.toAbsolutePath();

		LOGGER.info("path" + absolutePath);

	}

	// @Test
	// To read PDF file
	public void test_PDFtoString() {
		getPDFToString(System.getProperty("user.home") + "\\Downloads\\Proof-of-balance-20170419-042920.pdf");
	}

	
	// @Test
	public void test_JsonValues() throws FileNotFoundException, IOException, ParseException {
		String file = "C:\\Users\\C70032\\Automation\\temp\\DigitalMortage_Scenario_5.json";
		String nodeValue = getJsonNodeValue(file,
				"successResponseSegment.customerDataDrivenActual[].loanDetails.finalRateOffer");
		LOGGER.info("nodeValue: " + nodeValue);

	}

	// @Test
	// to compare two json files using JSONAssert class
	public void test_compare_json_files() throws FileNotFoundException, IOException, ParseException {
		// JSONObject jso = new
		JSONParser parser = new JSONParser();
		Object objExp = parser
				.parse(new FileReader("C:\\Users\\c70032.CLIENT\\Automation\\JSON_Compare\\Expected.json"));
		Object objAct = parser.parse(new FileReader("C:\\Users\\c70032.CLIENT\\Automation\\JSON_Compare\\Actual.json"));
		JSONObject jsonObject_exp = (JSONObject) objExp;
		JSONObject jsonObject_act = (JSONObject) objAct;
		LOGGER.info("Expected JSON:\n\n" + jsonObject_exp);
		LOGGER.info("\n\nActual JSON:\n\n" + jsonObject_act);
		List<String> ignoreList = new ArrayList<>();
		ignoreList.add("$.data.timestamp");

	}

	// @Test
	public void test_properitesfile() {
		String[] keys = { "accessNumber2", "securityNumber2", "password2", "clientApiVersion", "issueNumber",
				"deviceID", "serviceName" };
		String[] values = { "123456", "2468", "cat012", "clientApiVersion", "issueNumber", "deviceID", "serviceName" };

		setPropertiesFile_ClearExisting("C:\\Users\\Public\\CompassTestAutomation\\SoapUI\\Properties\\test.properties",
				keys, values);
	}

	// @Test
	public void test_Create_and_Write_File() throws Exception {
		String file = "C:\\Users\\C70032\\Automation\\temp\\test.txt";
		String input = SystemUtils.getDateTimeStamp("ddMMyy");
		writeToFile(file, input);
		LOGGER.info("File text:\n" + readFile(file));
	}

	// @Test
	public void test_write_file() throws Exception {
		LOGGER.info("Enter text ddMMyy_1");
		String dateStamp = SystemUtils.getDateTimeStamp("ddMMyy");
		String filePath = "\\\\entpublic.infau.wbcau.westpac.com.au\\Enterprise\\Public\\Compass ART\\DevOps\\test.txt";
		// to write ddMMyy_1 to test.txt file
		FileUtils.writeToFile(filePath, dateStamp + "_1");
	}

	// @Test
	public void test_filePath() throws Exception {

		File file = new File(System.getProperty("user.dir"));
		LOGGER.info("workspace path" + file.getParent().toString());
		LOGGER.info("parent path: " + FileUtils.getParentDirectoryPath("tools/autoit/jacob-1.18-x64.dll"));

	}

	// @Test
	public void test_compare_files() {
		Iterator it = org.apache.commons.io.FileUtils.iterateFiles(
				new File(System.getProperty("user.home") + "\\Downloads"), new SuffixFileFilter(".pdf"), null);
		while (it.hasNext()) {
			if (((File) it.next()).getName().contains("Transaction-listing-478120554")) {
				LOGGER.info(((File) it.next()).getName());
			}
		}
	}

	// @Test
	public void test_deletefiles() {
		String pathToDownLoadsFolder = System.getProperty("user.home") + "\\Downloads";
		File dir = new File(pathToDownLoadsFolder);
		File[] files = dir.listFiles();

		LocalDateTime now = LocalDateTime.now();
		int year = now.getYear();
		int month = now.getMonthValue();
		int day = now.getDayOfMonth();
		int hour = now.getHour();
		int minute = now.getMinute();
		int second = now.getSecond();
		int millis = now.get(ChronoField.MILLI_OF_SECOND); // Note: no direct getter available.

		LOGGER.info("Year:" + year);
		LOGGER.info("month:" + month);
		LOGGER.info("day:" + day);
		LOGGER.info("hour:" + hour);
		LOGGER.info("minute:" + minute);
		LOGGER.info("second:" + second);

		GregorianCalendar cal = new GregorianCalendar();
		cal.set(year, month - 1, day, 19, 40, second); // January 15th, 2008
		Date cutoffDate = cal.getTime();
		// displayFiles(dir, new AgeFileFilter(cutoffDate));

	}

	// @Test
	public void basicTreeModelRead() throws IOException {
		String file = "C:\\Users\\SarathKavati\\Automation\\GitRepo\\ApiTest\\src\\test\\resources\\ApiResource\\soapui\\ExpectedJSONFiles\\ActualResponse1.json";
		String jsonString = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
		JsonNode rootNode = new ObjectMapper().readTree(new StringReader(jsonString));
		// JsonNode innerNode = rootNode.get("HotelListResponse"); // Get the only
		// element in the root node
		// get an element in that node
		// JsonNode aField = innerNode.get("customerSessionId");
		JsonNode aField = rootNode.at("/Data/0/Fields/4/Value");
		// the customerSessionId has a String value
		String myString = aField.asText();

		// System.out.println("FormType:" + myString);
	}

	// @Test
	public void myTest() throws IOException {
		String expFile = "C:\\Users\\SarathKavati\\Automation\\GitRepo\\ApiTest\\src\\test\\resources\\ApiResource\\soapui\\ExpectedJSONFiles\\Documents_ImageRecognitionResponse31.json";
		String expJsonString = new String(Files.readAllBytes(Paths.get(expFile)), StandardCharsets.UTF_8);

		String actFile = "C:\\Users\\SarathKavati\\Automation\\GitRepo\\ApiTest\\src\\test\\resources\\ApiResource\\soapui\\ResponseFiles\\Documents_ImageRecognitionResponse31.json";
		String actJsonString = new String(Files.readAllBytes(Paths.get(actFile)), StandardCharsets.UTF_8);

		/*
		 * String input1 = expJsonString; String input2 = actJsonString; ObjectMapper om
		 * = new ObjectMapper(); try { Map<String, Object> m1 = (Map<String,
		 * Object>)(om.readValue(input1, Map.class)); Map<String, Object> m2 =
		 * (Map<String, Object>)(om.readValue(input2, Map.class));
		 * System.out.println(m1); System.out.println(m2);
		 * System.out.println(m1.equals(m2)); } catch (Exception e) {
		 * e.printStackTrace(); }
		 */
		/*
		 * Gson g = new Gson(); Type mapType = new TypeToken<Map<String,
		 * Object>>(){}.getType(); Map<String, Object> firstMap =
		 * g.fromJson(expJsonString, mapType); Map<String, Object> secondMap =
		 * g.fromJson(actJsonString, mapType);
		 * System.out.println(Maps.difference(firstMap, secondMap));
		 */
		try {
			JSONAssert.assertEquals(expJsonString, actJsonString, false);
		} catch (AssertionError e) {
			String[] error = e.toString().split(";");
			String str = "";
			for (int i = 0; i < error.length - 1; i++) {
				if (error[i].contains(".Percentage")) {
					// LOGGER.info("Its percentage value mismatch" + error[i]);
					String exp = StringUtils.ExtractBetweenTags(error[i], "Expected:", "got:", false);
					// LOGGER.info("Expected percentage:" + exp.trim());
					String[] act = error[i].split("got:");
					// LOGGER.info("actual percentage:" + act[1].trim());
					double de = Double.parseDouble(exp.trim());
					double da = Double.parseDouble(act[1].trim());
					double result = de - da;
					// To verify if difference between two percentage values more than 10%
					if (result > 0.1) {
						LOGGER.info("Percentage values mismatched\n" + error[i]);
						/*
						 * throw new Exception("\"" + comparePercentageNodes[i].trim() +
						 * "\" JSON node values are differed by more than 10% :<br /><br />" +
						 * "Expected Json node value:\n" + expectedNodeValue + "<br /><br />" +
						 * "Actual Json node value:<br />" + actualNodeValue + "<br /><br />" +
						 * stepInfo);
						 */
					}

				} else if (error[i].contains(".Id")) {
					continue;
				} else if (error[i].contains("Id=")) {
					continue;
				} else if (error[i].contains("RequisitionId")) {
					continue;
				} else {
					str = str + error[i];
				}

			}
			if (!str.contentEquals("")) {
				LOGGER.info("Other values mismatched:\n" + str);
			}
		}

	}

	//@Test
	public void test_getParentDirPath() {
		String connectionString="DefaultEndpointsProtocol=https;AccountName=creuatdocspipelineblob;AccountKey=3znXIgkm6qWolaZSfRqm5ytNdj4NMiNl/NGEXGdAEJfhR6qYs26vgrqRzkkZNerDtm3PjZv61o+rhY+vNLEx4Q==;EndpointSuffix=core.windows.net";
		String containerName="creuatdocumentspipeline-documents";
		String blobName="927235b4-54e8-4ea7-82f8-d600ac4211f3\\Predictions\\927235b4-54e8-4ea7-82f8-d600ac4211f3.json";
		if(isBlobPresent(connectionString, containerName, blobName)) {
			LOGGER.info("Blob is present");
		}else {
			LOGGER.info("Blob doesn't exist");
		}
	}
	
	//@Test
	public void test_getNthFileinFolder() throws IOException {
		File folder = new File("C:\\Users\\SarathKavati\\Automation\\GitRepo\\Credabl.Documents.Pipeline.Test\\ApiTestDocumentsPipeline\\src\\test\\resources\\ApiResource\\soapui\\ImageCollection");
		File[] listOfFiles = folder.listFiles();

		/*
		 * for (File file : listOfFiles) { if (file.isFile()) {
		 * LOGGER.info(file.getName()); } }
		 */
		LOGGER.info(listOfFiles[0].getName());
	}
	
	@Test
	public void whenUsingNIOFiles_thenReturnTotalNumberOfLines() throws IOException {
		File folder = new File("C:\\Users\\SarathKavati\\Automation\\GitRepo\\Credabl.Documents.Pipeline.Test\\ApiTestDocumentsPipeline\\src\\test\\resources\\ApiResource\\soapui\\ImageCollection");
		try (Stream<Path> paths = Files.walk(Paths.get("C:\\Users\\SarathKavati\\Automation\\GitRepo\\Credabl.Documents.Pipeline.Test\\ApiTestDocumentsPipeline\\src\\test\\resources\\ApiResource\\soapui\\ImageCollection"))) {
		    paths
		        .filter(Files::isRegularFile)
		        .forEach(System.out::println);
		} 
	
	}

}
