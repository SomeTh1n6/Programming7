package com.commands;

import com.utility.DataBaseConnection;
import com.worker.Worker;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class Update {

    private boolean flag = false;
    private Worker workerDelete = null;
    LocalDateTime localDateTime;
    public String execute(List<Worker> workers, Worker worker , int id, String sender) throws SQLException {
        for (Worker w: workers) {
            if (w.getId().equals(id)) {
                localDateTime = w.getCreationDate();
                workerDelete = w;
                flag = true;
                break;
            }
        }

        if (!flag){
            return "По заданному id работника не найдено";
        }
        PreparedStatement st = DataBaseConnection.getConnection().prepareStatement
                    ("SELECT owner_ AS owner_ FROM workers WHERE id = '"+id+"'");
        ResultSet rs = st.executeQuery();
        rs.next();
        if (rs.getString("owner_").equals(sender)){
            try {
                PreparedStatement prst = DataBaseConnection.getConnection().prepareStatement("UPDATE workers " +
                        "SET name = '"+worker.getName()+"'," +
                        "x = '"+worker.getX()+"'," +
                        "y = '"+worker.getY()+"'," +
                        "salary = '"+worker.getSalary()+"'," +
                        "position  = '"+worker.getPosition()+"'," +
                        "status  = '"+worker.getStatus()+"'," +
                        "organizationannualturnover  = '"+worker.getOrganizationAnnualTurnover()+"'," +
                        "organizationtype  = '"+worker.getOrganizationType()+"'," +
                        "organizationadress  = '"+worker.getOrganizationAdress()+"'" +
                        " WHERE id = '"+id+"'"
                        );
                prst.executeUpdate();
                prst.close();
                workers.remove(workerDelete); // удаление работника
                worker.setId(id);
                worker.setCreationDate(localDateTime);
                workers.add(worker);
                workers.sort(Worker::compareTo);
                return "Элемент успешно изменен";
            }catch (SQLException e){
                System.out.println(e.getMessage());
                return "Непредвиденная ошибка";
            }
        }else{
            return "Отказано в доступе";
        }


    }
}
