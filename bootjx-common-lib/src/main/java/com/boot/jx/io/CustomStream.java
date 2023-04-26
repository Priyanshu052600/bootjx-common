package com.boot.jx.io;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public interface CustomStream<T> extends Stream<T> {

    public Stream<T> stream();

    @Override
    public default Stream<T> filter(Predicate<? super T> predicate) {
	return new CustomStreamImpl<T>(stream().filter(predicate));
    }

    @Override
    public default <R> CustomStreamImpl<R> map(Function<? super T, ? extends R> mapper) {
	return new CustomStreamImpl<R>(stream().map(mapper));
    }

    @Override
    public default IntStream mapToInt(ToIntFunction<? super T> mapper) {
	return stream().mapToInt(mapper);
    }

    @Override
    default public LongStream mapToLong(ToLongFunction<? super T> mapper) {
	return stream().mapToLong(mapper);
    }

    @Override
    default public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
	return stream().mapToDouble(mapper);
    }

    @Override
    default public <R> CustomStreamImpl<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
	return new CustomStreamImpl<R>(stream().flatMap(mapper));
    }

    @Override
    default public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
	return stream().flatMapToInt(mapper);
    }

    @Override
    default public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
	return stream().flatMapToLong(mapper);
    }

    @Override
    default public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
	return stream().flatMapToDouble(mapper);
    }

    @Override
    default public CustomStreamImpl<T> distinct() {
	return new CustomStreamImpl<T>(stream().distinct());
    }

    @Override
    default public CustomStreamImpl<T> sorted() {
	return new CustomStreamImpl<T>(stream().sorted());
    }

    @Override
    default public CustomStreamImpl<T> sorted(Comparator<? super T> comparator) {
	return new CustomStreamImpl<T>(stream().sorted(comparator));
    }

    @Override
    default public CustomStreamImpl<T> peek(Consumer<? super T> action) {
	return new CustomStreamImpl<T>(stream().peek(action));
    }

    @Override
    default public CustomStreamImpl<T> limit(long maxSize) {
	return new CustomStreamImpl<T>(stream().limit(maxSize));
    }

    @Override
    default public CustomStreamImpl<T> skip(long n) {
	return new CustomStreamImpl<T>(stream().skip(n));
    }

    @Override
    default public void forEach(Consumer<? super T> action) {
	stream().forEach(action);
    }

    @Override
    default public void forEachOrdered(Consumer<? super T> action) {
	stream().forEachOrdered(action);
    }

    @Override
    default public Object[] toArray() {
	return stream().toArray();
    }

    @Override
    default public <A> A[] toArray(IntFunction<A[]> generator) {
	return stream().toArray(generator);
    }

    @Override
    default public T reduce(T identity, BinaryOperator<T> accumulator) {
	return stream().reduce(identity, accumulator);
    }

    @Override
    default public Optional<T> reduce(BinaryOperator<T> accumulator) {
	return stream().reduce(accumulator);
    }

    @Override
    default public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
	return stream().reduce(identity, accumulator, combiner);
    }

    @Override
    default public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator,
	    BiConsumer<R, R> combiner) {
	return stream().collect(supplier, accumulator, combiner);
    }

    @Override
    default public <R, A> R collect(Collector<? super T, A, R> collector) {
	return stream().collect(collector);
    }

    @Override
    default public Optional<T> min(Comparator<? super T> comparator) {
	return stream().min(comparator);
    }

    @Override
    default public Optional<T> max(Comparator<? super T> comparator) {
	return stream().max(comparator);
    }

    @Override
    default public long count() {
	return stream().count();
    }

    @Override
    default public boolean anyMatch(Predicate<? super T> predicate) {
	return stream().anyMatch(predicate);
    }

    @Override
    default public boolean allMatch(Predicate<? super T> predicate) {
	return stream().allMatch(predicate);
    }

    @Override
    default public boolean noneMatch(Predicate<? super T> predicate) {
	return stream().noneMatch(predicate);
    }

    @Override
    default public Optional<T> findFirst() {
	return stream().findFirst();
    }

    @Override
    default public Optional<T> findAny() {
	return stream().findAny();
    }

    @Override
    default public Iterator<T> iterator() {
	return stream().iterator();
    }

    @Override
    default public Spliterator<T> spliterator() {
	return stream().spliterator();
    }

    @Override
    default public boolean isParallel() {
	return stream().isParallel();
    }

    @Override
    default public CustomStreamImpl<T> sequential() {
	return new CustomStreamImpl<T>(stream().sequential());
    }

    @Override
    default public CustomStreamImpl<T> parallel() {
	return new CustomStreamImpl<T>(stream().parallel());
    }

    @Override
    default public CustomStreamImpl<T> unordered() {
	return new CustomStreamImpl<T>(stream().unordered());
    }

    @Override
    default public CustomStreamImpl<T> onClose(Runnable closeHandler) {
	return new CustomStreamImpl<T>(stream().onClose(closeHandler));
    }

    @Override
    default public void close() {
	stream().close();
    }

}
