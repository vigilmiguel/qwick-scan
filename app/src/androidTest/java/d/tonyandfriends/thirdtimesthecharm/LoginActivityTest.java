package d.tonyandfriends.thirdtimesthecharm;

import android.content.Intent;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginActivityTest {

   //The chosen hacker of our generation
   String fakeUser  = "Poop";
   String fakePass = "Neo(TheChoosenOne)";

   String emptyUser = null;
   String emptyPass = null;

    // We may change this as we want, to validate functionality for other users'
    String validUser = "ggezrootaccess@gmail.com";
    String validPass = "123456";

    int choice = 0;

    @Rule
    public ActivityTestRule<LoginActivity> testActivity =
    new ActivityTestRule(LoginActivity.class);


    @Before
    public void setUp() throws Exception {
        Intent intent = new Intent();
        testActivity.launchActivity(intent);
        //testReal();
        //testFake();
        testEmptiness();
        testActivity.getActivity().signInButton.callOnClick();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    @UiThreadTest
    public void onCreate() throws InterruptedException {

        Thread.sleep(10000);
        Assert.assertTrue(testActivity.getActivity().isCreated);
        //awaitility.await
    }

    @Test
    @UiThreadTest
    public void zheckThings() throws InterruptedException {
      Thread.sleep(3000);
      Log.d("my1",testActivity.getActivity().signInEmailText.getText().toString());
      Log.d("my2",testActivity.getActivity().signInPasswordText.getText().toString());
      Log.d("my3",Boolean.toString(testActivity.getActivity().isBadCombo));
      Log.d("my4",Boolean.toString(testActivity.getActivity().iGetHere));

      if(choice == 1)
      {
         Assert.assertFalse(testActivity.getActivity().isEmpty);
         Assert.assertTrue(testActivity.getActivity().isBadCombo);
         Assert.assertTrue(testActivity.getActivity().iGetHere);
      }
      if(choice == 2)
      {
          Assert.assertTrue(testActivity.getActivity().isEmpty);
          Assert.assertTrue(testActivity.getActivity().isBadCombo);
      }
      if(choice == 3)
      {

         Assert.assertFalse(testActivity.getActivity().isEmpty);
         Assert.assertFalse(testActivity.getActivity().isBadCombo);
         Assert.assertTrue(testActivity.getActivity().iGetHere);
      }


    }


    //A very relatable function, feelsEmptyMan
    public void testEmptiness() {
        testActivity.getActivity().signInPasswordText.setText(emptyPass);
        testActivity.getActivity().signInEmailText.setText(emptyUser);
        choice = 2;
    }


    //No knock-offs
    public void testFake() {
        testActivity.getActivity().signInPasswordText.setText(fakePass);
        testActivity.getActivity().signInEmailText.setText(fakeUser);
        choice = 1;
    }

    //Real Recogonize Real
    public void testReal() {
        testActivity.getActivity().signInPasswordText.setText(validPass);
        testActivity.getActivity().signInEmailText.setText(validUser);
        choice =3;
    }
}
