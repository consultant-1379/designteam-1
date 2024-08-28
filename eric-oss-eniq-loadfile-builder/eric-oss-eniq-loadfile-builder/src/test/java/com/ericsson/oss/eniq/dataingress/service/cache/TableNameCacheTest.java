package com.ericsson.oss.eniq.dataingress.service.cache;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TableNameCacheTest {

	private static Map<String, String> tagIdToTableNameMap = new ConcurrentHashMap<>();
	private static Map<String, String> tagIdToTpMap = new ConcurrentHashMap<>();

	TableNameCache tableNameCache=null;

	@Before
	public void setUp() throws Exception {
		tableNameCache =	new TableNameCache();
		tagIdToTableNameMap.put("MeCont", "DIM_E_CN:((166)):DIM_E_CN_AXE:csexport");
		tagIdToTableNameMap.put("Network", "DIM_E_CN:((166)):DIM_E_CN_AXE:csexport");
		
		tagIdToTpMap.put("CCDM","DIM_E_CN:((166)):DIM_E_CN_CCDM:ct");
		tagIdToTpMap.put("CCPC","DIM_E_CN:((166)):DIM_E_CN_CCPC:ct");
	}



	@Test
	public void testGetFolderName() {
		String strVal=tableNameCache.getFolderName("MeCont");

		assertEquals(strVal, tagIdToTableNameMap.get(0));
	}

	@Test
	public void testGetTpName() {
		String strVal = tableNameCache.getTpName("CCDM");
		assertEquals(strVal, tagIdToTpMap.get(0));
	}

	@After
	public void tearDown() throws Exception {

		tagIdToTableNameMap.clear();
		tagIdToTpMap.clear();
	}

}
