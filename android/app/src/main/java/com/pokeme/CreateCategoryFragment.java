package com.pokeme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CreateCategoryFragment extends Fragment {
    public CreateCategoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.create_new_category);
        return inflater.inflate(R.layout.fragment_create_category, container, false);
    }

    public void onSubmit(View view) {

    }

}
