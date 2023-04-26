package com.boot.jx.model;

public interface AuditCreateEntity {

	String getCreatedBy();

	void setCreatedBy(String createdBy);

	Long getCreatedStamp();

	void setCreatedStamp(Long createdStamp);

	public interface AuditUpdateEntity {
		public Long getUpdatedStamp();

		void setUpdatedStamp(Long createdStamp);

		public String getUpdatedBy();

		void setUpdatedBy(String createdBy);
	}

	public interface AuditIdentifier {
		public String auditIdentifier();
	}

	public interface AuditableEntity extends AuditCreateEntity, AuditUpdateEntity {
	}

}
