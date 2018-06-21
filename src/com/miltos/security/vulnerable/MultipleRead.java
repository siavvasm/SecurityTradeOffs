package com.miltos.security.vulnerable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.stream.Stream;


public class MultipleRead {
	
	final static String DATA_PATH = new File("C:\\Users\\siavvasm.ITI-THERMI.000\\Desktop\\data.txt").getAbsolutePath();

	public static void main(String[] args) throws Exception {

    	// 1. Open the desired file
    	FileReader fr = new FileReader(DATA_PATH);
    	BufferedReader br = new BufferedReader(fr);
    	
    	// 2. Read the commands that are stored in this file
    	Stream<String> commands = br.lines();
    	Iterator<String> commandsIt = commands.iterator();
    	
    	// Initialization of required objects
    	String command = "";
    	ProcessBuilder builder;
    	Process process;
    	
    	// 3. Execute the commands...
    	while(commandsIt.hasNext()) {
    		
    		// Read the desired command 
    		command = commandsIt.next();
    
    		// Create a builder for executing the desired command
    		builder = new ProcessBuilder("cmd.exe", "/c", command);
        	builder.redirectErrorStream(true);
    			
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
    		
    	}
    	
    	// 4. Close the connection to the file
    	br.close();
    	fr.close();
	}
}
