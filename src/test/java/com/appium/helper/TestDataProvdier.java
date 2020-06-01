package com.appium.helper;

import org.testng.annotations.DataProvider;

public class TestDataProvdier {
	
	@DataProvider (name = "login-credentials")
	 public Object[][] dpMethod(){
	 return new Object[][] {{"prasadmadgegmail.com", "vivy@test007" }};
	 }

}
