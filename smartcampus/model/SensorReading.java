/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.model;

/**
 * Represents a single timestamped measurement captured by a Sensor.
 *
 * id        – UUID string assigned at creation time
 * timestamp – epoch milliseconds (System.currentTimeMillis())
 * value     – the actual metric value recorded by the hardware
 */
public class SensorReading {

    private String id;        // UUID e.g. "a3f9c2d1-..."
    private long timestamp;   // Epoch ms
    private double value;     // The recorded measurement

    // ------------------------------------------------------------------ //
    // Constructors
    // ------------------------------------------------------------------ //

    /** No-arg constructor required by Jackson. */
    public SensorReading() {}

    public SensorReading(String id, long timestamp, double value) {
        this.id = id;
        this.timestamp = timestamp;
        this.value = value;
    }

    // ------------------------------------------------------------------ //
    // Getters & Setters
    // ------------------------------------------------------------------ //

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
