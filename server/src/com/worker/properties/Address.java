package com.worker.properties;

import java.io.Serializable;

public class Address implements Serializable {
    private String street; //Поле может быть null

    public Address(String street){
        this.street = street;
    }

    /**@return адрес
     * */
    public String getStreet(){
        return street;
    }
    /**@param st на какой адрес меняем начальный адрес
     * */
    public void setStreet(String st){
        this.street = st;
    }
}
