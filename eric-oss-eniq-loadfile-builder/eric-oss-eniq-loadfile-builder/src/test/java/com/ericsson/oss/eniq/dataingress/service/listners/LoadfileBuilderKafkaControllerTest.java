///**
// * 
// */
//package com.ericsson.oss.eniq.dataingress.service.listners;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//import org.apache.avro.generic.GenericRecord;
//import org.apache.kafka.clients.consumer.Consumer;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.platform.runner.JUnitPlatform;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.test.EmbeddedKafkaBroker;
//import org.springframework.kafka.test.condition.EmbeddedKafkaCondition;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.kafka.test.utils.KafkaTestUtils;
//
///**
// * @author zmairvn
// *
// */
//@EmbeddedKafka(topics = { LoadfileBuilderKafkaControllerTest.EXPECTED_TOPIC })
//@RunWith(JUnitPlatform.class)
//public class LoadfileBuilderKafkaControllerTest {
//
//	private static KafkaTemplate<String, String> template;
//
//	private static Consumer<String, String> consumer;
//
//	
//
//	private static final String TOPIC = "PM_E_ERBS_FILES";
//
//	public static final String EXPECTED_TOPIC = "PM_E_ERBS_FILES";
//	@Value("${num.of.writers}")
//	private int numOfWriters;
//
//	@InjectMocks
//	private RoutingLoadfileService routingLoadfileService;
//
//	@Autowired
//	ConsumerRecords<String, GenericRecord> consumerRecords = null;
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@Test
//	@BeforeAll
//	public void setUp() throws Exception {
//		MockitoAnnotations.initMocks(this);
//
//		EmbeddedKafkaBroker embeddedKafka = EmbeddedKafkaCondition.getBroker();
//		Map<String, Object> senderProps = KafkaTestUtils.producerProps(embeddedKafka);
//		DefaultKafkaProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<>(senderProps);
//		AtomicBoolean ppCalled = new AtomicBoolean();
//		pf.addPostProcessor(prod -> {
//			ppCalled.set(true);
//			return prod;
//		});
//		template = new KafkaTemplate<>(pf, true);
//		Map<String, Object> consumerProps = KafkaTestUtils
//				.consumerProps("KafkaTemplatetests" + UUID.randomUUID().toString(), "false", embeddedKafka);
//		DefaultKafkaConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
//		consumer = cf.createConsumer();
//		embeddedKafka.consumeFromAnEmbeddedTopic(consumer, TOPIC);
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@Test
//	@AfterAll
//	public void tearDown() throws Exception {
//		consumer.close();
//	}
//
//	/**
//	 * Test method for
//	 * {@link com.ericsson.oss.eniq.dataingress.service.listners.LoadfileBuilderKafkaController#listen(org.apache.kafka.clients.consumer.ConsumerRecords, org.springframework.kafka.support.Acknowledgment)}
//	 * .
//	 */
//	@Test
//	public void testListen() {
//		assertNotNull(numOfWriters);
//
//		RoutingLoadfileService dataServiceMock = mock(RoutingLoadfileService.class);
//
//		boolean rtnValue = dataServiceMock.routingLogic(TOPIC, consumerRecords);
//		when(rtnValue).thenReturn(true);
//		assertEquals(KafkaTestUtils.getSingleRecord(consumer, TOPIC).value(), EXPECTED_TOPIC);
//		assertEquals("true", "true");
//	}
//
//}
