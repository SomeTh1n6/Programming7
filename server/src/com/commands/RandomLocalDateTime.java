package com.commands;

import java.time.LocalDateTime;

public class RandomLocalDateTime {
    /** Генерация рандомного значения LocalDateTime
     * Каждый параметр генерируется рандомно и отдельно
     * @return LocalDateTime
     */
    public LocalDateTime randomLocalDateTime() {
        int year = (int) (Math.random() * (2021.0 - 1970.0) + 1970.0);
        int month = (int) (Math.random() * (13.0 - 1.0) + 1.0);
        int day;
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12){
            day = (int) (Math.random() * (32.0 - 1.0) + 1.0);
        }
        else if(month == 2){
            if ((year%4 == 0 && year%100 != 0) || year%400==0 ){
                day = (int) (Math.random() * (30.0 - 1.0) + 1.0);
            }
            else{
                day = (int) (Math.random() * (29.0 - 1.0) + 1.0);
            }
        }
        else{
            day = (int) (Math.random() * (31.0 - 1.0) + 1.0);
        }
        int hour = (int) (Math.random() * (24.0 - 1.0) + 1.0);
        int minute = (int) (Math.random() * (60.0 - 1.0) + 1.0);
        int second = (int) (Math.random() * (60.0 - 1.0) + 1.0);
        LocalDateTime localDateTime = LocalDateTime.of(year,month,day,hour,minute,second); //создается объект LocalDateTime

        return  localDateTime;
    }
}
