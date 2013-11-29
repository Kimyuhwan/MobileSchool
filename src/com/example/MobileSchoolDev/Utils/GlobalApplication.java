package com.example.MobileSchoolDev.Utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bugsense.trace.BugSenseHandler;
import com.example.MobileSchoolDev.Fragment.HomeFragment;
import com.example.MobileSchoolDev.Model.DialogueItem;
import com.example.MobileSchoolDev.Model.PartnerInfo;
import com.example.MobileSchoolDev.R;
import com.example.MobileSchoolDev.SchoolActivity;
import com.parse.Parse;
import com.parse.PushService;

import java.util.ArrayList;
import java.util.List;

/**
 * User: yuhwan
 * Date: 13. 10. 16
 * Time: 오후 11:35
 */
public class GlobalApplication extends Application {

    private String TAG = Constants.TAG;

    // Data Object
    private SchoolActivity schoolActivity;
    private AccountManager accountManager;
    private ContentManager contentManager;
    private CallRecorder callRecorder;
    private PartnerInfo partnerInfo;

    // Status Check Variable
    private boolean isSchoolActivityFront;
    private String fragmentName = null;
    private Fragment fragment = null;
    private int drawerType = -1;
    private int recallNumber = 0;
    private boolean isRecording = false;

    // Temporal storage
    private int waitingTime;
    private DialogueItem[] entryItems;

    // Class
    private String sender_id;
    private String session_id;
    private String session_type;
    private boolean session_connected;

    // Dialogue
    private List<DialogueItem> dialogueList;
    private DialogueItem[] rootItems;

