import java.time.Duration;

import javax.annotation.processing.RoundEnvironment;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Smartbuy_test {

	public WebDriver driver;
	public int numberOfTry = 8;
	SoftAssert softassertProcess = new SoftAssert();

	@BeforeTest()

	public void this_is_before_test() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://smartbuy-me.com/smartbuystore/");
		driver.findElement(By.xpath("/html/body/main/header/div[2]/div/div[2]/a")).click();
		driver.manage().window().maximize();

	}

	@Test()

	public void Test_Adding__items_SAMSUNG_50_inch() {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		for (int i = 0; i < numberOfTry; i++) {
		
			driver.findElement(By.xpath(
					"//*[@id=\"newtab-Featured\"]/div/div[1]/div/div/div/div[2]/div/div[3]/div[1]/div/div/form[1]/div[1]/button"))
					.click();
			String msg = driver.findElement(By.xpath("//*[@id=\"addToCartLayer\"]/div[1]")).getText();
			if (msg.contains("Sorry")) {
				numberOfTry = i;
				driver.findElement(By.xpath("//*[@id=\"addToCartLayer\"]/a[1]")).click();		
			}
			else {
				driver.findElement(By.xpath("//*[@id=\"addToCartLayer\"]/a[2]")).click();
			}
			

		}

	}

	@Test()
	public void we_need_to_check_the_correct_price() {
		
		driver.navigate().back();
		String the_single_item_price = driver
				.findElement(By.xpath(
						"//*[@id=\"newtab-Featured\"]/div/div[1]/div/div/div/div[2]/div/div[2]/div[2]/div/p"))
				.getText();
		String[] the_updated_single_item_price = the_single_item_price.split("JOD");
		String The_Final_Single_item_price_in_string = the_updated_single_item_price[0].trim();
		String updated = The_Final_Single_item_price_in_string.replace(",", ".");
		Double final_price = Double.parseDouble(updated);
		System.out.println(final_price);
		System.out.println(final_price * (numberOfTry));
		
		String ActualWebsite = driver.getTitle();
		softassertProcess.assertEquals(ActualWebsite, PassParameters.Title);
		softassertProcess.assertEquals(final_price *numberOfTry, 1845.0);
		softassertProcess.assertAll();
	}
	
	@Test()
	public void Discount_Assert() {

		String original_number = driver.findElement(By.xpath("//*[@id=\"newtab-Featured\"]/div/div[1]/div/div/div/div[3]/div/div[2]/div[2]/div/div/span[2]")).getText();
		String discount_value = driver.findElement(By.xpath("//*[@id=\"newtab-Featured\"]/div/div[1]/div/div/div/div[3]/div/div[2]/div[2]/div/div/span[1]")).getText();
		String [] original_number_string = original_number.split("JOD");
		String orignal_no_update = original_number_string[0].trim();
		String updated_replace= orignal_no_update.replace(",", ".");
		Double final_update_original_number = Double.parseDouble(updated_replace);
		System.out.println(final_update_original_number);
		
		String [] discount_update = discount_value.split("%");
		String dis_update= discount_update[0].trim();
		Double discount_updated =Double.valueOf(dis_update);
		System.out.println(discount_updated);
		
		Double value_Discount = final_update_original_number *(discount_updated/100);
		System.err.println(value_Discount);
		Double value_of_item_After_Discount = final_update_original_number - value_Discount;
		System.err.println(value_of_item_After_Discount + "value discount");
	
		
	}
}
