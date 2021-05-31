package com.commands;

import com.CollectionManager;
import com.sun.xml.internal.stream.writers.UTF8OutputStreamWriter;
import com.utility.DataBaseConnection;
import com.worker.Worker;
import com.worker.properties.*;

import java.sql.*;
import java.time.LocalDateTime;;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class Add {
    private Integer id;
    public static final Logger logger = Logger.getLogger(Add.class.getName());
    /***
     * Добавление готового Worker в коллекцию, генерация Id и LocalDateTime
     * @param workers непосредственно коллекция , куда добавляем
     * @param worker что добавляем
     */
    public void execute(List<Worker> workers, Worker worker, String sender){
        worker.setCreationDate(new RandomLocalDateTime().randomLocalDateTime());

        try {
            PreparedStatement prst = DataBaseConnection.getConnection().prepareStatement("INSERT INTO workers " +
                    "(owner_, name, x, y, creation_date," +
                    " salary, position, status, organizationannualturnover, organizationtype, organizationadress) " +
                    "VALUES (?, ?, ?, ?, ?, ?, CAST (? AS positions), CAST (? AS status), ?, CAST (? AS organizationtype), ?);");

            prst.setString(1,sender);
            prst.setString(2, worker.getName());
            prst.setInt(3,worker.getX());
            prst.setFloat(4,worker.getY());
            prst.setTimestamp(5,Timestamp.valueOf(worker.getCreationDate()));
            prst.setDouble(6, worker.getSalary());
            prst.setString(7, worker.getPosition());
            prst.setString(8, worker.getStatus());
            prst.setDouble(9, worker.getOrganizationAnnualTurnover());
            prst.setString(10, worker.getOrganizationType());
            prst.setString(11,worker.getOrganizationAdress());
            prst.executeUpdate();
            prst.close();
            PreparedStatement st = DataBaseConnection.getConnection().prepareStatement
                    ("SELECT MAX(id) AS id " + "FROM workers WHERE owner_=\'"+ sender +"\';");
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                worker.setId(id);
                workers.add(worker);
                //System.out.println(worker.toString());
                workers.sort(Worker::compareTo);
            }

        }catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
