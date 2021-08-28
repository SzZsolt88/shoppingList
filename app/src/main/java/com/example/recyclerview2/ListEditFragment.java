package com.example.recyclerview2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class ListEditFragment extends DialogFragment {

    public OnShoppingListEL onShoppingListEL;

    //widgets
    private AutoCompleteTextView listName;
    private Button modify;
    private int position;
    private String name;

    public ListEditFragment(OnShoppingListEL onShoppingListEL, int position, String name) {
        this.onShoppingListEL = onShoppingListEL;
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
                onShoppingListEL.editShoppingList(modifiedName, position);
                getDialog().dismiss();
            }
        });
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            onShoppingListEL = (OnShoppingListEL) getActivity();
        }catch (ClassCastException e) {
            Log.d("TAG", "onAttach: ClassCastException:  " + e.getMessage());
        }
    }
}
