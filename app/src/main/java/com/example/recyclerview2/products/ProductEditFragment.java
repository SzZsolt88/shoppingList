package com.example.recyclerview2.products;


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

import com.example.recyclerview2.R;
import com.example.recyclerview2.appDataBase.ProductClass;
import com.google.android.material.snackbar.Snackbar;

public class ProductEditFragment extends DialogFragment {
    private final String productName;
    private final String productQuantity;
    private final String productQuantityType;
    private final String productCategory;
    private AutoCompleteTextView nameField;
    private EditText quantityField;
    private Spinner unitSpinnerField;
    private Spinner productCategorySpinner;
    private Button modify;
    private Button cancelModifyProduct;

    private final int position;

    public ProductEditFragment(ProductClass originalProduct, int position) {
        this.productName = originalProduct.getName();
        this.productQuantity = originalProduct.getQuantity();
        this.productQuantityType = originalProduct.getQuantityType();
        this.productCategory = originalProduct.getProductCategory();
        this.position = position;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.product_edit, container, false);

        getDialog().setCanceledOnTouchOutside(false);
        nameField = v.findViewById(R.id.modifyName);
        quantityField = v.findViewById(R.id.quantityProduct);
        productCategorySpinner = v.findViewById(R.id.categorySpinner);
        unitSpinnerField = v.findViewById(R.id.unitSpinner);
        modify = v.findViewById(R.id.modifyButton);
        cancelModifyProduct = v.findViewById(R.id.cancelModifyProduct);

        //autotext ??rt??kek bet??lt??se
        nameField.setAdapter(((ProductActivity)getActivity()).passProductAdapter());

        //spinner felt??lt??se ??rt??kekkel
        String[] units = getResources().getStringArray(R.array.unit_variants);
        ArrayAdapter<String> adapterUnits = new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,units);
        unitSpinnerField.setAdapter(adapterUnits);

        //term??kcateg??ri??k Spiiner felt??lt??se
        String[] category = {"Gy??m??lcs ??s Z??lds??g","P??k??ru", "Tejterm??k", "Ital", "H??s", "Egy??b"};
        ArrayAdapter<String> adapterProductCategory = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,category);
        productCategorySpinner.setAdapter(adapterProductCategory);

        //alap??rt??kek be??ll??t??sa
        nameField.setText(productName);
        quantityField.setText(productQuantity);

        productCategorySpinner.setSelection(adapterProductCategory.getPosition(productCategory));
        if (!productQuantity.equals("")) {
            unitSpinnerField.setSelection(adapterUnits.getPosition(productQuantityType));
        } else {
            unitSpinnerField.setSelection(0);
        }
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String modifiedName = nameField.getText().toString();
                String modifiedProductCategory = productCategorySpinner.getSelectedItem().toString();
                boolean isProductCategoryModified = false;
                if (!modifiedProductCategory.equals(productCategory)) {
                    isProductCategoryModified = true;
                }
                String modifiedQuantity = quantityField.getText().toString();
                int modifiedQuantityType = unitSpinnerField.getSelectedItemPosition();
                if (modifiedName.length() > 0) {
                    ((ProductActivity)getActivity()).updateProduct(modifiedName, modifiedProductCategory,modifiedQuantity, modifiedQuantityType, position, isProductCategoryModified);
                    getDialog().dismiss();
                }
                else Snackbar.make(v, "Adj nevet a term??knek!", Snackbar.LENGTH_LONG).show();
            }
        });
        cancelModifyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return v;
    }
}