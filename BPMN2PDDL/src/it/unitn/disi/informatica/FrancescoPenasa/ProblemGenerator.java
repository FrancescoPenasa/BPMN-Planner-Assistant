package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

/**
 * 
 * @author FrancescoPenasa
 * 
 */
class ProblemGenerator {
	
	// ===================================== PARAMETERS ======================================= //
	
	// -------------------------------------- private ----------------------------------------- //	
	private FileWriter writer = null;
	private String nameFile = "";
	

	
	// ====================================== METHODS ========================================= //
	
	//------------------------------------- constructors -------------------------------------- //
	/**
	 * ProblemGenerator create a problem.pddl file that contains the data to match a domain.pddl file 
	 * the file will be readable for a planner that is able to read the syntax of pddl1.1
	 * @param domain
	 * @param from
	 * @param to
	 * @throws IOException
	 */
	public ProblemGenerator(String domain, String from, String to) throws IOException {
		String unicode = (String) UUID.randomUUID().toString().subSequence(0, 4);
		this.nameFile = domain + "_prob" + unicode + ".pddl";
		// open the writer 
		OutputWriter w = new OutputWriter (nameFile);
		writer = w.getFileWriter();
		File file = new File(this.nameFile);
		this.nameFile = file.getAbsolutePath();
		
		// start writing the prob file
		// until (:domain ; first 4 rows
		writeIntro(domain);
		
		String objects = getObjects(from);
		// write objects
		writeObjects(objects);
		
		String init = getInit(from);
		// write the init states, and init the constrans at 0
		writeInit(init);		

		String goals = getGoals(to);
		// write the goal states
		writeGoal(goals);
		
		//last parenthesiS
		writeOutro();
		
		writer.flush();
		writer.close();
		
		System.out.flush();	
		System.out.println("Standard Prob generator finished!");
	}
	

