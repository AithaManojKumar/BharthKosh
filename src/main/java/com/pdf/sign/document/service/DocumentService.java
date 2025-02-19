package com.pdf.sign.document.service;

import java.net.URI;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdf.sign.document.controller.DigitalSignatureValidatorRestController;

@Service
public class DocumentService {

    private final Logger logger = LoggerFactory.getLogger(DocumentService.class);

	public String sendXmlDataOld(String xmlString) throws Exception {
		String url = "https://mis.nth.gov.in/customer-online-pay/after/submission/?xml_data=";
		RestTemplate rest = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ObjectMapper obj = new ObjectMapper();

		String requestJson;
		try {
			logger.info("Request = "+url+"  , "+xmlString);
			
			String encoded = Base64.getEncoder().encodeToString(xmlString.getBytes());
			
			System.out.println("Req : "+url+encoded);
			URI licenceValidationURL = new URI(url+encoded);
			ResponseDetails response = rest.getForObject(licenceValidationURL,ResponseDetails.class);
			logger.info("get redirection url : "+response.getRedirect_url());
			
			return response.getRedirect_url();
		} catch (Exception e) {
			logger.error("Exception occured while sending xmlData = ",e);
			throw e;
		}
	}
	public String sendXmlData(String xmlString) throws Exception {
		String url = "https://mis.nth.gov.in/customer-online-pay/after/submission/?xml_data=";
		RestTemplate rest = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		ObjectMapper obj = new ObjectMapper();

		String requestJson;
		try {
			logger.info("Request = "+url + "  , "+xmlString);
			
			String encoded = Base64.getEncoder().encodeToString(xmlString.getBytes());
			
			MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
			formData.add("xml_data", encoded);
			
			System.out.println("Req : "+url+encoded);
			
			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

			ResponseDetails response = rest.postForObject(url, requestEntity, ResponseDetails.class);
			logger.info("get redirection url : "+response.getRedirect_url());
			
			return response.getRedirect_url();
		} catch (Exception e) {
			logger.error("Exception occured while sending xmlData = ",e);
			throw e;
		}
	}
	public static void main(String[] args) throws Exception {
		new DocumentService().sendXmlData("PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+PEJoYXJhdEtvc2hQYXltZW50IERlcGFydG1lbnRDb2RlPSIwMDMiIFZlcnNpb249IjEuMCI+PFN1Ym1pdD48T3JkZXJCYXRjaCBUb3RhbEFtb3VudD0iMjUwMC4wIiBUcmFuc2FjdGlvbnM9IjEiIG1lcmNoYW50QmF0Y2hDb2RlPSJOVEhfQVJPSEFURUNINDY3NCI+PE9yZGVyIEluc3RhbGxhdGlvbklkPSI0Njc0IiBPcmRlckNvZGU9Ik5USF9BUk9IQVRFQ0g0Njc0Ij48Q2FydERldGFpbHM+PERlc2NyaXB0aW9uPk5USDwvRGVzY3JpcHRpb24+PEFtb3VudCBDdXJyZW5jeUNvZGU9IklOUiIgZXhwb25lbnQ9IjAiIHZhbHVlPSIxIi8+PE9yZGVyQ29udGVudD4xNDgxMDwvT3JkZXJDb250ZW50PjxQYXltZW50VHlwZUlkPjA8L1BheW1lbnRUeXBlSWQ+PFBBT0NvZGU+MDAzODUwPC9QQU9Db2RlPjxERE9Db2RlPjIwMjEyODwvRERPQ29kZT48L0NhcnREZXRhaWxzPjxQYXltZW50TWV0aG9kTWFzaz48SW5jbHVkZSBDb2RlPSJPbkxpbmUiLz48L1BheW1lbnRNZXRob2RNYXNrPjxTaG9wcGVyPjxTaG9wcGVyRW1haWxBZGRyZXNzPmFtb251OUBnbWFpbC5jb208L1Nob3BwZXJFbWFpbEFkZHJlc3M+PC9TaG9wcGVyPjxTaGlwcGluZ0FkZHJlc3M+PEFkZHJlc3M+PEZpcnN0TmFtZT50ZXN0PC9GaXJzdE5hbWU+PExhc3ROYW1lPnRlc3Q8L0xhc3ROYW1lPjxBZGRyZXNzMT5uZXcgdG93biwga29sa2F0YSwgMjQgUGFyYWdhbmFzIFNvdXRoLCBXZXN0IEJlbmdhbCwgSW5kaWEgLSA3MDAxNTY8L0FkZHJlc3MxPjxBZGRyZXNzMj5uZXcgdG93biwga29sa2F0YSwgMjQgUGFyYWdhbmFzIFNvdXRoLCBXZXN0IEJlbmdhbCwgSW5kaWEgLSA3MDAxNTY8L0FkZHJlc3MyPjxQb3N0YWxDb2RlPjcwMDE1NjwvUG9zdGFsQ29kZT48Q2l0eT5rb2xrYXRhPC9DaXR5PjxTdGF0ZVJlZ2lvbj4yNCBQYXJhZ2FuYXMgU291dGg8L1N0YXRlUmVnaW9uPjxTdGF0ZT5XZXN0IEJlbmdhbDwvU3RhdGU+PENvdW50cnlDb2RlPkluZGlhPC9Db3VudHJ5Q29kZT48TW9iaWxlTnVtYmVyPjc4Mzg1NjcyMjc8L01vYmlsZU51bWJlcj48L0FkZHJlc3M+PC9TaGlwcGluZ0FkZHJlc3M+PEJpbGxpbmdBZGRyZXNzPjxBZGRyZXNzPjxGaXJzdE5hbWU+dGVzdDwvRmlyc3ROYW1lPjxMYXN0TmFtZT50ZXN0PC9MYXN0TmFtZT48QWRkcmVzczE+bmV3IHRvd24sIGtvbGthdGEsIDI0IFBhcmFnYW5hcyBTb3V0aCwgV2VzdCBCZW5nYWwsIEluZGlhIC0gNzAwMTU2PC9BZGRyZXNzMT48QWRkcmVzczI+bmV3IHRvd24sIGtvbGthdGEsIDI0IFBhcmFnYW5hcyBTb3V0aCwgV2VzdCBCZW5nYWwsIEluZGlhIC0gNzAwMTU2PC9BZGRyZXNzMj48UG9zdGFsQ29kZT43MDAxNTY8L1Bvc3RhbENvZGU+PENpdHk+a29sa2F0YTwvQ2l0eT48U3RhdGVSZWdpb24+MjQgUGFyYWdhbmFzIFNvdXRoPC9TdGF0ZVJlZ2lvbj48U3RhdGU+V2VzdCBCZW5nYWw8L1N0YXRlPjxDb3VudHJ5Q29kZT5JbmRpYTwvQ291bnRyeUNvZGU+PE1vYmlsZU51bWJlcj43ODM4NTY3MjI3PC9Nb2JpbGVOdW1iZXI+PC9BZGRyZXNzPjwvQmlsbGluZ0FkZHJlc3M+PFN0YXRlbWVudE5hcnJhdGl2ZS8+PFJlbWFya3MvPjwvT3JkZXI+PC9PcmRlckJhdGNoPjwvU3VibWl0PjwvQmhhcmF0S29zaFBheW1lbnQ+");
	}
}
