package com.miltos.security.clean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.WindowsCodec;

public class OSCommandInjectionClean {

	final static Logger logger = Logger.getLogger(MultipleReadClean.class);
	final static String DATA_PATH = new File("C:\\Users\\siavvasm.ITI-THERMI.000\\Desktop\\data2.txt").getAbsolutePath();
	final static String APP_PATH = new File("C:\\Users\\siavvasm.ITI-THERMI.000\\Desktop\\test.jar").getAbsolutePath();

	public static void main(String[] args) {
		
		// Initialize the objects required for reading the data from the corresponding file
    	FileReader fr = null;
    	BufferedReader br = null;
    	
		try {
			
			/*
			 * 0. Read the input data (i.e. parameters)
			 */
			
			// Setup a connection to the file containing the input data 
			fr = new FileReader(DATA_PATH);
			br = new BufferedReader(fr);
	    	
	    	// Read the user-defined parameters from the corresponding file
	    	Stream<String> parameters = br.lines();
	    	Iterator<String> parameterIt = parameters.iterator();
	    	
	    	/*
	    	 * 1. Execute the application for each parameter
	    	 */
	    	
	    	// Initialize the required objects
	    	String parameter = "";
	    	ProcessBuilder builder;
	    	Process process;
	    	
	    	// For each one of the retrieved parameters...
	    	while(parameterIt.hasNext()) {
	    		
	    		// Read the value of the parameter
	    		parameter = parameterIt.next();
	    		
	    		// Log the parameter
	    		if(logger.isDebugEnabled()) {
	    			logger.debug("-------------------------------");
	    			logger.debug("Input Parameter: " + parameter);
	    		}
	    		
	    		/*
	    		 * Security Mechanisms:
	    		 * 	A. Input Validation
	    		 *  B. Input Neutralization (Sanitization)
	    		 *  C. Encoding
	    		 *  D. Parameterization
	    		 */
	    		
	    		// A. Input Validation: Check if the parameter contains illegal characters
	    		if(!Pattern.matches("^[a-zA-Z0-9 ]{1,20}$", parameter)){		
	    			// Log the event
	    			logger.debug("The parameter contains illegal characters.");
	    			
	    			// B. Sanitize/Neutralize the parameter (i.e. remove illegal characters)
	    			parameter = parameter.replaceAll("[^a-zA-Z0-9 ]", "");
	    			
	    		} else {
	    			// Log the event
	    			logger.debug("The command does not contain any illegal character.");
	    		}
	    		
	    		// C. Encoding: Create and encode the basic command
	    		String command = "java -jar %s";
	    		command = String.format(command, APP_PATH);
	    		command =  ESAPI.encoder().encodeForOS(new WindowsCodec(), command);
	    		
	    		// C. Encoding: Encode the parameter that will be provided as input to the command
	    		parameter = ESAPI.encoder().encodeForOS(new WindowsCodec(), parameter);
	    		
	    		/*
	    		 * Execute the command
	    		 */
	    		
	    		// D. Parameterization: Provide command and data separately to the ProcessBuilder
	    		builder = new ProcessBuilder("cmd.exe", "/c", command, " ", parameter);
	        	builder.redirectErrorStream(true);
	        	
	    		try {
	    			
	    			// Execute the command as an individual process
					process = builder.start();
					
					/*
					 * Log the console output...
					 */
					BufferedReader consoleReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					
					// Retrieve the console output
		    		String line = "";
		    		String output = "";
		    		while (true) {
		    			line = consoleReader.readLine();
		    			output += line;
		    			if (line == null) { break; }
		    		}
		    		
		    		// Log the console output
		    		if(logger.isDebugEnabled()) {
		    			logger.debug("Command: " + command + " " + parameter);
		    			logger.debug("Console Output: " + output);
		    		}
		    		
				} catch (IOException e) {
					logger.error("The process could not be executed successfully!");
					logger.error(e.getMessage());
				}
	    	}
	    	
		} catch (FileNotFoundException e) {
			
			logger.error("The file could not be found!");
			logger.error(e.getMessage());
			
		} finally {
			
			/*
			 * Properly release the resources
			 */
	    	try {
	    		
	    		if (br != null) {
	    			br.close();
	    		}
	    		
			} catch (IOException e) {
				
				logger.error("Error closing the BufferedReader");
				logger.error(e.getMessage());
				
			} 	
	    	
	    	try {
	    		
	    		if (fr != null) {
	    			fr.close();
	    		}
	    		
			} catch (IOException e) {
				
				logger.error("Error closing the FileReader");
				logger.error(e.getMessage());
				
			} 

		}
	}
}


