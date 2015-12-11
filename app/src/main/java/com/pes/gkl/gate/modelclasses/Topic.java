package com.pes.gkl.gate.modelclasses;

public class Topic {
	public int id;
	public String name;

	public Topic(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public String toString() {
		/*
		StringBuilder builder = new StringBuilder();
		builder.append("NAME:");
		builder.append(name);
		builder.append("\n");
		builder.append("ID:");
		builder.append(id);
		builder.append("\n");
		return builder.toString();
		*/
		return this.name;
	}
}
