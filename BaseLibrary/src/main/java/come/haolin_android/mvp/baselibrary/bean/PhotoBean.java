package come.haolin_android.mvp.baselibrary.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：haoLin_Lee on 2019/05/10 23:28
 * 邮箱：Lhaolin0304@sina.com
 * class:
 */
public class PhotoBean implements Parcelable {


    /**
     * 图片原始路径
     */
    private String originalPath;
    /**
     * 是否压缩过
     */
    private boolean compressed;
    /**
     * 压缩后路径
     */
    private String compressPath;

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    public String getCompressPath() {
        return compressPath;
    }

    public void setCompressPath(String compressPath) {
        this.compressPath = compressPath;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.originalPath);
        dest.writeByte(this.compressed ? (byte) 1 : (byte) 0);
        dest.writeString(this.compressPath);
    }

    public PhotoBean(String originalPath) {
        this.originalPath = originalPath;
    }

    private PhotoBean(Parcel in) {
        this.originalPath = in.readString();
        this.compressed = in.readByte() != 0;
        this.compressPath = in.readString();
    }

    public static final Creator<PhotoBean> CREATOR = new Creator<PhotoBean>() {
        @Override
        public PhotoBean createFromParcel(Parcel source) {
            return new PhotoBean(source);
        }

        @Override
        public PhotoBean[] newArray(int size) {
            return new PhotoBean[size];
        }
    };
}
