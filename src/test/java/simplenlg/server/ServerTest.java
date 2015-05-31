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
 * Roman Kutlak, Margaret Mitchell, Saad Mahamood.
 */

package simplenlg.server;

import java.net.ServerSocket;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for simplenlg server
 *
 * @author Roman Kutlak
 */
public class ServerTest extends TestCase {

    private SimpleServer serverapp;
    private ServerSocket socket;
    
    @Before
    protected void setUp() {
        try {
        	socket = new ServerSocket(0);
            serverapp = new SimpleServer(socket);
            Thread server = new Thread(serverapp);
            server.setDaemon(true);
            server.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    @After
    protected void tearDown() {
        serverapp.terminate();
    }
    
    @Test
    public void testServer() {
        assertNotNull(serverapp);
        
        String expected = "Put the piano and the drum into the truck.";
     
        SimpleClient clientApp = new SimpleClient();
        
        String result = clientApp.run("localhost", socket.getLocalPort());
        
        // Shutdown serverapp:
        serverapp.terminate();
        
        assertEquals(expected, result);
    }
}
