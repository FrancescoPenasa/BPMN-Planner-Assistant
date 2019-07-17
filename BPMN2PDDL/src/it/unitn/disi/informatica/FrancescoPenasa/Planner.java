package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 *  //  https://www.mkyong.com/java/how-to-execute-shell-command-from-java/  //
 * @author lithium
 *
 */


public class Planner {

	/**
	 * vado a capire il planner utilizzato, in base al tipo di planner posso descrivere come andrà fatta la chiamata.
	 * chiama il planner con i parametri giusti
	 * @param plannerPath
	 * @param domainPath
	 * @param problemPath
	 * @param outputPath
	 * @param planner
	 */
	public Planner(String plannerPath, String domainPath, String problemPath, String outputPath, String planner) {
		List<String> argv = manage_planner(planner);
		
		String response = "";
		String command = plannerPath + " "
				+ argv.get(0) + " " + domainPath + " "
				+ argv.get(1) + " " + problemPath + " "
				+ argv.get(2) + " " + outputPath + " ";
		boolean waitForResponse = true;
	
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
		pb.redirectErrorStream(true);

		System.out.println("Linux command: " + command);

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

		}

		catch (IOException e) {
			System.out.println("Error occured while executing Linux command. Error Description: "
					+ e.getMessage());
		}

		catch (InterruptedException e) {
			System.out.println("Error occured while executing Linux command. Error Description: "
					+ e.getMessage());
		}
		
		System.out.println("Planner closed, result in --> " + outputPath);
	}
	
	
	/**
	 * take the planner name and returns the parameters used to call the planner
	 * parameter for the domain input for the problem input file
	 * and for the result output file.
	 * @param planner
	 * @return
	 */
	private List<String> manage_planner(String planner) {
		planner = planner.toLowerCase();
		List<String> argv = new ArrayList<String>();
		
		switch (planner) {
		case "strips":
			argv.add("-f");
			argv.add("-o");
			argv.add("-g");
			break;
		case "blackbox":
			argv.add("-o");
			argv.add("-f");
			argv.add("-g");
			break;
		case "colin2":
			argv.add("-f");
			argv.add("-o");
			argv.add("-g");
			break;
		case "popf2":
			argv.add(" ");
			argv.add(" ");
			argv.add(" ");
			break;
		case "ff":
			argv.add("-f");
			argv.add("-o");
			argv.add("-g");
			break;
		case "optic":
			argv.add("-f");
			argv.add("-o");
			argv.add("-g");
			break;
			
		case "satplan":
			argv.add("-f");
			argv.add("-o");
			argv.add("-g");
			break;
			
		default:
			System.err.println("planner not supported, "
					+ "will be used the standard syntax -> domain problem output");
			argv.add(" ");
			argv.add(" ");
			argv.add(" ");
			break;
		}
		return argv;
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

}

