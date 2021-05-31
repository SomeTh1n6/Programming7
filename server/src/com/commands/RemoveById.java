package com.commands;

import com.utility.DataBaseConnection;
import com.worker.Worker;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class RemoveById {
    /**Удаление по id
     * @param workers коллекция с которой проводится работа
     * @param id id по которому производится удаление
     * */
    public String execute(List<Worker> workers, int id, String sender) throws SQLException {
        boolean flag = workers.stream().anyMatch(worker -> worker.getId().equals(id));
        if (!flag)
            return "По заданому id сотрудника не найдено";
        else {
            try {
                PreparedStatement st = DataBaseConnection.getConnection().prepareStatement
                        ("SELECT owner_ AS owner_ FROM workers WHERE id = '"+id+"'");
                ResultSet rs = st.executeQuery();
                rs.next();
                if (rs.getString("owner_").equals(sender)){
                    try {
                        PreparedStatement prst = DataBaseConnection.getConnection().prepareStatement(
                                "DELETE FROM workers WHERE id = '"+id+"'"
                        );
                        prst.executeUpdate();
                        prst.close();
                        workers.removeIf(p -> p.getId().equals(id));
                        return "Сотрудник с id " + id + " успешно удален";
                    }catch (SQLException e){
                        System.out.println(e.getMessage());
                        return "Непредвиденная ошибка";
                    }
                }else{
                    return "Отказано в доступе";
                }
            }catch (SQLException e){
                return "Непредвиденная ошибка";
            }




        }
    }
}
