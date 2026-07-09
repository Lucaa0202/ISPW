package org.example.project3.dao.full.json;

import org.example.project3.dao.CustomerDAO;
import org.example.project3.exceptions.MailAlreadyExistsException;
import org.example.project3.model.Customer;


public class CustomerDAOJSON extends AbstractUserDAOJSON implements CustomerDAO {

    //Singleton
    private static CustomerDAOJSON instance=null;
    // Costruttore privato per evitare la creazione di nuove istanze
    private CustomerDAOJSON() {

    }
    // Metodo per ottenere l'istanza unica
    public static CustomerDAOJSON getInstance() {
        if(instance==null){
            instance=new CustomerDAOJSON();
        }
        return instance;
    }


    @Override
    public void registerCustomer(Customer customer) throws MailAlreadyExistsException {
        if(insertUser(customer.getCredentials())){
            userList.add(customer);
            addToFile(customer);
        }
        else
            throw new MailAlreadyExistsException("Mail già registrata");
    }


    @Override
    public void retrieveCustomer(Customer customer) {
        loadFile();
        Customer app = (Customer) userList.stream().filter(p -> p.getCredentials().getMail().equals(customer.getCredentials().getMail())).findFirst().orElse(null);
        if(app != null){
            setUserParameters(customer, app);
        }
    }


    @Override
    public void removeCustomer(Customer customer) {
        userList.removeIf(p -> p.getCredentials().getMail().equals(customer.getCredentials().getMail()));
        loadFile();
    }
}
