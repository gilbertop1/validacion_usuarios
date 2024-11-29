/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.validacionusuarios;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 *
 * @author donOs
 */
public class MongoDBConnection {
    private static final String CONNECTION_STRING = "mongodb+srv://validacion:2peeTq0tkxFnZanl@cluster.yix4x.mongodb.net/mydatabase?retryWrites=true&w=majority&appName=Cluster";
    private static MongoClient mongoClient = null;

    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(CONNECTION_STRING);
        }
        return mongoClient.getDatabase("test");
    }
}
