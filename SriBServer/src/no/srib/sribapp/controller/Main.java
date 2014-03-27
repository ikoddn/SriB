package no.srib.sribapp.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import no.srib.sribapp.model.Definition;
import no.srib.sribapp.model.Podcast;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("heroku_080ddff3a8918b6");
        EntityManager em = emf.createEntityManager();
        List<Podcast> list = em.createNamedQuery("Podcast.findAll", Podcast.class).getResultList();
        
        for (Podcast p : list) {
            System.out.println("Title: " + p.getTitle());
            Definition d = p.getDefinition();
            System.out.println("Program: " + d.getName());
        }
    }
}