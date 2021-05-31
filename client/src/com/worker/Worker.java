package com.worker;

import com.worker.properties.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;


/** Класс работника
 * */
public class Worker implements Comparable<Worker>, Serializable {
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private final String name; //Поле не может быть null, Строка не может быть пустой
    private final Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private final double salary; //Значение поля должно быть больше 0
    private final Position position; //Поле может быть null
    private final Status status; //Поле может быть null
    private final Organization organization; //Поле может быть null
    public Worker(Integer id, String name, Coordinates coordinates,LocalDateTime creationDate, double salary,
                  Position position, Status status, Organization organization){
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.salary = salary;
        this.position = position;
        this.status = status;
        this.organization = organization;
    }

    /**@return id работника
     * */
    public Integer getId(){ return this.id; }

    /**@return годового оборота организацииЦ работника
     * */
    public Double getOrganizationAnnualTurnover(){
        return (this.organization.getAnnualTurnover());
    }

    /**@return адрес организации работника
     * */
    public String getOrganizationAdress(){
        return (this.organization.getOfficialAddress());
    }

    /**@return дату создания работника
     * */
    public LocalDateTime getCreationDate(){
        return this.creationDate;
    }

    /**@return зарплату работника
     * */
    public Double getSalary(){
        return this.salary;
    }

    /**@return имя работника
     * */
    public String getName(){
        return this.name;
    }

    public Integer getX(){
        return this.coordinates.getX();
    }

    public Float getY(){
        return this.coordinates.getY();
    }

    public String getStatus(){
        return this.status.getStatus();
    }

    public String getPosition(){
        return this.position.getPosition();
    }

    public String getAdress(){
        return this.organization.getOfficialAddress();
    }

    public String getOrganizationType(){
        return this.organization.getOrganizationType();
    }

    public void setId(int id){
        this.id = id;
    }

    public void setCreationDate(LocalDateTime localDateTime){
        this.creationDate = localDateTime;
    }

    @Override
    public String toString() {
        return "\nID = " + id + ",\n    Name: " + name + "\n    Coordinates: (" + coordinates.getX() + ";" + coordinates.getY() +
                ")\n    Creation Date (YYYY-MM-DD-HH-MM-SS): " + creationDate +
                "\n    Salary: " + salary +
                "\n    Position: " + position.getPosition() +
                "\n    Status: " + status.getStatus() +
                "\n    Organization: \n      Type: " + organization.getOrganizationType() + "\n      Adress: " + organization.getOfficialAddress() +
                "\n      Annual Turnover: " +
                organization.getAnnualTurnover() + "\n" +
                "----------------------------------------------------------------------------";
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name,creationDate);
        result = 31 * result;
        return result;
    }

    @Override
    public int compareTo(Worker worker){
        return this.getId() - worker.getId();
    }
}