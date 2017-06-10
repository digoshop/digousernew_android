package com.digoshop.app.module.home.mvp;

import com.digoshop.app.base.BaseMvpPresenter;

import android.os.Handler;

 

public class HomePresenter extends BaseMvpPresenter {
	
	HomeView homeView;
	Handler pHandler = new Handler();
	
	
	public HomePresenter(HomeView homeView){
		this.homeView = homeView;
	}
	
	// 刷新数据
	public void refreshData() {
	}
	
//	加载更多数据
	public void loadMoreData(int page){
		
	}
}
