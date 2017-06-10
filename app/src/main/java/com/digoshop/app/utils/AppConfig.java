package com.digoshop.app.utils;

public class AppConfig {

    //drawable 图片修改
    //ic_launcher 改名
    //login_logo 改名
    //kcx_001  改名
    //manifest修改
    //修改application名字
    // 正大鸿泰   android:label="@string/app_nametc"
    //迪购  android:label="@string/app_namedigo"
    //WelcomeActivity
    // android:theme="@style/SplashThemedigo"


    //阿里云图片上传基础参数

    public static final String accessKeyId = "grPghY7hpHuc9PA2";
    public static final String accessKeySecret = "wI0aMyLcP9eZUEPEWJrNmjOZNkbGb8";
//    ////    // 测试用阿里云图片
//    public static final String testBucket = "zksxapp";
//    // 测试用
//    public static final String pj = "http://img.digoshop.com/";
//    //  测试用
//    public static final String endpoint = "img.digoshop.com";
//    //  测试用api
//    public static final String HOST = "http://digo.digoshop.com/";


//      // 上线用用阿里云图片
    public static final String testBucket = "digoimg";
    //    上线用
    public static final String endpoint = "image.digoshop.com";
    //    上线用
    public static final String pj = "http://image.digoshop.com/";
    //    上线用api
    public static final String HOST = "http://api.digoshop.com/";

    //铜川版
//    public static final String HOST = "http://tcapi.digoshop.com/";
//    public static final String endpoint = "tcimg.digoshop.com";
//    public static final String accessKeyId = "LTAI5rDtS6aDUXhM";
//    public static final String accessKeySecret = "EBDfSMpbZb99KqJJcP3wfWQAlyDe99";
//    public static final String testBucket = "digoapp";
//    public static final String pj = "http://tcimg.digoshop.com/";
//    public static final int iscity = 0;//0为全国 1，为铜川


    //铜川版
//	public static final int iscity=1;//0为全国 1，为铜川
//	public static final String HOST = "http://tcapi.digoshop.com/";
//	public static final String endpoint = "tcimg.digoshop.com";
//	public static final String accessKeyId = "LTAI5rDtS6aDUXhM";
//	public static final String accessKeySecret = "EBDfSMpbZb99KqJJcP3wfWQAlyDe99";
//	public static final String testBucket = "digoapp";
//    public static final String pj = "http://tcimg.digoshop.com/";


    // 设备类型，(0-phone,1-pad,2-ott)
    public static int phonetype = 1;
    public static final String RESULT_SUCCESS = "Success";
    public static final boolean IS_PRINT_LOG = true;
    public static final int timeint = 10;
    public static final int HTTP_TIMEOUT_CONNECTION = timeint * 1000;
    public static final int HTTP_SOTIMOUT = (timeint + 5) * 1000;
    //shareprer 文件名字
    public static String basefile = "basefilename";
    //只存贮设备注册的信息
    public static String basedevicefile = "basedevicefilename";
    // 登录/注册返回token
    public static String usertoken = "";
    // 登录/注册返回uid
    public static String userid = "";
    //app签名密钥
    public static String appkey = "561f89ea5004ba3fa95b8a888fddae73";
    public static String isusertype = "false";
    public static String ipstr="";
    public static String channel = "android_user";


    public static final String ISPHONEALIVE=HOST +"communityunion/passport/exist.json?";

