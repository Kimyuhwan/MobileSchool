package com.example.MobileSchoolSeasonTwo.BroadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.example.MobileSchoolSeasonTwo.Fragment.GuideFragment;
import com.example.MobileSchoolSeasonTwo.R;
import com.example.MobileSchoolSeasonTwo.SchoolActivity;
import com.example.MobileSchoolSeasonTwo.Utils.GlobalApplication;
import com.example.MobileSchoolSeasonTwo.Utils.Constants;

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
        globalApplication = (GlobalApplication) context.getApplicationContext();
        if(intent.getAction().equals(Constants.PUSH_CUSTOM_NOTIFICATION_CONFIRM_EVENT)) {
            if(!_isStudying()) {
                Log.d(TAG, "NotificationClickBroadcastReceiver : confirm");
                globalApplication.setFragment("Guide", new GuideFragment());
                globalApplication.setDrawerType(R.array.Waiting_menu_array);
                globalApplication.setSession_type(Constants.CODE_SESSION_PASSIVE);

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

    private boolean _isStudying() {
        String currentFragmentName = globalApplication.getFragmentName();
        String[] studyingFragments = new String[]{"DialogueStudent","DialogueTeacher","Guide","Profile","Script"};
        for(String fragmentName : studyingFragments) {
            if(fragmentName.equals(currentFragmentName))
                return true;
        }
        return false;
    }
}
