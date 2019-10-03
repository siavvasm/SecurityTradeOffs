/* TEMPLATE GENERATED TESTCASE FILE
Filename: CWE78_OS_Command_Injection__File_01.java
Label Definition File: CWE78_OS_Command_Injection.label.xml
Template File: sources-sink-01.tmpl.java
*/
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

public class CWE78_OS_Command_Injection_File_01_Vuln {
	
	final static String DATA = new File("C:\\Users\\siavvasm.ITI-THERMI.000\\Desktop\\input_data.txt").getAbsolutePath();

	public static void bad(String param) throws Exception, RuntimeException, NullPointerException {
		System.out.println("*** Start of potentially vulnerable method ***");
		String data;
		data = param;
        String osCommand;
        if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
            osCommand = "c:\\WINDOWS\\SYSTEM32\\cmd.exe /c dir ";
        } else {
            osCommand = "/bin/ls ";
        }
        Process process = Runtime.getRuntime().exec(osCommand + data);
        process.waitFor();
		System.out.println("*** End of potentially vulnerable method ***");
	}
	
	public static void main(String[] args) throws Exception, RuntimeException, NullPointerException {
		System.out.println("*** Executing an application that is vulnerable to OS Command Injection ...");
		FileReader fr = new FileReader(DATA);
		BufferedReader br = new BufferedReader(fr);
		Stream<String> parameters = br.lines();
		Iterator<String> parameterIt = parameters.iterator();
		String parameter = "";
		while(parameterIt.hasNext()) {
			parameter = parameterIt.next();
			bad(parameter);
			System.out.println(parameter);
		}
		br.close();
		fr.close();
	}


}
