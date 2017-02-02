package com.bb.igaming.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bb.igaming.Activity.MainActivity;
import com.bb.igaming.Model.Settings;
import com.bb.igaming.R;

/**
 * Created by bb on 1/9/2016.
 */
public class WecomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(
                R.layout.fragment_welcome, container, false);

        // board size
        view.findViewById(R.id.llBoard).setLayoutParams(
                new LinearLayout.LayoutParams((int) (Settings.scale * 794), (int) (Settings.scale * 759))
        );

        // set welcome text
        TextView tvWelcome = (TextView)view.findViewById(R.id.tvWelcome);
        String str = Settings.instance.resources.get("WELCOME");
        if (str != null) {
            tvWelcome.setText(Html.fromHtml(str));
        } else {
            tvWelcome.setText(Html.fromHtml(getResources().getString(R.string.welcome)));
        }

        // gogogo button
        TextView tvGogogo = (TextView)view.findViewById(R.id.tvGogogo);
        Settings.setString(tvGogogo, "GOGOGO", R.string.gogogo);
        tvGogogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).gogogo();
            }
        });

        return view;
    }
}
