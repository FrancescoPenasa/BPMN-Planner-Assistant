/**
 * 
 */
package javatest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lithium
 *
 */
public class javatest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<List<String>> res = new ArrayList<List<String>>();
		List<String> nodes = new ArrayList<String>();;
		
		nodes.add("test");
		res.add(nodes);
		nodes.add("test2");
		System.out.print(res);
	}

}
