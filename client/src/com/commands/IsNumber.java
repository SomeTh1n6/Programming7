package com.commands;

public class IsNumber {
    /** Проверка строки , является ли она float
     * @param s строка которую проверяем
     * @return true или false
     * */
    public boolean isNumberFloat(String s) throws NumberFormatException {
        try {
            Float.parseFloat(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }


    /** Проверка строки , является ли она double
     * @param s строка которую
     * @return true или false
     * */
    public boolean isNumberDouble(String s){
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }


    /** Проверка строки , является ли она int
     * @param s строка которую проверяем
     * @return true или false
     **/
    public boolean isNumberInteger(String s) throws NumberFormatException {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
