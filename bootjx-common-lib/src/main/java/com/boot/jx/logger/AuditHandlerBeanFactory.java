package com.boot.jx.logger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boot.common.HandlerBeanFactory;

@Component
public class AuditHandlerBeanFactory extends HandlerBeanFactory<AuditHandler> {

	private static final long serialVersionUID = 2959267620648739583L;

	public AuditHandlerBeanFactory(@Autowired(required = false) List<AuditHandler> libs) {
		super(libs);
	}

}
