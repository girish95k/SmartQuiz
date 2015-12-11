package com.pes.gkl.gate.modelclasses;

/**
 * Created by KAI on 25-Sep-15.
 */
public class TestStat
{
    public  String name;
    private  int correctAnswers;
    public  double rating;
    private  int total;
    public double average;
    public double percentile;
    public TestStat(String name, int correctAnswers, double rating, int total,double average,double percentile)
    {
        this.name=name;
        this.correctAnswers=correctAnswers;
        this.rating=rating;
        this.total=total;
        this.average=average;
        this.percentile=percentile;
    }
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("NAME:");
        builder.append(name);
        builder.append("\n");
        builder.append("CORRECT ANSWERS:");
        builder.append(correctAnswers);
        builder.append("\n");
        builder.append("RATING:");
        builder.append(rating);
        builder.append("\n");
        builder.append("TOTAL:");
        builder.append(total);
        builder.append("\n");
        builder.append("AVERAGE:");
        builder.append(average);
        builder.append("\n");
        builder.append("PERCENTILE:");
        builder.append(percentile);
        builder.append("\n");
        return builder.toString();
    }
}