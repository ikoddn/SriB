package no.srib.sribapp.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("heroku_080ddff3a8918b6");
        EntityManager em = emf.createEntityManager();
        List<Podcast> list = em.createNamedQuery("Podcast.findAll", Podcast.class).getResultList();
        
        for (Podcast p : list) {
            System.out.println(p.getTitle());
        }
    }
}