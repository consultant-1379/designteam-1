package com.ericsson.eniq.flsmock.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.ericsson.eniq.flsmock.pojo.ParserInput;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

@Service
public class ParserInputTemplateUtil {

	private ParserInput ctParserInput;
	private ParserInput mdcParserInput;
	private ParserInput ebsParserInput;
	private ParserInput asciiParserInput;
	private ParserInput csexportParserInput;
	private ParserInput asn1ParserInput;
	private ParserInput gppParserInput;
	private ParserInput bulkcmhandlerParserInput;
	
	@Autowired
	private ResourceLoader resourceLoader;
	@Value("${spring.profiles.active}")
	private String activeProfile;
	@Autowired
	private Gson gson;

	@PostConstruct
	private ParserInput loadCTParserInputTemplate() throws IOException {
		Resource resourceFile;
		if (activeProfile.equalsIgnoreCase("dev")) {
			resourceFile = resourceLoader.getResource("classpath:/CTParserInput-dev.json");
		} else {
			resourceFile = resourceLoader.getResource("classpath:/CTParserInput.json");
		}
		try (InputStream ris = resourceFile.getInputStream();
				JsonReader reader = gson.newJsonReader(new InputStreamReader(ris))) {
			ctParserInput = gson.fromJson(reader, ParserInput.class);
		}

		return null;
	}
	
	@PostConstruct
	private ParserInput loadMDCParserInputTemplate() throws IOException {
		Resource resourceFile;
		if (activeProfile.equalsIgnoreCase("dev")) {
			resourceFile = resourceLoader.getResource("classpath:/ParserInput.json");
		} else {
			resourceFile = resourceLoader.getResource("classpath:/ParserInput.json");
		}
		try (InputStream ris = resourceFile.getInputStream();
				JsonReader reader = gson.newJsonReader(new InputStreamReader(ris))) {
			mdcParserInput = gson.fromJson(reader, ParserInput.class);
		}

		return null;
	}
	
	@PostConstruct
	private ParserInput loadEBSParserInputTemplate() throws IOException {
		Resource resourceFile;
		if (activeProfile.equalsIgnoreCase("dev")) {
			resourceFile = resourceLoader.getResource("classpath:/EBSParserInput-dev.json");
		} else {
			resourceFile = resourceLoader.getResource("classpath:/EBSParserInput.json");
		}
		try (InputStream ris = resourceFile.getInputStream();
				JsonReader reader = gson.newJsonReader(new InputStreamReader(ris))) {
			ebsParserInput = gson.fromJson(reader, ParserInput.class);
		}

		return null;
	}
	
	@PostConstruct
	private ParserInput loadASCIIParserInputTemplate() throws IOException {
		Resource resourceFile;
		if (activeProfile.equalsIgnoreCase("dev")) {
			resourceFile = resourceLoader.getResource("classpath:/ASCIIParserInput-dev.json");
		} else {
			resourceFile = resourceLoader.getResource("classpath:/ASCIIParserInput.json");
		}
		try (InputStream ris = resourceFile.getInputStream();
				JsonReader reader = gson.newJsonReader(new InputStreamReader(ris))) {
			asciiParserInput = gson.fromJson(reader, ParserInput.class);
		}

		return null;
	}
	@PostConstruct
	private ParserInput loadASN1ParserInputTemplate() throws IOException {
		Resource resourceFile;
		if (activeProfile.equalsIgnoreCase("dev")) {
			resourceFile = resourceLoader.getResource("classpath:/ASN1ParserInput-dev.json");
		} else {
			resourceFile = resourceLoader.getResource("classpath:/ASN1ParserInput.json");
		}
		try (InputStream ris = resourceFile.getInputStream();
				JsonReader reader = gson.newJsonReader(new InputStreamReader(ris))) {
			asn1ParserInput = gson.fromJson(reader, ParserInput.class);
		}

		return null;
	}
	@PostConstruct
	private ParserInput loadCSExportParserInputTemplate() throws IOException {
		Resource resourceFile;
		if (activeProfile.equalsIgnoreCase("dev")) {
			resourceFile = resourceLoader.getResource("classpath:/CSExportParserInput.json");
		} else {
			resourceFile = resourceLoader.getResource("classpath:/CSExportParserInput.json");
		}
		try (InputStream ris = resourceFile.getInputStream();
				JsonReader reader = gson.newJsonReader(new InputStreamReader(ris))) {
			csexportParserInput = gson.fromJson(reader, ParserInput.class);
		}

		return null;
	}
	
	@PostConstruct
	private ParserInput loadgppParserInputTemplate() throws IOException {
		Resource resourceFile;
		if (activeProfile.equalsIgnoreCase("dev")) {
			resourceFile = resourceLoader.getResource("classpath:/GPPParserInput.json");
		} else {
			resourceFile = resourceLoader.getResource("classpath:/GPPParserInput.json");
		}
		try (InputStream ris = resourceFile.getInputStream();
				JsonReader reader = gson.newJsonReader(new InputStreamReader(ris))) {
			gppParserInput = gson.fromJson(reader, ParserInput.class);
		}

		return null;
	}
	
	@PostConstruct
	private ParserInput loadBULKCMHandlerParserInputTemplate() throws IOException {
		Resource resourceFile;
		if (activeProfile.equalsIgnoreCase("dev")) {
			resourceFile = resourceLoader.getResource("classpath:/BULKCMHandlerParserInput.json");
		} else {
			resourceFile = resourceLoader.getResource("classpath:/BULKCMHandlerParserInput.json");
		}
		try (InputStream ris = resourceFile.getInputStream();
				JsonReader reader = gson.newJsonReader(new InputStreamReader(ris))) {
			bulkcmhandlerParserInput = gson.fromJson(reader, ParserInput.class);
		}

		return null;
	}
	
	public ParserInput getParserInputTemplate(ParserType parserType) {
		switch (parserType) {
		case CT:
			return ctParserInput.clone();
		case EBS:
			return ebsParserInput.clone();
		case MDC:
			return mdcParserInput.clone();
		case ASN1:
			return asn1ParserInput.clone();
		case ASCII:
			return asciiParserInput.clone();
		case CSEXPORT:
			return csexportParserInput.clone();
		case GPP:
			return gppParserInput.clone();
		case BULKCMHANDLER:
			return bulkcmhandlerParserInput.clone();
		default:
			break;
		}
		return null;
	}

}
