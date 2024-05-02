package common.utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.apache.log4j.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import common.baselib.BaseMethods;

public class TableUtils {

	private static final Logger LOGGER = LogManager.getLogger(FileUtils.class);
	private static DocumentBuilderFactory factory;
	private static DocumentBuilder builder;
	private static Document doc;

	public static void TableToXML(WebDriver driver, String KeyColumn) {	
		// ................. Exmaple 3 .........................
		Date timeStamp = new Date();
		try {
			factory = DocumentBuilderFactory.newInstance();
			factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			builder = factory.newDocumentBuilder();
			doc = builder.newDocument();
			// root element
			Element xmlfile = doc.createElement("xmlfile");
			xmlfile.setAttribute("time", timeStamp.toString());
			doc.appendChild(xmlfile);
			// XML Storage Element
			Element XMLStorage = doc.createElement("XMLStorage");
			xmlfile.appendChild(XMLStorage);
			WebElement table = GetTable(driver, KeyColumn);
			List<WebElement> allRows = null;
			if (table !=null) {
			 allRows = table.findElements(By.tagName("tr"));
			}
			int i = 0;

			for (WebElement row : allRows) {
				if (!row.isDisplayed())
					continue;
				i += 1;
				Element Row = doc.createElement("Row" + i);
				XMLStorage.appendChild(Row);
				List<WebElement> cells = row.findElements(By.tagName("td"));

				int j = 0;
				for (WebElement cell : cells) {
					if (!cell.isDisplayed())
						continue;
					j += 1;
					if (cell.getText().isEmpty() || cell.getText().contentEquals("  "))
						continue;
					Element Col = doc.createElement("Col" + j);
					Row.appendChild(Col);
					Col.appendChild(doc.createTextNode(cell.getText()));
				}
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
			transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("C:\\TEMP\\TESTJAVA.xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			LOGGER.info("File saved!");

		} catch (ParserConfigurationException pce) {
			LOGGER.info(pce);
		} catch (TransformerException tfe) {
			LOGGER.info(tfe);
		}
	}

	// To get table instancce
	// arguments : driver, KeyColumn ( pick any cell value on the screen)
	public static WebElement GetTable(WebDriver driver, String KeyColumn) {
		List<WebElement> allTables = driver.findElements(By.tagName("table"));// BaseMethods.GetElements(driver,By.tagName("table"),10);
		WebElement tbl = null;
		ExitLoops: for (WebElement Table : allTables) {
			List<WebElement> cells = Table.findElements(By.tagName("td"));

			for (WebElement cell : cells) {
				if (cell.getText().equalsIgnoreCase(KeyColumn)) {
					tbl = Table;
					break ExitLoops; // similar to goto label

				}
			}

		}

		return tbl;

	}
	public static void Gettablecelldata(WebDriver driver, By by, String messagetext) throws Exception{

		  try
		  {
			  WebElement Table = BaseMethods.GetElement(driver, by);
		   	  // Get all rows
			  List<WebElement> rows = Table.findElements(By.tagName("tr"));
			  // Print data from each row
			  for (WebElement row : rows) {
				  List<WebElement> cols = row.findElements(By.tagName("td"));
				  for (WebElement col : cols) {
					  System.out.print(messagetext +col.getText() + "\t");
			      }
		   
		      }
		  } catch(Exception e)
		  {
			  LOGGER.info(e);
		  }
	}
	public static void Clicktablecelldata(WebDriver driver, By by) throws Exception{

		  try
		  {
			  WebElement Table = BaseMethods.GetElement(driver, by);
		   	  // Get all rows
			  List<WebElement> rows = Table.findElements(By.tagName("tr"));
			  // Print data from each row
			  for (WebElement row : rows) {
				  List<WebElement> cols = row.findElements(By.tagName("td"));
				  for (WebElement col : cols) {
					  col.click();
			      }
		    
		      }
		  } catch(Exception e)
		  {
			  LOGGER.info(e);
		  }
	}
	public static List<ArrayList<String>> Gettablecelldata(WebDriver driver, By by) throws Exception
	{
		WebElement Table = BaseMethods.GetElement(driver, by);
		// Get all rows
		List<WebElement> rows = Table.findElements(By.tagName("tr"));
		List<ArrayList<String>> rowsData = new ArrayList<ArrayList<String>>();
		// Print data from each row
		for (WebElement row : rows) {
			List<WebElement> cols = row.findElements(By.tagName("td"));
			ArrayList<String> colData = new ArrayList<String>();
			for (WebElement col : cols) {
				colData.add(col.getText().toString());
				}
			rowsData.add(colData);
			}
		return rowsData;
	}

}
