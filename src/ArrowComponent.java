import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JComponent;

public class ArrowComponent extends JComponent {
    private Point start;
    private Point end;
    private Color color;

    public ArrowComponent(Point start, Point end) {
        this.start = start;
        this.end = end;
        this.color = Color.BLACK;
        this.setBounds(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE); // setBounds(0, 0, max_width, max_height)
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (start != null && end != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);  // Arrow color
            g2d.setStroke(new BasicStroke(2));  // Line thickness

            // Define a dashed line pattern (10 pixels of dash, 10 pixels of space)
            float[] dashPattern = {10f, 10f};
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dashPattern, 0));

            // Dotted Line
            // float[] dotPattern = { 1f, 5f };  // Dots of size 1, with gaps of size 5
            // g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 30f, dotPattern, 0f));

            // Draw the main arrow line
            g2d.drawLine(start.x, start.y, end.x, end.y);

            // // Draw the arrowhead (simple triangle at the end)
            // int dx = end.x - start.x;
            // int dy = end.y - start.y;
            // double angle = Math.atan2(dy, dx);

            // // Points for the arrowhead
            // int arrowSize = 10;
            // int x1 = (int) (end.x - arrowSize * Math.cos(angle - Math.PI / 6));
            // int y1 = (int) (end.y - arrowSize * Math.sin(angle - Math.PI / 6));
            // int x2 = (int) (end.x - arrowSize * Math.cos(angle + Math.PI / 6));
            // int y2 = (int) (end.y - arrowSize * Math.sin(angle + Math.PI / 6));

            // Draw the arrowhead
            // g2d.fillPolygon(new int[] { end.x, x1, x2 }, new int[] { end.y, y1, y2 }, 3);
        }
    }
    // Getters and Setters for start and end points
    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }
}
