package org.example.project3.dao.full.json;

import org.example.project3.dao.TrainerDAO;
import org.example.project3.exceptions.MailAlreadyExistsException;
import org.example.project3.model.Trainer;



public class TrainerDAOJSON extends AbstractUserDAOJSON implements TrainerDAO {

    //Singleton
    private static TrainerDAOJSON instance=null;
    // Costruttore privato per evitare la creazione di nuove istanze
    private TrainerDAOJSON() {

    }
    // Metodo per ottenere l'istanza unica
    public static TrainerDAOJSON getInstance() {
        if(instance==null){
            instance=new TrainerDAOJSON();
        }
        return instance;
    }

    @Override
    public void registerTrainer(Trainer trainer) throws MailAlreadyExistsException {
        if(insertUser(trainer.getCredentials())){
            userList.add(trainer);
            addToFile(trainer);
        }
        else
            throw new MailAlreadyExistsException("Mail già registrata");
    }

    @Override
    public void retrieveTrainer(Trainer trainer) {
        loadFile();
        Trainer app = (Trainer) userList.stream().filter(p -> p.getCredentials().getMail().equals(trainer.getCredentials().getMail())).findFirst().orElse(null);
        if (app != null) {
            setUserParameters(trainer, app);
        }
    }

}
