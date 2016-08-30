import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class JUnitClass {
private FindAllPaths a;

FindAllPaths<String> findAllPaths;
Demo d;

	@Before
	public void test() {
		
		AllPaths<String> allPaths = new AllPaths<String>();
        allPaths.addNode("B1");
        allPaths.addNode("B2");
        allPaths.addNode("B3");
        allPaths.addNode("B4");
        allPaths.addNode("B5");
        allPaths.addNode("B6");
        allPaths.addNode("B7");
        

        allPaths.addEdge("B1", "B2", 10);
        allPaths.addEdge("B1", "B7", 10);
        allPaths.addEdge("B2", "B3", 10);
        allPaths.addEdge("B7", "B3", 10);
        allPaths.addEdge("B3", "B4", 10);
        allPaths.addEdge("B3", "B6", 10);
        allPaths.addEdge("B4", "B5", 10);
        allPaths.addEdge("B6", "B5", 10);

       

        findAllPaths = new FindAllPaths<String>(allPaths);
        d=new Demo();
        
  
	}
	
	@Test
	 public void addNode() {
		List<List<String>> paths = new ArrayList<List<String>>();

        List<String> path1 = new ArrayList<String>();
        path1.add("B1"); path1.add("B2"); path1.add("B3");path1.add("B4"); path1.add("B5");

        List<String> path2 = new ArrayList<String>();
       path2.add("B1");path2.add("B2");path2.add("B3");path2.add("B6");path2.add("B5");

        List<String> path3 = new ArrayList<String>();
        path3.add("B1"); path3.add("B7"); path3.add("B3");path3.add("B4"); path3.add("B5");

        List<String> path4 = new ArrayList<String>();
        path4.add("B1"); path4.add("B7"); path4.add("B3");path4.add("B6"); path4.add("B5");

        paths.add(path1);
        paths.add(path2);
        paths.add(path3);
        paths.add(path4);
              
        assertEquals(paths, findAllPaths.getAllPaths("B1", "B5"));
    
        assertEquals("ad",d.check(true));//Maps to path [B1, B2, B3, B6, B5]
        assertEquals("bc",d.check(false));//Maps to path  [B1, B7, B3, B4, B5]
        assertEquals("ac",d.check(true));//Maps to path [B1, B2, B3, B4, B5]
        assertEquals("bd",d.check(false));///Maps to path [B1, B7, B3, B6, B5]]

    }

}

