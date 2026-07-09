package org.example.project3.utilities.others.mappers;

import org.example.project3.beans.RequestBean;
import org.example.project3.model.Request;

public class RequestMapper implements BeanAndModelMapper<RequestBean, Request>{
    ScheduleMapper scheduleMapper = new ScheduleMapper();
    ExerciseMapper exerciseMapper = new ExerciseMapper();
    @Override
    public Request fromBeanToModel(RequestBean bean) {
        return new Request(bean.getID(), scheduleMapper.fromBeanToModel(bean.getScheduleBean()), exerciseMapper.fromBeanToModel(bean.getExerciseBean()), bean.getReason(), bean.getDateTime() );
    }

    @Override
    public RequestBean fromModelToBean(Request model) {
        return new RequestBean(model.getID(), scheduleMapper.fromModelToBean(model.getSchedule()), exerciseMapper.fromModelToBean(model.getExercise()), model.getReason(), model.getDateTime());
    }
}
