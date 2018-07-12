package net.aionstudios.api.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

import net.aionstudios.api.context.ContextHandler;

public class APIServer {
	
	HttpServer server;
	int serverPort = 26767;
	
	public APIServer(int port) throws IOException {
		if(port<0||port>65535) {
			System.out.println("No valid server port, using default "+serverPort);
		} else {
			this.serverPort=port;
		}
		server = HttpServer.create(new InetSocketAddress(serverPort), 0);
		server.createContext("/", new ContextHandler());
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
		System.out.println("Server started on port " + port);
	}

}
