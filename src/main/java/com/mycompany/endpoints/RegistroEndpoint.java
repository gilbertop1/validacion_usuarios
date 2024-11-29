package com.mycompany.endpoints;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.mycompany.validacionusuarios.MongoDBConnection;
import org.mindrot.jbcrypt.BCrypt;

    @Path("/register")
public class RegistroEndpoint {

    private final MongoDatabase database = MongoDBConnection.getDatabase();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(User user) {
    MongoCollection<Document> collection = database.getCollection("users");

    // Validar si el correo ya existe
    if (collection.find(Filters.eq("email", user.getEmail())).first() != null) {
        return Response.status(Response.Status.CONFLICT)
                       .entity(new ResponseMessage("El correo ya está registrado.", false))
                       .build();
    }

    // Encriptar la contraseña
    String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

    // Crear un nuevo documento de usuario
    Document newUser = new Document("name", user.getName())
            .append("email", user.getEmail())
            .append("password", hashedPassword)  // Almacena la contraseña encriptada
            .append("role", user.getRole())
            .append("status", user.getStatus());

    // Insertar el nuevo usuario
    collection.insertOne(newUser);

    return Response.ok(new ResponseMessage("Usuario registrado exitosamente.", true)).build();
}

    // Clase interna para recibir el JSON
    public static class User {
        private String name;
        private String email;
        private String password;
        private String role;
        private String status;

        // Getters y Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    // Clase para la respuesta
    public static class ResponseMessage {
        private String message;
        private boolean success;

        public ResponseMessage(String message, boolean success) {
            this.message = message;
            this.success = success;
        }

        // Getters
        public String getMessage() { return message; }
        public boolean isSuccess() { return success; }
    }
}
