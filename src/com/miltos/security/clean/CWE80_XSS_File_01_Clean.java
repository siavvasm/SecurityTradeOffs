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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.UnixCodec;
import org.owasp.esapi.codecs.WindowsCodec;

public class CWE80_XSS_File_01_Clean {
	
	final static String DATA = new File("C:\\Users\\siavvasm.ITI-THERMI.000\\Desktop\\input_data.txt").getAbsolutePath();
	final static Logger logger = Logger.getLogger(CWE80_XSS_File_01_Clean.class);
	
	public static void bad(String param) {

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
		
		// C. Encoding: Encode data before inserting them in the HTML page

		data = ESAPI.encoder().encodeForHTML(data); 
		System.out.println(data);
     
		// Add the data in the HTML page
        FileWriter fw = null;
        BufferedWriter bw = null;
		try {
			fw = new FileWriter("./webpage.html");
			bw = new BufferedWriter(fw);
	        
	        // D. Parameterization: Avoid concatenating Strings
	        String htmlContent = "</body><label>Data: %s </label></body>";
	        htmlContent = htmlContent.format(htmlContent, data);
	        
	        bw.write(htmlContent);
	        
	        
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			/*
			 * Properly release the resources...
			 */
			try {
				if (bw != null) {
					bw.close();
				}
			} catch (IOException e) {
				logger.error("Error closing the BufferedReader!");
				logger.error(e.getMessage());
			}
			
			try {
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {
				logger.error("Error closing the FileReader!");
				logger.error(e.getMessage());
			}
		}
        
        



        
        // Alternative Execution (Using Runtime.exec())
        /* POTENTIAL FLAW: command injection */
//        Process process = Runtime.getRuntime().exec(osCommand + data);
//        process.waitFor();
		
	}
	
    public static void good(String param) throws InterruptedException, IOException {
    	
    	// 1. Read the parameter
        String data = param;

        /* FIX: Use a hardcoded string */
        /* Replace the parameter with a benign String */
        data = "foo";

        String osCommand;
        if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
            /* running on Windows */
            osCommand = "c:\\WINDOWS\\SYSTEM32\\cmd.exe /c dir ";
        } else {
            /* running on non-Windows */
            osCommand = "/bin/ls ";
        }

        /* POTENTIAL FLAW: command injection */
        Process process = Runtime.getRuntime().exec(osCommand + data);
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
				
				//TODO: Remove this print
				System.out.println(parameter);
			}
		} catch(IOException e) {
			logger.error("The file could not be found!");
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
