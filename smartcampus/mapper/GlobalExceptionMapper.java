/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.mapper;

import com.smartcampus.util.ErrorResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Global safety-net mapper.
 *
 * Catches ANY throwable that is not caught by a more specific mapper
 * (e.g. NullPointerException, IndexOutOfBoundsException) and converts it
 * into a clean HTTP 500 response with NO stack trace in the body.
 *
 * -----------------------------------------------------------------------
 * SECURITY NOTE (coursework report question):
 * Exposing raw Java stack traces to API consumers is a serious security
 * risk because they reveal:
 *   1. Internal class/package names – lets attackers map the codebase.
 *   2. Library versions – helps identify known CVEs.
 *   3. File paths & line numbers – pinpoints where to look for logic bugs.
 *   4. Business logic clues – e.g., "DataStore.java:142" reveals the
 *      storage mechanism and potentially exploitable assumptions.
 * This mapper ensures the external response is always a generic message
 * while the full trace is written to the SERVER log only.
 * -----------------------------------------------------------------------
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable ex) {
        // Log the full stack trace server-side so developers can debug
        LOGGER.log(Level.SEVERE, "Unhandled exception caught by GlobalExceptionMapper", ex);

        // Return a safe, generic error to the client
        ErrorResponse body = new ErrorResponse(
                500,
                "Internal Server Error",
                "An unexpected error occurred on the server. Please contact the administrator."
        );
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)  // 500
                .entity(body)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
