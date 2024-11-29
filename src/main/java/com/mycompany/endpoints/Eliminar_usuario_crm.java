/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.endpoints;
import com.mycompany.validacionusuarios.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
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
@Path("/eliminar_usuario_crm")
public class Eliminar_usuario_crm {

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarUsuario(EliminarRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            MongoDatabase database = MongoDBConnection.getDatabase();
            MongoCollection<Document> usuariosCollection = database.getCollection("usuarios");  // Colección usuarios

            // Buscar el usuario por correo
            Document foundUser = usuariosCollection.find(new Document("correo", request.getCorreo())).first();

            if (foundUser != null) {
                // Si el usuario existe, lo eliminamos
                usuariosCollection.deleteOne(new Document("correo", request.getCorreo()));

                response.put("success", true);
                response.put("message", "Usuario eliminado exitosamente");
                return Response.ok(response).build();
            } else {
                // Si no se encuentra el usuario, respondemos con un error
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            // Capturamos cualquier excepción y respondemos con el error
            response.put("success", false);
            response.put("message", "Error en el servidor: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    public static class EliminarRequest {
        private String correo;

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }
    }
}