    /*
     * 设备信息注册
     */
    public static final String REGISTER_BY_DEVICE = HOST + "communityunion/passport/register_by_device.json?";
    /*
     * 发送短信验证码
     */
    public static final String SENDSMS = HOST + "communityunion/passport/sendsms.json?";
    /*
     * 根据城市id获取首页-限时竞拍，推荐活动两个优惠券，推荐产品店铺列表，推荐服务店铺列表
     */
    public static final String QUERYRECOMMENDMERCHANT = HOST + "communityunion/business/queryRecommendMerchant.json?";
    /*
     * 获取商场或商铺的活动列表
     */
    public static final String STOREATYLISTORSHOPATYLIST = HOST + "communityunion/business/news_by_tag.json?";
    /*
     * 获取商场或商铺的优惠券
     */
    public static final String STOREATYLISTORSHOPCOUPONLIST = HOST + "communityunion/coupon/coupons_by_tag.json?";
    /*
     * 获取首页
     */
    public static final String QUERYRECOMMENDSHOP = HOST + "communityunion/business/queryRecommendShop.json?";
    /*
     * 手机验证码登录
     */
    public static final String LOGIN_SMS = HOST + "communityunion/passport/login_sms.json?";
    /*
     * 验证短信码
     */
    public static final String VERIFYSMSCODE = HOST + "communityunion/passport/verifysmscode.json?";
    /*
     * 用户注册
     */
    public static final String REGISTER = HOST + "communityunion/passport/register.json?";
    /*
     * 用户登录
     */
    public static final String LOGIN = HOST + "communityunion/passport/login.json?";
    /*
     * 第三方登录
     */
    public static final String LOGIN_THIRD = HOST + "communityunion/passport/login_third.json?";
    /*
     * 重置密码 重置密码前需要先验证手机验证码 通过后再到此步骤
     */
    public static final String RESETPWD = HOST + "communityunion/passport/resetpwd.json?";
    /*
     * 使token保持存活
     */
    public static final String KEEPALIVE = HOST + "communityunion/passport/keepalive.json?";
    /*
     * 检查token
     */
    public static final String CHECKTOKEN = HOST + "communityunion/passport/checktoken.json?";
    /*
     * 变更用户信息
     */
    public static final String UPDATE_USER_INFO = HOST + "communityunion/passport/update_user_info.json?";
    /*
     * 变更用户信息
     */
    public static final String GET_USER = HOST + "communityunion/passport/get_user.json?";
    public static final String HOSTAR = "http://armanager.yoleedu.com/ylxy_project/";

    public static final String ACTCODE_ONE = HOSTAR + "YlIntFaceAct/actCodeone.yjson";
    public static final String ACTCODE_TWO = "http://192.168.1.21:8080/ylxy_project/YlIntFaceAct/actCodetwo.yjson";
    // public static final String ACTCODE_TWO =
    // "http://192.168.1.21:8080/ylxy_project/YlIntFaceAct/actCodetwo.yjson";
    public static final String APPVERSION_AR1 = "http://192.168.1.21:8080/ylxy_project/YlIntFaceVer/appVersion.yjson";
    public static final String VIDEON_DOWN = "http://192.168.1.21:8080/ylxy_project/YlIntFaceCouWare/couWare.yjson";
    /*
     * 获取商场详情
     */
    public static final String GETMERCHANTDETAIL = HOST + "communityunion/business/getMerchantDetail.json?";
    /*
 * 获取兑换列表 积分换购列表
 */
    public static final String EXCHANGE_LIST = HOST + "communityunion/product/queryProductList.json?";

    /*
     * 获取兑换商品详情
     */
    public static final String GET_EXCHANGE_DETAIL = HOST
            + "communityunion/product/queryProductDetail.json?";
    /*
     * 确认兑换
     */
    public static final String APPROVE_EXCHANGE = HOST + "communityunion/product/approveExchangeProduct.json?";
    /*
     * 获取周边商铺
     */
    public static final String AROUNT_SHOPS = HOST + "communityunion/business/queryAroundShops.json?";
    /*
     * 根据输入关键字搜索店铺
     */
    public static final String KEYWORDSERACH_SHOPS = HOST + "communityunion/business/search.json?";
    /*
     * 根据城市id查询活动列表
     */
    public static final String QUERYNEWSLIST = HOST + "communityunion/business/queryNewsList.json?";
    /*
     * 活动详情接口
     */
    public static final String ACTIVITY_DETIAL = HOST + "communityunion/business/getNewById.json?";
    /*
     * 获取用户消费记录
     */
    public static final String PAD_LIST = HOST + "communityunion/pay/user_get.json?";
    /*
     * 获取我的积分列表
     */
    public static final String POINTS = HOST + "communityunion/user/user_intg.json?";
    /*
     * 我的定制列表
     */
    public static final String GET_CUSTSERVICE_LISTA = HOST + "communityunion/custservice/queryCustServiceList.json?";
    /*
     * 获取定制信息详情
     */
    public static final String GET_CUSTSERVICE_CONTENT = HOST
            + "communityunion/custservice/queryCustServiceInfoAndReplyList.json?";
    /*
     * 我的关注列表
     */
    public static final String MYLIKES = HOST + "communityunion/relation/user_get.json?";

    /*
     * 我的优惠券列表
     */
    public static final String DISCOUNTCOUPONS = HOST + "communityunion/coupon/u_coupons.json?";
    /*
     * 获取定制商品和服务一级二级类别借口
     */
    public static final String GET_MANAGING_TYPE_LIST = HOST + "communityunion/business/queryCategory.json?";
    /*
     * 创建我的定制服务/商品采购
     */
    public static final String CREATE_MY_SERVICE = HOST + "communityunion/custservice/createCustService.json?";
    /*
     * 根据城市id获取业态商圈
     */
    public static final String GETMERCHANTBYNATIONCODE = HOST + "communityunion/business/getMerchantByNationCode.json";
    /*
     * 根据app所传的城市id获取优惠券列表
     */
    public static final String GET_NATION_COUPON_LIST = HOST + "communityunion/coupon/getCouponList.json?";
    public static final String GET_NATION_COUPON_LISTJINGXUAN = HOST + "communityunion/coupon/rec_coupons.json?";

