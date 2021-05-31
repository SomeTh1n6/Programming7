package com.utility;

import java.io.Serializable;

public class Response implements Serializable {
    private String responce;
    public Response(String responce){
        this.responce = responce;
    }

    public String getResponce(){
        return this.responce;
    }
}
