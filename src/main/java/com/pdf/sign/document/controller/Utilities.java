package com.pdf.sign.document.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;


public class Utilities {

    public static String getStringFromInputStream(InputStream is) throws Exception {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }
        return sb.toString();
    }

    public static Document convertStringToDocument(String xmlStr) throws Exception {
        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(xmlStr)));
        } catch (Exception e) {
            throw e;
        }
        return doc;
    }
    
    public static String GetXpathValue(XPath xPath, String RequestPath, Document doc) throws XPathExpressionException {
        String XpathValue = xPath.compile(RequestPath).evaluate(doc);
        xPath.reset();
        return XpathValue;
    }
    public static void getXmlData(String xmlString) throws Exception {
    	String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <paymentService version=\"1.0\" merchantCode=\"MERCHANT\">\r\n" + 
    			
    			"<customer>" + "<age>35</age>" + "<name>aaa</name>"
    		    + "</customer>";
    	
    	String paymentXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
    			"<paymentService version=\"1.0\" merchantCode=\"MERCHANT\">\r\n" + 
    			"   <reply>\r\n" + 
    			"      <orderStatus orderCode=\"NTH_AROHATECH3\" status=\"SUCCESS\">\r\n" + 
    			"         <reference id=\"1112220023208\" BankTransacstionDate=\"12/11/2022 19:41:48\" TotalAmount=\"1.00\" />\r\n" + 
    			"      </orderStatus>\r\n" + 
    			"   </reply>\r\n" + 
    			"   <Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">\r\n" + 
    			"      <SignedInfo>\r\n" + 
    			"         <CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\" />\r\n" + 
    			"         <SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\" />\r\n" + 
    			"         <Reference URI=\"\">\r\n" + 
    			"            <Transforms>\r\n" + 
    			"               <Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\" />\r\n" + 
    			"            </Transforms>\r\n" + 
    			"            <DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\" />\r\n" + 
    			"            <DigestValue>baZJCODDqgVHgeha6PJjcTk/Kx8=</DigestValue>\r\n" + 
    			"         </Reference>\r\n" + 
    			"      </SignedInfo>\r\n" + 
    			"      <SignatureValue>gNqx7bmloIE/J1FoPbN9Z0UF9YtaCch7e11th5RCXpFuNtmmJIS0mmmF9cxiin4neCkeLclzIgJFgezMzuX6RA4PzP81MUC11oxFTlq08EKbVawTFgkcF/Kh7vdqhAeQi+NgBuFs8wERinkp5fK/PW24zrcQJy5Vm3KI7dTKfcAJLEIH2SuUOHgb2ukU9fjI3jWzRItmapmSdx451l37Hm/C4zplz94FK5yi1CiNELi1GfVBAnQ+ZCQMPBpFCEz27mVznAYWnEH2k2CMI1wzXzrjBCaH4t6WHeg8/hOOuusnVrahZ8TgmON6KTxrIAR5+YPB59ZEEKODu/SPS05a+w==</SignatureValue>\r\n" + 
    			"      <KeyInfo>\r\n" + 
    			"         <X509Data>\r\n" + 
    			"            <X509IssuerSerial>\r\n" + 
    			"               <X509IssuerName>CN=GeoTrust TLS RSA CA G1, OU=www.digicert.com, O=DigiCert Inc, C=US</X509IssuerName>\r\n" + 
    			"               <X509SerialNumber>19348519733089636394052366833257350850</X509SerialNumber>\r\n" + 
    			"            </X509IssuerSerial>\r\n" + 
    			"            <X509Certificate>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</X509Certificate>\r\n" + 
    			"         </X509Data>\r\n" + 
    			"      </KeyInfo>\r\n" + 
    			"   </Signature>\r\n" + 
    			"</paymentService>";
    	InputSource source = new InputSource(new StringReader(paymentXml));
    	XPath xpath = XPathFactory.newInstance()
    	                          .newXPath();
    	Object customer = xpath.evaluate("/reply", source, XPathConstants.NODE);
    	String age = xpath.evaluate("orderStatus", customer);
    	String name = xpath.evaluate("orderCode", customer);
    	System.out.println(age + " " + name);
    }
    
    public static void main(String[] args) throws Exception {
		getXmlData("");
	}
}
