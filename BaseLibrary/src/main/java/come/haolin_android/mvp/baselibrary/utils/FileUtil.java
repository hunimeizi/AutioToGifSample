package come.haolin_android.mvp.baselibrary.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import come.haolin_android.mvp.baselibrary.base.BaseApplication;


/**
 * 与文件相关的类,主要负责文件的读写
 *
 * @author 杨龙辉 2012.04.07
 */
public final class FileUtil {

    // ------------------------------ 手机系统相关 ------------------------------
    public static final String NEWLINE = System.getProperty("line.separator");// 系统的换行符
    public static final String APPROOT = "UMMoka";// 程序的根目录
    public static final String ASSERT_PATH = "file:///android_asset";//apk的assert目录
    public static final String RES_PATH = "file:///android_res";//apk的assert目录

    //----------------------------------存放文件的路径后缀------------------------------------
    public static final String CACHE_IMAGE_SUFFIX = File.separator + APPROOT + File.separator + "images" + File.separator;
    public static final String CACHE_VOICE_SUFFIX = File.separator + APPROOT + File.separator + "voice" + File.separator;
    public static final String CACHE_MATERIAL_SUFFIX = File.separator + APPROOT + File.separator + "material" + File.separator;
    public static final String LOG_SUFFIX = File.separator + APPROOT + File.separator + "Log" + File.separator;

    // ------------------------------------数据的缓存目录-------------------------------------------------------
    public static String SDCARD_PAHT;// SD卡路径
    public static String LOCAL_PATH;// 本地路径,即/data/data/目录下的程序私有目录
    public static String CURRENT_PATH = "";// 当前的路径,如果有SD卡的时候当前路径为SD卡，如果没有的话则为程序的私有目录

    /**
     * 根目录
     */
    private static String APP_ROOT_DIR = "Future Voucher";

    /**
     * 日志目录
     */
    private static final String LOG_FILE = "log";

    /**
     * 图片目录
     */
    private static final String IMAGE_FILE = "image";

    /**
     * 图片缓存目录
     */
    private static final String IMAGE_CACHE_FILE = "imageCache";

    /**
     * 网络请求缓存目录
     */
    private static final String NET_CACHE_FILE = "netCache";

    static {
        init();
    }

