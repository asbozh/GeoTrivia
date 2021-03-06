package com.asbozh.geotrivia;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.example.games.basegameutils.BaseGameUtils;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        SignInFragment.Listener, MainMenuFragment.Listener, AboutUsFragment.Listener, ChooseLevelFragment.Listener, GameFragment.Listener, ResultFragment.Listener {

    // 1 - SignInFragment, 2 - MainMenuFragment, 3 - AboutUsFragment, 4 - ChooseLevelFragment, 5 - GameFragment, 6 - ResultFragment
    private int currentFragmentState = 2;

    // SharedPreferences object and variables
    SharedPreferences mSharedPreferences;
    private boolean isFirstTimeStarted = true;
    private String mNickName = "Играч";
    private String mLocalNickName = "Играч";
    private boolean isOffline = true;

    // Fragments
    SignInFragment mSignInFragment;
    MainMenuFragment mMainMenuFragment;
    AboutUsFragment mAboutUsFragment;
    ChooseLevelFragment mChooseLevelFragment;
    GameFragment mGameFragment;
    ResultFragment mResultFragment;

    // Client used to interact with Google APIs
    private GoogleApiClient mGoogleApiClient;

    // Are we currently resolving a connection failure?
    private boolean mResolvingConnectionFailure = false;

    // Has the user clicked the sign-in button?
    private boolean mSignInClicked = false;

    // Automatically start the sign-in flow when the Activity starts
    private boolean mAutoStartSignInFlow = true;

    // request codes we use when invoking an external activity
    private static final int RC_RESOLVE = 5000;
    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;


    // achievements and scores we're pending to push to the cloud
    // (waiting for the user to sign in)
    AccomplishmentsOutbox mOutbox = new AccomplishmentsOutbox();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  Hiding the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        // Create the Google Api Client with access to the Play Games services
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        // Create fragments
        mSignInFragment = new SignInFragment();
        mMainMenuFragment = new MainMenuFragment();
        mAboutUsFragment = new AboutUsFragment();
        mChooseLevelFragment = new ChooseLevelFragment();
        mGameFragment = new GameFragment();
        mResultFragment = new ResultFragment();


        // listen to fragment events
        mSignInFragment.setListener(this);
        mMainMenuFragment.setListener(this);
        mAboutUsFragment.setListener(this);
        mChooseLevelFragment.setListener(this);
        mGameFragment.setListener(this);
        mResultFragment.setListener(this);

        // Loading saved shared preferences
        mSharedPreferences = getPreferences(Context.MODE_PRIVATE);
        isFirstTimeStarted = mSharedPreferences.getBoolean(getString(R.string.saved_first_time), true);
        mNickName = mSharedPreferences.getString(getString(R.string.saved_nickname), "Играч");
        mLocalNickName = mSharedPreferences.getString(getString(R.string.saved_local_nickname), "Играч");
        isOffline = mSharedPreferences.getBoolean(getString(R.string.saved_is_offline), true);

        Log.d("asbozh", "first Time: " + isFirstTimeStarted);
        Log.d("asbozh", "nick name: " + mNickName);
        Log.d("asbozh", "local nick name: " + mLocalNickName);
        Log.d("asbozh", "offline: " + isOffline);


        if (isFirstTimeStarted && !isSignedIn()) {
            currentFragmentState = 1;
            switchToFragment(currentFragmentState);
        } else {
            currentFragmentState = 2;
            switchToFragment(currentFragmentState);
        }
        // load outbox from file
        mOutbox.loadLocal(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isFirstTimeStarted && !isOffline && !isSignedIn()) {
            mGoogleApiClient.connect();
            mAutoStartSignInFlow = true;
            Log.d("asbozh", "onStart(): Connecting");
        } else {
            Log.d("asbozh", "onStart(): not connecting");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
            Log.d("asbozh", "onStop(): Disconnecting");
        }
    }

    private void firstTimePassed() {
        isFirstTimeStarted = false;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(getString(R.string.saved_first_time), isFirstTimeStarted);
        editor.apply();
    }

    private boolean isSignedIn() {
        return (mGoogleApiClient != null && mGoogleApiClient.isConnected());
    }

    /***
     * *
     * Methods from SignInFragment
     * *
     ***/

    @Override
    public void onSignInButtonClicked() {
        mGoogleApiClient.connect();
        mSignInClicked = true;
        firstTimePassed();
        currentFragmentState = 2;
        switchToFragment(currentFragmentState);
    }

    @Override
    public void onSkipButtonClicked() {

        isOffline = true;

        if (mSignInFragment.getUserName().equals("")) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(getString(R.string.saved_is_offline), isOffline);
            editor.putString(getString(R.string.saved_nickname), mNickName);
            editor.putString(getString(R.string.saved_local_nickname), mLocalNickName);
            editor.apply();
        } else {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(getString(R.string.saved_is_offline), isOffline);
            editor.putString(getString(R.string.saved_nickname), mSignInFragment.getUserName().trim());
            editor.putString(getString(R.string.saved_local_nickname), mSignInFragment.getUserName().trim());
            editor.apply();
            mLocalNickName = mSignInFragment.getUserName().trim();
        }

        firstTimePassed();
        currentFragmentState = 2;
        switchToFragment(currentFragmentState);
    }

    /***
     * *
     * Methods for Signing in Google Play Games
     * *
     ***/

    @Override
    public void onConnected(Bundle bundle) {
        Player p = Games.Players.getCurrentPlayer(mGoogleApiClient);
        if (p != null) {
            mNickName = p.getDisplayName();
        }
        if (mNickName.indexOf(' ') > -1) { // Check if there is more than one word.
            mNickName = mNickName.substring(0, mNickName.indexOf(' ')); // Extract first word.
        }
        isOffline = false;
        onSetToolbar();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(getString(R.string.saved_is_offline), isOffline);
        editor.putString(getString(R.string.saved_nickname), mNickName.trim());
        editor.apply();
        Log.d("asbozh", "onConnected(): Connected");


        // if there are accomplishments to push, push them
        if (!mOutbox.isEmpty()) {
            pushAccomplishments();
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            return;
        }

        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;
            if (!BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, "Sign-in failed - Resolving failed")) {
                mResolvingConnectionFailure = false;
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                BaseGameUtils.showActivityResultError(this, requestCode, resultCode, R.string.signin_other_error);
            }
        }
    }

    /***
     * *
     * Methods from MainMenuFragment
     * *
     ***/

    @Override
    public void onSetToolbar() {

        if (isOffline) {
            mMainMenuFragment.setWelcomeUser(mLocalNickName);
            mMainMenuFragment.setOfflineUI(isOffline);
        } else {
            mMainMenuFragment.setWelcomeUser(mNickName);
            mMainMenuFragment.setOfflineUI(isOffline);
        }
    }

    @Override
    public void onMainMenuSignInButtonClicked() {
        mGoogleApiClient.connect();
        mSignInClicked = true;
    }

    @Override
    public void onMainMenuSignOutButtonClicked() {
        isOffline = true;
        mAutoStartSignInFlow = false;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(getString(R.string.saved_is_offline), isOffline);
        if (!mMainMenuFragment.getLocalUserName().equals("")) {
            mLocalNickName = mMainMenuFragment.getLocalUserName();
            editor.putString(getString(R.string.saved_local_nickname), mLocalNickName);
        }
        editor.apply();
        mSignInClicked = false;
        Games.signOut(mGoogleApiClient);
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        onSetToolbar();
    }

    @Override
    public void onStartNewGameClicked() {
        currentFragmentState = 4;
        switchToFragment(currentFragmentState);
    }

    @Override
    public void onLeaderBoardClicked() {
        if (isSignedIn()) {
            startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(mGoogleApiClient),
                    RC_UNUSED);
        } else {
            BaseGameUtils.makeSimpleDialog(this, getString(R.string.leaderboards_not_available)).show();
        }
    }

    @Override
    public void onAchievementsClicked() {
        if (isSignedIn()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient),
                    RC_UNUSED);
        } else {
            BaseGameUtils.makeSimpleDialog(this, getString(R.string.achievements_not_available)).show();
        }
    }

    @Override
    public void onAboutUsClicked() {
        currentFragmentState = 3;
        switchToFragment(currentFragmentState);
    }

    /***
     * *
     * Methods from AboutUsFragment
     * *
     ***/

    @Override
    public void onAboutUsBackClicked() {
        currentFragmentState = 2;
        switchToFragment(currentFragmentState);
    }

    /***
     * *
     * Methods from ChooseLevelFragment
     * *
     ***/

    @Override
    public void onChooseLevelBackButtonPressed() {
        currentFragmentState = 2;
        switchToFragment(currentFragmentState);
    }

    @Override
    public void onStartLevelClicked() {
        mGameFragment.setChosenLevel(mChooseLevelFragment.getLevelClicked());
        mGameFragment.setNickName(mMainMenuFragment.getCurrentUserName());
        currentFragmentState = 5;
        switchToFragment(currentFragmentState);
    }

    /***
     * *
     * Methods from GameFragment
     * *
     ***/

    @Override
    public void onGameFinish() {
        mResultFragment.setPoints(mGameFragment.getPoints());
        mResultFragment.setCurrentTable(mGameFragment.getCurrentTable());
        mResultFragment.setmQuestionsOrder(mGameFragment.getmQuestionsOrder());
        mResultFragment.setmUserAnswers(mGameFragment.getUserAnswers());

        // check for achievements
        checkForAchievements(mGameFragment.getPoints());

        // update leader boards
        updateLeaderBoards(mGameFragment.getCurrentTable(), mGameFragment.getPoints());

        // push those accomplishments to the cloud, if signed in
        pushAccomplishments();

        currentFragmentState = 6;
        switchToFragment(currentFragmentState);
    }

    private void checkForAchievements(int points) {

        if (points == 0) {
            mOutbox.mFunnyAchievement = true;
            achievementToast(getString(R.string.achievement_funny_toast_text));
        }
        if (points > 25) {
            mOutbox.mNerdAchievement = true;
            achievementToast(getString(R.string.achievement_nerd_toast_text));
        }
        if (points == 30) {
            mOutbox.mGeniusAchievement = true;
            achievementToast(getString(R.string.achievement_genius_toast_text));
        }
        mOutbox.mGamesPlayed++;
    }

    private void achievementToast(String toast) {
        // Only show toast if not signed in. If signed in, the standard Google Play toasts will appear
        if (!isSignedIn()) {
            Toast.makeText(this, toast,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void updateLeaderBoards(String subject, int points) {
        if (subject.contains("_GEO_") && mOutbox.mGeoScore < points) {
            mOutbox.mGeoScore = points;
        } else if (subject.contains("_HIS_") && mOutbox.mHisScore < points) {
            mOutbox.mHisScore = points;
        } else if (subject.contains("_BIO_") && mOutbox.mBioScore < points) {
            mOutbox.mBioScore = points;
        } else if (subject.contains("_PHI_") && mOutbox.mPhiScore < points) {
            mOutbox.mPhiScore = points;
        }
    }

    private void pushAccomplishments() {
        if (!isSignedIn()) {
            // can't push to the cloud, so save locally
            mOutbox.saveLocal(this);
            return;
        }
        if (mOutbox.mFunnyAchievement) {
            Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_funny));
            mOutbox.mFunnyAchievement = false;
        }
        if (mOutbox.mNerdAchievement) {
            Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_nerd));
            mOutbox.mNerdAchievement = false;
        }
        if (mOutbox.mGeniusAchievement) {
            Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_genius));
            mOutbox.mGeniusAchievement = false;
        }
        if (mOutbox.mGamesPlayed > 0) {
            Games.Achievements.increment(mGoogleApiClient, getString(R.string.achievement_curious),
                    mOutbox.mGamesPlayed);
            Games.Achievements.increment(mGoogleApiClient, getString(R.string.achievement_addicted),
                    mOutbox.mGamesPlayed);
            Games.Achievements.increment(mGoogleApiClient, getString(R.string.achievement_maniac),
                    mOutbox.mGamesPlayed);
            mOutbox.mGamesPlayed = 0;
        }
        if (mOutbox.mGeoScore >= 0) {
            Games.Leaderboards.submitScore(mGoogleApiClient, getString(R.string.leaderboard_geo),
                    mOutbox.mGeoScore);
            mOutbox.mGeoScore = -1;
        }
        if (mOutbox.mHisScore >= 0) {
            Games.Leaderboards.submitScore(mGoogleApiClient, getString(R.string.leaderboard_his),
                    mOutbox.mHisScore);
            mOutbox.mHisScore = -1;
        }
        if (mOutbox.mBioScore >= 0) {
            Games.Leaderboards.submitScore(mGoogleApiClient, getString(R.string.leaderboard_bio),
                    mOutbox.mBioScore);
            mOutbox.mBioScore = -1;
        }
        if (mOutbox.mPhiScore >= 0) {
            Games.Leaderboards.submitScore(mGoogleApiClient, getString(R.string.leaderboard_phi),
                    mOutbox.mPhiScore);
            mOutbox.mPhiScore = -1;
        }
        mOutbox.saveLocal(this);
    }

    /***
     * *
     * Methods from ResultFragment
     * *
     ***/

    @Override
    public void onPlayAgain() {
        currentFragmentState = 4;
        switchToFragment(currentFragmentState);
    }

    private void switchToFragment(int fragmentNumber) {

        // set fragments animations
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMainMenuFragment.setEnterTransition(new Slide());
            mMainMenuFragment.setAllowEnterTransitionOverlap(true);
            mChooseLevelFragment.setEnterTransition(new Explode());
            mChooseLevelFragment.setExitTransition(new Explode());
            mChooseLevelFragment.setAllowEnterTransitionOverlap(true);
            mChooseLevelFragment.setAllowReturnTransitionOverlap(false);
            mAboutUsFragment.setEnterTransition(new Fade());
            mAboutUsFragment.setExitTransition(new Fade());
            mAboutUsFragment.setAllowEnterTransitionOverlap(true);
            mAboutUsFragment.setAllowReturnTransitionOverlap(false);
            mGameFragment.setEnterTransition(new Fade());
            mGameFragment.setAllowEnterTransitionOverlap(false);
            mResultFragment.setEnterTransition(new Slide());
            mResultFragment.setAllowEnterTransitionOverlap(true);
        }
        switch (fragmentNumber) {
            case 1:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, mSignInFragment).commit();
                break;
            case 2:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, mMainMenuFragment).commit();
                break;
            case 3:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, mAboutUsFragment).commit();
                break;
            case 4:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, mChooseLevelFragment).commit();
                break;
            case 5:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, mGameFragment).commit();
                break;
            case 6:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, mResultFragment).commit();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        // 1 - SignInFragment, 2 - MainMenuFragment, 3 - AboutUsFragment, 4 - ChooseLevelFragment, 5 - GameFragment, 6 - ResultFragment
        if (currentFragmentState == 3 || currentFragmentState == 4 || currentFragmentState == 6) {
            currentFragmentState = 2;
            switchToFragment(currentFragmentState);
        } else if (currentFragmentState == 5) {
            AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
            adBuilder.setMessage(getString(R.string.exit_dialog_body));
            adBuilder.setCancelable(false);
            adBuilder.setPositiveButton(getResources().getString(R.string.exit_dialog_positive), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    currentFragmentState = 2;
                    switchToFragment(currentFragmentState);
                }
            });
            adBuilder.setNegativeButton(getResources().getString(R.string.exit_dialog_negative), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = adBuilder.create();
            alertDialog.show();
        }
        else {
            super.onBackPressed();
        }
    }

}
