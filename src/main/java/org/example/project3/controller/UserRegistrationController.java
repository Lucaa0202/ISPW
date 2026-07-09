package org.example.project3.controller;

import org.example.project3.beans.CustomerBean;
import org.example.project3.beans.LoggedUserBean;
import org.example.project3.beans.TrainerBean;
import org.example.project3.dao.CredentialsDAO;
import org.example.project3.dao.CustomerDAO;
import org.example.project3.dao.TrainerDAO;
import org.example.project3.exceptions.LoginAndRegistrationException;
import org.example.project3.exceptions.MailAlreadyExistsException;
import org.example.project3.model.Customer;
import org.example.project3.model.Trainer;
import org.example.project3.patterns.factory.BeanAndModelMapperFactory;
import org.example.project3.patterns.factory.FactoryDAO;
import org.example.project3.utilities.enums.Role;

public class UserRegistrationController {
    private final BeanAndModelMapperFactory beanAndModelMapperFactory;
    private final CredentialsDAO loginGeneric;
    private final CustomerDAO customerGeneric;
    private final TrainerDAO trainerGeneric;
    // Costruttore
    public UserRegistrationController(){
        this.beanAndModelMapperFactory = BeanAndModelMapperFactory.getInstance();
        this.loginGeneric = FactoryDAO.getDAO();
        this.customerGeneric = FactoryDAO.getCustomerDAO();
        this.trainerGeneric = FactoryDAO.getTrainerDAO();
    }

    public void registerUser(LoggedUserBean loggedUserBean) throws LoginAndRegistrationException, MailAlreadyExistsException {
        if(loggedUserBean.getCredentialsBean().getRole().equals(Role.CLIENT)){
            registerCustomer(new CustomerBean(loggedUserBean.getCredentialsBean(), loggedUserBean.getName(), loggedUserBean.getSurname(), loggedUserBean.getGender(), loggedUserBean.isOnline(), loggedUserBean.getBirthday()));
        }
        else if(loggedUserBean.getCredentialsBean().getRole().equals(Role.TRAINER)){
            registerTrainer(new TrainerBean(loggedUserBean.getCredentialsBean(), loggedUserBean.getName(), loggedUserBean.getSurname(), loggedUserBean.getGender(), loggedUserBean.isOnline(), loggedUserBean.getBirthday()));
        }
    }

    public void registerCustomer(CustomerBean customerBean) throws MailAlreadyExistsException, LoginAndRegistrationException {
        Customer customer = beanAndModelMapperFactory.fromBeanToModel(customerBean, CustomerBean.class);
        try{
            customerGeneric.registerCustomer(customer);
        } catch (MailAlreadyExistsException e) {
            throw new MailAlreadyExistsException(e.getMessage());
        } catch (LoginAndRegistrationException e) {
            throw new LoginAndRegistrationException(e.getMessage());
        }

    }

    public void registerTrainer(TrainerBean trainerBean) throws MailAlreadyExistsException, LoginAndRegistrationException {
        Trainer trainer = beanAndModelMapperFactory.fromBeanToModel(trainerBean, TrainerBean.class);
        try{
            trainerGeneric.registerTrainer(trainer);
        } catch (MailAlreadyExistsException e) {
            throw new MailAlreadyExistsException(e.getMessage());
        } catch (LoginAndRegistrationException e) {
            throw new LoginAndRegistrationException(e.getMessage());
        }

    }
}