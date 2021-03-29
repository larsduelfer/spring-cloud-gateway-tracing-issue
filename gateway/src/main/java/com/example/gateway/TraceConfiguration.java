/*
 * *************************************************************************
 *
 * Copyright:       Robert Bosch GmbH, 2018
 *
 * *************************************************************************
 */

package com.example.gateway;

import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.TraceContext;
import brave.sampler.Sampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TraceConfiguration {

    private static final String UUID_REGEX =
            "\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b";

    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }

    @Bean
    public SpanHandler spanNameAdjuster() {
        return new SpanHandler() {
            @Override
            public boolean end(TraceContext context, MutableSpan span, Cause cause) {
                String httpPath = span.tag("http.path");
                if (httpPath != null) {
                    span.name(span.name() + " " + httpPath.replaceAll(UUID_REGEX, "{id}"));
                }
                return true;
            }
        };
    }
}
