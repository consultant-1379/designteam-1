package com.ericsson.eniq.flsmock.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("spring")
public class SpringProperties {

	private Kafka kafka;
	
	public Kafka getKafka() {
		return kafka;
	}
	
	@Override
	public String toString() {
		return "SpringProperties [kafka=" + kafka + "]";
	}

	public void setKafka(Kafka kafka) {
		this.kafka = kafka;
	}

	public static class Kafka {
		private String bootstrapServers;
		private String ctParserTopic;
		private String mdcParserTopic;
		private String ebsParserTopic;
		private String asciiParserTopic;
		private String asn1ParserTopic;
		private String csexportParserTopic;
		private String gppParserTopic;
		private String bulkcmhandlerParserTopic;

		public String getGppParserTopic() {
			return gppParserTopic;
		}
		public void setGppParserTopic(String gppParserTopic) {
			this.gppParserTopic = gppParserTopic;
		}

		public String getAsn1ParserTopic() {
			return asn1ParserTopic;
		}

		public void setAsn1ParserTopic(String asn1ParserTopic) {
			this.asn1ParserTopic = asn1ParserTopic;
		}

		public String getBootstrapServers() {
			return bootstrapServers;
		}

		public void setBootstrapServers(String bootstrapServers) {
			this.bootstrapServers = bootstrapServers;
		}

		public String getCtParserTopic() {
			return ctParserTopic;
		}

		public void setCtParserTopic(String ctParserTopic) {
			this.ctParserTopic = ctParserTopic;
		}

		public String getMdcParserTopic() {
			return mdcParserTopic;
		}

		public void setMdcParserTopic(String mdcParserTopic) {
			this.mdcParserTopic = mdcParserTopic;
		}
		
		public String getEbsParserTopic() {
			return ebsParserTopic;
		}

		public void setEbsParserTopic(String ebsParserTopic) {
			this.ebsParserTopic = ebsParserTopic;
		}

		public String getAsciiParserTopic() {
			return asciiParserTopic;
		}

		public void setAsciiParserTopic(String asciiParserTopic) {
			this.asciiParserTopic = asciiParserTopic;
		}

		public String getCsexportParserTopic() {
			return csexportParserTopic;
		}

		public void setCsexportParserTopic(String csexportParserTopic) {
			this.csexportParserTopic = csexportParserTopic;
		}
		
		public String getBulkcmhandlerParserTopic() {
			return bulkcmhandlerParserTopic;
		}
		public void setBulkcmhandlerParserTopic(String bulkcmhandlerParserTopic) {
			this.bulkcmhandlerParserTopic = bulkcmhandlerParserTopic;
		}
		@Override
		public String toString() {
			return "Kafka [bootstrapServers=" + bootstrapServers + ", ctParserTopic=" + ctParserTopic
					+ ", mdcParserTopic=" + mdcParserTopic + ", ebsParserTopic=" + ebsParserTopic
					+ ", asciiParserTopic=" + asciiParserTopic + ", asn1ParserTopic=" + asn1ParserTopic
					+ ", csexportParserTopic=" + csexportParserTopic + ", gppParserTopic=" + gppParserTopic
					+ ", bulkcmhandlerParserTopic=" + bulkcmhandlerParserTopic + "]";
		}
		
	}
}
