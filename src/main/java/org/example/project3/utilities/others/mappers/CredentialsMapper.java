package org.example.project3.utilities.others.mappers;

import org.example.project3.beans.CredentialsBean;
import org.example.project3.model.Credentials;

public class CredentialsMapper implements BeanAndModelMapper<CredentialsBean, Credentials> {
    @Override
    public Credentials fromBeanToModel(CredentialsBean bean) {
        if(bean.getPassword() != null)
            return new Credentials(bean.getMail(), bean.getPassword(), bean.getRole());
        else
            return new Credentials(bean.getMail(), bean.getRole());
    }

    @Override
    public CredentialsBean fromModelToBean(Credentials model) {
        if(model.getPassword() != null)
            return new CredentialsBean(model.getMail(), model.getPassword(), model.getRole());
        else
            return new CredentialsBean(model.getMail(), model.getRole());
    }
}
