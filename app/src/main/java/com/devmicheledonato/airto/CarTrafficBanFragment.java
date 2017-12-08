package com.devmicheledonato.airto;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;


public class CarTrafficBanFragment extends DialogFragment {

    public static final String FRAGMENT_TAG = "CarTrafficBanFragment_TAG";

    private static final String EXTRA_LEVEL = "EXTRA_LEVEL";
    private static final String EXTRA_BLOCK_TYPE = "EXTRA_BLOCK_TYPE";

    private String mLevel;
    private String mBlockType;

    public CarTrafficBanFragment() {
        // Required empty public constructor
    }

    public static CarTrafficBanFragment newInstance(String level, String blockType) {
        CarTrafficBanFragment fragment = new CarTrafficBanFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_LEVEL, level);
        bundle.putString(EXTRA_BLOCK_TYPE, blockType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLevel = getArguments().getString(EXTRA_LEVEL);
            mBlockType = getArguments().getString(EXTRA_BLOCK_TYPE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mBlockType)
                .setTitle("Livello " + mLevel)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();
    }

}
