package com.bootloaderjs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.boot.utils.CommonDateTimeParser;
import com.boot.utils.TimeUtils;

public class DateUtilTest {

	public static void main(String args[]) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (z)");
		Date x = sdf.parse("Mon Jun 07 2021 03:32:59 GMT+0530 (India Standard Time)");

		// DateTimeFormatter d;
		// d.ofPattern(null)
		// System.out.println(x.toInstant().toEpochMilli());

		DateTimeFormatter f = DateTimeFormatter.ofPattern("eee MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)");

		// LocalDateTime date = LocalDateTime.parse("Mon Jun 07 2021 03:32:59 GMT+0530
		// (India Standard Time)", f);
//		LocalDateTime date2 = LocalDateTime.parse("Mon Jun 07 2021 03:32:59 GMT+0530 (India Standard Time)",
//				DateTimeFormatter.BASIC_ISO_DATE);

		// System.out.println(date.toString());
		System.out.println(
				ZonedDateTime.now().format(DateTimeFormatter.ofPattern("EE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)")));

		CommonDateTimeParser dtp = new CommonDateTimeParser().formatter("ccc MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)")
				.date("Mon Jun 07 2021 23:23:37 GMT+0530 (India Standard Time)").calculateZone()
				.formatter("M/d/yy, h:mm a").withZone();

		System.out.println(dtp.getZone());
		dtp.formatter("M/d/yy, h:m:s a").withZone();
		System.out.println(dtp.getZone());

		System.out.println(dtp.date("6/7/21, 3:32:59 AM").toUTCTimeStamp());

	}

	public static void main2(String args[]) {

		long yeterday = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1);
		long todays = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1);
		System.out.println("yeterday " + TimeUtils.isExpired(yeterday, "mid"));
		System.out.println("todays " + TimeUtils.isExpired(todays, "mid"));

	}
}
