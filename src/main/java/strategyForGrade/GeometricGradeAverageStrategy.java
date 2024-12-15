package strategyForGrade;

import java.util.Iterator;

import domainModel.Grade;

public class GeometricGradeAverageStrategy implements GradeAverageStrategy {

	private String name;

	public GeometricGradeAverageStrategy(String name) {
	 this.name = name;
	}
	
	@Override
	public double getAverage(Iterator<Grade> grades) {
		double product = 1;
		int total = 0;
		while (grades.hasNext()) {
			product = product * grades.next().getValue();
			total++;
		}
		return total != 0 ? Math.pow(product, 1.0 / total) : 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

}
