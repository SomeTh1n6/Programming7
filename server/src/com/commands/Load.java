package com.commands;

import com.utility.DataBaseConnection;
import com.worker.Worker;
import com.worker.properties.*;

import java.io.*;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class Load {

    public void execute(List<Worker> workers) throws SQLException {
        try{
            PreparedStatement st = DataBaseConnection.getConnection().prepareStatement
                    ("SELECT * FROM workers");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Coordinates coordinates = new Coordinates(rs.getInt("x"),rs.getFloat("y"));
                LocalDateTime creationDate = rs.getTimestamp("creation_date").toLocalDateTime();
                Double salary = rs.getDouble("salary");
                Position position = Position.getEnum(rs.getString("position"));
                Status status = Status.getEnum(rs.getString("status"));
                Organization organization = new Organization(
                        rs.getDouble("organizationannualturnover"),
                        OrganizationType.getEnum(rs.getString("organizationtype")),
                        new Address(rs.getString("organizationadress")));

                workers.add(new Worker(id,name,coordinates,creationDate,salary,position,status,organization));
                //System.out.println(worker.toString());
            }
            workers.sort(Worker::compareTo);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }
}
