package com.asbozh.geotrivia;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;

public class MainMenuFragment extends Fragment implements View.OnClickListener {

    private TextView tvWelcomeUser;
    private String localUserName;
    private Button bSignOut;
    private SignInButton bSignIn;
    private Button bNewGame, bLeaderBoard, bAchievements, bAboutUs;
    private FloatingActionButton fabNewGame, fabLeaderBoard, fabAchievements, fabAboutUs;


    public interface Listener {
        void onSetToolbar();
        void onMainMenuSignInButtonClicked();
        void onMainMenuSignOutButtonClicked();
        void onStartNewGameClicked();
        void onLeaderBoardClicked();
        void onAchievementsClicked();
        void onAboutUsClicked();
    }

    Listener mListener = null;

    public void setListener(Listener l) {
        mListener = l;
    }

    public String getLocalUserName() {
        return localUserName;
    }

    public void setLocalUserName(String localUserName) {
        this.localUserName = localUserName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvWelcomeUser = (TextView) getActivity().findViewById(R.id.tvWelcomeUser);
        initButtons();
        mListener.onSetToolbar();
    }

    private void initButtons() {
        bSignIn = (SignInButton) getActivity().findViewById(R.id.bSignIn);
        bSignIn.setStyle(SignInButton.SIZE_STANDARD, SignInButton.COLOR_LIGHT);

        bSignOut = (Button) getActivity().findViewById(R.id.bSignOut);

        bNewGame = (Button) getActivity().findViewById(R.id.bNewGame);
        bLeaderBoard = (Button) getActivity().findViewById(R.id.bLeaderBoard);
        bAchievements = (Button) getActivity().findViewById(R.id.bAchievements);
        bAboutUs = (Button) getActivity().findViewById(R.id.bAboutUs);

        fabNewGame = (FloatingActionButton) getActivity().findViewById(R.id.fabNewGame);
        fabLeaderBoard = (FloatingActionButton) getActivity().findViewById(R.id.fabLeaderBoard);
        fabAchievements = (FloatingActionButton) getActivity().findViewById(R.id.fabAchievements);
        fabAboutUs = (FloatingActionButton) getActivity().findViewById(R.id.fabAboutUs);

        bSignIn.setOnClickListener(this);
        bSignOut.setOnClickListener(this);
        bNewGame.setOnClickListener(this);
        bLeaderBoard.setOnClickListener(this);
        bAchievements.setOnClickListener(this);
        bAboutUs.setOnClickListener(this);
        fabNewGame.setOnClickListener(this);
        fabLeaderBoard.setOnClickListener(this);
        fabAchievements.setOnClickListener(this);
        fabAboutUs.setOnClickListener(this);
    }

    public void setWelcomeUser(String nickName) {
        nickName = "Здравейте " + nickName;
        tvWelcomeUser.setText(nickName);
    }

    public void setOfflineUI(boolean isOffline) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvWelcomeUser.getLayoutParams();
        if (isOffline) {
            bSignIn.setVisibility(View.VISIBLE);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.LEFT_OF, R.id.bSignIn);
            tvWelcomeUser.setLayoutParams(params);
            bSignOut.setVisibility(View.GONE);
        } else {
            bSignOut.setVisibility(View.VISIBLE);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.LEFT_OF, R.id.bSignOut);
            tvWelcomeUser.setLayoutParams(params);
            bSignIn.setVisibility(View.GONE);
        }
    }

    private int DpInPixels(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bSignIn) {
            mListener.onMainMenuSignInButtonClicked();
        }

        if (view.getId() == R.id.bSignOut) {
            //Dialog for user name
            final EditText etNickName = new EditText(getActivity());
            etNickName.setSingleLine();
            etNickName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
            etNickName.setHint(getResources().getString(R.string.username_dialog_hint));

            AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
            dialog.setMessage(getResources().getString(R.string.username_dialog_body));
            dialog.setTitle(getResources().getString(R.string.username_dialog_title));
            dialog.setCancelable(false);
            dialog.setView(etNickName, DpInPixels(24.0f), 0, DpInPixels(24.0f), 0);
            dialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.username_dialog_positive), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    setLocalUserName(etNickName.getText().toString());
                    mListener.onMainMenuSignOutButtonClicked();
                }
            });
            dialog.show();
        }

        if (view.getId() == R.id.bNewGame || view.getId() == R.id.fabNewGame) {
            mListener.onStartNewGameClicked();
            Toast.makeText(getActivity(), "New Game under construction", Toast.LENGTH_SHORT).show();
        }

        if (view.getId() == R.id.bLeaderBoard || view.getId() == R.id.fabLeaderBoard) {
            mListener.onLeaderBoardClicked();
            Toast.makeText(getActivity(), "Leader Board under construction", Toast.LENGTH_SHORT).show();
        }

        if (view.getId() == R.id.bAchievements || view.getId() == R.id.fabAchievements) {
            mListener.onAchievementsClicked();
            Toast.makeText(getActivity(), "Achievements under construction", Toast.LENGTH_SHORT).show();
        }

        if (view.getId() == R.id.bAboutUs || view.getId() == R.id.fabAboutUs) {
            mListener.onAboutUsClicked();
        }
    }
}
