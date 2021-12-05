package com.example.recyclerview2.appDataBase;

public abstract class AbstractFireStoreInstance {
    //Kollekciók három szinten: felhasználók: kontaktok/listák/statisztika; Listák: termékek
    protected static final String COLLECTION_OF_USERS = "users";
    protected static final String COLLECTION_OF_CONTACTS = "contactList";
    protected static final String COLLECTION_PRODUCTS = "products";
    protected static final String COLLECTION_OF_STATISTICS = "statistics";
    protected static final String COLLECTION_OF_CATEGORY = "productCategory";

    //Kontaktok adatai:
    protected static final String EMAIL = "contactEmail";
    protected static final String FULL_NAME = "contactFullName";
    protected static final String USER_NAME = "contactUserName";
    protected static final String CONTACT_STATUS = "contactStatus";
    //Kontaktok státuszai:
    protected static final String CONTACT_CONFIRMED = "0";
    protected static final String CONTACT_NOT_CONFIRMED = "1";
    protected static final String CONTACT_NEED_CONFIRM = "2";

    //Lista adatai: ID, Név, Tulajdonos (emailcím), Tulajdonos neve, Megosztás állapota (igaz/hamiz),
    //Felhasználók listája (akikkel a lista megosztásra került)
    protected static final String LIST_NAME = "listName";
    protected static final String LIST_ID = "listID";
    protected static final String OWNER = "owner";
    protected static final String OWNER_NAME = "ownerName";
    protected static final String LIST_IS_SHARED = "shared";
    protected static final String LIST_SHARED_WITH = "sharedWith";

    protected static final String USERS = "users";
    protected static final String LISTS = "lists";

    //A termékek adatai a listán belül a terméktömbben találhatóak,
    //mint a név, a mennyiség, mennyiségi egység és a vásárlás jelző
    protected static final String PRODUCTS_OF_LIST = "productsOfList";
    protected static final String PRODUCT_ARRAY = "products";
    protected static final String PRODUCT_NAME = "name";
    protected static final String PRODUCT_QUANTITY = "quantity";
    protected static final String PRODUCT_QUANTITY_TYPE = "quantityType";
    protected static final String PRODUCT_CATEGORY = "productCategory";
    protected static final String PRODUCT_CHECKED_STATUS = "checked";

    //Product Statictics
    protected static final String PRODUCT_STATISTICS = "statistics";
    protected static final String CATEGORY_FRUIT_AND_VEG = "Gyümölcs és Zöldség";
    protected static final String CATEGORY_BAKERY = "Pékáru";
    protected static final String CATEGORY_DAIRY = "Tejtermék";
    protected static final String CATEGORY_BEVERAGE = "Ital";
    protected static final String CATEGORY_MEAT = "Hús";
    protected static final String CATEGORY_OTHER = "Egyéb";
}
