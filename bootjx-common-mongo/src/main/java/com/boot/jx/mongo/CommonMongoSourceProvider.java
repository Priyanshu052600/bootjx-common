package com.boot.jx.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.boot.jx.AppContextUtil;
import com.boot.jx.scope.tnt.TenantScoped;
import com.boot.jx.scope.tnt.TenantValue;
import com.boot.jx.scope.tnt.Tenants.TenantResolver;
import com.boot.utils.ArgUtil;

@Component
@TenantScoped
public class CommonMongoSourceProvider {

	// WRITER
	private static Object WRITER = new Object();
	private CommonMongoSource writer;

	@TenantValue("${spring.data.mongodb.uri}")
	private String dataSourceUrl;

	@Value("${spring.data.mongodb.uri}")
	private String globalDataSourceUrl;

	@Value("${spring.data.mongodb.prefix}")
	private String globalDBProfix;

	// READER
	private static Object READER = new Object();
	private CommonMongoSource reader;
	@TenantValue("${spring.ro.data.mongodb.uri}")
	private String dataSourceUrlReadOnly;

	@Value("${spring.ro.data.mongodb.uri}")
	private String globalDataSourceUrlReadOnly;

	@Value("${spring.ro.data.mongodb.prefix}")
	private String globalDBProfixReadOnly;

	@Autowired(required = false)
	private TenantResolver tenantResolver;

	public CommonMongoSource getSource(String dataSourceUrl, String globalDBProfix, String globalDataSourceUrl) {
		String tnt = AppContextUtil.getTenant();
		String dbtnt = ArgUtil.is(tenantResolver) ? tenantResolver.getDBName(tnt) : tnt;
		CommonMongoSource commonMongoSource = new CommonMongoSource();
		commonMongoSource.setTenant(tnt);
		commonMongoSource.setTenantDB(dbtnt);
		commonMongoSource.setDataSourceUrl(dataSourceUrl);
		commonMongoSource.setGlobalDataSourceUrl(globalDataSourceUrl);
		commonMongoSource.setGlobalDBProfix(globalDBProfix);
		return commonMongoSource;
	}

	public CommonMongoSource getSource() {
		if (CommonMongoSource.isReadOnly()) {
			if (this.reader == null) {
				synchronized (READER) {
					this.reader = getSource(dataSourceUrlReadOnly, globalDBProfixReadOnly, globalDataSourceUrlReadOnly);
				}
			}
			return reader;
		}
		if (this.writer == null) {
			synchronized (WRITER) {
				this.writer = getSource(dataSourceUrl, globalDBProfix, globalDataSourceUrl);
			}
		}
		return writer;
	}

	public void setDataSourceUrl(String dataSourceUrl) {
		this.dataSourceUrl = dataSourceUrl;
	}

	public void setGlobalDataSourceUrl(String globalDataSourceUrl) {
		this.globalDataSourceUrl = globalDataSourceUrl;
	}

	public void setGlobalDBProfix(String globalDBProfix) {
		this.globalDBProfix = globalDBProfix;
	}

}