    /*
     * 获取用户统计信息
     */
    public static final String GET_STAT = HOST + "communityunion/user/get_stat.json?";
    /*
     * 获取我的消息列表
     */
    public static final String MESSAGE_LIST = HOST + "communityunion/notice/history.json?";
    /*
     * 获取未读消息数量
     */
    public static final String NO_READMESS = HOST + "communityunion/notice/real_time.json?";
    /*
     * 更新消息状态
     */
    public static final String UPDATE_SYS = HOST + "communityunion/notice/update_notice.json?";
    /*
     * 通知服务器用户已查看消息
     */
    public static final String FLUSH = HOST + "communityunion/notice/flush.json?";
    /*
     * 删除消息
     */
    public static final String DEL_SYS = HOST + "communityunion/notice/del_notice.json?";
    /*
     * 获取评论列表
     */
    public static final String GET_COMMENT_LIST = HOST + "communityunion/merchant_comment/user_list.json?";
    /*
     * 获取我的礼包
     */
    public static final String GET_MY_BAG = HOST + "communityunion/user/getUserGiftByUId.json?";
    /*
     * 获取我的卡包
     */
    public static final String GET_MY_CARD_BAG = HOST + "communityunion/relation/user_vips.json?";
    /*
     * 提交评论
     */
    public static final String SUBMIT_COMMENT = HOST + "communityunion/merchant_comment/post.json?";
    /*
     * 会员卡详情
     */
    public static final String VIP_DETAIL = HOST + "communityunion/pay/user_cost.json?";
    /*
     * 解绑会员卡
     */
    public static final String UN_BIND_VIP = HOST + "communityunion/relation/unbind.json?";
    /*
     * 根据城市id获取优惠券列表
     */
    public static final String GETNATION_COUPONLIST = HOST + "communityunion/coupon/getCouponList.json?";
    /*
     * 用户反馈
     */
    public static final String FEEDBACK = HOST + "communityunion/feedback/post.json?";
    /*
     * 用户签到
     */
    public static final String SIGN_IN = HOST + "communityunion/user/sign.json?";
    /*
     * 获取优惠券详情
     */
    public static final String COUPON_DETAIL = HOST + "communityunion/coupon/coupon_b_info.json?";
    /*
     * 领取优惠群
     */
    public static final String GET_COUPON = HOST + "communityunion/coupon/getCoupon.json?";
    /*
     * 热门关键词搜索
     */
    public static final String GET_SEARCHHOT = HOST + "communityunion/business/hotkey.json?";
    /*
     * 多条件搜索接口
     */
    public static final String QUERYSHOPMYLTI = HOST + "communityunion/business/queryShopMulti.json?";
    /*
     * 找商铺--根据三级类别id搜索品牌
     */
    public static final String BRANDTOMOID = HOST + "communityunion/business/query_brands.json?";
    /*
     * 看促销竞拍内页推荐位 活动 ER_INNER_PROMOTION 寻服务内页推荐位 活动 ER_INNER_SERVICE
     * 够优惠优惠活动列表中的推荐位ER_INNER_RNEWS
     */
    public static final String QUERY_LOOK_SEEK_ATY = HOST + "communityunion/business/query_r_news.json?";
    /*
     * 新品上市(targetType 1商户,2店铺) 商品没有详情页
     */
    public static final String GETPRODUCTNEWSLIST = HOST + "communityunion/product/getProductList.json?";
    /*
     * 获取商场或店铺的竞拍或兑换列表
     */
    public static final String GETSTORESHOPPRODUCTLIST = HOST + "communityunion/product/getProductList.json?";
    /*
     * 店铺详情接口
     */
    public static final String GETSHOPDETAIL = HOST + "communityunion/business/getShopById.json?";
    /*
     * 提交头像
     */
    public static final String GET_USERCENTER = HOST + "communityunion/passport/user_ava.json?";
    /*
     * 添加商铺关注
     */
    public static final String ADD_SHOP = HOST + "communityunion/relation/add.json?";
    /*
     * 取消商铺关注
     */
    public static final String CANCEL_SHOP = HOST + "communityunion/relation/cancel.json?";
    /*
     * 获取店铺评论列表
     */
    public static final String SHOP_COMMENTLIST = HOST + "communityunion/merchant_comment/merchant_list.json?";
    /*
     * 根据商场楼层获取店铺列表
     */
    public static final String GET_STOREFLOOR_SHOPS = HOST + "communityunion/business/getShopsByFloorId.json?";

