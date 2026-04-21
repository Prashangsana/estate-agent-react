/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resource;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.repository.DataStore;
import com.smartcampus.util.ErrorResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Sensor Resource – manages /api/v1/sensors
 *
 * -----------------------------------------------------------------------
 * REPORT QUESTION 3.1 – @Consumes and media-type mismatches
 *
 * @Consumes(MediaType.APPLICATION_JSON) tells JAX-RS that the POST method
 * ONLY accepts requests whose Content-Type header is "application/json".
 *
 * If a client sends text/plain or application/xml:
 *   1. JAX-RS inspects the incoming Content-Type header BEFORE invoking
 *      the method.
 *   2. It finds no matching resource method for that media type.
 *   3. It automatically returns HTTP 415 Unsupported Media Type.
 *   4. The resource method body is NEVER executed – the request is
 *      rejected at the JAX-RS layer, not in our code.
 *
 * This is a declarative, zero-code media-type enforcement mechanism that
 * keeps resource methods clean from manual content-type checks.
 * -----------------------------------------------------------------------
 *
 * REPORT QUESTION 3.2 – @QueryParam vs. @PathParam for filtering
 *
 * Query parameter approach (this implementation):
 *   GET /api/v1/sensors?type=CO2
 *
 *   + Optional by design – GET /api/v1/sensors still returns all sensors.
 *   + Multiple filters compose naturally: ?type=CO2&status=ACTIVE
 *   + Semantically correct: we are filtering a COLLECTION, not navigating
 *     to a different resource.
 *   + RESTful: the resource is still /sensors; the query string refines it.
 *
 * Path parameter alternative:
 *   GET /api/v1/sensors/type/CO2
 *
 *   - Implies CO2 is a sub-resource of sensors, which is incorrect.
 *   - Makes the "no filter" case awkward (you'd need a separate GET /sensors).
 *   - Cannot compose multiple filters without ugly path nesting.
 *   - Breaks REST resource identity: the same sensor collection is now
 *     reachable under two different URIs.
 *
 * Query parameters are the industry-standard choice for search and filter.
 * -----------------------------------------------------------------------
 *
 * REPORT QUESTION 4.1 – Sub-Resource Locator benefits
 *
 * Instead of defining @GET @Path("{sensorId}/readings") and
 * @POST @Path("{sensorId}/readings") directly in this class, we delegate
 * to a dedicated SensorReadingResource via a locator method.
 *
 * Benefits:
 *   1. Single Responsibility – SensorResource handles sensor CRUD;
 *      SensorReadingResource handles reading history. Each class has one job.
 *   2. Scalability – a large API with dozens of nested resources would
 *      become a 1000-line "god class" without this pattern.
 *   3. Testability – SensorReadingResource can be unit-tested in isolation.
 *   4. Reusability – the same sub-resource class could theoretically be
 *      mounted under different parent paths without duplication.
 *   5. Readability – each file is small, focused, and easy to navigate.
 * -----------------------------------------------------------------------
 */
@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private final DataStore store = DataStore.getInstance();

    // ------------------------------------------------------------------ //
    // GET /api/v1/sensors  (with optional ?type= filter)
    // ------------------------------------------------------------------ //

    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Sensor> allSensors = new ArrayList<>(store.getSensors().values());

        if (type != null && !type.isBlank()) {
            // Filter by type – case-insensitive comparison
            allSensors = allSensors.stream()
                    .filter(s -> type.equalsIgnoreCase(s.getType()))
                    .collect(Collectors.toList());
        }

        return Response.ok(allSensors).build();
    }

    // ------------------------------------------------------------------ //
    // POST /api/v1/sensors  – register a new sensor
    // ------------------------------------------------------------------ //

    @POST
    public Response createSensor(Sensor sensor) {
        if (sensor == null || sensor.getType() == null || sensor.getType().isBlank()) {
            ErrorResponse err = new ErrorResponse(400, "Bad Request",
                    "Sensor 'type' is required and must not be blank.");
            return Response.status(400).entity(err).build();
        }

        // Business rule: roomId must reference an existing room
        if (sensor.getRoomId() == null || sensor.getRoomId().isBlank()) {
            ErrorResponse err = new ErrorResponse(400, "Bad Request",
                    "Sensor 'roomId' is required.");
            return Response.status(400).entity(err).build();
        }

        if (!store.roomExists(sensor.getRoomId())) {
            // Throw 422 – the payload is valid JSON but references a non-existent room
            throw new LinkedResourceNotFoundException("Room", sensor.getRoomId());
        }

        // Auto-generate an ID if not provided
        if (sensor.getId() == null || sensor.getId().isBlank()) {
            String prefix = sensor.getType().toUpperCase().substring(0, Math.min(4, sensor.getType().length()));
            sensor.setId(prefix + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
        }

        // Duplicate ID check
        if (store.sensorExists(sensor.getId())) {
            ErrorResponse err = new ErrorResponse(409, "Conflict",
                    "A sensor with id '" + sensor.getId() + "' already exists.");
            return Response.status(409).entity(err).build();
        }

        // Default status to ACTIVE if not supplied
        if (sensor.getStatus() == null || sensor.getStatus().isBlank()) {
            sensor.setStatus("ACTIVE");
        }

        // Persist the sensor
        store.addSensor(sensor);

        // Link the sensor to its room
        Room room = store.getRoomById(sensor.getRoomId());
        room.getSensorIds().add(sensor.getId());

        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }

    // ------------------------------------------------------------------ //
    // GET /api/v1/sensors/{sensorId}  – get one sensor
    // ------------------------------------------------------------------ //

    @GET
    @Path("/{sensorId}")
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = store.getSensorById(sensorId);
        if (sensor == null) {
            ErrorResponse err = new ErrorResponse(404, "Not Found",
                    "Sensor with id '" + sensorId + "' was not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(err).build();
        }
        return Response.ok(sensor).build();
    }

    // ------------------------------------------------------------------ //
    // Sub-resource locator for /api/v1/sensors/{sensorId}/readings
    // ------------------------------------------------------------------ //

    /**
     * This is a SUB-RESOURCE LOCATOR METHOD.
     *
     * It does NOT carry @GET / @POST / @PUT / @DELETE.
     * JAX-RS sees a @Path annotation with no HTTP method annotation and
     * understands it should delegate further processing to the returned
     * object (SensorReadingResource).
     *
     * Jersey will then call the appropriate @GET or @POST method on the
     * returned SensorReadingResource instance to complete the request.
     */
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}
