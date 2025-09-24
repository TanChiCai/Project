/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment;

/**
 *
 * @author user
 */
import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.util.*;
import java.util.List;

public class MapVisualization extends JFrame {
    private InterfaceGraph graph;
    private Map<String, Point> locationPoints;
    private static final int CURVE_OFFSET = 40;
    private List<String> highlightedPath = new ArrayList<>();

    public MapVisualization(InterfaceGraph graph) {
        this.graph = graph;
        this.locationPoints = new HashMap<>();
        
        setTitle("Penang Location Map");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initializeLocationPoints();
        
        JPanel mapPanel = new MapPanel();

        add(mapPanel);
        setVisible(true);
    }
    
    public InterfaceGraph getGraph() {
        return graph;
    }
    
    private void initializeLocationPoints() {
        // Manually position the locations for visualization
        locationPoints.put("TanjungTokong", new Point(450, 50));
        locationPoints.put("TanjungBungah", new Point(750, 100));
        locationPoints.put("Gurney", new Point(450, 150));
        locationPoints.put("Komtar", new Point(450, 250));
        locationPoints.put("Jetty", new Point(150, 250));
        locationPoints.put("BatuFerringhi", new Point(1050, 150));
        locationPoints.put("Butterworth", new Point(150, 150));
        locationPoints.put("AyerItam", new Point(750, 250));
        locationPoints.put("PenangHill", new Point(1050, 250));
        locationPoints.put("BalikPulau", new Point(750, 350));
        locationPoints.put("USM", new Point(450, 350));
        locationPoints.put("Queensbay", new Point(450, 450));
        locationPoints.put("Airport", new Point(450, 550));
    }
    
    public void addLocation(String name, Point point) {
        locationPoints.put(name, point);
        repaint(); // Refresh the map to show the new location
    }
    
    public Map<String, Point> getLocationPoints() {
        return locationPoints;
    }
    
    public void removeLocation(String name) {
        locationPoints.remove(name);
        repaint(); // Refresh the map
    }
    
    public boolean locationExists(String name) {
        return locationPoints.containsKey(name);
    }
    
    public void setHighlightedPath(List<String> path) {
        this.highlightedPath = path;
        repaint();
    }
    
    class MapPanel extends JPanel {
        private static final int NODE_RADIUS = 20;
        private static final int ARROW_SIZE = 8;
        
