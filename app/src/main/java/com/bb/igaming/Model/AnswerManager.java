package com.bb.igaming.Model;

import android.content.Context;
import android.content.res.Resources;

import com.bb.igaming.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 1/14/2016.
 */
public class AnswerManager {

    // game kind
    public static final String GAMETYPE_SLOTS = "S";
    public static final String GAMETYPE_BLACKJACK = "B";
    public static final String GAMETYPE_ROULETE = "R";
    public static final String GAMETYPE_VIDEOPOKER = "P";
    public static final String GAMETYPE_OTHER = "O";

    public static final int DEPOSIT_0 = 0;
    public static final int DEPOSIT_1_100 = 1;
    public static final int DEPOSIT_101_500 = 2;
    public static final int DEPOSIT_501_1000 = 3;
    public static final int DEPOSIT_1001_5000 = 4;
    public static final int DEPOSIT_5001_ = 5;

    public static final int DEALS_FREE_SPINS = 0;
    public static final int DEALS_NO_DEPOSIT = 1;
    public static final int DEALS_1DEPOSIT = 2;
    public static final int DEALS_VIP_BONUS = 3;
    public static final String[] TABLE_DEALS = {"FreeSpins", "NoDeposit", "FirstDeposit", "VIPBonus"};

    public ArrayList<Answer> gameTypes = new ArrayList<Answer>();
    public ArrayList<Answer> freeSpinsYesNo = new ArrayList<Answer>();
    public ArrayList<Answer> noDepositYesNo = new ArrayList<Answer>();
    public ArrayList<Answer> depositKinds = new ArrayList<Answer>();
    public ArrayList<Answer> casinos;
    public ArrayList<Answer> countries;
    public ArrayList<Offer> offers = new ArrayList<Offer>();

    public static AnswerManager instance = new AnswerManager();

