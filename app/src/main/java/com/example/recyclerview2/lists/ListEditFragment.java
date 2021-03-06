package com.example.recyclerview2.lists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.recyclerview2.R;
import com.example.recyclerview2.appDataBase.ListClass;
import com.google.android.material.snackbar.Snackbar;


public class ListEditFragment extends DialogFragment {

    //widgets
    private AutoCompleteTextView listName;
    private Button modify;
    private Button cancelModify;
    private final ListClass originalList;

    public ListEditFragment(ListClass originalList) {
        this.originalList = originalList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lists_edit, container, false);
        listName = v.findViewById(R.id.modifyName);
        cancelModify = v.findViewById(R.id.cancelModifyListBtn);
        modify = v.findViewById(R.id.modifyButton);

        getDialog().setCanceledOnTouchOutside(false);

        listName.setText(originalList.getListName());

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String modifiedName = listName.getText().toString();
                if (modifiedName.isEmpty())
                    Snackbar.make(modify, "Adj nevet a listának!", Snackbar.LENGTH_LONG).show();
                else if (((ListActivity) getActivity()).alreadyExits(modifiedName)) {
                    Snackbar.make(modify, "Van már ilyen nevü lista, adj másik nevet!", Snackbar.LENGTH_LONG).show();
                }
                else {
                    ((ListActivity) getActivity()).editShoppingList(originalList, modifiedName);
                    getDialog().dismiss();
                }
            }
        });
        cancelModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return v;
    }
}