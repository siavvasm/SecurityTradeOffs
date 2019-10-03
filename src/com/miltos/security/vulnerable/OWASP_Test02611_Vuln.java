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
package com.miltos.security.vulnerable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

public class OWASP_Test02611_Vuln {
	final static String DATA_PATH = new File("C:\\Users\\siavvasm.ITI-THERMI.000\\Desktop\\input_data.txt").getAbsolutePath();

	public static void main(String[] args) throws IOException, InterruptedException {
		
		// 1. Open the desired file
    	FileReader fr = new FileReader(DATA_PATH);
    	BufferedReader br = new BufferedReader(fr);
    	
    	// 2. Read the user-defined parameters from the corresponding file
    	Stream<String> parameters = br.lines();
    	Iterator<String> parameterIt = parameters.iterator();
    	
    	String parameter = "";
    	
    	// 3. Execute the commands to the console
    	while(parameterIt.hasNext()) {
    		
    		// 3.1 Read the parameter 
    		parameter = parameterIt.next();
    		
    		// 3.2 Modify the parameter
    		String bar = doSomething(parameter);

    		// 3.3 Construct the command
    		String cmd = "";	
    		String a1 = "";
    		String a2 = "";
    		String[] args1 = null;
    		String osName = System.getProperty("os.name");

    		if (osName.indexOf("Windows") != -1) {
            	a1 = "cmd.exe";
            	a2 = "/c";
            	cmd = "echo ";
            	args1 = new String[]{a1, a2, cmd, bar};
            } else {
            	a1 = "sh";
            	a2 = "-c";
            	cmd = "ls";
            	args1 = new String[]{a1, a2, cmd, bar};
            	System.out.println(parameter);
            }
    		
    		// 3.4 Execute the command
    		Runtime r = Runtime.getRuntime();
    		Process p = r.exec(args1);
            p.waitFor();

    	}
    	// 4. Close the connection to the file
    	br.close();
    	fr.close();

	}

	private static String doSomething(String parameter) {
		if (parameter.equals("stop")) {
			return "foo";
		} else {
			return parameter;
		}
	}

}
