package com.gepardec.samples.microprofile.opentracing;

import io.opentracing.Span;
import org.apache.commons.io.IOUtils;

import javax.json.bind.JsonbBuilder;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ServerSpanDecorator implements io.opentracing.contrib.jaxrs2.server.ServerSpanDecorator {

    @Override
    public void decorateRequest(ContainerRequestContext requestContext, Span span) {
        String body = "";
        if (requestContext.hasEntity()) {
            body = serialize(requestContext.getEntityStream());
        }

        span.setBaggageItem("request.headers", serialize(requestContext.getHeaders()));
        span.setBaggageItem("request.body", body);
        span.setOperationName(requestContext.getUriInfo().getPath());

    }

    @Override
    public void decorateResponse(ContainerResponseContext responseContext, Span span) {
        String body = "";
        if (responseContext.hasEntity()) {
            body = serialize(responseContext.getEntity());
        }

        span.setBaggageItem("response.body", body);
    }

    private String readEntityFromInputStream(final InputStream is) {
        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            IOUtils.copy(is, bos);

            return new String(bos.toByteArray(), StandardCharsets.UTF_8);
        } catch (Throwable e) {
            return "not_readable: " + e.getMessage();
        }
    }

    private String serialize(Object obj) {
        if (obj == null) {
            return "";
        }
        return JsonbBuilder.create().toJson(obj);
    }
}
