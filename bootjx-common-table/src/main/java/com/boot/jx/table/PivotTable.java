package com.boot.jx.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.regex.Pattern;

import com.boot.utils.ArgUtil;
import com.boot.utils.CollectionUtil;
import com.boot.utils.Constants;
import com.boot.utils.StringUtils.StringMatcher;;

public class PivotTable {

	public static final Pattern FUN_AS_ALIAS_DEFAULT = Pattern
			.compile("^(sum|any|count|ucount) (.+) (AS|as|As|aS) (.+) DEFAULT (.+)$");
	public static final Pattern FUN_AS_ALIAS = Pattern.compile("^(sum|any|count|ucount) (.+) (AS|as|As|aS) (.+)$");
	public static final Pattern ROW_AS_ALIAS_DEFAULT = Pattern.compile("^(.+) (AS|as|As|aS) (.+) DEFAULT (.+)$");
	public static final Pattern ROW_AS_ALIAS = Pattern.compile("^(.+) (AS|as|As|aS) (.+)$");
	public static final Pattern FUN_COLS = Pattern.compile("^(sum|any|count|ucount) (.+)$");
	public static final Pattern COMPUTED = Pattern.compile("^(.+)=(.+)$");
	public static final Pattern NONCOMPUTED = Pattern.compile("^(.+)=(.+)$");
	public static final Pattern COL_DEFAULT = Pattern.compile("^(.+) (.+)$");
	public static final Pattern COL_GROUPING = Pattern.compile("^(.+)\\((.+)\\)$");

	List<String> rows = CollectionUtil.getList("*");
	List<String> rows_alias = CollectionUtil.getList("*");
	List<String> rows_default = CollectionUtil.getList("");
	List<String> cols = CollectionUtil.getList("*");
	List<String> vals = CollectionUtil.getList("*");
	List<String> vals_default = CollectionUtil.getList("");
	List<String> aggs = CollectionUtil.getList("*");
	List<String> alias = CollectionUtil.getList("*");
	List<String> defaultCols = CollectionUtil.getList();
	List<String> defaultColsValues = CollectionUtil.getList("");
	List<String> computedCols = CollectionUtil.getList();
	List<String> computedVals = CollectionUtil.getList();
	List<String> noncomputedCols = CollectionUtil.getList();
	List<String> noncomputedVals = CollectionUtil.getList();
	Map<String, Object> colGroup = null;

	public Map<String, PivotBucket> pivotrows;

	public PivotTable() {
		this.pivotrows = new HashMap<String, PivotBucket>();
	}

	public PivotTable(Map<String, List<String>> pivot) {
		this(pivot.get("rows"), pivot.get("cols"),
				pivot.get("vals"), pivot.get("aggs"), pivot.get("alias"),
				pivot.get("defaults"),
				pivot.get("computed"), pivot.get("noncomputed"),
				pivot.get("colgroups"));
	}

