package no.srib.sribapp.controller;

import java.util.List;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.hibernate.PodcastDAOImpl;
import no.srib.sribapp.dao.interfaces.PodcastDAO;
import no.srib.sribapp.model.Definition;
import no.srib.sribapp.model.Podcast;

public class Main {
    public static void main(String[] args) {
        PodcastDAO dao = new PodcastDAOImpl();
        
        List<Podcast> list = null;
        try {
            list = dao.getList();
        } catch (DAOException e) {
            e.printStackTrace();
        }
        
        for (Podcast p : list) {
            System.out.println("Title: " + p.getTitle());
            Definition d = p.getDefinition();
            System.out.println("Program: " + d.getName());
        }
    }
}