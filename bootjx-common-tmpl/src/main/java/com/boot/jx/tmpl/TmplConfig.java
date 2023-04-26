package com.boot.jx.tmpl;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.boot.jx.dict.Language;
import com.boot.jx.model.CommonFile;
import com.boot.jx.postman.model.Message;
import com.boot.jx.scope.tnt.TenantScoped;
import com.boot.jx.scope.tnt.TenantValue;

/**
 * The Class PostManConfig.
 */
@TenantScoped
@Component
@PropertySource("classpath:application-tmpl.properties")
public class TmplConfig {

    @Value("${jax.static.url}")
    String jaxStaticUrl;

    @Value("${jax.static.context}")
    String jaxStaticContext;
    
    /** The tenant. */
    @TenantValue("${tenant}")
    private String tenant;

    /** The tenant lang. */
    @TenantValue("${tenant.lang}")
    private Language tenantLang;

    /**
     * Gets the tenant.
     *
     * @return the tenant
     */
    public String getTenant() {
	return tenant;
    }

    /**
     * Gets the tenant lang.
     *
     * @return the tenant lang
     */
    public Language getTenantLang() {
	return tenantLang;
    }

    /**
     * Gets the local.
     *
     * @param file the file
     * @return the local
     */
    public Locale getLocal(CommonFile file) {
	if (file != null && file.template().getLang() != null) {
	    return new Locale(file.template().getLang());
	}
	if (tenantLang != null) {
	    new Locale(tenantLang.getCode());
	}
	return new Locale(Language.EN.getCode());
    }

    public Locale getLocal(Message<?> msg) {
	if (msg == null || msg.hsm().getLang() == null) {
	    return new Locale(tenantLang.getCode());
	}
	return new Locale(msg.hsm().getLang());
    }

    public String getStaticUrl() {
	return jaxStaticUrl;
    }

    public String getStaticContext() {
	return jaxStaticContext;
    }

}
