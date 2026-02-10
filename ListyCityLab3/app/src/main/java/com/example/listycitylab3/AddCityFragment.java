package com.example.listycitylab3;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.fragment.app.DialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AddCityFragment extends DialogFragment {

    private static final String ARG_CITY = "city";

    interface AddCityDialogListener {
        void addCity(City city);
    }

    private AddCityDialogListener listener;
    private City cityToEdit;

    // Preferred Android way
    public static AddCityFragment newInstance(City city) {
        AddCityFragment fragment = new AddCityFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    public AddCityFragment() {
        // Required empty constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            cityToEdit = (City) getArguments().getSerializable(ARG_CITY);
        }

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_add_city, null);

        EditText editCity = view.findViewById(R.id.edit_text_city_text);
        EditText editProvince = view.findViewById(R.id.edit_text_province_text);

        // 🔽 Pre-fill when editing
        if (cityToEdit != null) {
            editCity.setText(cityToEdit.getName());
            editProvince.setText(cityToEdit.getProvince());
        }

        return new AlertDialog.Builder(requireContext())
                .setTitle(cityToEdit == null ? "Add a city" : "Edit city")
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = editCity.getText().toString();
                    String province = editProvince.getText().toString();

                    if (cityToEdit != null) {
                        // Edit existing city
                        cityToEdit.setName(name);
                        cityToEdit.setProvince(province);
                        listener.addCity(null); // refresh only
                    } else {
                        // Add new city
                        listener.addCity(new City(name, province));
                    }
                })
                .create();
    }
}