    /*
     * 重置手机号
	 */
    public static final String SET_CHANGMOBILE = HOST + "communityunion/passport/changmobile.json";

    /*
     * 跟新版本
     */
    public static final String SET_AAPPUPDATE = HOST + "communityunion/setting/version.json?";
    /*
         热门推荐 热搜
        */
    public static final String GET_HOT_KEYS = HOST + "communityunion/business/hotkey.json?";

    /**
     * 获取用户的地址
     */
    public static final String GET_USER_ADDRESS = HOST + "communityunion/passport/user_contact_info.json?";
    /*
    *限时竞拍title
    */
    public static final String SET_PERIODTITLE = HOST + "communityunion/product/queryAuctionPeriodList.json?";

    /*
     *限时竞拍list
     */
    public static final String SET_PERIODLIST = HOST + "communityunion/product/queryProductList.json?";

    /*
     *限时竞拍详细
     */
    public static final String SET_PERIODDETAIL = HOST + "communityunion/product/queryProductDetail.json?";
    /*
     *竞拍记录
     */
    public static final String GET_PERIODRECORD = HOST + "communityunion/product/getAuctionLogListByPId.json?";

    /*
     *竞拍加价
     */
    public static final String GET_AUCTION = HOST + "communityunion/product/auction.json?";


    /*
     *获取商家邀请列表
     */
    public static final String SHOPINVITELISTS = HOST + "communityunion/relation/user_list.json?";

    /*
     *获取邀请页面信息详情
     */
    public static final String SHOPINVITE_LISTS_DETAIL = HOST + "communityunion/passport/vip_info.json?";

    /*
     *提交商家邀请
     */
    public static final String SHOPINVITE_COMMIT = HOST + "communityunion/relation/add_vip.json?";

    /**
     * 获取该商品的积分换购历史
     */
    public static final String SALE_HISTORY = HOST + "communityunion/product/queryExchangeLogListByPId.json?";

    /**
     * 获取推荐热门城市
     */
    public static final String GET_HOT_CITYS = HOST + "communityunion/nation/hot.json?";
    /**
     * 记录用户点击行为
     */
    public static final String PUT_USER_MESSAGE = HOST + "communityunion/business/report.json?";
    /**
     * 标记定制回复商家
     */
    public static final String CS_MST = HOST + "communityunion/custservice/updateCustomReplyMarkStatus.json?";
    /**
     * 更新开启的城市id
     */
    public static final String UPDB = HOST + "communityunion/nation/queryByCode.json?";
    /*
     * 消息设置
     */
    public static final String SETMESSAGE = HOST + "communityunion/user/update.json?";
    //获取开启和关闭的城市id
    public static final String LOCATION_NATION = HOST + "communityunion/nation/location.json?";
    /*
     *商场列表
     */
    public static final String LOOKSTORELIST = HOST + "communityunion/business/getMerchants.json?";
    /**
     * 首页推荐商品列表
     */
    public static final String HOMESALELIST = HOST + "communityunion/product/qr_product.json?";
    /**
     * 首页推荐店铺列表
     */
    public static final String HOMECOMMDEDLIST = HOST + "communityunion/business/qr_shop.json?";
    /**
     * 新版首页滚动大图
     */
    public static final String NEWHOMEBANNER = HOST + "communityunion/business/qr_banner.json?";
    /**
     * 评论详情
     */
    public static final String COMMENTDETAIL = HOST + "communityunion/merchant_comment/q_comment.json?";

    /**
     * 使用定制服务人数
     */
    public static final String CUSTOMNUM = HOST + "communityunion/custservice/queryCustServiceCount.json?";
    /**
     * 获取类别接口
     */
    public static final String GETTYPELIST = HOST + "communityunion/business/navigation.json?";
    /**
     * 获取店铺详情
     */
    public static final String GETSHOPDETAILNEW = HOST + "communityunion/business/getShopById.json?";
    /**
     * 店铺商品
     */
    public static final String GETSHOPNPRODUCT = HOST + "communityunion/product/productList.json?";
    /**
     * 店铺环境
     */
    public static final String GETSHOPENV = HOST + "communityunion/business/shop_album.json?";
    /**
     * 店铺产品分类
     */
    public static final String SHOPCATEGORY = HOST + "communityunion/business/categorys.json?";

    /**
     * 新版本商品详情
     */
    public static final String PRODUCTDETAILNEWW = HOST + "communityunion/product/queryProductDetails.json?";

}
