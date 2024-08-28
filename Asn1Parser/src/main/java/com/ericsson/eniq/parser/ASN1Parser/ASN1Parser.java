package com.ericsson.eniq.parser.ASN1Parser;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.ericsson.eniq.parser.*;
import com.ericsson.eniq.parser.MeasurementFileFactory.Channel;

/**
 * 
 * <br>
 * <br>
 * ASN1 Parser is executed via "Generic" Parser action <br>
 * <br>
 * <table border="1" width="100%" cellpadding="3" cellspacing="0">
 * <tr bgcolor="#CCCCFF" class="TableHeasingColor">
 * <td colspan="4"><font size="+2"><b>Parameter Summary</b></font></td>
 * </tr>
 * <tr>
 * <td><b>Name</b></td>
 * <td><b>Key</b></td>
 * <td><b>Description</b></td>
 * <td><b>Default</b></td>
 * </tr>
 * <tr>
 * <td>&nbsp;</td>
 * <td>objectMask</td>
 * <td>Defines the RegExp pattern that is used to retrieve the vendorTag from
 * MeasObjInstID.</td>
 * <td>(.*)</td>
 * </tr>
 * <tr>
 * <td>&nbsp;</td>
 * <td>nullValue</td>
 * <td>Defines the string that is put to the outputfile when null is read from
 * data.</td>
 * <td>NULL</td>
 * </tr>
 * <tr>
 * <td>&nbsp;</td>
 * <td>addVendorIDTo</td>
 * <td>Contains a list of comma delimited vendorTags where the vendorTag is
 * added to the counter name<br>
 * EX. if addVendorIDTo contains meastype A (addVendorIDTo=A) then conter names
 * (a,b,c) of A are changed to A_a, A_b and A_c.</td>
 * <td>NULL</td>
 * </tr>
 * <tr>
 * <td>&nbsp;</td>
 * <td>datetimeIDFormat</td>
 * <td>Defines the format for DATETIME_ID</td>
 * <td>yyyyMMddHHmm</td>
 * </tr>
 * <tr>
 * <td>&nbsp;</td>
 * <td>trimVendorTag</td>
 * <td>is the vendorTag trimmed.</td>
 * <td>true</td>
 * </tr>
 * <tr>
 * <td>&nbsp;</td>
 * <td>trimCounterName</td>
 * <td>Are the counter names trimmed.</td>
 * <td>true</td>
 * </tr>
 * <tr>
 * <td>&nbsp;</td>
 * <td>bufferSize</td>
 * <td>Size of the buffer (in bytes) for the asn1Parser.</td>
 * <td>100000</td>
 * </tr>
 * </table>
 * <br>
 * <br>
 * <table border="1" width="100%" cellpadding="3" cellspacing="0">
 * <tr bgcolor="#CCCCFF" class="TableHeasingColor">
 * <td colspan="2"><font size="+2"><b>Added DataColumns</b></font></td>
 * </tr>
 * <tr>
 * <td><b>Column name</b></td>
 * <td><b>Description</b></td>
 * </tr>
 * <tr>
 * <td>PERIOD_DURATION</td>
 * <td>Contains granularityPeriod from data.</td>
 * </tr>
 * <tr>
 * <td>DATETIME_ID</td>
 * <td>Contains data start time (measTimeStamp - granularityPeriod)</td>
 * </tr>
 * <tr>
 * <td>measTimeStamp</td>
 * <td>Contains measTimeStamp from data.</td>
 * </tr>
 * <tr>
 * <td>senderName</td>
 * <td>Contains senderName from data.</td>
 * </tr>
 * <tr>
 * <td>MeasObjInstId</td>
 * <td>Contains measObjInstId from data.</td>
 * </tr>
 * <tr>
 * <td>nEUserName</td>
 * <td>Contains nEUserName from data.</td>
 * </tr>
 * <tr>
 * <td>nEDistinguishedName</td>
 * <td>Contains nEDistinguishedName from data.</td>
 * </tr>
 * 
 * <tr>
 * <td>measFileFooter</td>
 * <td>Contains measFileFooter from data.</td>
 * </tr>
 * <tr>
 * <td>fileFormatVersion</td>
 * <td>Contains fileFormatVersion from data.</td>
 * </tr>
 * <tr>
 * <td>vendorName</td>
 * <td>Contains vendorName from data.</td>
 * </tr>
 * <tr>
 * <td>senderType</td>
 * <td>Contains senderType from data.</td>
 * </tr>
 * 
 * <tr>
 * <td>collectionBeginTime</td>
 * <td>Contains collectionBeginTime from data.</td>
 * </tr>
 * <tr>
 * <td>Filename</td>
 * <td>Contains the source files filename.</td>
 * </tr>
 * </tr>
 * <tr>
 * <td>vendorTag</td>
 * <td>Parsed from MeasObjInstId. See. objectMask</td>
 * </tr>
 * <tr>
 * <td>DC_SUSPECTFLAG</td>
 * <td>Contains suspectFlag from data.</td>
 * </tr>
 * <tr>
 * <td>DIRNAME</td>
 * <td>Conatins full path to the inputdatafile.</td>
 * </tr>
 * <tr>
 * <td>JVM_TIMEZONE</td>
 * <td>contains the JVM timezone (example. +0200)</td>
 * </tr>
 * </table>
 * <br>
 * <br>
 * 
 * @author savinen
 */

