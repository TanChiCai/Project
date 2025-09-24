/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
/**
 *
 * @author poher
 */
public class Graph implements InterfaceGraph{
    private Map<String, List<Edge>> adjList;
    private List<List<String>> allPaths;
    private List<Integer> allTimes; // store the time for each path

    public Graph() {
        adjList = new HashMap<>();
        allPaths = new ArrayList<>();
        allTimes = new ArrayList<>();
    }
    public List<List<String>> getAllPaths() {
        return allPaths;
    }
    
    public void addLocation(String location) {
        // Initialize an empty list of edges for this location
        adjList.putIfAbsent(location, new ArrayList<>());
    }
    
    public void removeLocation(String location) {
        // Remove the location and all edges connected to it
        adjList.remove(location);
        
        // Remove any edges pointing to this location
        for (List<Edge> edges : adjList.values()) {
            edges.removeIf(edge -> edge.destination.equals(location));
        }
    }
    
    public boolean removeEdge(String start, String end) {
        if (!adjList.containsKey(start)) {
            return false;
        }

        // Remove only the edge from start to end
        return adjList.get(start).removeIf(edge -> edge.destination.equals(end));
    }
    
    public boolean locationExists(String location) {
        return adjList.containsKey(location);
    }
    
    @Override
    public void addEdge(String src, String dest, int time, double distance) {
        adjList.putIfAbsent(src, new ArrayList<>());
        
        for (Edge e: adjList.get(src)) {
            if (e.destination.equals(dest)) {
                if (e.time == time && e.distance == distance) {
                    System.out.println("Edge from " + src + " to " + dest + " already exists with the same values!");
                    return;
                }
            }
        }
        adjList.get(src).add(new Edge(dest, time, distance));
        System.out.println("Added line from " + src + " to " + dest);
    }
    
    public List<String> dfs(String startNode, String goal) {
        allPaths.clear();
        allTimes.clear();

        List<String> path = new ArrayList<>();
        dfsHelper(startNode, goal, path, 0);

        if (allPaths.isEmpty()) {
            System.out.println("No path found");
            return Collections.emptyList();
        }

        int maxIndex = 0;
        for (int i = 0; i < allTimes.size(); i++) {
            if (allTimes.get(i) > allTimes.get(maxIndex)) {
                maxIndex = i;
            }
            else if (allTimes.get(i) == allTimes.get(maxIndex)) {
                // Check number of stops
                if (allPaths.get(i).size() > allPaths.get(maxIndex).size()) {
                    maxIndex = i;
                }
            }
        }
        
        List<String> longestPath = allPaths.get(maxIndex);
        
        System.out.println("");
        System.out.println("Longest path (time = " + allTimes.get(maxIndex) + " min): " + allPaths.get(maxIndex));
        
        return longestPath;
    }

    public void dfsHelper(String node, String goal, List<String> path, int currentTime) {
        path.add(node);
        
        if (node.equals(goal)) {
            allPaths.add(new ArrayList<>(path));
            allTimes.add(currentTime);
            System.out.println("Path: " + path + " | Time: " + currentTime + " minutes");
        }
        else {
            for (Edge edge: adjList.getOrDefault(node, new ArrayList<>())){
                String neighbor = edge.destination;
                if (!path.contains(neighbor)) {
                    dfsHelper(neighbor, goal, path, currentTime + edge.time);
                }
            }
        }
        
        path.remove(path.size() - 1);
    }
    
    
    
    public void dijkstra(String start, String goal) {
        Map<String, Integer> timeMap = new HashMap<>();  //<Node name,travel time>
        Map<String, String> prev = new HashMap<>();     //Stores the previous node for each node in the shortest path
        Set<String> visited = new HashSet<>();  //Keeps track of nodes we've already fully processed, so we don’t reprocess them.

        PriorityQueue<NodeTime> priorityQueue = new PriorityQueue<>((a, b) -> Integer.compare(a.time, b.time)); //always pick the next node with the least time.
        priorityQueue.offer(new NodeTime(start, 0));  //Add the starting node to the priority queue with time = 0
        timeMap.put(start, 0); //begin time is 0

        while (!priorityQueue.isEmpty()) {
            NodeTime current = priorityQueue.poll(); //Get the node with the smallest travel time
            String currentNode = current.node;
            int currentTime = current.time;

            if (visited.contains(currentNode)) {
                continue;
            }
            visited.add(currentNode);

            if (currentNode.equals(goal)) {
                break;
            }

            for (Edge edge : adjList.getOrDefault(currentNode, new ArrayList<>())) {
                String neighbor = edge.destination;
                int newTime = currentTime + edge.time;

                if (!timeMap.containsKey(neighbor) || newTime < timeMap.get(neighbor)) {
                    timeMap.put(neighbor, newTime);
                    prev.put(neighbor, currentNode); //reached the node neighbor by coming from the node currentNode
                    priorityQueue.offer(new NodeTime(neighbor, newTime));
                }
            }
        }

        if (!timeMap.containsKey(goal)) {
            System.out.println("No path found from " + start + " to " + goal);
            return;
        }

        // Reconstruct path
        List<String> path = new ArrayList<>();
        String current = goal;
        while (current != null) {
            path.add(current);
            current = prev.get(current);  //get the previous code
        }
        Collections.reverse(path);

        System.out.println("Shortest path by time from " + start + " to " + goal + ": " + path);
        System.out.println("Total time: " + timeMap.get(goal) + " minutes");
    }
    
    public Map<String, List<Edge>> getAdjList() {
        return adjList;
    }

    @Override
    public void printGraph() {
        for (String node: adjList.keySet()) {
            System.out.print(node + " -> ");
            for (Edge e: adjList.get(node)) {
                System.out.println(e.destination + "(" + e.time + "min, " + e.distance + "km), ");
            }
        }
    }
}
