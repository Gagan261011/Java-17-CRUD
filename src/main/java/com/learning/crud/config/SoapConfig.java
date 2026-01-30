package com.learning.crud.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

/**
 * SOAP CONFIGURATION: Sets up SOAP web services
 * 
 * This class configures:
 * 1. SOAP endpoint URL
 * 2. WSDL generation
 * 3. XSD schema mapping
 * 
 * @Configuration tells Spring: "This class contains configuration"
 * @EnableWs enables Spring Web Services (SOAP)
 */
@Configuration
@EnableWs
public class SoapConfig extends WsConfigurerAdapter {

    /**
     * Configures MessageDispatcherServlet for SOAP
     * 
     * This servlet handles all SOAP requests.
     * URL pattern: /ws/*
     * 
     * So SOAP requests go to: http://localhost:8080/ws
     * 
     * @Bean tells Spring: "Create and manage this object"
     */
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
            ApplicationContext applicationContext) {
        
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    /**
     * Generates WSDL (Web Service Description Language)
     * 
     * WSDL = Documentation for SOAP service
     * It tells clients:
     * - What operations are available
     * - What data to send
     * - What data to expect back
     * 
     * Access WSDL at: http://localhost:8080/ws/students.wsdl
     * 
     * Parameters explained:
     * - portTypeName: Name of the SOAP service
     * - locationUri: Base URL for SOAP endpoint
     * - targetNamespace: Unique identifier (must match XSD)
     */
    @Bean(name = "students")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema studentsSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("StudentsPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://learning.com/crud/soap");
        wsdl11Definition.setSchema(studentsSchema);
        return wsdl11Definition;
    }

    /**
     * Loads XSD schema from resources
     * 
     * XSD defines the structure of SOAP messages
     * Location: src/main/resources/xsd/students.xsd
     */
    @Bean
    public XsdSchema studentsSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/students.xsd"));
    }
}