public class ASN1Parser implements ASN1, Callable<Boolean> {
	boolean verbose = false;

	// Virtual machine timezone unlikely changes during execution of JVM
	private static final String JVM_TIMEZONE = (new SimpleDateFormat("Z")).format(new Date());

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");

	StringBuffer verboseStuffer;

	SourceFile sf;

	HashMap map = new HashMap();

	private MeasurementFile MeasurementFile;

	// Added new Variable
	private MeasurementFileFactory Measurementfilefactory;

	protected String fileBaseName;

	private Logger log;

	private String nullValue = "NULL";

	// ***************** Cache stuff ****************************

	//private ParserMeasurementCache measCache;

	private boolean cache = false;

	private int cacheSize;

	// ***************** Worker stuff ****************************

	private String techPack;

	private String setType;

	private String setName;

	private int status = 0;

	// private Main mainParserObject = null;

	private HashSet addVendorIDSet;

	private String workerName = "";

	private String objectMask;

	private String addObjectMask;

	private boolean trimVendorTag = true;

	private boolean trimCounterName = true;

	private int granularityPeriodMultiplier = 1000;

	private final static String ALLTAG = "#ALL#";

	private final static String RULES = "BEGIN\n" + "MeasDataCollection::=SEQUENCE {\n"
			+ "measFileHeader MeasFileHeader,\n" + "measData SEQUENCE OF MeasData,\n"
			+ "measFileFooter MeasFileFooter}\n" + "MeasFileHeader::=SEQUENCE{\n" + "fileFormatVersion INTEGER,\n"
			+ "senderName UTF8String (SIZE(0..400)),\n" + "senderType SenderType,\n"
			+ "vendorName GraphicString (SIZE (0..32)),\n" + "collectionBeginTime TimeStamp}\n"
			+ "SenderType::=GraphicString (SIZE (0..8))\n" + "TimeStamp::=GeneralizedTime\n" + "MeasData::=SEQUENCE {\n"
			+ "nEId NEId,\n" + "measInfo SEQUENCE OF MeasInfo}\n" + "NEId::=SEQUENCE{\n"
			+ "nEUserName GraphicString (SIZE(0..64)),\n" + "nEDistinguishedName GraphicString (SIZE (0..400))}\n"
			+ "MeasInfo::=SEQUENCE {\n" + "measStartTime TimeStamp,\n" + "granularityPeriod INTEGER,\n"
			+ "measTypes SEQUENCE OF MeasType,\n" + "measValues SEQUENCE OF MeasValue}\n"
			+ "MeasType::=GraphicString (SIZE (1..32))\n" + "MeasValue::=SEQUENCE {\n"
			+ "measObjInstId MeasObjInstId,\n" + "measResults SEQUENCE OF MeasResult,\n"
			+ "suspectFlag BOOLEAN DEFAULT FALSE}\n" + "MeasObjInstId::=GraphicString (SIZE (1..64))\n"
			+ "MeasResult::=CHOICE {\n" + "iValue INTEGER (0..4294967295),\n" + "rValue REAL,\n" + "noValue NULL,\n"
			+ "sValue GraphicString (SIZE (0..128))} }\n" + "MeasFileFooter::=TimeStamp\n" + "END";

