package com.ericsson.eniq.ebskafkaconsumer.cache;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.avro.Schema;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.ericsson.eniq.parser.cache.DItem;
import com.ericsson.eniq.parser.cache.DataFormatCache;

@Component
public class DataFormatCacheImpl {

	private static Map<String, String> dataFormatIdToTagIdMap = new HashMap<>();
	private static Map<String, List<DItem>> dataFormatIdToDItemsMap = new HashMap<>();
	private static Map<String, Schema> schemaRegistry = new ConcurrentHashMap<>();
	private static Set<String> tagIds = new HashSet<>();

	private static final Logger LOG = LogManager.getLogger(DataFormatCacheImpl.class);
	
	@Value("${schema.registry.url}") 
	private String schemaRegistryUrl;
	
	@Value("${producer.topic}") 
	private String producerTopic;
	

	public void readDB(String dbUrl, String username, String password, String driver, String versionid) {
		Connection con = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(dbUrl, username, password);

			DataFormatCache cache = DataFormatCache.getCache();

			loadDataFormats(con, cache, versionid);
			loadDataItems(con, cache, versionid);
			LOG.info("Data formatcache initialized successfully ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public void loadDataFormats(Connection con, DataFormatCache cache, String verisonid) throws Exception {
		String sql = "select di.interfacename, im.tagid, im.dataformatid, df.foldername, im.transformerid"
				+ " from datainterface di, interfacemeasurement im, dataformat df"
				+ " where di.interfacename = im.interfacename and im.dataformatid = df.dataformatid"
				+ " and di.status = 1 and im.status = 1 and df.versionid in (select versionid from "
				+ "dwhrep.tpactivation where status = 'ACTIVE') "
				+ "and im.dataformatid like '%:mdc' ORDER BY im.dataformatid";
		int rowcount = 0;
		try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery();) {

			while (rs.next()) {
				cache.addDataFormat(rs.getString(3), rs.getString(2), rs.getString(1), rs.getString(4),
						rs.getString(5));
				dataFormatIdToTagIdMap.put(rs.getString(3), rs.getString(2));
				tagIds.add(rs.getString(2));
				rowcount++;
			}

		}
		LOG.info("total rows read = " + rowcount);

	}

	public void loadDataItems(Connection con, DataFormatCache cache, String verisonid) throws Exception {
		String sql = " SELECT di.dataname, di.colnumber, di.dataid, di.process_instruction, di.dataformatid, di.datatype, di.datasize, di.datascale,"
				+ " COALESCE("
				+ " (SELECT 1 FROM MeasurementCounter mc WHERE di.dataname = mc.dataname AND df.typeid = mc.typeid),"
				+ " (SELECT 1 FROM ReferenceColumn rc WHERE di.dataname = rc.dataname AND df.typeid = rc.typeid AND uniquekey = 0),"
				+ " 0) AS is_counter FROM dwhrep.dataformat df JOIN "
				+ "dwhrep.dataitem di ON df.dataformatid = di.dataformatid WHERE df.versionid in (select versionid from "
				+ "dwhrep.tpactivation where status = 'ACTIVE') " + "and di.dataformatid like '%:mdc'";
		// and techpack_name = '"+ verisonid +"'
		try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery();) {
			String dataFormatId;
			String dataName;
			int colNumber;
			String dataID;
			String pi;
			String dataType;
			int dataSize;
			int dataScale;
			int isCounter;
			while (rs.next()) {
				dataFormatId = rs.getString(5);
				dataName = rs.getString(1);
				colNumber = rs.getInt(2);
				dataID = rs.getString(3);
				pi = rs.getString(4);
				dataType = rs.getString(6);
				dataSize = rs.getInt(7);
				dataScale = rs.getInt(8);
				isCounter = rs.getInt(9);
				cache.addDataItem(dataFormatId, dataName, colNumber, dataID, pi, dataType, dataSize, dataScale,
						isCounter);
				List<DItem> dItems = dataFormatIdToDItemsMap.get(dataFormatId);
				if (dItems == null) {
					dItems = new ArrayList<>();
					dataFormatIdToDItemsMap.put(dataFormatId, dItems);
				}
				dItems.add(new DItem(dataName, colNumber, dataID, pi, dataType, dataSize, dataScale, isCounter));
			}

		}
		LOG.info("Sorting data Items ");
		cache.sortDataItems();
		LOG.info("data Items sorted");
		getSchemas();
		// initSchemaRegistry();
	}

	/*private void initSchemaRegistry() {
		String tagId;
		List<DItem> dItems;
		for (Map.Entry<String, List<DItem>> entry : dataFormatIdToDItemsMap.entrySet()) {
			dItems = entry.getValue();
			Collections.sort(dItems);
			tagId = dataFormatIdToTagIdMap.get(entry.getKey());
			if (tagId != null) {
				schemaRegistry.put(tagId, SchemaGenerator.getAvroSchema(tagId, dItems.iterator()));
			} else {
				LOG.info("initSchemaRegistry: Folder name is null : " + entry.getKey());
			}

		}

	}*/

	private  void getSchemas() {
		for (String tagId : tagIds) {
			String subjectName = producerTopic + "-" + tagId;
			WebClient webClient = WebClient.builder().baseUrl(schemaRegistryUrl)
					.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
			Subject subject = webClient.method(HttpMethod.GET).uri("/subjects/" + subjectName + "/versions/latest")
					.exchange().block().bodyToMono(Subject.class).block();
			String schemaString = subject.getSchema();
			Schema.Parser parser = new Schema.Parser();
			if (schemaString != null) {
				Schema schema = parser.parse(schemaString);
				schemaRegistry.put(tagId, schema);
				LOG.log(Level.INFO, "Schema for tagId : "+tagId+" is :"+schema);
			} else {
				LOG.info("Schema is not present for tagId : " + tagId);
			}

			LOG.info("Schema registry initialized , size : "+ schemaRegistry.size());
		}

	}

	public static Schema getSchema(String folderName) {
		return schemaRegistry.get(folderName);
	}
	
	/*public static void main(String[] args) {
		WebClient webClient = WebClient.builder().baseUrl("http://10.45.193.129:32015")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
		Subject subject = webClient.method(HttpMethod.GET).uri("/subjects/PM_E_ERBS_DATA-" + "CapacityConnectedUsers_V" + "/versions/latest")
				.exchange().block().bodyToMono(Subject.class).block();
		//Gson gson = new GsonBuilder().serializeNulls().create();
		//Subject sub = gson.fromJson(subject, Subject.class);
		String schemaString = subject.getSchema();
		Schema.Parser parser = new Schema.Parser();
		Schema avrSchema = parser.parse(schemaString);
		System.out.println("avrschema : "+avrSchema);
		//AvrSchema schema = subject.getSchema();
	}*/

}
