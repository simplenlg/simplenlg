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

import java.net.*;
import java.io.*;

/**
 * An example implementation of a java client.
 * 
 * The client application can be implemented in any 
 * language as long as the protocol is obeyed.  
 * 
 * The protocol is: client sends an integer signalling 
 * the length of the message and then it sends raw UTF-8 
 * bytes. The server parses the bytes into the original 
 * UTF-8 string and then parse the string as nlg:Request.
 * 
 * The server responds by sending an integer with
 * the number of bytes to follow and then the raw bytes.
 * 
 * @author Roman Kutlak
 *
 */
public class SimpleClient {

    static final String testData 
        = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<nlg:NLGSpec xmlns=\"http://simplenlg.googlecode.com/svn/trunk/res/xml\"\n" +
                "    xmlns:nlg=\"http://simplenlg.googlecode.com/svn/trunk/res/xml\"\n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "    xsi:schemaLocation=\"http://simplenlg.googlecode.com/svn/trunk/res/xml \">\n" +
                "    <nlg:Request>\n" +
                "        <Document cat=\"PARAGRAPH\">\n" +
                "            <child xsi:type=\"SPhraseSpec\" FORM=\"IMPERATIVE\">\n" +
                "                <vp xsi:type=\"VPPhraseSpec\">\n" +
                "                    <head xsi:type=\"WordElement\" cat=\"VERB\">\n" +
                "                        <base>put</base>\n" +
                "                    </head>\n" +
                "                    <compl xsi:type=\"CoordinatedPhraseElement\" conj=\"and\" discourseFunction=\"OBJECT\">\n" +
                "                        <coord xsi:type=\"NPPhraseSpec\">\n" +
                "                            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\n" +
                "                                <base>the</base>\n" +
                "                            </spec>\n" +
                "                            <head xsi:type=\"WordElement\" cat=\"NOUN\">\n" +
                "                                <base>piano</base>\n" +
                "                            </head>\n" +
                "                        </coord>\n" +
                "                        <coord xsi:type=\"NPPhraseSpec\">\n" +
                "                            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\n" +
                "                                <base>the</base>\n" +
                "                            </spec>\n" +
                "                            <head xsi:type=\"WordElement\" cat=\"NOUN\">\n" +
                "                                <base>drum</base>\n" +
                "                            </head>\n" +
                "                        </coord>\n" +
                "                    </compl>\n" +
                "                    <compl xsi:type=\"PPPhraseSpec\">\n" +
                "                        <head xsi:type=\"WordElement\" cat=\"PREPOSITION\">\n" +
                "                            <base>into</base>\n" +
                "                        </head>\n" +
                "                        <compl xsi:type=\"NPPhraseSpec\">\n" +
                "                            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\n" +
                "                                <base>the</base>\n" +
                "                            </spec>\n" +
                "                            <head xsi:type=\"WordElement\" cat=\"NOUN\">\n" +
                "                                <base>truck</base>\n" +
                "                            </head>\n" +
                "                        </compl>\n" +
                "                    </compl>\n" +
                "                </vp>\n" +
                "            </child>\n" +
                "        </Document>\n" +
                "    </nlg:Request>\n" +
                "</nlg:NLGSpec>\n";


    
    public static void main(String[] args) {
        String serverName;
        int port;
        
        if (args.length > 0)
            serverName = args[0];
        else
            serverName = "localhost";
        
        try {
            port = Integer.parseInt(args[1]);
        } catch (Exception e) {
            port = 50007;
        }
        
        new SimpleClient().run(serverName, port);
    }
    
    public String run(String serverName, int port) {
        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            
            byte[] tmp = testData.getBytes("UTF-8");
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
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        
        return "";
    }
}
