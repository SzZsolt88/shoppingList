package com.example.recyclerview2.lists;

import com.example.recyclerview2.appDataBase.ContactClass;

public interface ListShareInterface {
    void saveSharedStatus(ContactClass contactClass, boolean isChecked);
}
