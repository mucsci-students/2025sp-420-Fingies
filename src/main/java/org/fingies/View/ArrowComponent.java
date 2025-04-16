package org.fingies.View;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Arc2D;

import javax.swing.JComponent;

import org.fingies.Model.RelationshipType;

public class ArrowComponent extends JComponent {
    private Point start;
    private Point end;
    private Color color;
    private RelationshipType relation;
    private int endPanelWidth;
    private int endPanelHeight;
    private boolean isSelfReferencing;

    public ArrowComponent(Point start, Point end, RelationshipType relation, int endPanelWidth, int endPanelHeight, boolean isSelfReferencing) {
        this.start = start;
        this.end = end;
        this.color = Color.BLACK;
        this.relation = relation;
        this.endPanelWidth = endPanelWidth;
        this.endPanelHeight = endPanelHeight;
        this.isSelfReferencing = isSelfReferencing;
        this.setBounds(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE); // setBounds(0, 0, max_width, max_height)
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (start != null && end != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);  // Arrow color
                
            if (relation == RelationshipType.Realization)
            {
                // Dashed + Hollow Arrow
                float[] dashPattern = {10f, 10f};
                g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dashPattern, 0));
            }
            else
            {
                // Solid
                g2d.setStroke(new BasicStroke(2));  // Line thickness
            }

            // Draw the arrow based on the self-referencing flag
            if (isSelfReferencing) 
            {
                drawSelfReferencingArrow(g2d);
            } 
            else 
            {
                drawRegularArrow(g2d);
            }
        }
    }

    // Helper method to draw a regular arrow
    private void drawRegularArrow(Graphics2D g2d) {
        // Calculate the angle of the line between start and end
        int dx = end.x - start.x;
        int dy = end.y - start.y;
        double angle = Math.atan2(dy, dx);

        Point offset = new Point((int)(8 * Math.sin(angle)), -(int)(8 * Math.cos(angle)));

        // The offset start & end points:
        Point newStart = new Point(start.x + offset.x, start.y + offset.y);
        Point newEnd = new Point(end.x + offset.x, end.y + offset.y);

        // Calculate the distance from the endpoint to the border of the JPanel
        double borderDistance = calculateBorderDistance(angle, endPanelWidth, endPanelHeight, offset);

        int distanceFromBorder = -10;
        // Adjust the distance to be 10 pixels from the border
        if (relation == RelationshipType.Inheritance || relation == RelationshipType.Realization) {
            distanceFromBorder += 10;
        }
        // Fixed distance from the border
        int distanceFromEnd = (int) (borderDistance - distanceFromBorder);

        // Calculate the position for the shape at the adjusted distance from the end point
        int shapeX = (int) (newEnd.x - distanceFromEnd * Math.cos(angle));
        int shapeY = (int) (newEnd.y - distanceFromEnd * Math.sin(angle));

        // Draw the main arrow line
        g2d.setColor(Color.BLACK);  // Reset to the original color
        g2d.drawLine(newStart.x, newStart.y, newEnd.x, newEnd.y);

        // Draw the shape based on the relationship type
        drawShape(g2d, shapeX, shapeY, angle);
    }


        private void drawSelfReferencingArrow(Graphics2D g2d) {
            // Define the fixed size of the loop
            int loopSize = 50; // Fixed size of the loop (adjust as needed)

            // Define the offset from the top and left edges of the panel
            int offset = - 10; // Fixed offset in pixels (adjust as needed)

            // Calculate the center of the loop
            int centerX = end.x + endPanelWidth / 2 - offset; // Center X at the right edge of the panel
            int centerY = end.y - endPanelHeight / 2 - offset; // Center Y at the top edge of the panel

            // Draw the loop as an arc starting from the top-right corner
            Arc2D arc = new Arc2D.Double(
                centerX - loopSize, centerY - loopSize, // Top-left corner of the bounding rectangle
                2 * loopSize, 2 * loopSize,           // Width and height of the bounding rectangle
                0, 360,                               // Start angle and extent (270 degrees for a loop)
                Arc2D.OPEN                            // Open arc type
            );
            g2d.draw(arc);
    
            // Calculate the angle at the end of the loop
            double angle = Math.toRadians(270); // End of the arc
    
            // Calculate the position for the shape at the end of the loop
            int shapeX = (int) (centerX + loopSize * Math.cos(angle));
            int shapeY = (int) (centerY + loopSize * Math.sin(angle));

            // Draw the shape based on the relationship type
            drawShape (g2d, shapeX, shapeY, angle);
        }

    private void drawShape(Graphics2D g2d, int x, int y, double angle) {
        if (relation == RelationshipType.Aggregation || relation == RelationshipType.Composition) 
        {
            drawDiamond(g2d, x, y, angle);
        }
        else
        {
            g2d.setStroke(new BasicStroke(2));  // Line thickness
            if (!isSelfReferencing)
                drawArrowhead(g2d, x, y, angle);
            else
            {
                drawArrowhead(g2d, x, y - 13, angle);
            }
        }
    }

    // Helper method to calculate the distance from the endpoint to the border of the JPanel
    private double calculateBorderDistance(double angle, int panelWidth, int panelHeight, Point offset) {
        // Calculate the intersection point of the line with the panel's border
        double halfWidth = panelWidth / 2.0;
        double halfHeight = panelHeight / 2.0;

        // Calculate the distance to the border along the line's direction
        double borderDistance = Math.min(
            Math.abs(halfWidth / Math.cos(angle) + offset.x),
            Math.abs(halfHeight / Math.sin(angle) + offset.y)
        );
        return borderDistance;
    }

    // Helper method to draw a diamond
    private void drawDiamond(Graphics2D g2d, int x, int y, double angle) {
        int diamondSize = 10;  // Size of the diamond
        int[] xPoints = {x - diamondSize, x, x + diamondSize, x};
        int[] yPoints = {y, y - diamondSize, y, y + diamondSize};

        // Add Rotation
        for (int i = 0; i < 4; i++) {
            int originalX = xPoints[i];
            int originalY = yPoints[i];
            
            // Apply rotation formula
            xPoints[i] = (int) (x + (originalX - x) * Math.cos(angle) - (originalY - y) * Math.sin(angle));
            yPoints[i] = (int) (y + (originalX - x) * Math.sin(angle) + (originalY - y) * Math.cos(angle));
        }
        
        if (relation == RelationshipType.Aggregation) {
        	// Draw the diamond inside (filled)
        	g2d.setColor(Color.WHITE);
            g2d.fillPolygon(xPoints, yPoints, 4);
            // Draw the diamond outline (unfilled)
        	g2d.setColor(Color.BLACK);
            g2d.drawPolygon(xPoints, yPoints, 4);
        } else {
        	// Draw the diamond (filled)
        	g2d.setColor(Color.BLACK);
            g2d.fillPolygon(xPoints, yPoints, 4);
        }
    }

    // Helper method to draw an arrowhead
    private void drawArrowhead(Graphics2D g2d, int x, int y, double angle) {
        int arrowSize = 20;  // Size of the arrowhead
        int x1 = (int) (x - arrowSize * Math.cos(angle - Math.PI / 6));
        int y1 = (int) (y - arrowSize * Math.sin(angle - Math.PI / 6));
        int x2 = (int) (x - arrowSize * Math.cos(angle + Math.PI / 6));
        int y2 = (int) (y - arrowSize * Math.sin(angle + Math.PI / 6));

        // Draw the arrowhead inside (filled)
    	g2d.setColor(Color.WHITE);
        g2d.fillPolygon(new int[]{x, x1, x2}, new int[]{y, y1, y2}, 3);
        // Draw the arrowhead outline (unfilled)
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(new int[]{x, x1, x2}, new int[]{y, y1, y2}, 3);
    }

    // Getters and Setters for start and end points
    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }
}
