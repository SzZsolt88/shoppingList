package com.example.recyclerview2.lists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.recyclerview2.R;
import com.example.recyclerview2.appDataBase.ContactClass;
import com.example.recyclerview2.appDataBase.ListClass;
import com.example.recyclerview2.appDataBase.ListDB;
import com.example.recyclerview2.appDataBase.UserClass;
import com.example.recyclerview2.charts.ChartActivity;
import com.example.recyclerview2.contacts.ContactsActivity;
import com.example.recyclerview2.products.ProductActivity;
import com.example.recyclerview2.user.UserEditDataActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ListActivity extends AppCompatActivity implements OnListItemCL {
    private EditText listName;
    private RecyclerView shoppingListsView;
    private ListAdapter adapter;
    private ListDB listDB;
    private UserClass currentUser;
    private String currentUserMail;
    private ConnectivityManager cm;
    private NetworkRequest networkRequest;
    private ConnectivityManager.NetworkCallback callback;
    private boolean connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lists);

        checkInternet(this);
        cm.registerNetworkCallback(networkRequest, callback);

        Intent getCurrentUser = getIntent();
        currentUser = getCurrentUser.getParcelableExtra("currentUser");
        currentUserMail = currentUser.getuMail();

        listName = findViewById(R.id.shoppingListName);
        Button addList = findViewById(R.id.createListButton);
        shoppingListsView = findViewById(R.id.shoppingLists);

        shoppingListsView.setHasFixedSize(true);
        shoppingListsView.setLayoutManager(new LinearLayoutManager(this));
        shoppingListsView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ListAdapter(this, currentUserMail);
        shoppingListsView.setAdapter(adapter);


        listDB = new ListDB(currentUserMail);
        listDB.getListOfsUser(connected);

        listDB.getListMutableLiveData().observe(this, new Observer<List<ListClass>>() {
            @Override
            public void onChanged(List<ListClass> strings) {
                adapter.setLists(strings);
            }
        });

        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameOfList = listName.getText().toString();
                String ownerMail = currentUser.getuMail();
                String ownerName = currentUser.getuName();
                if (nameOfList.isEmpty())
                    Snackbar.make(shoppingListsView, "Adj nevet a list??nak!", Snackbar.LENGTH_LONG).show();
                else if (alreadyExits(nameOfList)) {
                    Snackbar.make(shoppingListsView, "Van m??r ilyen lista, adj m??sik nevet!", Snackbar.LENGTH_LONG).show();
                    listName.getText().clear();
                    listName.requestFocus();
                }
                else {
                    ListClass newList = new ListClass(nameOfList,ownerMail,ownerName);
                    listDB.createList(newList);
                    adapter.notifyDataSetChanged();
                    listName.getText().clear();
                    listName.requestFocus();
                }
            }
        });
    }

    // Men?? l??trehoz??sa
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shoppinglist_menu, menu);
        String userName = currentUser.getuName();
        setTitle(userName + " List??i");
        return true;
    }

    // Men?? elemek programoz??sa
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.delete) {
            confirmationAndDelete();
        } else if (itemId == R.id.edit) {
            editLists();
        } else if (itemId == R.id.share) {
            shareLists();
        } else if (itemId == R.id.userEditActivity) {
            createActivity(this, UserEditDataActivity.class);
        } else if (itemId == R.id.contacts) {
            createActivity(this, ContactsActivity.class);
        } else if (itemId == R.id.chartsMenuButton) {
            createActivity(this, ChartActivity.class);
        } else if (itemId == R.id.logOutMenu) {
            logOut();
            finish();
        }
        adapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    // felhaszn??l??i adatainak m??dos??t??sa vagy a kimutat??s elind??t??s
    @SuppressWarnings("rawtypes")
    private void createActivity(Context context, Class activity){
        Intent startActivity = new Intent(context, activity);
        startActivity.putExtra("currentUser", currentUser);
        startActivity(startActivity);
    }

    // Megvizsg??lja, hogy van-e m??r azonos nev?? lista
    public boolean alreadyExits(String name) {
        boolean exits = false;
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i).getListName().equals(name)) {
                exits = true;
            }
        }
        return exits;
    }

    // listaelemre kattint??s
    @Override
    public void onListClick(ListClass list) {
        Intent openList = new Intent(ListActivity.this, ProductActivity.class);
        openList.putExtra("name", list.getListName());
        openList.putExtra("ID", list.getListID());
        openList.putExtra("ownerMail", list.getOwner());
        startActivity(openList);
    }
    // listaelemre hosszan kattint??s - kijel??l??s
    @Override
    public void onListLongClick(ListClass list) {
        list.setSelected(!list.isSelected());
        adapter.notifyDataSetChanged();
    }

    // lista t??rl??se el??tt meger??s??t??s k??r??se a felhaszn??l??t??l
    private void confirmationAndDelete() {
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setMessage("Biztosan t??r??lni szeretn?? a kijel??lt elemeket?")
                .setCancelable(false)
                .setPositiveButton("Igen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteLists();
                    }
                }).setNegativeButton("Nem", null);
        deleteDialog.show();
    }

    // lista t??nyleges t??rl??se
    private void deleteLists() {
        for (int i = adapter.getItemCount()-1; i >= 0;  i--) {
            if (adapter.getItem(i).isSelected()) {
                listDB.deleteList(adapter.getItem(i));
            }
        }
    }

    // lista szerkeszt??se fragment ind??t??sa
    private void editLists() {
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i).isSelected()) {
                if(adapter.getItem(i).getOwner().equals(currentUserMail)) {
                    ListEditFragment editDialog = new ListEditFragment(adapter.getItem(i));
                    editDialog.show(getSupportFragmentManager(), "listNameEdit");
                } else {
                    Snackbar.make(shoppingListsView, "Csak a saj??t list??d tudod ??tnevezni!", Snackbar.LENGTH_LONG).show();
                }
                adapter.getItem(i).setSelected(false);
            }
        }
        adapter.notifyDataSetChanged();
    }

    // lista szerkeszt??se, adatb??zis friss??t??se
    public void editShoppingList(ListClass originalList, String newName) {
        listDB.updateList(originalList, newName);
    }

    //Lista megoszt??sa: framgent l??trehoz??sa
    private void shareLists() {
        for (int i = adapter.getItemCount()-1; i >= 0;  i--) {
            if (adapter.getItem(i).isSelected()) {
                if (adapter.getItem(i).getOwner().equals(currentUserMail)) {
                    ListShareFragment shareDialog = new ListShareFragment(adapter.getItem(i), currentUser);
                    shareDialog.show(getSupportFragmentManager(), "listNameEdit");
                } else Snackbar.make(shoppingListsView, "Csak a saj??t list??d tudod megosztani!", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    // lista megoszt??sa adatb??zis friss??t??se
    public void shareList(ListClass shareList, List<ContactClass> sharedWith) {
        listDB.shareList(shareList, sharedWith);
    }

    private void checkInternet(Context context) {
        cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        networkRequest = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();
        callback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                connected = true;
                super.onAvailable(network);
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                connected = false;
            }
        };
    }

    @Override
    protected void onResume() {
        if (listDB.getCurrentUser() == null) {
            finish();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cm.unregisterNetworkCallback(callback);
    }

    // felhaszn??l?? kijelentkeztet??se
    private void logOut() {
        listDB.singOut();
    }

    // A vissza gomb megnyom??s??val lista n??zetben semmi ne t??rt??njen
    @Override
    public void onBackPressed() {
    }
}