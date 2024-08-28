package com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.controller;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.model.Metadata;
import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.service.DwhPartitionService;
import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.service.MetaDataService;
import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.sql.DwhPartitionSql;

@RestController
@RequestMapping(value="/test")
public class TestController {
	
	@Autowired
	MetaDataService metaDataService;
	
	@Autowired
    DwhPartitionSql dwhPartitionSql;
	
	private static DwhPartitionService dwhp;
	
	@GetMapping(value="/getMdata")
	public String getMdata() {
		
    	Metadata metadata = metaDataService.fetchMetadata("DC_E_ERBS_EUTRANFREQRELATION");
    	System.out.println("--------------------------------------------------------------");
    	System.out.println(metadata.toString());
    	
    	
    	dwhPartitionSql.readDB();
        if(dwhp ==null) {
            dwhp = DwhPartitionService.getData();
        }
        String tableraw=metadata.getTRANSFER_ACTION_NAME();
        String s =tableraw.replaceAll(Pattern.quote("Loader_"), "");
        List<String> activetable=dwhp.getActiveTables("DC_E_ERBS_EUTRANFREQRELATION:RAW");
        System.out.println("Test : "+activetable);
      //  String active1= activetable.get(0);
    	return "Hello World";

		
	}
	

}
