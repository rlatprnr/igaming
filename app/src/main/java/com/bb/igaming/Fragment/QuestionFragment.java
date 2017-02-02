package com.bb.igaming.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bb.igaming.Activity.MainActivity;
import com.bb.igaming.Model.AnswerManager;
import com.bb.igaming.Model.Settings;
import com.bb.igaming.R;

import java.util.ArrayList;

/**
 * Created by bb on 1/9/2016.
 */
public class QuestionFragment extends Fragment {

    protected enum BoardSize {SMALL, MEDIUM, BIG};

    protected View _view;
    protected TextView _tvGogogo;

    private AnswersAdapter.AnswerHolder _selectedHolder = null;
    private AnswerManager.Answer _selectedAnswer = null;

    protected boolean _enabledMultiSelect;
    protected ArrayList<AnswerManager.Answer> _answers = new ArrayList<AnswerManager.Answer>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState,
                             boolean enabledMultiSelect) {

        _view = inflater.inflate(
                R.layout.fragment_question, container, false);

        _enabledMultiSelect = enabledMultiSelect;

        // gogogo button
        _tvGogogo = (TextView)_view.findViewById(R.id.tvGogogo);
        Settings.setString(_tvGogogo, "GOGOGO", R.string.gogogo);
        _tvGogogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gogogo();
            }
        });

        ListView listView = (ListView)_view.findViewById(R.id.lvAnswer);
        AnswersAdapter answersAdapter = new AnswersAdapter(getActivity());
        listView.setAdapter(answersAdapter);

        return _view;
    }

    public void setBoard(int questionNo, BoardSize boardSize, String title) {

        // set board kind
        @DrawableRes int resId = R.drawable.ql_board;
        float height = 1316;
        if (boardSize == BoardSize.SMALL) {
            resId = R.drawable.qs_board;
            height =  1026;
        } else if (Settings.instance.wide || boardSize == BoardSize.MEDIUM) {
            resId = R.drawable.qm_board;
            height =  1086;
        }

        // set board size
        FrameLayout flBoard = (FrameLayout) _view.findViewById(R.id.flBoard);
        flBoard.setBackgroundResource(resId);
        flBoard.setLayoutParams(
                new LinearLayout.LayoutParams((int) (Settings.instance.scale * 794), (int) (Settings.instance.scale * height))
        );
        _view.findViewById(R.id.llContainer).setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Settings.instance.scale * (height - 552)))
        );

        // set number
        String str = getResources().getString(R.string.q) + (questionNo != -1 ? String.valueOf(questionNo) : "");
        ((TextView)_view.findViewById(R.id.tvNo)).setText(str);

        // set title
        str = Settings.instance.resources.get("Q"+(questionNo != -1 ? String.valueOf(questionNo) : "")+"_TITLE");
        if (str == null) {
            str = title;
        }
        ((TextView) _view.findViewById(R.id.tvTitle)).setText(str);

    }

    public void setAnswers(ArrayList<AnswerManager.Answer> answers) {
        _answers = answers;
        if (!_enabledMultiSelect) {
            for (AnswerManager.Answer answer : _answers) {
                if (answer.selected) {
                    _selectedAnswer = answer;
                    break;
                }
            }
        }
        updatedAnswer();
    }

    public void gogogo() {
        ((MainActivity) getActivity()).gogogo();
    }

    protected void updatedAnswer() {
        _tvGogogo.setEnabled(false);
        _tvGogogo.setTextColor(ContextCompat.getColor(getContext(), R.color.gogogoDisableColor));
        for (AnswerManager.Answer answer : _answers) {
            if (answer.selected) {
                _tvGogogo.setEnabled(true);
                _tvGogogo.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                break;
            }
        }
    }

    public class AnswersAdapter extends BaseAdapter {

        private LayoutInflater _layoutInflater;

        public AnswersAdapter(Activity context) {
            _layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return _answers.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = _layoutInflater.inflate(R.layout.cell_answer, null);
                AnswerHolder answerHolder = new AnswerHolder(convertView);
                convertView.setTag(answerHolder);
            }

            AnswerManager.Answer answer = _answers.get(position);
            AnswerHolder answerHolder = (AnswerHolder)convertView.getTag();
            answerHolder.setAnswer(answer);

            return convertView;
        }

        public class AnswerHolder {

            private AnswerManager.Answer _answer;
            private CheckBox _checkBox;
            private TextView _textView;

            public AnswerHolder(View view) {

                _checkBox = (CheckBox)view.findViewById(R.id.checkBox);

                _checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        changedState(isChecked);
                    }
                });

                _textView = (TextView)view.findViewById(R.id.tvLabel);
                _textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _checkBox.toggle();
                    }
                });
            }

            private void changedState(boolean isChecked) {

                if (!_enabledMultiSelect) {
                    if (isChecked) {
                        AnswerHolder oldSelectedHolder = _selectedHolder;
                        AnswerManager.Answer oldSelectedAnswer = _selectedAnswer;
                        _selectedAnswer = _answer;
                        _selectedHolder = this;
                        if (oldSelectedHolder != null && oldSelectedHolder._answer == oldSelectedAnswer) {
                            oldSelectedHolder._checkBox.setChecked(false);
                        }
                        if (oldSelectedAnswer != null) {
                            oldSelectedAnswer.selected = false;
                        }
                    } else {
                        if (_selectedAnswer == _answer) {
                            _checkBox.toggle();
                            return;
                        }
                    }
                }

                _checkBox.setBackgroundResource(isChecked ? R.drawable.checked : R.drawable.unchecked);
                _answer.selected = isChecked;
                updatedAnswer();
            }

            public void setAnswer(AnswerManager.Answer answer) {
                _answer = answer;
                _checkBox.setChecked(answer.selected);
                _textView.setText(answer.name);
            }
        }
    }
}
