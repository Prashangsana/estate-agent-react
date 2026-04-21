/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * JAX-RS filter that provides request and response observability.
 *
 * Implements BOTH ContainerRequestFilter (runs before resource method)
 * and ContainerResponseFilter (runs after resource method returns).
 *
 * -----------------------------------------------------------------------
 * WHY FILTERS INSTEAD OF Logger.info() IN EVERY RESOURCE METHOD?
 * (coursework report question)
 *
 * 1. Single Responsibility – resource methods handle business logic;
 *    filters handle cross-cutting concerns like logging, auth, CORS.
 * 2. DRY (Don't Repeat Yourself) – one filter covers every endpoint
 *    automatically, no manual insertion required.
 * 3. Consistency – every request is guaranteed to be logged; a developer
 *    cannot accidentally forget to add a Logger call to a new endpoint.
 * 4. Maintainability – changing the log format means editing one class,
 *    not dozens of resource files.
 * 5. Separation of concerns – business and infrastructure code stay clean
 *    and independently testable.
 * -----------------------------------------------------------------------
 */
@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());

    /**
     * Runs BEFORE the matched resource method executes.
     * Logs the HTTP method and full request URI.
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String method = requestContext.getMethod();
        String uri    = requestContext.getUriInfo().getRequestUri().toString();
        LOGGER.info(String.format("[REQUEST]  --> %s %s", method, uri));
    }

    /**
     * Runs AFTER the resource method has produced a Response.
     * Logs the HTTP status code returned to the client.
     */
    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {
        int    status = responseContext.getStatus();
        String method = requestContext.getMethod();
        String uri    = requestContext.getUriInfo().getRequestUri().toString();
        LOGGER.info(String.format("[RESPONSE] <-- %d  (%s %s)", status, method, uri));
    }
}
