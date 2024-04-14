package Testscripts;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import ProjectSpecificBase.WebBase;
import pages.OrangeHRMHomePage;
import pages.OrangeHRMLoginPage;

public class TS_OHRM_001 extends WebBase {

	@BeforeTest
	public void setValues() {

		testName = "TS_OHRM_002";
		testDescription = "Login";
		category = "Smoke_test";
		testAuthor = "Sabarivasan";
	}
	
	@Test
	public void tS_OHRM_001() throws Exception {
		OrangeHRMLoginPage a = new OrangeHRMLoginPage(driver,node);
		a.go_to_URL();
		a.enter_username(testdataload("Sheet1","TS_SDIR_001", "username"));
		a.enter_password(testdataload("Sheet1","TS_SDIR_001", "password"));
		a.click_login();
		OrangeHRMHomePage h = new OrangeHRMHomePage(driver, node);
		h.verifyhomepage();
	}
}
