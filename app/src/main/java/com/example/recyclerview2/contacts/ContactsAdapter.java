package com.example.recyclerview2.contacts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.recyclerview2.R;


import java.util.List;

import static com.example.recyclerview2.R.drawable.contact_background_confirm_needed;
import static com.example.recyclerview2.R.drawable.contact_background_not_confirmed;
import static com.example.recyclerview2.R.drawable.item_background;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.contactsViewHolder> {
    private List<ContactsClass> contactsList;
    private OnContactItemCL onContactItemCL;

    ContactsAdapter(List<ContactsClass> contactsList, OnContactItemCL onContactItemCL) {
        this.contactsList = contactsList;
        this.onContactItemCL = onContactItemCL;
    }

    @NonNull
    @Override
    public contactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_card,parent,false);
        return new contactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull contactsViewHolder holder, int position) {
        final ContactsClass contact = contactsList.get(position);
        holder.contactUserName.setText("Felhasználó: " + contact.getUserName() + " (" + contact.getFullName() + ")");
        holder.contactStatus.setText(contact.getContactStatus());
        if (contact.getContactStatus().trim().equals("Megerősített")) {
            holder.contactContainer.setBackgroundResource(item_background);
        }
        if (contact.getContactStatus().trim().equals("Megerősítettlen")) {
            holder.contactContainer.setBackgroundResource(contact_background_not_confirmed);
        }
        if (contact.getContactStatus().trim().equals("Megerősítendő")) {
            holder.contactContainer.setBackgroundResource(contact_background_confirm_needed);
        }
    }

    @Override
    public int getItemCount() {
        if (contactsList.size() > 0) {
            return contactsList.size();
        } else {
            return 0;
        }
    }


    public class contactsViewHolder extends RecyclerView.ViewHolder {
        public CardView contactContainer;
        public TextView contactStatus;
        public TextView contactUserName;

        public contactsViewHolder(View contactView) {
            super(contactView);
            contactContainer = contactView.findViewById(R.id.contactsContainer);
            contactStatus = contactView.findViewById(R.id.contactStatus);
            contactUserName = contactView.findViewById(R.id.contactUserName);
            contactView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAbsoluteAdapterPosition();
                    if(onContactItemCL != null && position != RecyclerView.NO_POSITION) {
                        onContactItemCL.OnContactClick(contactsList.get(position));
                    }
                }
            });
        }
    }

    public void setContacts(List<ContactsClass> contacts){
        this.contactsList = contacts;
        notifyDataSetChanged();
    }
}
