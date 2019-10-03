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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

public class OWASP_XSS_Vuln {
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
            FileWriter fw = new FileWriter("./webpage.html");
            BufferedWriter bw = new BufferedWriter(fw);
            
            String htmlContent = "</body> "
            				   + "<label>"
            				   + "Data: "
            				   + bar 
            				   + "</label>" 
            				   + "</body>";
            
            bw.write(htmlContent);

            bw.close();
            fw.close();

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
