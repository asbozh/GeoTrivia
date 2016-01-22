package com.asbozh.geotrivia;


import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AccomplishmentsOutbox {


    boolean mFunnyAchievement = false;
    boolean mNerdAchievement = false;
    boolean mGeniusAchievement = false;
    int mGamesPlayed = 0;
    int mGeoScore = -1;
    int mHisScore = -1;
    int mBioScore = -1;
    int mPhiScore = -1;

    String fileName = "offline_save";
    FileOutputStream outputStream;
    FileInputStream inputStream;

    boolean isEmpty() {
        return !mFunnyAchievement && !mNerdAchievement && !mGeniusAchievement && mGamesPlayed == 0 &&
                mGeoScore < 0 && mHisScore < 0 && mBioScore < 0 && mPhiScore < 0;
    }

    public void saveLocal(Context ctx) {
        // TODO: Add encryption to the file
        try {
            outputStream = ctx.openFileOutput(fileName, Context.MODE_PRIVATE);
            String data = mFunnyAchievement + "|" + mNerdAchievement + "|" + mGeniusAchievement + "|" + mGamesPlayed + "|" + mGeoScore + "|" + mHisScore + "|" + mBioScore + "|" + mPhiScore;
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadLocal(Context ctx) {

        String data = "";
        int c;

        try {
            inputStream = ctx.openFileInput(fileName);
            while ((c = inputStream.read()) != -1) {
                data = data + Character.toString((char) c);
            }
            String attributes[] = data.split("\\|");
            mFunnyAchievement = Boolean.valueOf(attributes[0]);
            mNerdAchievement = Boolean.valueOf(attributes[1]);
            mGeniusAchievement = Boolean.valueOf(attributes[2]);
            mGamesPlayed = Integer.valueOf(attributes[3]);
            mGeoScore = Integer.valueOf(attributes[4]);
            mHisScore = Integer.valueOf(attributes[5]);
            mBioScore = Integer.valueOf(attributes[6]);
            mPhiScore = Integer.valueOf(attributes[7]);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
