/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package assignment;
import javax.swing.SwingUtilities;
/**
 *
 * @author poher
 */
public class Assignment {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InterfaceGraph graph = new Graph();
        graph.addEdge("TanjungTokong", "TanjungBungah", 10, 2);

        graph.addEdge("Gurney", "TanjungTokong", 8, 1.2);
        graph.addEdge("Gurney", "TanjungBungah", 15, 3);

        graph.addEdge("Komtar", "Gurney", 15, 1.5);
        graph.addEdge("Komtar", "Jetty", 8, 1);
        graph.addEdge("Komtar", "BatuFerringhi", 35, 6);
        graph.addEdge("Komtar", "AyerItam", 15, 2.2);
        graph.addEdge("Komtar", "USM", 22, 4);

        graph.addEdge("Butterworth", "Gurney", 35, 5);

        graph.addEdge("Jetty", "Butterworth", 20, 2);
        graph.addEdge("BatuFerringhi", "TanjungBungah", 12, 2.2);
        
        graph.addEdge("AyerItam", "PenangHill", 10, 1);
        graph.addEdge("AyerItam", "BalikPulau",20, 2.6);

        graph.addEdge("PenangHill", "AyerItam", 10, 1);
        graph.addEdge("PenangHill", "BalikPulau", 30, 2);

        graph.addEdge("BalikPulau", "PenangHill", 30, 2);
        graph.addEdge("BalikPulau", "Komtar", 45, 8);
        graph.addEdge("USM", "Queensbay", 10, 2);
        graph.addEdge("Queensbay", "Airport", 15, 2);
        graph.addEdge("Airport", "BalikPulau", 20, 2.5);

     

        SwingUtilities.invokeLater(() -> {
            createAndShowMap(graph);
        });
        
    }
    
    private static void createAndShowMap(InterfaceGraph graph) {
        MapVisualization mapVisualization = new MapVisualization(graph);
        mapVisualization.setVisible(true);
        
        // Add window listener to start console menu AFTER window is opened
        mapVisualization.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                // Start console menu in a separate thread to avoid blocking EDT
                new Thread(() -> {
                    AddDeleteLocationLine.showMenu(graph, mapVisualization);
                }).start();
            }
            
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });
    }
    
}
