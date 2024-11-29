package com.mycompany.validacionusuarios;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Colecciones {
    private static final String CONNECTION_STRING = "mongodb+srv://validacion:2peeTq0tkxFnZanl@cluster.yix4x.mongodb.net/mydatabase?retryWrites=true&w=majority&appName=Cluster";
    private static MongoClient mongoClient = null;

    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(CONNECTION_STRING);
        }
        return mongoClient.getDatabase("test");
    }

    public static void main(String[] args) {
        MongoDatabase database = getDatabase();
        String coleccion = "usuarios";
        // Listar las colecciones
        System.out.println("Colecciones en la base de datos:");
        for (String collectionName : database.listCollectionNames()) {
            System.out.println(collectionName);
        }

        // Acceder a la colección "roles" y listar su contenido
        System.out.println("\nContenido de la colección: "+coleccion);
        MongoCollection<Document> rolesCollection = database.getCollection(coleccion);
        for (Document doc : rolesCollection.find()) {
            System.out.println(doc.toJson());
        }
    }
}







