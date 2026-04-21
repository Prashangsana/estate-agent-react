/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.config;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * JAX-RS Application bootstrap class.
 *
 * @ApplicationPath("/api/v1") sets the base URI for every resource in this
 * application. Jersey discovers all @Path-annotated classes automatically
 * when getClasses() / getSingletons() are left empty (package-scan mode
 * is configured in Main.java instead, which gives us more control in the
 * Grizzly environment).
 */
@ApplicationPath("/api/v1")
public class SmartCampusApplication extends Application {
    // Resource classes, providers and filters are registered
    // programmatically in Main.java via ResourceConfig for clarity
    // and NetBeans / Grizzly compatibility.
}

