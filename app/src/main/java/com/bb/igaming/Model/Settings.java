package com.bb.igaming.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created by bb on 1/21/2016.
 */
public class Settings {

    public static Settings instance = new Settings();

    public static float scale;
    public static boolean wide;

    public Dictionary<String, String> resources = new Hashtable<String, String>();

    public ArrayList<String> gameTypes;
    public int freeSpins;
    public int noDepositBonuses;
    public int depositKind;
    public ArrayList<String> casinos;
    public String country;
    public int offerKind;

    public boolean isEnableLiveSupport() {
        return Settings.instance.depositKind >= AnswerManager.DEPOSIT_1001_5000;
    }

    public void load(Context context) {
        SharedPreferences sharedPref =  context.getSharedPreferences("iGaming", Context.MODE_PRIVATE);
        String strGameTypes = sharedPref.getString("gameTypes", null);
        if (strGameTypes != null) {
            gameTypes = new ArrayList<String>(Arrays.asList(strGameTypes.split("%%")));
        } else {
            gameTypes = new ArrayList<String>();
        }

        freeSpins = sharedPref.getInt("freeSpins", -1);
        noDepositBonuses = sharedPref.getInt("noDeposit", -1);
        depositKind = sharedPref.getInt("depositKind", -1);

        String strCasinos = sharedPref.getString("casinos", null);
        if (strCasinos != null) {
            casinos = new ArrayList<String>(Arrays.asList(strCasinos.split("%%")));
        } else {
            casinos = new ArrayList<String>();
        }

        country = sharedPref.getString("country", "");
    }

    public void save(Context context) {
        SharedPreferences sharedPref =  context.getSharedPreferences("iGaming", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();

        String strGameType = null;
        for (String gameType : gameTypes) {
            if (strGameType == null) {
                strGameType = gameType;
            } else {
                strGameType += "%%" + gameType;
            }
        }
        edit.putString("gameTypes", strGameType);

        edit.putInt("freeSpins", freeSpins);
        edit.putInt("noDeposit", noDepositBonuses);
        edit.putInt("depositKind", depositKind);

        String strCasinos = null;
        for (String casino : casinos) {
            if (strCasinos == null) {
                strCasinos = casino;
            } else {
                strCasinos += "%%" + casino;
            }
        }
        edit.putString("casinos", strCasinos);

        edit.putString("country", country);

        edit.commit();
    }

    public static String getString(String key, Resources res, int defaultResID) {
        String str = instance.resources.get(key);
        if (str != null) {
            return str;
        } else {
            return res.getString(defaultResID);
        }
    }

    public static void setString(TextView textView, String key, int defaultResID) {
        String str = instance.resources.get(key);
        if (str != null) {
            textView.setText(str);
        } else {
            textView.setText(defaultResID);
        }
    }
}
