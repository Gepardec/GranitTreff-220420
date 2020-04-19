package com.gepardec.samples.microprofile.opentracing;

import io.opentracing.contrib.jaxrs2.client.ClientTracingFeature;
import io.opentracing.util.GlobalTracer;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.opentracing.Traced;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@RequestScoped
@Path("/custom")
@Traced
public class CustomRestResource {

    private static final String EXTERNAL_RESOURCE_KEY_URL = "externalResource/mp-rest/url";

    @Inject
    private Config config;

    @Path("/get")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get() {
        return createRestClient().get();
    }

    @Path("/delete")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String delete() {
        return createRestClient().delete();
    }

    @Path("/patch")
    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    public String patch() {
        return createRestClient().patch();
    }

    @Path("/post")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String post() {
        return createRestClient().post();
    }

    /**
     * @return the rest client manually built with manually built tracing feature
     */
    private ExternalRestResource createRestClient() {
        try {
            final String url = config.getOptionalValue(EXTERNAL_RESOURCE_KEY_URL, String.class)
                    .orElseThrow(() -> new IllegalStateException(String.format("Configuration property '%s' not defined", EXTERNAL_RESOURCE_KEY_URL)));
            final ClientTracingFeature feature = createClientTracingFeature();
            return RestClientBuilder.newBuilder()
                    .baseUrl(new URL(url))
                    .readTimeout(2000, TimeUnit.SECONDS)
                    .connectTimeout(2000, TimeUnit.SECONDS)
                    .register(feature)
                    .build(ExternalRestResource.class);
        } catch (MalformedURLException e) {
            throw new RuntimeException("URI is not valid");
        }
    }

    /**
     * @return the configured ClientTracingFeature instance with ClientSpanDecorator registered
     */
    private ClientTracingFeature createClientTracingFeature() {
        return new ClientTracingFeature.Builder(GlobalTracer.get())
                .withTraceSerialization(false)
                .withDecorators(Collections.singletonList(new ClientSpanDecorator()))
                .build();
    }
}
