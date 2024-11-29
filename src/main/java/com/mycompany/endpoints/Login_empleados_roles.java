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
import java.util.Arrays;
import org.bson.Document;


import java.util.HashMap;
import java.util.Map;

@Path("/login_empleados")
public class Login_empleados_roles {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserCredentials credentials) {
        MongoDatabase database = MongoDBConnection.getDatabase();
        MongoCollection<Document> empleadosCollection = database.getCollection("empleados");  // Colección empleados

        // Realizar la consulta agregada para obtener el rol junto con el usuario
        Document foundUser = empleadosCollection.aggregate(Arrays.asList(
            new Document("$match", new Document("email", credentials.getEmail())), // Buscar por email
            new Document("$lookup", new Document("from", "roles")  // Realizar el join con la colección de roles
                .append("localField", "ROL")
                .append("foreignField", "_id")
                .append("as", "rol_info")),
            new Document("$unwind", "$rol_info"),  // Desenrollar el array "rol_info"
            new Document("$project", new Document("nombre", 1)  // Seleccionar los campos
                .append("email", 1)
                .append("pwd", 1)
                .append("rol", "$rol_info.rol")  // Tomar el rol desde la colección "roles"
                .append("estatus", 1)  // Incluir el estado del empleado
            )
        )).first();  // Obtener el primer (y único) resultado de la consulta

        Map<String, Object> response = new HashMap<>();
        if (foundUser != null) {
            // Comparar la contraseña en texto plano
            String storedPassword = foundUser.getString("pwd");

            if (storedPassword.equals(credentials.getPassword())) {
                // Login exitoso
                response.put("success", true);
                response.put("message", "Login exitoso");
                response.put("role", foundUser.getString("rol"));  // Ahora se obtiene el rol correctamente
                response.put("estatus", foundUser.getString("estatus"));  // Agregar el estado del empleado
                return Response.ok(response).build();
            } else {
                // Contraseña incorrecta
                response.put("success", false);
                response.put("message", "Contraseña incorrecta");
                response.put("role","---");
                response.put("estatus", "---");
                return Response.status(Response.Status.UNAUTHORIZED).entity(response).build();
            }
        } else {
            // Usuario no encontrado
            response.put("success", false);
            response.put("message", "Usuario no encontrado");
            response.put("role","---");
            response.put("estatus", "---");
            return Response.status(Response.Status.NOT_FOUND).entity(response).build();
        }
    }
}
