package org.example.project3.controller;

import org.example.project3.dao.TrainerDAO;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.patterns.factory.FactoryDAO;
import org.example.project3.patterns.factory.BeanAndModelMapperFactory;

import org.example.project3.beans.*;
import org.example.project3.model.*;

import java.sql.SQLException;

import java.util.List;


public class TrainerDescriptionController {

    private final BeanAndModelMapperFactory factory;
    private final TrainerDAO trainerDAO;

    public TrainerDescriptionController() {
        this.factory = BeanAndModelMapperFactory.getInstance();
        this.trainerDAO = FactoryDAO.getTrainerDAO();
    }

    public TrainerBean trainerDescription(CourseBean courseBean){

        try {

            Trainer trainer;
            Course course = factory.fromBeanToModel(courseBean, CourseBean.class);
            trainer = trainerDAO.retrieveTrainerCourse(course);
            TrainerBean trainerBean = factory.fromModelToBean(trainer, Trainer.class);

            trainerBean.setSpecializations(trainerSpecializations(courseBean));

            return trainerBean;

        }catch(Exception e){
            throw new NoResultException("Errore recupero descrizione trainer"+ e.getMessage());
        }
    }

    public List<String> trainerSpecializations(CourseBean courseBean) throws SQLException {

        try {
            List<String> specializations;
            Course course = factory.fromBeanToModel(courseBean, CourseBean.class);
            specializations = trainerDAO.retrieveSpecialization(course);

            return specializations;

        }catch(Exception e){
            throw new NoResultException("Errore recuper specializzazionei"+ e.getMessage());
        }
    }

}