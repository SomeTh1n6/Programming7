package com.utility;

import com.worker.Worker;

import java.io.Serializable;
import java.net.UnknownServiceException;
import java.nio.channels.SocketChannel;

public class Request implements Serializable {

    private String command;
    private String argument;
    private Worker worker;
    private String script;
    private UserData auth;
    private SocketChannel socketChannel;

    public Request(String command, String argument,UserData auth){
        this.command = command;
        this.argument = argument;
        this.auth = auth;
    }

    public Request(String command, UserData auth){
        this.command = command;
        this.auth = auth;
    }


    public Request(String command, String argument ,String script,UserData auth){
        this.command = command;
        this.argument = argument;
        this.script = script;
        this.auth = auth;
    }

    public Request(String command,Worker worker,UserData auth){
        this.command = command;
        this.worker = worker;
        this.auth = auth;
    }

    public Request(String command,String argument,Worker worker,UserData auth){
        this.command = command;
        this.argument = argument;
        this.worker = worker;
        this.auth = auth;
    }

    public String getCommand(){
        return this.command;
    }

    public String getArgument(){
        return this.argument;
    }


    public Worker getWorker(){
        return this.worker;
    }

    public String getScript() {
        return this.script;
    }

    public UserData getAuth(){
        return this.auth;
    }

    public SocketChannel getSocketChannel(){
        return socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel){
        this.socketChannel = socketChannel;
    }
    @Override
    public String toString(){
        return ("Запрос : " +
                "\n   команда - " + getCommand() +
                " \n   аргумент команды - " + getArgument() +
                "\n данные для авторизации - " + getAuth().toString() +
                " \n   скрипт - " + getScript() +
                " \n   сотрудник : " + getWorker());
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request that = (Request) o;
        return command.equals(that.command);
    }

}
