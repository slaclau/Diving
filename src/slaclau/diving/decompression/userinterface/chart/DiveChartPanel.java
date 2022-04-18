package slaclau.diving.decompression.userinterface.chart;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import slaclau.diving.decompression.model.ModelledDive;
import slaclau.diving.decompression.model.buhlmann.BuhlmannModelWithGF;
import slaclau.diving.dive.DiveTimeOutOfBoundsException;
import slaclau.diving.gas.GasAtDepth;

import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

@SuppressWarnings("serial")
public class DiveChartPanel extends JPanel implements ItemListener {
	private ModelledDive dive;
	
	private XYSeriesCollection mswDataset, otuDataset, percentDataset, barDataset;
	private XYSeries depthSeries, ceilingSeries, 
	otuSeries, 
	cnsSeries, gfSeries, gradientSeries, surfaceGradientSeries,
	ppO2Series, ppN2Series, ppHeSeries;
	private JFreeChart chart;
	private XYPlot plot = new XYPlot();
	private XYLineAndShapeRenderer mswRenderer, otuRenderer, percentRenderer, barRenderer;
	private NumberAxis mswAxis, otuAxis, percentAxis, barAxis;
	
	private boolean graphDepth = true, graphCeiling = true, 
	graphOTUs, 
	graphCNS, graphGF, graphCurrentGradient, graphSurfaceGradient, 
	graphPPO2, graphPPN2, graphPPHe;
	
	private static final String DEPTH_SERIES_NAME = "Depth";
	private static final String CEILING_SERIES_NAME = "Ceiling";
	private static final String OTU_SERIES_NAME = "CPTD";
	private static final String CNS_SERIES_NAME = "CNS toxicity";
	private static final String GF_SERIES_NAME = "GF";
	private static final String GRADIENT_SERIES_NAME = "Current gradient";
	private static final String SURFACE_GRADIENT_SERIES_NAME = "Surface gradient";
	private static final String PPO2_SERIES_NAME = "Oxygen partial pressure";
	private static final String PPN2_SERIES_NAME = "Nitrogen partial pressure";
	private static final String PPHE_SERIES_NAME = "Helium partial pressure";
	
	
	private ChartPanel chartPanel;
	
	private static final double PLOT_INTERVAL = .1;
	
	private static double getPlotInterval() {
		return PLOT_INTERVAL;
	}
	
	public DiveChartPanel() {
		init();
	}
	
	private void init() {
		setLayout(new BorderLayout());
		
		depthSeries = new XYSeries(DEPTH_SERIES_NAME);
		ceilingSeries = new XYSeries(CEILING_SERIES_NAME);
				
		otuSeries = new XYSeries(OTU_SERIES_NAME);
		
		cnsSeries = new XYSeries(CNS_SERIES_NAME);
		gfSeries = new XYSeries(GF_SERIES_NAME);
		gradientSeries = new XYSeries(GRADIENT_SERIES_NAME);
		surfaceGradientSeries = new XYSeries(SURFACE_GRADIENT_SERIES_NAME);
		
		ppO2Series = new XYSeries(PPO2_SERIES_NAME);
		ppN2Series = new XYSeries(PPN2_SERIES_NAME);
		ppHeSeries = new XYSeries(PPHE_SERIES_NAME);
		
		createMSWDataset();
		createOTUDataset();
		createPercentDataset();
		createBarDataset();
		
		mswRenderer = new XYLineAndShapeRenderer(true, false);
		mswRenderer.setSeriesStroke(0, new BasicStroke(2.0f) );
		mswRenderer.setSeriesPaint(0, Color.BLACK );
		mswRenderer.setSeriesStroke(1, new BasicStroke(2.0f) );
		mswRenderer.setSeriesPaint(1, Color.RED );
		
		otuRenderer = new XYLineAndShapeRenderer(true, false);
		otuRenderer.setSeriesStroke(0, new BasicStroke(2.0f) );
		
		percentRenderer = new XYLineAndShapeRenderer(true, false);
		percentRenderer.setSeriesStroke(0, new BasicStroke(2.0f) );
		percentRenderer.setSeriesStroke(1, new BasicStroke(2.0f) );
		percentRenderer.setSeriesStroke(2, new BasicStroke(2.0f) );
		percentRenderer.setSeriesStroke(3, new BasicStroke(2.0f) );

		barRenderer = new XYLineAndShapeRenderer(true, false);
		barRenderer.setSeriesStroke(0, new BasicStroke(1.5f) );
		barRenderer.setSeriesPaint(0, Color.GRAY );
		barRenderer.setSeriesStroke(1, new BasicStroke(1.5f) );
		barRenderer.setSeriesPaint(1, Color.BLACK );
		barRenderer.setSeriesStroke(2, new BasicStroke(1.5f) );
		barRenderer.setSeriesPaint(2, new Color(128, 64, 0) );

		plot.setRenderer(0, mswRenderer);
		plot.setRenderer(1, otuRenderer);
		plot.setRenderer(2, percentRenderer);
		plot.setRenderer(3, barRenderer);
		
		mswAxis = new NumberAxis("Depth (msw)");
		otuAxis = new NumberAxis("OTUs");
		percentAxis = new NumberAxis("%");
		barAxis = new NumberAxis("bar");
				
		plot.setRangeAxis(0, mswAxis);
	    plot.setRangeAxis(1, otuAxis);
	    plot.setRangeAxis(2, percentAxis);
	    plot.setRangeAxis(3, barAxis);

	    plot.getRangeAxis(0).setInverted(true);
	    plot.setDomainAxis(new NumberAxis("Time (min)"));
	    
	    plot.mapDatasetToRangeAxis(0, 0);
	    plot.mapDatasetToRangeAxis(1, 1);
	    plot.mapDatasetToRangeAxis(2, 2);
	    plot.mapDatasetToRangeAxis(3, 3);
	    
	    chart = new JFreeChart("Dive chart", getFont(), plot, true);
		        
        chartPanel = new ChartPanel(chart);
        add(chartPanel);
	}
	
