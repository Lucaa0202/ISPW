package org.example.project3.utilities.others.mappers;

import org.example.project3.beans.ReservationBean;
import org.example.project3.model.Reservation;

public class ReservationMapper implements BeanAndModelMapper<ReservationBean, Reservation> {

    CustomerMapper customerMapper = new CustomerMapper();
    CourseMapper courseMapper = new CourseMapper();
    @Override
    public Reservation fromBeanToModel(ReservationBean bean) {
        return new Reservation(customerMapper.fromBeanToModel(bean.getCustomer()), courseMapper.fromBeanToModel(bean.getCourse()), bean.getDay(), bean.getHour());
    }

    @Override
    public ReservationBean fromModelToBean(Reservation model) {
        return new ReservationBean( customerMapper.fromModelToBean(model.getCustomer()), courseMapper.fromModelToBean(model.getCourse()), model.getDay(), model.getHour());
    }
}
