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
public class Question4Fragment extends QuestionFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState, false);

        setBoard(4, BoardSize.MEDIUM, getResources().getString(R.string.question4_title));
        setAnswers(AnswerManager.instance.depositKinds);

        return view;
    }

    @Override
    public void gogogo() {

        for (AnswerManager.Answer answer : _answers) {
            if (answer.selected) {
                Settings.instance.depositKind = (int)answer.data;
                break;
            }
        }

        super.gogogo();
    }
}
