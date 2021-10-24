package com.example.recyclerview2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class ListEditFragment extends DialogFragment {

    //widgets
    private AutoCompleteTextView listName;
    private Button modify;
    private int position;
    private String name;

    public ListEditFragment(int position, String name) {
        this.position = position;
        this.name = name;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lists_edit, container, false);
        listName = v.findViewById(R.id.modifyName);
        modify = v.findViewById(R.id.modifyButton);

        listName.setText(name);

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String modifiedName = listName.getText().toString();
                ((ListActivity)getActivity()).editShoppingList(modifiedName, position);
                getDialog().dismiss();
            }
        });
        return v;
    }
}