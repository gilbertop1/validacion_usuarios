package com.mycompany.endpoints;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mycompany.validacionusuarios.MongoDBConnection;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

@Path("/users")
public class UserEndpoint {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        try {
            MongoDatabase database = MongoDBConnection.getDatabase();
            MongoCollection<Document> collection = database.getCollection("users");

            // Obtener todos los documentos de la colección
            List<Document> userList = collection.find().into(new ArrayList<>());
            
            // Devuelve la lista como respuesta JSON
            return Response.ok(userList).build();
        } catch (Exception e) {
            return Response.serverError().entity("Error al acceder a la base de datos: " + e.getMessage()).build();
        }
    }
}
