package come.haolin_android.mvp.baselibrary.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 逗比 该类的所有方法在这都给你列出来了，只要你会ctrl+C 和ctrl+v 就行了  另外有的方法的参数我就不写了 不要把你懒死
 * 1.   PhoneUitls.getAlphaHexValue()     将透明度百分比值(0-100)转换为alphaFF色值(0-255)
 * 2.   PhoneUitls.dip2px()               根据手机的分辨率从 dp 的单位 转成为 px(像素)
 * 3.   PhoneUitls.px2dip()               根据手机的分辨率从 px(像素) 的单位 转成为 dp
 * 4.   PhoneUitls.getVerName()           获取版本名称
 * 5.   PhoneUitls.GetOperator()          中国移动：46000 46002 中国联通：46001 中国电信：46003
 * 6.   PhoneUitls.GetIMEI()              获取手机imei
 * 7.   PhoneUitls.getWifiInfo(context)   获取wifi信息
 * 8.   PhoneUitls.getLocalIpAddress()    得到wifi下的ip
 * 9.   PhoneUitls.isServiceRunning()     检查后台的server是否运行，通过输入server name
 * 10.  PhoneUitls.ScreenWH()             获取屏幕宽度
 * 11.  PhoneUitls.getScreenHeight()      获取屏幕高度
 * 12.  PhoneUitls.GetPhoneType()         获取手机型号
 * 13.  PhoneUitls.GetPhoneType(context)  获取当前应用的versioncode
 * 14.  PhoneUitls.getRealPathFromURI()   通过Uri获取文件在本地存储的真实路径
 * 15.  PhoneUitls.isRunningForeground()  判断activity是否在前台运行
 * 16.  PhoneUitls.getTopActivityName()   返回上一层activity的类名
 * 17.  PhoneUitls.getPackageName()       获取当前程序包名
 * 18.  PhoneUitls.getNetWorkType()       判断当前是否有可用的网络以及网络类型 0：无网络 1：WIFI 2：CMWAP 3：CMNET
 * 19.  PhoneUitls.sendSMS()              发送短信
 * 20.  PhoneUitls.getStateBarHeight()    获取状态栏高度
 * 21.  PhoneUitls.getVersionCode()       获取当前应用的versioncode
 */
public class PhoneUitls {

    public static int screenWidthPx; //屏幕宽 px
    public static int screenhightPx; //屏幕高 px
    public static float density;//屏幕密度
    public static int densityDPI;//屏幕密度
    public static float screenWidthDip;//  dp单位
    public static float screenHightDip;//  dp单位


