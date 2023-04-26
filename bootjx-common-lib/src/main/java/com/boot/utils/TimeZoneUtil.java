package com.boot.utils;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeZoneUtil {

	public static class TimeZoneDto {
		String id;
		String code;
		String title;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
	}

	static List<TimeZoneDto> timeZoneDtoLst = new ArrayList<>();
	static List<String> timeZoneLst = new ArrayList<>();

	static {
		String[] ids = TimeZone.getAvailableIDs();
		for (String id : ids) {
			TimeZoneDto dto = new TimeZoneDto();
			TimeZone tz = TimeZone.getTimeZone(id);
			long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
			long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset()) - TimeUnit.HOURS.toMinutes(hours);
			// avoid -4:-30 issue
			minutes = Math.abs(minutes);

			String key = null;
			String contry = null;
			String timezoffset = null;

			if (hours > 0) {
				contry = tz.getID();
				timezoffset = String.format("GMT+%d:%02d", hours, minutes);
				key = contry + "::" + timezoffset;
				dto.setId(key);
				dto.setCode(contry);
				dto.setTitle(key);
			} else {
				contry = tz.getID();
				timezoffset = String.format("GMT+%d:%02d", hours, minutes);
				key = contry + "::" + timezoffset;
				dto.setId(key);
				dto.setCode(contry);
				dto.setTitle(key);
			}
			timeZoneDtoLst.add(dto);
			timeZoneLst.add(key);
		}
	}

	public static List<TimeZoneDto> getTimeZoneDtoLst() {
		return timeZoneDtoLst;
	}

	public static void setTimeZoneDtoLst(List<TimeZoneDto> timeZoneDtoLst) {
		TimeZoneUtil.timeZoneDtoLst = timeZoneDtoLst;
	}

	public static List<String> getTimeZoneLst() {
		return timeZoneLst;
	}

	public static void setTimeZoneLst(List<String> timeZoneLst) {
		TimeZoneUtil.timeZoneLst = timeZoneLst;
	}

}
