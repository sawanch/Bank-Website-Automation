package test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class FirstTest {

	public static void main(String[] args) {
		
		
		WebDriver driver = new FirefoxDriver();
		
		driver.get("https://www.demo.guru99.com/V4/");
		
		//Find the username and enter the username
		
		WebElement textbox_username	= driver.findElement(By.name("uid"));
		
		textbox_username.sendKeys("mngr539530");
		
		//Password is provided
		
		driver.findElement(By.name("password")).sendKeys("UvavUpu");
		
		//login submit
		
		driver.findElement(By.xpath("//input[@type='submit']")).click();
		
		//driver.quit();

	}

}
