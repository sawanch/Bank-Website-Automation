package test;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FirstTest {

	public static WebDriver startFirefox() {
		return new FirefoxDriver();
	}

	public static void main(String[] args) throws InterruptedException, IOException {

		System.setProperty("webdriver.chrome.driver",
				"/Users/sawan/Documents/6-STS-workspace/SampleBankAutomationTest/drivers/chromedriver");

		ChromeOptions options = new ChromeOptions();
		options.setBinary("/Applications/Google Chrome for Testing.app/Contents/MacOS/Google Chrome for Testing");

		WebDriver driver = new ChromeDriver(options);

		Util util_obj = new Util();
		driver.get(util_obj.baseURL);

		try {
			// Wait for the iframe to be present in the DOM
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement iframeElement = wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//a[@id='saveAndExit' and contains(@class, 'save-exit')]")));

			// Wait for the iframe to become visible (including handling
			// JavaScript-controlled visibility)
			wait.until(ExpectedConditions.visibilityOf(iframeElement));

			// Switch to the iframe
			driver.switchTo().frame(iframeElement);

			// Switch back to the default content
			driver.switchTo().defaultContent();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		// Test Case 1: Correct username and password
		executeLogin(driver, Util.username, Util.passwordString, "Guru99 Bank Manager HomePage");

		// Navigate back to the login page
		driver.navigate().back();

		// Test Case 2: Correct username but wrong password
		executeLogin(driver, Util.username, Util.wrongPasswordString, "User or Password is not valid"); 

		driver.navigate().back();

		// Test Case 3: Wrong username and correct password
		executeLogin(driver, Util.wrongUsername, Util.passwordString, "User or Password is not valid");

		// Test Case 4: Wrong username and wrong password
		executeLogin(driver, Util.wrongUsername, Util.wrongPasswordString, "User or Password is not valid");

		// Close the browser after all test cases
		driver.quit();
	}

	public static void executeLogin(WebDriver driver, String username, String password, String expectedOutcome)
			throws InterruptedException, IOException {
		WebElement textbox_username = driver.findElement(By.name("uid"));
		WebElement textbox_password = driver.findElement(By.name("password"));

		textbox_username.clear(); // Clear any existing text
		textbox_username.sendKeys(username);

		textbox_password.clear();
		textbox_password.sendKeys(password);

		driver.findElement(By.xpath("//input[@type='submit']")).click();

		if ("Guru99 Bank Manager HomePage".equals(expectedOutcome)) {
			// Check for successful login by comparing titles
			String actualTitle = driver.getTitle();

			if (expectedOutcome.equals(actualTitle)) {

				System.out.println("Login Successful for Username: " + username);
				checkManagerIDSS(driver);

			} else {

				System.out.println("Login Failed for Username: " + username);

			}

		} else {

			// Check for alert message in case of unsuccessful login
			Alert alert = driver.switchTo().alert();
			String actualAlertMessage = alert.getText();
			if (expectedOutcome.equals(actualAlertMessage)) {

				System.out.println("Login Failed for Username: " + username + ". Alert message: " + actualAlertMessage);
				alert.accept();
				driver.switchTo().defaultContent(); // Switch back to the main window

			} else {

				System.out.println("Unexpected outcome for Username: " + username);

			}
		}
	}

	private static void checkManagerIDSS(WebDriver driver) throws IOException {

		// Get text displayed on the login page

		String xpath = "//td[contains(text(), 'Manger Id : mngr539530')]";
		WebElement tdElement = driver.findElement(By.xpath(xpath));
		String pageText = tdElement.getText();
		System.out.println(" @@@@@@ Text in <td> element: " + pageText);

		// Extract the dynamic text mngrXXXX on the page
		String[] parts = pageText.split(Util.pattern);

		System.out.println("@@@@@@@@@  parts : " + parts.length);

		// Ensure that the array has at least two parts
		if (parts.length >= 2) {
			String dynamicText = parts[1];

			// Check that the dynamic text is of the pattern mngrXXXX
			// First 4 characters must be "mngr"
			assertTrue(dynamicText.substring(1, 5).equals(Util.firstPattern));

			System.out.println("@@@@@@@@@ first pattern : " + dynamicText.substring(1, 5));

			// remain stores the "XXXX" in the pattern mngrXXXX

			String remain = dynamicText.substring(5);

			System.out.println("@@@@@@@@@ Remaining Pattern : " + remain);

			// Check remain string must be numbers;
			assertTrue(remain.matches(Util.secondPattern));

			System.out.println("@@@@@@@@@ Dynamic text found : " + dynamicText);

			// Code to take a screenshot
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			// Screenshot saved on Desktop of mac
			String desktopPath = System.getProperty("user.home") + "/Desktop/screenshot.png";
			File destFile = new File(desktopPath);

			FileUtils.copyFile(scrFile, destFile);

		} else {
			System.out.println("Dynamic text not found or does not match the expected pattern.");
		}
	}

}
