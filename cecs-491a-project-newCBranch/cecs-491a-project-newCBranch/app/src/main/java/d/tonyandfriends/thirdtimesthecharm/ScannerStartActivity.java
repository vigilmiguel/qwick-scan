package d.tonyandfriends.thirdtimesthecharm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.Barcode.UrlBookmark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * reads barcodes.
 */
public class ScannerStartActivity extends Activity implements DataTransporter  {

    // use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView contactname, contacttitle, contactorganization;

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";
    Spider spidey = new Spider();
    String productName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_start);

        statusMessage = (TextView)findViewById(R.id.status_message);

        Intent intent = new Intent(this, BarcodeCaptureActivity.class);
        startActivityForResult(intent, RC_BARCODE_CAPTURE);
    }

     @Override
     // This is our Adapter implementation
     // We take the result from the instance of our Spider object, which is a Name string that we parsed from some HTML
    public void onProcessDone(String result) {
        productName = "";

        // The name will return "Description $itemName", I dont want it to say Description, so this is a quickfix until we find a better way to parse the HTML
        // If we find a result...
        if(result.compareTo("Sorry we couldn't find that item")!=0) {
            for (int i = 0; i < result.length(); i++) {
                productName += result.charAt(i);
            }

            // Store it in the database
        }
        // If we don't find a result...
        else
            productName = result;

        First picture = new First();
        //.image();

       statusMessage.setText(productName);
       spidey.cancel(true); // May not be needed, someday I may even test it
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        String poop = "";
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);

                    int type = barcode.valueFormat;


                    switch (type) {
                        case Barcode.CONTACT_INFO:
                            Log.i(TAG, barcode.contactInfo.title);
                            Barcode.Email[] set = barcode.contactInfo.emails;
                            List<String> answers = new ArrayList<String>();
                            for (int i = 0; i < set.length; i++){
                                answers.add(set[i].address);

                                Toast.makeText(this, "emails " + answers, Toast.LENGTH_LONG).show();
                            }
                            contactorganization.setText(barcode.contactInfo.organization);
                            contacttitle.setText(barcode.contactInfo.title);
                            Barcode.PersonName name = barcode.contactInfo.name;
                            String contactName = name.first + " " + name.last;
                            contactname.setText(contactName);
                            break;
                        case Barcode.EMAIL:
                            Log.i(TAG, barcode.email.address);
                            break;
                        case Barcode.ISBN:
                            Log.i(TAG, barcode.rawValue);
                            break;
                        case Barcode.PHONE:
                            Log.i(TAG, barcode.phone.number);
                            break;
                        case Barcode.PRODUCT:
                            Log.i(TAG, barcode.rawValue);
                            // Get the ID, only thing we are concerned with as far as I'm concerned
                            poop = barcode.rawValue;
                            break;
                        case Barcode.SMS:
                            Log.i(TAG, barcode.sms.message);
                            break;
                        case Barcode.TEXT:
                            Log.i(TAG, barcode.rawValue);
                            break;
                        case Barcode.URL:
                            Log.i(TAG, "url: " + barcode.url.url);
                            break;
                        case Barcode.WIFI:
                            Log.i(TAG, barcode.wifi.ssid);
                            break;
                        case Barcode.GEO:
                            Log.i(TAG, barcode.geoPoint.lat + ":" + barcode.geoPoint.lng);
                            break;
                        case Barcode.CALENDAR_EVENT:
                            Log.i(TAG, barcode.calendarEvent.description);
                            break;
                        case Barcode.DRIVER_LICENSE:
                            Log.i(TAG, barcode.driverLicense.licenseNumber);
                            break;
                        default:
                            Log.i(TAG, barcode.rawValue);
                            break;
                    }
                    //For Jsoup or Async classes there are three Array fields <1,2,3>
                    //Field one will be for data we want to pass, field 2 is unimporant as far as I know, keep it void. Field 3 is our return data
                    String [] container = new String[1]; // Here we are passing String, so we need a String Array
                    container[0] = poop; // one day I may make this a legit name, we assign our ID we get from barcode into our Array
                    try { // Async threads can only run once, so if we want multiple scans we need new objects. This may not be the best way, but it works for now
                        spidey = spidey.getClass().newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    spidey.myVessel = this; // Assign the our instance to Spider
                    spidey.execute(container); // Jesus take the wheel

                    //Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    // IDK if these are ever even used, I've tried to get them to work, but nothing happens
                    statusMessage.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
