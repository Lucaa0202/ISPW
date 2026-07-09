package org.example.project3.controller;


import org.example.project3.beans.CredentialsBean;
import org.example.project3.beans.CustomerBean;
import org.example.project3.beans.LoggedUserBean;
import org.example.project3.beans.TrainerBean;
import org.example.project3.dao.CredentialsDAO;
import org.example.project3.dao.CustomerDAO;
import org.example.project3.dao.TrainerDAO;
import org.example.project3.exceptions.LoginAndRegistrationException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.exceptions.WrongEmailOrPasswordException;
import org.example.project3.model.Credentials;
import org.example.project3.model.Customer;
import org.example.project3.model.LoggedUser;
import org.example.project3.model.Trainer;
import org.example.project3.patterns.factory.BeanAndModelMapperFactory;
import org.example.project3.patterns.factory.FactoryDAO;
import org.example.project3.utilities.enums.Role;
import org.example.project3.utilities.others.Printer;

//Parte del caso d'uso: Login
public class LoginController {
    private final BeanAndModelMapperFactory beanAndModelMapperFactory;
    private final CredentialsDAO loginGeneric;
    private final CustomerDAO customerGeneric;
    private final TrainerDAO trainerGeneric;
    // Costruttore
    public LoginController(){
        this.beanAndModelMapperFactory = BeanAndModelMapperFactory.getInstance();
        this.loginGeneric = FactoryDAO.getDAO();
        this.customerGeneric = FactoryDAO.getCustomerDAO();
        this.trainerGeneric = FactoryDAO.getTrainerDAO();
    }

    public void login(CredentialsBean credentialsBean) throws WrongEmailOrPasswordException {
        try{
            Credentials credentials = beanAndModelMapperFactory.fromBeanToModel(credentialsBean, CredentialsBean.class);
            loginGeneric.login(credentials);
            credentialsBean.setRole(credentials.getRole());
        } catch (WrongEmailOrPasswordException e){
            throw new WrongEmailOrPasswordException(e.getMessage());
        } catch (LoginAndRegistrationException e){
            Printer.errorPrint(e.getMessage());
        }
    }

    public void retrieveCustomer(CustomerBean customerBean) throws NoResultException {
        Customer customer = beanAndModelMapperFactory.fromBeanToModel(customerBean, CustomerBean.class);
        retrieveUser(customer, customerBean);
    }

    public void retrieveTrainer(TrainerBean trainerBean) throws NoResultException {
        Trainer trainer = beanAndModelMapperFactory.fromBeanToModel(trainerBean, TrainerBean.class);
        retrieveUser(trainer, trainerBean);
    }

    private <M extends LoggedUser, B extends LoggedUserBean> void retrieveUser (M user, B userBean) throws NoResultException {
        // Recupera l'utente dal DAO
        if (user.getCredentials().getRole().equals(Role.CLIENT)) {
            customerGeneric.retrieveCustomer((Customer) user);
        }else if (user.getCredentials().getRole().equals(Role.TRAINER)) {
            trainerGeneric.retrieveTrainer((Trainer) user);
        }
        // Imposta i dati nel bean
        userBean.setName(user.getName());
        userBean.setSurname(user.getSurname());
        userBean.setGender(user.getGender());
        userBean.setOnline(user.isOnline());

    }
}