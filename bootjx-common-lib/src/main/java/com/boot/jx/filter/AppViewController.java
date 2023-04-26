package com.boot.jx.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boot.jx.AppConfig;
import com.boot.jx.AppConfigPackage.AppCommonConfig;
import com.boot.jx.swagger.DefaultSwaggerConfig;
import com.boot.utils.ArgUtil;

@Controller
public class AppViewController {

	@Autowired
	AppConfig appConfig;

	@Autowired(required = false)
	private AppCommonConfig appCommonConfig;

	@Autowired(required = false)
	private DefaultSwaggerConfig defaultSwaggerConfig;

	@RequestMapping(value = { "/swagger-ui.html" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String swagger(Model model) {

		model.addAttribute("CDN_URL", appConfig.getAppPrefix());
		if (ArgUtil.is(appCommonConfig)) {
			model.addAllAttributes(appCommonConfig.appAttributes());
		}
		if (defaultSwaggerConfig == null || !defaultSwaggerConfig.isLoggedIn()) {
			return "swagger-login";
		}
		return "swagger-ui";
	}

	@RequestMapping(value = { "/swagger-uix.html" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String swagger2(Model model) {

		model.addAttribute("CDN_URL", appConfig.getAppPrefix());
		if (ArgUtil.is(appCommonConfig)) {
			model.addAllAttributes(appCommonConfig.appAttributes());
		}

		if (defaultSwaggerConfig == null || !defaultSwaggerConfig.isLoggedIn()) {
			return "swagger-login";
		}

		model.addAttribute("APP_NAME", appConfig.getAppName());
		model.addAttribute("APP_CONTEXT", appConfig.getAppPrefix());
		return "swagger-uix";
	}

	@GetMapping({ "favicon.ico", "/favicon.ico", "/favicon.icon", "/favicon.**" })
	@ResponseBody
	public ResponseEntity<byte[]> returnNoFavicon() {
		byte[] image = new byte[0];
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
	}

}
