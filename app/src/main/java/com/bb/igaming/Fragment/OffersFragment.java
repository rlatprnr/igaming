package com.bb.igaming.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bb.igaming.Model.AnswerManager;
import com.bb.igaming.Model.Settings;
import com.bb.igaming.R;
import com.bb.igaming.Util.ImageLoader;

import java.util.ArrayList;

/**
 * Created by bb on 1/14/2016.
 */
public class OffersFragment extends Fragment {

    private View _view;
    private int _offerKind;
    private ArrayList<AnswerManager.Offer> _offers;
    private LinearLayout _popup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        _view = inflater.inflate(
                R.layout.fragment_offer, container, false);

        ((LinearLayout)_view.findViewById(R.id.llContainer)).setLayoutParams(
                new LinearLayout.LayoutParams((int) (Settings.scale * 1026), ViewGroup.LayoutParams.MATCH_PARENT)
        );

        return _view;
    }

    public void initFragment() {

        _offerKind = Settings.instance.offerKind;

        LinearLayout llHead = (LinearLayout)_view.findViewById(R.id.llHead);
        llHead.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Settings.scale * 269))
        );

        TextView tvTitle = (TextView)_view.findViewById(R.id.tvTitle);
        TextView tvHead1 = (TextView)_view.findViewById(R.id.tvHead1);
        TextView tvHead2 = (TextView)_view.findViewById(R.id.tvHead2);
        TextView tvHead3 = (TextView)_view.findViewById(R.id.tvHead3);
        TextView tvHead4 = (TextView)_view.findViewById(R.id.tvHead4);
        if (_offerKind == AnswerManager.DEALS_FREE_SPINS) {
            Settings.setString(tvTitle, "O1_TITLE", R.string.free_spins_offers_title);
            Settings.setString(tvHead1, "O_AMOUNT_OF_SPINS", R.string.amount_of_sping);
            Settings.setString(tvHead2, "O_WAG_REQ", R.string.wag_requ);
            Settings.setString(tvHead3, "O_RESTRI_SLOTS", R.string.rest_slots);
        } else if (_offerKind == AnswerManager.DEALS_NO_DEPOSIT) {
            Settings.setString(tvTitle, "O2_TITLE", R.string.no_deposit_offers_title);
            Settings.setString(tvHead1, "O_AMOUNT", R.string.amount);
            Settings.setString(tvHead2, "O_WAG_REQ", R.string.wag_requ);
            Settings.setString(tvHead3, "O_RESTRI_GAMES", R.string.rest_games);
        } else {
            if (_offerKind == AnswerManager.DEALS_1DEPOSIT) {
                Settings.setString(tvTitle, "O3_TITLE", R.string.deposit_offers_title);
            } else {
                Settings.setString(tvTitle, "O4_TITLE", R.string.vip_offers_title);
            }
            llHead.setBackgroundResource(R.drawable.offer_hear_bg1);
            tvHead1.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 201));
            tvHead2.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 225));
            tvHead3.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0));
            tvHead4.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 228));
            Settings.setString(tvHead1, "O_PERCENT", R.string.percent);
            Settings.setString(tvHead2, "O_UP_TO", R.string.up_to);
        }

        Settings.setString(tvHead4, "O_T_CS", R.string.t_cs);
        Settings.setString((TextView)_view.findViewById(R.id.tvHead5), "O_CASINO", R.string.casino);
        Settings.setString((TextView)_view.findViewById(R.id.tvHead6), "O_GO", R.string.go);

        _popup = (LinearLayout)_view.findViewById(R.id.flPopup);
        _popup.setVisibility(View.INVISIBLE);
        _popup.findViewById(R.id.tvClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _popup.setVisibility(View.INVISIBLE);
            }
        });
        ((TextView) _popup.findViewById(R.id.tvContent)).setMovementMethod(new ScrollingMovementMethod());
        _view.findViewById(R.id.llBoard).setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Settings.scale * 926))
        );

        final View flLoading = _view.findViewById(R.id.flLoading);
        flLoading.setVisibility(View.VISIBLE);

        AnswerManager.instance.getOffers(new AnswerManager.GetOfferCallBack() {
            @Override
            public void done(ArrayList<AnswerManager.Offer> offers) {
                flLoading.setVisibility(View.INVISIBLE);

                _offers = offers;

                ListView listView = (ListView) _view.findViewById(R.id.lvOffer);
                OffersAdapter offersAdapter = new OffersAdapter(getActivity());
                listView.setAdapter(offersAdapter);
            }
        });
    }

    public class OffersAdapter extends BaseAdapter {

        private LayoutInflater _layoutInflater = null;

        public OffersAdapter(Activity context) {
            _layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return _offers.size();
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

                convertView = _layoutInflater.inflate(R.layout.cell_offer, null);

                LinearLayout llCell = (LinearLayout)convertView.findViewById(R.id.llCell);
                TextView tvField1 = (TextView)convertView.findViewById(R.id.tvField1);
                TextView tvField2 = (TextView)convertView.findViewById(R.id.tvField2);
                TextView tvField3 = (TextView)convertView.findViewById(R.id.tvField3);
                TextView tvField4 = (TextView)convertView.findViewById(R.id.tvField4);
                if (_offerKind == AnswerManager.DEALS_1DEPOSIT || _offerKind == AnswerManager.DEALS_VIP_BONUS) {
                    llCell.setBackgroundResource(R.drawable.offer_cell_bg1);
                    tvField1.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 201));
                    tvField2.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 225));
                    tvField3.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0));
                    tvField4.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 228));
                }

                llCell.setLayoutParams(
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Settings.scale * 146))
                );

                OfferHolder offerHolder = new OfferHolder(convertView);
                convertView.setTag(offerHolder);
            }

            OfferHolder offerHolder = (OfferHolder)convertView.getTag();
            offerHolder.setOffer(_offers.get(position));

            return convertView;
        }

        public class OfferHolder {

            private TextView _tvField1;
            private TextView _tvField2;
            private TextView _tvField3;
            private ImageView _tvField5;
            private AnswerManager.Offer _offer;

            public OfferHolder(View view) {
                _tvField1 = (TextView)view.findViewById(R.id.tvField1);
                _tvField2 = (TextView)view.findViewById(R.id.tvField2);
                _tvField3 = (TextView)view.findViewById(R.id.tvField3);
                _tvField5 = (ImageView)view.findViewById(R.id.ivField5);

                ((TextView)view.findViewById(R.id.tvField4)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _popup.setVisibility(View.VISIBLE);
                        TextView textView = (TextView) _popup.findViewById(R.id.tvContent);
                        textView.setText(Html.fromHtml(_offer.tandc));
                    }
                });

                TextView tvField6 = (TextView)view.findViewById(R.id.tvField6);
                tvField6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(_offer.link));
                        startActivity(browserIntent);
                    }
                });
            }

            public void setOffer(AnswerManager.Offer offer) {
                _offer = offer;

                if (_offerKind == AnswerManager.DEALS_FREE_SPINS) {
                    _tvField1.setText(offer.spins);
                    _tvField2.setText(offer.wager);
                    _tvField3.setText(offer.restrictedslots);
                } else if (_offerKind == AnswerManager.DEALS_NO_DEPOSIT) {
                    _tvField1.setText(offer.nodepositamount);
                    _tvField2.setText(offer.wager);
                    _tvField3.setText(offer.restrictedgames);
                } else {
                    _tvField1.setText(offer.percent);
                    _tvField2.setText(offer.upto);
                }

                ImageLoader imageLoader = new ImageLoader(_layoutInflater.getContext());
                imageLoader.DisplayImage(offer.img, _tvField5);
            }
        }
    }
}
