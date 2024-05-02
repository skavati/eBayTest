package common.toolbox.selenium;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import ch.qos.logback.classic.Logger;
import common.baselib.BaseMethods;
import common.utilities.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePicker {
	// slect date from date picker
	// input date format "dd/mm/yyyy"( 05/09/2017)
	private static boolean status;
	public static void selectDateFromDatePicker(WebDriver driver, String date) throws InterruptedException {
		if (date.trim().equalsIgnoreCase("today")) {
			// click on Done
			WebElement btn_Today = BaseMethods.GetElement(driver, By.xpath(
					"#ui-datepicker-div > div.ui-datepicker-buttonpane.ui-widget-content > button.ui-datepicker-current.ui-state-default.ui-priority-secondary.ui-corner-all"));
			Action.moveToElement(driver,btn_Today);
			Waits.waitInSeconds(1);
			Action.click(driver, btn_Today);
		} else {

			String day1 = date.split("/")[0];
			// convert two digits to one if number has leanding zero
			// eg: 05 to 5; 15 to 15
			String day = String.valueOf(Integer.valueOf(day1));
			String month1 = date.split("/")[1];
			// to get 0 for Jan, 11 for Dec ..etc.
			String month = String.valueOf(Integer.valueOf(month1) - 1);
			String year = date.split("/")[2];

			// select month
			WebElement ddl_Month = BaseMethods.GetElement(driver, By.cssSelector(
					"#ui-datepicker-div > div.ui-datepicker-header.ui-widget-header.ui-helper-clearfix.ui-corner-all > div > select.ui-datepicker-month"));
			Action.click(driver, ddl_Month);
			Dropdown.SelectElementByValue(ddl_Month, month);

			// select year
			WebElement ddl_Year = BaseMethods.GetElement(driver, By.cssSelector(
					"#ui-datepicker-div > div.ui-datepicker-header.ui-widget-header.ui-helper-clearfix.ui-corner-all > div > select.ui-datepicker-year"));
			Action.click(driver, ddl_Year);
			Dropdown.SelectElementByValue(ddl_Year, year);

			// click on day
			WebElement btn_Day = BaseMethods.GetElement(driver,
					By.xpath("//*[@id='ui-datepicker-div']/table/tbody//a[text()='" + day + "']"));
			//btn_Day.click();
			Action.click(driver, btn_Day);

		}

	}
	
	//Created by Mahendra @13/6/2017 this steps for Travel_notification scenario:
	public static void selectNewDateFromDatePicker(WebDriver driver, String date) throws InterruptedException, ParseException {
		status=false;
		String day1 = date.split("/")[0];
		// convert two digits to one if number has leanding zero
		// eg: 05 to 5; 15 to 15
		String day = String.valueOf(Integer.valueOf(day1));
		String month1 = date.split("/")[1];
		// to get 0 for Jan, 11 for Dec ..etc.
		String month = String.valueOf(Integer.valueOf(month1) - 1);
		String year = date.split("/")[2];
//

		String Newdate="";
		Newdate =date;
		//Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(TransferAndPay_Page.get_modified_Date());
		Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(date);
		DateFormat dateFormat1 = new SimpleDateFormat("dd MMM yyyy");
		String reportDate = dateFormat1.format(date2);
		String newmonth=StringUtils.GetNonNumericString(reportDate);
		
		//Click on Calander to find Year:
		WebElement Year1 = BaseMethods.GetElement(driver, By.xpath("(.//th[contains(@class, 'datepicker-switch')])[1]"));
		//Action.click(driver, Year1);
		Year1.click();

		//Again click on it to select:		
		WebElement Year2 = BaseMethods.GetElement(driver, By.xpath("(.//th[contains(@class, 'datepicker-switch')])[2]"));
		Waits.waitForElementToBeVisible(driver, Year2, 2);
		//Action.click(driver, Year2);
		Year2.click();
		Thread.sleep(200);
		//Select Year:
		boolean staleElement=false;
		int retry = 3;
		int attempt=0;
		
		for(int i=0;i<100;i++)
		{
			Waits.waitInSeconds(2);
			Waits.waitForElementsToBeVisible(driver, driver.findElements(By.xpath("//span[@class='year']")));
			List<WebElement> lst_Dateilist=driver.findElements(By.xpath("//span[@class='year']"));
			List<WebElement> accountListPanel = lst_Dateilist;
			for (WebElement webEle : accountListPanel) 
			{
				if (webEle.getText().trim().equalsIgnoreCase(year.trim())) {
					
					driver.findElement(By.xpath("//span[(@class='year') and contains(text(),'"+year+"')]")).click();
					status=true;
					break;

				}
			}
			if(status==true)
			{
				break;
			}
			else
			{
				driver.findElement(By.xpath("(//table[@class='table-condensed']//th[@style='visibility: visible;'])[2]")).click();
				
			}
		}
		
		Thread.sleep(200);
		outerloop : for(int i=0;i<100;i++)
		{
			List<WebElement> lst_Monthlist=driver.findElements(By.xpath("//span[@class='month']"));
			List<WebElement> Monthlist = lst_Monthlist;	
		for (WebElement webEle : Monthlist) 
		{

			if (webEle.getText().trim().equalsIgnoreCase(newmonth.trim())) {
				driver.findElement(By.xpath("//span[(@class='month') and contains(text(),'"+newmonth.trim()+"')]")).click();
				break outerloop;

			}

		}
		}
		Thread.sleep(200);
		
		//Select Day:
		outerloop: for(int i=0;i<100;i++) {
		List<WebElement> lst_Dayhlist=driver.findElements(By.xpath("//td[(@class='day') and contains(text(),'')]"));
		List<WebElement> Daylist = lst_Dayhlist;	
		for (WebElement webEle : Daylist) 
		{

			if (webEle.getText().trim().equalsIgnoreCase(day.trim())) {
				driver.findElement(By.xpath("//td[(@class='day') and contains(text(),'"+day.trim()+"')]")).click();
				break outerloop;

			}
		}
		}



	}
}
