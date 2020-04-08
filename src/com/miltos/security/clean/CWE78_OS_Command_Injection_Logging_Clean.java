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

public class CWE78_OS_Command_Injection_Logging_Clean {
	
	final static String DATA = new File("C:\\Users\\siavvasm.ITI-THERMI.000\\Desktop\\input_data.txt").getAbsolutePath();
	final static Logger logger = Logger.getLogger(CWE78_OS_Command_Injection_Logging_Clean.class);
	
	public static void bad(String param) throws InterruptedException, IOException {
		
		// 1. Define the data of the command
		String data;
		data = param;
		
		// Log the parameter
		if(logger.isDebugEnabled()) {
			logger.debug("*** Start of potentially vulnerable method ***");
			logger.debug("*");
			logger.debug("* Issue: CWE78 - OS Command Injection");
			logger.debug("* Source: Juliet Test Suite");
			logger.debug("* ");
			logger.debug("* Input Parameter: " + data);
		}
		

		
		// 2. Define the command
        String osCommand;
        if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
        {
            /* running on Windows */
            osCommand = "c:\\WINDOWS\\SYSTEM32\\cmd.exe /c dir ";
        }
        else
        {
            /* running on non-Windows */
            osCommand = "/bin/ls ";
        }
        
   
        // D. Parameterization: Use ProcessBuilder instead of Runtime.exec()
        if(logger.isDebugEnabled()) {
			logger.debug("* The command that it is going to be executed is: " + osCommand + " " + data);
		}
        Process process = Runtime.getRuntime().exec(osCommand + data);
        process.waitFor();
        osCommand = null;
        data = null;
        
		//TODO: Remove this print 
        if(logger.isDebugEnabled()) {
        	logger.debug("* ");
        	logger.debug("*** End of potentially vulnerable method ***");
        }
        
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
    
	public static void main(String[] args) throws InterruptedException, IOException {
			
		//TODO: Remove this print
        if(logger.isDebugEnabled()) {
        	logger.debug("*** Executing an application that is vulnerable to OS Command Injection ...");
        }
		
		// 1. Open the desired file
		FileReader fr = new FileReader(DATA);
		BufferedReader br = new BufferedReader(fr);
		
		// 2. Read the user defined parameters from the desired file
		Stream<String> parameters = br.lines();
		Iterator<String> parameterIt = parameters.iterator();
		
		String parameter = "";
		
		// 3. Execute the command for each parameter
		while(parameterIt.hasNext()) {
			
			// Read the parameter
			parameter = parameterIt.next();
			
			// Execute the command with this parameter
			if(!parameter.equals(null)){
				bad(parameter);
			}

			//TODO: Remove this print
	        if(logger.isDebugEnabled()) {
	        	logger.debug("* " + parameter);
	        }
		}
		
		// 4. Release the resources
		br.close();
		fr.close();
		
			
	}

}
