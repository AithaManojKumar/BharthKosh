package com.pdf.sign.document.controller;

//import com.helger.xmldsig.keyselect.X509KeySelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helger.xmldsig.keyselect.X509KeySelector;
import com.pdf.sign.document.service.DocumentService;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509IssuerSerial;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DigitalSignatureValidatorRestController {

    private final Logger logger = LoggerFactory.getLogger(DigitalSignatureValidatorRestController.class);

    @Autowired
    DocumentService documentService;
    
    @GetMapping("/welcome")
    public String welcome() {
    	
    	return "Welcome to Bharat Payment API with Production Environment";
    }

    // XML Document as String for the signing
    @GetMapping(value = "/getDigitalSignature",produces = {"application/json"})
    public String getSignString(String xmlString) throws Exception {

        logger.info("Sign. started.");
        
       // xmlString = new String(Base64.getDecoder().decode(xmlString.getBytes("UTF-8")));


        // Access & read file only by server (not include anywhere in client request).

        //  ----------------------- Path to pfx file ----------
        //File privateKeyFile = new File(getClass().getClassLoader().getResource("classpath:TestCertForLOBA.pfx").getFile());
        File privateKeyFile = null;
        try{
            //privateKeyFile = ResourceUtils.getFile("classpath:TestCertForLOBA.pfx");
           // privateKeyFile = ResourceUtils.getFile("classpath:DS NATIONAL TEST HOUSE 1.pfx"); stage file
        	privateKeyFile = ResourceUtils.getFile("classpath:nth.pfx");
            
        }catch (Exception e){
            e.printStackTrace();
        }
        String output = "";
		try {
			XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
			Reference ref = fac.newReference
			        ("", fac.newDigestMethod(DigestMethod.SHA1, null),
			                Collections.singletonList
			                        (fac.newTransform
			                                (Transform.ENVELOPED, (TransformParameterSpec) null)),
			                null, null);

			SignedInfo si = fac.newSignedInfo
			        (fac.newCanonicalizationMethod
			                        (CanonicalizationMethod.INCLUSIVE,
			                                (C14NMethodParameterSpec) null),
			                fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
			                Collections.singletonList(ref));


			KeyStore p12 = KeyStore.getInstance("pkcs12");


			//---- getting the private key out of pfx
			p12.load(new FileInputStream(privateKeyFile), "12345678".toCharArray());
			Enumeration e = p12.aliases();
			String alias = (String) e.nextElement();
			KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) p12.getEntry(alias,
			        new KeyStore.PasswordProtection("12345678".toCharArray()));

			// Load the KeyStore and get the signing key and certificate.
			X509Certificate cert = (X509Certificate) keyEntry.getCertificate();

			// Create the KeyInfo containing the X509Data.
			KeyInfoFactory kif = fac.getKeyInfoFactory();

			ArrayList x509Content = new ArrayList();

			// according to third party API implementation document.
			String dn = cert.getIssuerDN().toString();
			BigInteger sn = cert.getSerialNumber();
			BigInteger bigsn2 = new BigInteger(1, sn.toByteArray());
			X509IssuerSerial xd1 = kif.newX509IssuerSerial(dn, bigsn2);
			x509Content.add(xd1);
			x509Content.add(cert);

			javax.xml.crypto.dsig.keyinfo.X509Data xd = kif.newX509Data(x509Content);
			javax.xml.crypto.dsig.keyinfo.KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));

			ByteArrayInputStream xmlStream = new ByteArrayInputStream(xmlString.getBytes());

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			dbf.setIgnoringElementContentWhitespace(false);
			dbf.setValidating(false);

			Document doc = dbf.newDocumentBuilder().parse(xmlStream);

			doc.setXmlStandalone(true);

			DOMSignContext dsc = new DOMSignContext(keyEntry.getPrivateKey(), doc.getDocumentElement());

			XMLSignature signature = fac.newXMLSignature(si, ki);

			signature.sign(dsc);

