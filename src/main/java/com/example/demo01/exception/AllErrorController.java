package com.example.demo01.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AllErrorController implements ErrorController {
    private static Logger logger = LoggerFactory.getLogger(AllErrorController.class);
    @Value("${error.path:/error}")
    private String errorPath;
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private ErrorAttributes errorAttributes = new DefaultErrorAttributes();

    public AllErrorController() {
    }

    @RequestMapping(
            value = {"${error.path:/error}"},
            produces = {"application/json;charset=UTF-8"}
    )
    @ResponseBody
    protected Object handle(HttpServletRequest request) {
        Map<String, Object> attributes = this.getErrorAttributes(request);
        Map<String, String> ret = new HashMap();
        ret.put("code", String.valueOf(attributes.get("status")));
        ret.put("message", (String)attributes.get("error"));
        this.logError(attributes, request);
        return ret;
    }

    protected Map<String, Object> getErrorAttributes(HttpServletRequest request) {
        WebRequest requestAttributes = new ServletWebRequest(request);
        return this.errorAttributes.getErrorAttributes(requestAttributes, false);
    }

    private void logError(Map<String, Object> attributes, HttpServletRequest request) {
        attributes.put("from", request.getRemoteAddr());

        try {
            logger.error(this.jsonMapper.writeValueAsString(attributes));
        } catch (JsonProcessingException var4) {
            var4.printStackTrace();
        }

    }

    public String getErrorPath() {
        return this.errorPath;
    }
}
