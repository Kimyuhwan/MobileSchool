package com.example.MobileSchool.Activities.SchoolFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 13.
 * Time: 오후 7:19
 */

public class HistoryFragment extends Fragment {

    private String TAG = Constants.TAG;
    private String title = "History";

    public HistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history_fragment, container, false);

        getActivity().setTitle(title);
        return rootView;
    }

}
