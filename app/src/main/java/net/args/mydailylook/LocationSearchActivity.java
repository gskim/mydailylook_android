package net.args.mydailylook;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AddPlaceRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import net.args.mydailylook.adapter.LocationSearchAdapter;
import net.args.mydailylook.common.Const;
import net.args.mydailylook.utils.DevLog;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Administrator on 2016-08-07.
 */
public class LocationSearchActivity extends Activity {
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    private GoogleApiClient mGoogleApiClient;
    private LocationSearchAdapter mAdapter;
    private ListView mListView;
    private EditText mSearchEdit;
    private Button mOkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        setContentView(R.layout.activity_location_search);

        initLayout();
        initListener();

//        checkPermission();
        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new LocationSearchAdapter(this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY, null);
        mListView.setAdapter(mAdapter);
    }

    private void initLayout() {
        mListView = (ListView) findViewById(R.id.ll_activity_location_search);
        mSearchEdit = (EditText) findViewById(R.id.et_activity_location_search);
        mOkBtn = (Button) findViewById(R.id.btn_activity_location_search_ok);
    }

    private void initListener() {
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchWord = mSearchEdit.getText().toString();
                if (searchWord != null && searchWord.trim().length() > 0) {
//                    mAdapter.getFilter().filter(searchWord);
                    addLocation(searchWord);
                }
            }
        });
    }

    private void addLocation(String name) {
        AddPlaceRequest place =
                new AddPlaceRequest(
                        name, // Name
                        new LatLng(37.481942, 126.900311), // Latitude and longitude
                        "", // Address
                        Collections.singletonList(Place.TYPE_CAFE), // Place types
                        null, null // Website
                );

        Places.GeoDataApi.addPlace(mGoogleApiClient, place)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        DevLog.i(Const.TAG, "Place add result: " + places.getStatus().toString());
                        DevLog.i(Const.TAG, "Added place: " + places.get(0).getName().toString());
                        places.release();
                    }
                });
    }

    private void checkPermission() {
        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            getCurrentPlaces();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(LocationSearchActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    };

    private void getCurrentPlaces() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        try {
            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                    .getCurrentPlace(mGoogleApiClient, null);
            result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                        DevLog.i(Const.TAG, String.format("Place '%s' has likelihood: %g",
                                placeLikelihood.getPlace().getName(),
                                placeLikelihood.getLikelihood()));
                    }
                    likelyPlaces.release();
                }
            });
        } catch (Exception e) {
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }

}