	private ASN1Handler asn1;

	private ArrayList seqNameList;

	private String seqName;

	private String senderName;

	private int dateFormatLen;

	private String nEUserName;

	private String nEDistinguishedName;

	private String measStartTime;

	private String datetime_id;

	private int granularityPeriod;

	private String vendorTag;

	private String addVendorTag;

	private String measObjInstId;

	private String oldVendorTag = "";

	private String suspectFlag = "false";

	private String measFileFooter;

	private String fileFormatVersion;

	private String senderType;

	private String vendorName;

	private String collectionBeginTime;

	private ArrayList counterList;
	// added interfaceName, channelMap, mFile
	private String interfaceName;

	private Map<String, Channel> channelMap = new HashMap<>();

	private Channel mFile = null;

	private int counterIndex = 0;

	private long parseStartTime;
	private long fileSize = 0L;
	private long totalParseTime = 0L;
	private int fileCount = 0;

	public ASN1Parser(final SourceFile sf, final String techPack, final String setType, final String setName,
			final String workerName) {

		this.sf = sf;
		this.techPack = techPack;
		this.setType = setType;
		this.setName = setName;
		this.status = 1;
		this.workerName = workerName;

		String logWorkerName = "";
		if (workerName.length() > 0) {
			logWorkerName = "." + workerName;
		}

		log = LogManager.getLogger("etl." + techPack + "." + setType + "." + setName + ".parser.ASN1" + logWorkerName);

	}

	public String choice(final ASN1Rule rule, final byte[] data) throws Exception {

		// Fix for TR HR39809
		String dataValue = "";
		int knownType = 0;

		if (rule.type.equalsIgnoreCase("INTEGER")) {
			// return new BigInteger(data) + "";
			dataValue = new BigInteger(data) + "";
			knownType = 1;
		} else if (rule.type.equalsIgnoreCase("REAL")) {
			// return asn1.doReal(data) + "";
			dataValue = asn1.doReal(data) + "";
			knownType = 1;
		} else if (rule.type.equalsIgnoreCase("NULL")) {
			// return nullValue;
			dataValue = nullValue;
			knownType = 1;
		} else if (rule.type.equalsIgnoreCase("GraphicString")) {
			// return asn1.doString(rule, data);
			dataValue = asn1.doString(rule, data);
			knownType = 1;
		}

		// Fix for TR HR39809
		if (knownType == 0) {
			log.warn("Unknown data type " + rule.type + " at " + rule);
			return "N/A";
		} else {
			if (dataValue == null || dataValue.equalsIgnoreCase("null")) {
				return nullValue;
			} else {
				return dataValue;
			}
		}
	}

	public int status() {
		return status;
	}

	// changed tadId to String from Int
	@Override
	public void seqStart(int tagID, int length, String name) throws Exception {
		// TODO Auto-generated method stub

		// // push sequence name to stack
		seqNameList.add(0, name);
		seqName = name;

		if (seqName.equalsIgnoreCase("MeasType")) {
			counterList = new ArrayList();
		} else if (seqName.equalsIgnoreCase("MeasResult")) {

//			if (cache) {
//				if (mFile == null) {
//		//			mFile = new Channel("", cacheSize,log);
//				}
////				measCache.setTP(techPack);
////				measCache.setSetType(setType);
////				measCache.setSetName(setName);
////				measCache.setWorkerName(workerName);
////				measCache.setSourceFile(sf);
//
//			} else {
				// create new measurementFile
				if (mFile == null || !oldVendorTag.equalsIgnoreCase(vendorTag)) {

					if (mFile != null) {
						mFile=null;
					}

					this.log.trace("Using vendorTag = " + vendorTag);

					// Add Channel

//					MeasurementFile = Main.createMeasurementFile(sf, vendorTag, techPack, setType, setName, workerName,
//							log);
					 mFile= getChannel(tagID);
				}
			}
			oldVendorTag = vendorTag;
			counterIndex = 0;
			suspectFlag = "false";

	//	}

		if (verbose) {
			log.debug(verboseStuffer + "Sequence[" + name + "]{ ");
			verboseStuffer.append(" ");
		}

	}

