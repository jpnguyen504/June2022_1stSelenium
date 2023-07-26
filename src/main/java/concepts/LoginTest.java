package concepts;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest {

	WebDriver driver;
	String browser;
	String url;
	String username;
	String password;

	By userNameField = By.xpath("//input[@id='username']");
	By passWordField = By.xpath("//*[@id='password']");
	By signInButtonField = By.xpath("//*[@type='submit']");
	By dashboardHeaderField = By.xpath("//h2[contains(text(), 'Dashboard')]");
	By customerMenuField = By.xpath("//*[@id='side-menu']/li[3]/a/span[1]");
	By addCustomerMenuField = By.xpath("//*[@id='side-menu']/li[3]/ul/li[1]/a");
	By fullNameField = By.xpath("//*[@id='account']");
	By fullNameHeaderField = By.xpath("//label[contains(text(), 'Full Name')]");
	By emailField = By.xpath("//input[@id='email']");
	By companyDropDownField = By.xpath("//select[@id='cid']");
	By addCustomerHeaderField = By.xpath("//h5[contains(text(), 'Add Contact')]");
	By countryDropDownField = By.xpath("//select[@name = 'country']");

	@BeforeClass
	public void readConfig() {
		// Classes used to read a file --> //InputStream //BufferedReader //FileReader
		// //Scanner

		try {

			InputStream input = new FileInputStream("src\\main\\java\\config\\config.properties");
			Properties prop = new Properties();
			prop.load(input);
			browser = prop.getProperty("browser");
			url = prop.getProperty("url");
			username = prop.getProperty("username");
			password = prop.getProperty("password");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@BeforeMethod
	public void init() {
		if (browser.equalsIgnoreCase("chrome")) {

			System.setProperty("webdriver.chrome.driver", "Driver\\chromedriver.exe");
			driver = new ChromeDriver();

		} else if (browser.equalsIgnoreCase("firefox")) {

			System.setProperty("webdriver.gecko.driver", "Driver\\geckodriver.exe");
			driver = new FirefoxDriver();

		}

		driver.manage().deleteAllCookies();
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

//	@Test(priority = 1)
	public void loginTest() {
		driver.findElement(userNameField).sendKeys(username);
		driver.findElement(passWordField).sendKeys(password);
		driver.findElement(signInButtonField).click();
		
		WebDriverWait wait = new WebDriverWait(driver,30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardHeaderField));
		Assert.assertEquals(driver.findElement(dashboardHeaderField).getText(), "Dashboard", "Dashboard page not found!!");
	}
	
	@Test(priority = 2)
	public void addCustomer() {
		loginTest();
		driver.findElement(customerMenuField).click();
		driver.findElement(addCustomerMenuField).click();
		
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		explicitWait(addCustomerHeaderField);
		Assert.assertEquals(driver.findElement(addCustomerHeaderField).getText(), "Add Contact", "Add Customer page not found!!");
		
		driver.findElement(fullNameField).sendKeys("Selenium" + generateRandomNum(999));
		
		selectFromDropdown(companyDropDownField, "Techfios");
		selectFromDropdown(countryDropDownField, "Afghanistan");
	}

	private void selectFromDropdown(By byElement, String visibleText) {
		Select sel = new Select(driver.findElement(byElement));
		sel.selectByVisibleText(visibleText);
		
	}
	
	private int generateRandomNum(int boundaryNum) {
		Random rnd = new Random();
		int genNum = rnd.nextInt(boundaryNum);
		return genNum;
	}
	private void explicitWait(By byElement) {
		WebDriverWait wait = new WebDriverWait(driver, 1000);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(byElement)));
		
	}
	
}
