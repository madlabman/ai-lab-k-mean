import java.util.*;

public class ClusterClassifier {
    private int predefinedClustersCount = 0;
    private Map<Integer, Cluster> clusterMap;

    public ClusterClassifier( int predefinedClustersCount, List<Point> points ) {
        this.predefinedClustersCount = predefinedClustersCount;
        this.clusterMap = this.initClusterHashMap();
        this.nextIteration( points ); // Initial distribute
    }

    public Map<Integer, Cluster> getClusterMap() {
        return this.clusterMap;
    }

    public void nextIteration( List<Point> points ) {
        this.recomputePointsClusters( points );
    }

    public void nextIteration() {
        this.recomputePointsClusters( this.getAllPoints() );
    }

    private void recomputePointsClusters( List<Point> points ) {
        Map<Integer, Cluster> clusterHashMap = initClusterHashMap();
        // Distribute points
        for ( Point p : points ) {
            int clusterIdx = 0;
            double minLength = p.getLength( this.clusterMap.get( clusterIdx ).getClusterCenter() );
            for ( int i = 1; i < this.predefinedClustersCount; i++ ) {
                if ( p.getLength( this.clusterMap.get( i ).getClusterCenter() ) < minLength ) {
                    clusterIdx = i;
                }
            }
            // Save point
            clusterHashMap.get( clusterIdx ).addPoint( p );
        }
        // Recompute centers
        for ( Map.Entry<Integer, Cluster> me : clusterHashMap.entrySet() ) {
            me.getValue().computeClusterCenter();
        }
        // Save state
        this.clusterMap = clusterHashMap;
    }

    private Map<Integer, Cluster> initClusterHashMap() {
        HashMap<Integer, Cluster> clusterHashMap = new HashMap<>( this.predefinedClustersCount );
        // Initialize clusters
        for ( int i = 0; i < this.predefinedClustersCount; i++ ) {
            clusterHashMap.put( i, new Cluster() );
        }

        return clusterHashMap;
    }

    public void setPredefinedClustersCount( int count ) {
        this.predefinedClustersCount = count;
    }

    public void rewindClusters() {
        List<Point> points = this.getAllPoints();
        this.clusterMap = this.initClusterHashMap();
        // Reinitialize points
        this.recomputePointsClusters( points );
    }

    protected List<Point> getAllPoints() {
        ArrayList<Point> points = new ArrayList<>(0);
        for ( Map.Entry<Integer, Cluster> me : this.clusterMap.entrySet() ) {
            points.addAll(me.getValue().getPoints());
        }

        return points;
    }

    public static List<Point> generateRandomPointsSet( int pointsCount ) {
        ArrayList<Point> points = new ArrayList<>( pointsCount );
        final Random random = new Random();
        for ( int i = 0; i < pointsCount; i++ ) {
            points.add( new Point( random.nextInt(100), random.nextInt(100) ) );
        }

        return points;
    }
}
