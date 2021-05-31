package com.utility;

import java.io.*;

public class Serializer {
    public byte[] serialize(Response response) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(response);
        objectOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

    public Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(data));
        return inputStream.readObject();
    }
}
