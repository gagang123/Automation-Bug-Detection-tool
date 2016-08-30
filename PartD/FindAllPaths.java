import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

class AllPaths<T> implements Iterable<T> {

    
    private final Map<T, Map<T, Double>> graph = new HashMap<T, Map<T, Double>>();

   //Add node
    public boolean addNode(T node) {
        if (node == null) {
            throw new NullPointerException("Input node is null");
        }
        if (graph.containsKey(node)) return false;

        graph.put(node, new HashMap<T, Double>());
        return true;
    }

  //Add Edge
    public void addEdge (T source, T destination, double length) {
        if (source == null || destination == null) {
            throw new NullPointerException("Source and Destination, both should be non-null.");
        }
        if (!graph.containsKey(source) || !graph.containsKey(destination)) {
            throw new NoSuchElementException("Source and Destination, both should be part of graph");
        }
       
        graph.get(source).put(destination,length);
    }

   

  
    public Map<T, Double> edgesFrom(T node) {
        if (node == null) {
            throw new NullPointerException("The node should not be null.");
        }
        Map<T, Double> edges = graph.get(node);
        if (edges == null) {
            throw new NoSuchElementException("Source node does not exist.");
        }
        return Collections.unmodifiableMap(edges);
    }

    
    @Override public Iterator<T> iterator() {
        return graph.keySet().iterator();
    }
}


public class FindAllPaths<T> {

    private final AllPaths<T> graph;

    public FindAllPaths(AllPaths<T> graph) {
        if (graph == null) {
            throw new NullPointerException("The input graph cannot be null.");
        }
        this.graph = graph;
    }

   
    public List<List<T>> getAllPaths(T source, T destination) {
        
    	List<List<T>> paths = new ArrayList<List<T>>();
        CheckLoops(source, destination, paths, new LinkedHashSet<T>());
        System.out.println(paths);
        return paths;
    }

    // to remove multiple loops, if any
    private void CheckLoops (T current, T destination, List<List<T>> paths, LinkedHashSet<T> path) {
        path.add(current);

        if (current == destination) {
            paths.add(new ArrayList<T>(path));
            path.remove(current);
            return;
        }

        final Set<T> edges  = graph.edgesFrom(current).keySet();

        for (T t : edges) {
            if (!path.contains(t)) {
                CheckLoops (t, destination, paths, path);
            }
        }

        path.remove(current);
    }    

    public static void main(String[] args) {
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

       

        FindAllPaths<String> findAllPaths = new FindAllPaths<String>(allPaths);
        
        findAllPaths.getAllPaths("B1", "B5");
        

       
    }
}
