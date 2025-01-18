package com.boot.test;

import java.text.ParseException;

import com.boot.model.MapModel.NodeEntry;

public class MapModelTest { // Noncompliant

	public static void main(String[] args) throws ParseException {
		compare(new NodeEntry<String>("v1"), new NodeEntry<String>("v2"));
		compare(new NodeEntry<String>("v2"), new NodeEntry<String>("v2"));
		compare(new NodeEntry<String>("v3"), new NodeEntry<String>("v2"));
		compare(new NodeEntry<String>(""), new NodeEntry<String>("v2"));
		compare(new NodeEntry<String>(), new NodeEntry<String>("v2"));
		compare(new NodeEntry<String>("v1"), new NodeEntry<String>());
		compare(new NodeEntry<String>("v1"), new NodeEntry<String>(""));
		compare(new NodeEntry<String>("v1"), new NodeEntry<String>("V1"));
		compare(new NodeEntry<String>("V1"), new NodeEntry<String>("v1"));
		compare(new NodeEntry<String>("v1"), new NodeEntry<String>("v11"));
	}

	public static void compare(NodeEntry<?> v1, NodeEntry<?> v2) {
		System.out.println(v1.asString() + " < " + v2.asString() + "  :  " + v1.lessThan(v2.asString()));
	}

}
