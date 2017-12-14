package com.devmicheledonato.airto;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.devmicheledonato.airto.utils.AirToWeatherUtils;

public class IpqaInfoFragment extends DialogFragment {

    public static final String FRAGMENT_TAG = "IpqaInfoFragment_TAG";

    private static final String EXTRA_IPQA_DATA = "EXTRA_IPQA_DATA";

    private String mIpqaInfo;

    public IpqaInfoFragment() {
        // Required empty public constructor
    }

    public static IpqaInfoFragment newInstance(String ipqaInfo) {
        IpqaInfoFragment fragment = new IpqaInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_IPQA_DATA, ipqaInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIpqaInfo = getArguments().getString(EXTRA_IPQA_DATA);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getString(R.string.ipqa_title);
        String message;
        if (mIpqaInfo != null) {
            message = AirToWeatherUtils.getIpqaInfoForIpqaCondition(getActivity(), mIpqaInfo);
        } else {
            message = getString(R.string.ipqa_no_info_msg);
        }

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
