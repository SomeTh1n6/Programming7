package com.worker.properties;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private int x; //Значение поля должно быть больше -738
    private Float y; //Поле не может быть null

    public Coordinates(int x, Float y){
        this.x = x;
        this.y = y;
    }

    /**@return х координата
     * */
    public int getX(){
        return this.x;
    }

    /**@return у координата
     * */
    public Float getY(){
        return this.y;
    }

}