	@Override
	public void seqEnd(int tagID, String name) throws Exception {

		// pop sequence name to stack

		if (seqNameList.isEmpty()) {
			throw new Exception("ASN1 Error: Unexpected sequence end after [" + seqName + "].");
		}

		final String thisSeqName = (String) seqNameList.remove(0);

		if (!seqNameList.isEmpty()) {
			seqName = (String) seqNameList.get(0);
		}

		// if this seq is measValue and the next seq is also measvalue this is
		// valid
		// measurement,
		// if the next value is != measvalue this is the last seq of measurement
		// and
		// no need to create dataline.
		if (thisSeqName.equalsIgnoreCase("MeasValue") && seqName.equalsIgnoreCase("MeasValue")) {

//			if (cache) {
//
//				measCache.put("PERIOD_DURATION", "" + granularityPeriod);
//				measCache.put("DATETIME_ID", datetime_id);
//				measCache.put("measStartTime", measStartTime);
//				measCache.put("senderName", senderName);
//				measCache.put("measObjInstId", measObjInstId);
//				measCache.put("MeasObjInstId", measObjInstId);
//				measCache.put("measFileFooter", measFileFooter);
//				measCache.put("fileFormatVersion", fileFormatVersion);
//				measCache.put("senderType", senderType);
//				measCache.put("vendorName", vendorName);
//				measCache.put("collectionBeginTime", collectionBeginTime);
//				measCache.put("nEUserName", nEUserName);
//				measCache.put("nEDistinguishedName", nEDistinguishedName);
//				measCache.put("Filename", sf.getName());
//				measCache.put("vendorTag", vendorTag);
//				measCache.put("DC_SUSPECTFLAG", suspectFlag);
//				measCache.put("DIRNAME", sf.getDir());
//				measCache.put("JVM_TIMEZONE", JVM_TIMEZONE);
//
//				measCache.endOfRow(vendorTag, techPack);
//
//			} else {
				if (mFile != null) {
					Map< String, String > channelMap1 = new HashMap<>();

					channelMap1.put("PERIOD_DURATION", "" + granularityPeriod);
					channelMap1.put("DATETIME_ID", datetime_id);
					channelMap1.put("measStartTime", measStartTime);
					channelMap1.put("senderName", senderName);
					channelMap1.put("measObjInstId", measObjInstId);
					channelMap1.put("MeasObjInstId", measObjInstId);
					channelMap1.put("measFileFooter", measFileFooter);
					channelMap1.put("fileFormatVersion", fileFormatVersion);
					channelMap1.put("senderType", senderType);
					channelMap1.put("vendorName", vendorName);
					channelMap1.put("collectionBeginTime", collectionBeginTime);
					channelMap1.put("nEUserName", nEUserName);
					channelMap1.put("nEDistinguishedName", nEDistinguishedName);
					channelMap1.put("Filename", sf.getName());
					channelMap1.put("vendorTag", vendorTag);
					channelMap1.put("DC_SUSPECTFLAG", suspectFlag);
					channelMap1.put("DIRNAME", sf.getDir());
					channelMap1.put("JVM_TIMEZONE", JVM_TIMEZONE);
					mFile.pushData(channelMap1);
					//MeasurementFile.saveData();
				}

				// MeasurementFile = null;
				counterIndex = 0;
			}
		// }

		if (verbose) {
			verboseStuffer.delete(0, 1);
			log.debug(verboseStuffer + "}" + name + "");
		}

	}

