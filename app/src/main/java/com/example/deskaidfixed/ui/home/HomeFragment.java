package com.example.deskaidfixed.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.deskaidfixed.Dashboard;
import com.example.deskaidfixed.R;
import com.example.deskaidfixed.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home,
                container, false);
        Button button = (Button) rootView.findViewById(R.id.btnDashboard);

        button.setOnClickListener(v -> updateDetail());
        return rootView;
    }

    public void updateDetail() {
        Intent intent = new Intent(getActivity(), Dashboard.class);
        startActivity(intent);
    }

}