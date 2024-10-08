package com.ericsson.eniq.loader.common;

import java.util.Map;
import java.util.Vector;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.distocraft.dc5000.etl.rock.Meta_collections;
import com.distocraft.dc5000.etl.rock.Meta_databases;
import com.distocraft.dc5000.etl.rock.Meta_databasesFactory;
import com.distocraft.dc5000.etl.rock.Meta_transfer_actions;
import com.distocraft.dc5000.etl.rock.Meta_versions;
import com.distocraft.dc5000.repository.cache.ActivationCache;
import com.distocraft.dc5000.repository.cache.DataFormatCache;
import com.distocraft.dc5000.repository.cache.PhysicalTableCache;
import com.ericsson.eniq.loader.DatabaseDetails;
import com.ericsson.eniq.loader.sql.Loader;
import com.ericsson.eniq.loader.sql.PartitionedLoader;

import ssc.rockfactory.RockFactory;

@Component
public class DbConnection {

	private static final Logger logger = LogManager.getLogger(DbConnection.class);

	public DbConnection() {

	}

	/**
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getDatabaseConnectionDetails() throws Exception {
		// DatabaseCache dbInfo=new DatabaseCache();

		// logger.info(dbInfo.toString());

		DatabaseDetails dbInfo = new DatabaseDetails();
		final Map<String, String> dbConnDetails = dbInfo.getDbDetails();
		return dbConnDetails;
	}

	public RockFactory createEtlrepRockFactory(final Map<String, String> databaseConnectionDetails) throws Exception {

		final String databaseUsername = databaseConnectionDetails.get("etlrepUser");
		final String databasePassword = databaseConnectionDetails.get("etlrepPass");
		final String databaseUrl = databaseConnectionDetails.get("repdbURL");
		final String databaseDriver = databaseConnectionDetails.get("driver");

		try {
			return new RockFactory(databaseUrl, databaseUsername, databasePassword, databaseDriver, "ETLREP", true);
		} catch (final Exception e) {
			e.printStackTrace();
			throw new Exception("Unable to initialize database connection.", e);
		}
	}

	public RockFactory createDwhrepRockFactory(RockFactory etlrepRockFactory) throws Exception {
		try {
			// logger.info("Inside createDWHrepRockFactory");
			final Meta_databases whereMetaDatabases = new Meta_databases(etlrepRockFactory);
			whereMetaDatabases.setConnection_name("dwhrep");
			whereMetaDatabases.setType_name("USER");
			final Meta_databasesFactory metaDatabasesFactory = new Meta_databasesFactory(etlrepRockFactory,
					whereMetaDatabases);
			final Vector<Meta_databases> metaDatabases = metaDatabasesFactory.get();

			if ((metaDatabases != null) && (metaDatabases.size() == 1)) {
				final Meta_databases targetMetaDatabase = metaDatabases.get(0);
				return new RockFactory("jdbc:sybase:Tds:ieatrcxb6510:2641", targetMetaDatabase.getUsername(),
						targetMetaDatabase.getPassword(), etlrepRockFactory.getDriverName(), "DWHREP", true);
			} else {
				throw new Exception(
						"Unable to connect metadata (No dwhrep or multiple dwhreps defined in Meta_databases)");
			}
		} catch (final Exception e) {
			e.printStackTrace();
			throw new Exception("Creating database connection to dwhrep failed.", e);
		}
	}

	public RockFactory createDwhdbRockFactory(RockFactory etlrepRockFactory) throws Exception {
		try {
			// logger.info("Inside createDWHrepRockFactory");
			final Meta_databases whereMetaDatabases = new Meta_databases(etlrepRockFactory);
			whereMetaDatabases.setConnection_name("dwh");
			whereMetaDatabases.setType_name("USER");
			final Meta_databasesFactory metaDatabasesFactory = new Meta_databasesFactory(etlrepRockFactory,
					whereMetaDatabases);
			final Vector<Meta_databases> metaDatabases = metaDatabasesFactory.get();

			if ((metaDatabases != null) && (metaDatabases.size() == 1)) {
				final Meta_databases targetMetaDatabase = metaDatabases.get(0);
				return new RockFactory("jdbc:sybase:Tds:ieatrcxb6510:2640", targetMetaDatabase.getUsername(),
						targetMetaDatabase.getPassword(), etlrepRockFactory.getDriverName(), "dwhdb", true);
			} else {
				throw new Exception(
						"Unable to connect metadata (No dwhrep or multiple dwhreps defined in Meta_databases)");
			}
		} catch (final Exception e) {
			e.printStackTrace();
			throw new Exception("Creating database connection to dwhrep failed.", e);
		}
	}

	public RockFactory createDBADwhdbRockFactory(RockFactory etlrepRockFactory) throws Exception {
		try {
			// logger.info("Inside createDWHrepRockFactory");
			final Meta_databases whereMetaDatabases = new Meta_databases(etlrepRockFactory);
			whereMetaDatabases.setConnection_name("dwh");
			whereMetaDatabases.setType_name("DBA");
			final Meta_databasesFactory metaDatabasesFactory = new Meta_databasesFactory(etlrepRockFactory,
					whereMetaDatabases);
			final Vector<Meta_databases> metaDatabases = metaDatabasesFactory.get();

			if ((metaDatabases != null) && (metaDatabases.size() == 1)) {
				final Meta_databases targetMetaDatabase = metaDatabases.get(0);
				return new RockFactory(targetMetaDatabase.getConnection_string(), targetMetaDatabase.getUsername(),
						targetMetaDatabase.getPassword(), etlrepRockFactory.getDriverName(), "TPInstall", true);
			} else {
				throw new Exception(
						"Unable to connect metadata (No dwhrep or multiple dwhreps defined in Meta_databases)");
			}
		} catch (final Exception e) {
			e.printStackTrace();
			throw new Exception("Creating database connection to dwhrep failed.", e);
		}
	}
	
		@PostConstruct
		public PartitionedLoader runner() throws Exception {
			
			logger.info("Checking connection to database...............");

			DbConnection getdb = new DbConnection();

			final Map<String, String> databaseConnectionDetails = getdb.getDatabaseConnectionDetails();

			RockFactory etlrepRockFactory = getdb.createEtlrepRockFactory(databaseConnectionDetails);

			RockFactory dwhrepRockFactory = getdb.createDwhrepRockFactory(etlrepRockFactory);

			RockFactory dwhdbRockFactory = getdb.createDwhdbRockFactory(etlrepRockFactory);
			
			logger.info("Connections to database created.", dwhdbRockFactory);
		
			DataFormatCache.initialize(etlrepRockFactory);
			Meta_versions version = new Meta_versions(etlrepRockFactory);
			Meta_collections set = new Meta_collections(etlrepRockFactory, 7293L, "((14))", 62L);
			Meta_transfer_actions trActions = new Meta_transfer_actions(etlrepRockFactory, "((14))", 30651L, 7293L, 62L);
			SetContext context = new SetContext();
			StaticProperties.reload();
			SessionHandler.init();
			java.util.logging.Logger log =java.util.logging.Logger.getLogger(DbConnection.class.getName());
			PartitionedLoader loader = new PartitionedLoader(version, trActions.getCollection_set_id(), set, trActions.getTransfer_action_id(),
	                  1L, 3L, etlrepRockFactory, trActions, context, log);
			ActivationCache.initialize(etlrepRockFactory);
			PhysicalTableCache.initialize(etlrepRockFactory);
			loader.execute();
			return loader;
		}

}