    public static void init() {
        SDCARD_PAHT = Environment.getExternalStorageDirectory().getPath();// SD卡路径
        LOCAL_PATH = BaseApplication.getInstance().getFilesDir().getAbsolutePath();// 本地路径,即/data/data/目录下的程序私有目录

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            CURRENT_PATH = SDCARD_PAHT;
        } else {
            CURRENT_PATH = LOCAL_PATH;
        }
    }

    /**
     * 得到与当前存储路径相反的路径(当前为/data/data目录，则返回/sdcard目录;当前为/sdcard，则返回/data/data目录)
     *
     * @return
     */
    public static String getDiffPath() {
        if (CURRENT_PATH.equals(SDCARD_PAHT)) {
            return LOCAL_PATH;
        }
        return SDCARD_PAHT;
    }


    public static String getDiffPath(String pathIn) {
        return pathIn.replace(CURRENT_PATH, getDiffPath());
    }

    // ------------------------------------文件的相关方法--------------------------------------------

    /**
     * 将数据写入一个文件
     *
     * @param destFilePath 要创建的文件的路径
     * @param data         待写入的文件数据
     * @param startPos     起始偏移量
     * @param length       要写入的数据长度
     * @return 成功写入文件返回true, 失败返回false
     */
    public static boolean writeFile(String destFilePath, byte[] data, int startPos, int length) {
        try {
            if (!createFile(destFilePath)) {
                return false;
            }
            FileOutputStream fos = new FileOutputStream(destFilePath);
            fos.write(data, startPos, length);
            fos.flush();
            if (null != fos) {
                fos.close();
                fos = null;
            }
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 从一个输入流里写文件
     *
     * @param destFilePath 要创建的文件的路径
     * @param in           要读取的输入流
     * @return 写入成功返回true, 写入失败返回false
     */
    public static boolean writeFile(String destFilePath, InputStream in) {
        try {
            if (!createFile(destFilePath)) {
                return false;
            }
            FileOutputStream fos = new FileOutputStream(destFilePath);
            int readCount = 0;
            int len = 1024;
            byte[] buffer = new byte[len];
            while ((readCount = in.read(buffer)) != -1) {
                fos.write(buffer, 0, readCount);
            }
            fos.flush();
            if (null != fos) {
                fos.close();
                fos = null;
            }
            if (null != in) {
                in.close();
                in = null;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean appendFile(String filename, byte[] data, int datapos, int datalength) {
        try {

            createFile(filename);

            RandomAccessFile rf = new RandomAccessFile(filename, "rw");
            rf.seek(rf.length());
            rf.write(data, datapos, datalength);
            if (rf != null) {
                rf.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 读取文件，返回以byte数组形式的数据
     *
     * @param filePath 要读取的文件路径名
     * @return
     */
    public static byte[] readFile(String filePath) {
        try {
            if (isFileExist(filePath)) {
                FileInputStream fi = new FileInputStream(filePath);
                return readInputStream(fi);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从一个数量流里读取数据,返回以byte数组形式的数据。
     * </br></br>
     * 需要注意的是，如果这个方法用在从本地文件读取数据时，一般不会遇到问题，但如果是用于网络操作，就经常会遇到一些麻烦(available()方法的问题)。所以如果是网络流不应该使用这个方法。
     *
     * @param in 要读取的输入流
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream in) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            byte[] b = new byte[in.available()];
            int length = 0;
            while ((length = in.read(b)) != -1) {
                os.write(b, 0, length);
            }

            b = os.toByteArray();

            in.close();
            in = null;

            os.close();
            os = null;

            return b;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取网络流
     *
     * @param in
     * @return
     */
    public static byte[] readNetWorkInputStream(InputStream in) {
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();

            int readCount = 0;
            int len = 1024;
            byte[] buffer = new byte[len];
            while ((readCount = in.read(buffer)) != -1) {
                os.write(buffer, 0, readCount);
            }

            in.close();
            in = null;

            return os.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                os = null;
            }
        }
        return null;
    }

    /**
     * 将一个文件拷贝到另外一个地方
     *
     * @param sourceFile    源文件地址
     * @param destFile      目的地址
     * @param shouldOverlay 是否覆盖
     * @return
     */
    public static boolean copyFiles(String sourceFile, String destFile, boolean shouldOverlay) {
        try {
            if (shouldOverlay) {
                deleteFile(destFile);
            }
            FileInputStream fi = new FileInputStream(sourceFile);
            writeFile(destFile, fi);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 路径名
     * @return
     */
    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 创建一个文件，创建成功返回true
     *
     * @param filePath
     * @return
     */
    public static boolean createFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                return file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 删除一个文件
     *
     * @param filePath 要删除的文件路径名
     * @return true if this file was deleted, false otherwise
     */
    public static boolean deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                return file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除 directoryPath目录下的所有文件，包括删除删除文件夹
     */
    public static boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] listFiles = dir.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                deleteDirectory(listFiles[i]);
            }
        }
       return dir.delete();
    }

    /**
     * 字符串转流
     *
     * @param str
     * @return
     */
    public static InputStream String2InputStream(String str) {
        ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
        return stream;
    }

    /**
     * 流转字符串
     *
     * @param is
     * @return
     */
    public static String inputStream2String(InputStream is) {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";

        try {
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    //批量更改文件后缀
    public static void reNameSuffix(File dir, String oldSuffix, String newSuffix) {
        if (dir.isDirectory()) {
            File[] listFiles = dir.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                reNameSuffix(listFiles[i], oldSuffix, newSuffix);
            }
        } else {
            dir.renameTo(new File(dir.getPath().replace(oldSuffix, newSuffix)));
        }
    }

    public static void writeImage(Bitmap bitmap, String destPath, int quality) {
        try {
            FileUtil.deleteFile(destPath);
            if (FileUtil.createFile(destPath)) {
                FileOutputStream out = new FileOutputStream(destPath);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取图片缓存路径
     *
     * @return 缓存路径File
     */
    public static File getImageCacheFile() {
        if (null == CURRENT_PATH) {
            return null;
        }
        String logPath = CURRENT_PATH + File.separator + APP_ROOT_DIR + File.separator
                + IMAGE_CACHE_FILE;
        if (isFileExists(logPath)) {
            createDir(logPath);
        }

        File file = new File(logPath);
        return file;
    }

    /**
     * 获取网络请求缓存路径
     *
     * @return 缓存路径File
     */
    public static File getNetCacheFile() {
        if (null == CURRENT_PATH) {
            return null;
        }
        String logPath = CURRENT_PATH + File.separator + APP_ROOT_DIR + File.separator
                + NET_CACHE_FILE;
        if (isFileExists(logPath)) {
            createDir(logPath);
        }

        File file = new File(logPath);
        return file;
    }

    public static boolean isSDExists() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static String getCurrentLogPath() {
        if (null == CURRENT_PATH) {
            return null;
        }
        String logPath = CURRENT_PATH + File.separator + APP_ROOT_DIR + File.separator
                + LOG_FILE;
        if (isFileExists(logPath)) {
            createDir(logPath);
        }
        return logPath;
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件地址
     * @return true 文件已经存在 false 文件不存在
     */
    public static boolean isFileExists(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        if (null == file || !file.exists()) {
            return false;
        }
        return true;
    }

    /**
     * 创建文件/文件夹
     *
     * @param fullDir 完整地址
     * @return 创建好的文件
     */
    public static File createDir(String fullDir) {
        File file = new File(fullDir);

        boolean isSucceed;
        if (!file.exists()) {
            File parentDir = new File(file.getParent());
            if (!parentDir.exists()) {
                isSucceed = parentDir.mkdirs();
                if (!isSucceed) {
                    DebugLog.e("createDir mkdirs failed");
                }
            }
            try {
                isSucceed = file.createNewFile();
                if (!isSucceed) {
                    DebugLog.e("createDir createNewFile failed");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static boolean isApkCanInstall(String path) {
        try {
            PackageManager pm = BaseApplication.getInstance().getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
            if (info != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取apk包的信息：版本号，名称，图标等
     *
     * @param absPath apk包的绝对路径
     * @param context
     */
    public static String apkVersionNam(String absPath, Context context) {

        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = pm.getPackageArchiveInfo(absPath, PackageManager.GET_ACTIVITIES);
        if (pkgInfo != null) {
            ApplicationInfo appInfo = pkgInfo.applicationInfo;
            /* 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
            appInfo.sourceDir = absPath;
            appInfo.publicSourceDir = absPath;
            return pkgInfo.versionName; // 得到版本信息
//            String appName = pm.getApplicationLabel(appInfo).toString();// 得到应用名
//            String packageName = appInfo.packageName; // 得到包名
            /* icon1和icon2其实是一样的 */
//            Drawable icon1 = pm.getApplicationIcon(appInfo);// 得到图标信息
//            Drawable icon2 = appInfo.loadIcon(pm);
//            String pkgInfoStr = String.format("PackageName:%s, Vesion: %s, AppName: %s", packageName, version, appName);
        }
        return "";
    }
    /**
     * 删除 directoryPath目录下的所有文件，包括删除删除文件夹
     */

    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }
}