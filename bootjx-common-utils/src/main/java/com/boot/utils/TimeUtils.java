package com.boot.utils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import com.boot.model.UtilityModels.Stringable;
import com.boot.utils.StringUtils.StringMatcher;

/**
 * The Class TimeUtils.
 */
public class TimeUtils {

	public static Map<String, TimeUnits> MAP = new HashMap<String, TimeUnits>();

	public static class Constants {
		public static long MILLIS_IN_MIN = 60 * 1000;
		public static long MILLIS_IN_HOUR = 3600 * 1000;
		public static long MILLIS_IN_DAY = 24 * MILLIS_IN_HOUR;
		public static long MILLIS_IN_WEEK = MILLIS_IN_DAY * 7;
	}

	public static enum TimeUnits {
		SECONDS(TimeUnit.SECONDS, "s", "sec", "second", "seconds"),
		MINUTES(TimeUnit.MINUTES, "mi", "min", "minute", "minutes"),
		HOUR(TimeUnit.HOURS, "h", "hr", "hrs", "hour", "hours"), DAYS(TimeUnit.DAYS, "d", "days", "day"),
		WEEK(TimeUnit.DAYS, 7, "w", "week", "wk", "weeks"), MONTH(TimeUnit.DAYS, 31, "mo", "mon", "month", "months"),
		YEAR(TimeUnit.DAYS, 365, "y", "yr", "year"), MIDNIGHT(TimeUnit.DAYS, "mid", "midnight") {
			public long toMillis(long days) {
				long todaysMillis = System.currentTimeMillis() - TimeUtils.getTodayStart().getTimeInMillis();
				return todaysMillis + TimeUnit.DAYS.toMillis(Math.max(days, 1) - this.getCount());
			}
		};

		TimeUnit timeUnit;
		int count;

		private TimeUnits(TimeUnit timeUnit, String... alias) {
			this(timeUnit, 1, alias);
		}

		private TimeUnits(TimeUnit timeUnit, int count, String... alias) {
			this.timeUnit = timeUnit;
			this.count = count;
			for (String string : alias) {
				MAP.put(string, this);
			}
			MAP.put(this.name().toLowerCase(), this);
		}

		public TimeUnit getTimeUnit() {
			return timeUnit;
		}

		public int getCount() {
			return count;
		}

		public long toMillis(long duration) {
			return (this.timeUnit.toMillis(duration) * this.count);
		}

		public long toSeconds(long duration) {
			return toMillis(duration) / 1000;
		}

		public long toHours(long duration) {
			return toMillis(duration) / 3600000;
		}

		public static Set<String> keys() {
			return MAP.keySet();
		}
	}

	public static class TimePeriod implements Stringable, Serializable {
		private static final long serialVersionUID = 1L;
		long millis;
		String stringValue;

		public long toMillis() {
			return millis;
		}

		public long toSeconds() {
			return millis / 1000;
		}

		public long toMinutes() {
			return millis / Constants.MILLIS_IN_MIN;
		}

		public long toHours() {
			return millis / Constants.MILLIS_IN_HOUR;
		}

		public long toDays() {
			return millis / Constants.MILLIS_IN_DAY;
		}

		public void setMillis(long millis) {
			this.millis = millis;
		}

		@Override
		public void fromString(String string) {
			this.stringValue = string;
			this.millis = TimeUtils.toMillis(string);
		}

		public String toString() {
			return stringValue;
		}

		public static TimePeriod from(String string) {
			TimePeriod tp = new TimePeriod();
			tp.fromString(string);
			return tp;
		}

		public static TimePeriod of(String string) {
			TimePeriod tp = new TimePeriod();
			tp.fromString(string);
			return tp;
		}
	}

	public static final Pattern PERIODS = Pattern.compile("^([0-9\\s]*)(" + String.join("|", TimeUnits.keys()) + ")$");

	public static long toMillis(String period) {
		period = period.toLowerCase();
		StringMatcher funkey = new StringMatcher(period);
		if (funkey.isMatch(PERIODS)) {
			String prd = funkey.group(2);
			long num = ArgUtil.parseAsLong(funkey.group(1).trim(), 0L);
			if (MAP.containsKey(prd)) {
				TimeUnits x = MAP.get(prd);
				return x.toMillis(num);
			} else if (period == "midnight") {

			}
		}
		return ArgUtil.parseAsLong(period, 0L);
	}

	public static long toHours(String period) {
		return toMillis(period) / 3600000;
	}

	@Deprecated
	public static long timeSince(String period) {
		return toMillis(period);
	}

	/**
	 * Time since.
	 *
	 * @param timethen the timethen
	 * @return the long
	 */
	public static long timeSince(long timethen) {
		return System.currentTimeMillis() - timethen;
	}

	/**
	 * Like expired, timeThen you pass has expired, based on maxAge provided. If
	 * maxAge is 0 then, true means its PAST-TimeStampe and false means its FUTRUE
	 * TimeStamp.
	 * 
	 * @param timeThen - timeStamp we want to check or creation time of timestamp,
	 *                 or birth-timestamp of entity
	 * @param maxAge   - maximum age of timestamp or entity, in milliseconds
	 * @return
	 */
	public static boolean isDead(long timeThen, long maxAge) {
		return (System.currentTimeMillis() - timeThen) > maxAge;
	}

	public static boolean isExpired(long timeThen, long maxAge) {
		return isDead(timeThen, maxAge);
	}

	public static boolean isExpired(long timeThen, String maxAge) {
		return isDead(timeThen, toMillis(maxAge));
	}

	public static boolean isExpired(Date dateThen, long maxAge) {
		return isDead(dateThen.getTime(), maxAge);
	}

	public static long getRotationNumber(long millis, int i) {
		return (System.currentTimeMillis() / (millis)) & i;
	}

	public static long getReverseRotationNumber(long millis, int i) {
		return i - (System.currentTimeMillis() / (millis)) & i;
	}

	/**
	 * 
	 * @param totalSlots  - number slots days needs to be devided into
	 * @param indexOfSlot - slot index starting from 0
	 * @return
	 */
	public static boolean inHourSlot(int totalSlots, int indexOfSlot) {
		Calendar calendar = Calendar.getInstance();
		int currentIndex = calendar.get(Calendar.HOUR_OF_DAY) / (24 / totalSlots);
		return currentIndex == indexOfSlot;
	}

	public static boolean inHours(int startHours, int endHours) {
		Calendar calendar = Calendar.getInstance();
		int currentIndex = calendar.get(Calendar.HOUR_OF_DAY);
		return (startHours <= currentIndex) && (currentIndex < endHours);
	}

	public static Calendar getTodayStart() {
		Calendar date = new GregorianCalendar();
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		return date;
	}

	public static long beforeTimeMillis(String period) {
		return System.currentTimeMillis() - toMillis(period);
	}
}
