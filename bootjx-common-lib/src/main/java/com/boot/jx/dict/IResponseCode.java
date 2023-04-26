package com.boot.jx.dict;

import com.boot.jx.dict.PayGCodes.CodeCategory;

public interface IResponseCode<T extends Enum<?>> {

	public CodeCategory getCodeCategoryByResponseCode(String responseCode);

	public T getResponseCodeEnumByCode(String responseCode);

}
