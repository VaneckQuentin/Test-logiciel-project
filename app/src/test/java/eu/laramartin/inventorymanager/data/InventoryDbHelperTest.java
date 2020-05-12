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

package eu.laramartin.inventorymanager.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.RenamingDelegatingContext;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static java.security.AccessController.getContext;

public class InventoryDbHelperTest {

    InventoryDbHelper inventoryDbHelper;
    StockItem stockItem;
    Cursor cursor;

    @Mock
    SQLiteDatabase dbMock;

    @Mock Context mockContext;

    @Before
    public void onSetup() {
        inventoryDbHelper = new InventoryDbHelper(mockContext);
        stockItem = new StockItem(
                "Test",
                "10 €",
                25,
                "Test GB",
                "+49 000 000 0000",
                "test@test.com",
                "android.resource://eu.laramartin.inventorymanager/drawable/gummibear");
    }

    @After
    public void tearDown() throws Exception{
        inventoryDbHelper.close();
//        super.tearDown();
    }

    @Test
    public void testInsertItem_addNewStockItem(){
        try{

            inventoryDbHelper.insertItem(stockItem);

            cursor = inventoryDbHelper.readStock();

            String x = cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_NAME));

            x.equals(x);
        }
        finally {

        }
    }
}
