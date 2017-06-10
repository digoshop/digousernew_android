package com.digoshop.app.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class ImageUtil {

	public static final String TAG = "ImageUtil";
	public static final String CACHE_TAG = "GP_IMAGE_CACHE";

	private ImageUtil() {
	}

	/**
	 * ��ͨͼƬѹ������ ��СΪ200k
	 * 
	 * @param image
	 *            ԭʼ��Դ
	 * @return
	 */
	public static Bitmap compressImage200(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		image.compress(Bitmap.CompressFormat.JPEG, options, baos);
		while (baos.toByteArray().length / 1024 > 200) {
			baos.reset();
			if (options >= 0 && options <= 100) {
				image.compress(Bitmap.CompressFormat.JPEG, options, baos);
				options -= 10;
			} else {
				image.compress(Bitmap.CompressFormat.JPEG, 10, baos);
			}
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
		return bitmap;
	}

	/**
	 * ͼƬѹ������
	 * 
	 * @param image
	 *            ԭʼ��Դ
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		image.compress(Bitmap.CompressFormat.JPEG, options, baos);
		image = Bitmap.createScaledBitmap(image, 150, 150, true);
		while (baos.toByteArray().length / 1024 > 8) {
			baos.reset();
			if (options >= 0 && options <= 100) {
				image.compress(Bitmap.CompressFormat.JPEG, options, baos);
				options -= 10;
			} else {
				image.compress(Bitmap.CompressFormat.JPEG, 10, baos);
			}
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
		return bitmap;
	}

	/**
	 * ����ѹ�� �����������������Բ��ܺܺõı�֤ͼƬ������
	 * 
	 * @param image
	 *            ԭʼ��Դ
	 * @param path
	 * @param srcPath
	 * @return
	 */
	public static Bitmap compressOptions(String path, String srcPath) {

		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// �����ȡ����
		newOpts.inSampleSize = 2;
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;
	}

	/**
	 * ����ͼƬ
	 * 
	 * @param bm
	 *            ��Ҫת����bitmap
	 * @param newWidth�µĿ�
	 * @param newHeight�µĸ�
	 * @return ָ����ߵ�bitmap
	 */
	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {

		if (bm == null)
			return null;
		// ���ͼƬ�Ŀ��
		int width = bm.getWidth();
		int height = bm.getHeight();
		// �������ű���
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// ȡ����Ҫ���ŵ�matrix����
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// �õ��µ�ͼƬ www.2cto.com
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		return newbm;
	}

	/**
	 * ����ͼƬ
	 * 
	 * @param bm
	 *            ��Ҫת����bitmap
	 * @param newWidth�µĿ�
	 * @return ָ�����bitmap
	 */
	public static Bitmap zoomImg(Bitmap bm, int newWidth) {

		if (bm == null)
			return null;
		// ���ͼƬ�Ŀ��
		int width = bm.getWidth();
		int height = bm.getHeight();
		if (width >= newWidth)
			return bm;
		// �������ű���
		float scaleWidth = ((float) newWidth) / width;
		// ȡ����Ҫ���ŵ�matrix����
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleWidth);
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		return newbm;
	}

	/**
	 * ����ͼƬ
	 * 
	 * @param bm
	 *            ��Ҫת����bitmap
	 * @param newWidth�µĿ�
	 * @return ָ�����bitmap
	 */
	public static Bitmap zoomImgForW(Bitmap bm, int imgWid) {

		if (bm == null)
			return null;
		// ���ͼƬ�Ŀ��
		int width = bm.getWidth();
		int height = bm.getHeight();

		if (width <= 0 || height <= 0 || imgWid <= 0) {
			return bm;
		}

		float scale;
		if (width > height) {
			if (height <= 0) {
				return bm;
			}
			scale = ((float) imgWid) / height;
		} else {
			if (width <= 0) {
				return bm;
			}
			scale = ((float) imgWid) / width;
		}
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		if (width > 0 && height > 0) {

			Bitmap newbm = null;
			try {
				newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return bm;
			}
			return newbm;
		}
		return bm;
	}

	/**
	 * ����ͼƬ
	 * 
	 * @param bm
	 *            ��Ҫת����bitmap
	 * @param newWidth�µĿ�
	 * @param newHeight�µĸ�
	 * @return ָ����ߵ�bitmap
	 */
	public static Bitmap zoomImgForWidth(Bitmap bm, int newWidth, boolean isphoto, float scaledWidth,
			float scaledHeight) {
		Bitmap newbm = null;
		// ���ͼƬ�Ŀ��
		int width = bm.getWidth();
		int height = bm.getHeight();
		Matrix matrix = new Matrix();
		if (isphoto) {
			float scaleWidth, scaleHeight;
			if (newWidth == width)
				return bm;

			if (height < width) {
				if (newWidth == height)
					return bm;
				// �������ű���
				scaleWidth = ((float) newWidth) / height;
				matrix.postScale(scaleWidth, scaleWidth);
			} else if (height == width) {
				// �������ű���
				scaleWidth = ((float) newWidth) / width;
				scaleHeight = ((float) newWidth) / height;
				matrix.postScale(scaleWidth, scaleHeight);
			} else {
				scaleWidth = ((float) newWidth) / width;
				matrix.postScale(scaleWidth, scaleWidth);
			}
		} else {
			matrix.postScale(scaledWidth, scaledHeight);
		}
		newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		if (bm != newbm && bm.isRecycled())
			bm.recycle();
		bm = null;
		return newbm;
	}

	/**
	 * �õ����ػ��������ϵ�bitmap url - ������߱���ͼƬ�ľ���·��,����:
	 * 
	 * A.����·��: url="http://blog.foreverlove.us/girl2.png" ;
	 * 
	 * B.����·��:url="file://mnt/sdcard/photo/image.png";
	 * 
	 * C.֧�ֵ�ͼƬ��ʽ ,png, jpg,bmp,gif�ȵȡ�0.������
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap GetLocalOrNetBitmap(String url) {
		Bitmap bitmap = null;
		InputStream in = null;
		BufferedOutputStream out = null;
		try {
			in = new BufferedInputStream(new URL(url).openStream(), 1024);
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, 1024);
			copy(in, out);
			out.flush();
			byte[] data = dataStream.toByteArray();
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			data = null;
			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] b = new byte[1024];
		int read;
		while ((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}
}
