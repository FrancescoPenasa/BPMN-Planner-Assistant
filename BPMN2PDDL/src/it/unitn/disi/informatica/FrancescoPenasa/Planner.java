package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 *  //  https://www.mkyong.com/java/how-to-execute-shell-command-from-java/  //
 * @author lithium
 *
 */


public class Planner {
	
	private String outputPath = "";

	/**
	 * genero un file di output nello stesso path del file problema
	 * chiamo il planner con i parametri specificati nell input sostituendo a domain e prob i path veri
	 * @param planner
	 * @param domainPath
	 * @param problemPath
	 * @param outputPath
	 */
	public Planner(String planner, String domainPath, String problemPath) {
		this.outputPath = problemPath.replace(".pddl", "_output");
		File file = new File(this.outputPath);
		this.outputPath = file.getAbsolutePath();
		
		planner = planner.replace("domain0", domainPath);
		planner = planner.replace("prob0", problemPath);
		planner = planner.replace("output0", this.outputPath);
		
		String response = "";
		String command = planner;
		boolean waitForResponse = true;
		
		System.out.println("Linux command: " + command);
	
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
		pb.redirectErrorStream(true);

		try {
			Process shell = pb.start();

			if (waitForResponse) {

				// To capture output from the shell
				InputStream shellIn = shell.getInputStream();

				// Wait for the shell to finish and get the return code
				int shellExitStatus = shell.waitFor();
				System.out.println("Calling the planner --> Exit status: " + shellExitStatus);

				//response = convertStreamToStr(shellIn);

				shellIn.close();
			}
		} catch (IOException e) {
			System.out.println("Error occured while executing Linux command. Error Description: "
					+ e.getMessage());
		} catch (InterruptedException e) {
			System.out.println("Error occured while executing Linux command. Error Description: "
					+ e.getMessage());
		}
		
		
		System.out.println("Planner closed, result in --> " + outputPath);
	}
	

	/*
	 * To convert the InputStream to String we use the Reader.read(char[]
	 * buffer) method. We iterate until the Reader return -1 which means
	 * there's no more data to read. We use the StringWriter class to
	 * produce the string.
	 */
	public static String convertStreamToStr(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		}
		else {
			return "";
		}
	}



	public String getOutputURL() {
		return this.outputPath;
	}

}


