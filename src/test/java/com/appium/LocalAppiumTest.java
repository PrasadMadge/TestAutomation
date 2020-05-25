package com.appium;

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;

public class LocalAppiumTest {

	public static void main(String[] args) {

		AndroidDriver<MobileElement> driver = null;

		DesiredCapabilities caps = new DesiredCapabilities();
		// minimum mandatory capabilites required for android
		caps.setCapability(MobileCapabilityType.DEVICE_NAME, "ONEPLUS7T");
		caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "android");
		caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, "10");
		caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.ANDROID_UIAUTOMATOR2);
		caps.setCapability(MobileCapabilityType.NO_RESET, false);
		caps.setCapability(MobileCapabilityType.UDID, "3af79ef5");

		// capabilities for apk
		caps.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "eu.uvita");
		caps.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "eu.uvita.ui.splash.SplashActivity");

		try {
			// App will launch if everything goes fine
			driver = new AndroidDriver<MobileElement>(new URL("http://0.0.0.0:4723/wd/hub"), caps);
			driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);

			// waiting for the app to launch
			new WebDriverWait(driver, 90L).until(
					ExpectedConditions.presenceOfElementLocated(MobileBy.id("eu.uvita:id/view_holder_tutorial_icon")));

			MobileElement haveVivyAccount = driver
					.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"I have a Vivy Account\")"));
			haveVivyAccount.click();

			// waiting for the sign up page to launch
			new WebDriverWait(driver, 90L)
					.until(ExpectedConditions.presenceOfElementLocated(MobileBy.id("eu.uvita:id/email")));

			MobileElement email = driver.findElement(MobileBy.id("eu.uvita:id/email"));
			email.clear();
			email.sendKeys("prasadmadge@gmail.com");

			MobileElement password = driver.findElement(MobileBy.id("eu.uvita:id/password"));
			password.clear();
			password.sendKeys("vivy@test007");

			MobileElement signIn = driver.findElement(MobileBy.id("eu.uvita:id/login_fragment_sign_in_button"));
			signIn.click();

			MobileElement noConsent = driver.findElement(MobileBy.id("eu.uvita:id/reminders_permissions_cancel"));
			noConsent.click();

			signIn.click();

			Thread.sleep(10000);

			driver.openNotifications();
			
			//String smsBody = new WebDriverWait(driver, 90L)
			//.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textContains(\"ist dein Vivy Bestätigungscode\")"))).getText();
			

			List<MobileElement> smses = driver.findElements(
					MobileBy.AndroidUIAutomator("new UiSelector().textContains(\"ist dein Vivy Bestätigungscode\")"));
			MobileElement lastSMS= smses.get(smses.size() - 1);
			String smsBody = lastSMS.getText();
			String otp = giveOTP(smsBody);
			System.out.println(otp);

			driver.pressKey(new KeyEvent(AndroidKey.BACK));

			//System.out.println(driver.getPageSource());

			MobileElement pin = driver.findElement(MobileBy.id("eu.uvita:id/inputPIN"));
			pin.sendKeys(otp);

			MobileElement confirm = driver.findElement(MobileBy.id("eu.uvita:id/button_confirm"));
			confirm.click();

			MobileElement skip = driver.findElement(MobileBy.id("eu.uvita:id/activity_export_private_key_skip"));
			skip.click();

			// save private key =
			// eu.uvita:id/activity_export_private_key_welcome_button_password

			// waiting for the home page to launch and click
			new WebDriverWait(driver, 90L)
					.until(ExpectedConditions.presenceOfElementLocated(MobileBy.name("Navigate up"))).click();
			

			MobileElement emailHomePage = driver.findElement(MobileBy.id("eu.uvita:id/home_activity_email_text_view"));
			String emailName = emailHomePage.getText();
			System.out.println(emailName);
			

		} catch (Exception e) {

			System.err.println("Error :" + e.getMessage());

		} finally {
			if (driver != null)
				driver.quit();
			System.err.println("App closed");
		}

	}

	public static String giveOTP(final String smsBody) {
		final Pattern p = Pattern.compile("(\\d{6})");
		final Matcher m = p.matcher(smsBody);
		if (m.find()) {
			return m.group(0);
		}
		return "";
	}

}
