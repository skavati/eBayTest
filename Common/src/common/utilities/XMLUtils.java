package common.utilities;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import ch.qos.logback.core.pattern.parser.Node;
import common.baselib.BaseMethods;
import common.toolbox.selenium.Element;

public class XMLUtils {
	private static final Logger LOGGER = LogManager.getLogger(XMLUtils.class);
	private static DocumentBuilderFactory factory;
	private static DocumentBuilder builder;
	private static Document doc;


	public static void WriteToXml(String fileName, String nodeName, String nodeText)
			throws ParserConfigurationException, SAXException, IOException {
		Date timeStamp = new Date();
		try {
			// define an empty document
			factory = DocumentBuilderFactory.newInstance();
			// disable external entities
			factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			builder = factory.newDocumentBuilder();
		
			// existing file
			Document doc = builder.parse(fileName);

			// root element

			NodeList node = doc.getElementsByTagName(nodeName);

			node.item(0).setTextContent(nodeText);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			//transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant
			//transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, ""); // Compliant
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			DOMSource source = new DOMSource(doc);
			// for brand new file to be created use below
			StreamResult result = new StreamResult(new File(fileName));
			// Output to console for testing
			transformer.transform(source, result);
		} catch (ParserConfigurationException pce) {
			LOGGER.info(pce);
		} catch (TransformerException tfe) {
			LOGGER.info(tfe);
		}
	}

	

	public static String ReadFile(String fileName, String id) throws ParserConfigurationException {

		String testinput = null;
		String projectPath = System.getProperty("user.dir");
		LOGGER.info(projectPath);
		File fXmlFile = new File(projectPath + File.separator + fileName);
		LOGGER.info(fXmlFile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		DocumentBuilder dBuilder = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			LOGGER.info(e);
		}
		Document doc = null;
		try {

			if (dBuilder != null) {
				doc = dBuilder.parse(fXmlFile);
			}
		} catch (SAXException e) {
			LOGGER.info(e);
		} catch (IOException e) {
			LOGGER.info(e);
		}
		if (doc!=null) {
		LOGGER.info("Root element :" + doc.getDocumentElement().getNodeName());
		}
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		String expression = String.format("/TestCases/Test[@id='" + id + "']/Name");
		LOGGER.info(expression);
		Node node = null;
		try {
			node = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			LOGGER.info(e);
		}
		testinput = node != null ? (((org.w3c.dom.Node) node).getTextContent()) : "cannot read the test case xml file";
		return testinput;

	}

	public static String getNodeText(String fileName, String nodeName)  {
		String nodeText = null;
		try {
			System.setProperty("javax.xml.parsers.DocumentBuilderFactory","com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			// disable external entities
			try {
				docBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(fileName));

			// normalize text representation
			doc.getDocumentElement().normalize();
			NodeList listOfNodes = doc.getElementsByTagName(nodeName);
			nodeText = listOfNodes.item(0).getTextContent().trim();

		} catch (SAXException | ParserConfigurationException | IOException  e) {
			LOGGER.error(e);
		}
		return nodeText;

	}

	// to get node text from xml file
	public static String getNodeTextFromFile(File file, String nodeName) throws ParserConfigurationException, SAXException, IOException {
		String nodeText = null;
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			// disable external entities
			docBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(file);

			// normalize text representation
			doc.getDocumentElement().normalize();
			NodeList listOfNodes = doc.getElementsByTagName(nodeName);
			nodeText = listOfNodes.item(0).getTextContent().trim();

		} catch (SAXParseException ex) {
			LOGGER.error(ex);
		}
		return nodeText;

	}

	//@Test
	public void getnodevalue() throws ParserConfigurationException, SAXException, IOException {
		String counter = getNodeText(
				"C:\\Users\\l083125\\Automation\\GitRepos\\compassautomation\\IBank\\src\\test\\resources\\IBResource\\config\\config.xml",
				"SecureCodeManualEntry");
		LOGGER.info("test data: " + counter);
	}
	

}
