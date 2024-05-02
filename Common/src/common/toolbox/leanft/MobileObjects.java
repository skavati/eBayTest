package common.toolbox.leanft;

import java.util.HashMap;


import org.apache.log4j.*;

import com.hp.lft.sdk.GeneralLeanFtException;
import com.hp.lft.sdk.RegExpProperty;
import com.hp.lft.sdk.mobile.Application;
import com.hp.lft.sdk.mobile.ApplicationDescription;
import com.hp.lft.sdk.mobile.Button;
import com.hp.lft.sdk.mobile.ButtonDescription;
import com.hp.lft.sdk.mobile.CheckBox;
import com.hp.lft.sdk.mobile.CheckBoxDescription;
import com.hp.lft.sdk.mobile.EditField;
import com.hp.lft.sdk.mobile.EditFieldDescription;
import com.hp.lft.sdk.mobile.Label;
import com.hp.lft.sdk.mobile.LabelDescription;
import com.hp.lft.sdk.mobile.Table;
import com.hp.lft.sdk.mobile.TableDescription;
import com.hp.lft.sdk.mobile.UiObject;
import com.hp.lft.sdk.mobile.UiObjectDescription;
import com.hp.lft.sdk.mobile.WebView;
import com.hp.lft.sdk.mobile.WebViewDescription;
import com.hp.lft.sdk.web.Link;
import com.hp.lft.sdk.web.Page;
import com.hp.lft.sdk.web.PageDescription;

import common.utilities.EmailUtils;

public class MobileObjects {
	MobileApp mobileApp = new MobileApp();
	private static final Logger LOGGER = LogManager.getLogger(MobileObjects.class);
	// to get button reference using object properties through HashMap
	public Button findButton(HashMap<String, Object> objProp) {

		try {
			Application app = MobileApp.getApp();
			// Describe a button in the application.
			Button button = app.describe(Button.class,
					new ButtonDescription.Builder().className((String) objProp.get("className"))
							.accessibilityId((String) objProp.get("accessibilityId"))
							.mobileCenterIndex((Integer) objProp.get("mobileCenterIndex"))
							.resourceId((String) objProp.get("resourceId")).text((String) objProp.get("text"))
							.container((String) objProp.get("container")).build());

			return button;
		} catch (GeneralLeanFtException e) {
			
			LOGGER.info(e);
		}
		return null;

	}

	// to get "EditField\TextBox" reference using object properties through
	// HashMap
	public EditField findEditField(HashMap<String, Object> objProp) {

		try {
			Application app = MobileApp.getApp();
			// Describe a EditField in the application.
			EditField textBox = app.describe(EditField.class,
					new EditFieldDescription.Builder().accessibilityId((String) objProp.get("accessibilityId"))
							.className((String) objProp.get("className")).container((String) objProp.get("container"))
							.mobileCenterIndex((Integer) objProp.get("mobileCenterIndex"))
							.resourceId((String) objProp.get("resourceId")).text((String) objProp.get("text")).build());

			return textBox;
		} catch (GeneralLeanFtException e) {
			
			LOGGER.info(e);
		}
		return null;

	}

	// to get "table" reference using object properties through HashMap
	public Table findTable(HashMap<String, Object> objProp) {

		try {
			Application app = MobileApp.getApp();
			Table table = app.describe(Table.class,
					new TableDescription.Builder().accessibilityId((String) objProp.get("accessibilityId"))
							.className((String) objProp.get("className")).container((String) objProp.get("container"))
							.mobileCenterIndex((Integer) objProp.get("mobileCenterIndex"))
							.resourceId((String) objProp.get("resourceId")).build());
			return table;
		} catch (GeneralLeanFtException e) {
			
			LOGGER.info(e);
		}
		return null;

	}