    public void init(final Context context, final CallBack callBack) {

        Settings.instance.load(context);
        Resources resources = context.getResources();


        instance.gameTypes.add(new Answer(Settings.getString("Q1_A1", resources,R.string.game_slots), GAMETYPE_SLOTS, Settings.instance.gameTypes.indexOf(GAMETYPE_SLOTS)!=-1));
        instance.gameTypes.add(new Answer(Settings.getString("Q1_A2", resources,R.string.game_blackjack), GAMETYPE_BLACKJACK, Settings.instance.gameTypes.indexOf(GAMETYPE_BLACKJACK)!=-1));
        instance.gameTypes.add(new Answer(Settings.getString("Q1_A3", resources,R.string.game_roulete), GAMETYPE_ROULETE, Settings.instance.gameTypes.indexOf(GAMETYPE_ROULETE)!=-1));
        instance.gameTypes.add(new Answer(Settings.getString("Q1_A4", resources,R.string.game_videopoker), GAMETYPE_VIDEOPOKER, Settings.instance.gameTypes.indexOf(GAMETYPE_VIDEOPOKER)!=-1));
        instance.gameTypes.add(new Answer(Settings.getString("Q1_A5", resources,R.string.game_other), GAMETYPE_OTHER, Settings.instance.gameTypes.indexOf(GAMETYPE_OTHER)!=-1));

        instance.freeSpinsYesNo.add(new Answer(Settings.getString("YES", resources,R.string.yes), 1, Settings.instance.freeSpins==1));
        instance.freeSpinsYesNo.add(new Answer(Settings.getString("NO", resources,R.string.no), 0, Settings.instance.freeSpins==0));

        instance.noDepositYesNo.add(new Answer(Settings.getString("YES", resources,R.string.yes), 1, Settings.instance.noDepositBonuses==1));
        instance.noDepositYesNo.add(new Answer(Settings.getString("NO", resources,R.string.no), 0, Settings.instance.noDepositBonuses==0));

        instance.depositKinds.add(new Answer(Settings.getString("Q4_A1", resources,R.string.deposit_0), DEPOSIT_0, Settings.instance.depositKind==DEPOSIT_0));
        instance.depositKinds.add(new Answer(Settings.getString("Q4_A2", resources,R.string.deposit_1_100), DEPOSIT_1_100, Settings.instance.depositKind==DEPOSIT_1_100));
        instance.depositKinds.add(new Answer(Settings.getString("Q4_A3", resources,R.string.deposit_101_500), DEPOSIT_101_500, Settings.instance.depositKind==DEPOSIT_101_500));
        instance.depositKinds.add(new Answer(Settings.getString("Q4_A4", resources,R.string.deposit_501_1000), DEPOSIT_501_1000, Settings.instance.depositKind==DEPOSIT_501_1000));
        instance.depositKinds.add(new Answer(Settings.getString("Q4_A5", resources,R.string.deposit_1001_5000), DEPOSIT_1001_5000, Settings.instance.depositKind==DEPOSIT_1001_5000));
        instance.depositKinds.add(new Answer(Settings.getString("Q4_A6", resources,R.string.deposit_5000_), DEPOSIT_5001_, Settings.instance.depositKind==DEPOSIT_5001_));

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Casinos");
        query.addAscendingOrder("casino");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                instance.casinos = new ArrayList<Answer>();
                if (e == null) {
                    for (ParseObject object : objects) {
                        String casino = object.getString("casino");
                        instance.casinos.add(new Answer(casino, object, Settings.instance.casinos.indexOf(casino)!=-1));
                    }
                }
                if (instance.countries != null) callBack.done();
            }
        });

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Country");
        query1.addDescendingOrder("order");
        query1.addAscendingOrder("name");
        query1.setLimit(1000);
        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                instance.countries = new ArrayList<Answer>();
                if (e == null) {
                    for (ParseObject object : objects) {
                        String cid = object.getString("cid");
                        instance.countries.add(new Answer(object.getString("name"), cid, cid.equals(Settings.instance.country)));
                    }
                }
                if (instance.casinos != null) callBack.done();
            }
        });
    }

    public ArrayList<Answer> getOfferKinds(Resources resources) {

        ArrayList<Answer> offerKinds = new ArrayList<Answer>();
        if (Settings.instance.freeSpins == 1) {
            offerKinds.add(new Answer(Settings.getString("Q_A1", resources,R.string.free_spins), DEALS_FREE_SPINS));
        }
        if (Settings.instance.noDepositBonuses == 1) {
            offerKinds.add(new Answer(Settings.getString("Q_A2", resources,R.string.no_deposit_bonus), DEALS_NO_DEPOSIT));
        }
        offerKinds.add(new Answer(Settings.getString("Q_A3", resources,R.string.deposit_bonus), DEALS_1DEPOSIT));
        if (Settings.instance.depositKind >= DEPOSIT_501_1000) {
            offerKinds.add(new Answer(Settings.getString("Q_A4", resources,R.string.vip_bonus), DEALS_VIP_BONUS));
        }
        return offerKinds;
    }

    public void getOffers(final GetOfferCallBack callBack) {

        ArrayList<String> casinos = new ArrayList<String>();
        for (AnswerManager.Answer answer : AnswerManager.instance.casinos) {
            if (answer.selected == false) {
                String geos = ((ParseObject)answer.data).getString("geos");
                if (geos.contains(Settings.instance.country) == false) {
                    casinos.add(answer.name);
                }
            }
        }

        String table = TABLE_DEALS[Settings.instance.offerKind];
        ParseQuery<ParseObject> query = ParseQuery.getQuery(table);
        query.whereContainedIn("casino", casinos);
        query.addAscendingOrder("casino");
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    int offerKind = Settings.instance.offerKind;
                    for (ParseObject object : objects) {
                        Offer offer = new Offer();
                        if (offerKind == DEALS_FREE_SPINS) {
                            offer.spins = String.valueOf(object.getInt("spins"));
                            offer.wager = String.valueOf(object.getInt("wager")) + "X";
                            offer.restrictedslots = object.getBoolean("restrictedslots") ? "Yes" : "No";
                        } else if (offerKind == DEALS_NO_DEPOSIT) {
                            offer.nodepositamount = String.valueOf(object.getInt("nodepositamount"));
                            offer.wager = String.valueOf(object.getInt("wager")) + "X";
                            offer.restrictedgames = object.getBoolean("restrictedgames") ? "Yes" : "No";
                        } else {
                            offer.percent = String.valueOf(object.getInt("percent"));
                            offer.upto = "$" + String.valueOf(object.getInt("upto"));
                        }

                        String tandc = object.getString("tandc");
                        offer.tandc = "<u><h3>Terms & Conditions</h3></u><br>" + (tandc != null ? tandc : "");
                        offer.img = getCasinoImg(object.getString("casino"));
                        offer.link = object.getString("link");
                        offers.add(offer);
                    }
                }
                callBack.done(offers);
            }
        });
    }

    private String getCasinoImg(String casino) {
        for (Answer answer : casinos) {
            if (answer.name.equals(casino)) {
                ParseObject parseObject = (ParseObject)answer.data;
                return parseObject.getString("img");
            }
        }
        return "";
    }

    public class Answer {
        public String name;
        public Object data;
        public boolean selected;

        public Answer(String name, Object data) {
            this(name, data, false);
        }

        public Answer(String name, Object data, boolean selected) {
            this.name = name;
            this.data = data;
            this.selected = selected;
        }
    }

    public class Offer {
        public String casino;
        public String tandc;
        public String img;
        public String link;
        public String spins;
        public String nodepositamount;
        public String wager;
        public String restrictedslots;
        public String restrictedgames;
        public String percent;
        public String upto;
    }


    public interface CallBack {
        void done();
    }

    public interface GetOfferCallBack {
        void done(ArrayList<Offer> offers);
    }

}
