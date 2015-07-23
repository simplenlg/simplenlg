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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Wewstwater,
 * Roman Kutlak, Margaret Mitchell.
 */
package simplenlg.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.Socket;

import simplenlg.xmlrealiser.XMLRealiser;
import simplenlg.xmlrealiser.XMLRealiserException;

/**
 * This class handles one realisation request.
 * @author Roman Kutlak
 *
 * The object will parse the xml from the client socket and return 
 * a surface realisation of the xml structure.
 * 
 * If an exception occurs, the request attempts to inform the client
 * by sending a string that starts with "Exception: " and continues
 * with the message of the exception. Sending the error might fail
 * (for example, if the client disconnected).
 * 
 * The program implements the "standard" socket protocol:
 * each message is preceded with an integer indicating the
 * length of the message (int is 4 bytes).
 */
public class RealisationRequest implements Runnable {

    Socket socket;
    
    static boolean DEBUG = SimpleServer.DEBUG;
    
    public RealisationRequest(Socket s) {
        this.socket = s;
    }
    
    public void run() {
        if (null == socket)
            return;

        DataInputStream input = null;
        DataOutputStream output = null;

        if (DEBUG) {
            System.out.println("Client connected from "
                    + socket.getRemoteSocketAddress());
        }
        
        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            // read the message length
            int msgLen = input.readInt();
            
            // create a buffer
            byte[] data = new byte[msgLen];
            // read the entire message (blocks until complete)
            input.readFully(data);

            if (data.length < 1) {
                throw new Exception("Client did not send data.");
            } 
            
            // now convert the raw bytes to utf-8
            String tmp = new String(data, "UTF-8");
            StringReader reader = new StringReader(tmp);
            
            // get the realisation
            String result = doRealisation(reader).trim();
            
            // convert the string to raw bytes
            byte[] tmp2 = result.getBytes("UTF-8");
            
            // write the length
            output.writeInt(tmp2.length);
            // write the data
            output.write(tmp2);
            
            if (DEBUG) {
                String text = "The following realisation was sent to client:";
                System.out.println(text + "\n\t" + result);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            try {
                // attempt to send the error message to the client
                byte[] tmp = ("Exception: " + e.getMessage()).getBytes("UTF-8");
                output.writeInt(tmp.length);
                output.write(tmp);
            } catch (IOException e1) {
            }
        } catch (XMLRealiserException e) {
            e.printStackTrace();
            try {
                // attempt to send the error message to the client
                byte[] tmp = ("Exception: " + e.getMessage()).getBytes("UTF-8");
                output.writeInt(tmp.length);
                output.write(tmp);
            } catch (IOException e1) {
            }
        } finally {
            try {
                socket.close();
                socket = null;
            } catch (IOException e) {
                System.err.println("Could not close client socket!");
            }
        }
    }

    protected String doRealisation(Reader inputReader) throws XMLRealiserException {
        simplenlg.xmlrealiser.wrapper.RequestType request =
                XMLRealiser.getRequest(inputReader);
        String output = XMLRealiser.realise(request.getDocument());
        return output;
    }
}
