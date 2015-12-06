package com.asbozh.geotrivia;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.SignInButton;

public class SignInFragment extends Fragment {

    private Button bSkip;
    private SignInButton signInButton;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public interface Listener {
        void onSignInButtonClicked();

        void onSkipButtonClicked();
    }

    Listener mListener = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_signin, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initButtons();
        bSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder adBuilder = new AlertDialog.Builder(getActivity());
                adBuilder.setTitle(getString(R.string.skip_dialog_title));
                adBuilder.setMessage(getString(R.string.skip_dialog_body));
                adBuilder.setCancelable(false);
                adBuilder.setPositiveButton(getResources().getString(R.string.btn_skip), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inputUserName();

                    }
                });
                adBuilder.setNegativeButton(getResources().getString(R.string.skip_dialog_negative), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = adBuilder.create();
                alertDialog.show();

            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSignInButtonClicked();
            }
        });
    }

    private void inputUserName() {
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

                setUserName(etNickName.getText().toString());
                mListener.onSkipButtonClicked();

            }
        });
        dialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.username_dialog_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private int DpInPixels(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void initButtons() {
        bSkip = (Button) getActivity().findViewById(R.id.btn_skip);
        signInButton = (SignInButton) getActivity().findViewById(R.id.sign_in_button);
        signInButton.setStyle(SignInButton.SIZE_WIDE, SignInButton.COLOR_LIGHT);
    }

    public void setListener(Listener l) {
        mListener = l;
    }
}