	public PivotTable(List<String> rowsTemp, List<String> colsTemp, List<String> valsTemp, List<String> aggsTemp,
			List<String> aliasTemp, List<String> defaultColsTemp,
			List<String> computedColsTemp, List<String> noncomputedValsTemp,
			List<String> colgroupList) {
		this();
		this.rows = rowsTemp;
		this.cols = colsTemp;
		this.vals = valsTemp;
		this.aggs = aggsTemp != null ? aggsTemp : this.aggs;
		this.alias = aliasTemp != null ? aliasTemp : this.alias;
		this.defaultCols = defaultColsTemp != null ? defaultColsTemp : this.defaultCols;
		this.computedCols = computedColsTemp != null ? computedColsTemp : this.computedVals;
		this.noncomputedVals = noncomputedValsTemp != null ? noncomputedValsTemp : this.noncomputedCols;
		colgroupList = colgroupList != null ? colgroupList : CollectionUtil.getList();

		int rowCount = this.rows.size();
		int valCount = this.vals.size();
		int colCount = this.cols.size();
		int computedCount = this.computedCols.size();
		int noncomputedCount = this.noncomputedCols.size();
		int defaultColsCount = this.defaultCols.size();

		for (int r = 0; r < rowCount; r++) {
			StringMatcher funkey = new StringMatcher(this.rows.get(r));
			if (funkey.isMatch(ROW_AS_ALIAS_DEFAULT)) {
				this.rows.set(r, funkey.group(1));
				CollectionUtil.set(this.rows_alias, r, funkey.group(3));
				CollectionUtil.set(this.rows_default, r, funkey.group(4));
			} else if (funkey.isMatch(ROW_AS_ALIAS)) {
				this.rows.set(r, funkey.group(1));
				CollectionUtil.set(this.rows_alias, r, funkey.group(3));
				CollectionUtil.set(this.rows_default, r, null);
			} else {
				CollectionUtil.set(this.rows_alias, r, funkey.toString());
				CollectionUtil.set(this.rows_default, r, null);
			}
		}

		for (int v = 0; v < valCount; v++) {
			StringMatcher funkey = new StringMatcher(this.vals.get(v));
			CollectionUtil.set(this.vals_default, v, Constants.BLANK);
			if (funkey.isMatch(FUN_AS_ALIAS_DEFAULT)) {
				CollectionUtil.set(this.aggs, v, funkey.group(1));
				this.vals.set(v, funkey.group(2));
				CollectionUtil.set(this.alias, v, funkey.group(4));
				CollectionUtil.set(this.vals_default, v, funkey.group(5));
			} else if (funkey.isMatch(FUN_AS_ALIAS)) {
				CollectionUtil.set(this.aggs, v, funkey.group(1));
				this.vals.set(v, funkey.group(2));
				CollectionUtil.set(this.alias, v, funkey.group(4));
			} else if (funkey.isMatch(ROW_AS_ALIAS)) {
				CollectionUtil.set(this.aggs, v, "count");
				this.vals.set(v, funkey.group(1));
				CollectionUtil.set(this.alias, v, funkey.group(3));
			} else if (funkey.isMatch(FUN_COLS)) {
				CollectionUtil.set(this.aggs, v, funkey.group(1));
				this.vals.set(v, funkey.group(2));
				CollectionUtil.set(this.alias, v, funkey.group(2));
			} else {
				CollectionUtil.set(this.aggs, v, "count");
				CollectionUtil.set(this.alias, v, funkey.toString());
			}
		}

		for (int cp = 0; cp < computedCount; cp++) {
			StringMatcher funkey = new StringMatcher(computedCols.get(cp));
			if (funkey.isMatch(COMPUTED)) {
				CollectionUtil.set(this.computedCols, cp, funkey.group(1));
				CollectionUtil.set(this.computedVals, cp, funkey.group(2));
			}
		}

		for (int ncp = 0; ncp < noncomputedCount; ncp++) {
			StringMatcher funkey = new StringMatcher(noncomputedCols.get(ncp));
			if (funkey.isMatch(NONCOMPUTED)) {
				CollectionUtil.set(this.noncomputedCols, ncp, funkey.group(1));
				CollectionUtil.set(this.noncomputedVals, ncp, funkey.group(2));
			}
		}

		for (int dc = 0; dc < defaultColsCount; dc++) {
			StringMatcher funkey = new StringMatcher(this.defaultCols.get(dc));
			if (funkey.isMatch(COL_DEFAULT)) {
				CollectionUtil.set(this.defaultCols, dc, funkey.group(1));
				CollectionUtil.set(this.defaultColsValues, dc, funkey.group(2));
			}
		}

		this.colGroup = new HashMap<String, Object>();

		for (String string : colgroupList) {
			StringMatcher funkey = new StringMatcher(string);
			if (funkey.isMatch(COL_GROUPING)) {
				colGroup.put(funkey.group(1), funkey.group(2).split(","));
			}
		}

	}

	public PivotBucket getRow(String rowId) {
		if (!this.pivotrows.containsKey(rowId)) {
			this.pivotrows.put(rowId, new PivotBucket());
		}
		return this.pivotrows.get(rowId);
	}

	public void add(Map<String, Object> item) {
		StringBuilder rowId = new StringBuilder();
		for (String rowKey : rows) {
			rowId.append(item.getOrDefault(rowKey, "#"));
		}

		StringBuilder colId = new StringBuilder();
		for (String colKey : cols) {
			colId.append(item.getOrDefault(colKey, "#"));
		}

		PivotBucket bucket = getRow(rowId.toString());
		bucket.getCol(colId.toString()).add(item);
	}

