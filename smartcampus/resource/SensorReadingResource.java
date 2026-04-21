/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resource;

import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.repository.DataStore;
import com.smartcampus.util.ErrorResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

/**
 * Sub-resource for sensor readings.
 *
 * This class is NOT directly annotated with @Path at the class level.
 * Instead, it is instantiated and returned by a sub-resource locator
 * method inside SensorResource. JAX-RS then uses reflection to find
 * the @GET and @POST methods here.
 *
 * Base path (set by the locator): /api/v1/sensors/{sensorId}/readings
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String    sensorId;
    private final DataStore store = DataStore.getInstance();

    /**
     * The locator in SensorResource passes the resolved sensorId so this
     * class always knows which sensor it belongs to.
     */
    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    // ------------------------------------------------------------------ //
    // GET /api/v1/sensors/{sensorId}/readings  – reading history
    // ------------------------------------------------------------------ //

    @GET
    public Response getReadings() {
        // Confirm the sensor exists first
        Sensor sensor = store.getSensorById(sensorId);
        if (sensor == null) {
            ErrorResponse err = new ErrorResponse(404, "Not Found",
                    "Sensor with id '" + sensorId + "' was not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(err).build();
        }

        List<SensorReading> history = store.getReadingsForSensor(sensorId);
        return Response.ok(history).build();
    }

    // ------------------------------------------------------------------ //
    // POST /api/v1/sensors/{sensorId}/readings  – add a new reading
    // ------------------------------------------------------------------ //

    @POST
    public Response addReading(SensorReading reading) {
        // Confirm the sensor exists
        Sensor sensor = store.getSensorById(sensorId);
        if (sensor == null) {
            ErrorResponse err = new ErrorResponse(404, "Not Found",
                    "Sensor with id '" + sensorId + "' was not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(err).build();
        }

        // Business rule: sensors in MAINTENANCE status cannot accept readings
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())
                || "OFFLINE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(sensorId, sensor.getStatus());
            // → caught by SensorUnavailableExceptionMapper → HTTP 403
        }

        // Validate that a value was provided
        if (reading == null) {
            ErrorResponse err = new ErrorResponse(400, "Bad Request",
                    "Request body must contain a SensorReading with a 'value' field.");
            return Response.status(400).entity(err).build();
        }

        // Auto-assign id and timestamp if not provided
        if (reading.getId() == null || reading.getId().isBlank()) {
            reading.setId(UUID.randomUUID().toString());
        }
        if (reading.getTimestamp() == 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }

        // Persist the reading
        store.addReading(sensorId, reading);

        // Side-effect: update the parent sensor's currentValue
        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED).entity(reading).build();
    }
}

