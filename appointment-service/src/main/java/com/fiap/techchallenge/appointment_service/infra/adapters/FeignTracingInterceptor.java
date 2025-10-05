package com.fiap.techchallenge.appointment_service.infra.adapters;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class FeignTracingInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        var attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null)
            return;
        HttpServletRequest request = attrs.getRequest();
        if (request == null)
            return;

        String xReqId = request.getHeader("x-request-id");
        if (xReqId != null)
            template.header("x-request-id", xReqId);

        String traceparent = request.getHeader("traceparent");
        if (traceparent != null)
            template.header("traceparent", traceparent);

        String xB3 = request.getHeader("x-b3-traceid");
        if (xB3 != null)
            template.header("x-b3-traceid", xB3);

    }
}