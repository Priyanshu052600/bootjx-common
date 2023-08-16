package com.amx.jax.grid;

import java.text.SimpleDateFormat;
import java.util.SimpleTimeZone;
import java.util.regex.Pattern;

public class GridConstants {

	public static Pattern OPERATOR_FILTER_DOUBLE = Pattern.compile("^(.*)(>=|\\*=|<=|!=|\\[\\])([a-zA-Z0-9_]*)$");
	public static Pattern OPERATOR_FILTER_SINGLE = Pattern.compile("^(.*)(>|=|<|~)([a-zA-Z0-9_]*)$");

	public static enum FilterOperater {
		GT(">"), GTE(">="), EQ("="), iEQ("="), STE("<="), ST("<"), LTE("<="), LT("<"), LK("like"), NEQ("!="),IN("IN");

		String sign;

		FilterOperater(String sign) {
			this.sign = sign;
		}

		public String getSign() {
			return sign;
		}

		public static FilterOperater fromSign(String signStr) {
			switch (signStr) {
				case ">":
					return GT;
				case ">=":
					return GTE;
				case "<":
					return ST;
				case "<=":
					return STE;
				case "*=":
					return LK;
				case "=":
					return EQ;
				case "~":
					return iEQ;
				case "!=":
					return NEQ;
				case "[]":
					return IN;	
				default:
					return EQ;
			}
		}

	}

	public static enum FilterDataType {
		STRING, NUMBER, DATE, TIME, TIMESTAMP
	}

	public static enum ColumnFunction {
		TRUNC("trunc"), LOWER("lower"), UPPER("upper");

		String colFunc;

		ColumnFunction(String colFunc) {
			this.colFunc = colFunc;
		}

		public String getColFunc() {
			return colFunc;
		}

	}

	public static final String GRID_DATE_FORMAT_SQL = "DD-MM-YYYY";
	public static final String GRID_DATE_FORMAT_JAVA = "dd-MM-yyyy";
	public static final String GRID_TIME_FORMAT_SQL = "DD-MM-YYYY HH24:MI:SS";
	public static final String GRID_TIME_FORMAT_JAVA = "dd-MM-yyyy HH:mm:ss";
	public static final SimpleDateFormat GRID_TIME_FORMATTER_JAVA = new SimpleDateFormat(GRID_TIME_FORMAT_JAVA);
	public static final SimpleDateFormat GRID_TIME_FORMATTER_JAVA_UTC = new SimpleDateFormat(GRID_TIME_FORMAT_JAVA);

	static {
		GRID_TIME_FORMATTER_JAVA_UTC.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
	}

	public static interface GridColumnOrder {
		public SortOrder getSortDir();

		public void setSortDir(SortOrder sortDir);

		public String getKey();

		public void setKey(String key);
	}
}
