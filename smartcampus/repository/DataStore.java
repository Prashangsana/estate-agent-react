/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.repository;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Shared in-memory data store for the Smart Campus API.
 *
 * -----------------------------------------------------------------------
 * WHY A SINGLETON?
 * -----------------------------------------------------------------------
 * By default, JAX-RS creates a NEW instance of every @Path resource class
 * for each incoming HTTP request (request-scoped lifecycle). This means if
 * we stored data inside a resource class, it would be lost after every
 * request. Instead, we use a classic Singleton so that all resource
 * instances share the SAME maps across the lifetime of the server process.
 *
 * -----------------------------------------------------------------------
 * THREAD SAFETY NOTE (mentioned in coursework report question)
 * -----------------------------------------------------------------------
 * In a real production system, multiple threads may call get() on the
 * singleton simultaneously and modify the same Map, causing a race
 * condition (e.g. two POST /rooms requests assigning the same ID).
 * A production fix would be to use ConcurrentHashMap and atomic ID
 * generation, or synchronised blocks, or a proper database.
 * For this coursework the single-threaded test workload is safe enough,
 * but the concern is acknowledged in the report.
 * -----------------------------------------------------------------------
 */
public class DataStore {

    // ---- Singleton boilerplate ---------------------------------------- //

    private static final DataStore INSTANCE = new DataStore();

    private DataStore() {
        seedData(); // Pre-populate with sample data for demo purposes
    }

    public static DataStore getInstance() {
        return INSTANCE;
    }

    // ---- Storage maps ------------------------------------------------- //

    // LinkedHashMap preserves insertion order for consistent list responses
    private final Map<String, Room>          rooms    = new LinkedHashMap<>();
    private final Map<String, Sensor>        sensors  = new LinkedHashMap<>();
    // readings keyed by sensorId -> list of readings for that sensor
    private final Map<String, List<SensorReading>> readings = new LinkedHashMap<>();

    // ------------------------------------------------------------------ //
    // Room operations
    // ------------------------------------------------------------------ //

    public Map<String, Room> getRooms() {
        return rooms;
    }

    public Room getRoomById(String id) {
        return rooms.get(id);
    }

    public void addRoom(Room room) {
        rooms.put(room.getId(), room);
    }

    public boolean roomExists(String id) {
        return rooms.containsKey(id);
    }

    public void deleteRoom(String id) {
        rooms.remove(id);
    }

    // ------------------------------------------------------------------ //
    // Sensor operations
    // ------------------------------------------------------------------ //

    public Map<String, Sensor> getSensors() {
        return sensors;
    }

    public Sensor getSensorById(String id) {
        return sensors.get(id);
    }

    public void addSensor(Sensor sensor) {
        sensors.put(sensor.getId(), sensor);
        // Initialise an empty reading list for this sensor
        readings.putIfAbsent(sensor.getId(), new ArrayList<>());
    }

    public boolean sensorExists(String id) {
        return sensors.containsKey(id);
    }

    // ------------------------------------------------------------------ //
    // SensorReading operations
    // ------------------------------------------------------------------ //

    public List<SensorReading> getReadingsForSensor(String sensorId) {
        return readings.getOrDefault(sensorId, new ArrayList<>());
    }

    public void addReading(String sensorId, SensorReading reading) {
        readings.computeIfAbsent(sensorId, k -> new ArrayList<>()).add(reading);
    }

    // ------------------------------------------------------------------ //
    // Seed data (so Postman demos work immediately without setup POSTs)
    // ------------------------------------------------------------------ //

    private void seedData() {
        // Rooms
        Room r1 = new Room("LIB-301", "Library Quiet Study", 50);
        Room r2 = new Room("LAB-101", "Computer Science Lab", 30);
        rooms.put(r1.getId(), r1);
        rooms.put(r2.getId(), r2);

        // Sensors
        Sensor s1 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 22.5, "LIB-301");
        Sensor s2 = new Sensor("CO2-001",  "CO2",         "ACTIVE", 415.0, "LIB-301");
        Sensor s3 = new Sensor("OCC-001",  "Occupancy",   "MAINTENANCE", 0.0, "LAB-101");

        sensors.put(s1.getId(), s1);
        sensors.put(s2.getId(), s2);
        sensors.put(s3.getId(), s3);

        // Link sensors to their rooms
        r1.getSensorIds().add(s1.getId());
        r1.getSensorIds().add(s2.getId());
        r2.getSensorIds().add(s3.getId());

        // Initialise reading history lists
        readings.put(s1.getId(), new ArrayList<>());
        readings.put(s2.getId(), new ArrayList<>());
        readings.put(s3.getId(), new ArrayList<>());
    }
}
