package com.mycompany;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api") // Define la ruta base para todos los recursos
public class MyApplication extends Application {
    // Aquí puedes agregar recursos si es necesario
}

