/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resource;

import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.model.Room;
import com.smartcampus.repository.DataStore;
import com.smartcampus.util.ErrorResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Room Resource – manages /api/v1/rooms
 *
 * -----------------------------------------------------------------------
 * REPORT QUESTION 2.1 – IDs vs. full objects in list responses
 *
 * Returning FULL room objects in a list:
 *   + Client has everything it needs in one round trip.
 *   + No follow-up GET /{id} requests required.
 *   - Higher payload size – wastes bandwidth when the client only needs IDs.
 *   - Larger responses are slower to parse on low-powered mobile clients.
 *
 * Returning IDs ONLY:
 *   + Tiny payload, ideal for high-volume polling scenarios.
 *   - Client must issue N follow-up requests to get details – "N+1 problem".
 *
 * Best practice for this coursework: return full objects for the list so
 * clients can render a room directory without extra round trips.
 * -----------------------------------------------------------------------
 *
 * REPORT QUESTION 2.2 – DELETE idempotency
 *
 * REST specifies that DELETE should be IDEMPOTENT: repeating the same
 * request must produce the same observable server state.
 *
 * In this implementation:
 *   - First DELETE /rooms/LIB-301 → room is removed → 200 OK.
 *   - Second DELETE /rooms/LIB-301 → room not found  → 404 Not Found.
 *
 * The server STATE is idempotent (the room is gone after the first call
 * and stays gone), but the HTTP STATUS CODE changes, which means this
 * implementation is "state-idempotent but not strictly response-idempotent".
 * Strictly speaking, returning 404 on repeated deletes is the most common
 * real-world approach and is accepted by the REST community.
 * -----------------------------------------------------------------------
 */
@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private final DataStore store = DataStore.getInstance();

    // ------------------------------------------------------------------ //
    // GET /api/v1/rooms  – list all rooms
    // ------------------------------------------------------------------ //

    @GET
    public Response getAllRooms() {
        Collection<Room> allRooms = store.getRooms().values();
        List<Room> roomList = new ArrayList<>(allRooms);
        return Response.ok(roomList).build();
    }

    // ------------------------------------------------------------------ //
    // POST /api/v1/rooms  – create a new room
    // ------------------------------------------------------------------ //

    @POST
    public Response createRoom(Room room) {
        // Validate required fields
        if (room == null || room.getName() == null || room.getName().isBlank()) {
            ErrorResponse err = new ErrorResponse(400, "Bad Request",
                    "Room 'name' is required and must not be blank.");
            return Response.status(400).entity(err).build();
        }

        // Auto-generate an ID if the client did not provide one
        if (room.getId() == null || room.getId().isBlank()) {
            // Generate a short, readable ID based on name
            String base = room.getName().toUpperCase().replaceAll("\\s+", "-");
            room.setId(base + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
        }

        // Check for duplicate ID
        if (store.roomExists(room.getId())) {
            ErrorResponse err = new ErrorResponse(409, "Conflict",
                    "A room with id '" + room.getId() + "' already exists.");
            return Response.status(409).entity(err).build();
        }

        // Ensure sensorIds list is never null
        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }

        store.addRoom(room);

        // Return 201 Created with the full room object
        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    // ------------------------------------------------------------------ //
    // GET /api/v1/rooms/{roomId}  – get a specific room by ID
    // ------------------------------------------------------------------ //

    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = store.getRoomById(roomId);
        if (room == null) {
            ErrorResponse err = new ErrorResponse(404, "Not Found",
                    "Room with id '" + roomId + "' was not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(err).build();
        }
        return Response.ok(room).build();
    }

    // ------------------------------------------------------------------ //
    // DELETE /api/v1/rooms/{roomId}  – delete a room
    // ------------------------------------------------------------------ //

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = store.getRoomById(roomId);

        // 404 if the room doesn't exist
        if (room == null) {
            ErrorResponse err = new ErrorResponse(404, "Not Found",
                    "Room with id '" + roomId + "' was not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(err).build();
        }

        // Business rule: cannot delete a room that still has sensors assigned
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(roomId, room.getSensorIds().size());
            // → caught by RoomNotEmptyExceptionMapper → HTTP 409
        }

        store.deleteRoom(roomId);

        // Return 200 OK with a confirmation message
        java.util.Map<String, String> body = new java.util.LinkedHashMap<>();
        body.put("message", "Room '" + roomId + "' has been successfully deleted.");
        body.put("deletedId", roomId);
        return Response.ok(body).build();
    }
}
