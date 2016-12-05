package net.args.mydailylook;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016-06-29.
 */
public class TermsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.6f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_terms);
    }
}
