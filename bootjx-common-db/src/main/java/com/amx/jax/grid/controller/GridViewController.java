package com.amx.jax.grid.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.grid.GridMeta;
import com.amx.jax.grid.GridQuery;
import com.amx.jax.grid.GridService;
import com.boot.jx.api.ApiResponse;
import com.boot.jx.logger.LoggerService;

@RestController
public class GridViewController {

	Logger LOGGER = LoggerService.getLogger(getClass());

	@Autowired
	GridService gridService;

	@RequestMapping(value = "/grid/view/{gridView}", method = { RequestMethod.POST })
	public ApiResponse<?, GridMeta> gridView(@PathVariable(value = "gridView") String gridView,
			@RequestBody GridQuery gridQuery) {
		return gridService.view(gridView, gridQuery).get();
	}

	@RequestMapping(value = "/grid/api/{gridViewName}/meta", method = { RequestMethod.POST })
	public ApiResponse<?, GridMeta> gridViewMeta(@PathVariable(value = "gridViewName") String gridViewName,
			@RequestBody GridQuery gridQuery) {
		return gridService.view(gridViewName, gridQuery).meta();
	}

	@RequestMapping(value = "/grid/api/{gridViewName}/data", method = { RequestMethod.POST })
	public ApiResponse<?, GridMeta> gridViewData(@PathVariable(value = "gridViewName") String gridViewName,
			@RequestBody GridQuery gridQuery) {
		return gridService.view(gridViewName, gridQuery).get();
	}

}
