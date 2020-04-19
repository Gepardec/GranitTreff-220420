package com.gepardec.samples.microprofile.opentracing;

import io.opentracing.contrib.jaxrs2.server.ServerTracingDynamicFeature;
import io.opentracing.util.GlobalTracer;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.util.Collections;

@Provider
public class TracingInitializerFeature implements DynamicFeature {

    private static DynamicFeature tracingFeature = new ServerTracingDynamicFeature.Builder(GlobalTracer.get())
            .withDecorators(Collections.singletonList(new ServerSpanDecorator()))
            .withSerializationDecorators(Collections.emptyList())
            .withTraceSerialization(false)
            .withJoinExistingActiveSpan(false)
            .build();

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        tracingFeature.configure(resourceInfo, context);
    }
}
