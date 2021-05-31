package com.commands;

import com.utility.DataBaseConnection;
import com.worker.Worker;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class RemoveHead {
    /** Удаляет первый элемент в коллекции, если она не пуста
     * @param workers коллекция, с которой ведется работа
     */
    public String execute(List<Worker> workers, String sender) throws SQLException {
        /*return workers.stream().findFirst()
                .map(worker -> "Первый элемент удален - " + workers.remove(worker)).orElse("Ошибка. Коллекция пуста.");*/
        if (!workers.isEmpty()){
            PreparedStatement st1 = DataBaseConnection.getConnection().prepareStatement
                    ("SELECT MIN(id) AS id FROM workers");
            ResultSet rs1 = st1.executeQuery();
            rs1.next();
            int id = rs1.getInt("id");
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
        }
        else {
            return "Коллекция пуста";
        }
    }
}
