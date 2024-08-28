//package com.ericsson.oss.eniq.dataingress.service.outputstream;
//
//import static org.junit.Assert.assertNotNull;
//
//import org.apache.avro.generic.GenericRecord;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class LoadFileTest {
//
//	LoadFile loadFile=null;
//	private String outDir="";
//	private String folderName="";
//	@Autowired
//	GenericRecord record=null;
//	private long currentId;
//	
//	private static final int THRESHOLD_NUMBER = 20;
//	private int count = 0;
//	
//	@Before
//	public void setUp() throws Exception {
//		loadFile=new LoadFile(outDir, folderName);
//		
//	}
//
//	
//
//	@Test
//	public void testLoadFile() {
//		assertNotNull(outDir);
//		assertNotNull(folderName);
//	}
//
//	@Test
//	public void testSave() {
//		assertNotNull(THRESHOLD_NUMBER);
//		if(count>=THRESHOLD_NUMBER){
//			assertNotNull(currentId);
//		}
//		
//	}
//
//	@Test
//	public void testGetOutFileName() {
//		assertNotNull(folderName);
//	}
//	
//	
//}
