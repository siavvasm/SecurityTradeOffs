/**
* OWASP Benchmark Project v1.2
*
* This file is part of the Open Web Application Security Project (OWASP)
* Benchmark Project. For details, please see
* <a href="https://www.owasp.org/index.php/Benchmark">https://www.owasp.org/index.php/Benchmark</a>.
*
* The OWASP Benchmark is free software: you can redistribute it and/or modify it under the terms
* of the GNU General Public License as published by the Free Software Foundation, version 2.
*
* The OWASP Benchmark is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
* even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* @author Nick Sanidas <a href="https://www.aspectsecurity.com">Aspect Security</a>
* @created 2015
*
* Modified by: Miltiadis Siavvas
*/
package com.miltos.security.clean;

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
import org.owasp.esapi.codecs.WindowsCodec;

public class OWASP_XSS_Clean {
	
	final static Logger logger = Logger.getLogger(OWASP_XSS_Clean.class);
	final static String DATA_PATH = new File("C:\\Users\\siavvasm.ITI-THERMI.000\\Desktop\\input_data.txt").getAbsolutePath();

	public static void main(String[] args) {
		
		FileReader fr = null;
		BufferedReader br = null;
		
		try{
			
			// 1. Open the desired file
	    	fr = new FileReader(DATA_PATH);
	    	br = new BufferedReader(fr);
	    	
	    	// 2. Read the user-defined parameters from the corresponding file
	    	Stream<String> parameters = br.lines();
	    	Iterator<String> parameterIt = parameters.iterator();
	    	
	    	String parameter = "";
	    	ProcessBuilder builder;
	    	Process process;
	    	
	    	// 3. Execute the commands to the console
	    	while(parameterIt.hasNext()) {
	    		
	    		// 3.1 Read the parameter 
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
	    		
	    		
	    		// 3.2 Modify the parameter
	    		String bar = doSomething(parameter);
	    		
	    		// 3.3 Construct the command

            	// C. Encoding: Create and encode the basic command and the parameter	
	    		parameter = ESAPI.encoder().encodeForHTML(parameter);

	    		
	    		// 3.4 Execute the command
	    		/*
	    		 * Execute the command
	    		 */
	    		
	    		
	    		FileWriter fw = null;
	            BufferedWriter bw = null;
	            
	    		try {
	    			fw = new FileWriter("./webpage.html");
	    			bw = new BufferedWriter(fw);
	    	        
	    			// D. Parameterization: Avoid String concatenation 
	    	        String htmlContent = "</body><label>Data: %s </label></body>";
	    	        htmlContent = htmlContent.format(htmlContent, parameter);
	    	        
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
	   }
		} catch (IOException e){
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

	private static String doSomething(String parameter) {
		if ("stop".equals(parameter)) {
			return "foo";
		} else {
			return parameter;
		}
	}

}
