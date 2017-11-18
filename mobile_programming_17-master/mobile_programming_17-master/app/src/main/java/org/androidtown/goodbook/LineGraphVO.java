package org.androidtown.goodbook;

import java.util.List;

public class LineGraphVO extends Graph{

	//max value
	private int maxValue 		= DEFAULT_MAX_VALUE;

	//increment
	private int increment 		= DEFAULT_INCREMENT;

	//animation
	private GraphAnimation animation 	= null;

	private String[] legendArr 			= null;
	private List<LineGraph> arrGraph 	= null;

	private int graphBG = -1;

	private boolean isDrawRegion 		= false;

	public LineGraphVO(String[] legendArr, List<LineGraph> arrGraph) {
		super();
		this.setLegendArr(legendArr);
		this.arrGraph = arrGraph;
	}

	public LineGraphVO(String[] legendArr, List<LineGraph> arrGraph, int graphBG) {
		super();
		this.setLegendArr(legendArr);
		this.arrGraph = arrGraph;
		this.setGraphBG(graphBG);
	}

	public LineGraphVO(int paddingBottom, int paddingTop, int paddingLeft,
					   int paddingRight, int marginTop, int marginRight, int maxValue,
					   int increment, String[] legendArr, List<LineGraph> arrGraph) {
		super(paddingBottom, paddingTop, paddingLeft, paddingRight, marginTop, marginRight);
		this.maxValue = maxValue;
		this.increment = increment;
		this.setLegendArr(legendArr);
		this.arrGraph = arrGraph;
	}

	public LineGraphVO(int paddingBottom, int paddingTop, int paddingLeft,
					   int paddingRight, int marginTop, int marginRight, int maxValue,
					   int increment, String[] legendArr, List<LineGraph> arrGraph, int graphBG) {
		super(paddingBottom, paddingTop, paddingLeft, paddingRight, marginTop, marginRight);
		this.maxValue = maxValue;
		this.increment = increment;
		this.setLegendArr(legendArr);
		this.arrGraph = arrGraph;
		this.setGraphBG(graphBG);
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public int getIncrement() {
		return increment;
	}

	public void setIncrement(int increment) {
		this.increment = increment;
	}

	public String[] getLegendArr() {
		return legendArr;
	}

	public void setLegendArr(String[] legendArr) {
		this.legendArr = legendArr;
	}

	public List<LineGraph> getArrGraph() {
		return arrGraph;
	}

	public void setArrGraph(List<LineGraph> arrGraph) {
		this.arrGraph = arrGraph;
	}

	public int getGraphBG() {
		return graphBG;
	}

	public void setGraphBG(int graphBG) {
		this.graphBG = graphBG;
	}

	public GraphAnimation getAnimation() {
		return animation;
	}

	public void setAnimation(GraphAnimation animation) {
		this.animation = animation;
	}

	public boolean isDrawRegion() {
		return isDrawRegion;
	}

	public void setDrawRegion(boolean isDrawRegion) {
		this.isDrawRegion = isDrawRegion;
	}
}
