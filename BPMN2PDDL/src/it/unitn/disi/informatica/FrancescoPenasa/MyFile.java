package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class MyFile {
	
	//----------- CONST -----------------//
	final private static int RND_SIZE_FROM = 0;
	final private static int RND_SIZE_TO = 4;
	
	/**
	 * Crea un backup del file bpmn2 nella stessa cartella in cui 
	 * e' presente il file originale.
	 * @param source url del file 
	 */
	public static void createBackup(String source, boolean original) {
		String dst;
		if (original) {
			dst = source.replaceAll(".bpmn2", "_original.bpmn2");
		} else {
			String unicode = (String) UUID.randomUUID().toString().subSequence(RND_SIZE_FROM, RND_SIZE_TO);
			dst = source.replaceAll(".bpmn2", "_" + unicode + ".bpmn2");
		}
		Path FROM = Paths.get(source);
        Path TO = Paths.get(dst);
        
        // overwrite the destination file if it exists, and copy
        // the file attributes, including the rwx permissions
        CopyOption[] options = new CopyOption[]{
          StandardCopyOption.REPLACE_EXISTING,
          StandardCopyOption.COPY_ATTRIBUTES
        }; 
        
		try {
			Files.copy(FROM, TO, options);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
	}
}

