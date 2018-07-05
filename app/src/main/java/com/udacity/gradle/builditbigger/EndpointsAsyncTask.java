package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.IdlingResources.IdlingManager;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;


//source: https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/77e9910911d5412e5efede5fa681ec105a0f02ad/HelloEndpoints#2-connecting-your-android-app-to-the-backend
public class EndpointsAsyncTask extends AsyncTask<String, Void, String> {

    private static MyApi myApiService = null;
    private Context mContext;
    private ResponseCallBack mResponseCallBack;

    @Nullable
    private IdlingManager mIdlingManager;

    public interface ResponseCallBack {
        void response(String result);
    }

    public EndpointsAsyncTask(Context context, ResponseCallBack callback, IdlingManager idlingManager) {
        mContext = context;
        mResponseCallBack = callback;
        mIdlingManager = idlingManager;
    }

    @Override
    protected String doInBackground(String... params) {

        if (mIdlingManager != null) {
            mIdlingManager.setIdleState(false);
        }

        if (myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // this is IP used for accessing to server by my local network, maybe you need to change it to 10.0.2.2 if you are using Android emulator or 10.0.3.2 if you are using genymotion
                    .setRootUrl("http://192.168.0.115:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver
            myApiService = builder.build();
        }

        try {
            return myApiService.fetchData().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        mResponseCallBack.response(result);
    }
}
