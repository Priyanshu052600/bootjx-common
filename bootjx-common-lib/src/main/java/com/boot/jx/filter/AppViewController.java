package com.boot.jx.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boot.jx.AppConfig;

@Controller
public class AppViewController {

	@Autowired
	AppConfig appConfig;

	@GetMapping({ "favicon.ico", "/favicon.ico", "/favicon.icon", "/favicon.**" })
	@ResponseBody
	public ResponseEntity<byte[]> returnNoFavicon() {
		byte[] image = new byte[0];
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
	}

}
