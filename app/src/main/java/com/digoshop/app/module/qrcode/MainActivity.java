package com.digoshop.app.module.qrcode;

/**
 * Created by lsqbeyond on 2017/1/19.
 */

import android.app.Activity;

// implements View.OnClickListener
public class MainActivity extends Activity  {

//    /**
//     * 用于展示消息的Fragment
//     */
//    private MessageFragment messageFragment;
//
//    /**
//     * 用于展示联系人的Fragment
//     */
//    private ContactsFragment contactsFragment;
//
//    /**
//     * 用于展示动态的Fragment
//     */
//    private NewsFragment newsFragment;
//
//    /**
//     * 用于展示设置的Fragment
//     */
//    private SettingFragment settingFragment;
//
//    /**
//     * 消息界面布局
//     */
//    private View messageLayout;
//
//    /**
//     * 联系人界面布局
//     */
//    private View contactsLayout;
//
//    /**
//     * 动态界面布局
//     */
//    private View newsLayout;
//
//    /**
//     * 设置界面布局
//     */
//    private View settingLayout;
//
//    /**
//     * 在Tab布局上显示消息图标的控件
//     */
//    private ImageView messageImage;
//
//    /**
//     * 在Tab布局上显示联系人图标的控件
//     */
//    private ImageView contactsImage;
//
//    /**
//     * 在Tab布局上显示动态图标的控件
//     */
//    private ImageView newsImage;
//
//    /**
//     * 在Tab布局上显示设置图标的控件
//     */
//    private ImageView settingImage;
//
//    /**
//     * 在Tab布局上显示消息标题的控件
//     */
//    private TextView messageText;
//
//    /**
//     * 在Tab布局上显示联系人标题的控件
//     */
//    private TextView contactsText;
//
//    /**
//     * 在Tab布局上显示动态标题的控件
//     */
//    private TextView newsText;
//
//    /**
//     * 在Tab布局上显示设置标题的控件
//     */
//    private TextView settingText;
//
//    /**
//     * 用于对Fragment进行管理
//     */
//    private FragmentManager fragmentManager;
//
//    /**
//     * 保存当前显示的是第几页
//     */
//    private int currentPage = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_main);
//        if(null != savedInstanceState){
//            currentPage = savedInstanceState.getInt("neo");
//        }
//
//        fragmentManager = getFragmentManager();
//        //在FragmentManager里面根据Tag去找相应的fragment. 用户屏幕发生旋转，重新调用onCreate方法。否则会发生重叠
//        messageFragment = (MessageFragment) fragmentManager.findFragmentByTag("message");
//        contactsFragment = (ContactsFragment) fragmentManager.findFragmentByTag("contacts");
//        newsFragment = (NewsFragment) fragmentManager.findFragmentByTag("news");
//        settingFragment = (SettingFragment) fragmentManager.findFragmentByTag("setting");
//        // 第一次启动时选中第0个tab
//        setTabSelection(currentPage);
//    }
//
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.message_layout:
//                // 当点击了消息tab时，选中第1个tab
//                setTabSelection(0);
//                currentPage = 0;
//                break;
//            case R.id.contacts_layout:
//                // 当点击了联系人tab时，选中第2个tab
//                setTabSelection(1);
//                currentPage = 1;
//                break;
//            case R.id.news_layout:
//                // 当点击了动态tab时，选中第3个tab
//                setTabSelection(2);
//                currentPage = 2;
//                break;
//            case R.id.setting_layout:
//                // 当点击了设置tab时，选中第4个tab
//                setTabSelection(3);
//                currentPage = 3;
//                break;
//            default:
//                break;
//        }
//    }
//
//    /**
//     * 根据传入的index参数来设置选中的tab页。
//     *
//     * @param index
//     *            每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。
//     */
//    private void setTabSelection(int index) {
//        // 开启一个Fragment事务
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
//        hideFragments(transaction);
//        switch (index) {
//            case 0:
//                // 当点击了消息tab时，改变控件的图片和文字颜色
//                messageImage.setImageResource(R.drawable.message_selected);
//                messageText.setTextColor(Color.WHITE);
//                if (messageFragment == null) {
//                    // 如果MessageFragment为空，则创建一个并添加到界面上
//                    messageFragment = new MessageFragment();
//                    transaction.add(R.id.content, messageFragment,"message");
//                } else {
//                    // 如果MessageFragment不为空，则直接将它显示出来
//                    transaction.show(messageFragment);
//                }
//
//                break;
//            case 1:
//                // 当点击了联系人tab时，改变控件的图片和文字颜色
//                contactsImage.setImageResource(R.drawable.contacts_selected);
//                contactsText.setTextColor(Color.WHITE);
//                //    contactsFragment = (ContactsFragment) fragmentManager.findFragmentByTag("contacts");
//                if (contactsFragment == null) {
//                    // 如果ContactsFragment为空，则创建一个并添加到界面上
//                    contactsFragment = new ContactsFragment();
//                    transaction.add(R.id.content, contactsFragment,"contacts");
//                } else {
//                    // 如果ContactsFragment不为空，则直接将它显示出来
//                    transaction.show(contactsFragment);
//                }
//                break;
//            case 2:
//                // 当点击了动态tab时，改变控件的图片和文字颜色
//                newsImage.setImageResource(R.drawable.news_selected);
//                newsText.setTextColor(Color.WHITE);
//                //    newsFragment = (NewsFragment) fragmentManager.findFragmentByTag("news");
//                if (newsFragment == null) {
//                    // 如果NewsFragment为空，则创建一个并添加到界面上
//                    newsFragment = new NewsFragment();
//                    transaction.add(R.id.content, newsFragment,"news");
//                } else {
//                    // 如果NewsFragment不为空，则直接将它显示出来
//                    transaction.show(newsFragment);
//                }
//                break;
//            case 3:
//            default:
//                // 当点击了设置tab时，改变控件的图片和文字颜色
//                settingImage.setImageResource(R.drawable.setting_selected);
//                settingText.setTextColor(Color.WHITE);
//                //    settingFragment = (SettingFragment) fragmentManager.findFragmentByTag("setting");
//                if (settingFragment == null) {
//                    // 如果SettingFragment为空，则创建一个并添加到界面上
//                    settingFragment = new SettingFragment();
//                    transaction.add(R.id.content, settingFragment,"setting");
//                } else {
//                    // 如果SettingFragment不为空，则直接将它显示出来
//                    transaction.show(settingFragment);
//                }
//                break;
//        }
//        transaction.commit();
//    }
//
//
//
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        outState.putInt("neo",currentPage);
//        super.onSaveInstanceState(outState);
//    }
}
