package com.example.tdxassist.demo.client;

import ch.iec.tc57._2011.schema.message.RequestMessage;
import ch.iec.tc57._2011.schema.message.ResponseMessage;
import es.ree.eemws.client.common.ParentClient;
import es.ree.eemws.core.utils.file.GZIPUtil;
import es.ree.eemws.core.utils.iec61968100.*;
import es.ree.eemws.core.utils.operations.HandlerException;
import es.ree.eemws.core.utils.security.CryptoException;
import es.ree.eemws.core.utils.security.CryptoManager;
import es.ree.eemws.core.utils.xml.XMLElementUtil;

import org.xml.sax.SAXException;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Created by INESCTEC
 * Author: André Garcia
 * Email: andre.f.garcia@inesctec.pt
 * Date: 21/11/2019
 * Subject: This demo is intended to implement the standard IEC 62325-504 client as a reference
 */
class Client extends ParentClient {
    ResponseMessage send(RequestMessage requestMessage) throws HandlerException {
        return sendMessage(requestMessage);
    }
}

public class MainClient {

    /**
     * Example: This method builds your request message
     * @return RequestMessage
     */
    static RequestMessage request() throws JAXBException, IOException, CryptoException, ParserConfigurationException,
            SAXException {

        // Build the Object to be sent over the payload
        Object payloadObject = new Object();

        String payload = XMLElementUtil.object2StringBuilder(payloadObject).toString();

        // Step 1: ZIP Message
        byte[] compressed = GZIPUtil.compress(payload.getBytes(StandardCharsets.UTF_8));

        // Step 2: Encode Message
        String encodedString = Base64.getEncoder().encodeToString(compressed);

        // Step 3: Encrypt Message
        StringBuilder encrytedMessage = new StringBuilder(CryptoManager.encrypt(encodedString));

        return MessageUtil.createRequestWithPayload(
                EnumVerb.CREATE.toString(),
                EnumNoun.ANY.toString(),
                encrytedMessage);
    }

    public static void main(String[] args) throws IOException, CryptoException, JAXBException, HandlerException,
            ParserConfigurationException, SAXException {

        Client client = new Client();
        String endpoint = "http://vcpes07.inesctec.pt:9000/ws/requestservice";

        client.setEndPoint((new URL(endpoint)));
        client.setVerifyResponse(true);
        client.setSignRequest(true);

        // Obtain certificate details
        // X509Certificate publicCertificate = SignatureHelper.getPublicKey(publicCertificateLocation);
        // PrivateKey privateKey = SignatureHelper.getPrivateKey(privateCertificateLocation);
        // client.setCertificate(publicCertificate);
        // client.setPrivateKey(privateKey);

        // get requestMessage object
        RequestMessage requestMessage = request();

        // Verify message
        System.out.println(XMLElementUtil.object2StringBuilder(requestMessage));

        // Send Message
        ResponseMessage responseMessage = client.send(requestMessage);

        // Check response
        System.out.println(XMLElementUtil.object2StringBuilder(responseMessage));

    }
}