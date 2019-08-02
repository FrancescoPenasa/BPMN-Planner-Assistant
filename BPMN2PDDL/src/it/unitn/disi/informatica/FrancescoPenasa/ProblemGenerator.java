package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class ProblemGenerator {
	
	private FileWriter writer = null;
	
	private String nameFile = "";
	


	/**
	 * questo costruttore si occupa di costrure il file pddl problem con le caratteristiche base,
	 * cioè, si occupa di costruire un file di problem.pddl conforme alle caratteristiche di PDDL 1.1
	 * con i requirements :strips :typing
	 * @param domain
	 * @param objects
	 * @param init
	 * @param goals
	 * @throws IOException
	 */
	public ProblemGenerator(String domain, String objects, String init, String goals) throws IOException {
		this.nameFile = domain + "_prob.pddl";
		// open the writer 
		OutputWriter w = new OutputWriter (nameFile);
		writer = w.getFileWriter();
		File file = new File(this.nameFile);
		this.nameFile = file.getAbsolutePath();
		
		// start writing the prob file
		// until (:domain ; first 4 rows
		writeIntro(domain);
		
		//(:objects
		writeObjects(objects);
		
		//init state
		writeInit(init);
		
		//goal state
		writeGoal(goals);
		
		//last parenthesiS
		writeOutro();
		
		writer.flush();
		writer.close();
		
		System.out.flush();	
		System.out.println("Standard Prob generator finished!");
	}
	
	private String generateNameFile (String constraints) {
		String name = constraints;
		name = name.replaceAll("\\(", "_");
		name = name.replaceAll("\\)", "");
		return name;
	}

	/**
	 * questo costruttore si occupa di generare un file problem.pddl con nome @param domain + "_prob
	 * @param domain
	 * @param objects
	 * @param init
	 * @param goals
	 * @param costrain
	 * @param condition
	 * @throws IOException
	 */
	public ProblemGenerator(String domain, String objects, String init, String goals, String max_constraints, String min_constraints) throws IOException {
		String constrains = max_constraints + min_constraints;
		// open the writer and create a new file called "domain_prob_constraint0_constraint1_.pddl
		this.nameFile = domain + "_prob" + generateNameFile(constrains) + ".pddl";
		OutputWriter w = new OutputWriter (nameFile);
		writer = w.getFileWriter();
		File file = new File(this.nameFile);
		this.nameFile = file.getAbsolutePath();
		
		// write info for the planner as the name of the problem and the domain
		writeIntro(domain, constrains);
		
		// write objects
		writeObjects(objects);
		
		// write the init states, and init the constrans at 0
		writeInit(init, constrains);		

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


	/**
	 * write definition and requirements on the domain file
	 * @throws IOException
	 */
	private void writeIntro(String DOMAIN) throws IOException {		
		writer.write(";; problem file: " + DOMAIN + "_prob0.pddl \n\n");		
		writer.write("(define (problem " + DOMAIN + "_prob0)\n");
		writer.write("\t(:domain " + DOMAIN + ")\n\n");
	}
	
	/**
	 * write definition and requirements on the domain file
	 * @throws IOException
	 */
	private void writeIntro(String DOMAIN, String constraints) throws IOException {	
		constraints = constraints.replaceAll(" ", "_");
		constraints = constraints.replaceAll("\\(", "");
		constraints = constraints.replaceAll("\\)", "");
		
		writer.write(";; problem file: " + DOMAIN + "_" + constraints + ".pddl \n\n");		
		writer.write("(define (problem " + DOMAIN + "_" + constraints + ")\n");
		writer.write("\t(:domain " + DOMAIN + ")\n\n");
	}
	
	
	//-------------------- INIT -------------------///
	// (at rabbit a) (at carrot b) (at box c)
	/**
	 * 
	 * @param INIT
	 * @throws IOException
	 */
	private void writeInit(String INIT) throws IOException {
		writer.write("\t(:init\n");
		writer.write("\t\t" + INIT + "\n");
		writer.write("\t)\n\n");
	}
	
	//-------------------- INIT -------------------///
	// (at rabbit a) (at carrot b) (at box c)
	/**
	 * 
	 * @param INIT
	 * @throws IOException
	 */
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
	
	
	//------------------------ OBJECTS ------------------------//
	// OBJ should be something like carrot rabbit box a b c
	/**
	 * 
	 * @param OBJ
	 * @throws IOException
	 */
	private void writeObjects(String OBJ) throws IOException {
		writer.write("\t(:objects\n");
		writer.write("\t\t" + OBJ + "\n");
		writer.write("\t)\n\n");
	}
	

	

	/**
	 * 
	 * @throws IOException
	 */
	private void writeOutro() throws IOException {
		final String OUTRO = new String(")");
		writer.write(OUTRO);
	}

	
	//-------------- GOAL ----------------------------//
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


	public Object getPath() {
		// TODO Auto-generated method stub
		return "pat";
	}

	public String getUrl() {
		return this.nameFile;
	}

}