package generic;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest 
{
	public WebDriver driver;
	public WebDriverWait wait;
//	public static final String CONFIGPATH = "./config.properties";
	public static final String XLPATH="./data/input.xlsx";
	public static final String REPORTPATH="./target/MyReport.hyml";
	public static ExtentReports eReport;
	public ExtentTest test;
	
	@BeforeSuite
	public void initReport()
	{
		eReport=new ExtentReports();
		ExtentSparkReporter reportType=new ExtentSparkReporter(REPORTPATH);
		eReport.attachReporter(reportType);
	}
	
	@AfterSuite
	public void publishReport()
	{
		eReport.flush();
	}
	
	@Parameters("property")
	@BeforeMethod
	public void openApplication(@Optional("config.properties")String propertyFile,Method testMethod) throws Exception
	{
		String testName = testMethod.getName();
		test=eReport.createTest(testName);
		
		String useGrid = Utility.getPropertyValue(propertyFile, "USEGRID");
		test.info("Grid is" +useGrid);
		String gridURL = Utility.getPropertyValue(propertyFile, "GRID_URL");
		test.info("gridURL" +gridURL);
		String browser = Utility.getPropertyValue(propertyFile, "BROWSER");
		test.info("browser" +browser);
		String url = Utility.getPropertyValue(propertyFile, "APP_URL");
		test.info("url is" +url);
		String strITO = Utility.getPropertyValue(propertyFile, "ITO");
		test.info("strITO is" +strITO);
		
		long lITO = Long.parseLong(strITO);
		String strETO = Utility.getPropertyValue(propertyFile, "ETO");
		test.info("strETO"+strETO);
		long lETO = Long.parseLong(strETO);
		
		if(useGrid.equalsIgnoreCase("yes"))
		{
			URL remoteURL=new URL(gridURL);
			DesiredCapabilities capability=new DesiredCapabilities();
			capability.setBrowserName(browser);
			driver=new RemoteWebDriver(remoteURL,capability);
			
		}
		
		else
		{
			switch(browser.toLowerCase())
			{
			case "chrome" : 
				               WebDriverManager.chromedriver().setup();
				               driver=new ChromeDriver();
				               break;
				               
			default:          
	                          test.warning("Running firefox browser locally");
	               	               
		    
			case "firefox" : 
	                         WebDriverManager.firefoxdriver().setup();
	                         driver=new FirefoxDriver();
	   		                
				               
		     
			}
			
		}
		
		
		
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(lITO));
		wait=new WebDriverWait(driver,Duration.ofSeconds(lETO));
		
	}
	
	@AfterMethod
	public void closeApplication(ITestResult result) throws Exception
	{
		String testName = result.getName();
		int status = result.getStatus();
		if(status==2)
		{
			test.info("test"+testName+"is failed");
			TakesScreenshot screenshot=(TakesScreenshot)driver;
			File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
			File dstFile=new File("./screenshots.png");
			FileUtils.copyFile(srcFile, dstFile);
			Reporter.log("Test" +testName+"Failed and screenshot is taken:"+dstFile, true);
			test.addScreenCaptureFromPath("./../screenshots"+testName+".png");
			String msg=result.getThrowable().getMessage();
			test.info("test"+testName+"is failed is due too :"+msg);
		}
		
		test.info("closing the browser");
		driver.quit();
	}

}
