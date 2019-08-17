package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * @author FrancescoPenasa
 * 
 */
public class FileScrapper {
	
	// ====================================== METHODS ========================================= //
	
	// --------------------------------------- public ----------------------------------------- //	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public static String get_domain_name(String url) {
		String domain = null;
		try (BufferedReader br = new BufferedReader(new FileReader(url))) {
			String line;
			while((line = br.readLine()) != null) {
				if (line.contains("domain")) {
					int i = line.indexOf("domain");
					domain = line.substring(i, line.length()-1);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		domain = domain.replaceAll("domain", "");
		domain = domain.replaceAll(" ", "");
		return domain;
	}
}
