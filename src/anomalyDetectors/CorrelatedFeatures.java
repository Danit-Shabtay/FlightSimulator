package anomalyDetectors;

import additional.Line;

public class CorrelatedFeatures {
	public final String feature1,feature2;
	public final double correlation;
	public final Line linReg;
	public final double threshold;
	
	public CorrelatedFeatures(String feature1, String feature2, double correlation, Line linReg, double threshold) {
		this.feature1 = feature1;
		this.feature2 = feature2;
		this.correlation = correlation;
		this.linReg = linReg;
		this.threshold = threshold;
	}

	public String getFeature1() {
		return feature1;
	}

	public String getFeature2() {
		return feature2;
	}

	public double getCorrelation() {
		return correlation;
	}

	public Line getlinReg() {
		return linReg;
	}

	public double getThreshold() {
		return threshold;
	}
}
