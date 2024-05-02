package common.baselib;

import org.openqa.selenium.WebDriver;

public class Initializer {
	protected WebDriver driver;

	public Initializer(WebDriver driver) {
		this.driver = driver;
	}

	public WebDriver getDriver() {
		return driver;
	}

}