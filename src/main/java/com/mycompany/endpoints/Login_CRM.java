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
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;
import org.bson.Document;
/**
 *
 * @author donOs
 */
@Path("/login_crm")
public class Login_CRM {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserCredentials credentials) {
        MongoDatabase database = MongoDBConnection.getDatabase();
        MongoCollection<Document> usuariosCollection = database.getCollection("usuarios");  // Colección usuarios

        // Buscar al usuario por correo electrónico
        Document foundUser = usuariosCollection.find(new Document("correo", credentials.getEmail())).first();

        Map<String, Object> response = new HashMap<>();
        if (foundUser != null) {
            // Obtener la contraseña almacenada en la base de datos
            String storedPassword = foundUser.getString("pwd");

            // Verificar la contraseña (sin encriptación si usas texto plano)
            if (storedPassword.equals(credentials.getPassword())) {
                // Login exitoso
                response.put("success", true);
                response.put("message", "Login exitoso");
                response.put("nombre", foundUser.getString("nombre"));
                response.put("correo", foundUser.getString("correo"));
                // Agregar más campos si es necesario
                return Response.ok(response).build();
            } else {
                // Contraseña incorrecta
                response.put("success", false);
                response.put("message", "Contraseña incorrecta");
                return Response.status(Response.Status.UNAUTHORIZED).entity(response).build();
            }
        } else {
            // Usuario no encontrado
            response.put("success", false);
            response.put("message", "Usuario no encontrado");
            return Response.status(Response.Status.NOT_FOUND).entity(response).build();
        }
    }
}
