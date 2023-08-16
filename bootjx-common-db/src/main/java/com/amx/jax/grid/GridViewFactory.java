package com.amx.jax.grid;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boot.jx.dict.GridView;
import com.boot.jx.grid.IGridViewMeta;

import jakarta.annotation.PostConstruct;

@Component
public class GridViewFactory {

	@Autowired
	IGridViewMeta<?> gridMeta;

	public static Map<String, GridInfo<?>> map = new HashMap<String, GridInfo<?>>();

	@PostConstruct
	public void init() {
		GridView[] views = gridMeta.getAllViews();
		for (GridView viewName : views) {
			map.put(viewName.name(), new GridInfo<Map<String, Object>>(viewName.name()).build());
		}

	}

	public static GridInfo<?> get(String gridView) {
		return map.get(gridView);
	}

	public static GridInfo<?> get(GridView gridView) {
		return map.get(gridView.name());
	}

}
