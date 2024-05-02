package common.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.junit.Test;

import com.opencsv.CSVReader;

public class CSVUtils {
	private static final Logger LOGGER = LogManager.getLogger(CSVUtils.class);

	public static void main(String[] args) throws Exception {
		// Build reader instance
		// Read data.csv
		// Default seperator is comma
		// Default quote character is double quote
		// Start reading from line number 2 (line numbers start from zero)
		try (CSVReader reader = new CSVReader(
				new FileReader("C:\\Users\\l083125\\Automation\\temp\\TestData_IBank_PROD.csv"))) {
			String[] nextLine;
			int scenarioColNo = 0;
			int fromAcctColNo = 0;
			int scenarioNo = 0;
			while ((nextLine = reader.readNext()) != null) {
				for (int colNo = 0; colNo < nextLine.length; colNo++) {
					if (nextLine[colNo].equals("FromAccount")) {
						fromAcctColNo = colNo;
					}
					if (nextLine[colNo].equals("Scenario")) {
						scenarioColNo = colNo;
					}
				}
				if (nextLine[scenarioColNo].equals("Account details(Happy path)")) {
					scenarioNo++;
					if (scenarioNo == 2) {
						String fromAcct = nextLine[fromAcctColNo];
						LOGGER.info(fromAcct);
					}
				}
			}

		} catch (Exception e) {
			LOGGER.error(e);
		}

	}

	// To get csv valuse based on given parameters
	// Eg:
	// file = Full path of csv file
	// repeatedValue = Scenario name
	// repetitionNo = Repetition no of scenario
	// keyColumn = Key header name
	public static String getCSVData(String file, String repeatedValue, Integer repetitionNo, String keyColumn)
			throws IOException {
		String csvValue = "";
		try (CSVReader reader = new CSVReader(new FileReader(file))) {
			String[] nextLine;
			int repeatedValueColNo = 0;
			int keyColNo = 0;
			int repeatNo = 0;
			while ((nextLine = reader.readNext()) != null) {
				for (int colNo = 0; colNo < nextLine.length; colNo++) {
					if (nextLine[colNo].equals(keyColumn)) {
						keyColNo = colNo;
					}

					if (nextLine[colNo].equals(repeatedValue)) {
						repeatedValueColNo = colNo;
					}
				}
				if (nextLine[repeatedValueColNo].equals(repeatedValue)) {
					repeatNo++;
					if (repeatNo == repetitionNo) {
						csvValue = nextLine[keyColNo];
						break;
					}
				}
			}

			return csvValue;

		} catch (Exception e) {
			return "";
		}

	}

	// to get count of repeated cell
	public static Integer getRepeatedCSVCellCount(String file, String repeatedValue) {
		try (CSVReader reader = new CSVReader(new FileReader(file))) {
			String[] nextLine;
			int repeatedValueColNo = 0;
			int repeatNo = 0;
			while ((nextLine = reader.readNext()) != null) {
				for (int colNo = 0; colNo < nextLine.length; colNo++) {
					if (nextLine[colNo].equals(repeatedValue)) {
						repeatedValueColNo = colNo;
					}
				}
				if (nextLine[repeatedValueColNo].equals(repeatedValue)) {
					repeatNo++;
				}
			}

			return repeatNo;

		} catch (Exception e) {
			return 0;
		}
	}

	public static Integer getRowNumber(String file) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			String input;
			int count = 0;
			while ((input = bufferedReader.readLine()) != null) {
				count++;
			}

			System.out.println("Count : " + (count-1));
			return count-1;

		} catch (Exception e) {
			return 0;
		}
	}

// ********************************* Below function are testing purpose only **************
	@Test
	public void test_CSVData() throws IOException {
		String file = "C:\\Users\\user\\Automation\\GitRepo\\CommunityAppTest\\CommunityAppTest\\src\\test\\resources\\AppTestResource\\data\\DeviceList.csv";
		             
		//LOGGER.info("csv value:" + getCSVData(file, "Image Detection", 1, "Document"));
		//Integer count = getRowNumber(file);                 
	String	platform = CSVUtils.getCSVData(file, "1",
				1, "DeviceName");
	System.out.println("DeviceName : " + platform);
		
	}
}
