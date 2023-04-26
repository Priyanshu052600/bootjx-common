package com.boot.test;

import java.util.regex.Pattern;

import com.boot.model.MapModel;
import com.boot.utils.CryptoUtil;
import com.boot.utils.JsonUtil;

public class ModeTest { // Noncompliant

    public static final Pattern pattern = Pattern.compile("^LINK <(.*)>$");

    /**
     * This is just a test method
     * 
     * @param args
     */
    public static void main(String[] args) {
	String json = JsonUtil.toJson(MapModel.createInstance().put("x", "Mr.X"));
	System.out.println("json : " + json);

	MapModel map = JsonUtil.parse(json, MapModel.class);
	System.out.println(map.getString("x"));

    }

    public static void generate() {
	System.out.println(CryptoUtil.generateHMAC("appd-kwt.amxremit.com", "traceId"));
    }

}
