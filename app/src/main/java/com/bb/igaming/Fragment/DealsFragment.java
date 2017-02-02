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
public class DealsFragment extends QuestionFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState, false);

        boolean isEnableLiveSupport = Settings.instance.isEnableLiveSupport();
        BoardSize bsize = (Settings.wide && isEnableLiveSupport) ? BoardSize.SMALL : BoardSize.MEDIUM;
        setBoard(-1, bsize, getResources().getString(R.string.question7_title));
        setAnswers(AnswerManager.instance.getOfferKinds(getResources()));

        return view;
    }

    @Override
    public void gogogo() {

        for (AnswerManager.Answer answer : _answers) {
            if (answer.selected) {
                Settings.instance.offerKind = (int)answer.data;
                break;
            }
        }

        AnswerManager.instance.offers.clear();

        super.gogogo();
    }
}
