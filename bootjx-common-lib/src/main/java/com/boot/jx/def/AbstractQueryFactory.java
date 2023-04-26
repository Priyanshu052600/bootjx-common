package com.boot.jx.def;

import java.lang.annotation.Annotation;
import java.util.List;

import com.boot.common.ScopedBeanFactory;
import com.boot.jx.def.AbstractQueryFactory.IQueryTemplate;
import com.boot.jx.def.AbstractQueryFactory.QueryProcessor;
import com.boot.model.MapModel;

public abstract class AbstractQueryFactory<QT extends IQueryTemplate, QA extends Annotation, QP extends MapModel>
		extends ScopedBeanFactory<QT, QueryProcessor<?, QP>> {

	private static final long serialVersionUID = -2475686410285813714L;

	public static interface IQueryTemplate {
	}

	public static interface QueryProcessor<B, Q> {
		public List<B> process(Q params);
	}

	public AbstractQueryFactory(List<QueryProcessor<?, QP>> libs) {
		super(libs);
	}

	public abstract Class<QA> getAnnotionClass();

	public abstract QT[] getValues(QA annotation);

	@Override
	public QT[] getKeys(QueryProcessor<?, QP> lib) {
		QA annotation = lib.getClass().getAnnotation(getAnnotionClass());
		return getValues(annotation);
	}

}