//         Output the resulting document.
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer trans = tf.newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			trans.setOutputProperty(OutputKeys.INDENT, "yes");

			StringWriter writer = new StringWriter();
			trans.transform(new DOMSource(doc), new StreamResult(writer));

			output = writer.getBuffer().toString().replaceAll("\n|\r", "");

			logger.info("output "+output);
			output = new String(Base64.getEncoder().encode(output.getBytes("UTF-8")));
			logger.info("Ended -> "+output);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}


        return output;
    }

    public  String validateXMLSignature(String xmlString) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(xmlString)));
        NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
        if (nl.getLength() == 0) {
            throw new Exception("Cannot find Signature element");
        }
        //XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
        //DOMValidateContext valContext = new DOMValidateContext(new X509KeySelector(), nl.item(0));
        //XMLSignature signature = fac.unmarshalXMLSignature(valContext);
        boolean coreValidity = true; //signature.validate(valContext);//skipping the validation
        
        if(coreValidity) {
        	try {
				return documentService.sendXmlData(xmlString);
			} catch (Exception e) {
				throw new Exception("Exception occured while sending the data to intenal application , error message : "+e.getMessage());
			}
        }
        return null;
    }
    
    @PostMapping(value = "/responseDetails")
	public ResponseEntity<Object> getResponseDetails1(HttpServletRequest httpServletRequest) throws Exception {

		String responseDecoded = httpServletRequest.getParameter("BharatkoshResponse");
		logger.info("resposne " + responseDecoded);
		String output = new String(Base64.getDecoder().decode(responseDecoded.getBytes("UTF-8")));
		System.out.println(output);
		String redirectUrl = validateXMLSignature(output);
		// System.out.println("Signature validate status :
		// "+validateXMLSignature(output));
		// getRequiredData(output);
		URI uri = new URI(redirectUrl);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(uri);
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);

	}
   /* @GetMapping(value = "/responseDetails1")
    public String getResponseDetails2(String xmlString) {
    	
    	String responseDecoded = xmlString;
    	System.out.println("resposne "+ responseDecoded);
        try {
			String output = new String(Base64.getDecoder().decode(responseDecoded.getBytes("UTF-8")));
			System.out.println(output);
			boolean flag = validateXMLSignature(output);
			System.out.println("Signature validate status : "+validateXMLSignature(output));
			getRequiredData(output);
			return flag ? "Signature Validated Success":"Signature Validation fail";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}  
    }*/
    public void getRequiredData(String xmlString) throws SAXException, IOException, ParserConfigurationException {
    	 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         dbf.setNamespaceAware(true);
         Document doc = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(xmlString)));
         NodeList nodeList = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "reply");
     System.out.println(nodeList);
     Node node = nodeList.item(0);
//     NamedNodeMap c = doc.getAttributes();
//     Node node = c.getNamedItem("reply");
     getElementValues(node);
     //List list = rootNode.getChildren("customer");
   
    }
    
    public static void getElementValues(Node node) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0, len = nodeList.getLength(); i < len; i++) {
            Node currentNode = nodeList.item(i);
            if (len == 1 && currentNode.getNodeType() == Node.TEXT_NODE) {
                System.out.println(node.getLocalName() + "=" + currentNode.getTextContent());
            }
            else if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                getElementValues(currentNode);
            }
        }
    }
    
    @GetMapping("getTrasactionStatus")
    public String getTrasactionStatus(@RequestParam("orderId")String orderId, @RequestParam("orderId")String purposeId) throws Exception{
    	
    	return getTransactionStatusData(orderId, purposeId);
    }
    
    public static String getTransactionStatusData(String OrderId,String PurposeId) throws Exception {

		RestTemplate rest = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		ObjectMapper obj = new ObjectMapper();
		String response = "";
		try {
			Map<String,String> data = new HashMap();
			data.put("OrderId", "NTH_AROHATECH3");
			data.put("PurposeId", "14810");
			HttpEntity<Map<String, String>> request = new HttpEntity<>(data,headers);
			//requestJson = obj.writeValueAsString(loginInputModel);
			//HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
			URI licenceValidationURL = new URI(
					"https://training.pfms.gov.in/Bharatkosh/getstatus");
				Object c;
				response = rest.postForObject(licenceValidationURL,request, String.class);
				System.out.println("testing" +response);
				
		} catch (Exception e) {
			throw e;
		}
		
		return response;
	}
    
    public static void main(String[] args) throws Exception {
	

    	/*for (int i = 1; i <= 62; i++) {
			System.out.println("private String marks_" + i + ";");
			System.out.println("private String chkRate_" + i + ";");
			System.out.println("");
		}*/

    /*	for (int i = 1; i <= 62; i++) {
    	System.out.println("if(hello.equals(auditCheckListModel.getChkRate_"+i+"())) {");
		System.out.println(" marks += Integer.parseInt(auditCheckListModel.getChkRate_"+i+"());");
		System.out.println(" total += Integer.parseInt(auditCheckListModel.getMarks_"+i+"());");
		System.out.println("}");
		System.out.println();
    	//getTransactionStatusData("NTH_AROHATECH3", "14810");
    }*/
	}
    
    @GetMapping ("/test")
    public ResponseEntity<Object> redirectToExternalUrl() throws URISyntaxException {
        URI uri = new URI("http://nthmis2.stagemyapp.com/customer_pending_payment_test_list/");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }
}