    private AutoUpdateApk aua;

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, Constants.PARSE_APPLICATION_ID, Constants.PARSE_CLIENT_KEY);
        PushService.setDefaultPushCallback(this, SchoolActivity.class);

        if(accountManager == null)
            accountManager = new AccountManager(this);
        if(contentManager == null)
            contentManager = new ContentManager(this);
        if(callRecorder == null)
            callRecorder = new CallRecorder();
        if(dialogueList == null)
            dialogueList = new ArrayList<DialogueItem>();

        // Bug Sense
        BugSenseHandler.initAndStartSession(this, Constants.BUGSENSE_KEY);

        // Auto Update
        aua = new AutoUpdateApk(getApplicationContext());
        AutoUpdateApk.enableMobileUpdates();
        aua.setUpdateInterval(15 * AutoUpdateApk.MINUTES);
    }

    // Recording
    public boolean isRecording() {
        return isRecording;
    }

    public void setRecording(boolean recording) {
        isRecording = recording;
    }


    // Recall Number
    public int getRecallNumber() {
        return recallNumber;
    }

    public void setRecallNumber(int recallNumber) {
        this.recallNumber = recallNumber;
    }

    // Dialogue List
    public void addDialogue(DialogueItem item) {
        dialogueList.add(item);
    }

    public List<DialogueItem> getDialogueList() {
        return dialogueList;
    }

    public void initDialogueList() {
        dialogueList = new ArrayList<DialogueItem>();
    }

    // Data Object Functions
    public void setSchoolActivity(SchoolActivity schoolActivity) {
        this.schoolActivity = schoolActivity;
    }

    public SchoolActivity getSchoolActivity() {
        return schoolActivity;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public ContentManager getContentManager() {
        return contentManager;
    }

    public void setPartnerInfo(PartnerInfo partnerInfo) {
        this.partnerInfo = partnerInfo;
    }

    public PartnerInfo getPartnerInfo() {
        return this.partnerInfo;
    }


    // Status Check Functions
    public boolean isSchoolActivityFront() {
        return isSchoolActivityFront;
    }

    public void setSchoolActivityFront(boolean schoolActivityFront) {
        isSchoolActivityFront = schoolActivityFront;
    }

    public void setFragment(String fragmentName, Fragment fragment) {
        this.fragmentName = fragmentName;
        this.fragment = fragment;
    }

    public String getFragmentName() {
        return this.fragmentName;
    }

    public Fragment getFragment() {
        if(fragment == null)
            return new HomeFragment();
        else
            return fragment;
    }

    public int getFragmentPosition() {
        if(fragmentName == null)
            return 0;
        else
            return _getPosition(fragmentName);
    }

    private int _getPosition(String fragmentName) {
        Log.d(TAG, "Position fragmentName : " + fragmentName);
        String[] menu_array = getResources().getStringArray(getDrawerType());
        int index = 0;
        for(String menu : menu_array) {
            Log.d(TAG, "Position drawerType : " + menu);
            if(fragmentName.equals(menu))
                return index;
            index++;
        }
        return 0;
    }

    public void freeFragment() {
        fragmentName = null;
        fragment = null;
    }

    public void setDrawerType(int type) {
        this.drawerType = type;
    }

    public int getDrawerType() {
        if(drawerType == -1)
            return R.array.Home_menu_array;
        else
            return drawerType;
    }


    //  Temporal storage Functions
    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public DialogueItem[] getEntryItems() {
        return entryItems;
    }

    public void setEntryItems(DialogueItem[] entryItems) {
        this.entryItems = entryItems;
    }

    // Recorder
    public CallRecorder getCallRecorder() {
        return callRecorder;
    }

    public void setCallRecorder(CallRecorder callRecorder) {
        this.callRecorder = callRecorder;
    }

    // Font
    public void setAppFont(ViewGroup mContainer)
    {
        Typeface font = Typeface.createFromAsset(getAssets(), "Applemint.ttf");
        if (mContainer != null) {
            int mCount = mContainer.getChildCount();

            // Loop through all of the children.
            for (int i = 0; i < mCount; ++i)
            {
                View mChild = mContainer.getChildAt(i);
                if (mChild instanceof TextView)
                {
                    // Set the font if it is a TextView.
                    ((TextView) mChild).setTypeface(font);
                }
                else if (mChild instanceof ViewGroup)
                {
                    // Recursively attempt another ViewGroup.
                    setAppFont((ViewGroup) mChild);
                }
            }
        }
    }

    // Root Items
    public DialogueItem[] getRootItems() {
        return rootItems;
    }

    public void setRootItems(DialogueItem[] rootItems) {
        this.rootItems = rootItems;
    }

    // Parse
   public void addSubscribe() {
       PushService.subscribe(this, accountManager.getUniqueId(), SchoolActivity.class);
   }

   public void removeSubscribe() {
       PushService.unsubscribe(this, accountManager.getMyInfo().getUnique_id());
   }

    //Version check
   public boolean isValidVersion() {
       SharedPreferences sharedPreferences = getSharedPreferences(Constants.TAG, 0);
       try {
           PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
           String currentVersion = pInfo.versionName;
           String localVersion = sharedPreferences.getString("version", Constants.SHAREDPREFERENCES_EMPTY);
           Log.d(TAG, "Version check : " + localVersion + "\t" + currentVersion);
           if(currentVersion.equals(localVersion))
               return true;
           else
               return false;
       } catch (PackageManager.NameNotFoundException e) { e.printStackTrace(); }
       return false;
   }

   public void setVersion() {
       try {
           PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
           String currentVersion = pInfo.versionName;
           SharedPreferences sharedPreferences = getSharedPreferences(Constants.TAG, 0);
           SharedPreferences.Editor editor = sharedPreferences.edit();
           editor.putString("version", currentVersion);
           editor.commit();
       } catch (PackageManager.NameNotFoundException e) { e.printStackTrace(); }
   }


   // Session

   public String getSender_id() {
        return sender_id;
   }

   public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
   }

   public String getSession_id() {
       return session_id;
   }

   public void setSession_id(String session_id) {
        this.session_id = session_id;
   }

   public String getSession_type() {
        return session_type;
   }

   public void setSession_type(String session_type) {
        this.session_type = session_type;
   }

   public boolean isSession_connected() {
        return session_connected;
   }

   public void setSession_connected(boolean session_connected) {
        this.session_connected = session_connected;
   }


}
