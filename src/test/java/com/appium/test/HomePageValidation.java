package com.appium.test;

import java.util.List;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.appium.helper.BaseTestClass;
import com.appium.helper.TestDataProvdier;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;

public class HomePageValidation extends BaseTestClass {

	@Test(groups= {"appium_tests"})
	@Parameters({ "email", "password" })
	public void homePageValidation(String email, String password) {

		appLogin(email, password); // app login
		
		// waiting for the SMS
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		driver.openNotifications();

		// String smsBody = new WebDriverWait(driver, 90L)
		// .until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new
		// UiSelector().textContains(\"ist dein Vivy Bestätigungscode\")"))).getText();

		List<MobileElement> smses = driver.findElements(
				MobileBy.AndroidUIAutomator("new UiSelector().textContains(\"ist dein Vivy Bestätigungscode\")"));
		MobileElement lastSMS = smses.get(smses.size() - 1);
		String smsBody = lastSMS.getText();
		String otp = giveOTP(smsBody);
		System.out.println(otp);

		driver.pressKey(new KeyEvent(AndroidKey.BACK));

		// System.out.println(driver.getPageSource());

		MobileElement pin = driver.findElement(MobileBy.id("eu.uvita:id/inputPIN"));
		pin.sendKeys(otp);

		MobileElement confirm = driver.findElement(MobileBy.id("eu.uvita:id/button_confirm"));
		confirm.click();

		new WebDriverWait(driver, 90L).until(ExpectedConditions.presenceOfElementLocated(
				MobileBy.id("eu.uvita:id/activity_import_encrypted_private_key_welcome_no_key"))).click();

		MobileElement deleteData = driver.findElement(MobileBy.id("eu.uvita:id/activity_no_key_available_button"));
		deleteData.click();

		MobileElement yesDeleteMyData = driver.findElement(
				MobileBy.id("eu.uvita:id/fragment_import_encrypted_private_key_delete_data_delete_button"));
		yesDeleteMyData.click();

		// wait for skip and click
		new WebDriverWait(driver, 90L).until(ExpectedConditions
				.presenceOfElementLocated(MobileBy.id("eu.uvita:id/activity_export_private_key_skip"))).click();

		// save private key =
		// eu.uvita:id/activity_export_private_key_welcome_button_password

		// waiting for the home page to launch and click
		new WebDriverWait(driver, 90L).until(ExpectedConditions
				.presenceOfElementLocated(MobileBy.xpath("//android.widget.ImageButton[@content-desc='Navigate up']")))
				.click();

		MobileElement emailHomePage = driver.findElement(MobileBy.id("eu.uvita:id/home_activity_email_text_view"));
		String emailName = emailHomePage.getText();
		System.out.println(emailName);

	}

	

}
