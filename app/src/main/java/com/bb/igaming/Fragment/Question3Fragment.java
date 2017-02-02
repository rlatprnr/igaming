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
public class Question3Fragment extends QuestionFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState, false);

        setBoard(3, BoardSize.SMALL, getResources().getString(R.string.question3_title));
        setAnswers(AnswerManager.instance.noDepositYesNo);

        return view;
    }

    @Override
    public void gogogo() {
        Settings.instance.noDepositBonuses = _answers.get(0).selected ? 1 : 0;
        super.gogogo();
    }
}
