package test;

import org.testng.Assert;
import org.testng.annotations.Test;

import generic.BaseTest;
import generic.Utility;
import page.EnterTimeTrackPage;
import page.LoginPage;

public class ValidLogin extends BaseTest{

	@Test
	public void testValidLogin()
	{
		String un = Utility.getXlData(XLPATH, "Sheet1", 1, 0);
		String pwd = Utility.getXlData(XLPATH, "Sheet1", 1, 1);
		
		LoginPage loginpage=new LoginPage(driver);
		loginpage.setUserName(un);
		test.info("Enter valid username " + un);
		
		loginpage.setPassword(pwd);
		test.info("Enter valid password " + pwd);
		
		loginpage.clickLoginButton();
		
		EnterTimeTrackPage ettPage=new EnterTimeTrackPage(driver);
		boolean result = ettPage.VerifyLogoutLink(wait);
		Assert.assertEquals(result, true, "Home Page is not displayed");
		test.pass("Home Page is displayed");
		
				
	}
}
