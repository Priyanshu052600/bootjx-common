package com.boot.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.regex.Pattern;

import com.boot.utils.ArgUtil;

public class DateTest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");
	public static final Pattern DATE_PRT = Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{2}:\\d{2}\\.\\d{3}$");

	/**
	 * This is just a test method
	 * 
	 * @param args
	 * @throws ParseException
	 */
	public static void main2(String[] args) throws ParseException {
		Date sysDate = new Date(0L);
		System.out.println("=====" + sysDate.toString());

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
		sdf.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
		String oldString = sdf.format(sysDate);
		System.out.println("=====" + oldString);
		sdf.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
		System.out.println("=====" + sdf.parse(oldString).toString());
	}

	public static void main(String[] args) throws ParseException {
		String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
		DateFormat df = new SimpleDateFormat(pattern);
		Date today = Calendar.getInstance().getTime();
		String todayAsString = df.format(today);
		System.out.println(today.toString() + "saveKioskLogs Created Date : " + todayAsString);
		Date today2 = ArgUtil.parseAsSimpleDate(todayAsString);
		System.out.println(today2.toString() + "saveKioskLogs Created Date : " + todayAsString);
		
		

	}

}
