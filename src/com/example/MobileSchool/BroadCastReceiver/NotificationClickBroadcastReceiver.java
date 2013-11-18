package com.example.MobileSchool.BroadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.example.MobileSchool.Activities.EntryActivity;
import com.example.MobileSchool.Activities.IntroActivity;
import com.example.MobileSchool.Fragment.GuideFragment;
import com.example.MobileSchool.R;
import com.example.MobileSchool.SchoolActivity;
import com.example.MobileSchool.Utils.GlobalApplication;
import com.example.MobileSchool.Utils.Constants;

/**
 * User: yuhwan
 * Date: 13. 10. 17
 * Time: 오후 4:04
 */
public class NotificationClickBroadcastReceiver extends BroadcastReceiver {
    private String TAG = Constants.TAG;

    private GlobalApplication globalApplication;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Constants.PUSH_CUSTOM_NOTIFICATION_CONFIRM_EVENT)) {
            Log.d(TAG, "NotificationClickBroadcastReceiver : confirm");

            globalApplication = (GlobalApplication) context.getApplicationContext();
            globalApplication.setFragment("Guide", new GuideFragment());
            globalApplication.setDrawerType(R.array.Waiting_menu_array);

            if(globalApplication.isSchoolActivityFront()) {
                globalApplication.getSchoolActivity().initDrawer();
                globalApplication.getSchoolActivity().initFragment();
            } else {
                if(globalApplication.getSchoolActivity() != null)
                    globalApplication.getSchoolActivity().finish();
                Intent guideIntent = new Intent(context, SchoolActivity.class);
                guideIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(guideIntent);
            }
        }
    }
}
