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
@Path("/validar_crm")
public class Validad_email_crm {
@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validarYRegistrarUsuario(UserCredentials credentials) {
        MongoDatabase database = MongoDBConnection.getDatabase();
        MongoCollection<Document> usuariosCollection = database.getCollection("usuarios");  // Colección usuarios

        // Primero, buscar si ya existe un usuario con el mismo correo
        Document foundUser = usuariosCollection.find(new Document("correo", credentials.getEmail())).first();

        Map<String, Object> response = new HashMap<>();

        if (foundUser != null) {
            // Si el correo ya está registrado, respondemos con un mensaje
            response.put("success", false);
            response.put("message", "El correo electrónico ya está registrado");
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        } else {
            // Si el correo no está registrado, proceder con el registro del nuevo usuario

            // Crear el nuevo documento para el usuario
            Document newUser = new Document()
                .append("nombre", credentials.getNombre())
                .append("apellido_paterno", credentials.getApellidoPaterno())
                .append("apellido_materno", credentials.getApellidoMaterno())
                .append("correo", credentials.getEmail())
                .append("pwd", credentials.getPassword())  // Asegúrate de encriptar la contraseña si es necesario
                .append("telefono", credentials.getTelefono())
                .append("direccion", new Document("calle", credentials.getCalle())
                    .append("numero", credentials.getNumero())
                    .append("colonia", credentials.getColonia())
                    .append("ciudad", credentials.getCiudad())
                    .append("estado", credentials.getEstado())
                    .append("CP", credentials.getCp()))
                .append("fecha_nacimiento", credentials.getFechaNacimiento())
                .append("no_cuenta", credentials.getNoCuenta())
                .append("fecha_creacion", new java.util.Date());  // Fecha de creación del usuario

            // Insertar el nuevo usuario en la colección
            usuariosCollection.insertOne(newUser);

            response.put("success", true);
            response.put("message", "Usuario registrado exitosamente");
            return Response.ok(response).build();
        }
    }
}