	public void primitive(final int tagID, final String type, final byte[] data, final ASN1Rule rule) throws Exception {
		HashMap <String, String> channelMap1 = new HashMap<>();
		if (seqName.equalsIgnoreCase("MeasDataCollection")) {

			if (rule.name.equalsIgnoreCase("TimeStamp")) {
				measFileFooter = asn1.doString(rule, data);

			}

		} else if (seqName.equalsIgnoreCase("MeasFileHeader")) {

			// handle header
			if (rule.name.equalsIgnoreCase("fileFormatVersion")) {
				fileFormatVersion = "" + new BigInteger(data);

			} else if (rule.name.equalsIgnoreCase("senderName")) {
				senderName = asn1.doString(rule, data);

			} else if (rule.name.equalsIgnoreCase("senderType")) {
				senderType = asn1.doString(rule, data);

			} else if (rule.name.equalsIgnoreCase("vendorName")) {
				vendorName = asn1.doString(rule, data);

			} else if (rule.name.equalsIgnoreCase("collectionBeginTime")) {
				collectionBeginTime = asn1.doString(rule, data);

			}

		} else if (seqName.equalsIgnoreCase("MeasData")) {

		} else if (seqName.equalsIgnoreCase("NEId")) {

			if (rule.name.equalsIgnoreCase("nEDistinguishedName")) {
				nEDistinguishedName = asn1.doString(rule, data);
			} else if (rule.name.equalsIgnoreCase("nEUserName")) {
				nEUserName = asn1.doString(rule, data);
			}

		} else if (seqName.equalsIgnoreCase("MeasInfo")) {

			if (rule.name.equalsIgnoreCase("TimeStamp")) {
				measStartTime = asn1.doString(rule, data);
			} else if (rule.name.equalsIgnoreCase("granularityPeriod")) {
				granularityPeriod = new BigInteger(data).intValue();
				if (granularityPeriod > 0) {
					// The measStartTime must be fixed. measStartTime is
					// actually the
					// endtime, so granularityPeriod must be reduced from the
					// measStartTime.
					// granularityPeriod is in seconds. Convert it to
					// milliseconds.
					// put the calculated starttime to DATETIME_ID so original
					// string in
					// measStartTime is unchanged.

					final long granPeriodMillis = granularityPeriod * granularityPeriodMultiplier;
					final String oldMeasStartTime = measStartTime;
					try {

						final long measStartTimeTs = dateFormat.parse(measStartTime.substring(0, dateFormatLen))
								.getTime();

						final long measStartTimeTsTmp = (measStartTimeTs - granPeriodMillis);

						final Date measStartDate = new Date(measStartTimeTsTmp);

						datetime_id = dateFormat.format(measStartDate) + measStartTime.substring(dateFormatLen);

						this.log.trace("Fixed DATETIME_ID from " + oldMeasStartTime + " to " + datetime_id
								+ " because granularityPeriod has value " + granularityPeriod);

					} catch (Exception e) {
						log.log(Level.WARN, "Failed to fix old DATETIME_ID " + oldMeasStartTime
								+ " with new value reduced by granularityPeriod.", e);
					}
				}
			}

		} else if (seqName.equalsIgnoreCase("MeasType")) {

			counterList.add(asn1.doString(rule, data));

		} else if (seqName.equalsIgnoreCase("MeasValue")) {

			if (rule.name.equalsIgnoreCase("measObjInstId")) {

				measObjInstId = asn1.doString(rule, data);

				if (trimVendorTag) {
					vendorTag = parseFileName(measObjInstId, objectMask).trim();
					addVendorTag = parseFileName(measObjInstId, addObjectMask).trim();
				} else {
					vendorTag = parseFileName(measObjInstId, objectMask);
					addVendorTag = parseFileName(measObjInstId, addObjectMask);
				}

			} else if (rule.name.equalsIgnoreCase("suspectFlag")) {
				suspectFlag = "" + asn1.doBoolean(data);
			}

		} else if (seqName.equalsIgnoreCase("MeasResult")) {

			if (addVendorIDSet.isEmpty()) {


				//				if (cache) {
//					measCache.put((String) counterList.get(counterIndex), choice(rule, data));
//				} else {
				channelMap1.put((String) counterList.get(counterIndex), choice(rule, data));
					mFile.pushData(channelMap1);
					this.log.trace(
							"Adding data " + (String) counterList.get(counterIndex) + " = " + choice(rule, data));
				// }

			} else if (addVendorIDSet.contains(ALLTAG)) {

//				if (cache) {
//					measCache.put(addVendorTag + "_" + counterList.get(counterIndex), choice(rule, data));
//				} else {
				channelMap1.put(addVendorTag + "_" + counterList.get(counterIndex), choice(rule, data));
				mFile.pushData(channelMap1);
					this.log.trace("Adding data " + addVendorTag + "_" + counterList.get(counterIndex) + " = "
							+ choice(rule, data));
				// }

			} else {

				String cname = "";

				if (trimCounterName) {

					cname = ((String) counterList.get(counterIndex)).trim();

				} else {

					cname = ((String) counterList.get(counterIndex));
				}

				if (addVendorIDSet.contains(vendorTag)) {
//					if (cache) {
//						measCache.put(addVendorTag + "_" + cname, choice(rule, data));
//					} else {
				//	mFile=getChannel(1);
					channelMap1.put(addVendorTag + "_" + cname, choice(rule, data));
					mFile.pushData(channelMap1);
						this.log.trace("Adding data " + addVendorTag + "_" + cname + " = " + choice(rule, data));
					// }
				}
			else {
//					if (cache) {
//						measCache.put(cname, choice(rule, data));
//					} else {
				channelMap1.put(cname, choice(rule, data));
				mFile.pushData(channelMap1);
						this.log.trace("Adding data " + cname + " = " + choice(rule, data));
					//}

				}
		//	}

			counterIndex++;
		}

		if (verbose) {
			log.debug(verboseStuffer + "primitive[(" + rule.name + ") " + rule.type + "] " + asn1.doData(rule, data)
					+ " ");
		}
		}

	}

