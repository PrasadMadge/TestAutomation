package com.API.helper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BaseTestClass {
	
	// creating them to store previous APIs data and use it in the next API call to compare
	public  String previousAPIs_lastkey;
	public  ArrayList<String> previousAPIs_lastkey_doctorsIDs;
	private FileWriter file;
	
	public void storeJSON(String s) {

		try {
			file = new FileWriter("response/Doctors.json");
			file.write((s));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				file.flush();
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