        public MapPanel() {
            setBackground(Color.WHITE);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw connections
            g2d.setColor(Color.LIGHT_GRAY);
            
            for (int x = 50; x <= getWidth(); x += 50) {
                g2d.drawLine(x, 0, x, getHeight());   // vertical lines
                g2d.drawString(String.valueOf(x), x + 2, 12); // x-axis labels at top
            }
            
            for (int y = 50; y <= getHeight(); y += 50) {
                g2d.drawLine(0, y, getWidth(), y);    // horizontal lines
                g2d.drawString(String.valueOf(y), 2, y - 2); // y-axis labels at left
            }
            
            BasicStroke stroke = new BasicStroke(2f);
            g2d.setStroke(stroke);
            
            // Track edges between nodes to detect multiple connections
            Map<String, Integer> edgeCount = new HashMap<>();
            
            // Use adjacency list to draw edges
            Map<String, List<Edge>> adjList = graph.getAdjList();
            for (String node : adjList.keySet()) {
                Point p1 = locationPoints.get(node);
                if (p1 == null) continue;
                
                for (Edge edge : adjList.get(node)) {
                    Point p2 = locationPoints.get(edge.destination);
                    if (p2 != null) {
                        // Create a unique key for this edge (sorted to handle both directions)
                        String edgeKey = generateEdgeKey(node, edge.destination);
                        
                        // Count how many times we've seen this edge
                        int count = edgeCount.getOrDefault(edgeKey, 0);
                        edgeCount.put(edgeKey, count + 1);
                        
                        // Draw the line - straight for first occurrence, curved for second
                        if (count == 0) {
                            // First edge - draw straight line
                            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                            drawArrow(g2d, p1.x, p1.y, p2.x, p2.y);
                        } else {
                            // Second edge - draw curved line
                            drawCurvedLine(g2d, p1, p2, count);
                        }
                        
                        // Draw distance and time
                        int midX, midY;
                        if (count == 0) {
                            // For straight line, place text in the middle
                            midX = (p1.x + p2.x) / 2 + 5;
                            midY = (p1.y + p2.y) / 2 + 10;
                            
                        } else {
                            // For curved line, calculate the midpoint of the curve
                            Point midPoint = calculateCurveMidpoint(p1, p2, count);
                            midX = midPoint.x + 5;
                            midY = midPoint.y + (17 * count);
                        }
                        
                        g2d.setColor(Color.BLUE);
                        g2d.drawString(edge.time + "min, " + edge.distance + "km", midX, midY);
                        g2d.setColor(Color.LIGHT_GRAY);
                    }
                }
            }
            
            if (highlightedPath != null && highlightedPath.size() > 1) {
                g2d.setColor(Color.RED);
                g2d.setStroke(new BasicStroke(4f)); // thicker line

                // First pass: count edges in the highlighted path
                Map<String, Integer> pathEdgeCount = new HashMap<>();
                for (int i = 0; i < highlightedPath.size() - 1; i++) {
                    String from = highlightedPath.get(i);
                    String to = highlightedPath.get(i + 1);
                    String edgeKey = generateEdgeKey(from, to);
                    pathEdgeCount.put(edgeKey, pathEdgeCount.getOrDefault(edgeKey, 0) + 1);
                }

                // Second pass: draw with proper counting
                Map<String, Integer> drawnCount = new HashMap<>();
                for (int i = 0; i < highlightedPath.size() - 1; i++) {
                    String from = highlightedPath.get(i);
                    String to = highlightedPath.get(i + 1);

                    Point p1 = locationPoints.get(from);
                    Point p2 = locationPoints.get(to);

                    if (p1 != null && p2 != null) {
                        String edgeKey = generateEdgeKey(from, to);
                        int count = drawnCount.getOrDefault(edgeKey, 0);
                        drawnCount.put(edgeKey, count + 1);

                        // Always draw straight lines for highlighted path for simplicity
                        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                        drawArrow(g2d, p1.x, p1.y, p2.x, p2.y);
                    }
                }
            }
            
            // Draw nodes
            for (Map.Entry<String, Point> entry : locationPoints.entrySet()) {
                Point p = entry.getValue();
                String node = entry.getKey();
                
                // Draw circle
                g2d.setColor(Color.BLACK);
                g2d.fillOval(p.x - NODE_RADIUS, p.y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
                
                // Draw border
                g2d.setColor(Color.BLACK);
                g2d.drawOval(p.x - NODE_RADIUS, p.y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
                
                // Draw label
                FontMetrics metrics = g2d.getFontMetrics();
                int labelWidth = metrics.stringWidth(node);
                g2d.setColor(Color.BLACK);
                g2d.drawString(node, p.x - labelWidth/2, p.y - NODE_RADIUS - 10);
            }
        }
        
        private String generateEdgeKey(String node1, String node2) {
            // Create a consistent key regardless of edge direction
            if (node1.compareTo(node2) < 0) {
                return node1 + ":" + node2;
            } else {
                return node2 + ":" + node1;
            }
        }
        
        private void drawCurvedLine(Graphics2D g2d, Point p1, Point p2, int curveIndex) {
            // Calculate control point for the quadratic curve
            int dx = p2.x - p1.x;
            int dy = p2.y - p1.y;
            
            // Calculate perpendicular direction (rotate 90 degrees)
            int perpx = -dy;
            int perpy = dx;
            
            // Normalize perpendicular vector
            double length = Math.sqrt(perpx * perpx + perpy * perpy);
            double normx = perpx / length;
            double normy = perpy / length;
            
            // Set curve offset based on curveIndex (alternate sides for multiple curves)
            int curveOffset = 30 * (curveIndex % 2 == 0 ? 1 : -1);
            
            // Calculate control point
            int X = (p1.x + p2.x) / 2 + (int)(normx * curveOffset);
            int Y = (p1.y + p2.y) / 2 + (int)(normy * curveOffset);
            
            // Draw quadratic curve
            g2d.draw(new QuadCurve2D.Float(p1.x, p1.y, X, Y, p2.x, p2.y));
            
            // Draw arrow at the end of the curve
            drawArrowOnCurve(g2d, p1, p2, new Point(X, Y));
        }
        
        private void drawArrowOnCurve(Graphics2D g2d, Point p1, Point p2, Point ctrl) {
            // Calculate the end point of the curve (just before the destination node)
            QuadCurve2D curve = new QuadCurve2D.Float(p1.x, p1.y, ctrl.x, ctrl.y, p2.x, p2.y);
            
            // Use a small t value close to 1 to get the end of the curve
            double t = 0.95;
            double endX = Math.pow(1 - t, 2) * p1.x + 2 * (1 - t) * t * ctrl.x + Math.pow(t, 2) * p2.x;
            double endY = Math.pow(1 - t, 2) * p1.y + 2 * (1 - t) * t * ctrl.y + Math.pow(t, 2) * p2.y;
            
            // Calculate the derivative at t to get the tangent direction
            double dx = 2 * (1 - t) * (ctrl.x - p1.x) + 2 * t * (p2.x - ctrl.x);
            double dy = 2 * (1 - t) * (ctrl.y - p1.y) + 2 * t * (p2.y - ctrl.y);
            
            // Draw the arrow head
            double angle = Math.atan2(dy, dx);
            
            g2d.setColor(Color.BLACK);
            g2d.drawLine((int)endX, (int)endY, 
                (int) (endX - ARROW_SIZE * Math.cos(angle - Math.PI/6)), 
                (int) (endY - ARROW_SIZE * Math.sin(angle - Math.PI/6)));
            
            g2d.drawLine((int)endX, (int)endY, 
                (int) (endX - ARROW_SIZE * Math.cos(angle + Math.PI/6)), 
                (int) (endY - ARROW_SIZE * Math.sin(angle + Math.PI/6)));
        }
        
        private Point calculateCurveMidpoint(Point p1, Point p2, int curveIndex) {
            // Calculate control point for the quadratic curve (same as in drawCurvedLine)
            int dx = p2.x - p1.x;
            int dy = p2.y - p1.y;
            
            int perpx = -dy;
            int perpy = dx;
            
            double length = Math.sqrt(perpx * perpx + perpy * perpy);
            double normx = perpx / length;
            double normy = perpy / length;
            
            int curveOffset = CURVE_OFFSET * (curveIndex % 2 == 0 ? 1 : -1);
            
            int X = (p1.x + p2.x) / 2 + (int)(normx * curveOffset);
            int Y = (p1.y + p2.y) / 2 + (int)(normy * curveOffset);
            
            // Calculate midpoint of the curve (t = 0.5)
            double midX = Math.pow(0.5, 2) * p1.x + 2 * 0.5 * (1 - 0.5) * X + Math.pow(1 - 0.5, 2) * p2.x;
            double midY = Math.pow(0.5, 2) * p1.y + 2 * 0.5 * (1 - 0.5) * Y + Math.pow(1 - 0.5, 2) * p2.y;
            
            return new Point((int)midX, (int)midY);
        }
        
        private void drawArrow(Graphics2D g2d, int x1, int y1, int x2, int y2) {
            // Calculate the angle of the line
            double angle = Math.atan2(y2 - y1, x2 - x1);
            
            // Calculate the point where the arrow should be drawn (closer to the destination)
            int endX = (int) (x2 - NODE_RADIUS * Math.cos(angle));
            int endY = (int) (y2 - NODE_RADIUS * Math.sin(angle));
            
            // Draw the arrow lines
            g2d.setColor(Color.BLACK);
            g2d.drawLine(endX, endY, 
                (int) (endX - ARROW_SIZE * Math.cos(angle - Math.PI/6)), 
                (int) (endY - ARROW_SIZE * Math.sin(angle - Math.PI/6)));
            
            g2d.drawLine(endX, endY, 
                (int) (endX - ARROW_SIZE * Math.cos(angle + Math.PI/6)), 
                (int) (endY - ARROW_SIZE * Math.sin(angle + Math.PI/6)));
            
            g2d.setColor(Color.RED);
        }
    }
    
    public void refreshMap() {
        // Repaint the map to reflect changes
        repaint();
        revalidate();
    }
}