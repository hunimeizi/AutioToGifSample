package come.haolin_android.mvp.baselibrary.imagepicker.util;

import java.util.ArrayList;
import java.util.List;

import come.haolin_android.mvp.baselibrary.bean.ImageItem;

public class CollectionHelper {
    public static List<String> imageItem2String(ArrayList<ImageItem> mImageItems) {
        List<String> result = new ArrayList<>(mImageItems.size());
        int length = mImageItems.size();
        for (int i = 0; i < length; i++) {
            result.add(mImageItems.get(i).getImageUrl());
        }
        return result;
    }
}
