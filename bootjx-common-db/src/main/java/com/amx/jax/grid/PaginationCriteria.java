package com.amx.jax.grid;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.amx.jax.grid.FilterBy.Condition;
import com.amx.jax.grid.GridConstants.FilterDataType;
import com.boot.utils.ArgUtil;
import com.boot.utils.DateFormatUtil;

/**
 * The Class PaginationCriteria.
 */
public class PaginationCriteria {

	/** The page number. */
	private Integer pageNumber;

	/** The page size. */
	private Integer pageSize;

	/** The total records. */
	private Integer totalRecords;

	/** The sort by. */
	private SortBy sortBy;

	/** The filter by. */
	private FilterBy filterBy;

	/**
	 * Gets the page number.
	 *
	 * @return the pageNumber
	 */
	public Integer getPageNumber() {
		return (null == pageNumber) ? 0 : pageNumber;
	}

	/**
	 * Sets the page number.
	 *
	 * @param pageNumber the pageNumber to set
	 */
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * Gets the page size.
	 *
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		return (null == pageSize) ? 10 : pageSize;
	}

	/**
	 * Sets the page size.
	 *
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * Gets the total records.
	 *
	 * @return the totalRecords
	 */
	public Integer getTotalRecords() {
		return totalRecords;
	}

	/**
	 * Sets the total records.
	 *
	 * @param totalRecords the totalRecords to set
	 */
	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

	/**
	 * Gets the sort by.
	 *
	 * @return the sortBy
	 */
	public SortBy getSortBy() {
		return sortBy;
	}

	/**
	 * Sets the sort by.
	 *
	 * @param sortBy the sortBy to set
	 */
	public void setSortBy(SortBy sortBy) {
		this.sortBy = sortBy;
	}

	/**
	 * Gets the filter by.
	 *
	 * @return the filterBy
	 */
	public FilterBy getFilterBy() {
		return filterBy;
	}

	/**
	 * Sets the filter by.
	 *
	 * @param filterBy the filterBy to set
	 */
	public void setFilterBy(FilterBy filterBy) {
		this.filterBy = filterBy;
	}

	/**
	 * Checks if is filter by empty.
	 *
	 * @return true, if is filter by empty
	 */
	public boolean isFilterByEmpty() {
		if (null == filterBy || null == filterBy.getMapOfLikeFilters() || filterBy.getMapOfLikeFilters().size() == 0) {
			return true;
		}
		return false;
	}

	public boolean isWhereFilterByEmpty() {
		if (null == filterBy || null == filterBy.getMapOfWhereFilters()
				|| filterBy.getMapOfWhereFilters().size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if is sort by empty.
	 *
	 * @return true, if is sort by empty
	 */
	public boolean isSortByEmpty() {
		if (null == sortBy || null == sortBy.getSortBys() || sortBy.getSortBys().size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the filter by clause.
	 *
	 * @return the filter by clause
	 */
	public String getFilterByClause() {

		StringBuilder fbsb = null;

		if (!isFilterByEmpty()) {
			Iterator<Entry<String, String>> fbit = filterBy.getMapOfLikeFilters().entrySet().iterator();

			while (fbit.hasNext()) {

				Map.Entry<String, String> pair = fbit.next();

				if (null == fbsb) {
					fbsb = new StringBuilder();
					fbsb.append(BRKT_OPN);

					fbsb.append(SPACE).append(BRKT_OPN).append(pair.getKey()).append(LIKE_PREFIX)
							.append(pair.getValue()).append(LIKE_SUFFIX).append(BRKT_CLS);

				} else {

					fbsb.append(filterBy.isGlobalSearch() ? OR : AND).append(BRKT_OPN).append(pair.getKey())
							.append(LIKE_PREFIX).append(pair.getValue()).append(LIKE_SUFFIX).append(BRKT_CLS);

				}
			}
			fbsb.append(BRKT_CLS);
		}

		return (null == fbsb) ? BLANK : fbsb.toString();
	}

	public String getWhereFilterByClause() {

		StringBuilder fbsb = null;

		if (!isWhereFilterByEmpty()) {
			Iterator<Entry<String, Condition>> fbit = filterBy.getMapOfWhereFilters().entrySet().iterator();

			while (fbit.hasNext()) {
				Map.Entry<String, Condition> pair = fbit.next();
				if (null == fbsb) {
					fbsb = new StringBuilder();
					fbsb.append(BRKT_OPN);
					fbsb.append(SPACE);
				} else {
					fbsb.append(AND);
				}
				fbsb.append(BRKT_OPN).append(pair.getValue().toColumn());
				fbsb.append(SPACE + pair.getValue().getOpertor().getSign());

				// Value Function
				if (ArgUtil.is(pair.getValue().getValueFunc())) {
					fbsb.append(SPACE + pair.getValue().getValueFunc() + BRKT_OPN);
				}

				if (FilterDataType.TIMESTAMP.equals(pair.getValue().getType())
						|| ((FilterDataType.DATE.equals(pair.getValue().getType())
								|| FilterDataType.TIME.equals(pair.getValue().getType()))
								&& DateFormatUtil.isTimeStamp(pair.getValue().getValue()))) {
					fbsb.append(
							" TO_TIMESTAMP_TZ( '1970-01-01 00:00:00.0+00:00', 'YYYY-MM-DD HH24:MI:SS.FFTZH:TZM' ) + NUMTODSINTERVAL("
									+ pair.getValue().getValue() + "/1000, 'SECOND')");
				} else if (FilterDataType.TIME.equals(pair.getValue().getType())) {
					String format = DateFormatUtil.determineSqlDateFormat(pair.getValue().getValue());
					fbsb.append(" TO_DATE( '" + pair.getValue().getValue() + "', '" + format + "' )");
				} else if (FilterDataType.DATE.equals(pair.getValue().getType())) {
					String format = DateFormatUtil.determineSqlDateFormat(pair.getValue().getValue());
					fbsb.append(" TO_DATE( '" + pair.getValue().getValue() + "', '" + format + "' )");
				} else if (FilterDataType.NUMBER.equals(pair.getValue().getType())) {
					fbsb.append(pair.getValue().getValue());
				}else if(pair.getValue().getOpertor().equals(GridConstants.FilterOperater.IN)){
					fbsb.append(SPACE + pair.getValue().getValue());
				}else {
					fbsb.append(WHERE_PREFIX + pair.getValue().getValue() + WHERE_SUFFIX);
				}

				// Value Function
				if (ArgUtil.is(pair.getValue().getValueFunc())) {
					fbsb.append(BRKT_CLS + SPACE);
				}

				fbsb.append(BRKT_CLS);
			}
			fbsb.append(BRKT_CLS);
		}

		return (null == fbsb) ? BLANK : fbsb.toString();
	}

	/**
	 * Gets the order by clause.
	 *
	 * @return the order by clause
	 */
	public String getOrderByClause() {

		StringBuilder sbsb = null;

		if (!isSortByEmpty()) {
			Iterator<Entry<String, SortOrder>> sbit = sortBy.getSortBys().entrySet().iterator();

			while (sbit.hasNext()) {
				Map.Entry<String, SortOrder> pair = sbit.next();
				if (!ArgUtil.isEmpty(pair.getKey())) {
					if (null == sbsb) {
						sbsb = new StringBuilder();
						sbsb.append(ORDER_BY).append(pair.getKey()).append(SPACE).append(pair.getValue());
					} else {
						sbsb.append(COMMA).append(pair.getKey()).append(SPACE).append(pair.getValue());
					}
				}
			}
		}

		return (null == sbsb) ? BLANK : sbsb.toString();
	}

	/** The Constant BLANK. */
	private static final String BLANK = "";

	/** The Constant SPACE. */
	private static final String SPACE = " ";

	/** The Constant LIKE_PREFIX. */
	private static final String LIKE_PREFIX = " LIKE '%";

	/** The Constant LIKE_SUFFIX. */
	private static final String LIKE_SUFFIX = "%' ";

	private static final String WHERE_PREFIX = " '";

	private static final String WHERE_SUFFIX = "' ";

	/** The Constant AND. */
	private static final String AND = " AND ";

	/** The Constant OR. */
	private static final String OR = " OR ";

	/** The Constant ORDER_BY. */
	private static final String ORDER_BY = " ORDER BY ";

	private static final String BRKT_OPN = " ( ";

	private static final String BRKT_CLS = " ) ";

	/** The Constant COMMA. */
	private static final String COMMA = " , ";

	/** The Constant PAGE_NO. */
	public static final String PAGE_NO = "start";

	/** The Constant PAGE_SIZE. */
	public static final String PAGE_SIZE = "length";

	/** The Constant DRAW. */
	public static final String DRAW = "draw";

}
