/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment;

import static assignment.dijkstra.dijkstra;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
/**
 *
 * @author user
 */
public class AddDeleteLocationLine {
    
    // This should be a method, not a constructor
    public static void showMenu(InterfaceGraph graph, MapVisualization mapVisualization) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Welcome to 8-11 Bus Route Management System");

        while (true) {
            System.out.println("\n=== Console Menu ===");
            System.out.println("1. Add location");
            System.out.println("2. Delete location");
            System.out.println("3. Add line");
            System.out.println("4. Delete line");
            System.out.println("5. Find shortest Time Taken");
            System.out.println("6. Find longest path");
            System.out.println("7. Find shortest path");

            System.out.println("0. Exit");
            System.out.print("Please choose a number: ");

            String num = scanner.nextLine();

            while(!num.equals("0") && !num.equals("1") && !num.equals("2") && 
                  !num.equals("3") && !num.equals("4") && !num.equals("5") &&
                  !num.equals("6")&&!num.equals("7"))
            {
                System.out.print("Invalid number, please enter again: ");
                num = scanner.nextLine();
            }

            if (num.equals("1")){
                if (addLocation(scanner, graph, mapVisualization)) {
                    mapVisualization.refreshMap();
                }
            } else if (num.equals("2")){
                if (deleteLocation(scanner, graph, mapVisualization)) {
                    mapVisualization.refreshMap();
                }
            } else if (num.equals("3")){
                if (addLine(scanner, graph, mapVisualization)) {
                    mapVisualization.refreshMap();
                }
            } else if (num.equals("4")){
                if (deleteLine(scanner, graph, mapVisualization)) {
                    mapVisualization.refreshMap();
                }
            } else if (num.equals("5")){
                findShortestTimeTaken(scanner, graph, mapVisualization);
            } else if (num.equals("6")){  
                findLongestPath(scanner, graph, mapVisualization);
            }else if (num.equals("7")){  
                findShortestPath(scanner, graph, mapVisualization);
            } else if (num.equals("0")){
                scanner.close();
                System.exit(0);
            }
        }
    }
    
    private static boolean addLocation(Scanner scanner, InterfaceGraph graph, MapVisualization mapVisualization) {
        System.out.print("Enter new location name to add: ");
        String location = scanner.nextLine();

        // Check if location already exists
        if (mapVisualization.locationExists(location)) {
            System.out.println("Location '" + location + "' already exists!");
            return false;
        }

        // Get coordinates from user
        int x = -1, y = -1;
        // Validate X coordinate immediately
        while (true) {
            System.out.print("Enter X coordinate (50 - 1150): ");
            String input = scanner.nextLine().trim();
            try {
                x = Integer.parseInt(input);
                if (x < 50 || x > 1150) {
                    System.out.println("X must be between 50 and 1150!");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter an integer for X.");
            }
        }

        // Validate Y coordinate immediately
        while (true) {
            System.out.print("Enter Y coordinate (50 - 750): ");
            String input = scanner.nextLine().trim();
            try {
                y = Integer.parseInt(input);
                if (y < 50 || y > 750) {
                    System.out.println("Y must be between 50 and 750!");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter an integer for Y.");
            }
        }
        
        // ✅ Check if coordinates are already taken by another location
        if (mapVisualization.locationExists(location)) {
            System.out.println("Location '" + location + "' already exists!");
            return false;
        }

        for (Point existing : mapVisualization.getLocationPoints().values()) {
            if (existing.x == x && existing.y == y) {
                System.out.println("A location already exists at (" + x + ", " + y + ")!");
                return false;
            }
        }
        // Add to visualization
        mapVisualization.addLocation(location, new Point(x, y));

        // Add to graph (if your graph structure needs to know about locations)
        if (graph instanceof Graph) {
            ((Graph) graph).addLocation(location);
        }

        System.out.println("Added location: " + location);
        mapVisualization.refreshMap();
        return true;
    }
    
    private static boolean deleteLocation(Scanner scanner, InterfaceGraph graph, MapVisualization mapVisualization) {
        System.out.print("Enter location name to delete: ");
        String location = scanner.nextLine();
        
        // Check if location exists
        if (!mapVisualization.locationExists(location)) {
            System.out.println("Location '" + location + "' does not exist!");
            return false;
        }
        
        // Remove from visualization
        mapVisualization.removeLocation(location);
        
        // Remove from graph
        if (graph instanceof Graph) {
            ((Graph) graph).removeLocation(location);
        }
        
        System.out.println("Deleted location: " + location);
        mapVisualization.refreshMap();
        return true;
    }
    
    private static boolean addLine(Scanner scanner, InterfaceGraph graph, MapVisualization mapVisualization) {
        System.out.print("Enter start location: ");
        String start = scanner.nextLine();
        
        // Check if start location exists
        if (!mapVisualization.locationExists(start)) {
            System.out.println("Start location '" + start + "' does not exist!");
            return false;
        }
        
        System.out.print("Enter end location: ");
        String end = scanner.nextLine();
        
        // Check if end location exists
        if (!mapVisualization.locationExists(end)) {
            System.out.println("End location '" + end + "' does not exist!");
            return false;
        }
        
        // Prevent self-loop (start == end)
        if (start.equalsIgnoreCase(end)) {
            System.out.println("❌ Start and end locations cannot be the same!");
            return false;
        }
        
        int time = -1;
        while (true) {
            System.out.print("Enter time (minutes): ");
            String input = scanner.nextLine().trim();
            try {
                time = Integer.parseInt(input);
                if (time <= 0) {
                    System.out.println("Time must be a positive number!");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a whole number (e.g., 15).");
            }
        }

        double distance = -1;
        while (true) {
            System.out.print("Enter distance (km): ");
            String input = scanner.nextLine().trim();
            try {
                distance = Double.parseDouble(input);
                if (distance <= 0) {
                    System.out.println("Distance must be greater than 0!");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number (e.g., 12.5).");
            }
        }
        
        graph.addEdge(start, end, time, distance);
        mapVisualization.refreshMap();
        return true;
    }
    
    private static boolean deleteLine(Scanner scanner, InterfaceGraph graph, MapVisualization mapVisualization) {
        System.out.print("Enter start location: ");
        String start = scanner.nextLine();
        
        // Check if start location exists
        if (!mapVisualization.locationExists(start)) {
            System.out.println("Start location '" + start + "' does not exist!");
            return false;
        }
        
        System.out.print("Enter end location: ");
        String end = scanner.nextLine();
        
        // Check if end location exists
        if (!mapVisualization.locationExists(end)) {
            System.out.println("End location '" + end + "' does not exist!");
            return false;
        }
        
        // Remove only the directional edge from start to end
        boolean removed = graph.removeEdge(start, end);

        if (!removed) {
            System.out.println("No line exists from " + start + " to " + end);
            return false;
        }
        
        System.out.println("Deleted line from " + start + " to " + end);
        mapVisualization.refreshMap();
        return true;
    }
    
    private static void findShortestTimeTaken(Scanner scanner, InterfaceGraph graph, MapVisualization mapVisualization) {
        System.out.print("Enter start location: ");
        String start = scanner.nextLine();
        
        // Check if start location exists
        if (!mapVisualization.locationExists(start)) {
            System.out.println("Start location '" + start + "' does not exist!");
            return;
        }
        
        System.out.print("Enter destination: ");
        String goal = scanner.nextLine();
        
        // Check if goal location exists
        if (!mapVisualization.locationExists(goal)) {
            System.out.println("Destination '" + goal + "' does not exist!");
            return;
        }
        
        List<String> shortestPath = dijkstra(start, goal, graph.getAdjList());
        
        if (!shortestPath.isEmpty()) {
            mapVisualization.setHighlightedPath(shortestPath); // highlight
        }
    }
    
    private static void findLongestPath(Scanner scanner, InterfaceGraph graph, MapVisualization mapVisualization) {
        System.out.print("Enter start location: ");
        String start = scanner.nextLine();
        
        if (!mapVisualization.locationExists(start)) {
            System.out.println("Start location '" + start + "' does not exist!");
            return;
        }
        
        System.out.print("Enter destination: ");
        String goal = scanner.nextLine();
        
        if (!mapVisualization.locationExists(goal)) {
            System.out.println("Destination '" + goal + "' does not exist!");
            return;
        }
        
        if (graph instanceof Graph) {
            System.out.println("");
            List<String> longestPath = ((Graph) graph).dfs(start, goal);

            if (!longestPath.isEmpty()) {
                mapVisualization.setHighlightedPath(longestPath);
            }
        }
    }
    
    
    
    
    
 
    private static void findShortestPath(Scanner scanner, InterfaceGraph graph, MapVisualization mapVisualization) {
        System.out.print("Enter start location: ");
        String start = scanner.nextLine();

        if (!mapVisualization.locationExists(start)) {
            System.out.println("Start location '" + start + "' does not exist!");
            return;
        }

        System.out.print("Enter destination: ");
        String goal = scanner.nextLine();

        if (!mapVisualization.locationExists(goal)) {
            System.out.println("Destination '" + goal + "' does not exist!");
            return;
        }

        // Get adjacency list from graph
        Map<String, List<Edge>> adjList = graph.getAdjList();

        // Breadth-First Search (BFS)
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
            return;
        }

        // Reconstruct path
        List<String> path = new ArrayList<>();
        String current = goal;
        int totalTime = 0;
        double totalDistance = 0.0;

        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }

        Collections.reverse(path);

        // Calculate total time and distance
        for (int i = 0; i < path.size() - 1; i++) {
            String from = path.get(i);
            String to = path.get(i + 1);

            for (Edge edge : adjList.getOrDefault(from, new ArrayList<>())) {
                if (edge.destination.equals(to)) {
                    totalTime += edge.time;
                    totalDistance += edge.distance;
                    break;
                }
            }
        }

       
        System.out.println("Total distance: " + totalDistance + " km");
    

        mapVisualization.setHighlightedPath(path);  // Highlight on map
    }

    
     
}
    
    
    
    
    
    
    
    
 