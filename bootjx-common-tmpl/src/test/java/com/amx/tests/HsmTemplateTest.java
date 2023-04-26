package com.amx.tests;

import java.net.URL;

import org.junit.Test;

import com.boot.jx.tmpl.CommonTmpPackageImpl;
import com.boot.model.MapModel;
import com.boot.utils.FileUtil;
import com.boot.utils.JsonUtil;

public class HsmTemplateTest {

	public static CommonTmpPackageImpl ppkg = new CommonTmpPackageImpl();

	public static void main(String[] arg) {
		new HsmTemplateTest().wa360Templates();
	}

	@Test
	public void wa360Templates() {
		// assertEquals("t1", StringUtils.trim("/abc/def/ghij", '/'), "abc/def/ghij");
		URL url = FileUtil.getResource("sample/sample_model.json");
		String json = FileUtil.read(url);
		MapModel map = MapModel.from(JsonUtil.fromJsonToMap(json));

		System.out.println(ppkg.process("   "
				//+ "{{contact.name}} - "
				//+ "{{data.customer}} "
				+ "{{any data.customer contact.name}} ",
				map.toMap(), CommonTmpPackageImpl.HANDLEBARS_JS));
	}

}
