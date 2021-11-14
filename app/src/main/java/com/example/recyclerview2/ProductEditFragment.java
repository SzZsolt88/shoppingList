package com.example.recyclerview2;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

public class ProductEditFragment extends DialogFragment {
    private String productName;
    private int productQuantity;
    private String productQuantityType;
    private AutoCompleteTextView nameField;
    private EditText quantityField;
    private Spinner unitSpinnerField;
    private Button modify;

    private int position;

    public ProductEditFragment(String productName, int productQuantity, String productQuantityType, int position) {
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productQuantityType = productQuantityType;
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.product_edit, container, false);
        nameField = v.findViewById(R.id.modifyName);
        quantityField = v.findViewById(R.id.quantityProduct);
        unitSpinnerField = v.findViewById(R.id.unitSpinner);
        modify = v.findViewById(R.id.modifyButton);

        //autotext értékek betöltése
        String[] productVariants = getResources().getStringArray(R.array.product_variants);
        ArrayAdapter<String> adapterProducts = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, productVariants);
        nameField.setAdapter(adapterProducts);

        //spinner feltöltése értékekkel
        String[] units = getResources().getStringArray(R.array.unit_variants);
        ArrayAdapter<String> adapterUnits = new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,units);
        unitSpinnerField.setAdapter(adapterUnits);

        //alapértékek beállítása
        nameField.setText(productName);
        quantityField.setText(productQuantity);
        unitSpinnerField.setSelection(adapterUnits.getPosition(productQuantityType));

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String modifiedName = nameField.getText().toString();
                int modifiedQuantity = Integer.getInteger(quantityField.getText().toString());
                int modifiedQuantityType = unitSpinnerField.getSelectedItemPosition();
                if (modifiedName.length() > 0) {
                    ((ProductActivity)getActivity()).editProduct(modifiedName, modifiedQuantity, modifiedQuantityType, position);
                    getDialog().dismiss();
                }
                else Snackbar.make(v, "Adj nevet a terméknek!", Snackbar.LENGTH_LONG).show();
            }
        });
        return v;
    }
}