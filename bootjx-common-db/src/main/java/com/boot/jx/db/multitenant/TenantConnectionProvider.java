package com.boot.jx.db.multitenant;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TenantConnectionProvider implements MultiTenantConnectionProvider {

	private static final long serialVersionUID = 1L;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private DataSource dataSource;

	@Autowired
	TenantDBConfig tenantDBConfig;

	@Override
	public Connection getAnyConnection() throws SQLException {
		return dataSource.getConnection();
	}

	@Override
	public void releaseAnyConnection(Connection connection) throws SQLException {
		connection.close();
	}

	@Override
	public Connection getConnection(String tenantIdentifie) throws SQLException {
		DataSource ds = tenantDBConfig.getDataSource();
		Connection connection;

		if (ds != null) {
			LOGGER.debug("Gettign Connection from Tenant DataSource");
			connection = ds.getConnection();
		} else {
			LOGGER.debug("Gettign Connection from Default DataSource");
			connection = getAnyConnection();
		}
		LOGGER.debug("Recvd Connection");
		return connection;
	}

	public DataSource getDataSource() {
		DataSource ds = tenantDBConfig.getDataSource();
		return ds;
	}

	@Override
	public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
		LOGGER.debug("Releasing Connection");
		connection.close();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean isUnwrappableAs(Class unwrapType) {
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> unwrapType) {
		return null;
	}

	@Override
	public boolean supportsAggressiveRelease() {
		return false;
	}
}
