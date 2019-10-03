/* TEMPLATE GENERATED TESTCASE FILE
Filename: CWE78_OS_Command_Injection__File_08.java
Label Definition File: CWE78_OS_Command_Injection.label.xml
Template File: sources-sink-08.tmpl.java
*/
/*
* @description
* CWE: 78 OS Command Injection
* BadSource: File Read data from file (named c:\data.txt)
* GoodSource: A hardcoded string
* BadSink: exec dynamic command execution with Runtime.getRuntime().exec()
* Flow Variant: 08 Control flow: if(privateReturnsTrue()) and if(privateReturnsFalse())
*
* */
/*
* Modified by: Miltiadis Siavvas
*/
package com.miltos.security.vulnerable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

public class CWE78_OS_Command_Injection_File_08_Vuln {
	
	final static String DATA = new File("C:\\Users\\siavvasm.ITI-THERMI.000\\Desktop\\input_data.txt").getAbsolutePath();
	
    /* The methods below always return the same value, so a tool
     * should be able to figure out that every call to these
     * methods will return true or return false.
     */
    private static boolean privateReturnsTrue() throws Exception
    {
        return true;
    }

    private static boolean privateReturnsFalse() throws Exception
    {
        return false;
    }
    
	public static void bad(String param) throws Exception {
		//TODO: Remove this print 
		System.out.println("Inside BAD method");
		// 1. Define the data of the command
		String data;
		data = param;
		
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
        
        // 3. Execute the command
        /* POTENTIAL FLAW: command injection */
        if(privateReturnsTrue()) {
            data = data + " true";
        } else {
        	data = data + " false";
        }

        Process process = Runtime.getRuntime().exec(osCommand + data);
        process.waitFor();
        osCommand = null;
        data = null;
        param = null;

        
		//TODO: Remove this print 
		System.out.println("End of BAD method");
		
	}
	
    public static void good(String param) throws Exception {
    	
    	// 1. Read the parameter
        String data = param;

        /* FIX: Use a hardcoded string */
        /* Replace the parameter with a benign String */
        data = null;

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
    
	public static void main(String[] args) throws Exception {
		
		//TODO: Remove this print
		System.out.println("This is a dummy class!!");
		
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
			System.out.println(parameter);
		}
		
		parameter = null;
		fr = null;
		br = null;
	}

}
