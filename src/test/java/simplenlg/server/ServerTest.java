/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * https://www.mozilla.org/en-US/MPL/
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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Westwater, Roman Kutlak, Margaret Mitchell, and Saad Mahamood.
 */
package simplenlg.server;

import java.io.FileNotFoundException;
import java.net.ServerSocket;
import java.net.URISyntaxException;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import simplenlg.util.TestUtility;

/**
 * Tests for SimpleNLG Simple Server using the {@link simplenlg.xmlrealiser.XMLRealiser}
 *
 * @author Roman Kutlak
 */
public class ServerTest extends TestCase {

	private SimpleServer serverapp;
	private ServerSocket socket;

	private TestUtility testUtility;

	@Before
	protected void setUp() {
		testUtility = new TestUtility();

		try {
			socket = new ServerSocket(0);
			serverapp = new SimpleServer(socket);
			Thread server = new Thread(serverapp);
			server.setDaemon(true);
			server.start();
		} catch(Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	@After
	protected void tearDown() {
		serverapp.terminate();
	}

	@Test
	public void testSimpleServer() throws FileNotFoundException, URISyntaxException {
		assertNotNull(serverapp);

		String expected = "Put the piano and the drum into the truck.";

		String request = testUtility.getResourceFileAsString("XMLSimpleClient/XMLSimpleClientTest.xml");
		SimpleClientExample clientApp = new SimpleClientExample(request);

		String result = clientApp.run("localhost", socket.getLocalPort());

		// Shutdown serverapp:
		serverapp.terminate();

		assertEquals(expected, result);
	}
}
