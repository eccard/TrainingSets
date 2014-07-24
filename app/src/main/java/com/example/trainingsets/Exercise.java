package com.example.trainingsets;

public class Exercise{
	
	public static String[] colunas = new String[]{Exercise.NOME,Exercise.GRUPOMUSCULAR,Exercise.SERIE,Exercise.REPETICAO,Exercise.PESO,Exercise.DONE};
    
	private int id;
	private String name;
	private String muscularGroup;
	private int qntSeries;
	private int qntRepeats;
	private int weight;
	private int treino;
	private int done;
	
    public static String NOME = "nome";
    public static String GRUPOMUSCULAR = "grupomuscular";
    public static String SERIE = "serie";
    public static String REPETICAO = "repeticoes";
    public static String PESO = "peso";
    public static String DONE = "concluido";
	
	public Exercise(int id, String name, String muscularGroup, int qntSeries, 
			int qntRepeats,int weight, int treino){
		
		this.id=id;
		this.name = name;
		this.muscularGroup = muscularGroup;
		this.qntSeries = qntSeries;
		this.qntRepeats = qntRepeats;
		this.treino=treino;
		this.weight = weight;
		this.done = 0;
	}
	
	// Métodos Set
	public void setName(String name){
		
		this.name = name;
	}
	
	public void setMuscularGroup(String muscularGroup){
		
		this.muscularGroup = muscularGroup;
	}
	
	public void setQntSeries(int qntSeries){
		
		this.qntSeries = qntSeries;
	}
	
	public void setQntRepeats(int qntRepeats){
	
		this.qntRepeats = qntRepeats;
	}
	
	public void setWeight(int weight){
		
		this.weight = weight;
	}
	
   public void setDone(int done){
		
		this.done = done;
	}
	
	// Métodos Get
   public int getDone(){
		
		return this.done;
	}
   
   public String getName(){
		
		return this.name;
	}
	
	public String getMuscularGroup(){
		
		return this.muscularGroup;
	}

	public int getQntSeries(){
		
		return this.qntSeries;
	}
	
	public int getQntRepeats(){
	
		return this.qntRepeats;
	}
	
	public int getWeight(){
		
		return this.weight;
	}
	
	public int getId(){
		
		return this.id;
	}
	
	public int getTreino() {
	
		return this.treino;
		
	}
	public String tostring(){
		
		return + this.id + 
				" " + this.name +
				" "  + this.muscularGroup + 
				" "+ this.qntRepeats + 
				" " + this.qntSeries + 
				" " + this.weight + 
				" " + this.treino;
	}
}
