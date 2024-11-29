/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.validacionusuarios;
import com.mongodb.client.MongoDatabase;

/**
 *
 * @author donOs
 */
public class MongoDBConnectionTest {
    public static void main(String[] args) {
        try {
            // Intenta obtener la base de datos
            MongoDatabase database = MongoDBConnection.getDatabase();
            System.out.println("Conexión exitosa a la base de datos: " + database.getName());
            
            // Listar las colecciones en la base de datos
            System.out.println("Colecciones disponibles:");
            for (String collectionName : database.listCollectionNames()) {
                System.out.println("- " + collectionName);
            }
        } catch (Exception e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }
}
