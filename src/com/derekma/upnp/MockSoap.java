package com.derekma.upnp;

import javax.xml.soap.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

public class MockSoap {

    /**
     * Mock Soap message
     */
    public static void main(String args[]) {
        try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Send SOAP Message to SOAP Server
            String url = "http://192.168.0.18:49153/upnp/control/basicevent1";
        
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(), url);
            System.out.println("Waiting for response...");

            // Process the SOAP Response
            printSOAPResponse(soapResponse);

            soapConnection.close();
        } catch (Exception e) {
            System.err.println("Error occurred while sending SOAP Request to Server");
            e.printStackTrace();
        }
    }

    private static SOAPMessage createSOAPRequest() throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "http://192.168.0.18:49153/upnp/control/basicevent1";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("u", serverURI);

        /*
        Constructed SOAP Request Message:
		<s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/" xmlns:u="http://192.168.0.18:49153/upnp/control/basicevent1">
		    <s:Body>
		        <u:GetBinaryStateResponse xmlns:u="urn:Belkin:service:basicevent:1">
		            <BinaryState>0</BinaryState>
		        </u:GetBinaryStateResponse>
		    </s:Body>
		</s:Envelope>
         */

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("GetBinaryStateResponse", "u");
     
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("BinaryState", "u");
        soapBodyElem1.addTextNode("1");

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", "\"urn:Belkin:service:basicevent:1#GetBinaryState\"");

        soapMessage.saveChanges();

        /* Print the request message */
        System.out.print("Request SOAP Message = ");
        soapMessage.writeTo(System.out);
        System.out.println();

        return soapMessage;
    }

    /**
     * Method used to print the SOAP Response
     */
    private static void printSOAPResponse(SOAPMessage soapResponse) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        System.out.print("\nResponse SOAP Message = ");
        StreamResult result = new StreamResult(System.out);
        transformer.transform(sourceContent, result);
    }

}