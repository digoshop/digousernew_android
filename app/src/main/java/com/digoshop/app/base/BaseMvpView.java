package com.digoshop.app.base;

public interface BaseMvpView<T> {
	void showLoading();
	void hideLoading();
	void bind(T t);
	void unbind();
}
