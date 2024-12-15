package domainModel;

import java.util.Iterator;

public class WeightedGradeAverageStrategy implements GradeAverageStrategy {
	
	private String name;
	public WeightedGradeAverageStrategy(String name) {
		this.name = name;
	}
	
	@Override
	public double getAverage(Iterator<Grade> grades) {
		double sum = 0;
		int total = 0;
		Grade grade;
		while (grades.hasNext()) {
			grade = grades.next();
			sum = sum + grade.getValue() * grade.getWeight();
			total = total + grade.getWeight();
		}
		return total != 0 ? sum / total : 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

}
