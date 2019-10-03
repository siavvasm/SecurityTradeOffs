package com.miltos.security.clean;

/* TEMPLATE GENERATED TESTCASE FILE
Filename: CWE78_OS_Command_Injection__File_01.java
Label Definition File: CWE78_OS_Command_Injection.label.xml
Template File: sources-sink-01.tmpl.java
*/
/*
* Modified by: Miltiadis Siavvas
*/

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
import org.owasp.esapi.codecs.UnixCodec;
import org.owasp.esapi.codecs.WindowsCodec;

public class CWE78_OS_Command_Injection_File_01_Clean {
	
	final static String DATA = new File("C:\\Users\\siavvasm.ITI-THERMI.000\\Desktop\\input_data.txt").getAbsolutePath();
	final static Logger logger = Logger.getLogger(CWE78_OS_Command_Injection_File_01_Clean.class);
	
	public static void bad(String param) throws InterruptedException, IOException {

		// 1. Define the data of the command
		String data;
		data = param;
		
		// Log the parameter
		if(logger.isDebugEnabled()) {
			logger.debug("-----------------------------------");
			logger.debug("Input Parameter: " + data);
		}
		
		/*
		 * Security Mechanisms:
		 * 	A. Input Validation
		 *  B. Input Neutralization (Sanitization)
		 *  C. Encoding
		 *  D. Parameterization
		 */
		
		// A. Input Validation: Check if the parameter contains illegal characters
		if(!Pattern.matches("^[a-zA-Z0-9 ]{1,20}$", data)) {
			
			// Log the event
			if(logger.isDebugEnabled()) {
				logger.debug("The input parameter contains illegal characters.");
			}
			
			// B. Sanitize/Neutralize the parameter (i.e., remove illegal characters) 
			data = data.replaceAll("[^a-zA-Z0-9 ]", "");

		} else {
			// Log the event 
			if(logger.isDebugEnabled()) {
				logger.debug("The input parameter does not contain illegal characters.");
			}
		}
		
		// Construct the command
        String osCommand;
        String shell1 = "";
        String shell2 = "";
        
        if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
            /* running on Windows */
        	// Define the shell
        	shell1 = "cmd.exe";
        	shell2 = "/c";
        	
        	// Create the Windows Command
            osCommand = "c:\\WINDOWS\\SYSTEM32\\cmd.exe /c dir ";
            
            // C. Encoding: Create and encode the basic command and the parameter (for Windows)
            data = ESAPI.encoder().encodeForOS(new WindowsCodec(), data);
            osCommand = ESAPI.encoder().encodeForOS(new WindowsCodec(), osCommand);
            
        }
        else {
            /* running on non-Windows */
        	// Define the shell
        	shell1 = "/bin/bash";
        	shell2 = "-c";
        	
        	// Create the command
            osCommand = "/bin/ls ";
            
            // C. Encoding: Create and encode the basic command and the parameter (for Unix)
            data = ESAPI.encoder().encodeForOS(new UnixCodec(), data);
            osCommand = ESAPI.encoder().encodeForOS(new UnixCodec(), osCommand);      
        }
        
      
        // D. Parameterization: Use ProcessBuilder instead of Runtime.exec()
        ProcessBuilder builder = new ProcessBuilder(shell1, shell2, osCommand, " ", data);
        builder.redirectErrorStream(true);
        
        try {
        	// Execute the command as an individual process
			Process process = builder.start();
			
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
    			logger.debug("Command: " + osCommand + " " + data);
    			logger.debug("Console Output: " + output);
    		}
        } catch(IOException e) {
			logger.error("The file could not be found!");
			logger.error(e.getMessage());
        }
        
        // Alternative Execution (Using Runtime.exec())
        /* POTENTIAL FLAW: command injection */
//        Process process = Runtime.getRuntime().exec(osCommand + data);
//        process.waitFor();
		
	}
	
    public static void good(final String param) throws InterruptedException, IOException {
    	
    	// 1. Read the parameter
        String data = param;

        /* FIX: Use a hardcoded string */
        /* Replace the parameter with a benign String */
        data = "foo";

        final String osCommand;
        if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
            /* running on Windows */
            osCommand = "c:\\WINDOWS\\SYSTEM32\\cmd.exe /c dir ";
        } else {
            /* running on non-Windows */
            osCommand = "/bin/ls ";
        }

        /* POTENTIAL FLAW: command injection */
        final Process process = Runtime.getRuntime().exec(osCommand + data);
        process.waitFor();

    }
    
	public static void main(String[] args) {
			
		// Initialize the file pointers
		FileReader fr = null;
		BufferedReader br = null;
		
		try {
			// 1. Open the desired file
			fr = new FileReader(DATA);
			br = new BufferedReader(fr);
			
			// 2. Read the user defined parameters from the desired file
			Stream<String> parameters = br.lines();
			Iterator<String> parameterIt = parameters.iterator();
			
			String parameter = "";
			
			// 3. Execute the command for each parameter
			while(parameterIt.hasNext()) {
				
				// Read the parameter
				parameter = parameterIt.next();
				
				// Execute the command with this parameter
				bad(parameter);
				
				
			}
		} catch(IOException e) {
			logger.error("The file could not be found!");
			logger.error(e.getMessage());
		} catch (InterruptedException e) {
			logger.error("Interrupted Exception Thrown!");
			logger.error(e.getMessage());
		} finally {
			/*
			 * Properly release the resources...
			 */
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				logger.error("Error closing the BufferedReader!");
				logger.error(e.getMessage());
			}
			
			try {
				if (fr != null) {
					fr.close();
				}
			} catch (IOException e) {
				logger.error("Error closing the FileReader!");
				logger.error(e.getMessage());
			}
		}
		
			
	}

}
