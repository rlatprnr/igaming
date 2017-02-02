package com.bb.igaming.Fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bb.igaming.Model.AnswerManager;
import com.bb.igaming.Model.Settings;
import com.bb.igaming.R;

/**
 * Created by bb on 1/14/2016.
 */
public class Question5Fragment extends QuestionFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState, true);

        setBoard(5, BoardSize.BIG, getResources().getString(R.string.question5_title));
        setAnswers(AnswerManager.instance.casinos);

        return view;
    }

    @Override
    protected void updatedAnswer() {
    }

    @Override
    public void gogogo() {

        Settings.instance.casinos.clear();
        for (AnswerManager.Answer answer : AnswerManager.instance.casinos) {
            if (answer.selected) {
                Settings.instance.casinos.add(answer.name);
            }
        }

        super.gogogo();
    }
}
