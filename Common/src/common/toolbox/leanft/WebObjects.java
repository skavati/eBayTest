package common.toolbox.leanft;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.log4j.*;
import org.junit.Test;
import org.openqa.selenium.By;

import com.hp.lft.sdk.GeneralLeanFtException;
import com.hp.lft.sdk.RegExpProperty;
import com.hp.lft.sdk.StringProperty;
import com.hp.lft.sdk.web.Button;
import com.hp.lft.sdk.web.ButtonDescription;
import com.hp.lft.sdk.web.CSSDescription;
import com.hp.lft.sdk.web.CheckBox;
import com.hp.lft.sdk.web.CheckBoxDescription;
import com.hp.lft.sdk.web.EditField;
import com.hp.lft.sdk.web.EditFieldDescription;
import com.hp.lft.sdk.web.Link;
import com.hp.lft.sdk.web.LinkDescription;
import com.hp.lft.sdk.web.ListBox;
import com.hp.lft.sdk.web.ListBoxDescription;
import com.hp.lft.sdk.web.ListItem;
import com.hp.lft.sdk.web.NumericField;
import com.hp.lft.sdk.web.NumericFieldDescription;
import com.hp.lft.sdk.web.Page;
import com.hp.lft.sdk.web.Table;
import com.hp.lft.sdk.web.WebElement;
import com.hp.lft.sdk.web.WebElementDescription;
import com.hp.lft.sdk.web.XPathDescription;

import common.baselib.BaseMethods;
import common.toolbox.selenium.Action;
import common.toolbox.selenium.Dropdown;

public class WebObjects {
	private static final Logger LOGGER = LogManager.getLogger(WebObjects.class);
	// to get button reference using object properties and Page object as
	// parameters
	public Button findButton(HashMap<String, Object> objProp, Page page) {
		try {
			Button button;
			if (objProp.containsKey("cSSSelector")) {
				button = page.describe(Button.class, new CSSDescription((String) objProp.get("cSSSelector")));
				return button;
			} else if (objProp.containsKey("xPath")) {
				button = page.describe(Button.class, new XPathDescription("xPath"));
				return button;
			} else if (objProp.containsKey("regExp")) {
				button = page.describe(Button.class, new ButtonDescription.Builder().name((String) objProp.get("name"))
						.className((String) objProp.get("className")).tagName((String) objProp.get("tagName"))
						.role((String) objProp.get("role")).buttonType((String) objProp.get("buttonType")).index((Integer) objProp.get("index"))
						.text((String) objProp.get("text")).visible((Boolean) objProp.get("visible"))
						.accessibilityName(new RegExpProperty((String) objProp.get("accessibilityName"))).build());
				return button;
			} else {
				button = page.describe(Button.class,
						new ButtonDescription.Builder().name((String) objProp.get("name"))
								.className((String) objProp.get("className")).tagName((String) objProp.get("tagName"))
								.role((String) objProp.get("role")).buttonType((String) objProp.get("buttonType")).index((Integer) objProp.get("index"))
								.text((String) objProp.get("text")).visible((Boolean) objProp.get("visible"))
								.accessibilityName((String) objProp.get("accessibilityName")).build());
				return button;
			}

		} catch (GeneralLeanFtException e) {
			
			LOGGER.info(e);
		}
		return null;

	}

	// to find type:WebElement from page
	public WebElement findWebElement(HashMap<String, Object> objProp, Page page) {
		
		try {
			WebElement element;
			if (objProp.containsKey("cSSSelector")) {
				element = page.describe(WebElement.class, new CSSDescription((String) objProp.get("cSSSelector")));
				return element;
			} else if (objProp.containsKey("xPath")) {
				element = page.describe(WebElement.class, new XPathDescription((String) objProp.get("xPath")));
				return element;
			} else if (objProp.containsKey("regExp")) {
				element = page.describe(WebElement.class,
						new WebElementDescription.Builder().tagName((String) objProp.get("tagName"))
								.className((String) objProp.get("className")).index((Integer) objProp.get("index"))
								.visible((Boolean) objProp.get("isVisible"))
								.innerText(new RegExpProperty((String) objProp.get("innerText"))).build());
				return element;
			} else {
				element = page.describe(WebElement.class,
						new WebElementDescription.Builder().tagName((String) objProp.get("tagName"))
								.className((String) objProp.get("className")).index((Integer) objProp.get("index"))
								.visible((Boolean) objProp.get("isVisible"))
								.innerText((String) objProp.get("innerText")).build());
				return element;
			}
		} catch (GeneralLeanFtException e) {
			
			LOGGER.info(e);
		}
		return null;

	}

