package sep4x2.android;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import sep4x2.android.ui.login.LoginFragment;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RegisterTest {

    //@Rule
    //public FragmentTestRule<?, LoginFragment> fragmentTestRule =
    //      FragmentTestRule.create(LoginFragment.class);

    @Test
    public void test() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("sep4x2.android", appContext.getPackageName());
    }
}
