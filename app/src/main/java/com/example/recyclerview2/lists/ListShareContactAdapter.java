package com.example.recyclerview2.lists;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview2.R;
import com.example.recyclerview2.appDataBase.ContactClass;

import java.util.ArrayList;
import java.util.List;

public class ListShareContactAdapter extends RecyclerView.Adapter<ListShareContactAdapter.listSCAViewHolder> {
    private List<ContactClass> contactClasses;
    private List<ContactClass> sharedWith;
    private ListShareInterface listShareInterface;

    ListShareContactAdapter(ListShareInterface listShareInterface) {
        this.contactClasses = new ArrayList<>();
        this.sharedWith = new ArrayList<>();
        this.listShareInterface = listShareInterface;
    }

    public class listSCAViewHolder extends RecyclerView.ViewHolder {
        public CardView contactsContainer;
        public TextView contactName;
        public CheckBox checkBox;

        public listSCAViewHolder(View contactView) {
            super(contactView);
            contactsContainer = contactView.findViewById(R.id.contactsContainer);
            contactName = contactView.findViewById(R.id.contactName);
            checkBox = contactView.findViewById(R.id.checkBoxShare);
        }
    }


    @NonNull
    @Override
    public listSCAViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_share_card,parent,false);
        return new listSCAViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull listSCAViewHolder holder, int position) {
        final ContactClass contactClass = contactClasses.get(position);
        holder.contactName.setText(contactClass.getContactUserName() + " (" + contactClass.getContactFullName() + ")");

        holder.checkBox.setOnCheckedChangeListener(null);
        if (sharedWith != null) {
            for (ContactClass checkShared : sharedWith) {
                if (checkShared.getContactEmail().equals(contactClass.getContactEmail())) {
                    holder.checkBox.setChecked(true);
                    break;
                } else holder.checkBox.setChecked(false);
            }
        } else holder.checkBox.setChecked(false);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listShareInterface.saveSharedStatus(contactClass,isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactClasses.size();
    }

    public void setContacts(List<ContactClass> contactClasses, List<ContactClass> sharedWith)  {
        this.contactClasses = contactClasses;
        this.sharedWith = sharedWith;
        this.notifyDataSetChanged();
    }
}
