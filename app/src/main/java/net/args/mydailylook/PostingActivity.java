package net.args.mydailylook;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.rey.material.widget.Spinner;
import com.volokh.danylo.hashtaghelper.HashTagHelper;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.util.FileUtils;

import net.args.mydailylook.common.Const;
import net.args.mydailylook.common.Preferences;
import net.args.mydailylook.model.BaseModel;
import net.args.mydailylook.model.DaumSearchItem;
import net.args.mydailylook.network.MyDailyLookService;
import net.args.mydailylook.network.ServiceGenerator;
import net.args.mydailylook.utils.DownloadUtils;
import net.args.mydailylook.utils.OsUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016-07-24.
 */
public class PostingActivity extends Activity implements View.OnClickListener {
    private static final int REQUEST_SELECT_PICTURE = 100;
    private static final int REQUEST_SEARCH_PLACE = 101;
    private static final int MAX_ADD_IMAGE_COUNT = 3;

    private EditText mContentEdit;
    private HashTagHelper mTextHashTagHelper;
    private ImageView mPostingView;
    private ImageView mThumbView;
    private ImageView mAddView;
    private LinearLayout mSubLayout;
    private LinearLayout mAddedLayout;
    private Spinner mSpinner;
    private TextView mAddPlaceView;
    private LinearLayout mPlaceResultLayout;
    private TextView mPlaceTitleView;
    private TextView mPlaceAddrView;
    private ImageView mPlaceDelView;

    private boolean mIsSelectAddImg;
    private String mLocation;
    private MyDailyLookService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        mService = ServiceGenerator.getService();

