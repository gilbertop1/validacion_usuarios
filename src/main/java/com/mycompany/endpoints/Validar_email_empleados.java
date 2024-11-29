/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.endpoints;
import com.mycompany.validacionusuarios.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mycompany.validacionusuarios.UserCredentials;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author donOs
 */
@Path("/validar_email")
public class Validar_email_empleados {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validarEmail(UserCredentials credentials) {
        MongoDatabase database = MongoDBConnection.getDatabase();
        MongoCollection<Document> empleadosCollection = database.getCollection("empleados");  // Colección empleados

        // Buscar si ya existe un usuario con el mismo correo electrónico
        Document foundUser = empleadosCollection.find(new Document("email", credentials.getEmail())).first();

        Map<String, Object> response = new HashMap<>();
        if (foundUser != null) {
            // Si el usuario ya existe, respondemos con un mensaje
            response.put("success", false);
            response.put("message", "El correo electrónico ya está registrado");
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        } else {
            // Si el correo no existe, respondemos con un mensaje indicando que está disponible
            response.put("success", true);
            response.put("message", "Correo electrónico disponible");
            return Response.ok(response).build();
        }
    }
}