	public void calculate() {
		for (Entry<String, PivotBucket> rowEntrySet : pivotrows.entrySet()) {
			PivotBucket row = rowEntrySet.getValue();
			int rowCount = rows.size();
			int valCount = vals.size();
			int colCount = cols.size();
			int defaultColsCount = defaultCols.size();

			int noncomputedColsCount = noncomputedCols.size();

			if (defaultColsCount > 0) {
				for (int d = 0; d < defaultColsCount; d++) {
					row.result.put(defaultCols.get(d), defaultColsValues.get(d));
				}
			}

			if (colCount > 0) {
				for (Entry<String, PivotBucket> colEntrySet : row.pivotcols.entrySet()) {
					PivotBucket col = colEntrySet.getValue();

					for (int r = 0; r < rowCount; r++) {
						String funkey = rows.get(r);
						String funkeyDefault = rows_default.get(r);
						row.result.put(rows_alias.get(r), col.any(funkey, funkeyDefault));
					}

					StringJoiner rowKeyDot = new StringJoiner(".");
					StringJoiner rowKey_ = new StringJoiner("_");
					for (int c = 0; c < colCount; c++) {
						String thisCol = (String) col.any(cols.get(c), Constants.BLANK);
						// row.result.put(cols.get(c), thisCol);
						// colGroup.put(thisCol,thisCol);
						rowKey_.add(thisCol);
						rowKeyDot.add(thisCol);
					}
					String rowKey_String = rowKey_.toString();
					String rowKeyDotString = rowKey_.toString();

					for (int i = 0; i < valCount; i++) {
						String fun = aggs.get(i);
						String funkey = vals.get(i);
						String funkeyDefault = ArgUtil.parseAsString(vals_default.get(i), Constants.BLANK);
						String funkeyAlias = alias.get(i);
						String colRowKey = rowKey_String + "_" + funkeyAlias;
						colGroup.put(rowKeyDotString + "$" + funkeyAlias, colRowKey);
						this.calculate(row, colRowKey, fun, funkey, col, funkeyDefault);
					}
				}
			} else {
				for (int r = 0; r < rowCount; r++) {
					String funkey = rows.get(r);
					row.result.put(funkey, row.any(funkey, Constants.BLANK));
				}
				for (int i = 0; i < valCount; i++) {
					String fun = aggs.get(i);
					String funkey = vals.get(i);
					this.calculate(row, funkey, fun, funkey, row, Constants.BLANK);
				}
			}

			row.exp(computedCols, computedVals);

			for (int i = 0; i < noncomputedColsCount; i++) {
				String fun = noncomputedVals.get(i);
				String funkey = noncomputedCols.get(i);
				row.result.put(funkey, fun);
			}
		}
	}

	public List<Map<String, Object>> toBulk() {
		List<Map<String, Object>> bulk = new ArrayList<Map<String, Object>>();
		for (Entry<String, PivotBucket> e : this.pivotrows.entrySet()) {
			bulk.add(e.getValue().result);
		}
		return bulk;
	}

	private void calculate(PivotBucket row, String rowKey, String fun, String funkey, PivotBucket col,
			Object emptyValue) {
		row.result.put(rowKey, emptyValue);
		switch (fun) {
		case "any":
			row.result.put(rowKey, col.any(funkey, emptyValue));
			break;
		case "sum":
			// System.out.println("SUM" +String.format("rowKey %s fun %s funkey %s
			// emptyValue %s",rowKey,fun,funkey,emptyValue));
			row.result.put(rowKey, col.sum(funkey));
			break;
		case "avg":
			row.result.put(rowKey, col.avg(funkey));
			break;
		case "ucount":
			// System.out.println("SUM" +String.format("rowKey %s fun %s funkey %s
			// emptyValue %s",rowKey,fun,funkey,emptyValue));
			row.result.put(rowKey, col.ucount(funkey));
			break;
		default:
			row.result.put(rowKey, col.count());
			break;
		}
	}

	public List<String> getCols() {
		return cols;
	}

	public Map<String, Object> getColGroup() {
		return colGroup;
	}

}