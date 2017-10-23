package com.dglbc;

import com.dglbc.core.Receive;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws SocketException {
        DatagramSocket receSocket = new DatagramSocket(10002);
        new Thread(new Receive(receSocket)).start();
    }
}
