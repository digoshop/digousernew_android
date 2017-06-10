package com.digoshop.app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.module.home.model.ShowInfo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 二维码生成工具类
 */
public class QRCodeUtil {
    /**
     * 生成二维码Bitmap
     *
     * @param content   内容
     * @param widthPix  图片宽度
     * @param heightPix 图片高度
     * @param logoBm    二维码中心的Logo图标（可以为null）
     * @param filePath  用于存储二维码图片的文件路径
     * @return 生成二维码及保存文件是否成功
     */
    public static boolean createQRImage(String content, int widthPix,
                                        int heightPix, Bitmap logoBm, String filePath) {
        try {
            if (content == null || "".equals(content)) {
                return false;
            }

            // 配置参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            // 设置空白边距的宽度
            hints.put(EncodeHintType.MARGIN, 0); // default is 4

            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content,
                    BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
            int[] pixels = new int[widthPix * heightPix];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < widthPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = 0xff000000;
                    } else {
                        pixels[y * widthPix + x] = 0xffffffff;
                    }
                }
            }

            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);

            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm);
            }

            // 必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            return bitmap != null
                    && bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream(filePath));
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 在二维码中间添加Logo图案
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        // 获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        // logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight,
                Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2,
                    (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }

    /**
     * 生成二维码Bitmap
     *
     * @param content  内容
     * @param width    图片宽度
     * @param height   图片高度
     * @param logoBm   二维码中心的Logo图标（可以为null）
     * @param filePath 用于存储二维码图片的文件路径
     * @return 生成二维码及保存文件是否成功
     */
    public static boolean createShareToWeiXinQRImage(String content, int width,
                                                     int height, Bitmap logoBm, Bitmap productImg, String filePath) {
        try {

            int widthPix = 500;
            int heightPix = 500;
            if (content == null || "".equals(content)) {
                return false;
            }

            // 配置参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            // 设置空白边距的宽度
            // hints.put(EncodeHintType.MARGIN, 2); //default is 4

            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content,
                    BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
            int[] pixels = new int[widthPix * heightPix];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < widthPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = 0xff000000;
                    } else {
                        pixels[y * widthPix + x] = 0xffffffff;
                    }
                }
            }

            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);

            if (logoBm != null) {
                bitmap = addZhiwen(bitmap, logoBm, productImg, width, height);
            }

            // 必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            return bitmap != null
                    && bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream(filePath));
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 在二维码中间添加Logo图案
     *
     * @param height
     * @param width
     */
    private static Bitmap addZhiwen(Bitmap qrImg, Bitmap logo, Bitmap prodImg,
                                    int width, int height) {
        if (qrImg == null) {
            return null;
        }

        if (logo == null) {
            return qrImg;
        }

        // 获取图片的宽高
        int srcWidth = qrImg.getWidth();
        int srcHeight = qrImg.getHeight();

        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        int prodWidth = prodImg.getWidth();
        int prodHeight = prodImg.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return qrImg;
        }

        // logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        float prodScaleFactor = width * 3.0f / 4 / prodHeight;

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(prodImg, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2,
                    (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }

    /**
     * 生成二维码Bitmap
     *
     * @param content   内容
     * @param widthPix  图片宽度
     * @param heightPix 图片高度
     * @param logoBm    二维码中心的Logo图标（可以为null）
     * @return 生成二维码及保存文件是否成功
     */
    public static Bitmap createQRImageBitmap(String content, int widthPix,
                                             int heightPix, Bitmap logoBm) {
        try {
            if (content == null || "".equals(content)) {
                return null;
            }

            // 配置参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            // 设置空白边距的宽度
            hints.put(EncodeHintType.MARGIN, 0); // default is 4

            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix;
            bitMatrix = new QRCodeWriter().encode(content,
                    BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
            int[] pixels = new int[widthPix * heightPix];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < widthPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = 0xff000000;
                    } else {
                        pixels[y * widthPix + x] = 0xffffffff;
                    }
                }
            }

            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);

            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm);
            }

            // 必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            // return bitmap != null &&
            // bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new
            // FileOutputStream(filePath));
            return bitmap;
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("NewApi")
    public static boolean createShareWechatImg(Context context, ShowInfo showInfo, String filePath) {
        int width = showInfo.getWidth();
        int height = showInfo.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        if (null != showInfo) {
            String logoText = showInfo.getLogoName();
            String storeName = showInfo.getStoreName();
            String name = showInfo.getUserName();
            String hint = showInfo.getWeChatHint();
            String prodName = showInfo.getProdName();
            String prodPrice = showInfo.getProdPrice();
            String prodActivityPrice = showInfo.getProdActivityPrice();
            String qrcodeHint = showInfo.getQrCodeHint();

            // 创建画笔 绘制大背景
            Paint paint = new Paint();
            paint.setColor(context.getResources().getColor(R.color.qianhui));
            canvas.drawRect(0, 0, width, height, paint);
            //绘制头部红色背景
            paint.setColor(Color.RED);// 设置红色
            // 红色矩形 其高度为屏幕高度的八分之一
            float rectHeight = (float) (height / 8.0);
            // 画红色矩形
            canvas.drawRect(0, 0, width, rectHeight, paint);
            //准备绘制setLogoName
            paint.setTextSize(100);
            // 不能设置这个，设置这个需要从计算字体中心坐标
            // paint.setTextAlign(Paint.Align.CENTER);
            paint.setColor(Color.WHITE);
            FontMetrics fontMetrics = paint.getFontMetrics();// 计算文字高度
            //rectHeight红色矩形高度
            Log.v("ceshi", " fontMetrics.bottom+" + fontMetrics.bottom);
            float baseline = (rectHeight - fontMetrics.bottom - fontMetrics.top) / 2;
            float logoTextMarginLeft = 50;
            // 其中的y坐标不是字体左上角的坐标而是字体的基线y坐标
            canvas.drawText(logoText, logoTextMarginLeft, baseline, paint);
            //logoText所占的宽度
            float logoTextWidth = paint.measureText(logoText);
            float lineX = logoTextMarginLeft * 2 + logoTextWidth;
            float lineY = baseline + fontMetrics.ascent;
            float lineB = baseline + fontMetrics.descent;
            //线的宽度
            paint.setStrokeWidth(5);
            // 画分割线 (float startX, float startY, float stopX, float stopY, Paint paint)
            canvas.drawLine(lineX, lineY, lineX, lineB, paint);
            //绘制setStoreName
            paint.setTextSize(70);
            // paint.setTextAlign(Align.CENTER);
            paint.setColor(Color.WHITE);
            FontMetrics storeFontMetrics = paint.getFontMetrics();// 计算文字高度
            float storeBaseline = (rectHeight - storeFontMetrics.bottom - storeFontMetrics.top) / 2;
            // float fontHeight1 = fontMetrics1.descent -
            // fontMetrics1.ascent;//计算文字baseline
            float storeWidth = paint.measureText(storeName);

            float storeX = (width - logoTextMarginLeft * 2 - logoTextWidth - 5 - storeWidth)
                    / 2 + logoTextMarginLeft * 2 + logoTextWidth + 5;
            // 画店名称字样
            canvas.drawText(storeName, storeX, storeBaseline, paint);
            //准备绘制setUserName，rectHeight红色背景的高度
            paint.setColor(Color.RED);// 设置红色
            paint.setTextSize(50);
            float userMarginTopBottom = 25;
            float userMarginLeft = 50;
            FontMetrics userFontMetrics = paint.getFontMetrics();
            float userFontHeight = userFontMetrics.descent
                    - userFontMetrics.ascent;
            canvas.drawText(name, userMarginLeft, rectHeight
                    + userMarginTopBottom + userFontHeight, paint);// 画文本
            //准备绘制setWeChatHint
            //nameWidth user的宽度
            float nameWidth = paint.measureText(name);
            paint.setColor(Color.BLACK);
            canvas.drawText(hint, nameWidth + userMarginLeft * 2, rectHeight
                    + userMarginTopBottom + userFontHeight, paint);
            //准备绘图片
            Bitmap bg = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.linwel);
            // FontMetrics fontMetrics2 = paint.getFontMetrics();//计算文字高度
            // float fontHeight2 = fontMetrics2.descent -
            // fontMetrics2.ascent;//计算文字baseline
            int prodImgX = 0;
            int prodImgY = (int) (rectHeight + 2 * userMarginTopBottom + userFontHeight);
            int prodImgRight = width;
            int prodImgBottom = prodImgY + height / 2;
            //int left, int top, int right, int bottom..左上角的位置是（0，0）
            Rect rect = new Rect(prodImgX, prodImgY, prodImgRight,
                    prodImgBottom);
            //(Bitmap bitmap, Rect src, Rect dst, Paint paint
            //src要绘制的bitmap 区域  dst要将bitmap 绘制在屏幕的什么地方
            canvas.drawBitmap(bg, null, rect, null);
            //绘制setProdName
            canvas.drawText(prodName, 0 + userMarginLeft, prodImgBottom
                    + userMarginTopBottom + userFontHeight, paint);
            //绘制现价
            paint.setColor(Color.RED);
            canvas.drawText(prodActivityPrice, 0 + userMarginLeft,
                    prodImgBottom + userMarginTopBottom + userFontHeight
                            + userFontHeight, paint);
            //绘制原价
            paint.setColor(Color.GRAY);
            int flag = paint.getFlags();
            paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            float priceWidth = paint.measureText(prodActivityPrice);
            canvas.drawText(prodPrice, 0 + userMarginLeft + userMarginLeft
                    + priceWidth, prodImgBottom + userMarginTopBottom + 2
                    * userFontHeight, paint);
            //准备绘制二维码
            float qrCodeMargin = 50;
            // 画蓝色矩形
            paint.setColor(context.getResources().getColor(R.color.title_bar_bg));
            float bottomRectLeft = 0;
            float bottomRectRight = width;
            float bottomRectTop = prodImgBottom + 2 * userMarginTopBottom + 2
                    * userFontHeight + qrCodeMargin;
            float bottomRectBottom = height;
            canvas.drawRect(bottomRectLeft, bottomRectTop, bottomRectRight,
                    bottomRectBottom, paint);
            //准备绘制二维码
            float LeftHeight = height - bottomRectTop;
            float qrWidthHeight = LeftHeight - 2 * qrCodeMargin;
            Bitmap logoImg = BitmapFactory.decodeResource(
                    context.getResources(), R.drawable.ic_launcher);
            Bitmap qrCodeBitmap = createQRImageBitmap(showInfo.getProdUrl(),
                    (int) qrWidthHeight, (int) qrWidthHeight, logoImg);
            int qrCodeImgLeft = (int) qrCodeMargin;
            int qrCodeImgRight = (int) (qrWidthHeight + qrCodeMargin);
            int qrCodeImgTop = (int) (bottomRectTop + qrCodeMargin);
            int qrCodeImgBottom = (int) (qrCodeImgTop + qrWidthHeight);
            Rect rect1 = new Rect(qrCodeImgLeft, qrCodeImgTop, qrCodeImgRight,
                    qrCodeImgBottom);
            canvas.drawBitmap(qrCodeBitmap, null, rect1, null);

            Bitmap fingerprint = BitmapFactory.decodeResource(
                    context.getResources(), R.drawable.fingerprint);
            paint.setColor(Color.BLACK);
            paint.setFlags(flag);

            float qrHintWidth = paint.measureText("长按识别" + qrcodeHint);
            // float testLeft = qrCodeImgRight + qrCodeMargin;
            // float testTop = qrCodeImgTop;
            // float testRight = width - qrCodeMargin;
            // float testBottom = qrCodeImgBottom;
            // canvas.drawRect(testLeft, testTop, testRight, testBottom, paint);

            int fingerLeft = (int) (qrCodeImgRight + qrCodeMargin + (width
                    - qrCodeMargin - qrCodeImgRight - qrCodeMargin
                    - qrWidthHeight + userFontHeight) / 2);
            int fingerRight = (int) (fingerLeft + qrWidthHeight - userFontHeight);
            int fingerTop = qrCodeImgTop;
            int fingerBottom = (int) (qrCodeImgBottom - userFontHeight);

            // int fingerLeft = (int) (fingerRight - qrWidthHeight +
            // userFontHeight);
            // int fingerRight = (int) (width - qrCodeMargin);
            // int fingerTop = qrCodeImgTop;
            // int fingerBottom = (int) (qrCodeImgBottom - userFontHeight)
            Rect fingerRect = new Rect(fingerLeft, fingerTop, fingerRight,
                    fingerBottom);

            canvas.drawBitmap(fingerprint, null, fingerRect, null);

            paint.setColor(Color.RED);
            float fingerHintX = qrCodeImgRight
                    + qrCodeMargin
                    + (width - qrCodeMargin - qrCodeImgRight - qrCodeMargin - qrHintWidth)
                    / 2;
            float fingerHintY = fingerBottom + userFontHeight;
            canvas.drawText("长按识别", fingerHintX, fingerHintY, paint);

            paint.setColor(Color.BLACK);
            canvas.drawText(qrcodeHint,
                    fingerHintX + paint.measureText("长按识别"), fingerHintY, paint);

        }
        try {
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
//			Resources res = context.getResources();
//			img.setBackground(new BitmapDrawable(res, bitmap));
//			
            //若参数设置为Bitmap.CompressFormat.JPEG会出现背景为黑色的图片
            return bitmap != null
                    && bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream(filePath));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // img.setImageBitmap(bitmap);
        return false;
    }

    // 文件存储根目录
    public static String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }

        return context.getFilesDir().getAbsolutePath();
    }

    @SuppressLint("NewApi")
    public static boolean createShareShopImg(Context context, ShowInfo showInfo, String filePath) {
        int width = showInfo.getWidth();
        int height = showInfo.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        if (null != showInfo) {
            String digoslogan = showInfo.getDigoslogan();
            String shopname = showInfo.getShopname();
            String shoptype = showInfo.getShoptype();
            String shopintroduce = showInfo.getShopintroduce();
            String shoptime = showInfo.getShoptime();
            String shoptel = showInfo.getShoptel();
            String shopaddress = showInfo.getShopaddress();
            String qrcodeHint = showInfo.getQrcodecontent();

            // 创建画笔 绘制大背景
            Paint paint = new Paint();
            paint.setColor(context.getResources().getColor(R.color.qianhui));
            canvas.drawRect(0, 0, width, height, paint);
            //准备绘制icon
            //准备绘图片
            Bitmap iconbg = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ic_launcher);
            int iconleft = context.getResources().getDimensionPixelOffset(R.dimen.base_dimen_240);
            int icontop = context.getResources().getDimensionPixelOffset(R.dimen.base_dimen_50);
            int iconrignt = iconleft + context.getResources().getDimensionPixelOffset(R.dimen.base_dimen_120);
            int iconbottom = icontop + context.getResources().getDimensionPixelOffset(R.dimen.base_dimen_120);
            //int left, int top, int right, int bottom..左上角的位置是（0，0）
            Rect rect = new Rect(iconleft, icontop, iconrignt,
                    iconbottom);
            //(Bitmap bitmap, Rect src, Rect dst, Paint paint//src要绘制的bitmap 区域  dst要将bitmap 绘制在屏幕的什么地方
            canvas.drawBitmap(iconbg, null, rect, null);
            canvas.save();
            //准备绘制广告语
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(context.getResources().getColor(R.color.qinahei));
            textPaint.setTextSize(context.getResources().getDimensionPixelOffset(R.dimen.base_dimen_33));
            textPaint.setAntiAlias(true);
            StaticLayout layout = new StaticLayout(digoslogan, textPaint, context.getResources().getDimensionPixelOffset(R.dimen.base_dimen_170), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
            // 这里的参数300，表示字符串的长度，当满10时，就会换行，也可以使用“\r\n”来实现换行
            canvas.translate(iconrignt + context.getResources().getDimensionPixelOffset(R.dimen.base_dimen_20), context.getResources().getDimensionPixelOffset(R.dimen.base_dimen_65));//从100，100开始画
            layout.draw(canvas);
            canvas.restore();

            Bitmap shareshopimg = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shareshopbg);


            int shareshopleft = context.getResources().getDimensionPixelOffset(R.dimen.base_dimen_24);
            int shareshoptop = iconbottom + context.getResources().getDimensionPixelOffset(R.dimen.base_dimen_38);
            int shaeshopright = width - shareshopleft;
            int shaershopbottom = shareshoptop + shaeshopright * 2 / 3;
            Log.v("ceshi", "width+" + width);
            Log.v("ceshi", "shareshopleft+" + shareshopleft);
            Log.v("ceshi", "height+" + height);
            Rect rectshareshop = new Rect(shareshopleft, shareshoptop, shaeshopright,
                    shaershopbottom);
            canvas.drawBitmap(shareshopimg, null, rectshareshop, null);
            //准备绘制店名
//            paint.setColor(context.getResources().getColor(R.color.black_text));// 设置红色
//            paint.setTextSize(context.getResources().getDimensionPixelOffset(R.dimen.base_dimen_33));
//            float userMarginTopBottom = 25;
//            float userMarginLeft = 50;
//            FontMetrics shopnameFontMetrics = paint.getFontMetrics();
//            float userFontHeight = shopnameFontMetrics.descent
//                    - shopnameFontMetrics.ascent;
//            canvas.drawText(shopname, userMarginLeft, rectHeight
//                    + userMarginTopBottom + userFontHeight, paint);// 画文本
        }
        try {
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
            //若参数设置为Bitmap.CompressFormat.JPEG会出现背景为黑色的图片
            return bitmap != null
                    && bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream(filePath));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // img.setImageBitmap(bitmap);
        return false;
    }

    @SuppressLint("NewApi")
    public static boolean createShareShopImgNew(Context context, ShowInfo showInfo, String filePath, Bitmap bitmap) {
        Activity aty = (Activity) context;
        View view = aty.getLayoutInflater().inflate(R.layout.share_shop_bg, null);
        ImageView iv_share_shop_bg = (ImageView) view.findViewById(R.id.iv_share_shop_bg);
        LinearLayout lin_share_shop_type = (LinearLayout) view.findViewById(R.id.lin_share_shop_type);
        TextView tv_share_shop_name = (TextView) view.findViewById(R.id.tv_share_shop_name);
        TextView tv_share_shop_text = (TextView) view.findViewById(R.id.tv_share_shop_text);
        TextView tv_share_shop_intrduce = (TextView) view.findViewById(R.id.tv_share_shop_intrduce);
        TextView tv_share_shop_time = (TextView) view.findViewById(R.id.tv_share_shop_time);
        TextView tv_share_shop_tel = (TextView) view.findViewById(R.id.tv_share_shop_tel);
        TextView tv_share_shop_address = (TextView) view.findViewById(R.id.tv_share_shop_address);
        ImageView iv_share_shop_qrcode = (ImageView) view.findViewById(R.id.iv_share_shop_qrcode);
        LinearLayout ll_parent = (LinearLayout) view.findViewById(R.id.ll_parent);
        DisplayMetrics dm = new DisplayMetrics();
        aty.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeigth = dm.heightPixels;


        //  int heightiv = screenWidth * 2 / 3;

        int widthshopimg = screenWidth - 2 * context.getResources().getDimensionPixelOffset(R.dimen.base_dimen_24);
       // int heightshopimg = widthshopimg * 7 / 15;
        int heightshopimg = widthshopimg * 2 / 3;
        ViewGroup.LayoutParams params = iv_share_shop_bg.getLayoutParams();
        params.height = heightshopimg;
        params.width = widthshopimg;
        iv_share_shop_bg.setLayoutParams(params);


        if (null != showInfo) {
            String shopname = showInfo.getShopname();
            String shoptype = showInfo.getShoptype();
            String shopintroduce = showInfo.getShopintroduce();
            String shoptime = showInfo.getShoptime();
            String shoptel = showInfo.getShoptel();
            String shopaddress = showInfo.getShopaddress();
            String qrcodecontent = showInfo.getQrcodecontent();
            iv_share_shop_bg.setImageBitmap(bitmap);
            tv_share_shop_name.setText(shopname);
            if (TextUtils.isEmpty(shoptype)) {
                lin_share_shop_type.setVisibility(View.GONE);
            } else {
                lin_share_shop_type.setVisibility(View.VISIBLE);
                if ("签约".equals(shoptype)) {
                    lin_share_shop_type.setBackgroundResource(R.drawable.a_shoptype_qinayue);
                } else {
                    lin_share_shop_type.setBackgroundResource(R.drawable.a_shoptype_renzheng);
                }
                tv_share_shop_text.setText(shoptype);
            }
            Log.v("ceshi","shopintroduce+"+shopintroduce);
            tv_share_shop_intrduce.setText(shopintroduce);
            tv_share_shop_time.setText(shoptime);
            tv_share_shop_tel.setText(shoptel);
            tv_share_shop_address.setText(shopaddress);
            Bitmap logoImg = BitmapFactory.decodeResource(
                    context.getResources(), R.drawable.ic_launcher);
            Bitmap qrCodeBitmap = createQRImageBitmap(qrcodecontent,
                    context.getResources().getDimensionPixelOffset(R.dimen.base_dimen_180), context.getResources().getDimensionPixelOffset(R.dimen.base_dimen_180), logoImg);
            iv_share_shop_qrcode.setImageBitmap(qrCodeBitmap);

            ll_parent.setDrawingCacheEnabled(true);
            ll_parent.measure(screenWidth, screenHeigth);
            ll_parent.layout(0, 0, screenWidth, screenHeigth);
            Bitmap allbitmap = Bitmap.createBitmap(ll_parent.getDrawingCache());
            ll_parent.setDrawingCacheEnabled(false);

            try {
                //若参数设置为Bitmap.CompressFormat.JPEG会出现背景为黑色的图片
                return allbitmap != null
                        && allbitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                        new FileOutputStream(filePath));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        } else {
            return false;
        }


    }
}
