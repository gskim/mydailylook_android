package net.args.mydailylook.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import net.args.mydailylook.common.Preferences;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

/**
 * Created by arseon on 2016-08-26.
 */
public class OsUtils {

    public static boolean checkAvailableApp(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(intent,
                PackageManager.GET_META_DATA);

        if (list == null)
            return false;
        return list.size() > 0;
    }

    /**
     * Returns 안드로이드 버전
     *
     * @param
     * @return int
     */
    public static int getAndroidVersion() {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        return SDK_INT;
    }

    /**
     * Returns OS 버전 (ex> 4.1.2)
     *
     * @return String
     */
    public static String getOsVersion() {
        String osVersion = android.os.Build.VERSION.RELEASE;
        return osVersion;
    }

    /**
     * Returns App Version
     *
     * @param context
     * @return version
     */
    public static int getVersionCode(Context context) {
        PackageInfo i;
        int version = 0;
        try {
            i = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            version = i.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return version;
    }

    /**
     * @param packageName
     * @return
     */
    public static String getOtherAppVersion(Context context, String packageName) {
        String version = "";
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version = "0";
        }
        return version;
    }

    public static int getTargetSdkVersion(Context context) {
        int targetSdkVersion = 0;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(getPackageName(context), 0);
            if (applicationInfo != null) {
                targetSdkVersion = applicationInfo.targetSdkVersion;
            }
        } catch (Exception e) {
            targetSdkVersion = 0;
        }
        return targetSdkVersion;
    }

    /**
     * Returns App VersionName
     *
     * @param context
     * @return version
     */
    public static String getVersionName(Context context) {
        PackageInfo i;
        String version = "";
        try {
            i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = "" + i.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return version;
    }

    public static String getApplicationName(Context context) {
        int stringId = context.getApplicationInfo().labelRes;
        return context.getString(stringId);
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static String getDeviceModelName() {
        return Build.MODEL;
    }

    public static void openApkFile(Context context, String apkName)
            throws FileNotFoundException {
//        File file = new File(
//                android.os.Environment.getExternalStorageDirectory() + "/"
//                        + APK_CACHE_DIR + "/" + apkName);
//        Uri apkUri = Uri.fromFile(file);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
//        context.startActivity(intent);
    }

    public static void deleteApkFilesFolder(Context context) {
//        File dir = new File(
//                android.os.Environment.getExternalStorageDirectory() + "/"
//                        + APK_CACHE_DIR);
//        OsUtil.deleteDirectory(dir);
    }

    private static void deleteDirectory(File dir) {
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                OsUtils.deleteDirectory(file);
            } else {
                file.delete();
            }
        }
    }

    public static boolean isInstalled(Context context, String packageName) {
        boolean bool = true;
        try {
            context.getPackageManager().getApplicationInfo(packageName.trim(), 1);
        } catch (Exception e) {
            e.printStackTrace();
            bool = false;
        }
        return bool;
    }

    public static boolean runPackage(Context context, String packageName) {
        boolean bool = true;
        try {
            Intent intent = context.getPackageManager()
                    .getLaunchIntentForPackage(packageName);
            context.startActivity(intent);
        } catch (Exception localException) {
            bool = false;
        }
        return bool;
    }

    public static boolean goMarket(Context paramContext, String paramString) {
        boolean bool = false;
        try {
            Uri localUri = Uri.parse("market://details?id=" + paramString);
            DevLog.i("goMarket()", "uri=market://details?id=" + paramString);
            Intent localIntent = new Intent("android.intent.action.VIEW",
                    localUri);
            if (localIntent != null) {
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                paramContext.startActivity(localIntent);
            }
            bool = true;
        } catch (Exception localException) {
            localException.printStackTrace();
        }

        return bool;
    }

    public static String getGooglePlayStoreAddress(Context context) {
        return "https://play.google.com/store/apps/details?id=" + getPackageName(context);
    }

    public static String getMarketAddress(Context context) {
        return "market://details?id=" + getPackageName(context);
    }

    private static String getMacaddress(Context context) {
        String result = "";
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();

        result = info.getMacAddress();
        if (result == null || result.length() <= 0) {
            result = info.getBSSID();
        }
        result = result.replace(":", "");
        return result;
    }

    public static String getDeviceId(Context context) {
        Preferences pref = Preferences.getInstance(context);
        String result = pref.getDeviceID();
        if (result == null || result.length() <= 0) {
            try {
                TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                result = telManager.getDeviceId();

                if (result == null || result.length() <= 0) {
                    result = getMacaddress(context);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (result == null || result.length() <= 0) {
                result = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            pref.setDeviceID(result);
        }

        return result;
    }

    public static String getDeviceHashId(Context context) {
        String deviceId = null;
        String deviceIdMD5 = null;

        try {
            deviceId = getDeviceId(context);
            deviceIdMD5 = Hash.convertToHex(Hash.MD5(deviceId.toUpperCase(Locale.getDefault())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deviceIdMD5;
    }

    public static String getMD5Hash(String str) {
        String MD5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte byteData[] = md.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            MD5 = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            MD5 = null;
        }
        return MD5;
    }

}