	public void eof() throws Exception {
//		if (cache) {
//			measCache.saveData();
//		}
	}

	@Override
	public Boolean call() throws Exception {
		try {
			//
			this.status = 2;
			// SourceFile sf = null;
			parseStartTime = System.currentTimeMillis();

			// while ((sf = mainParserObject.nextSourceFile()) != null) {

			// try {
			// fileCount++;
			// fileSize += sf.fileSize();
			// mainParserObject.preParse(sf);
			parse(sf, techPack, setType, setName);
			// mainParserObject.postParse(sf);
			// } catch (Exception e) {
			// mainParserObject.errorParse(e, sf);
			// } finally {
			// mainParserObject.finallyParse(sf);
			// }
			// }
			totalParseTime = System.currentTimeMillis() - parseStartTime;
			if (totalParseTime != 0) {
				log.info("Parsing Performance :: " + fileCount + " files parsed in " + totalParseTime
						+ " ms, filesize is " + fileSize + " bytes and throughput : " + (fileSize / totalParseTime)
						+ " bytes/ms.");
			}
		} catch (Exception e) {
			// Exception catched at top level. No good.
			log.log(Level.WARN, "Worker parser failed to exception", e);
		} finally {
			
			this.status = 3;
		}
		return cache;
	}

	public String parseFileName(final String str, final String regExp) {

		final Pattern pattern = Pattern.compile(regExp);
		final Matcher matcher = pattern.matcher(str);

		if (matcher.matches()) {
			final String result = matcher.group(1);
			log.trace(" regExp (" + regExp + ") found from " + str + "  :" + result);
			return result;
		} else {
			log.warn("String " + str + " doesn't match defined regExp " + regExp);
		}

		return "";

	}

