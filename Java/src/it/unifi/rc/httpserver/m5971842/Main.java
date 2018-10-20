package it.unifi.rc.httpserver.m5971842;

import java.util.Iterator;
import java.util.LinkedList;
import java.net.InetAddress;

import it.unifi.rc.httpserver.m5971842.*;
import it.unifi.rc.httpserver.HTTPHandler;

public class Main{
  public static void main(String args[]){
      InetAddress addr = InetAddress.getByName("127.0.0.1");
      HTTPHandler handler = new GET_HTTPHandler1_0(new File("www"));
      MyHTTPServer server = new MyHTTPServer(9999, 10, addr, handler);
      server.start();
  }
}
