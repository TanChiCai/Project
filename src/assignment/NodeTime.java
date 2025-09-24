/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment;

/**
 *
 * @author user
 */
public class NodeTime implements Comparable<NodeTime> {
    String node;
    int time;

    public NodeTime(String node, int time) {
        this.node = node;
        this.time = time;
    }

    @Override
    public int compareTo(NodeTime other) {
        return Integer.compare(this.time, other.time);
    }
}