	// Anvitha
	// link object properties
	public Link findLink(HashMap<String, Object> objProp, Page page) {

		try {
			// Describe a link in the application.
			Link link;
			if (objProp.containsKey("cSSSelector")) {
				link = page.describe(Link.class, new CSSDescription((String) objProp.get("cSSSelector")));
				return link;
			} else if (objProp.containsKey("xPath")) {
				link = page.describe(Link.class, new XPathDescription("xPath"));
				return link;
			} else if (objProp.containsKey("regExp")) {
				link = page.describe(Link.class,
						new LinkDescription.Builder().innerText(new RegExpProperty((String) objProp.get("innerText")))
								.tagName((String) objProp.get("tagName")).id((String) objProp.get("id"))
								.index((Integer) objProp.get("index")).build());
				return link;
			} else {
				link = page.describe(Link.class,
						new LinkDescription.Builder().innerText((String) objProp.get("innerText"))
								.tagName((String) objProp.get("tagName")).id((String) objProp.get("id"))
								.index((Integer) objProp.get("index")).build());
				return link;
			}

		} catch (GeneralLeanFtException e) {
			
			LOGGER.info(e);
		}
		return null;

	}

	// list box (drop down)
	public ListBox findListBox(HashMap<String, Object> objProp, Page page) {

		try {
			ListBox listBox;
			if (objProp.containsKey("cSSSelector")) {
				listBox = page.describe(ListBox.class, new CSSDescription((String) objProp.get("cSSSelector")));
				return listBox;
			} else if (objProp.containsKey("xPath")) {
				listBox = page.describe(ListBox.class, new XPathDescription("xPath"));
				return listBox;
			} else {
				listBox = page.describe(ListBox.class,
						new ListBoxDescription.Builder().name((String) objProp.get("name"))
								.tagName((String) objProp.get("tagName")).className((String) objProp.get("className"))
								.id((String) objProp.get("id")).build());
				return listBox;
			}

		} catch (GeneralLeanFtException e) {
			
			LOGGER.info(e);
		}
		return null;

	}

	// to get Text Box (Edit Field) reference

	public EditField findEditField(HashMap<String, Object> objProp, Page page) {
		try {
			EditField textbox;
			if (objProp.containsKey("cSSSelector")) {
				textbox = page.describe(EditField.class, new CSSDescription((String) objProp.get("cSSSelector")));
				return textbox;
			} else if (objProp.containsKey("xPath")) {
				textbox = page.describe(EditField.class, new XPathDescription("xPath"));
				return textbox;
			} else {
				textbox = page.describe(EditField.class,
						new EditFieldDescription.Builder().name((String) objProp.get("name"))
								.tagName((String) objProp.get("tagName")).type((String) objProp.get("type"))
								.index((Integer) objProp.get("index")).build());
				return textbox;
			}

		} catch (GeneralLeanFtException e) {
			
			LOGGER.info(e);
		}
		return null;

	}

	public NumericField findNumericField(HashMap<String, Object> objProp, Page page) {
		try {
			NumericField textbox;
			if (objProp.containsKey("cSSSelector")) {
				textbox = page.describe(NumericField.class, new CSSDescription((String) objProp.get("cSSSelector")));
				return textbox;
			} else if (objProp.containsKey("xPath")) {
				textbox = page.describe(NumericField.class, new XPathDescription("xPath"));
				return textbox;
			} else {
				textbox = page.describe(NumericField.class, new NumericFieldDescription.Builder()
						.name((String) objProp.get("name")).index((Integer) objProp.get("index")).type((String) objProp.get("type")).tagName((String) objProp.get("tagName")).build());
				return textbox;
			}

		} catch (GeneralLeanFtException e) {
			
			LOGGER.info(e);
		}
		return null;

	}

