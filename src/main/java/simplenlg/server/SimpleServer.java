/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is "Simplenlg".
 *
 * The Initial Developer of the Original Code is Ehud Reiter, Albert Gatt and Dave Westwater.
 * Portions created by Ehud Reiter, Albert Gatt and Dave Westwater are Copyright (C) 2010-11 The University of Aberdeen. All Rights Reserved.
 *
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Westwater,
 * Roman Kutlak, Margaret Mitchell, and Saad Mahamood.
 */
package simplenlg.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Properties;

import simplenlg.xmlrealiser.XMLRealiser;
import simplenlg.xmlrealiser.XMLRealiser.LexiconType;

/**
 * SimpleServer is a program that realises xml requests.
 * 
 * @author Roman Kutlak.
 *
 * The program listens on a socket for client connections. 
 * When a client connects, the server creates an instance 
 * of RealisationRequest that serves the client.
 * 
 * The RealisationRequest parses the xml structure and
 * sends back corresponding surface string.
 * 
 * The server port can be specified as the first parameter
 * of the program; 50007 is used by default.
 * 
 * Typing exit will terminate the server.
 */
public class SimpleServer implements Runnable {

    private ServerSocket serverSocket;

    /**
     * Set to true to enable printing debug messages.
     */
    static boolean DEBUG = false;
    
    /**
     * This path should be replaced by the path to the specialist lexicon.
     * If there is an entry for DB_FILENAME in lexicon.properties, that path
     * will be searched for the lexicon file. Otherwise, the path below will
     * be used.
     */
    String lexiconPath = "src/main/resources/NIHLexicon/lexAccess2011.data";

    // control the run loop
    private boolean isActive = true;
    
    /**
     * Construct a new server.
     * 
     * @param port
     *      the port on which to listen
     *      
     * @throws IOException
     */
    public SimpleServer(int port) throws IOException {
        startServer(new ServerSocket(port, 8));
    }
   
    /**
     * Construct a server with a pre-allocated socket.
     * @param socket
     * @throws IOException
     */
    public SimpleServer(ServerSocket socket) throws IOException {
    	startServer(socket);
    }
    

	/**
	 * startServer -- Start's the SimpleServer with a created ServerSocket.
	 * @param socket -- The socket for the server to use.
	 * @throws IOException
	 * @throws SocketException
	 */
	private void startServer(ServerSocket socket) throws IOException, SocketException {
		serverSocket = socket;
        serverSocket.setReuseAddress(true);
        serverSocket.setSoTimeout(0);
        
        System.out.println("Port Number used by Server is: " + serverSocket.getLocalPort());
        
        // try to read the lexicon path from lexicon.properties file
        try {
            Properties prop = new Properties();
            FileReader reader = new FileReader(new File("./src/main/resources/lexicon.properties"));
            prop.load(reader);
            
            String dbFile = prop.getProperty("DB_FILENAME");
            
            if (null != dbFile)
                lexiconPath = dbFile;
            else
                throw new Exception("No DB_FILENAME in lexicon.properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("Server is using the following lexicon: "
                           + lexiconPath);
        
        XMLRealiser.setLexicon(LexiconType.NIHDB, this.lexiconPath);
	}

    static void print(Object o) {
        System.out.println(o);
    }

    /**
     * Terminate the server. The server can be started
     * again by invoking the <code>run()</code> method.
     */
    public void terminate() {
        this.isActive = false;
    }

    
    /**
     * Start the server.
     * 
     * The server will listen on the port specified at construction
     * until terminated by calling the <code>terminate()</code> method.
     * 
     * Note that the <code>exit()</code> and <code>exit(int)</code>
     * methods will terminate the program by calling System.exit().
     */
    public void run() {
        try {
            while (this.isActive) {
                try {
                    if (DEBUG) {
                        System.out.println("Waiting for client on port " +
                                serverSocket.getLocalPort() + "...");
                    }
                    
                    Socket clientSocket = serverSocket.accept();
                    handleClient(clientSocket);
                } catch (SocketTimeoutException s) {
                    System.err.println("Socket timed out!");
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this.serverSocket.close();
            } catch (Exception ee) {
                System.err.println("Could not close socket!");
            }
        }
    }

    /**
     * Handle the incoming client connection by constructing 
     * a <code>RealisationRequest</code> and starting it in a thread.
     * 
     * @param socket
     *          the socket on which the client connected
     */
    protected void handleClient(Socket socket) {
        if (null == socket)
            return;

        Thread request = new Thread(new RealisationRequest(socket));
        request.setDaemon(true);
        request.start();
    }

    /**
     * Perform shutdown routines.
     */
    synchronized public void shutdown() {
        System.out.println("Server shutting down.");
        
        terminate();
        // cleanup...like close log, etc.
    }

    /**
     * Exit the program without error.
     */
    synchronized public void exit() {
        exit(0);
    }

    /**
     * Exit the program signalling an error code.
     * 
     * @param code
     *          Error code; 0 means no error
     */
    synchronized public void exit(int code) {
        System.exit(code);
    }

    /**
     * The main method that starts the server.
     * 
     * The program takes one optional parameter,
     * which is the port number on which to listen.
     * The default value is 50007.
     * 
     * Once the program starts, it can be terminated
     * by typing the command 'exit'
     * 
     * @param args
     *          Program arguments
     */
    public static void main(String[] args) {
        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            port = 50007;
        }

        try {
            SimpleServer serverapp = new SimpleServer(port);

            Thread server = new Thread(serverapp);
            server.setDaemon(true);
            server.start();

            // allow the user to terminate the server by typing "exit"
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

            while (true) {
                try {
                    bw.write(":>");
                    bw.flush();
                    String input = br.readLine();

                    if (null != input && input.compareToIgnoreCase("exit") == 0) {
                        serverapp.shutdown();
                        serverapp.exit();
                    }
                } catch (IOException e) {
                    serverapp.shutdown();
                    e.printStackTrace();
                    serverapp.exit(-1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
