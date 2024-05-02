package common.baselib;

import java.util.Date;
import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

public class Handlecookie {

	public Set<Cookie> getAllCookies(WebDriver webDriver) {
		return webDriver.manage().getCookies();
	}
	
	public Cookie getCookieNamed(WebDriver webDriver, String name) {
		return webDriver.manage().getCookieNamed(name);
	}

	public String getValueOfCookieNamed(WebDriver webDriver, String name) {
		return webDriver.manage().getCookieNamed(name).getValue();
	}

	public void addCookie(WebDriver webDriver, String name, String value, String domain, String path, Date expiry) {
		webDriver.manage().addCookie(new Cookie(name, value, domain, path, expiry));
	}

	public void deleteCookieNamed(WebDriver webDriver, String name) {
		webDriver.manage().deleteCookieNamed(name);
	}

	public void deleteAllCookies(WebDriver webDriver) {
		webDriver.manage().deleteAllCookies();
	}

}