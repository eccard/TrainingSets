package com.example.con.webservice;

import java.util.ArrayList;
import java.util.List;

import com.example.con.model.Fichax;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;


public class FichaREST {
//	 private static final String URL_WS = "http://10.0.2.2:8080/WebServiceREST/cliente/";
//	 private static final String URL_WS = "http://10.0.0.103:8080/TSsystem2/ws/ficha/";
	 private static final String URL_WS = "http://10.0.0.107:8080/TSsystem2/ws/ficha/Gson/1";

	    public Fichax getFicha(int id) throws Exception {

	     String[] resposta = new WebServiceCliente().get(URL_WS + id);
	    
	     if (resposta[0].equals("200")) {
	         Gson gson = new Gson();
	         Fichax fichax = gson.fromJson(resposta[1], Fichax.class);
	         return fichax;
	     } else {
	         throw new Exception(resposta[1]);
	     }
	    }
	   
	    public List<Fichax> getListaFicha() throws Exception {

	     String[] resposta = new WebServiceCliente().get(URL_WS);
//	       String[] resposta = new WebServiceCliente().get(URL_WS + "buscarTodos");
	    
	     if (resposta[0].equals("200")) {
	         Gson gson = new Gson();
	         ArrayList<Fichax> listaFicha = new ArrayList<Fichax>();
	         JsonParser parser = new JsonParser();
	        JsonArray array = parser.parse(resposta[1]).getAsJsonArray();
	         
	        for (int i = 0; i < array.size(); i++) {
	             listaFicha.add(gson.fromJson(array.get(i), Fichax.class));
	         }
	         return listaFicha;
	     } else {
	         throw new Exception(resposta[1]);
	     }
	    }
	   
	    public String inserirFicha(Fichax fichax) throws Exception {
	    
	     Gson gson = new Gson();
	     String fichaJSON = gson.toJson(fichax);
	     String[] resposta = new WebServiceCliente().post(URL_WS + "inserir", fichaJSON);
	     if (resposta[0].equals("200")) {
	         return resposta[1];
	     } else {
	         throw new Exception(resposta[1]);
	     }
	    }
	   
	    public String deletarFicha(int id) {   
	     String[] resposta = new WebServiceCliente().get(URL_WS + "delete/" + id);
	     return resposta[1];
	    }

}
