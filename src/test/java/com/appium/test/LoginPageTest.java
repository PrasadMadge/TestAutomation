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

public class LoginPageTest extends BaseTestClass {
	
	
	//@Test (dataProvider = "login-credentials", dataProviderClass = TestDataProvdier.class)
	
	@Test(groups= {"appium_tests"})
	@Parameters({ "email", "password" })
	public void login(String email, String password) {
		
		appLogin(email,password);	
				
	}
	
	


}
