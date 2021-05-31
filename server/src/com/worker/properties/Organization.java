package com.worker.properties;

import java.io.Serializable;

public class Organization implements Serializable {
    private double annualTurnover; //Значение поля должно быть больше 0
    private OrganizationType type; //Поле может быть null
    private Address officialAddress; //Поле может быть null

    public Organization(double annualTurnover, OrganizationType type, Address officialAddress){
        this.annualTurnover = annualTurnover;
        this.type = type;
        this.officialAddress = officialAddress;
    }

    /**@return годовой оборот организации
     * */
    public double getAnnualTurnover(){
        return annualTurnover;
    }

    /**@return тип организации
     * */
    public String getOrganizationType(){
        return type.getOrganizationType();
    }

    public OrganizationType getType(){
        return type;
    }

    /**@return официальный адрес организации
     * */
    public String getOfficialAddress(){
        return officialAddress.getStreet();
    }
}
