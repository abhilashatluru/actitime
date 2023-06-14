package page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage 
{
	@FindBy(xpath="//input[@name='username']")
	private WebElement userName;
	
	@FindBy(xpath="//input[@name='pwd']")
	private WebElement password;
	
	@FindBy(xpath="//a[@id='loginButton']")
	private WebElement loginBTN;
	
	public HomePage(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}
	
	public void setUserName()
	{
		userName.sendKeys("admin");
	}
	
	public void setPassword()
	{
		password.sendKeys("manager");
	}
	
	public void clickLoginButton()
	{
		loginBTN.click();
	}

}
