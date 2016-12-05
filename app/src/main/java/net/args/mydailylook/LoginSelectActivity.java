package net.args.mydailylook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by arseon on 2016-08-26.
 */
public class LoginSelectActivity extends Activity implements View.OnClickListener {
    private Button mLoginBtn;
    private Button mJoinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_select);

        mLoginBtn = (Button) findViewById(R.id.btn_activity_login_select_login);
        mJoinBtn = (Button) findViewById(R.id.btn_activity_login_select_join);

        mLoginBtn.setOnClickListener(this);
        mJoinBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity_login_select_login:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_activity_login_select_join:
                Intent joinIntent = new Intent(this, JoinActivity.class);
                startActivity(joinIntent);
//                Intent joinIntent = new Intent(this, ProfileInputActivity.class);
//                startActivity(joinIntent);
                break;
        }
    }

}
