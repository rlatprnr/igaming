package com.bb.igaming.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bb.igaming.Activity.MainActivity;
import com.bb.igaming.Model.AnswerManager;
import com.bb.igaming.Model.Settings;
import com.bb.igaming.R;

/**
 * Created by bb on 1/14/2016.
 */
public class Question6Fragment extends QuestionFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState, false);

        setBoard(6, BoardSize.BIG, getResources().getString(R.string.question6_title));
        setAnswers(AnswerManager.instance.countries);

        return view;
    }

    @Override
    public void gogogo() {

        for (AnswerManager.Answer answer : _answers) {
            if (answer.selected) {
                Settings.instance.country = (String)answer.data;
                break;
            }
        }

        Settings.instance.save(getContext());
        ((MainActivity) getActivity()).showDeals();
    }
}
