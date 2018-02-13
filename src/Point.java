public class Point {
    private Double x;
    private Double y;
    private int clusterClass = 0;

    public Point(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y) {
        this.x = (double)x;
        this.y = (double)y;
    }

    public Point(int x, int y, int clusterClass) {
        this(x, y);
        this.clusterClass = clusterClass;
    }

    public Double getX() {
        return this.x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public void setX(int x) {
        this.x = (double)x;
    }

    public Double getY() {
        return this.y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public void setY(int y) {
        this.y = (double)y;
    }

    public int getClusterClass() {
        return this.clusterClass;
    }

    public void setClusterClass(int c) {
        this.clusterClass = c;
    }

    public Double getLength(Point p) {
        return Math.sqrt(
                Math.pow((this.x - p.getX()), 2.0) +
                        Math.pow((this.y - p.getY()), 2.0));
    }
}
