package com.boot.jx.db.multitenant.conf;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * The Class MultiTenantJaxProperties.
 */
@Configuration
@ConfigurationProperties(prefix = "multitenancy")
public class MultiTenantProperties {

	/** The data sources props. */
	private List<DataSourceProperties> dataSourcesProps;
	// Getters and Setters

	/**
	 * Gets the data sources props.
	 *
	 * @return the data sources props
	 */
	public List<DataSourceProperties> getDataSourcesProps() {
		return dataSourcesProps;
	}

	/**
	 * Sets the data sources props.
	 *
	 * @param dataSourcesProps the new data sources props
	 */
	public void setDataSourcesProps(List<DataSourceProperties> dataSourcesProps) {
		this.dataSourcesProps = dataSourcesProps;
	}

	/**
	 * The Class DataSourceProperties.
	 */
	public static class DataSourceProperties extends org.springframework.boot.autoconfigure.jdbc.DataSourceProperties {

		/** The tenant id. */
		private String tenantId;

		/** The initial size. */
		private Integer initialSize;

		/** The max active. */
		private Integer maxActive;

		/** The max idle. */
		private Integer maxIdle;

		/** The min idle. */
		private Integer minIdle;

		// Getters and Setters

		/**
		 * Gets the tenant id.
		 *
		 * @return the tenant id
		 */
		public String getTenantId() {
			return tenantId;
		}

		/**
		 * Sets the tenant id.
		 *
		 * @param tenantId the new tenant id
		 */
		public void setTenantId(String tenantId) {
			this.tenantId = tenantId;
		}

		/**
		 * Gets the initial size.
		 *
		 * @return the initial size
		 */
		public Integer getInitialSize() {
			return initialSize;
		}

		/**
		 * Sets the initial size.
		 *
		 * @param initialSize the new initial size
		 */
		public void setInitialSize(Integer initialSize) {
			this.initialSize = initialSize;
		}

		/**
		 * Gets the max active.
		 *
		 * @return the max active
		 */
		public Integer getMaxActive() {
			return maxActive;
		}

		/**
		 * Sets the max active.
		 *
		 * @param maxActive the new max active
		 */
		public void setMaxActive(Integer maxActive) {
			this.maxActive = maxActive;
		}

		/**
		 * Gets the max idle.
		 *
		 * @return the max idle
		 */
		public Integer getMaxIdle() {
			return maxIdle;
		}

		/**
		 * Sets the max idle.
		 *
		 * @param maxIdle the new max idle
		 */
		public void setMaxIdle(Integer maxIdle) {
			this.maxIdle = maxIdle;
		}

		/**
		 * Gets the min idle.
		 *
		 * @return the min idle
		 */
		public Integer getMinIdle() {
			return minIdle;
		}

		/**
		 * Sets the min idle.
		 *
		 * @param minIdle the new min idle
		 */
		public void setMinIdle(Integer minIdle) {
			this.minIdle = minIdle;
		}

	}
}
