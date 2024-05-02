package common.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.log4j.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import common.constants.GlobalConstants;

public class ExcelUtils {
	private static final Logger LOGGER = LogManager.getLogger(ExcelUtils.class);
	private static XSSFSheet ExcelWSheet;

	private static XSSFWorkbook ExcelWBook;

	private static XSSFCell Cell;

	private static XSSFRow Row;

	// This method is to read the test data from the Excel cell, in this we are
	// passing parameters as Row num and Col num

	public static String getCellValue(Sheet sheet, int RowNum, int ColNum) throws Exception {

		try {
			DataFormatter formatter = new DataFormatter();
			org.apache.poi.ss.usermodel.Cell Cell = sheet.getRow(RowNum).getCell(ColNum);
			String CellData = formatter.formatCellValue(Cell);

			return CellData;

		} catch (Exception e) {

			return "";

		}

	}

	// This method is to read the test data from the Excel cell, in this we are
	// passing parameters as FileName,sheetName,KeyCell ( scenarioName),Column (
	// eg: Port)

	public static String getCellData(String FileName, String SheetName, String KeyCell, String Column)
			throws Exception {

		try {

			XSSFSheet sheet = GetExcelSheet(FileName, SheetName);
			Integer RowNo = GetRowNo(sheet, KeyCell);
			Integer ColNo = GetCellNo(sheet.getRow(0), Column);
			String CellData = getCellValue(sheet, RowNo, ColNo);
			return CellData;

		} catch (Exception e) {

			return "";

		}

	}

	// This method is to set a value

	public static void setCellData(String fileName, String sheetName, String keyCell, String column, String value) {

		try {
			try (FileInputStream file = new FileInputStream(new File(fileName))) {
				try (XSSFWorkbook workbook = new XSSFWorkbook(file)) {
					XSSFSheet sheet = workbook.getSheet(sheetName);
					Integer rowNo = GetRowNo(sheet, keyCell);
					Integer colNo = GetCellNo(sheet.getRow(0), column);
					sheet.getRow(rowNo).getCell(colNo, XSSFRow.MissingCellPolicy.CREATE_NULL_AS_BLANK);

					if (Cell == null) {
						Cell = sheet.getRow(rowNo).createCell(colNo);
						Cell.setCellValue(value);
					} else {
						Cell.setCellValue(value);
					}

					FileOutputStream fileOut = new FileOutputStream(new File(fileName));
					workbook.write(fileOut);
					fileOut.flush();
					fileOut.close();
					LOGGER.info("cell data after set new value: " + getCellData(fileName, sheetName, keyCell, column));
				}
			}
		} catch (Exception e) {

		}

	}

	// This method is to write the test data from the Excel cell, in this we are
	// passing parameters as FileName,sheetName,KeyCell(
	// scenarioNo),RepeatedCell (ScenarioName),Column (Amount)
	public static void setRepeatedCellData(String fileName, String sheetName, String repeatedCell,
			Integer nthRepeatedCell, String column, String value) throws Exception {
		try {
			try (FileInputStream file = new FileInputStream(fileName)) {
				try (Workbook workbook = WorkbookFactory.create(file)) {
					XSSFSheet sheet = GetExcelSheet(fileName, sheetName);
					// values are not getting updated with XSSFSheet so using Sheet
					Sheet sheet1 = workbook.getSheet(sheetName);
					Integer rowNo = GetRowNo(fileName, sheetName, repeatedCell);
					Integer colNo = GetCellNo(sheet.getRow(0), column);
					org.apache.poi.ss.usermodel.Cell cell = sheet1.getRow(rowNo + (nthRepeatedCell - 1)).getCell(colNo);
					if (cell == null) {
						cell = sheet1.getRow(rowNo + (nthRepeatedCell - 1)).createCell(colNo);
					}
					cell.setCellValue(value);
					FileOutputStream fileOut = new FileOutputStream(fileName);
					workbook.write(fileOut);
					fileOut.flush();
					fileOut.close();
				}
			}
		} catch (Exception e) {

		}

	}

	// This method is to read the test data from the Excel cell, in this we are
	// passing parameters as FileName,sheetName,KeyCell(
	// scenarioNo),RepeatedCell (ScenarioName),Column (Amount)

