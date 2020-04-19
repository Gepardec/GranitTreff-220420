package com.gepardec.samples.microprofile.opentracing;

import io.opentracing.Span;
import org.apache.commons.io.IOUtils;

import javax.json.bind.JsonbBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ClientSpanDecorator implements io.opentracing.contrib.jaxrs2.client.ClientSpanDecorator {

    @Override
    public void decorateRequest(ClientRequestContext requestContext, Span span) {
        String body = "";
        if (requestContext.hasEntity()) {
            body = serialize(requestContext.getEntity());
        }

        span.setBaggageItem("request.headers", serialize(requestContext.getHeaders()));
        span.setBaggageItem("request.body", body);
        span.setOperationName(String.format("[%s] %s",
                requestContext.getUri().getHost(),
                requestContext.getUri().getPath()));
    }

    @Override
    public void decorateResponse(ClientResponseContext responseContext, Span span) {
        String body = "";
        if (responseContext.hasEntity()) {
            body = readEntityFromInputStream(responseContext.getEntityStream());
            // We have read the stream, therefore we need to set it again
            responseContext.setEntityStream(new BufferedInputStream(new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8))));
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
