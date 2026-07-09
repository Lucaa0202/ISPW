package org.example.project3.dao.full.json;

import org.example.project3.dao.CredentialsDAO;
import org.example.project3.model.*;
import org.example.project3.exceptions.WrongEmailOrPasswordException;


public class CredentialsDAOJSON extends AbstractUserDAOJSON implements CredentialsDAO {

    //Singleton
    private static CredentialsDAOJSON instance=null;
    // Costruttore privato per evitare la creazione di nuove istanze
    private CredentialsDAOJSON() {
        loadFile();
    }
    // Metodo per ottenere l'istanza unica
    public static CredentialsDAOJSON getInstance() {
        if(instance==null){
            instance=new CredentialsDAOJSON();
        }
        return instance;
    }

    @Override
    public void login(Credentials credentials) throws WrongEmailOrPasswordException {
        loadFile();
        LoggedUser  loggedUser  = userList.stream().filter(p -> p.getCredentials().getMail().equals(credentials.getMail())).findFirst().orElse(null);
        if (loggedUser == null || !loggedUser.getCredentials().getPassword().equals(credentials.getPassword())) {
            throw new WrongEmailOrPasswordException("Mail o password errati");
        }
        credentials.setRole(loggedUser.getCredentials().getRole());
    }

    @Override
    protected String convertUserToString(LoggedUser loggedUser,Credentials credentials){
        throw new UnsupportedOperationException("Non supportato.");
    }


}
