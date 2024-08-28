package com.ericsson.oss.eniq.dataingress.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.ericsson.oss.eniq.dataingress.service.cache.TableNameCache;

import io.micrometer.core.instrument.MeterRegistry;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CoreApplicationTest {
	@Autowired
	TableNameCache cache;

	@Autowired
	private ApplicationContext context;

	@Autowired
	CoreApplication coreApplication=new CoreApplication();

	@Test
	public void contextLoads() {
	}

	@Test
	public void testRun() throws Exception {
		cache.initCache();
		assertNotNull(context.getBean(TableNameCache.class));

	}

	@Test
	public void applicationContextTest() {
		CoreApplication.main(new String[] {});
	}
	
//	@Test
//	public void testMetricsCommonTags() {
//		MeterRegistryCustomizer<MeterRegistry> registry =coreApplication.metricsCommonTags();
//		assertNotNull(registry);
//		
//	}
}
