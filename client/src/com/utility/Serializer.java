package com.utility;

import java.io.*;

/**
 * Сериализация и десериализация данных
 */
public class Serializer {
    /**
     * Сериализация Request
     * @param request - что сериализуем
     * @return байты
     */
    public byte[] serializeRequest(Object request) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(request);
        objectOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

    /**
     *
     * @param bytes
     * @return объект Request
     */
    public Response deserializeResponse(byte[] bytes) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
        return (Response) objectInputStream.readObject();
    }
}
