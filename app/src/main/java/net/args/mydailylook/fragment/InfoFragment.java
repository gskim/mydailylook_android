package net.args.mydailylook.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.util.FileUtils;

import net.args.mydailylook.App;
import net.args.mydailylook.R;
import net.args.mydailylook.SettingActivity;
import net.args.mydailylook.adapter.InfoAdapter;
import net.args.mydailylook.common.Const;
import net.args.mydailylook.common.Preferences;
import net.args.mydailylook.model.BaseModel;
import net.args.mydailylook.model.PersonalInfo;
import net.args.mydailylook.model.PostingList;
import net.args.mydailylook.model.ProfileImage;
import net.args.mydailylook.network.MyDailyLookService;
import net.args.mydailylook.network.ServiceGenerator;
import net.args.mydailylook.utils.DevLog;
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
 * Created by Administrator on 2016-10-02.
 */
public class InfoFragment extends Fragment {
    private static final int REQUEST_SELECT_PICTURE = 100;

    private View mView;
    private RecyclerView mRecyclerView;
    private InfoAdapter mAdapter;
    private MyDailyLookService mService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_info, container, false);
        mService = ServiceGenerator.getService();

        initLayout();
        initListener();
        requestInfo();
        requestPostingList();
        DevLog.i(Const.TAG, "[InfoFragment] onCreateView() ===================");
        return mView;
    }

    private View findViewById(int resId) {
        return mView.findViewById(resId);
    }

    private void initLayout() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_fragment_info_gallery);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) return 3;
                return 1;
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void initListener() {
        mAdapter = new InfoAdapter(getActivity(), null);
        mAdapter.setOnInfoListener(new InfoAdapter.OnInfoListener() {
            @Override
            public void onProfileImgClick() {
                checkPermission();
            }

            @Override
            public void onSettingClick() {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void requestInfo() {
        final Call<PersonalInfo> info = mService.info(Preferences.getInstance(getActivity()).getAccessToken()
                , OsUtils.getDeviceHashId(getActivity()), "");

        info.enqueue(new Callback<PersonalInfo>() {
            @Override
            public void onResponse(Call<PersonalInfo> call, Response<PersonalInfo> response) {
                if (response == null || response.body() == null) {
                    App.toast(R.string.response_fail);
                    return;
                }

                PersonalInfo info = response.body();
                if (mAdapter != null)
                    mAdapter.setPersonalInfo(info);
            }

            @Override
            public void onFailure(Call<PersonalInfo> call, Throwable t) {
                App.toast(R.string.response_fail);
            }
        });
    }

    private void requestPostingList() {
        final Call<PostingList> postingList = mService.postingList(Preferences.getInstance(getActivity()).getAccessToken()
                , OsUtils.getDeviceHashId(getActivity()), "");

        postingList.enqueue(new Callback<PostingList>() {
            @Override
            public void onResponse(Call<PostingList> call, Response<PostingList> response) {
                if (response == null || response.body() == null) {
                    App.toast(R.string.response_fail);
                    return;
                }

                PostingList postingList = response.body();
                if (mAdapter != null)
                    mAdapter.setPostingList(postingList.getData());
            }

            @Override
            public void onFailure(Call<PostingList> call, Throwable t) {
                App.toast(R.string.response_fail);
            }
        });
    }

    private void checkPermission() {
        new TedPermission(getActivity())
                .setPermissionListener(permissionlistener)
                .setDeniedMessage(R.string.permission_storage_retry)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
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

    private void requestProfileImg(final Uri imgUri) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("accessToken", DownloadUtils.toRequestBody(Preferences.getInstance(getActivity()).getAccessToken()));
        map.put("deviceId", DownloadUtils.toRequestBody(OsUtils.getDeviceHashId(getActivity())));
        MultipartBody.Part body1 = DownloadUtils.prepareFilePart(getActivity(), "profile", imgUri);

        final Call<ResponseBody> profileImg = mService.profileImg(map, body1);
        profileImg.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response == null) {
                    App.toast(R.string.response_fail);
                    return;
                }

                ResponseBody body = response.body();
                try {
                    String result = body.string();
                    ProfileImage model = new Gson().fromJson(result, ProfileImage.class);

//                    if (model.getCode().equals("1")) {
                    saveCroppedImage(imgUri, model.getImgId());
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                App.toast(R.string.response_fail);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PICTURE) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startCropActivity(selectedUri);
                } else {
                    App.toast(R.string.toast_cannot_retrieve_selected_image);
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }

    private void startCropActivity(@NonNull Uri uri) {
        String filePath = FileUtils.getPath(getActivity(), uri);
        String fileExt = filePath.substring(filePath.lastIndexOf(".") + 1);

        String fileName = Const.TAG + "_" + System.currentTimeMillis();
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getActivity().getCacheDir(), fileName + "." + fileExt)));
        uCrop = uCrop.withAspectRatio(1, 1);
        uCrop = advancedConfig(uCrop);
        uCrop.start(getActivity());
    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
        options.setFreeStyleCropEnabled(false);
        return uCrop.withOptions(options);
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
//            saveCroppedImage(resultUri);
            requestProfileImg(resultUri);

        } else {
            App.toast(R.string.toast_cannot_retrieve_cropped_image);
        }
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            App.toast(cropError.getMessage());
        } else {
            App.toast(R.string.toast_unexpected_error);
        }
    }

    private void saveCroppedImage(Uri imageUri, String imgId) {
        if (imageUri != null && imageUri.getScheme().equals("file")) {
            mAdapter.setProfileImg(
                    (InfoAdapter.ViewHolder) mRecyclerView.findViewHolderForAdapterPosition(0), imgId);
        } else {
            App.toast(R.string.toast_unexpected_error);
        }
    }

}
