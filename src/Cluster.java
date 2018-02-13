import java.util.ArrayList;
import java.util.List;

public class Cluster {
    private List<Point> points;
    private Point clusterCenter;
    private Point lastClusterCenter;

    public Cluster() {
        this.points = new ArrayList<>();
        this.clusterCenter = new Point(0, 0);
        this.lastClusterCenter = new Point(0, 0);
    }

    public Cluster( List<Point> points ) {
        this.points = points;
        this.computeClusterCenter();
    }

    public void computeClusterCenter() {
        if ( !this.points.isEmpty() ) {

            double minX = this.points.get(0).getX();
            double maxX = minX;
            double minY = this.points.get(0).getY();
            double maxY = minY;
            for (Point p : this.points) {
                // Check X
                if (p.getX() < minX) {
                    minX = p.getX();
                } else if (p.getX() > maxX) {
                    maxX = p.getX();
                }
                // Check Y
                if (p.getY() < minY) {
                    minY = p.getY();
                } else if (p.getY() > maxY) {
                    maxY = p.getY();
                }
            }
            // Save last state
            this.lastClusterCenter.setX(this.clusterCenter.getX());
            this.lastClusterCenter.setY(this.clusterCenter.getY());
            // Compute new state
            this.clusterCenter.setX((minX + maxX) / 2);
            this.clusterCenter.setY((minY + maxY) / 2);

        }
    }

    public void addPoint( Point p ) {
        this.points.add( p );
    }

    public List<Point> getPoints() {
        return this.points;
    }

    public Point getClusterCenter() {
        return this.clusterCenter;
    }
}