	// to find type:CheckBox from page
	public CheckBox findCheckBox(HashMap<String, Object> objProp, Page page) {

		try {
			CheckBox element;
			if (objProp.containsKey("cSSSelector")) {
				element = page.describe(CheckBox.class, new CSSDescription((String) objProp.get("cSSSelector")));
				return element;
			} else if (objProp.containsKey("xPath")) {
				element = page.describe(CheckBox.class, new XPathDescription((String) objProp.get("xPath")));
				return element;
			} else if (objProp.containsKey("regExp")) {
				
			} else {
				element = page.describe(CheckBox.class,
						new CheckBoxDescription.Builder().tagName((String) objProp.get("tagName"))
								.className((String) objProp.get("className")).index((Integer) objProp.get("index"))
								.visible((Boolean) objProp.get("isVisible"))
								.innerText((String) objProp.get("innerText"))
								.accessibilityName((String) objProp.get("accessibilityName"))
								.outerHTML((String) objProp.get("outerHTML")).build());
				return element;
			}
		} catch (GeneralLeanFtException e) {
			
			LOGGER.info(e);
		}
		return null;

	}

	// to select dropdown element using full\partial text of list item
	public void select_ListItem(ListBox parent, String text) {
		try {
			parent.click();
			List<ListItem> items = parent.getItems();
			for (int i = 1; i < items.size(); i++) {
				if (items.get(i).getText().trim().contains(text)) {
					parent.select(i);
					break;
				}
			}
		} catch (GeneralLeanFtException e) {
			
			LOGGER.info(e);
		}
	}

	// slect date from date picker
	// input date format "dd/mm/yyyy"(05/09/2017)
	public void selectDateFromDatePicker(String date) throws InterruptedException {
		try {
			String day1 = date.split("/")[0];
			// convert two digits to one if number has leanding zero
			// eg: 05 to 5; 15 to 15
			String day = String.valueOf(Integer.valueOf(day1));
			String month_no = date.split("/")[1];
			// to get month values as "Jan", "Dec" etc...
			String month = new DateFormatSymbols().getMonths()[Integer.valueOf(month_no) - 1].substring(0, 3);
			String year = date.split("/")[2];
			Page page = MobileApp.getPage();
			// click on date picker switch
			WebElement tag_DatePickerSwitch = page.describe(WebElement.class, new WebElementDescription.Builder()
					.className("datepicker-switch").tagName("TH").visible(true).build());
			tag_DatePickerSwitch.click();
			Thread.sleep(500);
			// select year if displayed else click next button
			WebElement tag_Year = page.describe(WebElement.class, new WebElementDescription.Builder()
					.className("datepicker-switch").tagName("TH").innerText(year).build());
			while (!tag_Year.exists(1)) {
				// click next button
				page.describe(WebElement.class,
						new WebElementDescription.Builder().className("next").tagName("TH").visible(true).build())
						.click();
				tag_Year = page.describe(WebElement.class, new WebElementDescription.Builder()
						.className("datepicker-switch").tagName("TH").innerText(year).build());
				if (tag_Year.exists(1)) {
					tag_Year.highlight();
					break;
				}
			}
			// select month
			page.describe(WebElement.class,
					new WebElementDescription.Builder().className("month").tagName("SPAN").innerText(month).build())
					.click();
			Thread.sleep(500);
			// select day
			page.describe(WebElement.class, new WebElementDescription.Builder().tagName("TD").innerText(day).index(0).build())
					.click();

		} catch (GeneralLeanFtException e) {
			
			LOGGER.info(e);
		}
	}
	
	
	// Enter value using Calculator
	// Actual calculation such as add,subtract,multiply will be impletement later
	
	public void EnterFromCalculator(String value){
		try{
			Page page = MobileApp.getPage();
			for (int i = 0; i < value.length(); i++) {
				page.describe(WebElement.class, new WebElementDescription.Builder()
                        .tagName("A").innerText(value.substring(i,i+1)).build()).click();
			}
			page.describe(WebElement.class, new WebElementDescription.Builder()
                    .tagName("SPAN").innerText("Done").build()).click();
		} catch (GeneralLeanFtException e) {
			
			LOGGER.info(e);
		}
	}

	// ************************ below are for testing purpose only ********

	@Test
	public void test_month_format() throws InterruptedException {
		
		// LOGGER.info(new
		// SimpleDateFormat("MMMM").format("05/12/2017"));
		String value = "152.70";
		for (int i = 0; i < value.length(); i++) {
			LOGGER.info(value.substring(i,i+1));
		}
	}

}
