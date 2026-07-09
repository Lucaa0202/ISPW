package org.example.project3.utilities.others.mappers;

import org.example.project3.beans.TrainerBean;
import org.example.project3.model.Trainer;

public class TrainerMapper implements BeanAndModelMapper<TrainerBean,Trainer> {
    private final CredentialsMapper credentialsMapper = new CredentialsMapper();
    @Override
    public Trainer fromBeanToModel(TrainerBean bean) {
        if(bean.getName() != null)
            return new Trainer(credentialsMapper.fromBeanToModel(bean.getCredentialsBean()), bean.getName(), bean.getSurname(), bean.getGender(), bean.isOnline(), bean.getBirthday());
        else
            return new Trainer(credentialsMapper.fromBeanToModel(bean.getCredentialsBean()));
    }

    @Override
    public TrainerBean fromModelToBean(Trainer model) {
        if(model.getName() != null)
            return new TrainerBean(credentialsMapper.fromModelToBean(model.getCredentials()), model.getName(), model.getSurname(), model.getGender(), model.isOnline(), model.getBirthday());
        else
            return new TrainerBean(credentialsMapper.fromModelToBean(model.getCredentials()));
    }

}
