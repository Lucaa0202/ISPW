package org.example.project3.controller;


import org.example.project3.beans.RequestBean;
import org.example.project3.beans.ScheduleBean;

import org.example.project3.beans.TrainerBean;
import org.example.project3.dao.RequestDAO;
import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.NoResultException;

import org.example.project3.model.*;

import org.example.project3.patterns.factory.BeanAndModelMapperFactory;
import org.example.project3.patterns.factory.FactoryDAO;
import org.example.project3.patterns.observer.RequestManagerConcreteSubject;

import java.util.ArrayList;
import java.util.List;

public class RequestModifyController {
    private final BeanAndModelMapperFactory beanAndModelMapperFactory;
    private final RequestDAO requestDAO;

    //Costruttore
    public RequestModifyController(){
        this.beanAndModelMapperFactory = BeanAndModelMapperFactory.getInstance();
        this.requestDAO = FactoryDAO.getRequestDAO();
    }

    //metodo per eliminare una richiesta
    public void deleteRequest(RequestBean requestBean) throws DAOException{
        try {
            RequestManagerConcreteSubject requestManagerConcreteSubject = RequestManagerConcreteSubject.getInstance();  //recupero istanza del ConcreteSubject
            Request request = beanAndModelMapperFactory.fromBeanToModel(requestBean, RequestBean.class);
            requestDAO.deleteRequest(request);
            requestManagerConcreteSubject.removeRequest(request);//rimuovo la richiesta dalla lista salvata nel ConcreteSubject
        }catch(DAOException e){
            throw new DAOException("Errore nel DAO",e);
        }
    }

    //metodo per creare una richiesta
    public RequestBean createRequestBean(Request request) {
        return beanAndModelMapperFactory.fromModelToBean(request, Request.class);
    }

    //Metodo per inviare una richiesta
    public void sendRequest(RequestBean requestBean) throws DAOException {
        Request request = beanAndModelMapperFactory.fromBeanToModel(requestBean, RequestBean.class);
        try{
            requestDAO.sendRequest(request);
            RequestManagerConcreteSubject requestManagerConcreteSubject = RequestManagerConcreteSubject.getInstance();
            requestManagerConcreteSubject.addRequest(request);
        } catch (Exception e){
            throw new DAOException("Errore nell'invio della richiesta controller"+e.getMessage(),e);
        }
    }

    //Metodo per controllare se è già stata inviata una richiesta
    public boolean hasAlreadySentARequest(ScheduleBean scheduleBean) throws NoResultException {
        Schedule schedule = beanAndModelMapperFactory.fromBeanToModel(scheduleBean, ScheduleBean.class);
        Request request= new Request(schedule);
        try{
            return requestDAO.hasAlreadySentRequest(request);
        } catch (Exception e){
            throw new NoResultException("Errore nel controllo delle richieste",e);
        }
    }

    //Metodo per recuperare le richieste del trainer
    public void retrieveRequests(TrainerBean trainerBean, List<RequestBean> requestBeans) throws NoResultException {
        // converte il bean in model per poter cercare la scheda visto che non si effettua con il bean
        try{

            List<Request> requests = new ArrayList<>();

            Trainer trainer = beanAndModelMapperFactory.fromBeanToModel(trainerBean, TrainerBean.class);
            requestDAO.retrieveRequests(trainer, requests);
            for(int i = 0; i < requests.size(); i++){
                RequestBean requestBean = beanAndModelMapperFactory.fromModelToBean(requests.get(i), Request.class);

                //inserisce i dati nel bean
                requestBean.setID(requests.get(i).getID());
                requestBean.setSchedule(beanAndModelMapperFactory.fromModelToBean(requests.get(i).getSchedule(), Schedule.class));
                requestBean.setExercise(beanAndModelMapperFactory.fromModelToBean(requests.get(i).getExercise(), Exercise.class));
                requestBean.setReason(requests.get(i).getReason());
                requestBean.setDateTime(requests.get(i).getDateTime());

                requestBeans.add(requestBean);
            }
            RequestManagerConcreteSubject requestManagerConcreteSubject = RequestManagerConcreteSubject.getInstance();
            requestManagerConcreteSubject.loadRequests(requests);
        }catch(NoResultException _){
            throw new NoResultException("Nessuna scheda trovata");
        }catch(DAOException e){
            throw new DAOException("Errore durante la ricerca",e);
        }
    }
}
