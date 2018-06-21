package com.miltos.security.vulnerable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.owasp.esapi.ESAPI;

public class OSCommandInjectionFile {

	public static void main(String[] args) throws IOException {
  	
    	// 1. Open the desired file
    	FileReader fr = new FileReader("C:\\Users\\siavvasm.ITI-THERMI.000\\Desktop\\data.txt");
    	BufferedReader br = new BufferedReader(fr);
    	
    	// 2. Read the command that should be executed
    	String data = br.readLine();
    	
    	// Print the original and encoded version
    	System.out.println("Original Value: " + data);
    	System.out.println("Encoded Value:" + ESAPI.encoder().encodeForHTML(data));
    	
    	// 3. Neutralize the command by encoding it
    	String sanitizedData = ESAPI.encoder().encodeForHTML(data);
    	
    	// 3. Execute the command
    	ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", sanitizedData);
    	builder.redirectErrorStream(true);
    	
		//Execute the command

		Process process = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));

		//Print the console output for debugging purposes
		String line;
		while (true) {
			line = r.readLine();
			if (line == null) { break; }
		}
    	
    	// 4. Close the connection to the file
    	br.close();
    	fr.close();

	}

}