	// to get "Label" reference using object properties through HashMap
	public Label findLabel(HashMap<String, Object> objProp) {

		try {
			Application app = MobileApp.getApp();
			if (objProp.containsKey("regExp")) {
				Label label = app.describe(Label.class, new LabelDescription.Builder()
						.accessibilityId((String) objProp.get("accessibilityId"))
						.className((String) objProp.get("className")).container((String) objProp.get("container"))
						.mobileCenterIndex((Integer) objProp.get("mobileCenterIndex"))
						.resourceId((String) objProp.get("resourceId")).text(new RegExpProperty((String) objProp.get("text"))).build());
				return label;
			} else {
				Label label = app.describe(Label.class, new LabelDescription.Builder()
						.accessibilityId((String) objProp.get("accessibilityId"))
						.className((String) objProp.get("className")).container((String) objProp.get("container"))
						.mobileCenterIndex((Integer) objProp.get("mobileCenterIndex"))
						.resourceId((String) objProp.get("resourceId")).text((String) objProp.get("text")).build());
				return label;
			}
			
			// LOGGER.info("Label text: "+label.getText());
			
		} catch (GeneralLeanFtException e) {
			
			LOGGER.info(e);
		}
		return null;

	}

	// to get "Label" reference using object properties, table (parent), OCRTest
	// and KeyProperty
	public Label findLabel(HashMap<String, Object> objProp, Table table) throws CloneNotSupportedException {

		try {
			Application app = MobileApp.getApp();
			Label[] labelArray = null;

			labelArray = table.findChildren(Label.class,
					new LabelDescription.Builder().accessibilityId((String) objProp.get("accessibilityId"))
							.className((String) objProp.get("className")).container((String) objProp.get("container"))
							.mobileCenterIndex((Integer) objProp.get("mobileCenterIndex"))
							.resourceId((String) objProp.get("resourceId")).text((String) objProp.get("text")).build());

			// for (Label label : labelArray) {

			if (objProp.containsKey("OCRText")) {
				for (Label label : labelArray) {
					if (label.getTextLocations((String) objProp.get("OCRText")).length > 0) {
						return label; // if this doesn't work then use below
						// add extra (indexed) property
						
					}

				}
			} else if (objProp.containsKey("keyProperty")) {
				switch ((String) objProp.get("keyProperty")) {
				case "displayName":
					for (Label label : labelArray) {
						if (label.getDisplayName().trim().equalsIgnoreCase((String) objProp.get("keyValue"))) {
							return label; // if this doesn't work then use
							// below
							// add extra (indexed) property into object property
							// list

							// objProp.put("mobileCenterIndex", (String)label.);
							// return findLabel(objProp);

						}
					}
					break;

				case "text":
					for (Label label : labelArray) {
						if (label.getText().equalsIgnoreCase((String) objProp.get("keyValue"))) {
							return label;// if this doesn't work then use
							// below
							// add extra (indexed) property into object property
							// list
							
						}
					}
					break;

				}

			}
			// }
			// return label;
		} catch (GeneralLeanFtException e) {
			
			LOGGER.info(e);
		}
		return null;

	}

	// to get uiobject reference using object properties through HashMap
	public UiObject findUiObject(HashMap<String, Object> objProp) {

		try {
			Application app = MobileApp.getApp();
			// Describe an uiObject in the application.
			UiObject uiObject = app.describe(UiObject.class,
					new UiObjectDescription.Builder().className((String) objProp.get("className"))
							.accessibilityId((String) objProp.get("accessibilityId"))
							.mobileCenterIndex((Integer) objProp.get("mobileCenterIndex"))
							.resourceId((String) objProp.get("resourceId")).build());

			return uiObject;
		} catch (GeneralLeanFtException e) {
			
			LOGGER.info(e);
		}
		return null;

	}

	// to get check box reference
	public CheckBox findCheckBox(HashMap<String, Object> objProp) {

		try {
			Application app = MobileApp.getApp();
			// Describe an checkbox in the application.
			CheckBox checkBox = app.describe(CheckBox.class,
					new CheckBoxDescription.Builder().className((String) objProp.get("className"))
							.accessibilityId((String) objProp.get("accessibilityId"))
							.mobileCenterIndex((Integer) objProp.get("mobileCenterIndex"))
							.resourceId((String) objProp.get("resourceId")).container((String) objProp.get("container"))
							.build());

			return checkBox;
		} catch (GeneralLeanFtException e) {
			
			LOGGER.info(e);
		}
		return null;

	}
}
