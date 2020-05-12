/*
 * PROJECT LICENSE
 *
 * This project was submitted by Lara Martín as part of the Nanodegree At Udacity.
 *
 * As part of Udacity Honor code, your submissions must be your own work, hence
 * submitting this project as yours will cause you to break the Udacity Honor Code
 * and the suspension of your account.
 *
 * Me, the author of the project, allow you to check the code as a reference, but if
 * you submit it, it's your own responsibility if you get expelled.
 *
 * Copyright (c) 2017 Lara Martín
 *
 * Besides the above notice, the following license applies and this license notice
 * must be included in all works derived from this project.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package eu.laramartin.inventorymanager;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import eu.laramartin.inventorymanager.data.InventoryDbHelper;
import eu.laramartin.inventorymanager.data.StockContract;
import eu.laramartin.inventorymanager.data.StockItem;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DataBaseTest {

    StockItem item;
    StockItem item2;

    @ClassRule
    public static ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    //@Rule
    //public ActivityTestRule<DetailsActivity> dActivityTestRule = new ActivityTestRule<>(DetailsActivity.class);

    private static InventoryDbHelper dbHelper;


    @BeforeClass
    public static void setUp() throws Exception {
        dbHelper = new InventoryDbHelper(mActivityTestRule.getActivity());
    }

    @Before
    public void setUpEach() throws Exception {
        item = new StockItem(
                "testItem1".trim(),
                "10".trim(),
                Integer.parseInt("10".trim()),
                "firstSupplierName".trim(),
                "09900900900".trim(),
                "firstSupplierName@example.com".trim(),
                "https://dev.laromana-fils.be/wp-content/uploads/2018/01/Test-Logo-Small-Black-transparent-1.png");

        item2 = new StockItem(
                "testItem2".trim(),
                "20".trim(),
                Integer.parseInt("20".trim()),
                "secondSupplierName".trim(),
                "09900900900".trim(),
                "secondSupplierName@example.com".trim(),
                "https://dev.laromana-fils.be/wp-content/uploads/2018/01/Test-Logo-Small-Black-transparent-1.png");

        dbHelper.insertItem(item);
        dbHelper.insertItem(item2);

    }

    @After
    public void tearDown() throws Exception {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(StockContract.StockEntry.TABLE_NAME, null, null);
    }

    @Test
    public void insertItem_newStockItemTest() {
        Long itemId = null;
        StockItem item = null;

        try {
            //Set up
            String name = "testItem3";
            String price = "10 €";
            Integer quantity = 10;
            String firstSupplierName = "firstSupplierName";
            String phoneNumber = "09900900900";
            String email = "firstSupplierName@example.com";
            String image = "https://dev.laromana-fils.be/wp-content/uploads/2018/01/Test-Logo-Small-Black-transparent-1.png";


            item = new StockItem(
                    name,
                    price,
                    quantity,
                    firstSupplierName,
                    phoneNumber,
                    email,
                    image);

            // Exercice
            mActivityTestRule.launchActivity(null);

            dbHelper.insertItem(item);

            // Verify
            Cursor cursor = dbHelper.readStock();

            cursor.moveToLast();
            String cursorName = cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_NAME));
            String cursorPrice = cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_PRICE));
            Integer cursorQuantity = Integer.valueOf(cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_QUANTITY)));
            String cursorFirstSupplierName = cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_SUPPLIER_NAME));
            String cursorPhoneNumber = cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_SUPPLIER_PHONE));
            String cursorEmail = cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_SUPPLIER_EMAIL));
            String cursorImage = cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_IMAGE));

            assertEquals(name, cursorName);
            assertEquals(price, cursorPrice);
            assertEquals(quantity, cursorQuantity);
            assertEquals(firstSupplierName, cursorFirstSupplierName);
            assertEquals(phoneNumber, cursorPhoneNumber);
            assertEquals(email, cursorEmail);
            assertEquals(image, cursorImage);
        } finally {

        }
    }

    @Test(expected = NullPointerException.class)
    public void insertItem_nullStockItemTest() {
        try {
            //Set up
            StockItem item = null;

            // Exercice
            mActivityTestRule.launchActivity(null);

            dbHelper.insertItem(item);
        } finally {

        }
    }

    @Test
    public void readStock_verifyCountTest() {
        Integer countNumber = null;

        try {
            //Set up
            countNumber = 2;

            // Exercice
            mActivityTestRule.launchActivity(null);
            Cursor cursor = dbHelper.readStock();

            // Verify
            Integer realCount = cursor.getCount();
            assertEquals(realCount, countNumber);

        } finally {

        }
    }

    @Test
    public void readItem_checkAddedItemsWithIdTest() {

        Cursor cursorItem1;
        Cursor cursorItem2;
        try {
            //Set up
            Cursor cursor = dbHelper.readStock();
            cursor.moveToPosition(0);
            Long itemId1 = cursor.getLong(cursor.getColumnIndex(StockContract.StockEntry._ID));
            cursor.moveToPosition(1);
            Long itemId2 = cursor.getLong(cursor.getColumnIndex(StockContract.StockEntry._ID));

            // Exercice
            mActivityTestRule.launchActivity(null);
            cursorItem1 = dbHelper.readItem(itemId1);

            cursorItem2 = dbHelper.readItem(itemId2);

            // Verify
            assertCursor(cursorItem1, item);
            assertCursor(cursorItem2, item2);
        } finally {

        }
    }

    @Test(expected = CursorIndexOutOfBoundsException.class)
    public void readItem_checkAddedItemsWithNegativeIdTest() {

        Cursor cursorItem;
        try {
            //Set up
            long itemId = -1;

            // Exercice
            mActivityTestRule.launchActivity(null);
            cursorItem = dbHelper.readItem(itemId);

            // Verify
            assertCursor(cursorItem, item);
        } finally {

        }
    }

    @Test(expected = NullPointerException.class)
    public void readItem_checkAddedItemsWithNullIdTest() {
        try {
            //Set up
            Integer itemId = null;

            // Exercice
            mActivityTestRule.launchActivity(null);
            dbHelper.readItem(itemId);
        } finally {

        }
    }

    @Test
    public void readItem_checkAddItemsWithIdTest() {

        Cursor cursorItem1;
        Cursor cursorItem2;
        try {
            //Set up
            Cursor cursor = dbHelper.readStock();
            cursor.moveToPosition(0);
            Long itemId1 = cursor.getLong(cursor.getColumnIndex(StockContract.StockEntry._ID));
            cursor.moveToPosition(1);
            Long itemId2 = cursor.getLong(cursor.getColumnIndex(StockContract.StockEntry._ID));

            // Exercice
            mActivityTestRule.launchActivity(null);
            cursorItem1 = dbHelper.readItem(itemId1);

            cursorItem2 = dbHelper.readItem(itemId2);

            // Verify
            assertCursor(cursorItem1, item);
            assertCursor(cursorItem2, item2);
        } finally {

        }
    }

    @Test
    public void updateItem_WithIdAndPositiveQuantity() {
        try {
            //Set up
            int quantity = 20;
            assertUpdateItem(quantity);
        } finally {

        }
    }

    @Test
    public void updateItem_WithIdAndNegativeQuantity() {
        try {
            //Set up
            int quantity = -10;
            assertUpdateItem(quantity);
        } finally {

        }
    }

    @Test
    public void updateItem_WithIdAndZeroQuantity() {
        try {
            //Set up
            int quantity = 0;
            assertUpdateItem(quantity);
        } finally {

        }
    }

    @Test
    public void updateItem_WithIdAndMaxBoundQuantity() {
        try {
            //Set up
            int quantity = Integer.MAX_VALUE;
            assertUpdateItem(quantity);
        } finally {

        }
    }

    @Test
    public void updateItem_WithIdAndMinBoundQuantity() {
        try {
            //Set up
            int quantity = Integer.MIN_VALUE;
            assertUpdateItem(quantity);
        } finally {

        }
    }

    @Test(expected = NullPointerException.class)
    public void updateItem_WithIdAndMinNullQuantity() {
        try {
            //Set up
            Integer quantity = null;
            assertUpdateItem(quantity);
        } finally {

        }
    }

    @Test(expected = CursorIndexOutOfBoundsException.class)
    public void updateItem_WithNotExistingIdAndPositiveQuantity() {
        try {
            //Set up
            int quantity = 25;
            long itemId = -1;
            assertUpdateItemNonExistingId(itemId,quantity);
        } finally {

        }
    }

    @Test(expected = CursorIndexOutOfBoundsException.class)
    public void updateItem_WithNotExistingIdAndNegativeQuantity() {
        try {
            //Set up
            int quantity = -10;
            long itemId = -1;
            assertUpdateItemNonExistingId(itemId,quantity);
        } finally {

        }
    }

    @Test(expected = CursorIndexOutOfBoundsException.class)
    public void updateItem_WithNotExistingIdAndZeroQuantity() {
        try {
            //Set up
            int quantity = 0;
            long itemId = -1;
            assertUpdateItemNonExistingId(itemId,quantity);
        } finally {

        }
    }

    @Test(expected = CursorIndexOutOfBoundsException.class)
    public void updateItem_WithNotExistingIdAndMaxBoundQuantity() {
        try {
            //Set up
            int quantity = Integer.MAX_VALUE;
            long itemId = -1;
            assertUpdateItemNonExistingId(itemId,quantity);
        } finally {

        }
    }

    @Test(expected = CursorIndexOutOfBoundsException.class)
    public void updateItem_WithNotExistingIdAndMinBoundQuantity() {
        try {
            //Set up
            int quantity = Integer.MIN_VALUE;
            long itemId = -1;
            assertUpdateItemNonExistingId(itemId,quantity);
        } finally {

        }
    }

    @Test(expected = NullPointerException.class)
    public void updateItem_WithNotExistingIdAndMinNullQuantity() {
        try {
            //Set up
            Integer quantity = null;
            long itemId = -1;
            assertUpdateItemNonExistingId(itemId,quantity);
        } finally {

        }
    }

    @Test(expected = NullPointerException.class)
    public void updateItem_WithNullIdAndPositiveQuantity() {
        try {
            //Set up
            int quantity = 30;
            Long itemId = null;
            assertUpdateItemNonExistingId(itemId,quantity);
        } finally {

        }
    }

    

    private void assertCursor(Cursor cursor, StockItem item) {
        cursor.moveToFirst();
        String cursorName = cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_NAME));
        String cursorPrice = cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_PRICE));
        Integer cursorQuantity = Integer.valueOf(cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_QUANTITY)));
        String cursorFirstSupplierName = cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_SUPPLIER_NAME));
        String cursorPhoneNumber = cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_SUPPLIER_PHONE));
        String cursorEmail = cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_SUPPLIER_EMAIL));
        String cursorImage = cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_IMAGE));

        Integer quantity = item.getQuantity();

        assertEquals(item.getProductName(), cursorName);
        assertEquals(item.getPrice(), cursorPrice);
        assertEquals(quantity, cursorQuantity);
        assertEquals(item.getSupplierName(), cursorFirstSupplierName);
        assertEquals(item.getSupplierPhone(), cursorPhoneNumber);
        assertEquals(item.getSupplierEmail(), cursorEmail);
        assertEquals(item.getImage(), cursorImage);
    }

    private void assertUpdateItem(Integer quantity){
        // Setup
        Cursor cursor = dbHelper.readStock();
        cursor.moveToPosition(0);
        long itemId = cursor.getLong(cursor.getColumnIndex(StockContract.StockEntry._ID));

        // Exercice
        mActivityTestRule.launchActivity(null);
        dbHelper.updateItem(itemId, quantity);

        // Verify
        cursor = dbHelper.readItem(itemId);
        cursor.moveToFirst();

        Integer cursorQuantity = Integer.valueOf(cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_QUANTITY)));

        assertEquals(cursorQuantity, quantity);
    }

    private void assertUpdateItemNonExistingId(Long itemId, Integer quantity){
        Cursor cursor = dbHelper.readStock();

        // Exercice
        mActivityTestRule.launchActivity(null);
        dbHelper.updateItem(itemId, quantity);

        // Verify
        cursor = dbHelper.readItem(itemId);
        cursor.moveToFirst();

        Integer cursorQuantity = Integer.valueOf(cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_QUANTITY)));

        assertEquals(cursorQuantity, quantity);
    }
}
