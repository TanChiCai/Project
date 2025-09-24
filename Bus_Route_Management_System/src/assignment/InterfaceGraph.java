/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment;

/**
 *
 * @author poher
 */

import java.util.List;
import java.util.Map;

public interface InterfaceGraph {
    void addEdge(String src, String dest, int time, double distance);
    boolean removeEdge(String start, String end);
    List<List<String>> getAllPaths();
    List<String> dfs(String startNode, String goal);
    void dijkstra(String startNode, String goal);
    void dfsHelper(String node, String goal, List<String> path, int totalTime);
    void printGraph();
    Map<String, List<Edge>> getAdjList();
}
