package com.ericsson.eniq.flsmock.controller;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.eniq.flsmock.config.SpringProperties;
import com.ericsson.eniq.flsmock.pojo.ParserInput;
import com.ericsson.eniq.flsmock.pojo.ProducerInput;
import com.ericsson.eniq.flsmock.util.FLSUtil;
import com.ericsson.eniq.flsmock.util.ParserInputTemplateUtil;
import com.ericsson.eniq.flsmock.util.ParserType;

import io.swagger.annotations.ApiOperation;

@RestController
public class FlsmockController {

	private static final Logger log = LogManager.getLogger(FlsmockController.class);

	int batchId = 1000;

	@Autowired
	private ParserInputTemplateUtil parserInputTemplateUtil;
	@Autowired
	private SpringProperties springProperties;
	
	@Autowired 
	FLSUtil fLSUtil;
	
	@RequestMapping(value="/asn1/produce", method = RequestMethod.POST, consumes = "application/json", produces = "text/plain")
	@ApiOperation(value="ANS1 Parser", notes="This IS ASN1 Parser ")
	public ResponseEntity<Object> produceAsn1Message(@RequestBody ProducerInput input)
	{
		log.info("input Received : {}" + input.toString());
		log.info("input Received : {}" + input.toString());
		for (int i = 0; i < input.getTotalBatches(); i++) {
			batchId++;
			ParserInput parserInputTemplate = parserInputTemplateUtil.getParserInputTemplate(ParserType.ASN1);
			fLSUtil.produceBatch(batchId, input, parserInputTemplate, springProperties.getKafka().getAsn1ParserTopic());
		}
		return new ResponseEntity<Object>("ASN1 Messages produced", HttpStatus.OK);
	} 

	@RequestMapping(value = "/produce", method = RequestMethod.POST, consumes = "application/json", produces = "text/plain")
	@ApiOperation(value = "MDC parser", notes = "This is MDC parser")
	public ResponseEntity<Object> produceMessages(@RequestBody ProducerInput input) {
		log.info("input Received : " + input.toString());
		for (int i = 0; i < input.getTotalBatches(); i++) {
			batchId++;
			ParserInput parserInputTemplate =parserInputTemplateUtil.getParserInputTemplate(ParserType.MDC);
			fLSUtil.produceBatch(batchId, input, parserInputTemplate, springProperties.getKafka().getMdcParserTopic());
		}
		return new ResponseEntity<Object>("Messages produced", HttpStatus.OK);
	}

	@RequestMapping(value = "/ct/produce", method = RequestMethod.POST, consumes = "application/json", produces = "text/plain")
	@ApiOperation(value = "CT parser", notes = "This is CT parser")
	public ResponseEntity<Object> produceCTSMessages(@RequestBody ProducerInput input) {
		log.info("input Received : " + input.toString());
		for (int i = 0; i < input.getTotalBatches(); i++) {
			batchId++;
			ParserInput parserInputTemplate =parserInputTemplateUtil.getParserInputTemplate(ParserType.CT);
			fLSUtil.produceBatch(batchId, input, parserInputTemplate, springProperties.getKafka().getCtParserTopic());
		}
		return new ResponseEntity<Object>("Messages produced", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/ebs/produce", method = RequestMethod.POST, consumes = "application/json", produces = "text/plain")
	@ApiOperation(value = "EBS parser", notes = "This is EBS parser")
	public ResponseEntity<Object> produceEBSSMessages(@RequestBody ProducerInput input) {
		log.info("Ebs parser input Received : " + input.toString());
		for (int i = 0; i < input.getTotalBatches(); i++) {
			batchId++;
			ParserInput parserInputTemplate = parserInputTemplateUtil.getParserInputTemplate(ParserType.EBS);
			fLSUtil.produceBatch(batchId, input, parserInputTemplate, springProperties.getKafka().getEbsParserTopic());
		}
		return new ResponseEntity<Object>("EBS Messages produced", HttpStatus.OK);
	}
	@RequestMapping(value = "/ascii/produce", method = RequestMethod.POST, consumes = "application/json", produces = "text/plain")
	@ApiOperation(value = "ASCII parser", notes = "This is ASCII parser")
	public ResponseEntity<Object> produceASCIIMessages(@RequestBody ProducerInput input) {
		log.info("ascii parser input Received : " + input.toString());
		for (int i = 0; i < input.getTotalBatches(); i++) {
			batchId++;
			ParserInput parserInputTemplate =parserInputTemplateUtil.getParserInputTemplate(ParserType.ASCII);
			fLSUtil.produceBatch(batchId, input, parserInputTemplate, springProperties.getKafka().getAsciiParserTopic());
		}
		return new ResponseEntity<Object>("Messages produced", HttpStatus.OK);
	}
	@RequestMapping(value = "/csexport/produce", method = RequestMethod.POST, consumes = "application/json", produces = "text/plain")
	@ApiOperation(value = "CSExport parser", notes = "This is CSExport parser")
	public ResponseEntity<Object> produceCSExportMessages(@RequestBody ProducerInput input) {
		log.info("csexport parser input Received : " + input.toString());
		for (int i = 0; i < input.getTotalBatches(); i++) {
			batchId++;
			ParserInput parserInputTemplate =parserInputTemplateUtil.getParserInputTemplate(ParserType.CSEXPORT);
			fLSUtil.produceBatch(batchId, input, parserInputTemplate, springProperties.getKafka().getCsexportParserTopic());
		}
		return new ResponseEntity<Object>("Messages produced", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/3GPP/produce", method = RequestMethod.POST, consumes = "application/json", produces = "text/plain")
	@ApiOperation(value = "3GPP parser", notes = "This is 3GPPparser")
	public ResponseEntity<Object> produceGPPMessages(@RequestBody ProducerInput input) {
		log.log(Level.INFO, "3GPP parser input Received : {}",input.toString());
		log.log(Level.INFO, "3GPP parser input Topic : {}",springProperties.getKafka().getGppParserTopic());
		for (int i = 0; i < input.getTotalBatches(); i++) {
			batchId++;
			ParserInput parserInputTemplate =parserInputTemplateUtil.getParserInputTemplate(ParserType.GPP);
			fLSUtil.produceBatch(batchId, input, parserInputTemplate, springProperties.getKafka().getGppParserTopic());
		}
		return new ResponseEntity<Object>("Messages produced", HttpStatus.OK);
	}

	@RequestMapping(value = "/bulkcmhandler/produce", method = RequestMethod.POST, consumes = "application/json", produces = "text/plain")
	@ApiOperation(value = "BULKCMHANDLER parser", notes = "This is BULKCMHANDLER parser")
	public ResponseEntity<Object> produceBULKCMHandlerMessages(@RequestBody ProducerInput input) {
		log.info("bulkcmhandler parser input Received : " + input.toString());
		for (int i = 0; i < input.getTotalBatches(); i++) {
			batchId++;
			ParserInput parserInputTemplate = parserInputTemplateUtil.getParserInputTemplate(ParserType.BULKCMHANDLER);
			fLSUtil.produceBatch(batchId, input, parserInputTemplate,
					springProperties.getKafka().getBulkcmhandlerParserTopic());
		}
		return new ResponseEntity<Object>("Messages produced", HttpStatus.OK);
	}

}
