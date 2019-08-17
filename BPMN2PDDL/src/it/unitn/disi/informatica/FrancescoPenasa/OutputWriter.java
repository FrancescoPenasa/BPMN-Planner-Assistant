package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * @author FrancescoPenasa
 * 
 */
public class OutputWriter{

	//====================================== PARAMETERS ========================================//
	
	//---------------------------------------- private ---------------------------------------- //	
	private FileWriter fileWriter = null;
	
	
	
	// ==================================== constructor ====================================== //
	public OutputWriter() {
	}
	
	
	
	// ====================================== METHODS ========================================= //
	
	//---------------------------------------- public ----------------------------------------- //
	public OutputWriter(String name) throws IOException {
		File file = new File(name); 
        if(file.exists()) { 
        	System.err.println("File: " + file.toString() + " already exist.\n"
		    		+ "Please delete " + file.toString());
        	
        	//debug purpose
        	System.out.println("Actually, its debug mode");
        	file.delete();
        	//System.exit(1);
        }
        
        fileWriter = new FileWriter(name, true);		
	}
	
	
	public FileWriter getFileWriter() {
		return fileWriter;
	}
}
