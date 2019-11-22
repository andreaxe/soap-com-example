package com.example.tdxassist.demo.server;

import _504.iec62325.wss._1._0.MsgFaultMsg;
import _504.iec62325.wss._1._0.PortTFEDIType;
import ch.iec.tc57._2011.schema.message.*;
import es.ree.eemws.core.utils.iec61968100.EnumMessageStatus;
import es.ree.eemws.core.utils.iec61968100.MessageUtil;
import es.ree.eemws.core.utils.security.SignatureManager;
import es.ree.eemws.core.utils.xml.XMLElementUtil;
import es.ree.eemws.core.utils.xml.XMLUtil;
import org.w3c.dom.Document;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 * Created by INESCTEC
 * Author: André Garcia
 * Email: andre.f.garcia@inesctec.pt
 * Date: 21/11/2019
 * This demo is intended to implement the standard IEC 62325-504 server as a reference
 */
@BindingType("http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
@WebService(endpointInterface = "_504.iec62325.wss._1._0.PortTFEDIType")
public class Request implements PortTFEDIType {

    public ResponseMessage request(RequestMessage requestMessage) throws MsgFaultMsg {

        try {

            Document incomingRequest = (Document) XMLUtil.string2Document(
                    XMLElementUtil.object2StringBuilder(requestMessage));

            // validate incoming message
            SignatureManager.verifyDocument(incomingRequest);

            // requestMessage with be dealt here

        } catch (Exception e) {

            // A very simple example of how to deal with an exception

            String message = String.format("Sonething went wrong with your request: %s", e.getMessage());
            e.printStackTrace();

            FaultMessage fault = new FaultMessage();
            ReplyType errorReply = new ReplyType();
            errorReply.setResult("ERROR");

            ErrorType error = new ErrorType();
            error.setLevel("ERROR");
            error.setCode("ErrorCode");
            error.setDetails(e.getMessage());

            errorReply.getErrors().add(error);
            fault.setReply(errorReply);

            throw new MsgFaultMsg(message, fault);
        }
        // create a default OK response

        // sign the Response with assistance of XMLUtil and SignatureManager from the ree utils package
        // and return the response
        // SignatureManager.signDocument(requestMessageBody, privateKey, publicCertificate);

        return MessageUtil.createResponseWithNoPayload(EnumMessageStatus.OK);
    }
}
