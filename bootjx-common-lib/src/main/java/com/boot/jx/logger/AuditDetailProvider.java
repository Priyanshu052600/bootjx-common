package com.boot.jx.logger;

import com.boot.jx.model.AuditCreateEntity;
import com.boot.jx.model.AuditCreateEntity.AuditUpdateEntity;
import com.boot.jx.rest.AppRequestInterfaces.AppAuthUser;
import com.boot.utils.ArgUtil;

public interface AuditDetailProvider {

	public AppAuthUser getAuthUser();

	public String getAuditUser();

	public default <T extends AuditCreateEntity> T auditCreate(T entity) {
		entity.setCreatedBy(getAuditUser());
		entity.setCreatedStamp(System.currentTimeMillis());
		return entity;
	}

	public default <T extends AuditUpdateEntity> T auditUpdate(T entity) {
		entity.setUpdatedBy(getAuditUser());
		entity.setUpdatedStamp(System.currentTimeMillis());
		return entity;
	}

	public default <T> T audit(T entity) {
		if (entity instanceof AuditUpdateEntity) {
			AuditUpdateEntity auditUpdateEntity = (AuditUpdateEntity) entity;
			if (ArgUtil.isEmpty(auditUpdateEntity.getUpdatedBy())) {
				this.auditUpdate(auditUpdateEntity);
			}
		}
		if (entity instanceof AuditCreateEntity) {
			AuditCreateEntity auditCreateEntity = (AuditCreateEntity) entity;
			if (ArgUtil.isEmpty(auditCreateEntity.getCreatedBy())) {
				this.auditCreate(auditCreateEntity);
			}
		}
		return entity;
	}

}
