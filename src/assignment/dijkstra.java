/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */

public class dijkstra {
    
    
    public static List<String> dijkstra(String start, String goal, Map<String, List<Edge>> adjList) {
     


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
            return Collections.emptyList();
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
        
        return path;
    }

}
 


