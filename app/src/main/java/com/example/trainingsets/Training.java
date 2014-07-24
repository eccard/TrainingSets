package com.example.trainingsets;

import java.util.ArrayList;

public class Training {

	private String name;
	private int qntExercise;
	public ArrayList<Exercise> exerciseList;
	
	//Construtor
	
	public Training(String name, int qntExercise){
		// TODO Auto-generated constructor stub
	
		this.name = name;
		this.qntExercise = qntExercise;
		this.exerciseList = new ArrayList<Exercise>();
	}
	
	
	//Métodos Set
	public void setName(String name){
		
		this.name = name;
	}
	
	public void setQntExercise(int qntExercise){
		
		this.qntExercise = qntExercise;
	}
	
	public void setExerciseList(ArrayList<Exercise> exerciseList){
		this.exerciseList=exerciseList;
	}
	
	//Métodos Get
	public String getName(){
		
		return this.name;
	}
	
	public int getQntExercise(){
		
		return this.qntExercise;
	}
	
	public ArrayList<Exercise> getExerciseList(){
		
		return this.exerciseList;
	}
}
