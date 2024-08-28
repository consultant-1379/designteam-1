package com.ericsson.eniq.sbkafka.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ericsson.eniq.parser.ParseSession;
import com.ericsson.eniq.parser.Parser;
import com.ericsson.eniq.parser.SourceFile;
import com.ericsson.eniq.sbkafka.controller.pojo.ParserInput;
import com.google.gson.Gson;

@Controller
public class SbKafkaController {

	
	private static final Logger logger = LogManager.getLogger(SbKafkaListener.class);

	private List<Callable<Boolean>> workers = new ArrayList<>();

	@Value("${parser.batch.size}")
	private String batchSize;
	
	private int consumptionBatchId = 100;
	
	private String hostName;

	@PostMapping(value = "/produce", consumes = "application/json", produces = "text/plain")
	@ResponseBody
	public String produceMessages(@RequestBody ParserInput input) {

		createWorker(input);	
		return "Messages produced";

	}
	private void createWorker(ParserInput input) {
		try
		{
			File inputFile = new File(input.getInputFile());
			String setName = input.getSetName();
			String setType = input.getSetType();
			String techpack = input.getTp();
			input.getActionContents().put("outDir",input.getActionContents().get("outDir")+File.separator+"_"+getHostName() + "_"+consumptionBatchId);
			Properties conf = new Properties();
			conf.putAll(input.getActionContents());
			ParseSession session = new ParseSession(8888, conf);
			SourceFile sf = new SourceFile(inputFile, conf, session, conf.getProperty("useZip", "gzip"), logger);
			Parser parser = new Parser(sf, techpack, setType, setName, "ct_worker");
			workers.add(parser);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getHostName() {
		if (hostName == null) {
			hostName = System.getenv("HOSTNAME");
		}
		return hostName;
	}
}
