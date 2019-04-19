package src.main.java;

public class StudentData
{
	public String[] dates;
	public String[] startTime;
	public String[] endTime;
	public String[] times;
	
	public int length;
	public String name;
	public float totalTime;
	
	public StudentData(String[] Dates, String[] StartTime, String[] EndTime, String[] Times, String StudentName, int Length, float TotalTime)
	{
		dates = Dates;
		startTime = StartTime;
		endTime = EndTime;
		times = Times;
		
		name = StudentName;
		length = Length;
		totalTime = TotalTime;
	}
}