	public static String getRepeatedCellData(String FileName, String SheetName, String RepeatedCell, String KeyCell,
			String Column) throws Exception {
		XSSFSheet sheet = null;
		try {
			String cellvalue = "";
			sheet = GetExcelSheet(FileName, SheetName);
			if (sheet == null) {
				throw new IllegalArgumentException("sheet is null");
			}
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Integer colNo1 = GetCellNo(sheet.getRow(i), RepeatedCell);
				Integer colNo2 = GetCellNo(sheet.getRow(i), KeyCell);
				Integer colNo3 = GetCellNo(sheet.getRow(0), Column);
				if (colNo1 > -1 && colNo2 > -1) {
					cellvalue = cellToString(sheet.getRow(i).getCell(colNo3));
					break;
				}
			}
			return cellvalue;

		} catch (Exception e) {
			return "";
		}

	}

	// This method is to read the test data from the Excel cell, in this we are
		// passing parameters as FileName,sheetName,RepeatedCell
		// (ScenarioName),Column (AppId), nth repeated cell (first,2nd,thrid etc
		// repeated
		// cell)

		public static String getRepeatedCellData(String fileName, String sheetName, String repeatedCell,
				Integer nthRepeatedCell, String column) throws Exception {

			try {
				String configPath = FileUtils.findFullPath(GlobalConstants.USER_DIR, "config.xml");
				String testDataFile = XMLUtils.getNodeText(configPath, "TestDataFile");
				String cellvalue = "";
				if (testDataFile.contains(".xlsx")) {
					XSSFSheet sheet = GetExcelSheet(fileName, sheetName);
					Integer rowNo = GetRowNo(fileName, sheetName, repeatedCell);
					Integer colNo = GetCellNo(sheet.getRow(0), column);
					cellvalue = cellToString(sheet.getRow(rowNo + (nthRepeatedCell - 1)).getCell(colNo));
				} else {
					String csvFolder = FileUtils.findFullPath(GlobalConstants.USER_DIR, testDataFile);
					String csvFile = csvFolder + "\\" + sheetName + ".csv";
					cellvalue = CSVUtils.getCSVData(csvFile, repeatedCell, nthRepeatedCell, column);
				}
				return cellvalue;
			} catch (Exception e) {
				return "";
			}

		}

	// to get date cell value from excel sheet
	public static Date getDateCellData(String FileName, String SheetName, String KeyCell, String Column)
			throws Exception {

		try {

			XSSFSheet sheet = GetExcelSheet(FileName, SheetName);
			Integer rowNo = GetRowNo(sheet, KeyCell);
			if (sheet == null) {
				throw new IllegalArgumentException("sheet is null");
			}
			Integer colNo = GetCellNo(sheet.getRow(0), Column);
			org.apache.poi.ss.usermodel.Cell cell = sheet.getRow(rowNo).getCell(colNo);
			Date cellData = cell.getDateCellValue();
			return cellData;

		} catch (Exception e) {

		}
		return null;

	}

	// to get boolean cell value from excel sheet
	public static Boolean getBooleanCellData(String FileName, String SheetName, String KeyCell, String Column)
			throws Exception {

		try {
			XSSFSheet sheet = GetExcelSheet(FileName, SheetName);
			Integer rowNo = GetRowNo(sheet, KeyCell);
			if (sheet == null) {
				throw new IllegalArgumentException("sheet is null");
			}
			Integer colNo = GetCellNo(sheet.getRow(0), Column);
			org.apache.poi.ss.usermodel.Cell cell = sheet.getRow(rowNo).getCell(colNo);
			Boolean cellData = cell.getBooleanCellValue();
			return cellData;

		} catch (Exception e) {
			return false;
		}

	}

	// This method is to write in the Excel cell, Row num and Col num are the
	// parameters

	@SuppressWarnings("deprecation")
	public static void setCellData(String CellValue, int RowNum, int ColNum, String Path) throws Exception {

		try {

			Row = ExcelWSheet.getRow(RowNum);

			Cell = Row.getCell(ColNum, org.apache.poi.ss.usermodel.Row.RETURN_BLANK_AS_NULL);

			if (Cell == null) {

				Cell = Row.createCell(ColNum);

				Cell.setCellValue(CellValue);

			} else {

				Cell.setCellValue(CellValue);

			}

			// Constant variables Test Data path and Test Data file name

			FileOutputStream fileOut = new FileOutputStream(Path);

			ExcelWBook.write(fileOut);

			fileOut.flush();

			fileOut.close();

		} catch (Exception e) {

			throw (e);

		}

	}

	// To get sheet from a given file name and sheet name values coming from
	// GlobalConstants
	public static XSSFSheet GetExcelSheet(String FileName, String SheetName) throws IOException, InterruptedException {

		try {
			try (FileInputStream file = new FileInputStream(new File(FileName))) {
				try (XSSFWorkbook workbook = new XSSFWorkbook(file)) {
					XSSFSheet sheet = workbook.getSheet(SheetName);
					return sheet;
				}
			}
		} catch (Exception e) {
			LOGGER.info(e);
		}

		return null;

	}

	// to get Row number from cell content
	public static int GetRowNo(XSSFSheet sheet, String keyCell) {
		int rowNo = -1;
		try {
			DataFormatter formatter = new DataFormatter();

			loops: for (Iterator rowiterator = sheet.rowIterator(); rowiterator.hasNext();) {
				XSSFRow row = (XSSFRow) rowiterator.next();
				for (Iterator celliterator = row.cellIterator(); celliterator.hasNext();) {
					XSSFCell cell = (XSSFCell) celliterator.next();
					String cellvalue = formatter.formatCellValue(cell);
					if (cellvalue.trim().equalsIgnoreCase(keyCell)) {
						rowNo = row.getRowNum();
						break loops;
					}

				}

			}
			return rowNo;
		} catch (Exception e) {
			LOGGER.info("Exception in method 'GetRowNo'" + e.getMessage());
			return -1;
		}

	}

	// to get RowNo from cell content using parameters: file, sheetname,keycell
	public static int GetRowNo(String fileName, String sheetName, String keyCell) {
		int rowNo = -1;
		try {
			DataFormatter formatter = new DataFormatter();
			XSSFSheet sheet = GetExcelSheet(fileName, sheetName);
			loops: for (Iterator rowiterator = sheet.rowIterator(); rowiterator.hasNext();) {
				XSSFRow row = (XSSFRow) rowiterator.next();
				for (Iterator celliterator = row.cellIterator(); celliterator.hasNext();) {
					XSSFCell cell = (XSSFCell) celliterator.next();
					String cellvalue = formatter.formatCellValue(cell);
					if (cellvalue.trim().equalsIgnoreCase(keyCell)) {
						rowNo = row.getRowNum();
						break loops;
					}

				}

			}
			return rowNo;
		} catch (Exception e) {
			LOGGER.info("Exception in method 'GetRowNo'" + e.getMessage());
			return -1;
		}

	}

	// to get Cell number\index from cell content

	@SuppressWarnings("deprecation")
	public static Integer GetCellNo(XSSFRow row, String cellContent) throws Exception {
		int cellNo = -1;
		try {
			for (int cn = 0; cn < row.getLastCellNum(); cn++) {
				XSSFCell c = row.getCell(cn);
				if (c == null || c.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
					// Can't be this cell - it's empty
					continue;

				}
				String cell = cellToString(c).trim();
				if (c.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) { // 2.0
					int intTemp = Float.valueOf(cell).intValue();
					cell = Integer.toString(intTemp);
				}

				if (cellContent.equalsIgnoreCase(cell)) {
					cellNo = cn;
					break;
				}

			}

			return cellNo;

		} catch (Exception e) {

			return -1;
		}
	}

	// to convert all cell types to cell type string
	@SuppressWarnings("deprecation")
	public static String cellToString(org.apache.poi.ss.usermodel.Cell cell) {
		int type;
		Object result;
		type = cell.getCellType();
		switch (type) {

		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC: // numeric
			int numericCellValue = (int) cell.getNumericCellValue();

			result = BigDecimal.valueOf(numericCellValue).toPlainString();
			break;

		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA:
			result = cell.getCellFormula();
			break;

		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING: // String Value
																// in Excel
			result = cell.getStringCellValue();
			break;

		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN: // boolean
																	// value
			result = cell.getBooleanCellValue();
			break;
		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_ERROR:
		default:
			throw new IllegalArgumentException("There is no support for this type of cell");
		}

		return result.toString();
	}

	public static void removeRow(Sheet sheet, int rowIndex) {
		int lastRowNum = sheet.getLastRowNum();
		if (rowIndex >= 0 && rowIndex < lastRowNum) {
			sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
		}
		if (rowIndex == lastRowNum) {
			org.apache.poi.ss.usermodel.Row removingRow = sheet.getRow(rowIndex); // works
																					// for
																					// before
																					// and
																					// after
																					// Excel
																					// 2007
			if (removingRow != null) {
				sheet.removeRow(removingRow);
			}
		}
	}

	// to create temp Excel file
	@SuppressWarnings("deprecation")
	public static void CreateTempExcelFile() throws IOException {

		LOGGER.info("Reading from Excel STARTS...");
		FileInputStream file;
		file = new FileInputStream(new File(System.getProperty("user.dir") + "\\Compass_TestData.xlsx"));
		try (XSSFWorkbook workbook = new XSSFWorkbook(file)) {
			XSSFSheet sheet = workbook.getSheet("Login");

			for (int i = 0; i < sheet.getLastRowNum(); i++) {
				XSSFRow row = (XSSFRow) sheet.getRow(i);
				XSSFCell cell = row.getCell(0);
				if (cell.getCellType() == cell.CELL_TYPE_STRING) {
					if (cell.getRichStringCellValue().getString().equalsIgnoreCase("n")) {
						sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1); // to
																			// remove
																			// row
																			// with
																			// "RunTest=N"
						--i; // to decrease row no.by 1 as row with "RunTest=N" has
								// been removed

					}
				}

			}

			LOGGER.info("Updated RowCount: " + sheet.getLastRowNum());
			// **************** To Replace "Y" with Batch No
			// ********************************

			XSSFSheet sheet1 = workbook.getSheet("ParallelTest");
			Integer keyIndex = sheet.getLastRowNum() / sheet1.getLastRowNum();
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = sheet.getRow(i);
				XSSFCell cell = row.getCell(0);
				double n = ((double) cell.getRowIndex() / (double) keyIndex);

				for (int j = 1; j <= sheet1.getLastRowNum(); j++) {
					if (n <= j && n > (j - 1)) {
						cell.setCellValue("B" + j);
						break;
					} else if (n > j) {
						cell.setCellValue("B1"); // to replace remainder rows with
													// B1

					}
				}

			}

			// ******************* end
			// ***********************************************************
			file.close();
			FileOutputStream fileOut = new FileOutputStream(
					new File(System.getProperty("user.dir") + "\\Compass_TestData_Temp.xlsx")); // new
																								// file
																								// gets
																								// created
			workbook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		}
		LOGGER.info("Excel updated");

		// to get numeric value from string
		Matcher m = Pattern.compile("\\d+").matcher("B5");
		while (m.find()) {
			LOGGER.info("Batch No: " + m.group(0));
		}

	}

	// to get any cell value including blank

	@SuppressWarnings("deprecation")
	public static String[] rowToString(org.apache.poi.ss.usermodel.Row row) {
		Iterator<org.apache.poi.ss.usermodel.Cell> cells = row.cellIterator();
		String[] data = new String[row.getLastCellNum()];

		int previousCell = 0;

		org.apache.poi.ss.usermodel.Cell cell = cells.next();
		int currentCell = cell.getColumnIndex();

		while (true) {
			if (previousCell == currentCell) {
				switch (cell.getCellType()) {
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
					data[previousCell] = cell.getNumericCellValue() + "";
					break;
				case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
					data[previousCell] = cell.getStringCellValue();
					break;

				}
				if (cells.hasNext()) {
					cell = cells.next();
					currentCell = cell.getColumnIndex();
				} else {
					break;
				}

			} else {
				data[previousCell] = "";
			}
			previousCell++;

		}

		return data;

	}

	public static Integer getRepeatedCellCount(String FileName, String SheetName, String KeyCell, String Column)
			throws Exception {

		try {
			String configPath = FileUtils.findFullPath(System.getProperty("user.dir"), "config.xml");
			String testDataFile = XMLUtils.getNodeText(configPath, "TestDataFile");
			Integer count = 0;
			if (testDataFile.contains(".xlsx")) {
				XSSFSheet sheet = GetExcelSheet(FileName, SheetName);
				Integer colNo = GetCellNo(sheet.getRow(0), Column);
				for (int i = 1; i <= sheet.getLastRowNum(); i++) {
					Integer colNo1 = GetCellNo(sheet.getRow(i), KeyCell);
					if (colNo1 > -1 && colNo1.equals(colNo)) {
						count = count + 1;
					}
				}
			} else {
				String csvFolder = FileUtils.findFullPath(System.getProperty("user.dir"), testDataFile);
				String csvFile = csvFolder + "\\" + SheetName + ".csv";
				count = CSVUtils.getRepeatedCSVCellCount(csvFile, KeyCell);
			}
			return count;

		} catch (Exception e) {
			return 0;
		}

	}

	// verify if repeated scenario is skipped
	public static Boolean IsScenarioSkipped(String FileName, String SheetName, String KeyCell, String Column) {
		try {
			XSSFSheet sheet = GetExcelSheet(FileName, SheetName);
			Integer colNo = GetCellNo(sheet.getRow(0), Column);
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Integer colNo1 = GetCellNo(sheet.getRow(i), KeyCell);
				if (colNo1 > -1 && colNo1.equals(colNo)) {
					LOGGER.info("value of RunTest: " + sheet.getRow(i).getCell(0).getStringCellValue().toLowerCase());
					if (sheet.getRow(i).getCell(0).getStringCellValue().toLowerCase().contentEquals("y")) {
						return false;
					}
				}
			}

		} catch (Exception e) {
			return true;
		}
		return true;
	}

}