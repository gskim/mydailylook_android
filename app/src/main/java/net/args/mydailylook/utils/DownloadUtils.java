package net.args.mydailylook.utils;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.yalantis.ucrop.util.FileUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2016-09-11.
 */
public class DownloadUtils {
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    public static RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
    }

//    @NonNull
//    public RequestBody createPartFromString(String descriptionString) {
//        return RequestBody.create(
//                MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
//    }

    @NonNull
    public static MultipartBody.Part prepareFilePart(Context context, String partName, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(context, fileUri);
        if (file == null) {
            return null;
        }

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

}
