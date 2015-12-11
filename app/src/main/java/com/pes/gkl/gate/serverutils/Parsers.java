package com.pes.gkl.gate.serverutils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;

import com.pes.gkl.gate.modelclasses.Question;
import com.pes.gkl.gate.modelclasses.TestStat;
import com.pes.gkl.gate.modelclasses.Topic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Parsers {
	/*
	 * Takes response obtained from /Quiz/get_topics
	 * Returns Topic array of topic names
	 * See the Topic class for attribute details 
	 */
	public static Topic[] topiclistparser(String response)
	{
		/*
		System.out.println("Response: " + response);
		String[] topics=response.split(",");
		System.out.println("Length: " + topics.length);
		Topic[] topicslist=new Topic[topics.length];
		for (int i=0;i<topics.length;i++){
			String topic=topics[i];
			String[] attrs=topic.split("-");
			topicslist[i]=new Topic(Integer.parseInt(attrs[1])+"",attrs[0]);
		}
		return topicslist;
		*/
		try {
			JSONArray topics = new JSONArray(response);
			Topic[] parsedList = new Topic[topics.length()];
			for (int index = 0; index < topics.length(); index++) {
				JSONObject topic = topics.getJSONObject(index);
				String name = topic.getString("name");
				int id = topic.getInt("id");
				parsedList[index] = new Topic(id, name);

			}
			return parsedList;
		}
		catch (Exception E){

		}
		return null;
	}
	/*
	 * Takes response from /Quiz/get_questions_of_topic
	 * Returns Question array of topic names
	 * See Question class for attribute details
	 */
	/**
	 * @param response
	 *            from the server to parse
	 * @return Array of Question objects obtained from response See
	 *         Question.java for attribute details
	 * @throws JSONException
	 *             if the response isn't properly structured
	 */

	public static Question[] questionlistparser(String response) throws JSONException
	{
		/*
		String[] questions=response.split("@@@@NEXT@@@@");
		Question[] questionslist=new Question[questions.length];
		for(int i=0;i<questions.length;i++)
		{
			String question=questions[i];
			String[] attrs=question.split("@@@@DELIM@@@@");
			questionslist[i]=new Question(attrs[0],Integer.parseInt(attrs[1]),Integer.parseInt(attrs[2]),attrs[3],attrs[4],attrs[5],attrs[6],Integer.parseInt(attrs[7]),attrs[8]);
		}
		return questionslist;
		*/
		JSONArray questions = new JSONArray(response);
		Question[] parsedList = new Question[questions.length()];
		for (int index = 0; index < questions.length(); index++) {
			JSONObject question = questions.getJSONObject(index);
			String name = question.getString("name");
			int id = question.getInt("id");
			String optionA = question.getString("optionA");
			String optionB = question.getString("optionB");
			String optionC = question.getString("optionC");
			String optionD = question.getString("optionD");
			int rating = question.getInt("rating");
			int answer = question.getInt("answer");
			String solution = question.getString("solution");
			parsedList[index] = new Question(name, id, rating, optionA,
					optionB, optionC, optionD, answer, solution);
		}
		return parsedList;
	}
	public static TestStat[] teststatparser(String response) throws  JSONException
	{
		JSONObject json1 = new JSONObject(response);
		Iterator<String> i= json1.keys();
		TestStat[] parsedList=new TestStat[json1.length()];
		int index=0;
		while(i.hasNext()){
			String name=(String) i.next();
			JSONObject json=new JSONObject(json1.getString(name));
			int correctAnswers= json.getInt("Correct answers");
			double rating=json.getDouble("Rating");
			int total=json.getInt("Total");
			double average=json.getDouble("Average");
			double percentile=json.getDouble("Percentile");
			parsedList[index++]=new TestStat(name,correctAnswers,rating,total,average,percentile);
		}

		/*
		Collections.sort(new ArrayList<TestStat>(Arrays.asList(parsedList)), new Comparator<TestStat>() {

			public int compare(TestStat o1, TestStat o2) {
				if(o1.rating>o2.rating)return 1;
				return -1;
			}
		});
		*/
		return parsedList;
	}

	/*
	public static Question[] questionListParser(String response)
			throws JSONException {
		JSONArray questions = new JSONArray(response);
		Question[] parsedList = new Question[questions.length()];
		for (int index = 0; index < questions.length(); index++) {
			JSONObject question = questions.getJSONObject(index);
			String name = question.getString("name");
			int id = question.getInt("id");
			String optionA = question.getString("optionA");
			String optionB = question.getString("optionB");
			String optionC = question.getString("optionC");
			String optionD = question.getString("optionD");
			int rating = question.getInt("rating");
			int answer = question.getInt("answer");
			String solution = question.getString("solution");
			parsedList[index] = new Question(name, id, rating, optionA,
					optionB, optionC, optionD, answer, solution);
		}
		return parsedList;
	}*/
}
