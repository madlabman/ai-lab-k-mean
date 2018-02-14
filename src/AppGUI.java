import com.bulenkov.darcula.DarculaLaf;
import org.jfree.chart.*;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class AppGUI extends JFrame {
    private static JFreeChart chart;
    private static ChartPanel chartPanel;
    private static ClusterClassifier clusterClassifier;
    private static JNumberTextField clustersCountInput;
    private final static Color darculaGREY = new Color(60, 63, 65);
    private final static Color darculaLIGHT = new Color(164, 164, 165);

    public AppGUI( String title ) {
        super( title );

        try {
            UIManager.setLookAndFeel(DarculaLaf.class.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Generate
        AppGUI.clusterClassifier = new ClusterClassifier(2,
                ClusterClassifier.generateRandomPointsSet( 100 ));
        // Draw UI
        setContentPane(AppGUI.initUI());
    }

    public static JPanel initUI() {
        // Add components
        //createDemoPanel(cl.getSeries()))
        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout());
        rootPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));

        JButton resetBtn = new JButton("Сбросить");
        resetBtn.addActionListener( e -> AppGUI.resetSet() );
        rootPanel.add(resetBtn, BorderLayout.CENTER);

        JButton nextBtn = new JButton("Следующая итерация");
        nextBtn.addActionListener( e -> AppGUI.paintNextIteration() );
        rootPanel.add(nextBtn, BorderLayout.EAST);

        AppGUI.clustersCountInput = new JNumberTextField();
        AppGUI.clustersCountInput.setColumns(2);
        rootPanel.add(clustersCountInput, BorderLayout.WEST);

        rootPanel.add(createDemoPanel( AppGUI.clusterClassifier.getClusterMap() ), BorderLayout.SOUTH);

        return rootPanel;
    }

    public static JPanel createDemoPanel( Map<Integer, Cluster> clusterMap ) {
        AppGUI.chart = createChart(createDataset( clusterMap ));
        AppGUI.chartPanel = new ChartPanel( chart );

        return AppGUI.chartPanel;
    }

    private static JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart_xy = ChartFactory.createScatterPlot(
                "",
                "Признак X",
                "Признак Y",
                dataset);

        // Font styling
        final String fontFamily = "Arial";
        final StandardChartTheme chartTheme = (StandardChartTheme)StandardChartTheme.createJFreeTheme();

        final Font oldExtraLargeFont = chartTheme.getExtraLargeFont();
        final Font oldLargeFont = chartTheme.getLargeFont();
        final Font oldRegularFont = chartTheme.getRegularFont();
        final Font oldSmallFont = chartTheme.getSmallFont();

        final Font extraLargeFont = new Font(fontFamily, oldExtraLargeFont.getStyle(), oldExtraLargeFont.getSize());
        final Font largeFont = new Font(fontFamily, oldLargeFont.getStyle(), oldLargeFont.getSize());
        final Font regularFont = new Font(fontFamily, oldRegularFont.getStyle(), oldRegularFont.getSize());
        final Font smallFont = new Font(fontFamily, oldSmallFont.getStyle(), oldSmallFont.getSize());

        chartTheme.setExtraLargeFont(extraLargeFont);
        chartTheme.setLargeFont(largeFont);
        chartTheme.setRegularFont(regularFont);
        chartTheme.setSmallFont(smallFont);

        chartTheme.setLegendBackgroundPaint(darculaGREY);
        chartTheme.setLegendItemPaint(darculaLIGHT);
        chartTheme.setChartBackgroundPaint(darculaGREY);
        chartTheme.setAxisLabelPaint(darculaLIGHT);
        chartTheme.setTickLabelPaint(darculaLIGHT);

        chartTheme.apply(chart_xy);
        chart_xy.setAntiAlias(true);
        chart_xy.getLegend().setFrame(BlockBorder.NONE);

        // Plot styling
        final XYPlot plot = chart_xy.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        // Remove lines visibility
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            renderer.setSeriesLinesVisible(i, false);
        }
        // Background
        plot.setBackgroundPaint(AppGUI.darculaGREY);
        plot.setRangeGridlinePaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.WHITE);

        plot.setRenderer(renderer);

        // Set range
        ValueAxis yAxis = plot.getRangeAxis();
        yAxis.setAutoRange(false);
        yAxis.setRange(0, 100);
        // Set domain
        ValueAxis xAxis = plot.getDomainAxis();
        xAxis.setAutoRange(false);
        xAxis.setRange(0, 100);

        // Plot clusters centers
        for ( Map.Entry<Integer, Cluster> me : AppGUI.clusterClassifier.getClusterMap().entrySet() ) {
            Point centerPoint = me.getValue().getClusterCenter();
            XYPointerAnnotation clusterPointer = new XYPointerAnnotation("Кластер " + me.getKey(), centerPoint.getX(), centerPoint.getY(), 0);
            clusterPointer.setLabelOffset(24);
            clusterPointer.setPaint(Color.WHITE);
            clusterPointer.setBackgroundPaint(Color.BLACK);
            clusterPointer.setArrowPaint(Color.WHITE);
            plot.addAnnotation(clusterPointer);
        }

        return chart_xy;
    }

    private static XYDataset createDataset( Map<Integer, Cluster> clusterMap ) {
        final XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries xySeria;
        for (Map.Entry<Integer, Cluster> s : clusterMap.entrySet()) {
            String name = "Кластер " + s.getKey();

            xySeria = new XYSeries( name );
            for (Point p : s.getValue().getPoints()) {
                xySeria.add(p.getX(), p.getY());
            }

            dataset.addSeries(xySeria);
        }

        return dataset;
    }

    private static void resetSet() {
        if ( AppGUI.clustersCountInput.getNumber() != 0 ) {
            AppGUI.clusterClassifier = new ClusterClassifier( AppGUI.clustersCountInput.getNumber(),
                    AppGUI.clusterClassifier.getAllPoints());
        } else {
            AppGUI.clusterClassifier.rewindClusters();
        }

        AppGUI.repaintUI();
    }

    private static void paintNextIteration() {
        AppGUI.clusterClassifier.nextIteration();
        AppGUI.repaintUI();
    }

    private static void repaintUI() {
        try {
            JFrame appFrame = (JFrame) AppGUI.getFrames()[0];
            // Update frame content
            appFrame.setContentPane(AppGUI.initUI());
            appFrame.setVisible(true);
            appFrame.pack();
            appFrame.repaint();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
