package com.appium.helper;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class BaseTestClass {
	
	String nodePath="/usr/local/bin/node";
	String appiumMainJsPath = "/Applications/Appium.app/Contents/Resources/app/node_modules/appium/build/lib/main.js";

	//    /Applications/Appium.app/Contents/Resources/app/node_modules/appium/build/lib/main.js
	// 	  /usr/local/lib/node_modules/appium/build/lib/main.js

	int portNo=4723;


	AppiumDriverLocalService service ;
	
	
	protected AndroidDriver<MobileElement> driver = null;

	@BeforeMethod(groups= {"appium_tests"})
	public void setup() {

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
			driver = new AndroidDriver<MobileElement>(new URL("http://0.0.0.0:"+portNo+"/wd/hub"), caps);
		} catch (MalformedURLException e) {
			System.err.println("ERROR IN DRIVER CREATION");
			e.printStackTrace();
		}
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

	}
	
	@AfterMethod(groups= {"appium_tests"})
	public void cleanUp() {
		if (driver != null)
			driver.quit();
		System.err.println("App closed and test finished");

	}
	
	
	
	// test case methods
	public static String giveOTP(final String smsBody) {
		final Pattern p = Pattern.compile("(\\d{6})");
		final Matcher m = p.matcher(smsBody);
		if (m.find()) {
			return m.group(0);
		}
		return "";
	}

	public void appLogin(String email, String password) {

		new WebDriverWait(driver, 90L).until(
				ExpectedConditions.presenceOfElementLocated(MobileBy.id("eu.uvita:id/view_holder_tutorial_icon")));

		MobileElement haveVivyAccount = driver
				.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"I have a Vivy Account\")"));
		haveVivyAccount.click();

		// waiting for the sign up page to launch
		new WebDriverWait(driver, 90L)
				.until(ExpectedConditions.presenceOfElementLocated(MobileBy.id("eu.uvita:id/email")));

		MobileElement emailm = driver.findElement(MobileBy.id("eu.uvita:id/email"));
		emailm.clear();
		emailm.sendKeys(email);

		MobileElement passwordm = driver.findElement(MobileBy.id("eu.uvita:id/password"));
		passwordm.clear();
		passwordm.sendKeys(password);

		MobileElement signIn = driver.findElement(MobileBy.id("eu.uvita:id/login_fragment_sign_in_button"));
		signIn.click();

		MobileElement noConsent = driver.findElement(MobileBy.id("eu.uvita:id/reminders_permissions_cancel"));
		noConsent.click();
		signIn.click();

		// waiting for the PIN page to launch
		new WebDriverWait(driver, 15L)
				.until(ExpectedConditions.presenceOfElementLocated(MobileBy.id("eu.uvita:id/inputPIN")));

	}
	
	

	//@BeforeSuite
	public void startAppiumServer() {
		
		service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
				.usingDriverExecutable(new File(nodePath))
				.withAppiumJS(new File(appiumMainJsPath))
				.withIPAddress("0.0.0.0").usingPort(portNo));
		
		System.out.println("Appium server Starting");
		service.start();
		System.out.println("Appium server started");
		
		
	}
	//@AfterSuite
	public void stopAppiumServer() {
		service.stop();
		System.out.println("Appium server stopped");
		
	}
	

}
