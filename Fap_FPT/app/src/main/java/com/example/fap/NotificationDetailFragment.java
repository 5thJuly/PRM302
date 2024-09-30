package com.example.fap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NotificationDetailFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_CONTENT = "content";

    public static NotificationDetailFragment newInstance(String title, String content) {
        NotificationDetailFragment fragment = new NotificationDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_CONTENT, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_detail, container, false);

        TextView titleTextView = view.findViewById(R.id.detailTitle);
        TextView contentTextView = view.findViewById(R.id.detailContent);

        if (getArguments() != null) {
            titleTextView.setText(getArguments().getString(ARG_TITLE));
            contentTextView.setText(getArguments().getString(ARG_CONTENT));
        }

        return view;
    }
}
