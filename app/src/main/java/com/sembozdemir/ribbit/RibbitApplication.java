package com.sembozdemir.ribbit;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Semih Bozdemir on 16.2.2015.
 */
public class RibbitApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "qNpPqiymZd6Hs3zMko2Wrbr00L3oGOsfDVz1sW1r", "CcIg1NXeoA2FUBTn8iUfDJA1iqidHUqesHYLef57");
    }
}
