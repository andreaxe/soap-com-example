package com.example.tdxassist.demo.server;

import javax.xml.ws.Endpoint;

/**
 * @author myname
 */
public class Main {
    public static void main(String[] args) {
        Endpoint.publish("http://0.0.0.0:9000/ws/request", new Request());
    }
}
