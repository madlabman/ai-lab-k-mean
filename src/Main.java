public class Main {

    public static void main(String[] args) {
        ClusterClassifier cc = new ClusterClassifier(
                2,
                ClusterClassifier.generateRandomPointsSet( 22 ) );
        while ( true ) {
            cc.nextIteration();
        }
    }
}
