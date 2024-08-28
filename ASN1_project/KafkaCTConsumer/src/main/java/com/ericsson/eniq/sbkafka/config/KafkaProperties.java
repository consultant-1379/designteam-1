package com.ericsson.eniq.sbkafka.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("spring.kafka")
public class KafkaProperties {
	private String bootstrapServers;
	private Consumer consumer;
	
	
	public String getBootstrapServers() {
		return bootstrapServers;
	}


	public void setBootstrapServers(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}

	
	@Override
	public String toString() {
		return "KafkaProperties [bootstrapServers=" + bootstrapServers + ", consumer=" + consumer + "]";
	}


	public Consumer getConsumer() {
		return consumer;
	}


	public void setConsumer(Consumer consumer) {
		this.consumer = consumer;
	}


	public static class Consumer
	{
		private String groupId;
		private int maxPollRecords;
		private Boolean enableAutoCommit;
		private String autoOffsetReset;
		private String ackMode;
		public String getGroupId() {
			return groupId;
		}
		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}
		public int getMaxPollRecords() {
			return maxPollRecords;
		}
		public void setMaxPollRecords(int maxPollRecords) {
			this.maxPollRecords = maxPollRecords;
		}
		public Boolean getEnableAutoCommit() {
			return enableAutoCommit;
		}
		public void setEnableAutoCommit(Boolean enableAutoCommit) {
			this.enableAutoCommit = enableAutoCommit;
		}
		public String getAutoOffsetReset() {
			return autoOffsetReset;
		}
		public void setAutoOffsetReset(String autoOffsetReset) {
			this.autoOffsetReset = autoOffsetReset;
		}
		public String getAckMode() {
			return ackMode;
		}
		public void setAckMode(String ackMode) {
			this.ackMode = ackMode;
		}
		@Override
		public String toString() {
			return "Consumer [groupId=" + groupId + ", maxPollRecords=" + maxPollRecords + ", enableAutoCommit="
					+ enableAutoCommit + ", autoOffsetReset=" + autoOffsetReset + ", ackMode=" + ackMode + "]";
		}
		
	}
}
