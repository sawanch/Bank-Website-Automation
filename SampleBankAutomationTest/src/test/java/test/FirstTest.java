package test;

import java.time.Duration;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class FirstTest {

    public static WebDriver startFirefox() {
        return new FirefoxDriver();
    }

    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = startFirefox();

        Util util_obj = new Util();
        driver.get(util_obj.baseURL);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Test Case 1: Correct username and password
        executeLogin(driver, util_obj.username, util_obj.passwordString, "Guru99 Bank Manager HomePage");
        
        

        // Navigate back to the login page
        driver.navigate().back();
        
        
        // Test Case 2: Correct username but wrong password
        executeLogin(driver, util_obj.username, "wrong_password", "User or Password is not valid"); // Change the expected alert message

       
        driver.navigate().back();
        
       
        // Test Case 3: Wrong username and correct password
        executeLogin(driver, "wrong_username", util_obj.passwordString, "User or Password is not valid"); // Change the expected alert message

       
        
        // Close the browser after all test cases
        driver.quit();
    }

    public static void executeLogin(WebDriver driver, String username, String password, String expectedOutcome) throws InterruptedException {
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
}