	/**
	 * ProblemGenerator create a problem.pddl file that contains the data to match a domain.pddl file 
	 * the file will be readable for a planner that is able to read the syntax of pddl2.1 or pddl3.1 
	 * due to the presence of the metrics section.
	 * @param domain
	 * @param from
	 * @param to
	 * @param max_constraints
	 * @param min_constraints
	 * @throws IOException
	 */
	public ProblemGenerator(String domain, String from, String to, String max_constraints, String min_constraints) throws IOException {
		String constrains = max_constraints + min_constraints;
		// open the writer and create a new file called "domain_prob_constraint0_constraint1_.pddl
		this.nameFile = domain + "_prob" + generateNameFile(constrains) + ".pddl";
		OutputWriter w = new OutputWriter (nameFile);
		writer = w.getFileWriter();
		File file = new File(this.nameFile);
		this.nameFile = file.getAbsolutePath();
		
		// write info for the planner as the name of the problem and the domain
		writeIntro(domain, constrains);	
		
		String objects = getObjects(from);
		// write objects
		writeObjects(objects);
		
		String init = getInit(from);
		// write the init states, and init the constrans at 0
		writeInit(init, constrains);		

		String goals = getGoals(to);
		// write the goal states
		writeGoal(goals);
		
		// write the metric conditions used to prefer output that
		writeMetric(max_constraints, min_constraints);
		
		// write the end of the file
		writeOutro();
		
		writer.flush();
		writer.close();
		
		System.out.flush();	
		System.out.println(nameFile + " has been generated!");
		System.out.flush();	
	}


	
	//---------------------------------------- private ---------------------------------------- //
	private String generateNameFile (String constraints) {
		String name = constraints;
		name = name.replaceAll("\\(", "_");
		name = name.replaceAll("\\)", "");
		return name;
	}
	
	
	private String getGoals(String path) {
		String line;
		String goals = "";
		String start_marker = ":goal";
		boolean reading = false;
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			while((line = br.readLine()) != null) {
				if(reading) {
					goals += line;
				}
				if(line.contains(start_marker)){
					reading = true;
					int index = line.indexOf(start_marker);
					index += start_marker.length();
					goals += line.subSequence(index, line.length());					
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return MyString.removeInvalidParenthesis(goals);
	}
	

	private String getInit(String path) {
		String line;
		String goals = "";
		String marker = ":init";
		String marker_stop = ":goal";
		boolean reading = false;
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			while((line = br.readLine()) != null) {
				if(line.contains(marker_stop)){
					reading = false;				
				}
				if(reading) {
					goals += line;
				}
				if(line.contains(marker)){
					reading = true;
					int index = line.indexOf(marker);
					index += marker.length();
					goals += line.subSequence(index, line.length());					
				}				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return MyString.removeInvalidParenthesis(goals);
	}
	

	private String getObjects(String path) {
		String line;
		String goals = "";
		String marker = ":objects";
		String marker_stop = ":init";
		boolean reading = false;
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			while((line = br.readLine()) != null) {
				if(line.contains(marker_stop)){
					reading = false;				
				}
				if(reading) {
					goals += line;
				}
				if(line.contains(marker)){
					reading = true;
					int index = line.indexOf(marker);
					index += marker.length();
					goals += line.subSequence(index, line.length());					
				}				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return MyString.removeInvalidParenthesis(goals);
	}
	

	private void writeIntro(String DOMAIN) throws IOException {		
		writer.write(";; problem file: " + DOMAIN + "_prob0.pddl \n\n");		
		writer.write("(define (problem " + DOMAIN + "_prob0)\n");
		writer.write("\t(:domain " + DOMAIN + ")\n\n");
	}
	
	
	private void writeIntro(String DOMAIN, String constraints) throws IOException {	
		constraints = constraints.replaceAll(" ", "_");
		constraints = constraints.replaceAll("\\(", "");
		constraints = constraints.replaceAll("\\)", "");
		
		writer.write(";; problem file: " + DOMAIN + "_" + constraints + ".pddl \n\n");		
		writer.write("(define (problem " + DOMAIN + "_" + constraints + ")\n");
		writer.write("\t(:domain " + DOMAIN + ")\n\n");
	}
	
	
	private void writeInit(String INIT) throws IOException {
		writer.write("\t(:init\n");
		writer.write("\t\t" + INIT + "\n");
		writer.write("\t)\n\n");
	}
		

	private void writeInit(String INIT, String constraint) throws IOException {
		writer.write("\t(:init\n");
		writer.write("\t\t" + INIT + "\n");
		
		String [] constraints = constraint.split("\\)");
		
		for (int i = 0; i < constraints.length; i++) {
			int index = constraints[i].indexOf("(");
			writer.write("\t\t(= (" + constraints[i].substring(index+1) + ") 0)\n");
		}
		writer.write("\t)\n\n");
	}
		

	private void writeObjects(String OBJ) throws IOException {
		writer.write("\t(:objects\n");
		writer.write("\t\t" + OBJ + "\n");
		writer.write("\t)\n\n");
	}
	
	
	private void writeOutro() throws IOException {
		final String OUTRO = new String(")");
		writer.write(OUTRO);
	}

	
	private void writeGoal(String goal) throws IOException {
		writer.write("\t(:goal\n");
		writer.write("\t\t" + goal + "\n");
		writer.write("\t)\n");
	}

	
	private void writeMetric(String maximize, String minimize) throws IOException {
		writer.write("\t(:metric \n");
		

		if (minimize.length() > 1) {
			writer.write("\t\tmaximize ");	
			writer.write(maximize + "\n");
		}
			
		
		if (minimize.length() > 1) {
			writer.write("\t\tminimize ");			
			writer.write(minimize + "\n");
		}
		
		writer.write("\t)\n");		
	}


	
	// =============================== SETTER AND GETTER ====================================== //
	public String getUrl() {
		return this.nameFile;
	}
}