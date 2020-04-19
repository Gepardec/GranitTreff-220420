package com.gepardec.samples.microprofile.opentracing;

import io.jaegertracing.Configuration;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

/**
 * With this factory we provide our own Tracer because it seems smallrye doesn't register the tracer on the GlobalTracer.
 */
public class TracerFactory implements io.opentracing.contrib.tracerresolver.TracerFactory {

    @Override
    public Tracer getTracer() {
        final Configuration configuration = Configuration.fromEnv();
        final Tracer tracer = configuration.getTracer();
        GlobalTracer.register(tracer);
        return tracer;
    }
}
