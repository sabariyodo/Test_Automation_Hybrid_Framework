package pages;

import java.io.IOException;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.aventstack.extentreports.ExtentTest;

import ProjectSpecificBase.WebBase;

public class OrangeHRMHomePage extends WebBase {

	public OrangeHRMHomePage(RemoteWebDriver driver, ExtentTest node) {
		this.driver = driver;
		this.node = node;
	}

	public void verifyhomepage() throws Exception {
		try {
			Thread.sleep(5000);
			// isDisplayed("xpath", locator.get("Title"));
			if (elementIsDisplayed(getLocators("Login", "title"))) {
				reportStep("The dashboard title is displayed", "pass");
			}

		} catch (Exception e) {
			reportStep("The dash board title is not displayed and the exception is " + e + "", "fail");

			throw new RuntimeException();

		}
		
	}

	public void click_logout() throws IOException {

		try {
			waitUntilElementIsDisplayed(30, locator.get("HRMLogout"));
			clickElement(locator.get("HRMLogout"));
			reportStep("The user has clicked HRMLogout", "pass");
		} catch (Exception e) {

			throw new RuntimeException();

		}
	
	}

	public void logout() throws IOException {

		try {
			waitUntilElementIsDisplayed(30, locator.get("Logout"));
			clickElement(locator.get("Logout"));
			reportStep("The user has Logout from HRMWebsite", "pass");
		} catch (Exception e) {

			throw new RuntimeException();

		}
		
	}

}
