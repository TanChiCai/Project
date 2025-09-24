package assignment;

import java.util.*;

public class BFS {
    
    public static List<String> bfsWithPath(String start, String goal, Map<String, List<Edge>> adjList) {
        Queue<String> queue = new LinkedList<>();
        Map<String, String> parent = new HashMap<>();
        Set<String> visited = new HashSet<>();
        
        queue.add(start);
        visited.add(start);
        parent.put(start, null);
        
        boolean found = false;
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            
            if (current.equals(goal)) {
                found = true;
                break;
            }
            
            for (Edge edge : adjList.getOrDefault(current, new ArrayList<>())) {
                String neighbor = edge.destination;
                
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        
        if (!found) {
            System.out.println("No path found from " + start + " to " + goal);
            return new ArrayList<>();
        }
        
        List<String> path = new ArrayList<>();
        String current = goal;
        int totalTime = 0;
        double totalDistance = 0.0;
        
        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }
        Collections.reverse(path);
        
        for (int i = 0; i < path.size() - 1; i++) {
            String from = path.get(i);
            String to = path.get(i + 1);
            
            for (Edge edge : adjList.get(from)) {
                if (edge.destination.equals(to)) {
                    totalTime += edge.time;
                    totalDistance += edge.distance;
                    break;
                }
            }
        }
        
 
        System.out.println("Total time: " + totalTime + " minutes");
   
        
        return path;
    }
    
    
}