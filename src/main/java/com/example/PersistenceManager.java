/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package com.example;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author marin
 */
public class PersistenceManager {

    public static final boolean DEBUG = true;
    public static final PersistenceManager singleton = new PersistenceManager();
    protected EntityManagerFactory emf;

    private PersistenceManager() {
    }

    public static PersistenceManager getInstance() {
        return singleton;
    }
    
    public EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            createEntityManagerFactory();
        }
        return emf;
    }

    public void closeEntityManagerFactory() {
        if (emf != null) {
            emf.close();
            emf = null;
            if (DEBUG) {
                System.out.println("Persistence finished at " + new java.util.Date());
            }
        }
    }
    
    protected void createEntityManagerFactory() {
        this.emf = Persistence.createEntityManagerFactory("JPA-persistenceU", System.getProperties());
        if (DEBUG) {
            System.out.println("Persistence started at " + new java.util.Date());
        }
    }
}
