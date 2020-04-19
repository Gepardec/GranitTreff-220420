package com.gepardec.samples.microprofile.opentracing;

import org.eclipse.microprofile.opentracing.Traced;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/")
@Traced
public class RestResource {

    // Qualified injection of the type safe rest client
    @Inject
    @RestClient
    ExternalRestResource restResource2;

    @Path("/get")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get() {
        return restResource2.get();
    }

    @Path("/delete")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String delete() {
        return restResource2.delete();
    }

    @Path("/patch")
    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    public String patch() {
        return restResource2.patch();
    }

    @Path("/post")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String post() {
        return restResource2.post();
    }
}
