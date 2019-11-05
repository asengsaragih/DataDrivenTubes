package org.d3ifcool.utang.UnitTest;

import org.d3ifcool.utang.FormActivity;
import org.d3ifcool.utang.MainActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

@RunWith(AndroidJUnit4.class)
public class Driven {

    @Rule
    public ActivityTestRule<FormActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(FormActivity.class);

    @Test
    public void dataDriven() {

    }
}
