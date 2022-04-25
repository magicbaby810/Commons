package com.sk.commons.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;

import com.gyf.barlibrary.ImmersionBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    /**
     * 旋转bitmap
     * @param bitmap
     * @param angle
     * @return
     */
    public static Bitmap rotateImg(Bitmap bitmap, int angle) {
        Matrix matrix = new Matrix();
        matrix.setRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 根据两个经纬度计算偏移角度
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double getAngle(double lat1, double lng1, double lat2, double lng2) {
        double fLat = Math.PI * (lat1) / 180.0;
        double fLng = Math.PI * (lng1) / 180.0;
        double tLat = Math.PI * (lat2) / 180.0;
        double tLng = Math.PI * (lng2) / 180.0;

        double degree = (Math.atan2(Math.sin(tLng - fLng) * Math.cos(tLat), Math.cos(fLat) * Math.sin(tLat) - Math.sin(fLat) * Math.cos(tLat) * Math.cos(tLng - fLng))) * 180.0 / Math.PI;
        if (degree >= 0) {
            return degree;
        } else {
            return 360 + degree;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 判断是不是wifi网络状态
     *
     * @param paramContext
     * @return
     */
    public static boolean isWifi(Context paramContext) {
        return "2".equals(getNetType(paramContext)[0]);
    }

    /**
     * 判断是不是2/3G网络状态
     *
     * @param paramContext
     * @return
     */
    public static boolean isMobile(Context paramContext) {
        return "1".equals(getNetType(paramContext)[0]);
    }

    /**
     * 网络是否可用
     *
     * @param paramContext
     * @return
     */
    public static boolean isNetAvailable(Context paramContext) {
        if ("1".equals(getNetType(paramContext)[0]) || "2".equals(getNetType(paramContext)[0])) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前网络状态 返回2代表wifi,1代表2G/3G
     *
     * @param paramContext
     * @return
     */
    public static String[] getNetType(Context paramContext) {
        String[] arrayOfString = {"Unknown", "Unknown"};
        PackageManager localPackageManager = paramContext.getPackageManager();
        if (localPackageManager.checkPermission("android.permission.ACCESS_NETWORK_STATE", paramContext.getPackageName()) != 0) {
            arrayOfString[0] = "Unknown";
            return arrayOfString;
        }

        ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext.getSystemService("connectivity");
        if (localConnectivityManager == null) {
            arrayOfString[0] = "Unknown";
            return arrayOfString;
        }

        NetworkInfo localNetworkInfo1 = localConnectivityManager.getNetworkInfo(1);
        if (localNetworkInfo1 != null && localNetworkInfo1.getState() == NetworkInfo.State.CONNECTED) {
            arrayOfString[0] = "2";
            return arrayOfString;
        }

        NetworkInfo localNetworkInfo2 = localConnectivityManager.getNetworkInfo(0);
        if (localNetworkInfo2 != null && localNetworkInfo2.getState() == NetworkInfo.State.CONNECTED) {
            arrayOfString[0] = "1";
            arrayOfString[1] = localNetworkInfo2.getSubtypeName();
            return arrayOfString;
        }

        return arrayOfString;
    }


    /**
     * 获取虚拟导航键的高度
     * @param context
     * @return
     */
    public static int getVirtualBarHeight(Context context) {
        int vh = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - display.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }


    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        if (!validateString(str))
            return false;
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,6,8,7,9][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    public static boolean validateString(String... text) {
        for (int i = 0; i < text.length; i++) {
            if (text[i] == null || "".equals(text[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkPermissions(final Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }



    // 隐藏虚拟键盘
    public static void hideKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != imm) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    /**
     * 字符串判空
     * @param str
     * @return
     */
    public static String checkNull(String str) {
        return TextUtils.isEmpty(str) ? "" : str;
    }

    /**
     * 字符串 int判空
     * @param str
     * @return
     */
    public static int checkIntegerNull(String str) {
        return TextUtils.isEmpty(str) ? 0 : Integer.parseInt(str);
    }

    /**
     * 字符串 double判空
     * @param str
     * @return
     */
    public static Double checkDoubleNull(String str) {
        return TextUtils.isEmpty(str) ? 0 : Double.parseDouble(str);
    }

    /**
     * 字符串 long判空
     * @param str
     * @return
     */
    public static Long checkLongNull(String str) {
        return TextUtils.isEmpty(str) ? 0 : Long.parseLong(str);
    }

    /**
     * 读取buildconfig信息
     * @param fieldName
     * @return
     */
    public static Object getBuildConfigValue(String fieldName) {
        try {
            Class<?> clazz = Class.forName("com.sk.shanhai.BuildConfig");
            Field field = clazz.getField(fieldName);
            return field.get(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 提供精确的乘法运算
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static Float mul(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).floatValue();
    }


    //注册广播

    /**
     * 两个Float数相加
     * @param v1
     * @param v2
     * @return Float
     */
    public static Float add(Float v1,Float v2){
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.add(b2).floatValue();
    }

    /**
     * 金额相加
     * @param v1
     * @param v2
     * @return String
     */
    public static String add(String v1, String v2){
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).toString();
    }

    /**
     * 金额相减
     * @param v1
     * @param v2
     * @return String
     */
    public static String minus(String v1, String v2){
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).toString();
    }

    /**
     * 金额相除
     * @param v1
     * @param v2
     * @return String
     */
    public static String divide(String v1, String v2, int pre){
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, pre, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 金额相除
     * @param v1
     * @param v2
     * @return String
     */
    public static String divide(String v1, String v2){
        return divide(v1, v2, 2);
    }

    /**
     * @desc 分转化为元，字符串操作，防止丢失精度
     * @param money
     * @return
     */
    public static String getMoney(Object money){
        String returnMoney="";
        if(money==null)
            return returnMoney;
        String moneyStr=money.toString();
        if(moneyStr.length()==1){
            returnMoney= "0.0"+money;
        }else if(moneyStr.length()==2){
            returnMoney= "0."+money;
        }else if(moneyStr.length()>2){
            returnMoney=moneyStr.substring(0,moneyStr.length()-2)+"."+moneyStr.substring(moneyStr.length()-2,moneyStr.length());
        }
        return returnMoney;
    }


    /**
     * @desc 精确到小数点两位
     */
    public static String IntRoundingTwo(int one) {

        DecimalFormat df = new DecimalFormat("0.00");
        String two = df.format((float) one / 1000);
        return two;
    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    /**
     * 获取应用包信息
     * @param context
     * @return
     */
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            pi = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    public static String getIMEI(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                ? ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId()
                : "";
    }

    //邮箱验证
    public static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        if (TextUtils.isEmpty(strPattern)) {
            return false;
        } else {
            return strEmail.matches(strPattern);
        }
    }

    /**
     * @desc :防止频繁触(间隔1S)
     * @return
     */
    public static boolean isFastClick() {
        boolean flag = true;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = curClickTime;
        return flag;
    }


    //获取屏幕原始尺寸高度，包括虚拟功能键高度
    public static DisplayMetrics getDpi(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics",DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            //dpi=displayMetrics.heightPixels;
        }catch(Exception e){
            e.printStackTrace();
        }
        return displayMetrics;
    }


    /**
     * 按图片的高度设置ratingbar高度
     * @param context
     * @param resourceId
     * @param ratingBar
     */
    public static void setRatingBarHeightByOriginImgHeight(Context context, int resourceId, RatingBar ratingBar) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resourceId);

            //将获取的图片高度设置给RatingBar
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ratingBar.getLayoutParams();
            lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            lp.height = bmp.getHeight();
            ratingBar.setLayoutParams(lp);

            if (null != bmp && !bmp.isRecycled()) {
                bmp.recycle();
                bmp = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取当前IP
     * @return
     */
    public static String getLocalIpAddress()
    {
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress())
                    {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        catch (SocketException ex)
        {
            Log.e("Wifi IpAddress", ex.toString());
        }
        return null;
    }


    public static String[] getPhoneContacts(Context context, Uri uri){
        String[] contact = new String[2];
        //得到ContentResolver对象**
        ContentResolver cr = context.getContentResolver();
        //取得电话本中开始一项的光标**
        Cursor cursor = cr.query(uri,null,null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
            //取得联系人姓名**
            int nameFieldColumnIndex=cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            //取得电话号码**
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if (phone != null) {
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            if (null != phone) phone.close();
            cursor.close();
        }
        else {
            return null;
        }
        return contact;
    }


    /**
     * 随机用户姓名
     */
    public static String randomUsername() {
        char a = 'a';
        String username = "U" + String.valueOf((char)(a+(int)(Math.random()*26))) + String.valueOf((char)(a+(int)(Math.random()*26))) + "10"+ new Random().nextInt(1000);
        return username;
    }

    /**
     * 判断字符串是否是json格式
     * @param str
     * @return
     */
    public static boolean isJson(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            return true;
        } catch (Exception e) {
            try {
                JSONArray jsonArray = new JSONArray(str);
                return true;
            } catch (Exception e1) {
                return false;
            }
        }
    }

    /**
     * view 转bitmap
     * @param view
     */
    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return bitmap;
    }

    /**
     * 拼接数据
     * @param hashMap
     * @return 返回拼接好的字符串
     */
    public static String pieceDataByMap(HashMap hashMap) {
        StringBuilder stringBuffer = new StringBuilder();
        for (Object key : hashMap.keySet()) {
            String value = (String) hashMap.get(key);
            stringBuffer.append("&").append(key).append("=").append(value);
        }
        return stringBuffer.toString();
    }


    /**
     * 反射去除searchview下划线
     * @param searchView
     */
    public static void removeUnderline(SearchView searchView) {
        if (searchView != null) {
            try {        //--拿到字节码
                Class<?> argClass = searchView.getClass();
                //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
                Field ownField = argClass.getDeclaredField("mSearchPlate");
                //--暴力反射,只有暴力反射才能拿到私有属性
                ownField.setAccessible(true);
                View mView = (View) ownField.get(searchView);
                //--设置背景
                mView.setBackgroundColor(Color.TRANSPARENT);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断当前手机是否被root
     *
     * @return
     */
    public synchronized static boolean checkRoot() {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            return exitValue == 0;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * @desc 在矩形内随机生成经纬度
     * @param MinLon：最小经度
     * 		  MaxLon：最大经度
     *  	  MinLat：最小纬度
     * 		  MaxLat：最大纬度
     * @return
     */
    public static Map<String, String> randomLonLat(double MinLon, double MaxLon, double MinLat, double MaxLat) {
        BigDecimal db = new BigDecimal(Math.random() * (MaxLon - MinLon) + MinLon);
        // 小数后6位
        String lon = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();
        db = new BigDecimal(Math.random() * (MaxLat - MinLat) + MinLat);
        String lat = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();
        Map<String, String> map = new HashMap<>();
        map.put("lon", lon);
        map.put("lat", lat);
        return map;
    }


    public static int getNavigationHeight(Context activity) {
        if (isNavigationBarShow((Activity) activity)) {
            return ImmersionBar.getNavigationBarHeight((Activity) activity);
        }
        return 0;
    }

    /**
     * 检测虚拟按键是否可见
     *
     * @param activity activity
     * @return
     */
    public static boolean isNavigationBarShow(Activity activity) {

        //虚拟键的view,为空或者不可见时是隐藏状态
        View view = activity.findViewById(android.R.id.navigationBarBackground);
        if (view == null) {
            return false;
        }
        int visible = view.getVisibility();
        if (visible == View.GONE || visible == View.INVISIBLE) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检查网络是否可用
     *
     * @param paramContext
     *
     * @return
     */
    public static boolean isNetConnected(Context paramContext) {
        boolean i = false;
        NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if ((localNetworkInfo != null) && (localNetworkInfo.isAvailable())) {
            return true;
        }
        return false;
    }

    /**
     * 保存图片到本地
     * @param path
     * @param bmp
     */
    public static void saveImage(Bitmap bmp, String path) {
        // 首先保存图片
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存图片到相册
     * @param context
     * @param bmp
     */
    public static void saveImageToGallery(Context context, Bitmap bmp, String path) {

        String fileName = path.substring(path.lastIndexOf("/"));
        saveImage(bmp, path);
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), path, fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));

    }


}
