/**
 * 
 */
package it.unitn.disi.informatica.FrancescoPenasa;

/**
 * @author lithium
 *
 */
public class myString {
	//  method checks if character is parenthesis(open 
	// or closed) 
	public static boolean isParenthesis(char c) 
	{ 
	    return ((c == '(') || (c == ')')); 
	} 
  
	//  method returns true if string contains valid 
	// parenthesis 
	public static boolean isValidString(String str) 
	{ 
	    int cnt = 0; 
	    for (int i = 0; i < str.length(); i++) 
	    { 
	        if (str.charAt(i) == '(') 
	            cnt++; 
	        else if (str.charAt(i)  == ')') 
	            cnt--; 
	        if (cnt < 0) 
	            return false; 
	    } 
	    return (cnt == 0); 
	} 
	
  
	//  method to remove invalid parenthesis 
	public static String removeInvalidParenthesis(String str) 
	{
		char c;
		int open = 0;
	    
	    for (int i = 0; i < str.length(); i++) {
	    	c = str.charAt(i);
	    	if (c == '(') {
	    		open++;
	    	}
	    	if (c == ')'){
	    		open--;	    		
	    		if (open < 0) {
	    			str = str.substring(0, i) + str.substring(i+1, str.length());
	    			i--;
	    			open = 0;
	    		}
	    	}
	    }
		return str;
	} 
}