package com.worker.properties;

import java.io.Serializable;
import java.util.Arrays;

public enum Position implements Serializable {
    ENGINEER("Инженер"),
    HEAD_OF_DIVISION("Начальник отдела"),
    LEAD_DEVELOPER("Ведущий разработчик"),
    BAKER("Пекарь"),
    UNDEFINED("-");

    private String position;

    Position(String position) {
        this.position = position;
    }

    /**@return должность, занимаемая в организации
     * */
    public String getPosition(){
        return position;
    }

    public static Position getEnum(String value) {

        Position position = Arrays.stream(Position.values()).
                filter(m -> m.position.equals(value)).
                findAny().orElse(null);
        return position;
    }
}
