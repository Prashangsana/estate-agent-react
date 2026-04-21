/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Discovery endpoint – GET /api/v1
 *
 * Returns metadata about the API: name, version, contact, and a map of
 * available resource collections with their URIs.
 *
 * This implements a lightweight version of HATEOAS (Hypermedia As The
 * Engine of Application State). By embedding links to related resources
 * directly in the response, client developers can discover the API
 * dynamically without relying solely on external documentation.
 *
 * -----------------------------------------------------------------------
 * REPORT QUESTION: Why is HATEOAS considered a hallmark of advanced REST?
 *
 * HATEOAS means the server embeds navigation links inside every response,
 * making the API "self-documenting". Benefits:
 *
 *   1. Discoverability – a client can start at the root URL and follow
 *      links to any resource without ever reading external docs.
 *   2. Loose coupling – if a URI changes, the server updates the link;
 *      clients that follow links (rather than hardcoding paths) adapt
 *      automatically.
 *   3. Reduced cognitive load – client developers see exactly what
 *      actions and sub-resources are available from a given state.
 *   4. Evolvability – new resources can be added to the links map and
 *      existing clients will ignore unknown entries gracefully.
 *
 * Compared to static documentation (e.g. a PDF or Swagger spec that is
 * out-of-date), hypermedia-driven clients are resilient to server-side
 * changes because the source of truth lives in the API itself.
 * -----------------------------------------------------------------------
 */
@Path("/")      // Note: the base "/api/v1" is set by @ApplicationPath in SmartCampusApplication
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {

    @GET
    public Response discover() {
        Map<String, Object> response = new LinkedHashMap<>();

        // ---- API metadata -------------------------------------------- //
        response.put("apiName",     "Smart Campus API");
        response.put("version",     "1.0.0");
        response.put("description", "RESTful API for campus room and sensor management");
        response.put("status",      "operational");

        // ---- Administrative contact ---------------------------------- //
        Map<String, String> contact = new LinkedHashMap<>();
        contact.put("name",  "Smart Campus Infrastructure Team");
        contact.put("email", "smartcampus@university.ac.uk");
        contact.put("department", "School of Computer Science and Engineering");
        response.put("contact", contact);

        // ---- Available resource collections (HATEOAS links) ---------- //
        Map<String, String> resources = new LinkedHashMap<>();
        resources.put("rooms",   "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");
        response.put("resources", resources);

        // ---- Quick-start actions ------------------------------------- //
        Map<String, String> actions = new LinkedHashMap<>();
        actions.put("listRooms",    "GET  /api/v1/rooms");
        actions.put("createRoom",   "POST /api/v1/rooms");
        actions.put("listSensors",  "GET  /api/v1/sensors");
        actions.put("createSensor", "POST /api/v1/sensors");
        actions.put("filterSensors","GET  /api/v1/sensors?type={type}");
        response.put("actions", actions);

        return Response.ok(response).build();
    }
}
