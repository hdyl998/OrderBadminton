package com.order.badminton.adapter;

public interface IMultiItemViewType<T> {
	  int getViewTypeCount();
	  int getItemViewType(int position, T t);
	  int getLayoutId(int viewType);
}
