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
public class Fichax implements Serializable {
    
    String id;
    String nome;
    int qntTreinos;
    String dataInicio;
    String dataFim;

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

    public int getQntTreinos() {
        return qntTreinos;
    }

    public void setQntTreinos(int qntTreinos) {
        this.qntTreinos = qntTreinos;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }
    
}
