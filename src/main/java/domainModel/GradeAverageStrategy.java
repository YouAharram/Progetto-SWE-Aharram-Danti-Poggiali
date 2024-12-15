package domainModel;

import java.util.Iterator;

public interface GradeAverageStrategy {
	
    String name = ""; 
    String getName();
    
	public double getAverage(Iterator<Grade> grades);
	
}