package no.srib.sribapp.controller;

import java.util.List;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.hibernate.PodcastDAOImpl;
import no.srib.sribapp.dao.interfaces.PodcastDAO;
import no.srib.sribapp.model.Podcast;
import no.srib.sribapp.model.Programinfo;

public class Main {
    public static void main(String[] args) {
        PodcastDAO dao = new PodcastDAOImpl();
        
        List<Podcast> list = null;
        try {
            list = dao.getList();
        } catch (DAOException e) {
            e.printStackTrace();
        }
        System.out.println("asdf");
        for (Podcast p : list) {
            System.out.println("Title: " + p.getTitle());
            Programinfo info = p.getPrograminfo();
            System.out.println("Program: " + info.getTitle());
        }
    }
}