/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.mapper;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.util.ErrorResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Converts LinkedResourceNotFoundException into HTTP 422 Unprocessable Entity.
 *
 * 422 is semantically more accurate than 404 here because:
 *   - The request URI (/api/v1/sensors) is perfectly valid – 404 would be wrong.
 *   - The JSON payload itself is well-formed – 400 would be inaccurate.
 *   - The problem is that a referenced resource (roomId) does not exist, which
 *     makes the entity "unprocessable" from a business-logic standpoint.
 */
@Provider
public class LinkedResourceNotFoundExceptionMapper
        implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException ex) {
        ErrorResponse body = new ErrorResponse(
                422,
                "Unprocessable Entity",
                ex.getMessage()
        );
        return Response
                .status(422)                         // 422 – no named constant in JAX-RS 2.x
                .entity(body)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
