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


import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import eu.laramartin.inventorymanager.data.InventoryDbHelper;
import eu.laramartin.inventorymanager.data.StockContract;
import eu.laramartin.inventorymanager.data.StockItem;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

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
        StockItem item = new StockItem(
                "testItem1".trim(),
                "10".trim(),
                Integer.parseInt("10".trim()),
                "firstSupplierName".trim(),
                "09900900900".trim(),
                "firstSupplierName@example.com".trim(),
                "https://dev.laromana-fils.be/wp-content/uploads/2018/01/Test-Logo-Small-Black-transparent-1.png");

        StockItem item2 = new StockItem(
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
    public void goToDetailsActivityAddNewItemTest() {
        mActivityTestRule.launchActivity(null);

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withText("Add new item"), withParent(withId(R.id.action_bar))));
        textView2.check(matches(withText("Add new item")));
    }

    @Test
    public void goToDetailsActivityEditItemTest() {
        mActivityTestRule.launchActivity(null);

        DataInteraction linearLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.list_view),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                1)))
                .atPosition(0);
        linearLayout.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withText("Edit item"), withParent(withId(R.id.action_bar))));
        textView2.check(matches(withText("Edit item")));
    }

    @Test
    public void decreaseQuantityOnClickCartTest(){
        mActivityTestRule.launchActivity(null);

        ViewInteraction textView = onView(
                allOf(withId(R.id.quantity), withText("10"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1),
                                2),
                        isDisplayed()));
        textView.check(matches(withText("10")));

        DataInteraction appCompatImageView = onData(anything())
                .inAdapterView(
                        allOf(withId(R.id.list_view),
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        1)))
                .atPosition(0).onChildView(withId(R.id.sale));
        appCompatImageView.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.quantity), withText("9"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1),
                                2),
                        isDisplayed()));
        textView2.check(matches(withText("9")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
