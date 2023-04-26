package com.boot.jx.table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;

import com.boot.jx.logger.LoggerService;
import com.boot.utils.ArgUtil;
import com.github.gianlucanitti.javaexpreval.Expression;
import com.github.gianlucanitti.javaexpreval.ExpressionContext;
import com.github.gianlucanitti.javaexpreval.ExpressionException;

public class PivotBucket {

	public static final Map<String, PivotBucketColFunction> MAP = new HashMap<String, PivotBucketColFunction>();
	private static final Logger LOGGER = LoggerService.getLogger(PivotBucket.class);

	public static interface PivotBucketColFunction {
		default Object body(PivotBucket col, String rowId, String exp) {
			return exp;
		}
	}

	public List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
	public Map<String, Object> result = new HashMap<String, Object>();
	public Map<String, PivotBucket> pivotcols;

	public PivotBucket() {
		this.pivotcols = new HashMap<String, PivotBucket>();
	}

	public PivotBucket getCol(String colId) {
		if (!this.pivotcols.containsKey(colId)) {
			this.pivotcols.put(colId, new PivotBucket());
		}
		return this.pivotcols.get(colId);
	}

	public Object any(String rowId, Object defaultValue) {
		for (Map<String, Object> map : rows) {
			Object x = map.getOrDefault(rowId, defaultValue);
			if (ArgUtil.is(x)) {
				return x;
			}
		}
		return defaultValue;
	}

	public Object sum(String rowId) {
		BigDecimal sum = new BigDecimal(0);
		for (Map<String, Object> map : rows) {
			BigDecimal x = ArgUtil.parseAsBigDecimal(map.get(rowId), BigDecimal.ZERO);
			sum = sum.add(x);
		}
		return sum;
	}

	public Object avg(String rowId) {
		BigDecimal sum = new BigDecimal(0);
		int count = 0;
		for (Map<String, Object> map : rows) {
			BigDecimal x = ArgUtil.parseAsBigDecimal(map.get(rowId), BigDecimal.ZERO);
			sum = sum.add(x);
			count++;
		}
		if (count > 0) {
			return sum.divide(new BigDecimal(count));
		} else {
			return 0;
		}
	}

	public Object count() {
		return rows.size();
	}

	public Object ucount(String rowId) {
		Map<String, Object> uniqueMap = new HashMap<String, Object>();
		for (Map<String, Object> map : rows) {
			String x = ArgUtil.parseAsString(map.get(rowId));
			if (ArgUtil.is(x)) {
				uniqueMap.put(x, x);
			}
		}
		return uniqueMap.keySet().size();
	}

	public void add(Map<String, Object> e) {
		rows.add(e);
	}

	public static void register(String fun, PivotBucketColFunction funBody) {
		MAP.put(fun, funBody);
	}

	public void exp(List<String> computedCols, List<String> computedVals) {
		int computedColsCount = computedCols.size();
		if (computedColsCount == 0) {
			return;
		}

		ExpressionContext c = new ExpressionContext();
		try {
			for (Entry<String, Object> entry : this.result.entrySet()) {
				c.setVariable(entry.getKey(), ArgUtil.parseAsDouble(entry.getValue(), Double.valueOf(0)));
			}

			for (int i = 0; i < computedColsCount; i++) {
				String funExp = computedVals.get(i);
				String funkey = computedCols.get(i);

				Expression expr = Expression.parse(funExp);
				// expr.
				double result = expr.eval(c);
				this.result.put(funkey, result);
				c.setVariable(funkey, result);
			}
		} catch (ExpressionException e) {
			LOGGER.error("ExpressionContext Fauilure", e);
		}
	}

}