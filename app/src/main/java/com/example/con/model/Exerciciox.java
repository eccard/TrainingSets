/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example.con.model;

import java.io.Serializable;


/**
 *
 * @author eccard
 */

public class Exerciciox implements Serializable {
    String id;
        String nome;
	String grupoMuscular;
	int qntSeries;
	int qntReticoes;
        int peso;
        int treino;
        int concluido;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }

    public int getQntSeries() {
        return qntSeries;
    }

    public void setQntSeries(int qntSeries) {
        this.qntSeries = qntSeries;
    }

    public int getQntReticoes() {
        return qntReticoes;
    }

    public void setQntReticoes(int qntReticoes) {
        this.qntReticoes = qntReticoes;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getTreino() {
        return treino;
    }

    public void setTreino(int treino) {
        this.treino = treino;
    }

    public int getConcluido() {
        return concluido;
    }

    public void setConcluido(int concluido) {
        this.concluido = concluido;
    }
    
}
