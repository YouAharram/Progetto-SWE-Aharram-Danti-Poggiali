package strategyForGrade;

import java.util.Iterator;

import domainModel.Grade;

public interface GradeAverageStrategy {
	
    String name = ""; 
    String getName();
    
	public double getAverage(Iterator<Grade> grades);
	
}