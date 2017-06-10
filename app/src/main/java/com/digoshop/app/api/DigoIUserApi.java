package com.digoshop.app.api;

import com.digoshop.app.base.BaseInfo;
import com.digoshop.app.module.arrountshops.model.MyArrayListCityBean;
import com.digoshop.app.module.arrountshops.model.ShopInfoBean;
import com.digoshop.app.module.customServices.model.CategoryChooseBean;
import com.digoshop.app.module.home.cityselect.model.HotCityBean;
import com.digoshop.app.module.home.model.ActivityDetailBean;
import com.digoshop.app.module.home.model.Adimg;
import com.digoshop.app.module.home.model.HomeInfo;
import com.digoshop.app.module.looksales.model.AuctionBean;
import com.digoshop.app.module.looksales.model.ExChangeBean;
import com.digoshop.app.module.looksales.model.ExChangeDetail_Data;
import com.digoshop.app.module.looksales.model.FlashSaleDetailBean;
import com.digoshop.app.module.looksales.model.FlashSaleListItemBean;
import com.digoshop.app.module.looksales.model.PoitGoodBean;
import com.digoshop.app.module.looksales.model.SaleHistoryBean;
import com.digoshop.app.module.looksales.model.TiemTitleData;
import com.digoshop.app.module.looksales.xianshi.bean.AddPriceBean;
import com.digoshop.app.module.search.ShopinfoData;
import com.digoshop.app.module.seekshop.model.BrandDate;
import com.digoshop.app.module.seekshop.model.SeekCateGoryData;
import com.digoshop.app.module.setting.model.AppUpdata;
import com.digoshop.app.module.shopdetail.model.ShopDetailDataBean;
import com.digoshop.app.module.shopdetailnew.model.ShopDetailNData;
import com.digoshop.app.module.shopdetailnew.model.ShopProduct;
import com.digoshop.app.module.storedetail.model.Merchant;
import com.digoshop.app.module.storedetail.model.Shopdatalist;
import com.digoshop.app.module.storedetail.model.StoreDetailInfo;
import com.digoshop.app.module.storedetail.model.UserInfoData;
import com.digoshop.app.module.userCenter.model.AddShopInviteUserInfo;
import com.digoshop.app.module.userCenter.model.CommentData;
import com.digoshop.app.module.userCenter.model.CommentInfo;
import com.digoshop.app.module.userCenter.model.CostHistorData;
import com.digoshop.app.module.userCenter.model.CouponDetailData;
import com.digoshop.app.module.userCenter.model.CustomizedReply;
import com.digoshop.app.module.userCenter.model.DiscountcouponData;
import com.digoshop.app.module.userCenter.model.Discountcoupons;
import com.digoshop.app.module.userCenter.model.Markbean;
import com.digoshop.app.module.userCenter.model.MerchanRreplyEntity;
import com.digoshop.app.module.userCenter.model.MessageData;
import com.digoshop.app.module.userCenter.model.MyLikeListData;
import com.digoshop.app.module.userCenter.model.MybagEntityData;
import com.digoshop.app.module.userCenter.model.MycardData;
import com.digoshop.app.module.userCenter.model.MymenbrescardData;
import com.digoshop.app.module.userCenter.model.PointsData;
import com.digoshop.app.module.userCenter.model.ShopInviteDetailBean;
import com.digoshop.app.module.userCenter.model.SingData;
import com.digoshop.app.module.userCenter.model.UserDataBean;
import com.digoshop.app.module.userCenter.model.UserStatBean;
import com.digoshop.app.module.welcome.module.DeviceInfo;
import com.digoshop.app.utils.db.CityBean;
import com.digoshop.app.utils.http.WSError;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public interface DigoIUserApi {
    public ArrayList<ShopProduct> get_MyProductNotLogged(String nationCode, String prtag, String page) throws WSError, JSONException;

    public boolean PutPoneisAlive(String phone
    ,String devicetoken, String deviceuid) throws WSError, JSONException;

    public ArrayList<ShopProduct> get_exchange_listNew(String moid, String id, String nationCode, String page, String page_length) throws WSError, JSONException;

    public ArrayList<CategoryChooseBean> getShopAllCategory(String sid) throws WSError, JSONException;

    public ExChangeDetail_Data getProductDetail(String pid,
                                                String pt ) throws WSError, JSONException;

    public ArrayList<CategoryChooseBean> getShopCategory(String sid) throws WSError, JSONException;

    public ShopDetailNData getShopEnv(String sid) throws WSError, JSONException;

    public ShopDetailNData getShopnProdcuts(String productType, String tid, String nationCode, String priceMode, String page, String moid) throws WSError, JSONException;

    public ShopDetailNData getShopDetailN_aty(String sid
    ) throws WSError, JSONException;

    public ShopDetailNData getShopDetailN(String sid
    ) throws WSError, JSONException;

    /**
     * @param userSign APP端设备ID
     * @param couKeyID 视频课件的编号
     * @param couVsion 视频课件的版本号
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public String videodown(String userSign, String couKeyID, String couVsion) throws WSError, JSONException;

    /**
     * @param userSign APP端设备ID
     * @param verSign  APP标识；
     * @param verVsion APP版本号
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public String verison_ar1(String userSign, String verSign, String verVsion) throws WSError, JSONException;

    /**
     * 查询首页的广告图
     *
     * @param nationCode 定位坐标
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public List<Adimg> queryRecommendMerchant(String nationCode) throws WSError, JSONException;

    /**
     * @param userSign APP端设备ID
     * @param uactCode 激活码
     * @param facKn    系统令牌
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public String actCodetwo(String userSign, String uactCode, String facKn) throws WSError, JSONException;

    /**
     * @param userSign APP端设备ID
     * @param uactCode 激活码
     * @param facKn    系统令牌
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public String actCodeone(String userSign, String uactCode) throws WSError, JSONException;

    /**
     * 设备信息注册
     *
     * @param ip         设备ip
     * @param deviceinfo 客户端设备信息
     * @return
     * @throws WSError       错误信息
     * @throws JSONException 解析错误信息
     */
    public BaseInfo register_by_device(String ip, DeviceInfo deviceinfo) throws WSError, JSONException;

    /**
     * 发送短信验证码
     *
     * @param mobile 手机号
     * @param check  是否检查手机号注没注册 0 不检查 1检查
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public Boolean sendsms(String mobile, int check, String devicetoken, String deviceuid) throws WSError, JSONException;

    /**
     * @param mobile  手机号
     * @param smscode 短信验证码
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public Boolean verifysmscode(String mobile, String smscode) throws WSError, JSONException;

    /**
     * @param mobile   手机号
     * @param password 密码
     * @param invcode  邀请码 非必填
     * @param email    邮箱
     * @param smscode  短信验证码
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public UserDataBean register(UserDataBean userInfo) throws WSError, JSONException;

    /**
     * @param userInfo 登录用户信息
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public UserDataBean login(UserDataBean userInfo) throws WSError, JSONException;

    /**
     * 修改密码
     *
     * @param old_pwd  老的密码
     * @param new_pwd  新的密码
     * @param mobile   手机号
     * @param smscode  验证码
     * @param typecode 修改密码类型 1快捷登录修改  2个人中心修改
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public Boolean resetpwd(String mobile, String old_pwd, String new_pwd, String smscode, int typecode) throws WSError, JSONException;

    /**
     * @param situation 使用情景 0正常使用 1 启动应用 2 唤醒应用
     * @param password  用户密码
     * @return
     */
    public UserDataBean keepalive(String situation, String password) throws WSError, JSONException;

    /**
     * @return
     */
    public UserDataBean checktoken() throws WSError, JSONException;

    /**
     * @param userInfo 手机验证码登录
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public UserDataBean login_sms(UserDataBean userInfo) throws WSError, JSONException;

    /**
     * @param token       第三方的
     * @param type        1微信，2QQ，3微博
     * @param uid         从第三方获得的uid
     * @param channel     下载渠道
     * @param app_version APP版本号
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public UserDataBean login_third(String access_token, String type, String openid, String app_version, String devicetoken, String deviceuid)
            throws WSError, JSONException;

    /**
     * 获取商场详情
     *
     * @param MerchantId
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public StoreDetailInfo getMerchantDetail(String MerchantId) throws WSError, JSONException;

    /**
     * 获取兑换列表
     *
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ArrayList<PoitGoodBean> get_exchange_list(String moid, String id, String nationCode, String page, String page_length) throws WSError, JSONException;

    /**
     * 活动详情
     *
     * @param newId 活动id
     * @param lon   经度
     * @param lat   维度
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ActivityDetailBean getNewById(String newId, String lon, String lat) throws WSError, JSONException;

    /**
     * 根据城市id获取首页-限时竞拍，推荐活动两个优惠券，推荐产品店铺列表，推荐服务店铺列表
     *
     * @param nationCode
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public HomeInfo queryRecommendShop(String nationCode) throws WSError, JSONException;

    /**
     * 获取用户消费记录
     *
     * @param is_comment 默认全部-不填 0未评价 1已评价
     * @param max_id     最大ID
     * @param min_id     最小ID
     * @param count      条数
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public CostHistorData get_pay_list(String is_comment, String max_id, String min_id, String count)
            throws WSError, JSONException;

    /**
     * 查询历史积分
     *
     * @param max_id 往前翻页的时候最小值传到此
     * @param min_id 往后翻页的时候最大值传到此
     * @param count  每页现实条数
     * @param start  开始时间
     * @param end    结束时间
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public PointsData get_My_points(String max_id, String min_id, int count, String start, String end)
            throws WSError, JSONException;

    /**
     * 我的定制列表
     *
     * @param st 0获取发布列表 1获取商家回复的列表 -1获取已结束列表
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public List<MerchanRreplyEntity> get_custservicelist(String st, int page, int page_length) throws WSError, JSONException;

    /**
     * 获取商家回复详情
     *
     * @param custServiceId 定制服务ID
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public CustomizedReply get_custservice_content(String custServiceId) throws WSError, JSONException;

    // /**
    // * 获取商家回复详情
    // *
    // * @param custServiceId
    // * 定制服务ID
    // * @return
    // * @throws WSError
    // * @throws JSONException
    // */
    // public ArrayList<CurlistEntity> get_Curlist(String custServiceId) throws
    // WSError, JSONException;

    /**
     * 我的关注列表
     *
     * @param page        页码
     * @param page_length 每页数据
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public MyLikeListData getMyLikeslist(int page, int page_length) throws WSError, JSONException;

    /**
     * 我的优惠券列表
     *
     * @param cs          cs状态 3 未使用 4已使用 5 已过期
     * @param page        页码
     * @param page_length 每页条数
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public DiscountcouponData getPayDiscouncoupons(String cs, String page, String page_length)
            throws WSError, JSONException;

    /**
     * 删除关注商铺
     *
     * @param tid
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public String cancel_shop(String tid) throws WSError, JSONException;

    /**
     * 根据type 获取经营类别，当moid为空时候获取的是一级的
     *
     * @param type               1 商品类别 2是服务类别
     * @param level              level 是经营类别的级别 1是1级类别 2是2级类别 3是3级类别
     * @param moid               类别id
     * @param page_length        每页长度
     * @param page               每页类别
     * @param nationCode就是当前城市代码 带城市编码查询 代表需要返回推荐活动
     * @param page               child
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ArrayList<CategoryChooseBean> get_managing_type(String type, String level, String moid, String nationCode,
                                                           String child, String page_length, String page) throws WSError, JSONException;

    /**
     * 获取找商铺二级三级数据
     *
     * @param type               1 商品类别 2是服务类别
     * @param level              level 是经营类别的级别 1是1级类别 2是2级类别 3是3级类别
     * @param moid               类别id
     * @param page_length        每页长度
     * @param page               每页类别
     * @param nationCode就是当前城市代码 带城市编码查询 代表需要返回推荐活动
     * @param page               child
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public SeekCateGoryData get_managing_twothree(String type, String level, String moid, String nationCode,
                                                  String child, String page_length, String page) throws WSError, JSONException;

    /**
     * 提交我的定制服务/商品采购
     *
     * @param cid       类别id
     * @param csName    定制服务标题
     * @param desc      我是定制补充内容
     * @param longitude 经纬度
     * @param latitude  经纬度
     * @param etime     回复截止日期
     * @param urls      多张图片加逗号
     * @param uName     姓名
     * @param uAddress  地址
     * @param uMobile   电话
     * @param tp        1-定制服务 2-商品采购
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public boolean create_myservice(String cid, String csName, String desc, String longitude, String latitude,
                                    String etime, String urls, String uName, String uAddress, String uMobile, String tp, String nid)
            throws WSError, JSONException;

    /**
     * 根据城市id获取业态商圈
     *
     * @param nationCode 城市id
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ArrayList<Merchant> getMerchantByNationCode(String nationCode) throws WSError, JSONException;

    /**
     * 根据城市id查询活动列表
     *
     * @param nationCode  城市code
     * @param regionId    所在区域id,通过城市code查本地数据库
     * @param type        1 商品类 2服务类 空为全部
     * @param lat         经纬度
     * @param lgt         经纬度
     * @param page        页码
     * @param page_length 页长
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ArrayList<ActivityDetailBean> queryNewsList(String nationCode, String sort, String regionId, String type, String lat,
                                                       String lgt, String page, String page_length) throws WSError, JSONException;

    /**
     * 根据app所传的城市id获取优惠券列表
     *
     * @param nationCode
     *            app通过高德获取的城市id
     * @param page
     *            页码
     * @param page_length
     *            每页显示条数
     * @return
     * @throws WSError
     * @throws JSONException
     */
    /**
     * 根据app所传的城市id获取优惠券列表
     *
     * @param nationCode  app通过高德获取的城市id
     * @param typeId      代金券/1000000 折扣券/1000001 满减券/1000002
     * @param otype       0全部 1 商品 2 服务类
     * @param page        页码
     * @param page_length 页长
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ArrayList<Discountcoupons> getCouponList(String nationCode, String moid, String type, String coupontype, String page,
                                                    String page_length) throws WSError, JSONException;

    public ArrayList<Discountcoupons> getCouponListJiXuan(String nationCode, String page) throws WSError, JSONException;

    /**
     * 根据app所传的城市id获取优惠券列表
     *
     * @param nationCode  app通过高德获取的城市id
     * @param regionId    区县id 全部是空
     * @param typeId      代金券/1000000 折扣券/1000001 满减券/1000002
     * @param otype       0全部 1 商品 2 服务类
     * @param lat         经纬度
     * @param lgt经纬度
     * @param page        页码
     * @param page_length 页长
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public boolean get_nation_coupon_list(String nationCode, String regionId, String typeId, String otype, String lat,
                                          String lgt, String page, String page_length) throws WSError, JSONException;

    /**
     * 获取用户统计信息
     *
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public UserStatBean get_stat() throws WSError, JSONException;

    /**
     * 获取我的消息列表
     *
     * @param type        98商户消息 100系统消息 全部消息 -1
     * @param page        页码，默认1
     * @param page_length 页长，[1,50]，默认50
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public MessageData get_message_list(String type, String page, String page_length) throws WSError, JSONException;

    /**
     * 通知服务器 把当前用户的所有未读消息数重置为0
     *
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public boolean FlushMessageList() throws WSError, JSONException;

    /**
     * 获取未读消息
     *
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public String NoReadMessag() throws WSError, JSONException;

    /**
     * 更新消息状态
     *
     * @param nidarray
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public boolean update_notice(String nidarray) throws WSError, JSONException;

    /**
     * 删除我的消息
     *
     * @param nid   消息id
     * @param empty 清空(0-false, 1-true) empty=1则删除全部notice
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public boolean delete_message(String nid_array, String empty) throws WSError, JSONException;

    /**
     * 获取评论列表
     *
     * @param page        页数
     * @param page_length 页长
     * @param sort        排序方式 0正序 1倒序 默认倒序
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public CommentData get_comment_list(String page, String page_length, String sort) throws WSError, JSONException;

    /**
     * 获取我的礼包
     *
     * @param page        页数
     * @param page_length 页长
     * @param str         排序方式 0正序 1倒序 默认倒序
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public MybagEntityData getMybagWall(String page, String page_length, String str) throws WSError, JSONException;

    /**
     * 获取我的卡包
     *
     * @param page        页数
     * @param page_length 页长
     * @param sort        排序方式 0正序 1倒序 默认倒序
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public MycardData getMycardbagWall(String page, String page_length) throws WSError, JSONException;

    /**
     * 提交我的评论
     *
     * @param page        页数
     * @param page_length 页长
     * @param sort        排序方式 0正序 1倒序 默认倒序
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public boolean submit_comment(String sid, String eid, String type, String text, String imgs)
            throws WSError, JSONException;

    /**
     * 会员卡详情
     *
     * @param vip_id 会员卡id
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public MymenbrescardData vip_detail(String vip_id, String page,
                                        String page_length) throws WSError, JSONException;

    /**
     * 解绑会员卡
     *
     * @param vid
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public MycardData un_bind_vip(String vid) throws WSError, JSONException;

    /**
     * 寻服务和找商铺的上榜好店
     *
     * @param typestr    寻服务内页推荐位 (上榜好店SERVICE_GOOD_SHOP 找商铺内页推荐位(上榜好店) GOOD_SHOPS_TAG
     * @param nationCode
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ArrayList<ShopInfoBean> GoodShops(String typestr, String nationCode) throws WSError, JSONException;

    /**
     * @param content 反馈内容
     * @param from    来源
     * @param new_pwd
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public boolean feedback(String type, String content, String from, String sid) throws WSError, JSONException;

    /**
     * 签到
     *
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public SingData sing_in() throws WSError, JSONException;

    /**
     * @param info 0 false 1true 是否查询用户详细信息
     * @param hide 0 false 1true 是否隐藏用户手机号和邮箱号
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public UserInfoData get_user(String info, String hide) throws WSError, JSONException;

    /**
     * @param realname    // 真实姓名
     * @param nick        //昵称
     * @param area        // 所在区域 ?
     * @param address     // 详细地址
     * @param village     // 所在小区
     * @param birthday    // 生日
     * @param occipaction // 职业
     * @param wedding     // 结婚纪念日
     * @param gen         // 性别 1 男 2 女 0 保密
     * @param avatar      //头像
     * @param hobby       //兴趣
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public UserInfoData update_user_info(String realname, String nick, String area, String address, String village,
                                         String birthday, String occipaction, String wedding, String gen, String avatar, String hobby, String province, String citycode, String city, String district)
            throws WSError, JSONException;

    /**
     * 获取商场或店铺的活动列表（有数据）
     *
     * @param targetId   商场 /店铺id
     * @param targetType targetType 1商场 2店铺
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ArrayList<ActivityDetailBean> getStoreorShopAtyList(String targetId, String targetType, String page,
                                                               String page_length) throws WSError, JSONException;

    /**
     * 根据据商场或商铺的id获取优惠券列表
     *
     * @param targetId   商场 /店铺id
     * @param targetType targetType 1商场 2店铺
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ArrayList<Discountcoupons> getStoreorShopCouponList(String targetId, String targetType, String page,
                                                               String page_length) throws WSError, JSONException;

    /**
     * 店铺搜索接口
     *
     * @param keywords    搜索文字
     * @param page
     * @param page_length
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ShopinfoData SerachShops(String type, String keywords, String mark, String nationCode)
            throws WSError, JSONException;

    /**
     * 获取优惠券详情
     *
     * @param couponBatchId 优惠券id
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public CouponDetailData coupon_detail(String couponBatchId) throws WSError, JSONException;

    /**
     * 领取优惠券
     *
     * @param couponBatchId 优惠券ID
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public boolean get_coupon(String couponBatchId) throws WSError, JSONException;

    /**
     * 热门关键词接口
     *
     * @param nationCode 城市id
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public boolean getSearchApi(String nationCode) throws WSError, JSONException;

    /**
     * 多条件搜索店铺列表
     *
     * @param brandId    //品牌id
     * @param lat        经纬度
     * @param lgt        经纬度
     * @param mfid       业态商圈中的商场id
     * @param operateId  类别id
     * @param mlvl       查询几级类的的字段 mlvl
     * @param regionId   区域ID
     * @param sort       排序 0 离我最近 1 好评优先 全部空
     * @param type       类型 1 找商铺页面 2寻服务页面
     * @param nationCode 城市ID
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ArrayList<ShopInfoBean> queryShopMulti(String brandId, String lat, String lgt, String mfid, String operateId,
                                                  String mlvl, String regionId, String sort, String type, String nationCode, String page, String page_length) throws WSError, JSONException;

    /**
     * 根据商场楼层获取店铺列表
     *
     * @param mid         商场id
     * @param floorId     楼层id  获取所有的时候传空
     * @param page        页码
     * @param page_length 页长
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public Shopdatalist get_floor_shops(String mid, String floorId, String page, String page_length)
            throws WSError, JSONException;

    /**
     * 找商铺--根据三级类别id获取相关的品牌
     *
     * @param moid
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ArrayList<BrandDate> getBrandToMoid(String moid, String page, String page_length)
            throws WSError, JSONException;

    /**
     * 看促销竞拍内页推荐位 活动 ER_INNER_PROMOTION 寻服务内页推荐位 活动 ER_INNER_SERVICE
     *
     * @param nationCode
     * @param tag
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ArrayList<ActivityDetailBean> getLookSeekAty(String page, String nationCode, String latstr, String logstr, String tag) throws WSError, JSONException;

    /**
     * 商铺/商场 新品上市
     *
     * @param targetId                  商场/商铺id
     * @param targetType新品上市(targetType 1商户,2店铺)
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ArrayList<ExChangeBean> getNewProduct(String targetId, String targetType, String page, String page_length)
            throws WSError, JSONException;

    /**
     * 获取商场或店铺的竞拍或兑换列表
     *
     * @param targetId    商场或店铺id
     * @param targetType  商场是1 店铺是2
     * @param productType 竞拍是1，兑换是2
     * @param page        页面
     * @param page_length 每页长度
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ArrayList<ExChangeBean> getJingpaiDuihuan(String id, String targetId, String targetType, String productType,
                                                     String page, String page_length) throws WSError, JSONException;

    /**
     * 获取商铺详情
     *
     * @param sid
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ShopDetailDataBean getShopDetail(String shopId) throws WSError, JSONException;

    /**
     * 提交头像
     *
     * @param couponBatchId
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public boolean get_UserCenter(String ava) throws WSError, JSONException;

    /**
     * 添加商铺关注
     *
     * @param tid 商铺ID
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public boolean add_shop(String tid) throws WSError, JSONException;

    /**
     * 获取店铺评论接口
     *
     * @param sid    店铺id
     * @param max_id
     * @param min_id
     * @param count  每页条数
     * @param type   (全部空，好0，中1，差2)
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public CommentData get_shopcomment_list(String sid, String max_id, String min_id, String count, String type)
            throws WSError, JSONException;


    /**
     * 重新绑定手机号
     *
     * @param mobile  手机号
     * @param smscode 验证码
     * @param type    状态  1绑定 2更换
     * @throws WSError
     * @throws JSONException
     */
    public boolean set_changmobile(String mobile, String smscode, String type)
            throws WSError, JSONException;


    /**
     * 版本跟新
     *
     * @param version_code 版本号
     * @throws WSError
     * @throws JSONException
     */
    public AppUpdata appUpdata(String version_code, String appmd5)
            throws WSError, JSONException;

    /**
     * 获取热搜关键词
     *
     * @param nationCode
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ArrayList<String> gethotkey(String nationCode)
            throws WSError, JSONException;

    /**
     * 获取用户地址
     *
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public UserInfoData get_user_address() throws WSError, JSONException;

    /**
     * 限时竞拍title
     *
     * @throws WSError
     * @throws JSONException
     */
    public TiemTitleData getPeriodTitle(String starttime, String endtime, String nationCode)
            throws WSError, JSONException;

    /**
     * 限时竞拍list
     *
     * @throws WSError
     * @throws JSONException
     */
    public ArrayList<FlashSaleListItemBean> getPeriodList(String productType, String auctionTime, String nationCode, String page, String page_length)
            throws WSError, JSONException;

    /**
     * 限时竞拍list
     *
     * @throws WSError
     * @throws JSONException
     */
    public FlashSaleDetailBean getPeriodDetail( String pid, String pt)
            throws WSError, Exception;

    /**
     * 竞拍记录
     *
     * @throws WSError
     * @throws JSONException
     */
    public List<AuctionBean> getAuction(String pid, String page, String page_length)
            throws WSError, Exception;

    /**
     * 竞拍
     *
     * @throws WSError
     * @throws JSONException
     */
    public AddPriceBean getAuctionCount(String apr, String pid)
            throws WSError, Exception;

    /**
     * 获取商家邀请列表
     *
     * @param page
     * @param page_length
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ArrayList<ShopInfoBean> getShopInviteList(String page, String page_length) throws WSError, JSONException;

    /**
     * 获取邀请页面信息
     *
     * @param sid 邀请店铺id
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ShopInviteDetailBean getShopInvitDetail(String sid) throws WSError, JSONException;

    /**
     * 填写邀请接口
     *
     * @param addShopInviteUserInfo
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public boolean AddShopInvite(AddShopInviteUserInfo addShopInviteUserInfo) throws WSError, JSONException;

    /**
     * 获取该商品的竞拍历史
     *
     * @param pid 商品id
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ArrayList<SaleHistoryBean> getSaleHistoryList(String pid) throws WSError, JSONException;

    /**
     * 获取热门推荐城市
     *
     * @param nationCode
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ArrayList<HotCityBean> getHotcitys(String nationCode) throws WSError, JSONException;

    /**
     * 提交用户行为记录
     *
     * @param type 1 商场 2 店铺
     * @param tid  商场ID 或者店铺ID
     * @param tag  1 详情浏览  2 来电次数
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public boolean putUserMessage(String type, String tid, String tag) throws WSError, JSONException;

    public Markbean CusttomMst(String rid, String mark) throws WSError, JSONException;

    public ArrayList<CityBean> upDb(String nation) throws WSError, JSONException;

    public ArrayList<CityBean> upCityLocation() throws WSError, JSONException;

    /**
     * 是否禁止生活圈提醒 0false 1true
     *
     * @param merchant 是否禁止商户消息提醒 0false 1true
     * @param system   是否禁止系统消息提醒 0false 1true
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public boolean MessAgeSet(String merchant, String system) throws WSError, JSONException;

    /**
     * @param nationCode 城市id
     * @param page       页码
     * @param lgt        经纬度
     * @param lat
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ArrayList<StoreDetailInfo> getStoreList(String nationCode, String page, String lgt, String lat) throws WSError, JSONException;

    /**
     * @param nationCode 城市id
     * @param prtag      参数值  RC_PRODUCT_GOOD
     * @param page       页码
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ArrayList<PoitGoodBean> getHomeSaleList(String nationCode, String prtag, String page) throws WSError, JSONException;

    public ArrayList<ShopInfoBean> getHomeCommendShop(String nationCode, String page) throws WSError, JSONException;

    /**
     * 新版获取大图详情接口u
     *
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public List<Adimg> getNewHomeBanner(String nationCode) throws WSError, JSONException;

    /**
     * 获取评论详情
     *
     * @param id
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public CommentInfo getCommentDetail(String id) throws WSError, JSONException;

    /**
     * 获取领券中心的类别
     *
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ArrayList<CityBean> getCouponTypeList(String nationCode) throws WSError, JSONException;

    /**
     * 周边商铺类别
     *
     * @param nationCode
     * @return
     * @throws WSError
     * @throws JSONException
     */
    public ArrayList<MyArrayListCityBean> getArroutTypeList(String nationCode) throws WSError, JSONException;

    public ArrayList<MyArrayListCityBean> getSaleTypeList(String nationCode) throws WSError, JSONException;

    public ArrayList<ShopInfoBean> getArountHotdShop(String nationCode, String page, String tag) throws WSError, JSONException;
}
