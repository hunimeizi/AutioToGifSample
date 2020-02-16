package come.haolin_android.mvp.baselibrary.imagepicker.util;

import android.content.Context;

public class ProviderUtil {

    public static String getFileProviderName(Context context){
        return context.getPackageName()+".fileprovider";
    }
}
