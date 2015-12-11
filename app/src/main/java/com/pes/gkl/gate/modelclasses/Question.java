package com.pes.gkl.gate.modelclasses;

public class Question {
	public String name;
	public int id;
	public String optionA;
	public String optionB;
	public String optionC;
	public String optionD;
	public int rating ;
	public int answer;
	public String solution;
	public int userAnswer=-1;
	public String topic;
	public double timeSpent=0;
	public long startedTime;
	public Question(String name,int id,int rating,String a,String b,String c,String d,int answer,String solution)
	{
		this.name=name;
		this.id=id;
		optionA=a;
		optionB=b;
		optionC=c;
		optionD=d;
		this.rating=rating;
		this.answer=answer;
		this.solution=solution;
	}
}
