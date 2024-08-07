package com.boot.utils;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import com.boot.jx.exception.ApiHttpExceptions.ApiHttpArgException;
import com.boot.jx.exception.ApiHttpExceptions.ApiStatusCodes;
import com.boot.utils.DateFormatUtil.MyDateConverter;

public class EntityDtoUtil {

	/**
	 * Creates DTO object from Entity object
	 * 
	 * @param <E>
	 * @param <D>
	 * @param srcEntity
	 * @param destDto
	 * @return
	 */
	public static <E, D> D entityToDto(E srcEntity, D destDto) {
		if (!ArgUtil.is(srcEntity) || !ArgUtil.is(destDto)) {
			return destDto;
		}
		try {
			BeanUtils.copyProperties(destDto, srcEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new ApiHttpArgException(ApiStatusCodes.ENTITY_CONVERSION_EXCEPTION);
		}
		return destDto;
	}

	/**
	 * Creates Entity object from DTO object
	 * 
	 * @param <D>
	 * @param <E>
	 * @param srcDto
	 * @param destEntity
	 * @return
	 */
	public static <D, E> E dtoToEntity(D srcDto, E destEntity) {
		if (!ArgUtil.is(srcDto) || !ArgUtil.is(destEntity)) {
			return destEntity;
		}
		try {
			BeanUtils.copyProperties(destEntity, srcDto);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new ApiHttpArgException(ApiStatusCodes.ENTITY_CONVERSION_EXCEPTION);
		}
		return destEntity;
	}

	/**
	 * This is replica of {@link BeanUtils#copyProperties(Object, Object)}
	 * 
	 * @param <D>
	 * @param <E>
	 * @param srcDto
	 * @param destEntity
	 * @return
	 */
	public static <D, S> D copyProperties(D toDestObject, S fromSrcObject) {
		if (!ArgUtil.is(toDestObject) || !ArgUtil.is(fromSrcObject)) {
			return toDestObject;
		}
		try {
			BeanUtils.copyProperties(toDestObject, fromSrcObject);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new ApiHttpArgException(ApiStatusCodes.ENTITY_CONVERSION_EXCEPTION);
		}
		return toDestObject;
	}

	static {
		ConvertUtils.register(new MyDateConverter(), java.util.Date.class);
	}
}
