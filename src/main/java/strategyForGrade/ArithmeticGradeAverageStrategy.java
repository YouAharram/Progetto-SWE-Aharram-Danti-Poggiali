package strategyForGrade;

import java.util.Iterator;

import domainModel.Grade;

public class ArithmeticGradeAverageStrategy implements GradeAverageStrategy {
	
	private String name;

	public ArithmeticGradeAverageStrategy(String name) {
		this.name = name;
	}
	
	@Override
	public double getAverage(Iterator<Grade> grades) {
		double sum = 0;
		int total = 0;
		while (grades.hasNext()) {
			sum = sum + grades.next().getValue();
			total++;
		}
		return total != 0 ? sum / total : 0;
	}

	@Override
	public String getName() {
		return name;
	}

}
