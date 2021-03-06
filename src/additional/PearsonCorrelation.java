package additional;

import anomalyDetectors.CorrelatedFeatures;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PearsonCorrelation {
	public Map<String, CorrelatedFeatures> getTheMostCorrelatedFeaturesMap(TimeSeries ts) {
		ArrayList<CorrelatedFeatures> allCf = new ArrayList();
		double[][] val = new double[ts.getNumOfColumns()][ts.getRowSize()];
		for (int i = 0; i < ts.getNumOfColumns(); i++) {
			for (int j = 0; j < ts.getRowSize(); j++) {
				val[i][j] = ts.getAttributeData(ts.getAttributes().get(i)).get(j);
			}
		}
		for (int i = 0; i < ts.getNumOfColumns(); i++) {
			for (int j = i + 1; j < ts.getNumOfColumns(); j++) {
				double p = StatLib.pearson(val[i], val[j]);

				Point ps[] = toPoints(ts.getAttributeData(ts.getAttributes().get(i)), ts.getAttributeData(ts.getAttributes().get(j)));
				Line lin_reg = StatLib.linearReg(ps);
				double threshold = findThreshold(ps, lin_reg) * 1.1f; 

				CorrelatedFeatures c = new CorrelatedFeatures(ts.getAttributes().get(i), ts.getAttributes().get(j), p, lin_reg, threshold);
				if (Math.abs(p) > 0) {
					allCf.add(c);
				}
			}
		}

		Map<String, CorrelatedFeatures> tmpfs = new HashMap();
		String feature1, feature2;
		double correlation;

		for (int i = 0; i < allCf.size(); i++) {
			CorrelatedFeatures correlatedFeature = allCf.get(i);
			correlation = Math.abs(correlatedFeature.getCorrelation());
			feature1 = correlatedFeature.getFeature1();
			feature2 = correlatedFeature.getFeature2();

			if (!tmpfs.containsKey(feature1) || tmpfs.get(feature1).correlation < correlation) {
				tmpfs.put(feature1, correlatedFeature);
			}
			if (!tmpfs.containsKey(feature2) || tmpfs.get(feature2).correlation < correlation) {
				tmpfs.put(feature2, correlatedFeature);
			}
		}
		return tmpfs;
	}

	private double findThreshold (Point ps[],Line rl){
		double max = 0;
		for (int i = 0; i < ps.length; i++) {
			double d = Math.abs(ps[i].getY() - rl.f(ps[i].getX()));
			if (d > max)
				max = d;
		}
		return max;
	}

	private Point[] toPoints (ArrayList < Double > x, ArrayList < Double > y){
		Point[] ps = new Point[x.size()];
		for (int i = 0; i < ps.length; i++)
			ps[i] = new Point(x.get(i), y.get(i));
		return ps;
	}

	private static class StatLib {
		public static double avg(double[] x) {
			double sum = 0;
			for (int i = 0; i < x.length; sum += x[i], i++) ;
			return sum / x.length;
		}

		public static double var(double[] x) {
			double av = avg(x);
			double sum = 0;
			for (int i = 0; i < x.length; i++) {
				sum += x[i] * x[i];
			}
			return sum / x.length - av * av;
		}

		public static double cov(double[] x, double[] y) {
			double sum = 0;
			for (int i = 0; i < x.length; i++) {
				sum += x[i] * y[i];
			}
			sum /= x.length;
			return sum - avg(x) * avg(y);
		}

		public static double pearson(double[] x, double[] y) {
			return (double) (cov(x, y) / (Math.sqrt(var(x)) * Math.sqrt(var(y))));
		}

		public static Line linearReg(Point[] points) {
			double x[] = new double[points.length];
			double y[] = new double[points.length];
			for (int i = 0; i < points.length; i++) {
				x[i] = points[i].getX();
				y[i] = points[i].getY();
			}
			double a = cov(x, y) / var(x);
			double b = avg(y) - a * (avg(x));

			return new Line(a, b);
		}
	}
}

