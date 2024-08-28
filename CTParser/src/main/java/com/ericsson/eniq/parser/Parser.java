package com.ericsson.eniq.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.xml.parsers.SAXParserFactory;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.ericsson.eniq.parser.MeasurementFileFactory.Channel;
import com.ericsson.eniq.parser.sink.ISink;
import com.ericsson.eniq.parser.util.FlsUtils;

/**
 * 
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
 * <td>VendorID from</td>
 * <td>readVendorIDFrom</td>
 * <td>Defines where the vendorID is retrieved from <b>data</b> (moid-tag) or
 * from <b>filename</b>. RegExp is used to further define the actual vendorID.
 * Vendor id is added to the outputdata as objectClass. See. VendorID Mask and
 * objectClass</td>
 * <td>data</td>
 * </tr>
 * <tr>
 * <td>VendorID Mask</td>
 * <td>vendorIDMask</td>
 * <td>Defines the RegExp mask that is used to extract the vendorID from either
 * data or filename. See. VendorID from</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>&nbsp;</td>
 * <td>multiline</td>
 * <td>are multiple sequences (seq) handled as separate lines (multiline = true)
 * or single line with items comma delimited (multiline = false).</td>
 * <td>false</td>
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
 * <td>filename</td>
 * <td>contains the filename of the inputdatafile.</td>
 * </tr>
 * <tr>
 * <td>fdn</td>
 * <td>contains the data from FDN tag.</td>
 * </tr>
 * <tr>
 * <td>seq</td>
 * <td>contains the data from MOID tag.</td>
 * </tr>
 * <tr>
 * <td>PERIOD_DURATION</td>
 * <td>contains the data from GP tag.</td>
 * </tr>
 * <tr>
 * <td>DATETIME_ID</td>
 * <td>contains the data from CBT tag.</td>
 * </tr>
 * <tr>
 * <td>objectClass</td>
 * <td>contains the same data as in vendorID (see. readVendorIDFrom)</td>
 * </tr>
 * <tr>
 * <td>DC_SUSPECTFLAG</td>
 * <td>contains the sf -tag.</td>
 * </tr>
 * <tr>
 * <td>DIRNAME</td>
 * <td>Conatins full path to the inputdatafile.</td>
 * </tr>
 * <tr>
 * <td>JVM_TIMEZONE</td>
 * <td>contains the JVM timezone (example. +0200)</td>
 * </tr>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * </table>
 * <br>
 * <br>
 * 
 * @author savinen <br>
 *         <br>
 * 
 */
public class Parser extends DefaultHandler implements Callable<Boolean> {

	// Virtual machine timezone unlikely changes during execution of JVM
	private static final String JVM_TIMEZONE = (new SimpleDateFormat("Z")).format(new Date());
	ISink sink;
	private Logger log;

	private SourceFile sourceFile;

	private String charValue = "";

	private String fdn;

	private String objectMask;

	private String objectClass;

	private String oldObjectClass;

	private String readVendorIDFrom;

	private String key;

	private String seqKey = "";

	private int seqCount = 0;

	private ArrayList seqContainer = null;

	private Channel measFile = null;

	private Map measData;

	private boolean multiLine = false;
	private Map<String, String> itemValuesMap = new HashMap<String, String>();

	// ***************** Worker stuff ****************************

	private String techPack;

	private String setType;

	private String setName;

	private int status = 0;

	private final static String EMPTY_SUSPECT_FLAG = "";

	private String workerName = "";

	private static String nodeVersion;

	private static boolean flag;

	private static boolean flag1;

	// ******************60K

	private HashMap<String, String> nodefdn;

	private String ne_type = "";

	private boolean ne_typeExist = false;

	private boolean isFlsEnabled = false;

	private String serverRole = null;

	private Map<String, String> ossIdToHostNameMap;
	private long parseStartTime;
	private long fileSize = 0L;
	private long totalParseTime = 0L;
	private int fileCount = 0;
	private String interfaceName;
	private Map<String, Channel> channelMap = new HashMap<>();

	public Parser(final SourceFile sourceFile, final String techPack, final String setType, final String setName,
			final String workerName) {
		this.sourceFile = sourceFile;
		this.techPack = techPack;
		this.setType = setType;
		this.setName = setName;
		this.status = 1;
		this.workerName = workerName;

		String logWorkerName = "";
		if (workerName.length() > 0) {
			logWorkerName = "." + workerName;
		}

		log = LogManager.getLogger("etl." + techPack + "." + setType + "." + setName + ".parser.XML" + logWorkerName);
	}

