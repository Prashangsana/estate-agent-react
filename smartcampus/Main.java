/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;

import com.smartcampus.filter.LoggingFilter;
import com.smartcampus.mapper.GlobalExceptionMapper;
import com.smartcampus.mapper.LinkedResourceNotFoundExceptionMapper;
import com.smartcampus.mapper.RoomNotEmptyExceptionMapper;
import com.smartcampus.mapper.SensorUnavailableExceptionMapper;
import com.smartcampus.resource.DiscoveryResource;
import com.smartcampus.resource.RoomResource;
import com.smartcampus.resource.SensorResource;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;
import java.util.logging.Logger;

/**
 * Application entry point.
 *
 * Bootstraps the embedded Grizzly HTTP server and registers all
 * JAX-RS components (resources, filters, exception mappers, features).
 *
 * HOW TO RUN IN NETBEANS:
 *   1. Right-click the project → Clean and Build
 *   2. Right-click Main.java → Run File   (or press Shift+F6)
 *   3. The server starts at http://localhost:8080/api/v1
 *   4. Press ENTER in the console to stop it.
 */
public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    /** Base URI – all resource paths will be appended to this. */
    public static final String BASE_URI = "http://localhost:8080/api/v1/";

    /**
     * Build and return a configured Grizzly HttpServer.
     * This is also called by integration tests (if any are added later).
     */
    public static HttpServer startServer() {
        // ResourceConfig is Jersey's programmatic equivalent of the
        // web.xml / @ApplicationPath configuration.
        ResourceConfig config = new ResourceConfig();

        // ---- Register resource classes -------------------------------- //
        config.register(DiscoveryResource.class);
        config.register(RoomResource.class);
        config.register(SensorResource.class);
        // Note: SensorReadingResource is NOT registered here because it is
        // instantiated dynamically by the sub-resource locator in SensorResource.

        // ---- Register exception mappers ------------------------------- //
        config.register(RoomNotEmptyExceptionMapper.class);
        config.register(LinkedResourceNotFoundExceptionMapper.class);
        config.register(SensorUnavailableExceptionMapper.class);
        config.register(GlobalExceptionMapper.class);

        // ---- Register filters ----------------------------------------- //
        config.register(LoggingFilter.class);

        // ---- Enable JSON support via Jackson -------------------------- //
        config.register(JacksonFeature.class);

        // Create and return the Grizzly HTTP server
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
    }

    public static void main(String[] args) throws Exception {
        final HttpServer server = startServer();

        LOGGER.info("=======================================================");
        LOGGER.info("  Smart Campus API started successfully!");
        LOGGER.info("  Base URL : " + BASE_URI);
        LOGGER.info("  Discovery: GET http://localhost:8080/api/v1/");
        LOGGER.info("  Rooms    : GET http://localhost:8080/api/v1/rooms");
        LOGGER.info("  Sensors  : GET http://localhost:8080/api/v1/sensors");
        LOGGER.info("  Press ENTER to stop the server.");
        LOGGER.info("=======================================================");

        // Keep the server alive until the user presses ENTER
        System.in.read();

        server.shutdown();
        LOGGER.info("Server stopped. Goodbye!");
    }
}