        initLayout();
        initListener();
    }

    private void initLayout() {
        mContentEdit = (EditText) findViewById(R.id.et_activity_posting_content);
        mThumbView = (ImageView) findViewById(R.id.iv_activity_posting_thumb);
        mAddView = (ImageView) findViewById(R.id.iv_activity_posting_add);
        mPostingView = (ImageView) findViewById(R.id.iv_activity_posting);
        mSubLayout = (LinearLayout) findViewById(R.id.ll_activity_posting_added_images);
        mAddedLayout = (LinearLayout) findViewById(R.id.ll_activity_posting_images);
        mSpinner = (Spinner) findViewById(R.id.spinner_activity_posting_permission);
        String[] publicList = getResources().getStringArray(R.array.public_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, publicList);
        mSpinner.setAdapter(adapter);

        mAddPlaceView = (TextView) findViewById(R.id.tv_activity_posting_add_place);
        mPlaceResultLayout = (LinearLayout) findViewById(R.id.ll_activity_posting_place_result);
        mPlaceTitleView = (TextView) findViewById(R.id.tv_activity_posting_place_title);
        mPlaceAddrView = (TextView) findViewById(R.id.tv_activity_posting_place_addr);
        mPlaceDelView = (ImageView) findViewById(R.id.iv_activity_posting_place_delete);
    }

    private void initListener() {
        findViewById(R.id.iv_activity_posting_close).setOnClickListener(this);
        findViewById(R.id.ll_activity_posting_add_place).setOnClickListener(this);
        mThumbView.setOnClickListener(this);
        mAddView.setOnClickListener(this);
        mPostingView.setOnClickListener(this);
        mPlaceDelView.setOnClickListener(this);

        mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
            @Override
            public void onHashTagClicked(String hashTag) {
            }
        });
        mTextHashTagHelper.handle(mContentEdit);
    }

    private void addImageView(Bitmap bitmap, Uri imgUri) {
        final FrameLayout layout = (FrameLayout) getLayoutInflater().inflate(R.layout.item_add_image, null);
        layout.setTag(R.id.tag_index, Integer.valueOf(mAddedLayout.getChildCount()));
        layout.setTag(R.id.tag_img_uri, imgUri);
//        layout.setTag(Integer.valueOf(mAddedLayout.getChildCount()));

        ImageView imgView = (ImageView) layout.findViewById(R.id.iv_item_add_image);
        imgView.setImageURI(imgUri);
        imgView.setTag(imgUri);

        ImageView deleteView = (ImageView) layout.findViewById(R.id.iv_item_add_image_delete);
        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout parent = (FrameLayout) v.getParent();
                mAddedLayout.removeViewAt((Integer) parent.getTag(R.id.tag_index));

                resetAddTag();
                mAddView.setVisibility(View.VISIBLE);
            }
        });

        mAddedLayout.addView(layout);
        if (mSubLayout.getVisibility() != View.VISIBLE)
            mSubLayout.setVisibility(View.VISIBLE);

        if (mAddedLayout.getChildCount() >= MAX_ADD_IMAGE_COUNT)
            mAddView.setVisibility(View.GONE);
    }

    private void resetAddTag() {
        int childCnt = mAddedLayout.getChildCount();
        for (int i = 0; i < childCnt; i++) {
            View view = mAddedLayout.getChildAt(i);
            view.setTag(R.id.tag_index, i);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_activity_posting_close:
                finish();
                break;
            case R.id.iv_activity_posting_thumb:
                mIsSelectAddImg = false;
                checkPermission();
                break;
            case R.id.iv_activity_posting_add:
                mIsSelectAddImg = true;
                checkPermission();
                break;
            case R.id.iv_activity_posting:
                if (checkValid()) {
                    requestPosting();
                }
                break;
            case R.id.ll_activity_posting_add_place:
                Intent intent = new Intent(PostingActivity.this, PlaceSearchActivity.class);
                startActivityForResult(intent, REQUEST_SEARCH_PLACE);
                break;
            case R.id.iv_activity_posting_place_delete:
                mLocation = "";
                mPlaceResultLayout.setVisibility(View.GONE);
                mAddPlaceView.setVisibility(View.VISIBLE);
                mPlaceDelView.setVisibility(View.GONE);
                break;
        }
    }

    private void checkPermission() {
        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage(R.string.permission_storage_retry)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            if (mIsSelectAddImg) {
                if (mAddedLayout.getChildCount() >= MAX_ADD_IMAGE_COUNT) {
                    Toast.makeText(PostingActivity.this, R.string.toast_cannot_add_image, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            pickFromGalley();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            App.toast(R.string.permission_storage_deny);
        }
    };

    private void pickFromGalley() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), REQUEST_SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PICTURE) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startCropActivity(selectedUri);
                } else {
                    Toast.makeText(this, R.string.toast_cannot_retrieve_selected_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            } else if (requestCode == REQUEST_SEARCH_PLACE) {
                DaumSearchItem item = (DaumSearchItem) data.getSerializableExtra("searchResult");
                setSearchResult(item);
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }

    private void setSearchResult(DaumSearchItem item) {
        String title = item.getTitle();
        String addr = item.getAddress();
        mLocation = item.getLatitude() + "," + item.getLongitude();

        mAddPlaceView.setVisibility(View.GONE);
        mPlaceResultLayout.setVisibility(View.VISIBLE);
        mPlaceTitleView.setText(title);
        mPlaceAddrView.setText(addr);
        mPlaceDelView.setVisibility(View.VISIBLE);
    }

    private void startCropActivity(@NonNull Uri uri) {
        String filePath = FileUtils.getPath(this, uri);
        String fileExt = filePath.substring(filePath.lastIndexOf(".") + 1);

        String fileName = Const.TAG + "_" + System.currentTimeMillis();
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), fileName + "." + fileExt)));
        uCrop = uCrop.withAspectRatio(1, 1);
        uCrop = advancedConfig(uCrop);
        uCrop.start(this);
    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
        options.setFreeStyleCropEnabled(false);
        return uCrop.withOptions(options);
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            saveCroppedImage(resultUri);
        } else {
            Toast.makeText(this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Toast.makeText(this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveCroppedImage(Uri imageUri) {
        if (imageUri != null && imageUri.getScheme().equals("file")) {
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                if (mIsSelectAddImg) {
                    addImageView(bm, imageUri);
                } else {
                    mThumbView.setImageURI(imageUri);
                    mThumbView.setTag(imageUri);
                    if (mSubLayout.getVisibility() != View.VISIBLE)
                        mSubLayout.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.toast_unexpected_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ViewUnbindHelper.unbindReferences(getWindow().getDecorView().getRootView());
    }

    //    private void copyFileToDownloads(Uri croppedFileUri) throws Exception {
//        String downloadsDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
//        String filename = String.format("%d_%s", Calendar.getInstance().getTimeInMillis(), croppedFileUri.getLastPathSegment());
//
//        File saveFile = new File(downloadsDirectoryPath, filename);
//
//        FileInputStream inStream = new FileInputStream(new File(croppedFileUri.getPath()));
//        FileOutputStream outStream = new FileOutputStream(saveFile);
//        FileChannel inChannel = inStream.getChannel();
//        FileChannel outChannel = outStream.getChannel();
//        inChannel.transferTo(0, inChannel.size(), outChannel);
//        inStream.close();
//        outStream.close();
//
//        showNotification(saveFile);
//    }

    private boolean checkValid() {
        String content = mContentEdit.getText().toString();
        if (content == null || content.trim().length() == 0) {
            App.toast(R.string.input_content);
            return false;
        }

        if (mThumbView.getTag() == null) {
            App.toast(R.string.input_thumbnail);
            return false;
        }

        return true;
    }

    private void requestPosting() {
        String placeName = "";
        if (mPlaceResultLayout.getVisibility() == View.VISIBLE) {
            placeName = mPlaceTitleView.getText().toString();
        }

        List<String> allHashTags = mTextHashTagHelper.getAllHashTags();

        Map<String, RequestBody> map = new HashMap<>();
        map.put("accessToken", DownloadUtils.toRequestBody(Preferences.getInstance(this).getAccessToken()));
        map.put("deviceId", DownloadUtils.toRequestBody(OsUtils.getDeviceHashId(this)));
        map.put("content", DownloadUtils.toRequestBody(mContentEdit.getText().toString()));
        map.put("tag", DownloadUtils.toRequestBody(allHashTags.toString()));
        map.put("permission", DownloadUtils.toRequestBody("" + (mSpinner.getSelectedItemPosition() + 1)));
        map.put("place_name", DownloadUtils.toRequestBody(placeName));
        map.put("place_position", DownloadUtils.toRequestBody(mLocation));

        Uri[] imgUris = new Uri[4];
        imgUris[0] = (Uri) mThumbView.getTag();

        int fileCnt = mAddedLayout.getChildCount();
        for (int i = 0; i < fileCnt; i++) {
            View view = mAddedLayout.getChildAt(i);
            imgUris[i + 1] = (Uri) view.getTag(R.id.tag_img_uri);
        }

        MultipartBody.Part body1 = DownloadUtils.prepareFilePart(this, "file1", imgUris[0]);
        MultipartBody.Part body2 = DownloadUtils.prepareFilePart(this, "file2", imgUris[1]);
        MultipartBody.Part body3 = DownloadUtils.prepareFilePart(this, "file3", imgUris[2]);
        MultipartBody.Part body4 = DownloadUtils.prepareFilePart(this, "file4", imgUris[3]);

        final Call<ResponseBody> posting = mService.posting(map, body1, body2, body3, body4);
        posting.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                closeSoftKeyboard();

                if (response == null) {
                    App.toast(R.string.response_fail);
                    return;
                }

                ResponseBody body = response.body();
                try {
                    String result = body.string();
                    BaseModel model = new Gson().fromJson(result, BaseModel.class);

                    if (model.getCode().equals("1")) {
                        App.toast(R.string.success_posting);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void closeSoftKeyboard() {
        // 키보드 닫기
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}
