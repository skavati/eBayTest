package common.utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;


import org.apache.log4j.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRelation;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.junit.Test;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STUnderline;

public class WordUtils {
	private static final Logger LOGGER = LogManager.getLogger(WordUtils.class);
	// setRun(paragraph.createRun() , "Calibre LIght" , 10, "2b5079" , "Some
	// string" , false, false);

	private static void setRun(XWPFRun run, String fontFamily, int fontSize, String colorRGB, String text, boolean bold,
			boolean addBreak) {
		run.setFontFamily(fontFamily);
		run.setFontSize(fontSize);
		run.setColor(colorRGB);
		run.setText(text);
		run.setBold(bold);
		if (addBreak)
			run.addBreak();
	}

	// to get row no. from key cell value
	@SuppressWarnings("resource")
	public static int getRowNo(String file, String keyCell) throws Exception {
		XWPFDocument doc = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			doc = new XWPFDocument(fis);
			List<XWPFTable> tables = doc.getTables();
			// to get first table if there are multiple tables in the document
			XWPFTable table = tables.get(0);
			XWPFTableRow[] rows = table.getRows().toArray(new XWPFTableRow[0]);
			for (int r = 0; r < rows.length; r++) {
				if (r > 0) {
					XWPFTableRow row = rows[r];
					List<XWPFTableCell> cells = row.getTableCells();
					for (XWPFTableCell cell : cells) {
						if (cell.getText().contentEquals(keyCell)) {
							return r;
						}
					}
				}
			}
		} catch (Exception e) {

		} finally {
			if (doc != null) {
				doc.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
		return 0;
	}

	// https://stackoverflow.com/questions/37928363/how-to-add-a-hyperlink-to-a-xwpfrun
	public static void appendExternalHyperlink(String url, String text, XWPFParagraph paragraph) {

		// Add the link as External relationship
		String id = paragraph.getDocument().getPackagePart()
				.addExternalRelationship(url, XWPFRelation.HYPERLINK.getRelation()).getId();

		// Append the link and bind it to the relationship
		CTHyperlink cLink = paragraph.getCTP().addNewHyperlink();
		cLink.setId(id);

		// Create the linked text
		CTText ctText = CTText.Factory.newInstance();
		ctText.setStringValue(text);
		CTR ctr = CTR.Factory.newInstance();
		ctr.setTArray(new CTText[] { ctText });
		CTRPr rpr = ctr.addNewRPr();
		CTColor colour = CTColor.Factory.newInstance();
		colour.setVal("0000FF");
		rpr.setColor(colour);
		CTRPr rpr1 = ctr.addNewRPr();
		rpr1.addNewU().setVal(STUnderline.SINGLE);

		// Insert the linked text into the link
		cLink.setRArray(new CTR[] { ctr });
	}

	public static void addHyperlink(XWPFParagraph para, String text, String bookmark) {
		// Create hyperlink in paragraph
		CTHyperlink cLink = para.getCTP().addNewHyperlink();
		cLink.setAnchor(bookmark);
		// Create the linked text
		CTText ctText = CTText.Factory.newInstance();
		ctText.setStringValue(text);
		CTR ctr = CTR.Factory.newInstance();
		ctr.setTArray(new CTText[] { ctText });

		// Create the formatting
		CTFonts fonts = CTFonts.Factory.newInstance();
		fonts.setAscii("Calibri Light");
		CTRPr rpr = ctr.addNewRPr();
		CTColor colour = CTColor.Factory.newInstance();
		colour.setVal("0000FF");
		rpr.setColor(colour);
		CTRPr rpr1 = ctr.addNewRPr();
		rpr1.addNewU().setVal(STUnderline.SINGLE);

		// Insert the linked text into the link
		cLink.setRArray(new CTR[] { ctr });
	}

	// To update word table with any text
	// Parameter Examples
	// doc:"C:\\Users\\C70032\\Automation\\test.docx"
	// for parameter fontColor use below examples
	// Red:FF0000;Green:00CC00;Black:000000;Maroon:800000;Blue:0000ff
	public static void updateTableWithText(String doc, Integer rowNo, Integer colNo, String text, String fontFamily,
			Integer fontSize, String fontColor, Boolean isClearContents, Boolean isBold, Integer textLength)
			throws IOException {
		XWPFDocument document = null;
		try {
			document = new XWPFDocument(new FileInputStream(doc));
			// to get first table (if there are many tables in a document)
			XWPFTable table = document.getTables().get(0);
			XWPFTableCell cell = table.getRow(rowNo).getCell(colNo);
			// to clear cell contents
			if (isClearContents) {
				cell.getCTTc().setPArray(new CTP[] { CTP.Factory.newInstance() });
				// to clear empty line after clearing all contents
				cell.removeParagraph(0);
			}
			XWPFParagraph p = cell.addParagraph();
			XWPFRun run = p.createRun();
			// if textLenth is null then
			// take whole text passed in as parameter
			// else chop the text by its length specified in parameter
			// (textLength)
			String newText = null;
			if (!(textLength == null)) {
				newText = text.substring(0, Math.min(text.length(), textLength));
			} else {
				newText = text;
			}
			setRun(run, fontFamily, fontSize, fontColor, newText, isBold, false);
			document.write(new FileOutputStream(doc));
		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			if (document != null) {
				document.close();
			}
		}

	}

	// To update word table with image file (screenshot)
	// Parameter Examples
	// doc:"C:\\Users\\C70032\\Automation\\test.docx"
	// imgFile: "C:\\Users\\C70032\\ScreenShots\\290517120920081.png"
	//
	public static void updateTableWithImage(String doc, Integer rowNo, Integer colNo, String imgFile, String format,
			int pixelX, int pixelY, Boolean isClearContents) throws Exception {
		FileInputStream imgFileinput = null;
		try {
			try (XWPFDocument document = new XWPFDocument(new FileInputStream(doc))){
			// to get first table (if there are many tables in a document)
			XWPFTable table = document.getTables().get(0);
			XWPFTableCell cell = table.getRow(rowNo).getCell(colNo);
			// to clear cell contents
			if (isClearContents) {
				cell.getCTTc().setPArray(new CTP[] { CTP.Factory.newInstance() });
				// to clear empty line after clearing all contents
				cell.removeParagraph(0);
			}
			XWPFParagraph p = cell.addParagraph();
			XWPFRun run = p.createRun();

			Integer imgFormat = null;

			switch (format.toLowerCase()) {
			case "png":
				imgFormat = XWPFDocument.PICTURE_TYPE_PNG;
				break;
			case "jpeg":
				imgFormat = XWPFDocument.PICTURE_TYPE_JPEG;
				break;
			case "bmp":
				imgFormat = XWPFDocument.PICTURE_TYPE_BMP;
				break;
			}
			// To pass any text before image
			run.setText(imgFile);
			run.setFontFamily("Arial");
			run.setFontSize(10);
			run.addBreak();
			imgFileinput = new FileInputStream(imgFile);
			// to add a picture
			run.addPicture(imgFileinput, imgFormat, imgFile, Units.toEMU(pixelX), Units.toEMU(pixelY));
			// to save file
			document.write(new FileOutputStream(doc));
			} 
		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			if (imgFileinput != null) {
				imgFileinput.close();				
			} 
		}
	}

	public static int getParagraphLength(String doc, int rowNo, int colNo) throws IOException {
		XWPFDocument document = null;
		try {
			int pl;
			document = new XWPFDocument(new FileInputStream(doc));
			// to get first table (if there are many tables in a document)
			XWPFTable table = document.getTables().get(0);
			XWPFTableCell cell = table.getRow(rowNo).getCell(colNo);
			pl = cell.getParagraphs().size();
			return pl;
		} catch (Exception e) {

		} finally {
			if (document != null) {
				document.close();
			}
		}
		return 0;
	}

	// to get cell
	public static XWPFTableCell getCell(String doc, int rowNo, int colNo) throws IOException {
		XWPFDocument document = null;
		try {
			document = new XWPFDocument(new FileInputStream(doc));
			// to get first table (if there are many tables in a document)
			XWPFTable table = document.getTables().get(0);
			XWPFTableCell cell = table.getRow(rowNo).getCell(colNo);
			return cell;
		} catch (Exception e) {

		} finally {
			if (document != null) {
				document.close();
			}
		}
		return null;
	}

	// to get cell text
	public static String getCellText(String doc, int rowNo, int colNo) throws IOException {
		XWPFDocument document = null;
		try {
			document = new XWPFDocument(new FileInputStream(doc));
			// to get first table (if there are many tables in a document)
			XWPFTable table = document.getTables().get(0);
			XWPFTableCell cell = table.getRow(rowNo).getCell(colNo);
			String txt = cell.getText();
			return txt;
		} catch (Exception e) {

		} finally {
			if (document != null) {
				document.close();
			}
		}
		return "";
	}

	// ---------------Below are testing purpose only-------------

}
