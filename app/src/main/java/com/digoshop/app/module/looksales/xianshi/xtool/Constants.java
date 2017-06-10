package com.digoshop.app.module.looksales.xianshi.xtool;

import com.digoshop.app.module.looksales.xianshi.bean.NewsClassify;

import java.util.ArrayList;


public class Constants {public static ArrayList<NewsClassify> getData() {
	ArrayList<NewsClassify> newsClassify = new ArrayList<NewsClassify>();
	NewsClassify classify = new NewsClassify();
	classify.setId(0);
	classify.setTitle("8:00\n推荐");
	newsClassify.add(classify);
	classify = new NewsClassify();
	classify.setId(1);
	classify.setTitle("8:00\n热点");
	newsClassify.add(classify);
	classify = new NewsClassify();
	classify.setId(2);
	classify.setTitle("8:00\n数码");
	newsClassify.add(classify);
	classify = new NewsClassify();
	classify.setId(3);
	classify.setTitle("8:00\n杭州");
	newsClassify.add(classify);
	classify = new NewsClassify();
	classify.setId(4);
	classify.setTitle("8:00\n社会");
	newsClassify.add(classify);
	classify = new NewsClassify();
	classify.setId(5);
	classify.setTitle("8:00\n娱乐");
	newsClassify.add(classify);
	classify = new NewsClassify();
	classify.setId(6);
	classify.setTitle("8:00\n科技");
	newsClassify.add(classify);
	classify = new NewsClassify();
	classify.setId(7);
	classify.setTitle("8:00\n汽车");
	newsClassify.add(classify);
	return newsClassify;
}
}