    /**
     * 将透明度百分比值(0-100)转换为alphaFF色值(0-255)
     *
     * @return 任何一种颜色的值范围都是 0 到 255（00 到 ff）。对于 alpha，00 表示完全透明，ff 表示完全不透明。
     */
    public static int getAlphaHexValue(int alpha, Color color) {
        return (int) (alpha / 100.0 * 255);
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
     *
     * @param context Context
     * @param pxValue float
     * @return int
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取版本名称
     *
     * @param c Context
     * @return 版本 String
     */
    public static String getVerName(Context c) {
        try {
            PackageInfo packageInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "未知版本";
    }

    /**
     * 中国移动：46000 46002 中国联通：46001 中国电信：46003
     *
     * @param c Context
     * @return 运营商
     */
    public static String GetOperator(Context c) {
        TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = tm.getSimOperator();
        if (operator != null) {
            switch (operator) {
                case "46000":
                case "46002":
                case "46007":
                    return "中国移动";
                case "46001":
                    return "中国联通";
                case "46003":
                    return "中国电信";
            }
        }
        return "未知";
    }


    /**
     * 得到wifi状态下的ip 需要使用wifi中，需要添加权限 <uses-permission
     * android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
     * <uses-permission
     * android:name="android.permission.CHANGE_WIFI_STATE"></uses-permission>
     * <uses-permission
     * android:name="android.permission.WAKE_LOCK"></uses-permission>
     *
     * @return String格式的字符串（10.111.130.233;fe80::7ad6:f0ff:fe0a:da50%wlan0;192.168
     * .1.100）
     */
    public String getLocalIpAddress() {
        String ipaddress = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ipaddress = ipaddress + ";" + inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return ipaddress;
    }

    /**
     * 检查后台的server是否运行，通过输入server name
     *
     * @param context   Context
     * @param className String
     * @return boolean
     */
    public static boolean isServiceRunning(Context context, String className) {

        boolean isRunning = false;

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);

        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }
    /**
     * 获取屏幕宽高
     *
     * @param c Activity
     * @return int[]
     */
    public static int[] ScreenWH(Activity c) {
        int[] screenwh = new int[2];
        DisplayMetrics dm = new DisplayMetrics();
        c.getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenwh[0] = dm.widthPixels;
        screenwh[1] = dm.heightPixels;
        return screenwh;
    }

    private static int widthPixels;

    /**
     * 获取屏幕宽度
     *
     * @param context Context
     * @return int
     */
    public static int getScreenWidth(Context context) {
        if (widthPixels > 0) {
            return widthPixels;
        } else {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            return dm.widthPixels;
        }
    }

    private static int heightPixels;

    /**
     * 获取屏幕高度
     *
     * @param context Context
     * @return int
     */
    public static int getScreenHeight(Context context) {
        if (heightPixels > 0) {
            return heightPixels;
        } else {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            return dm.heightPixels;
        }
    }

    /**
     * 获取phone型号
     *
     * @return String
     */
    public static String GetPhoneType() {
        return android.os.Build.MODEL.replaceAll(" ", "_");
    }

    /**
     * 获取当前应用的versioncode
     *
     * @return int
     */
    public static int GetPhoneType(Context c) {
        try {
            PackageInfo packageInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 通过Uri获取文件在本地存储的真实路径
     *
     * @param act        Activity
     * @param contentUri Uri
     * @return String
     */
    public static String getRealPathFromURI(Activity act, Uri contentUri) {
        // can post image
        String[] proj = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = act.managedQuery(contentUri, proj, // Which columns to
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     * 判断当前activity是否在前台运行
     *
     * @param context   Context
     * @param className String
     * @return boolean
     */
    public static boolean isRunningForeground(Context context, String className) {
        String packageName = getPackageName(context);
        String topActivityClassName = getTopActivityName(context);
        return topActivityClassName.equals(className) || packageName != null && topActivityClassName.startsWith(packageName);
    }

    /**
     * 返回最上层的activity类名
     *
     * @param context Context
     * @return String
     */
    public static String getTopActivityName(Context context) {
        String topActivityClassName = null;
        ActivityManager activityManager = (ActivityManager) (context.getSystemService(Context.ACTIVITY_SERVICE));
        List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topActivityClassName = f.getClassName();
        }
        return topActivityClassName;
    }

    /**
     * 获取当前程序包名
     *
     * @param context Context
     * @return String
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 判断当前是否有可用的网络以及网络类型 0：无网络 1：WIFI 2：CMWAP 3：CMNET
     *
     * @param context Context
     * @return int
     */
    public static int getNetWorkType(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return 0;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        if (anInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            return 1;
                        } else if (anInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            String extraInfo = anInfo.getExtraInfo();
                            if ("cmwap".equalsIgnoreCase(extraInfo) || "cmwap:gsm".equalsIgnoreCase(extraInfo)) {
                                return 2;
                            }
                            return 3;
                        }
                    }
                }
            }
        }
        return 0;
    }

    /**
     * 发送短信
     *
     * @param context  Context
     * @param tonumber String
     * @param smsBody  String
     */
    public static void sendSMS(Context context, String tonumber, String smsBody) {
        Uri smsToUri = Uri.parse("smsto:  " + tonumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("sms_body", smsBody);
        context.startActivity(intent);
    }

    /**
     * 获取状态栏高度
     *
     * @param context Activity
     * @return int
     */
    public static int getStateBarHeight(Activity context) {

        Class<?> c;
        Object obj;
        Field field;
        int x, sbar;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
            return sbar;
        } catch (Exception e1) {
            e1.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取当前应用的versioncode
     *
     * @param c Context
     * @return int
     */
    public static int getVersionCode(Context c) {
        try {
            PackageInfo packageInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 当前号码是否手机号码
     *
     * @param mobiles 手机号码
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^1[3|4|5|6|7|8|9][0-9]{9}$");
        Matcher m = p.matcher(preHandleNumber(mobiles));
        return m.matches();
    }

    private static String preHandleNumber(String phone) {
        String phoneNumber = phone.replace(" ", "");
        if (phoneNumber.contains("+86") && phoneNumber.length() == 14) {
            phoneNumber = phoneNumber.substring(3, phoneNumber.length());
        }
        return phoneNumber;
    }

    public static boolean getSystemLocationSetting(final Context context) {
        try {
            android.location.LocationManager locationManager = (android.location.LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);
            boolean gps = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
            if (gps) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * 获取状态栏高度
     */

    public static int getStatusHeight(Activity activity) {
        int statusHeight = -1;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusHeight = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return statusHeight;

    }
    public static int getNavigationBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Navi height:" + height);
        return height;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
    /*
   * 获取设备的屏幕宽度（px）
   * */
    public static int getScreenWidthPx(Context context){
        WindowManager windowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display=windowManager.getDefaultDisplay();
        DisplayMetrics metrics=new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static  void refreshAlbum(Context context, File file) {
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }

    public static void updateFileFromDatabase(Context context, String filepath){
        String where= MediaStore.Audio.Media.DATA+" like \""+filepath+"%"+"\"";
        context.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,where,null);
    }

    /**
     * 打开wifi设置
     * @param context context
     */
    public static void openWifi(Context context) {
        Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
        context.startActivity(wifiSettingsIntent);
    }
}
