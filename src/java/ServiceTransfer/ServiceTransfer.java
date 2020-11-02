/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServiceTransfer;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.data.PcmlException;
import com.ibm.as400.data.ProgramCallDocument;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 * 
 * @author Figueroa
 */
@WebService(serviceName = "ServiceTransfer")
public class ServiceTransfer {
    
    static AS400 as400;
    static final String HOST ="206.60.106.2";  // Prod
    //static final String USUARIO ="MASERRANO";
    //static final String PASSWORD ="Mercede5";
    static final String USUARIO ="CREDITODC";
    static final String PASSWORD ="HEB416SL51";
    //static final String HOST ="206.60.106.125";  // QA
    //static final String HOST ="206.60.106.206";  // DS  

    /**
     * 
     *    
     * GetServiceResponse
     * 
     * 
     * 
     * @param XmlRequest
     * @return 
     */
    @WebMethod(operationName = "GetServiceResponse")
    public String GetServiceResponse(@WebParam(name = "XmlRequest") String XmlRequest) {
        String xmlresponse="";
        String msgId, msgText;
        
        //XmlRequest = XmlRequest.replaceAll(" ",""); 
        
        as400 = new AS400(HOST,USUARIO,PASSWORD);
        String path = "/QSYS.LIB/BANTRABOBJ.LIB/SRVP009.SRVPGM";
        try {
            ProgramCallDocument pcml = new ProgramCallDocument(as400, "SRVP009");
            pcml.setPath ("PAY_GETSERVICERESPONSE", path);
            
            //XmlRequest            
            pcml.setValue("PAY_GETSERVICERESPONSE.XMLREQUEST", XmlRequest);            
            
                      
            pcml.setValue("PAY_GETSERVICERESPONSE.XML", xmlresponse);
            
        
            boolean rc = pcml.callProgram("PAY_GETSERVICERESPONSE");
        
            if (rc){        
                xmlresponse    = (String) pcml.getValue("PAY_GETSERVICERESPONSE.XML");                
                System.out.println("XML:" + xmlresponse);
            }else{
                AS400Message[] msgs = pcml.getMessageList("PAY_GETSERVICERESPONSE");
                for (AS400Message msg : msgs) {
                    msgId = msg.getID();
                    msgText = msg.getText();
                    System.out.println("    " + msgId + " - " + msgText);
                }                
                System.out.println("** Ha TRONADO la llamada a PAY_GETSERVICERESPONSE. Vea los mensajes anteriores **");
                xmlresponse = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soapenvelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soapenv:Header/><soapenv:Body><status><status_code>01000</status_code><status_description>Internal Error</status_description><sub_status_code>01001</sub_status_code><sub_status_description>System Error</sub_status_description></status></soapenv:Body></soapenv:Envelope>";

            }
            
        }catch(PcmlException e){
            System.out.println(e.getLocalizedMessage());    
            System.out.println("*** Ha fallado la llamada a PAY_GETSERVICERESPONSE. ***");  
            xmlresponse = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soapenvelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soapenv:Header/><soapenv:Body><status><status_code>40000</status_code><status_description>connection failure</status_description><sub_status_code>40002</sub_status_code><sub_status_description>Waiting for bank reply</sub_status_description></status></soapenv:Body></soapenv:Envelope>";

        }    
        
                
        as400.disconnectAllServices();
        
        return xmlresponse;

        
        
        
        
        
        
        
        
        
        
    }// Fin-GetServiceResponse
    
    
    
    
    
    
    
    
}// Fin ServiceTransfer
