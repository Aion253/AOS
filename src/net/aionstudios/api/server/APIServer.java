package net.aionstudios.api.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

import net.aionstudios.api.context.ContextHandler;

/**
 * A class that creates the {@link APIServer} and sets up request handling.
 * 
 * @author Winter Roberts
 *
 */
public class APIServer {
	
	HttpServer server;
	int serverPort = 26767;
	private APISecureServer secureServer;
	
	/**
	 * Creates a new {@link APIServer}
	 * 
	 * @param port The port on which the server should start.
	 */
	public APIServer(int port) {
		secureServer = new APISecureServer();
		if(port<0||port>65535) {
			System.out.println("No valid server port, using default "+serverPort);
		} else {
			this.serverPort=port;
		}
		try {
			server = HttpServer.create(new InetSocketAddress(serverPort), 0);
		} catch (IOException e) {
			System.err.println("Failed to AOS Server!");
			e.printStackTrace();
			System.exit(0);
		}
		server.createContext("/", new ContextHandler());
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
		System.out.println("Server started on port " + port);
	}
	
	public APISecureServer getSecureServer() {
		return secureServer;
	}

}
