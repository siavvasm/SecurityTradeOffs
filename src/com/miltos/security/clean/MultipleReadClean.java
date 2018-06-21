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
import org.owasp.esapi.Executor;
import org.owasp.esapi.codecs.WindowsCodec;

public class MultipleReadClean {
	
	final static Logger logger = Logger.getLogger(MultipleReadClean.class);
	final static String DATA_PATH = new File("C:\\Users\\siavvasm.ITI-THERMI.000\\Desktop\\data.txt").getAbsolutePath();

	public static void main(String[] args) {
		
		// Initialize the objects required for reading the data from the corresponding file
    	FileReader fr = null;
    	BufferedReader br = null;
    	
		try {
			
			fr = new FileReader(DATA_PATH);
			br = new BufferedReader(fr);
	    	
	    	// 2. Read the commands that are stored in this file
	    	Stream<String> commands = br.lines();
	    	Iterator<String> commandsIt = commands.iterator();
	    	
	    	String command = "";
	    	ProcessBuilder builder;
	    	Process process;
	    	
	    	while(commandsIt.hasNext()) {
	    		
	    		// Read the next command
	    		command = commandsIt.next();
	    		
	    		if(logger.isDebugEnabled()) {
	    			logger.debug("-------------------------------");
	    			logger.debug("Received Command: " + command);
	    		}
	    		
	    		// 1. Input Validation: Check if the command contains illegal characters
	    		if(!Pattern.matches("^[a-zA-Z0-9 ]{1,20}$", command)){
	    			logger.debug("The command contains illegal characters.");
	    			
	    			// 2. Sanitize/Neutralize the command (i.e. remove illegal characters)
	    			command = command.replaceAll("[^a-zA-Z0-9 ]", "");
	    			
	    		} else {
	    			logger.debug("The command does not contain any illegal character.");
	    		}
	    		
	    		// 3. Encode the command before executing it
	    		command = ESAPI.encoder().encodeForOS(new WindowsCodec(), command);
	    		
	    		// Create a builder for executing the desired command
	    		builder = new ProcessBuilder("cmd.exe", "/c", command);
	        	builder.redirectErrorStream(true);
	        	
	    		try {
	    			
	    			// Execute the command
					process = builder.start();
					BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
					
					// Retrieve the console output
		    		String line = "";
		    		String output = "";
		    		while (true) {
		    			line = r.readLine();
		    			output += line;
		    			if (line == null) { break; }
		    		}
		    		
		    		// Log the output
		    		if(logger.isDebugEnabled()) {
		    			logger.debug("Encoded Command: " + command);
		    			logger.debug("Console Output: " + output);
		    		}
		    		
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
	    	}
	    	
		} catch (FileNotFoundException e) {
			
			// Log the exception message
			logger.error(e.getMessage());
			
		} finally {
			
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