	public int status() {
		return status;
	}

	@Override
	public Boolean call() {

		try {
			this.status = 2;
			long parseStartTime = System.currentTimeMillis();
			long fileSize = sourceFile.fileSize();
			interfaceName = sourceFile.getProperty("interfaceName", "");
			parse(sourceFile, techPack, setType, setName);
			long totalParseTime = System.currentTimeMillis() - parseStartTime;
			fileCount++;
			if (totalParseTime != 0) {
				log.info("Parsing Performance :: " + fileCount + " files parsed in " + totalParseTime / 1000
						+ " sec, filesize is " + fileSize / 1000 + " Kb and throughput : " + (fileSize / totalParseTime)
						+ " bytes/ms.");
			}
		} catch (Exception e) {
			// Exception catched at top level. No good.
			log.log(Level.WARN, "Worker parser failed to exception", e);
		} finally {
			this.status = 3;
		}
		return true;
	}

	public void parse(final SourceFile sf, final String techPack, final String setType, final String setName)
			throws Exception {
		measData = new HashMap();

		this.sourceFile = sf;

		log.log(Level.INFO, "Reading configuration...");

		SAXParserFactory factory = SAXParserFactory.newInstance();
		final XMLReader xmlReader = factory.newSAXParser().getXMLReader();
		xmlReader.setContentHandler(this);
		xmlReader.setErrorHandler(this);

		objectMask = sf.getProperty("vendorIDMask", ".+,(.+)=.+");
		readVendorIDFrom = sf.getProperty("readVendorIDFrom", "data");
		multiLine = "TRUE".equalsIgnoreCase(sf.getProperty("multiline", "false"));

		log.log(Level.INFO, "Staring to parse...");

		xmlReader.setEntityResolver(new EntityResolver());
		xmlReader.parse(new InputSource(sf.getFileInputStream()));

		log.log(Level.INFO, "Parse finished.");
	}

	/**
	 * Event handlers
	 */
	@Override
	public void startDocument() {

		oldObjectClass = null;
		nodefdn = new HashMap<String, String>();
	}

	@Override
	public void endDocument() throws SAXException {

		try {
			String dir = sourceFile.getProperty("inDir");
			String enmDir = getOSSIdFromInDir(dir);
			String enmAlias = MeasurementFile.resolveDirVariable(enmDir);
			isFlsEnabled = FlsUtils.isFlsEnabled(enmAlias);
			/*
			 * if (isFlsEnabled) { multiEs = (IEnmInterworkingRMI) Naming
			 * .lookup(RmiUrlFactory.getInstance().getMultiESRmiUrl(EnmInterCommonUtils.
			 * getEngineIP())); String enmHostName = FlsUtils.getEnmShortHostName(enmAlias);
			 * try { for (Map.Entry<String, String> entry : nodefdn.entrySet()) { if ( (
			 * entry.getValue() != null && !entry.getValue().equals("") ) && (
			 * entry.getKey() != null && !entry.getKey().equals("") ) && enmHostName !=
			 * null){ log.
			 * info("In FLS mode. Adding FDN to the Automatic Node Assignment Blocking Queue "
			 * + entry.getValue() + " " + entry.getKey() + " " + enmHostName);
			 * multiEs.addingToBlockingQueue(entry.getValue(), entry.getKey(), enmHostName);
			 * }
			 * 
			 * } } catch (Exception abqE) { log.log(Level.WARN,
			 * "Exception occured while adding FDN to Blocking Queue!", abqE); }
			 * 
			 * } else { log.log(Level.
			 * INFO,"FLS is not enabled. FDN will not be added to blocking Queue!"); }
			 */
		} catch (Exception e) {
			log.log(Level.WARN, "Exception occured while checking for FLS", e);
		}
		log.log(Level.INFO, "End document");
		measData.clear();
		sourceFile = null;
		itemValuesMap.clear();

		// reset seq also
		seqKey = "";
		seqCount = 0;
		seqContainer = null;
	}

