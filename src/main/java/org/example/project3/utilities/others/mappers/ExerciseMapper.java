package org.example.project3.utilities.others.mappers;

import org.example.project3.beans.ExerciseBean;
import org.example.project3.model.Exercise;

public class ExerciseMapper implements BeanAndModelMapper<ExerciseBean,Exercise> {
    CustomerMapper customerMapper = new CustomerMapper();
    TrainerMapper trainerMapper = new TrainerMapper();
    @Override
    public Exercise fromBeanToModel(ExerciseBean bean) {
        return new Exercise(bean.getId(), bean.getName(), bean.getDescription(), bean.getNumberSeries(), bean.getNumberReps(), bean.getRestTime());

    }

    @Override
    public ExerciseBean fromModelToBean(Exercise model) {
        return new ExerciseBean(model.getId(), model.getName(), model.getDescription(), model.getNumberSeries(), model.getNumberReps(), model.getRestTime());
    }

}