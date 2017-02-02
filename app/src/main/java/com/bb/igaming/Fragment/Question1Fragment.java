package com.bb.igaming.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bb.igaming.Model.AnswerManager;
import com.bb.igaming.Model.Settings;
import com.bb.igaming.R;

/**
 * Created by bb on 1/14/2016.
 */
public class Question1Fragment extends QuestionFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState, true);

        setBoard(1, BoardSize.SMALL, getResources().getString(R.string.question1_title));
        setAnswers(AnswerManager.instance.gameTypes);

        return view;
    }

    @Override
    public void gogogo() {

        Settings.instance.gameTypes.clear();
        for (AnswerManager.Answer answer : _answers) {
            if (answer.selected) {
                Settings.instance.gameTypes.add((String)answer.data);
            }
        }

        super.gogogo();
    }
}