	@Override
	public void startElement(final String uri, final String name, final String qName, final Attributes atts)
			throws SAXException {

		charValue = "";

		if (qName.equals("mo")) {

			// read object class from data
			for (int i = 0; i < atts.getLength(); i++) {
				if (atts.getLocalName(i).equalsIgnoreCase("fdn")) {
					fdn = atts.getValue(i);
					log.log(Level.INFO, "fdn: " + fdn);
				}
			}

			// where to read objectClass (moid)
			if (readVendorIDFrom.equalsIgnoreCase("file")) {

				// read vendor id from file
				objectClass = parseFileName(sourceFile.getName(), objectMask);

			} else if (readVendorIDFrom.equalsIgnoreCase("data")) {

				// read vendor id from file
				objectClass = parseFileName(fdn, objectMask);

			} else if (!readVendorIDFrom.equalsIgnoreCase("file") && !readVendorIDFrom.equalsIgnoreCase("data")
					&& readVendorIDFrom != null) {

				// read vendor id from the tag
				objectClass = readVendorIDFrom;
				log.log(Level.INFO, "Reading vendor id from the given tag:" + objectClass);

			} else {

				// error
				log.log(Level.WARN, "readVendorIDFrom property" + readVendorIDFrom + " is not defined");
				throw new SAXException("readVendorIDFrom property" + readVendorIDFrom + " is not defined");
			}
		}
		if (qName.equals("attr")) {

			for (int i = 0; i < atts.getLength(); i++) {
				if (atts.getLocalName(i).equalsIgnoreCase("name")) {
					key = atts.getValue(i);
				}

			}

		}
		if (qName.equals("seq")) {

			seqKey = key;

			for (int i = 0; i < atts.getLength(); i++) {
				if (atts.getLocalName(i).equalsIgnoreCase("count")) {
					seqCount = Integer.parseInt(atts.getValue(i));
				}
			}

			seqContainer = new ArrayList();

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void endElement(final String uri, final String name, final String qName) throws SAXException {

		if (qName.equals("item")) {
			if (itemValuesMap.get(key) != null) {
				String itemValues = itemValuesMap.get(key);
				itemValues = itemValues.concat(",").concat(charValue);
				itemValuesMap.put(key, itemValues);
			} else {
				itemValuesMap.put(key, charValue);
			}

			if (seqContainer.size() < seqCount) {
				seqContainer.add(charValue);
			}

		} else if (qName.equals("seq")) {

		} else if (qName.equals("attr")) {
			log.log(Level.INFO, "Key:" + key + " Items Value: " + itemValuesMap.get(key)
					+ " Value from measData.get(key) : " + measData.get(key));

			if (objectClass.equalsIgnoreCase(key)) {

				if (itemValuesMap.get(key) != null) {
					objectClass = parseFileName(itemValuesMap.get(key), objectMask);
				} else {
					objectClass = parseFileName(charValue, objectMask);
				}

				objectClass = objectClass.trim();
			}

			if (itemValuesMap.get(key) != null) {
				measData.put(key, itemValuesMap.get(key));
			} else {

				log.log(Level.INFO, "Key: " + key + " Value: " + charValue);
				measData.put(key, charValue);

			}

			// Strange case of HSS
			String item = (String) measData.get("managedElementType");
			if (item != null) {
				ne_typeExist = true;
				ne_type = item;
				if (item.trim().equalsIgnoreCase("HSS-FE".trim()) && !flag) {
					nodeVersion = (String) measData.get("nodeVersion");
				}
			}
			if (nodeVersion != null) {
				flag = true;
			}
			item = (String) measData.get("Hss_feFunctionId");
			if (item != null && !flag1) {
				measData.put("nodeVersion", nodeVersion);
				log.log(Level.INFO, "Key: nodeVersion" + " Value: " + nodeVersion);
				flag1 = true;
			}

		} else if (qName.equals("mo")) {

			if (objectClass != null) {

				try {

					if (!objectClass.equals(oldObjectClass)) {

						log.log(Level.INFO, "New objectClass found: " + objectClass);

						oldObjectClass = objectClass;

						measFile = getChannel(objectClass);

					} else {
						log.log(Level.INFO, "Old objectClass, no need to create new measFile " + oldObjectClass);
					}

					// no sequenses just add once
					if (seqContainer == null || seqContainer.size() == 0) {
						Map<String,String> fileData=new HashMap<>();
						fileData.put("Filename", sourceFile.getName());
						fileData.put("DC_SUSPECTFLAG", EMPTY_SUSPECT_FLAG);
						fileData.put("DIRNAME", sourceFile.getDir());
						fileData.put("objectClass", objectClass);
						fileData.put("fdn", fdn);
						fileData.put("JVM_TIMEZONE", JVM_TIMEZONE);
						measData.putAll(fileData);
						//measFile.pushData(fileData);
						measFile.pushData(measData);
						// measFile.saveData();

					} else {

						if (!multiLine) {

							// there is sequnce but we want only one datarow.
							final StringBuffer tmp = new StringBuffer();
							for (int i = 0; i < seqContainer.size(); i++) {
								if (i > 0) {
									tmp.append(",");
								}
								tmp.append((String) seqContainer.get(i));
							}

							measFile.pushData(measData);
							measFile.pushData(seqKey, tmp.toString());
							measFile.pushData("Filename", sourceFile.getName());
							measFile.pushData("DC_SUSPECTFLAG", EMPTY_SUSPECT_FLAG);
							measFile.pushData("DIRNAME", sourceFile.getDir());
							measFile.pushData("objectClass", objectClass);
							measFile.pushData("fdn", fdn);
							measFile.pushData("JVM_TIMEZONE", JVM_TIMEZONE);
							// measFile.saveData();

						} else {

							// there is sequence and we want multiple datarows
							// -> clone data.
							for (int i = 0; i < seqContainer.size(); i++) {

								measFile.pushData(measData);
								measFile.pushData(seqKey, (String) seqContainer.get(i));
								measFile.pushData("Filename", sourceFile.getName());
								measFile.pushData("DC_SUSPECTFLAG", EMPTY_SUSPECT_FLAG);
								measFile.pushData("DIRNAME", sourceFile.getDir());
								measFile.pushData("objectClass", objectClass);
								measFile.pushData("fdn", fdn);
								measFile.pushData("JVM_TIMEZONE", JVM_TIMEZONE);
								// measFile.saveData();
							}
						}

						seqContainer.clear();
					}
					if (ne_typeExist) {
						log.log(Level.INFO, "inside mo " + fdn + "   " + ne_type);
						nodefdn.put(fdn, ne_type);
						ne_typeExist = false;
					}

					//measData.clear();

				} catch (Exception e) {
					log.log(Level.WARN, "Error in writing measurement file", e);
				}

			}

		}
	}

	@Override
	public void characters(final char ch[], final int start, final int length) {
		final StringBuffer charBuffer = new StringBuffer(length);
		for (int i = start; i < start + length; i++) {
			// If no control char
			if (ch[i] != '\\' && ch[i] != '\n' && ch[i] != '\r' && ch[i] != '\t') {
				charBuffer.append(ch[i]);
			}
		}
		charValue += charBuffer;
	}

	/**
	 * Extracts a substring from given string based on given regExp
	 * 
	 */
	public String parseFileName(final String str, final String regExp) {

		final Pattern pattern = Pattern.compile(regExp);
		final Matcher matcher = pattern.matcher(str);

		if (matcher.matches()) {
			final String result = matcher.group(1);
			log.log(Level.INFO, " regExp (" + regExp + ") found from " + str + "  :" + result);
			return result;
		} else {
			log.log(Level.WARN, "String " + str + " doesn't match defined regExp " + regExp);
		}

		return "";
	}

	/**
	 * Lookups variables from filename
	 * 
	 * @param name
	 *            of the input file
	 * @param pattern
	 *            the pattern to lookup from the filename
	 * @return result returns the group(1) of result, or null
	 */
	private String transformFileVariables(String filename, String pattern) {
		String result = null;
		try {
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(filename);
			if (m.matches()) {
				result = m.group(1);
			}
		} catch (PatternSyntaxException e) {
			log.log(Level.WARN, "Error performing transformFileVariables for CT Parser.", e);
		}
		return result;
	}

	/**
	 * Lookups variables from filename
	 * 
	 * @param name
	 *            of the input file
	 * @return result returns OSSId, or null
	 */
	private String getOSSIdFromInDir(String filename) {
		String result = null;
		try {
			if (filename.contains("/")) {
				result = filename.split("/")[1];
			} else {
				log.log(Level.WARN, "InDir of the source file is not proper");
			}
		} catch (Exception e) {
			log.log(Level.WARN, "Error performing getOSSIdFromInDir ", e);
		}
		return result;
	}

	private Channel getChannel(String tagId) {
		try {
			String folderName = MeasurementFileFactory.getFolderName(sourceFile, interfaceName, tagId, log);
			Channel channel;
			if (folderName != null) {
				if ((channel = channelMap.get(folderName)) != null) {
					return channel;
				}
				channel = MeasurementFileFactory.createChannel(sourceFile, tagId, folderName, log, sink);
				channelMap.put(folderName, channel);
				return channel;
			}
		} catch (Exception e) {
			log.log(Level.WARN, "Exception while getting channel : ", e);
		}
		return null;
	}
}