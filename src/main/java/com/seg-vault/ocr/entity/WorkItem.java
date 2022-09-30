/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.seg-vault.ocr.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;


/**
 *
 * @author paint
 */
@Entity
public class WorkItem {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    @Column(nullable = false)
    private String path;
    
    protected WorkItem(){}
    
    public WorkItem(String path){
        this.path = path;
    }

    public Long getId(){
        return this.id;
    }
    
    public String getPath(){
        return this.path;
    }
}