	// // ***************** Worker stuff ****************************
	//
	// *//**
	// *
	// * @see
	// com.distocraft.dc5000.etl.parser.Parser#parse(com.distocraft.dc5000.etl.parser.SourceFile,
	// * java.lang.String, java.lang.String, java.lang.String)
	// *//*
	public void parse(final SourceFile sf, final String techPack, final String setType, final String setName)
			throws Exception {

		verboseStuffer = new StringBuffer();
		seqNameList = new ArrayList();
		MeasurementFile = null;

		this.sf = sf;

		final String rules = sf.getProperty("rules", RULES);

		objectMask = sf.getProperty("vendorIDMask", "(.*)");
		addObjectMask = sf.getProperty("addVendorIDMask", objectMask);

		// cache props
		cache = sf.getProperty("cache", "false").equalsIgnoreCase("true");
		cacheSize = Integer.parseInt(sf.getProperty("cacheSize", "5000"));

		verbose = "TRUE".equalsIgnoreCase(sf.getProperty("verbose", "false"));
		trimVendorTag = "TRUE".equalsIgnoreCase(sf.getProperty("trimVendorTag", "true"));
		trimCounterName = "TRUE".equalsIgnoreCase(sf.getProperty("trimCounterName", "true"));
		final int bufferSize = Integer.parseInt(sf.getProperty("bufferSize", "100000"));
		granularityPeriodMultiplier = Integer.parseInt(sf.getProperty("granularityPeriodMultiplier", "1000"));
		final String dateFormatStr = sf.getProperty("datetimeIDFormat", "yyyyMMddHHmm");
		dateFormat = new SimpleDateFormat(dateFormatStr);
		dateFormatLen = dateFormatStr.length();

		nullValue = sf.getProperty("nullValue", "");
		final String addVendorIDTmp = sf.getProperty("addVendorIDTo", "");

		final String[] addVendorIDs = addVendorIDTmp.split(",");

		addVendorIDSet = new HashSet();

		if (addVendorIDTmp.equalsIgnoreCase("all")) {
			addVendorIDSet.add(ALLTAG);
		} else {
//			for (int i = 0; i < addVendorIDs.length; i++) {
//				addVendorIDSet.add(addVendorIDs[i]);
//			}
			List<String> data=Arrays.asList("ABISIP","ABISTG","AGW","AGWTRAF","AOIP","AOIPCAP","BSC","BSCAMSG","BSCGEN","BSCGPRS",
					"BSCGPRS2","BSCMSLOT","BSCPOS","BSCRFSUP","BSCSCCCL"
					,"C7ODS","C7ODS24","C7OPCDS","C7OPCDS24","C7RTTOTAL","C7SCCPUSE"
					,"C7SCPERF","C7SCQOS","C7SL1","C7SL2",
					"CCCHLOAD","CELEVENTD","CELEVENTH","CELEVENTI","CELEVENTS",
					"CELEVENTSC","CELLAFFER","CELLAHFER","CELLANR",
					"CELLAWFER","CELLBTSPS","CELLCBCH","CELLCCH"
					,"CELLCCHDR","CELLCCHHO","CELLCONF","CELLDUALT","CELLDYNPC","CELLEFFER"
					,"CELLEIT","CELLEIT2","CELLEVENT","CELLFERF","CELLFERH",
					"CELLFFER","CELLFLXAB","CELLGEN","CELLGPRS","CELLGPRS2","CELLGPRS3","CELLGPRS4"
					,"CELLGPRSB","CELLGPRSEC","CELLGPRSO","CELLHCS","CELLHFER","CELLHO","CELLHSCSD"
					,"CELLMSCAP","CELLMSQ","CELLPAG","CELLPAGB","CELLPOS","CELLQOSEC","CELLQOSEG",
					"CELLQOSG","CELLQOSS","CELLSDCCH","CELLSQI","CELLSQIDL","CELLTCH","CELLTCHDR",
					"CELTCHF","CELTCHFP","CELTCHFV","CELTCHH","CELTCHHV","CHGRP0F","CHGRP0H","CHGRP0SQI","CLBCCHPS","CLCCCH","CLCCHEST1","CLCCHEST1O","CLCCHEST2","CLCCHEST2O","CLCSFB","CLCTRLBL","CLDTMEST","CLDTMPER","CLDTMQOS","CLE2ADBL","CLE2ARTDL","CLE2ARTUL","CLEDBL","CLENHREL","CLERETRDL","CLERETRUL","CLEVENTIV","CLGDBL","CLGPRSE2","CLGPRSE2O","CLGPRSTSC","CLGRETR","CLPSDLPC","CLPSVOLSN","CLQOSE2A","CLQOSSCON","CLQOSSCON2","CLRATECHG","CLRXQUAL","CLSDCCH","CLSDCCHO","CLSMS","CLSQIDLV","CLSQIULV","CLTCH","CLTCHDRAF","CLTCHDRAH","CLTCHDRAW","CLTCHDRF","CLTCHDRH","CLTCHEAS","CLTCHF","CLTCHFV1","CLTCHFV2","CLTCHFV3","CLTCHFV3C","CLTCHFV5","CLTCHFV5C","CLTCHH","CLTCHHV1","CLTCHHV2","CLTCHHV3","CLTCHHV3C","CLTRAFSN","CLVGCSEST","CP","DELSTRTBF","DIP","DIPPRM","DOWNTIME","ECCCCHLOAD","EM","EMG","EMRP","GANDIREPA","GANRESALL","GANRPLOAD","GANTRAF","GANTRAFEV","GPHLOADREG","GPRSCAP","GPRSGEN","GRPSWITCH","GSH","IDLECH","IDLEOTCHF","IDLEOTCHH","IDLEUTCHF","IDLEUTCHH","LOADREG","LOAS","LOAS1","LOASINCO","LOASMISC","MIBICMPMST","MIBICMPST","MIBIFT","MIBIFXT","MIBIPIFST","MIBIPIFST2","MIBIPLCG","MIBIPSST","MIBIPSST2","MIBIPV4G","MIBIPV4IT","MIBSCTPLG","MIBSCTPST2","MOTG","MRVTOT","MSCENHREL","NETWSYNC","NONRES64K","PGW","PGWLDIST","PREEMP","RANDOMACC","RES64K","RLBITRE2A","RLINKBITR","RNDACCEXT","RP","SCTPAM","SCTPLM","SS7SCCPUSE","SS7SCQOS","SS7SLMT1","SS7SLMT2","SS7SLTRAFF","SS7TIMERS","SS7TOTAL","SS7TOTAL2","SUCORCL","SUPERCH2","TCUSE","TG","TRAFDLGPRS","TRAFE2DL1","TRAFE2DL2","TRAFE2UL1","TRAFE2UL2","TRAFEEVO","TRAFGPRS2","TRAFGPRS3","TRAFGPRS4","TRAFULGPRS","TRALOST","TRAPCOM","TRASEVENT","TRH","CLTCHFV5C","ASITERXQ","ASITESQIDL","ASITESQIUL","ASITETCHDR","SCAGGR","CLGPRSPEO"
					,"CLRXQUALO","CELLGASC","ECCRU","ECCTG","CLRXQUALC0","CLECCCCH");
			addVendorIDSet.addAll(data);
		}
		// log.fine("objectMask: " + objectMask);
		// log.fine("rules: " + rules);
		log.trace("objectMask: " + objectMask);
		log.trace("rules: " + rules);

		asn1 = new ASN1Handler(this);
		asn1.init(sf.getFileInputStream());
		asn1.setBuffer(bufferSize);
		asn1.setRules(rules);
		asn1.parse();
	}

	private Channel getChannel(int tagId) {
		
		
		//String tagId1=String.valueOf(tagId);
		String tagId1="LAPD";
		
		try {
			// String folderName = MeasurementFileFactory.getFolderName(sf, interfaceName, tagId1, log);
			 String folderName = "DC_E_BSS_LAPD";

			Channel channel;
			if (folderName != null) {
				if ((channel = channelMap.get(folderName)) != null) {
					return channel;
				}
				channel = MeasurementFileFactory.createChannel(sf, tagId1, folderName, log, null);
				channelMap.put(folderName, channel);
				return channel;
			}
		} catch (Exception e) {
			log.log(Level.WARN, "Exception while getting channel : ", e);
		}
		return null;
	}

}
