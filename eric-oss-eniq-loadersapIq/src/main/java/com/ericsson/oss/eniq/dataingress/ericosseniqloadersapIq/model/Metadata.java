package com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.model;

public class Metadata {
	
    private String VERSION_NUMBER;
    private Long TRANSFER_ACTION_ID;
    private Long COLLECTION_ID;
    private Long COLLECTION_SET_ID;
    private String ACTION_TYPE;
    private String TRANSFER_ACTION_NAME;
    private Long ORDER_BY_NO;
    private String DESCRIPTION;
    private String WHERE_CLAUSE_01;
    private String ACTION_CONTENTS_01;
    private String ENABLED_FLAG;
    private Long CONNECTION_ID;
    private String WHERE_CLAUSE_02;
    private String WHERE_CLAUSE_03;
    private String ACTION_CONTENTS_02;
    private String ACTION_CONTENTS_03;
    
	public String getVERSION_NUMBER() {
		return VERSION_NUMBER;
	}
	
	public void setVERSION_NUMBER(String vERSION_NUMBER) {
		VERSION_NUMBER = vERSION_NUMBER;
	}
	
	public Long getTRANSFER_ACTION_ID() {
		return TRANSFER_ACTION_ID;
	}
	
	public void setTRANSFER_ACTION_ID(Long tRANSFER_ACTION_ID) {
		TRANSFER_ACTION_ID = tRANSFER_ACTION_ID;
	}
	
	public Long getCOLLECTION_ID() {
		return COLLECTION_ID;
	}
	
	public void setCOLLECTION_ID(Long cOLLECTION_ID) {
		COLLECTION_ID = cOLLECTION_ID;
	}
	
	public Long getCOLLECTION_SET_ID() {
		return COLLECTION_SET_ID;
	}
	
	public void setCOLLECTION_SET_ID(Long cOLLECTION_SET_ID) {
		COLLECTION_SET_ID = cOLLECTION_SET_ID;
	}
	
	public String getACTION_TYPE() {
		return ACTION_TYPE;
	}
	
	public void setACTION_TYPE(String aCTION_TYPE) {
		ACTION_TYPE = aCTION_TYPE;
	}
	
	public String getTRANSFER_ACTION_NAME() {
		return TRANSFER_ACTION_NAME;
	}
	
	public void setTRANSFER_ACTION_NAME(String tRANSFER_ACTION_NAME) {
		TRANSFER_ACTION_NAME = tRANSFER_ACTION_NAME;
	}
	
	public Long getORDER_BY_NO() {
		return ORDER_BY_NO;
	}
	
	public void setORDER_BY_NO(Long oRDER_BY_NO) {
		ORDER_BY_NO = oRDER_BY_NO;
	}
	
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	
	public String getWHERE_CLAUSE_01() {
		return WHERE_CLAUSE_01;
	}
	
	public void setWHERE_CLAUSE_01(String wHERE_CLAUSE_01) {
		WHERE_CLAUSE_01 = wHERE_CLAUSE_01;
	}
	
	public String getACTION_CONTENTS_01() {
		return ACTION_CONTENTS_01;
	}
	
	public void setACTION_CONTENTS_01(String aCTION_CONTENTS_01) {
		ACTION_CONTENTS_01 = aCTION_CONTENTS_01;
	}
	
	public String getENABLED_FLAG() {
		return ENABLED_FLAG;
	}
	
	public void setENABLED_FLAG(String eNABLED_FLAG) {
		ENABLED_FLAG = eNABLED_FLAG;
	}
	
	public Long getCONNECTION_ID() {
		return CONNECTION_ID;
	}
	
	public void setCONNECTION_ID(Long cONNECTION_ID) {
		CONNECTION_ID = cONNECTION_ID;
	}
	
	public String getWHERE_CLAUSE_02() {
		return WHERE_CLAUSE_02;
	}
	
	public void setWHERE_CLAUSE_02(String wHERE_CLAUSE_02) {
		WHERE_CLAUSE_02 = wHERE_CLAUSE_02;
	}
	
	public String getWHERE_CLAUSE_03() {
		return WHERE_CLAUSE_03;
	}
	
	public void setWHERE_CLAUSE_03(String wHERE_CLAUSE_03) {
		WHERE_CLAUSE_03 = wHERE_CLAUSE_03;
	}
	
	public String getACTION_CONTENTS_02() {
		return ACTION_CONTENTS_02;
	}
	
	public void setACTION_CONTENTS_02(String aCTION_CONTENTS_02) {
		ACTION_CONTENTS_02 = aCTION_CONTENTS_02;
	}
	
	public String getACTION_CONTENTS_03() {
		return ACTION_CONTENTS_03;
	}
	
	public void setACTION_CONTENTS_03(String aCTION_CONTENTS_03) {
		ACTION_CONTENTS_03 = aCTION_CONTENTS_03;
	}
	
	@Override
	public String toString() {
		return "Metadata [VERSION_NUMBER=" + VERSION_NUMBER + ", TRANSFER_ACTION_ID=" + TRANSFER_ACTION_ID
				+ ", COLLECTION_ID=" + COLLECTION_ID + ", COLLECTION_SET_ID=" + COLLECTION_SET_ID + ", ACTION_TYPE="
				+ ACTION_TYPE + ", TRANSFER_ACTION_NAME=" + TRANSFER_ACTION_NAME + ", ORDER_BY_NO=" + ORDER_BY_NO
				+ ", DESCRIPTION=" + DESCRIPTION + ", WHERE_CLAUSE_01=" + WHERE_CLAUSE_01 + ", ACTION_CONTENTS_01="
				+ ACTION_CONTENTS_01 + ", ENABLED_FLAG=" + ENABLED_FLAG + ", CONNECTION_ID=" + CONNECTION_ID
				+ ", WHERE_CLAUSE_02=" + WHERE_CLAUSE_02 + ", WHERE_CLAUSE_03=" + WHERE_CLAUSE_03
				+ ", ACTION_CONTENTS_02=" + ACTION_CONTENTS_02 + ", ACTION_CONTENTS_03=" + ACTION_CONTENTS_03 + "]";
	}
	
}
