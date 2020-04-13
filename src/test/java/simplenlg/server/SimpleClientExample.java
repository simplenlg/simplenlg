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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.junit.Ignore;

/**
 * An example implementation of a java client.
 * <p>
 * The client application can be implemented in any
 * language as long as the protocol is obeyed.
 * <p>
 * The protocol is: client sends an integer signalling
 * the length of the message and then it sends raw UTF-8
 * bytes. The server parses the bytes into the original
 * UTF-8 string and then parse the string as nlg:Request.
 * <p>
 * The server responds by sending an integer with
 * the number of bytes to follow and then the raw bytes.
 *
 * @author Roman Kutlak
 */
@Ignore
public class SimpleClientExample {

	private String request;

	public SimpleClientExample(String request) {
		this.request = request;
	}

	/**
	 * Executes the {@link SimpleClientExample} by connecting to the {@link SimpleServer} and
	 * sending an XML Request to realise.
	 *
	 * @param serverAddress -- The sever address to user e.g. "localhost"
	 * @param port -- The port number to use.
	 * @return The realised {@link String} output from the {@link simplenlg.xmlrealiser.XMLRealiser}.
	 */
	protected String run(String serverAddress, int port) {
		try {
			System.out.println("Connecting to " + serverAddress + " on port " + port);
			Socket client = new Socket(serverAddress, port);
			OutputStream outToServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);

			byte[] tmp = request.getBytes("UTF-8");
			out.writeInt(tmp.length);
			out.write(tmp);

			InputStream inFromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(inFromServer);
			int len = in.readInt();
			byte[] data = new byte[len];
			// read the entire message (blocks until complete)
			in.readFully(data);

			String text = new String(data, "UTF-8");

			System.out.println("Realisation: " + text);

			client.close();

			return text;
		} catch(Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		return "";
	}
}
