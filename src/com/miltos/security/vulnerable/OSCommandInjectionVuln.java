package com.miltos.security.vulnerable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.stream.Stream;

public class OSCommandInjectionVuln {
	
	final static String DATA_PATH = new File("C:\\Users\\siavvasm.ITI-THERMI.000\\Desktop\\input_data.txt").getAbsolutePath();
	final static String APP_PATH = new File("C:\\Users\\siavvasm.ITI-THERMI.000\\Desktop\\test.jar").getAbsolutePath();
	
	public static void main(String[] args) throws Exception {

    	// 1. Open the desired file
    	FileReader fr = new FileReader(DATA_PATH);
    	BufferedReader br = new BufferedReader(fr);
    	
    	// 2. Read the user-defined parameters from the corresponding file
    	Stream<String> parameters = br.lines();
    	Iterator<String> parameterIt = parameters.iterator();
    	
    	String parameter = "";
    	ProcessBuilder builder;
    	Process process;
    	
    	// 3. Execute the commands to the console
    	while(parameterIt.hasNext()) {
    		
    		// Read the parameter 
    		parameter = parameterIt.next();
    		
    		// Construct the command
    		if(!parameter.equals(null)){
    			String command = "java -jar " + APP_PATH + " " + parameter;
    			System.out.println("Command: " + command);
    		
    		// Create the ProcessBuilder object
    		builder = new ProcessBuilder("cmd.exe", "/c", command);
        	builder.redirectErrorStream(true);
        	
			// Execute the command 
			process = builder.start();
			
			// Log the console output
			BufferedReader consoleReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			// Retrieve the console output
    		String line = "";
    		String output = "";
    		while (true) {
  
    			line = consoleReader.readLine();
    			
    			output += line;
    			System.out.println(output);
    			if (line == null) { break; }
    		}
    		
    		System.out.println("Console Output: " + line);
    	}
    		
    	}
    	// 4. Close the connection to the file
    	br.close();
    	fr.close();
    	
    	br = null;
    	fr = null;
    	parameter = null;
    	builder = null;
	}
}