	private void update() {
		createMSWDataset();
		createOTUDataset();
		createPercentDataset();
		createBarDataset();
		
		chart = new JFreeChart("Dive chart", getFont(), plot, true);
        
        chartPanel = new ChartPanel(chart);
        add(chartPanel);
	}
	
	private void createMSWDataset() {
        mswDataset = new XYSeriesCollection();
        mswDataset.addSeries(depthSeries);
        mswDataset.addSeries(ceilingSeries);
        
        plot.setDataset(0, mswDataset);
    }
	
	private void createOTUDataset() {
		otuDataset = new XYSeriesCollection(otuSeries);
		
		plot.setDataset(1, otuDataset);
	}
	
	private void createPercentDataset() {
        percentDataset = new XYSeriesCollection();
        percentDataset.addSeries(cnsSeries);
        percentDataset.addSeries(gfSeries);
        percentDataset.addSeries(gradientSeries);
        percentDataset.addSeries(surfaceGradientSeries);
        
        plot.setDataset(2, percentDataset);
    }
	
	private void createBarDataset() {
		barDataset = new XYSeriesCollection();
		barDataset.addSeries(ppO2Series);
		barDataset.addSeries(ppN2Series);
		barDataset.addSeries(ppHeSeries);
		
		plot.setDataset(3, barDataset);
	}
	
    public void plotDive(ModelledDive dive) {
    	this.dive = dive;
    	Runnable r = () -> {

    		depthSeries = new XYSeries(DEPTH_SERIES_NAME);
    		ceilingSeries = new XYSeries(CEILING_SERIES_NAME);
    		
    		otuSeries = new XYSeries(OTU_SERIES_NAME);
    		
    		cnsSeries = new XYSeries(CNS_SERIES_NAME);
    		gfSeries = new XYSeries(GF_SERIES_NAME);
    		gradientSeries = new XYSeries(GRADIENT_SERIES_NAME);
    		surfaceGradientSeries = new XYSeries(SURFACE_GRADIENT_SERIES_NAME);

    		ppO2Series = new XYSeries(PPO2_SERIES_NAME);
    		ppN2Series = new XYSeries(PPN2_SERIES_NAME);
    		ppHeSeries = new XYSeries(PPHE_SERIES_NAME);

    		double duration = dive.getTime();
    		double time = 0;
    		try {
    			BuhlmannModelWithGF followDive = (BuhlmannModelWithGF) dive.cloneAndReset();
    			followDive.setFirstStop( ( (BuhlmannModelWithGF) dive).getFirstStop() );

    			GasAtDepth divePoint;
    			double depth;

    			while ( time < duration ) {
    				divePoint = dive.getPoint(time);
    				depth = divePoint.getDepth();
    				if (graphDepth) depthSeries.add(time, depth);

    				followDive.goTo(time, divePoint);
    				if (graphCeiling) ceilingSeries.add(time, followDive.getActualCeiling());
    				
    				if (graphOTUs) otuSeries.add(time, followDive.getOTUs());

    				if (graphGF) gfSeries.add(time, 100 * followDive.getGradientFactor());
    				if (graphCNS) cnsSeries.add(time, followDive.getCNS());
    				if (graphCurrentGradient) gradientSeries.add(time, 100 * followDive.getCurrentGradient());
    				if (graphSurfaceGradient) surfaceGradientSeries.add(time, 100 * followDive.getSurfaceGradient());

    				if (graphPPO2) ppO2Series.add(time, followDive.getCurrentPoint().getOxygenPartialPressure());
    				if (graphPPN2) ppN2Series.add(time, followDive.getCurrentPoint().getNitrogenPartialPressure());
    				if (graphPPHe) ppHeSeries.add(time, followDive.getCurrentPoint().getHeliumPartialPressure());
    				time += getPlotInterval();
    			}
    		} catch (DiveTimeOutOfBoundsException e) {
    			e.printStackTrace();
    		}
    		update();
    	};

    	Thread t = new Thread(r);
    	t.start();
    }

	@Override
	public void itemStateChanged(ItemEvent e) {
		JCheckBox source = (JCheckBox) e.getItem();
		String string = source.getText();		
		
		switch (string) {
		case "Show depth":
			if (source.isSelected()) graphDepth = true;
			else graphDepth = false;
			break;
		case "Show ceiling":
			if (source.isSelected()) graphCeiling = true;
			else graphCeiling = false;
			break;
		case "Show OTUs":
			if (source.isSelected()) graphOTUs = true;
			else graphOTUs = false;
			break;
		case "Show CNS load":
			if (source.isSelected()) graphCNS = true;
			else graphCNS = false;
			break;
		case "Show GF":
			if (source.isSelected()) graphGF = true;
			else graphGF = false;
			break;
		case "Show current gradient":
			if (source.isSelected()) graphCurrentGradient = true;
			else graphCurrentGradient = false;
			break;
		case "Show surface gradient":
			if (source.isSelected()) graphSurfaceGradient = true;
			else graphSurfaceGradient = false;
			break;
		case "Show oxygen partial pressure":
			if (source.isSelected()) graphPPO2 = true;
			else graphPPO2 = false;
			break;
		case "Show nitrogen partial pressure":
			if (source.isSelected()) graphPPN2 = true;
			else graphPPN2 = false;
			break;
		case "Show helium partial pressure":
			if (source.isSelected()) graphPPHe = true;
			else graphPPHe = false;
			break;
		}
		plotDive(dive);
	}
}
