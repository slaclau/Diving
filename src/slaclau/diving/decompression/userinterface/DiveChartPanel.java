package slaclau.diving.decompression.userinterface;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;

@SuppressWarnings("serial")
public class DiveChartPanel extends JPanel implements ItemListener {
	private XYDataset mswDataset, secondaryDataset, tertiaryDataset;
	private XYSeries depthSeries, ceilingSeries, otuSeries, cnsSeries;
	private JFreeChart chart;
	private XYPlot plot = new XYPlot();
	private XYLineAndShapeRenderer mswRenderer, secondaryRenderer, tertiaryRenderer;
	private NumberAxis mswAxis, secondaryAxis, tertiaryAxis;
	
	public DiveChartPanel() {
		init();
	}
	
	private void init() {
		mswDataset = createMSWDataset();
		secondaryDataset = createSecondaryDataset();
		tertiaryDataset = createTertiaryDataset();
		
		plot.setDataset(0, mswDataset);
		plot.setDataset(1, secondaryDataset);
		plot.setDataset(2, tertiaryDataset);
		
		
		
		mswRenderer = new XYLineAndShapeRenderer(true, false);
		secondaryRenderer = new XYLineAndShapeRenderer(true, false);
		tertiaryRenderer = new XYLineAndShapeRenderer(true, false);
		
		secondaryRenderer.setSeriesVisible(0, false);
		tertiaryRenderer.setSeriesVisible(0, false);
		
		plot.setRenderer(0, mswRenderer);
		plot.setRenderer(1, secondaryRenderer);
		plot.setRenderer(2, tertiaryRenderer);
		
		
		
		mswAxis = new NumberAxis("Depth (msw)");
		secondaryAxis = new NumberAxis("OTUs");
		tertiaryAxis = new NumberAxis("CNS%");
		
		secondaryAxis.setVisible(false);
		tertiaryAxis.setVisible(false);
		
		tertiaryAxis.setRange(0, 100);
		
		plot.setRangeAxis(0, mswAxis);
	    plot.setRangeAxis(1, secondaryAxis);
	    plot.setRangeAxis(2, tertiaryAxis);

	    plot.getRangeAxis(0).setInverted(true);
	    plot.setDomainAxis(new NumberAxis("Time (min)"));
	    
	    plot.mapDatasetToRangeAxis(0, 0);
	    plot.mapDatasetToRangeAxis(1, 1);
	    plot.mapDatasetToRangeAxis(2, 2);
	    
	    chart = new JFreeChart("Dive chart", getFont(), plot, true);
		        
        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel);
	}
	
	private XYDataset createMSWDataset() {

        depthSeries = new XYSeries("Depth");
        ceilingSeries = new XYSeries("Ceiling");

        var dataset = new XYSeriesCollection();
        dataset.addSeries(depthSeries);
        dataset.addSeries(ceilingSeries);
        
        ceilingSeries.add(10, 10);
        ceilingSeries.add(20, 10);

        return dataset;
    }
	
	private XYDataset createSecondaryDataset() {
        otuSeries = new XYSeries("OTUs");

        var dataset = new XYSeriesCollection();
        dataset.addSeries(otuSeries);
        
        otuSeries.add(10, 10);
        otuSeries.add(5,5);

        return dataset;
    }
	
	private XYDataset createTertiaryDataset() {
        cnsSeries = new XYSeries("CNS%");

        var dataset = new XYSeriesCollection();
        dataset.addSeries(cnsSeries);
        
        cnsSeries.add(20, 10);
        cnsSeries.add(80,110);

        return dataset;
    }
  
    public void addDepthPoint(double depth, double time) {
    	depthSeries.add(time, depth);
    }

	@Override
	public void itemStateChanged(ItemEvent e) {
		JCheckBox source = (JCheckBox) e.getItem();
		String string = source.getText();		
		
		switch (string) {
		case "Show depth":
			if (source.isSelected()) mswRenderer.setSeriesVisible(0, true);
			else mswRenderer.setSeriesVisible(0, false);
			break;
		case "Show ceiling":
			if (source.isSelected()) mswRenderer.setSeriesVisible(1, true);
			else mswRenderer.setSeriesVisible(1, false);
			break;
		case "Show OTUs":
			if (source.isSelected()) {
				secondaryRenderer.setSeriesVisible(0, true);
				secondaryAxis.setVisible(true);
			} else {
				secondaryRenderer.setSeriesVisible(0, false);
				secondaryAxis.setVisible(false);
			}
			break;
		case "Show CNS%":
			if (source.isSelected()) {
				tertiaryRenderer.setSeriesVisible(0, true);
				tertiaryAxis.setVisible(true);
			} else {
				tertiaryRenderer.setSeriesVisible(0, false);
				tertiaryAxis.setVisible(false);
			}
			break;
		}
	}
}
