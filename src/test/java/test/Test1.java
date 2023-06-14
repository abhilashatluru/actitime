package test;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import generic.BaseTest;
import generic.Utility;
import page.HomePage;

public class Test1 extends BaseTest
{
	@Test
	public void test1()
	{
		String v = Utility.getXlData(XLPATH, "sheet1", 0, 0);
		test.info(v);
		
		String title = driver.getTitle();
		test.info(title);
		
        HomePage hp=new HomePage(driver);
        hp.setUserName();
        hp.setPassword();
        hp.clickLoginButton();
        String actualURL=driver.getCurrentUrl();
        String expectedURL = "https://demo.actitime.com/user/submit_tt.do";
        wait.until(ExpectedConditions.urlMatches(expectedURL));
        test.info(actualURL);
        
        Assert.assertEquals(actualURL,expectedURL );
	}

}
