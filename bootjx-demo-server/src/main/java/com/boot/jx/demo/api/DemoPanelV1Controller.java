package com.boot.jx.demo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.boot.jx.AppConfig;
import com.boot.jx.AppConfigPackage.AppCommonConfig;
import com.boot.utils.ArgUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Demo Panel", description = "API's for Panel", hidden = true)
@Controller
@RequestMapping("/panel")
public class DemoPanelV1Controller {
	@Autowired
	private AppConfig appConfig;

	@Autowired(required = false)
	private AppCommonConfig appCommonConfig;

	@ApiOperation(value = "Page", hidden = true)
	@RequestMapping(path = { "", "/", "/**" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String defaultPage(Model model) {
		model.addAttribute("APP_NAME", appConfig.getAppName());
		model.addAttribute("APP_CONTEXT", appConfig.getAppPrefix());
		model.addAttribute("CDN_URL", appConfig.getAppPrefix());
		if (ArgUtil.is(appCommonConfig)) {
			model.addAllAttributes(appCommonConfig.appAttributes());
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("APP_USER", "");
		model.addAttribute("APP_USER_NAME", "User");
		model.addAttribute("APP_USER_ROLE", "['GUEST']");
		return "app-contak";
	}

}
