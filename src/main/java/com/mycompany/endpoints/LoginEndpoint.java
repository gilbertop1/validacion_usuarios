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
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

@Path("/login")
public class LoginEndpoint {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserCredentials credentials) {
        MongoDatabase database = MongoDBConnection.getDatabase();
        MongoCollection<Document> usersCollection = database.getCollection("users");
        System.out.println(credentials.getEmail());

        Document foundUser = usersCollection.find(new Document("email", credentials.getEmail())).first();

        Map<String, Object> response = new HashMap<>();
        if (foundUser != null) {
            String storedHashedPassword = foundUser.getString("password");

            if (BCrypt.checkpw(credentials.getPassword(), storedHashedPassword)) {
                // Login exitoso
                response.put("success", true);
                response.put("message", "Login exitoso");
                response.put("role", foundUser.getString("role"));
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

