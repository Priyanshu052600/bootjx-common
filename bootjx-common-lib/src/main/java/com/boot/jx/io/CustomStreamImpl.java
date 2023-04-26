package com.boot.jx.io;

import java.util.stream.Stream;

public class CustomStreamImpl<T> implements CustomStream<T> {

    private final Stream<T> stream;

    @Override
    public Stream<T> stream() {
	return stream;
    }

    public CustomStreamImpl(Stream<T> stream) {
	this.stream = stream;
    }


}
