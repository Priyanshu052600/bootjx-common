package com.amx.jax.grid;

import com.boot.utils.ArgUtil;

/**
 * The Class AppUtil.
 *
 * @author pavan.solapure
 */
public class GridUtil {

	/**
	 * Builds the paginated query.
	 *
	 * @param baseQuery          the base query
	 * @param paginationCriteria the pagination criteria
	 * @return the string
	 */
	public static String buildPaginatedQuery(String baseQuery, PaginationCriteria paginationCriteria) {
		// String queryTemplate = "SELECT FILTERED_ORDERD_RESULTS.* FROM (SELECT
		// BASEINFO.* FROM ( %s ) BASEINFO %s %s ) FILTERED_ORDERD_RESULTS LIMIT %d,
		// %d";
		StringBuilder sb = new StringBuilder(
				"SELECT FILTERED_ORDERD_RESULTS.* FROM (SELECT BASEINFO.* FROM ( #BASE_QUERY# ) BASEINFO #WHERE_CLAUSE#  #ORDER_CLASUE# ) FILTERED_ORDERD_RESULTS LIMIT #PAGE_START#, #PAGE_SIZE#");
		String finalQuery = null;
		if (!ArgUtil.isEmpty(paginationCriteria)) {
			int pageStart = paginationCriteria.getPageNumber() * paginationCriteria.getPageSize();
			String whereClaus = paginationCriteria.getFilterByClause();
			finalQuery = sb.toString().replaceAll("#BASE_QUERY#", baseQuery)
					.replaceAll("#WHERE_CLAUSE#",
							((ArgUtil.isEmpty(whereClaus)) ? "" : " WHERE ")
									+ whereClaus)
					.replaceAll("#ORDER_CLASUE#", paginationCriteria.getOrderByClause())
					.replaceAll("#PAGE_START#", ArgUtil.parseAsString(pageStart))
					.replaceAll("#PAGE_SIZE#", paginationCriteria.getPageSize().toString());
		}
		return (null == finalQuery) ? baseQuery : finalQuery;
	}

	public static String buildPaginatedQueryForOracle(String baseQuery, PaginationCriteria paginationCriteria,
			boolean isGridViewRecord, boolean isCustomeQuery, GridInfo<?> gridInfo) {

		String finalQuery = null;

		StringBuilder sb = null;

		if (isCustomeQuery) {
			// Record Query with Custom // SLOWEST
			sb = new StringBuilder(
					"SELECT * FROM (SELECT FILTERED_ORDERED_RESULTS.*, COUNT(1) OVER() total_records, ROWNUM AS RN FROM (SELECT BASEINFO.* FROM ( #BASE_QUERY# ) BASEINFO ) FILTERED_ORDERED_RESULTS #WHERE_CLAUSE# #ORDER_CLASUE# ) WHERE RN > (#PAGE_NUMBER# * #PAGE_SIZE#) AND RN <= (#PAGE_NUMBER# + 1) * #PAGE_SIZE# ");
		} else if (!isCustomeQuery && !isGridViewRecord) {
			// Plain Query
			sb = new StringBuilder(
					"SELECT * FROM (SELECT FILTERED_ORDERED_RESULTS.* , 0 total_records, ROWNUM as RN FROM (#BASE_QUERY# #WHERE_CLAUSE# #GROUP_CLAUSE# #ORDER_CLASUE# ) FILTERED_ORDERED_RESULTS ) WHERE RN > (#PAGE_NUMBER# * #PAGE_SIZE#) AND RN <= (#PAGE_NUMBER# + 1) * #PAGE_SIZE#");
		} else if (!isCustomeQuery && isGridViewRecord) {
			// Record Query : MEDIUM
			sb = new StringBuilder(
					"SELECT * FROM (SELECT FILTERED_ORDERED_RESULTS.*, COUNT(1) OVER() total_records, ROWNUM as RN FROM (#BASE_QUERY# #WHERE_CLAUSE# #GROUP_CLAUSE# #ORDER_CLASUE# ) FILTERED_ORDERED_RESULTS) WHERE RN > (#PAGE_NUMBER# * #PAGE_SIZE#) AND RN <= (#PAGE_NUMBER# + 1) * #PAGE_SIZE# ");
		}

		if (!ArgUtil.isEmpty(paginationCriteria)) {

			String whereValueClause = paginationCriteria.getWhereFilterByClause();
			String whereSearchClause = paginationCriteria.getFilterByClause();

			String combinedClause = (ArgUtil.isEmpty(whereValueClause) ? " " : whereValueClause)
					+ ((!ArgUtil.isEmpty(whereValueClause) && !ArgUtil.isEmpty(whereSearchClause)) ? " AND " : "")
					+ (ArgUtil.isEmpty(whereSearchClause) ? " " : whereSearchClause);

			finalQuery = sb.toString().replaceAll("#BASE_QUERY#", baseQuery)
					.replaceAll("#WHERE_CLAUSE#",
							((ArgUtil.isEmpty(combinedClause)) ? "" : " WHERE ")
									+ combinedClause)
					.replaceAll("#NOWHERE#",
							((ArgUtil.isEmpty(combinedClause)) ? " WHERE " : " AND "))

					.replaceAll("#GROUP_CLAUSE#",
							((ArgUtil.isEmpty(gridInfo.getGroupBy())) ? "" : (" GROUP BY " + gridInfo.getGroupBy())))
					.replaceAll("#ORDER_CLASUE#", paginationCriteria.getOrderByClause())
					.replaceAll("#PAGE_NUMBER#", paginationCriteria.getPageNumber().toString())
					.replaceAll("#PAGE_SIZE#", paginationCriteria.getPageSize().toString());

		}
		return (null == finalQuery) ? baseQuery : finalQuery;
	}
}
