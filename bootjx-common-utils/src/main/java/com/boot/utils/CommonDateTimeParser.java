package com.boot.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CommonDateTimeParser {

	DateTimeFormatter formatter;
	String dateAsString;
	ZoneId zone;

	public ZoneId getZone() {
		return this.zone;
	}

	public CommonDateTimeParser formatter(String formatter) {
		this.formatter = DateTimeFormatter.ofPattern(formatter).withLocale(java.util.Locale.US);
		return this;
	}

	public CommonDateTimeParser date(String dateAsString) {
		this.dateAsString = dateAsString;
		return this;
	}

	public CommonDateTimeParser zone(ZoneId zone) {
		this.zone = zone;
		return this;
	}

	// Output Convertors
	public LocalDateTime toLocalDate() {
		return LocalDateTime.parse(dateAsString, this.formatter);
	}

	public OffsetDateTime toOffsetDateTime() {
		return OffsetDateTime.parse(dateAsString, formatter);
	}

	public ZonedDateTime toZonedDateTime() {
		return ZonedDateTime.parse(dateAsString, formatter);
	}

	public Instant toInstant() {
		return toZonedDateTime().toInstant();
	}

	public long toUTCTimeStamp() {
		return toInstant().toEpochMilli();
	}

	// Adjustors
	public CommonDateTimeParser calculateZone() {
		this.zone = this.toZonedDateTime().getZone();
		return this;
	}

	public CommonDateTimeParser withZone() {
		this.formatter = this.formatter.withZone(zone);
		return this;
	}
}
