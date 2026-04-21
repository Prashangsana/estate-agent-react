/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exception;

/**
 * Thrown when a request body references a resource ID that does not exist.
 *
 * Example: POST /sensors with a roomId that has no matching Room.
 *
 * Mapped to HTTP 422 Unprocessable Entity by LinkedResourceNotFoundExceptionMapper.
 * 422 is preferred over 404 because the URI itself is valid – the problem is
 * inside the request body (a broken foreign-key reference), not the URL.
 */
public class LinkedResourceNotFoundException extends RuntimeException {

    private final String resourceType; // e.g. "Room"
    private final String resourceId;   // e.g. "LIB-999"

    public LinkedResourceNotFoundException(String resourceType, String resourceId) {
        super(resourceType + " with id '" + resourceId + "' does not exist. "
              + "Please create the " + resourceType + " first.");
        this.resourceType = resourceType;
        this.resourceId   = resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }
}
