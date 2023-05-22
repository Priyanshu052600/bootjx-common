package com.boot.jx.scope.vendor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.boot.common.ScopedBeanFactory;
import com.boot.jx.AppConstants;
import com.boot.utils.ClazzUtil;
import com.boot.utils.ContextUtil;
import com.boot.utils.StringUtils;

public class VendorContext<T> extends ScopedBeanFactory<String, T> {

	private static final long serialVersionUID = 4007091611441725719L;

	public static void setVendor(String vendor) {
		ContextUtil.map().put("scopedTarget." + StringUtils.decapitalize(VendorContext.class.getSimpleName()), vendor);
	}

	public static String getVendor() {
		return (String) ContextUtil.map()
				.get("scopedTarget." + StringUtils.decapitalize(VendorContext.class.getSimpleName()));
	}

	public static final String DEFAULT_VENDOR_KEY = "*";

	@Target({ ElementType.TYPE, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	//@ApiMockParams({
		//	@ApiMockParam(name = AppConstants.AUTH_ID_XKEY, value = "Vendor Id",
			//		defaultValue = "vendor", paramType = MockParamType.HEADER),
			//@ApiMockParam(name = AppConstants.AUTH_TOKEN_XKEY, value = "Vendor Request Token",
				//	defaultValue = "vendor@1234", paramType = MockParamType.HEADER) })
	public @interface ApiVendorHeaders {

	}

	@Qualifier
	@Scope(value = AppConstants.Scopes.VENDOR, proxyMode = ScopedProxyMode.TARGET_CLASS)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface VendorScoped {
		String[] value() default DEFAULT_VENDOR_KEY;
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface VendorValue {
		String value();
	}

	public VendorContext(List<T> libs) {
		super(libs);
	}

	@Override
	public String[] getKeys(T lib) {
		VendorScoped annotation = ClazzUtil.getAnnotationFromBean(lib, VendorScoped.class);
		if (annotation != null) {
			return annotation.value();
		}
		return null;
	}

	@Override
	public String getKey() {
		return null;
	}

	@Override
	public String getDefaultKey() {
		return DEFAULT_VENDOR_KEY;
	}

}
