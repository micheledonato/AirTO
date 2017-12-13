package com.devmicheledonato.airto;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.style.StyleSpan;


public class CarTrafficBanFragment extends DialogFragment {

    public static final String FRAGMENT_TAG = "CarTrafficBanFragment_TAG";

    private static final String EXTRA_CAR_BAN_DATA = "EXTRA_CAR_BAN_DATA";

    private String[] mCarBanData;

    public CarTrafficBanFragment() {
        // Required empty public constructor
    }

    public static CarTrafficBanFragment newInstance(String[] carBanData) {
        CarTrafficBanFragment fragment = new CarTrafficBanFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray(EXTRA_CAR_BAN_DATA, carBanData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCarBanData = getArguments().getStringArray(EXTRA_CAR_BAN_DATA);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getString(R.string.car_ban_title);
        SpannableString message;

        String levelToday;
        String blockTypeToday;
        if (mCarBanData != null && mCarBanData[0] != null) {
            levelToday = "Oggi livello " + mCarBanData[0] + ":";
            blockTypeToday = mCarBanData[1];
        }else {
            levelToday = "Oggi:";
            blockTypeToday = getString(R.string.car_ban_no_info_msg);
        }
        SpannableString messageToday = new SpannableString(levelToday + "\n" + blockTypeToday + "\n");
        messageToday.setSpan(new StyleSpan(Typeface.BOLD), 0, levelToday.length(), 0);

        String levelTomorrow;
        String blockTypeTomorrow;
        if (mCarBanData != null && mCarBanData[2] != null) {
            levelTomorrow = "Domani livello " + mCarBanData[2] + ":";
            blockTypeTomorrow = mCarBanData[3];
        } else {
            levelTomorrow = "Domani:";
            blockTypeTomorrow = getString(R.string.car_ban_no_info_msg);
        }
        SpannableString messageTomorrow = new SpannableString(messageToday + "\n" + levelTomorrow + "\n" + blockTypeTomorrow);
        messageTomorrow.setSpan(new StyleSpan(Typeface.BOLD), 0, levelToday.length(), 0);
        messageTomorrow.setSpan(new StyleSpan(Typeface.BOLD), messageToday.length() + 1, messageToday.length() + 1 + levelTomorrow.length(), 0);
        message = messageTomorrow;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();
    }

}
