package com.dglbc;

import com.dglbc.transport.Server;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, InterruptedException {
        Server.start(Server.DEFAULT_PORT);
        while(true){
            Thread.sleep(1000);
        }
    }
}
