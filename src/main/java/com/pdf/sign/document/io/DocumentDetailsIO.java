package com.pdf.sign.document.io;

import java.io.Serializable;

public class DocumentDetailsIO implements Serializable {

	private String pdfPath;
	private String pdfBtyeArray;
	private String pdfStringData;
	private String fileName;
	private String status;

	private String signerId;
	private String signaturePosition;
	private String signatureContent;
	private String pdfSignatureCordinates;
	private String eGatewayType;
	private String responseMessage;
	private String gateWayParam;
	private String redirectURL;
	private String signedPdfString;
	private String transactionId;
	
	public String getPdfPath() {
		return pdfPath;
	}
	public void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
	}
	public String getPdfBtyeArray() {
		return pdfBtyeArray;
	}
	public void setPdfBtyeArray(String pdfBtyeArray) {
		this.pdfBtyeArray = pdfBtyeArray;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}


	public String geteGatewayType() {
		return eGatewayType;
	}

	public void seteGatewayType(String eGatewayType) {
		this.eGatewayType = eGatewayType;
	}

	public String getGateWayParam() {
		return gateWayParam;
	}

	public void setGateWayParam(String gateWayParam) {
		this.gateWayParam = gateWayParam;
	}

	public String getRedirectURL() {
		return redirectURL;
	}

	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}
	public String getSignedPdfString() {
		return signedPdfString;
	}
	public void setSignedPdfString(String signedPdfString) {
		this.signedPdfString = signedPdfString;
	}
	public String getPdfStringData() {
		return pdfStringData;
	}
	public void setPdfStringData(String pdfStringData) {
		this.pdfStringData = pdfStringData;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getSignerId() {
		return signerId;
	}
	public void setSignerId(String signerId) {
		this.signerId = signerId;
	}
	public String getSignaturePosition() {
		return signaturePosition;
	}
	public void setSignaturePosition(String signaturePosition) {
		this.signaturePosition = signaturePosition;
	}
	public String getSignatureContent() {
		return signatureContent;
	}
	public void setSignatureContent(String signatureContent) {
		this.signatureContent = signatureContent;
	}
	public String getPdfSignatureCordinates() {
		return pdfSignatureCordinates;
	}
	public void setPdfSignatureCordinates(String pdfSignatureCordinates) {
		this.pdfSignatureCordinates = pdfSignatureCordinates;
	}
	
	
	
}
