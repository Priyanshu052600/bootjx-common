package com.boot.jx.logger;

import java.math.BigDecimal;

import com.boot.utils.ArgUtil;
import com.boot.utils.EnumType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditActor {

	public static enum ActorType implements EnumType {

		G, C, I, W, E, S,

		// Not To be used
		GUEST(G),
		CUSTOMER(C),
		IDENTITY(I),
		WHATSAPP(W),
		EMP(E),
		SYSTEM(S);

		ActorType shortName;

		ActorType(ActorType shortName) {
			this.shortName = shortName;
		}

		ActorType() {
			this.shortName = this;
		}

		@Override
		public String toString() {
			return this.shortName.name();
		}

		@Override
		public String stringValue() {
			return this.shortName.toString();
		}

		public ActorType enunValue() {
			return this.shortName;
		}

		public String getId(Object id) {
			return this.shortName + ":" + ArgUtil.parseAsString(id);
		}
	}

	@JsonProperty("type")
	ActorType actorType;

	@JsonProperty("id")
	String actorId;

	public AuditActor(ActorType actorType, Object actorId) {
		this.actorType = actorType.enunValue();
		this.actorId = ArgUtil.parseAsString(actorId);
	}

	public ActorType getActorType() {
		return actorType == null ? null : actorType.enunValue();
	}

	public void setActorType(ActorType actorType) {
		this.actorType = actorType;
	}

	public String getActorId() {
		return actorId;
	}

	@JsonIgnore
	public BigDecimal getActorIdAsBigDecimal() {
		return ArgUtil.parseAsBigDecimal(actorId);
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public String toString() {
		return String.format("%s:%s", this.getActorType(), this.actorId);
	}
}
