package com.API.test;

import static org.hamcrest.Matchers.lessThan;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.API.helper.BaseTestClass;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class DoctorsAPIs extends BaseTestClass{

	private Response response;
	private ValidatableResponse json;
	private RequestSpecification request;
	

	private String baseURL = "https://vivy.com/interviews/challenges/android/";
	public String lastkey;

	@Test(groups= {"api_tests"})
	public void doctorsAPI() {

		request = RestAssured.given();
		response = request.when().get(baseURL + "doctors.json");
		json = response.then().statusCode(200);
		response.then().assertThat().time(lessThan(new Long(2000L)));

		// First get the JsonPath object instance from the Response interface
		JsonPath jsonPathEvaluator = response.jsonPath();

		// Then simply query the JsonPath object to get a String value of the node
		lastkey = jsonPathEvaluator.get("lastKey");
		previousAPIs_lastkey=lastkey;

		// Let us print the city variable to see what we got
		System.out.println("lastKey received from Response " + lastkey);

		// Validate the response
		Assert.assertEquals(lastkey, "CvQD7gEAAKjcb", "Correct lastKey received in the Response");

		// Extracting response and saving it
		ArrayList<String> doctors = jsonPathEvaluator.get("doctors");
		ArrayList<String> doctorsIDs1 = jsonPathEvaluator.get("doctors.id");
		previousAPIs_lastkey_doctorsIDs=doctorsIDs1;
		JSONArray jsdoctors = new JSONArray(doctors);
		System.out.println(jsdoctors.toString());

		// storing JSON response to file
		storeJSON(jsdoctors.toString());
}
	
	@Test(groups= {"api_tests"})
	public void doctorsAPIWithLastKey() {
		

		// --------------- Fetching next 20 doctors using lastkey ---------------------
		// --------------------------------
		request = RestAssured.given();
		response = request.when().get(baseURL + "doctors-" + previousAPIs_lastkey + ".json");

		// Generic validation
		json = response.then().statusCode(200);
		response.then().assertThat().time(lessThan(new Long(900L)));

		// getting and updating last key
		JsonPath  jsonPathEvaluator = response.jsonPath();
		previousAPIs_lastkey = jsonPathEvaluator.get("lastKey");

		// extracting doctors ids from second API response
		ArrayList<String> doctorsID_current = jsonPathEvaluator.get("doctors.id");

		ArrayList<String> copyOfDocsIdsAPI1 = previousAPIs_lastkey_doctorsIDs;
		
		System.out.println(doctorsID_current);
		System.out.println(copyOfDocsIdsAPI1);
		
		previousAPIs_lastkey_doctorsIDs.removeAll(doctorsID_current);

		// List.equals() method return true if both elements are of same size and both
		// contains same set of elements in exactly same order.
		Collections.sort(copyOfDocsIdsAPI1); // sorting doctors IDs found in first API response.
		Collections.sort(previousAPIs_lastkey_doctorsIDs); // sorting doctors IDs after comparing it with seconds API response.

		// finding difference between orginalDoctorsIds1 and doctorsIDs1 after removal
		// of similar Ids if any
		boolean isEqual = copyOfDocsIdsAPI1.equals(previousAPIs_lastkey_doctorsIDs);
		System.out.println(isEqual);

		Assert.assertEquals(true, isEqual, "Doctors IDs don't match");

	
		
	}

	

}
