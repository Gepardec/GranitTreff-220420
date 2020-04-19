package com.gepardec.samples.microprofile.opentracing;

import io.opentracing.contrib.jaxrs2.client.ClientTracingFeature;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.annotation.RegisterProviders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

// Makes the rest client available via CDI and register a config key,
// so we can avoid to use fully qualified class names as config key prefixes.
@RegisterRestClient(configKey = "externalResource")
// Unfortunately MicroProfile-Rest-Client doesn't integrate with MicroProfile-Opentracing yet
// so we have to register the client tracer via an feature
@RegisterProviders(@RegisterProvider(ClientTracingFeature.class))
@Path("/")
public interface ExternalRestResource {

    @Path("/get")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    String get();

    @Path("/delete")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    String delete();

    @Path("/patch")
    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    String patch();

    @Path("/post")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    String post();

    @Path("/invalid")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    String invalid();
}
