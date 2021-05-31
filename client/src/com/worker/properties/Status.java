package com.worker.properties;

import java.io.Serializable;
import java.util.Arrays;

public enum Status implements Serializable {
    FIRED("Уволен"),
    HIRED("Нанят"),
    RECOMMENDED_FOR_PROMOTION("Рекомендуется для продвижения по службе"),
    REGULAR("Основной"),
    PROBATION("На испытательном сроке"),
    UNDEFINED("-");

    private String status;

    Status(String status) {
        this.status = status;
    }

    /**@return статус работника в организации
     * */
    public String getStatus(){
        return status;
    }

    public static Status getEnum(String value){
        Status status = Arrays.stream(Status.values()).
                filter(m -> m.status.equals(value)).
                findAny().orElse(null);
        return status;
    }
}
