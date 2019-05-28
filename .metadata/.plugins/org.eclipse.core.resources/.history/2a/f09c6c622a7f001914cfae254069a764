/**
 * 
 */
package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;

/**
 * @author ubuntu
 *
 */
public class ToPDDL {

	/**
	 * 
	 */
	
	private String domain = null;
	private String problem = null;
	
	public ToPDDL() {
		// TODO Auto-generated constructor stub
	}
	
	public ToPDDL(String domain, String problem) throws IOException {
		this.domain = domain;
		this.problem = problem;
		
		create_domain();
	}
	
	private void create_domain() throws IOException {
		File file = new File(domain);
		FileOutputStream out = new FileOutputStream(file);
		
		
		out.close();
	}
	
	
}
