package com.boot.jx.mongo.logger;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import com.boot.jx.mongo.CommonDocInterfaces.IDocument;
import com.boot.jx.mongo.CommonDocInterfaces.TimeStampIndex.TimeStampDoc;

@Document(collection = "DUMMY_PING")
@TypeAlias("DummyPing")
public class DummyPingDoc extends TimeStampDoc implements IDocument {

	@Id
	private String dummyId;

	private String text;

	public String getDummyId() {
		return dummyId;
	}

	public void setDummyId(String dummyId) {
		this.dummyId = dummyId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
