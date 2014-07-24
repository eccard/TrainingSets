package com.example.trainingsets;

import java.util.ArrayList;


public class Card{

	public static String[] colunas = new String[]{Card.NOME,Card.QNTTREINO,Card.DATAINIC,Card.DATAFIM};
	
	public int id;
	public String name;
	public int training;
	public String startDate;
	public String endDate;
	ArrayList<Training> trainingList;
	
	public static final String ID = "id";
	public static final String NOME = "nome";
	public static final String QNTTREINO = "qnttreino";
	public static final String DATAINIC = "datainic";
	public static final String DATAFIM = "datafim";
	
	public Card(int id,String name, int training, String startDate, String endDate){
		
		this.id = id;
		this.name = name;
		this.training = training;
		this.startDate = startDate;
		this.endDate = endDate;
		this.trainingList = new ArrayList<Training>();
		
		for(int i=0;i<this.training;i++){
			
			Training newTraining = new Training(String.valueOf
					(Character.toChars(97+i)),0);
			this.trainingList.add(newTraining);
		}
	}
	
	public void setId(int id){
		
		this.id = id;
	}
	
	
	public void setName(String name){
		
		this.name = name;
	}
	
	public void setTraining(int training){
		
		this.training = training;
	}
	
	public void setStartDate(String startDate){
	
		this.startDate = startDate;
	}
	
	public void setEndDate(String endDate){
		
		this.endDate = endDate;
	}
	
	public void setTrainingList(ArrayList<Training> trainingList){
		
		this.trainingList = trainingList;
	}
		
	public int getId(){

		return this.id;
	}
	
	public String getName(){
		
		return this.name;
	}
	
	public int getTraining(){
		
		return this.training;
	}
	
	public String getStartDate(){
	
		return this.startDate;
	}
	
	public String getEndDate(){
		
		return this.endDate;
	}
	
	public ArrayList<Training> getTrainingList(){
		
		return this.trainingList;
	}
}
