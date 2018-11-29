package com.ecolab.mike.genusbar_app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ecolab.mike.genusbar_app.R;
import com.ecolab.mike.genusbar_app.activity.HomeActivity;
import com.ecolab.mike.genusbar_sdk.bean.Token;
import com.ecolab.mike.genusbar_sdk.utils.DataCache;
import com.microsoft.aad.adal.ADALError;
import com.microsoft.aad.adal.AuthenticationCallback;
import com.microsoft.aad.adal.AuthenticationContext;
import com.microsoft.aad.adal.AuthenticationException;
import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.aad.adal.IDispatcher;
import com.microsoft.aad.adal.Logger;
import com.microsoft.aad.adal.PromptBehavior;
import com.microsoft.aad.adal.Telemetry;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public class MainActivity extends AppCompatActivity {

    /* UI & Debugging Variables */
    private static final String TAG = MainActivity.class.getSimpleName();
    Button callGraphButton;
    Button signOutButton;

    /* Azure AD Constants */
    /* Authority is in the form of https://login.microsoftonline.com/yourtenant.onmicrosoft.com */
    private static final String AUTHORITY = "https://login.microsoftonline.com/nalco.microsoftonline.com";
    /* The clientID of your application is a unique identifier which can be obtained from the app registration portal */
//    private static final String CLIENT_ID = "383ca184-1406-47bf-83a8-56f15ed96b8b";
    private static final String CLIENT_ID = "52ac7047-212b-4868-a5c0-e481b3e97fcf";
    /* Resource URI of the endpoint which will be accessed */
    private static final String RESOURCE_ID = "https://nalco.microsoftonline.com/48653e60-29b5-417a-bb54-11cfa8ef6a38";

    private static final String TECH_LOUNGE_RESOURCE_ID = "api://43af5a83-2c1a-412f-8f26-b49851852538";
    //    private static final String RESOURCE_ID = "0b7b17fe-6854-4468-a286-6b3c45be727c";

    /* The Redirect URI of the application (Optional) */
    private static final String REDIRECT_URI = "SSOTestClient://auth";

    /* Azure AD Variables */
    private AuthenticationContext mAuthContext;
    private AuthenticationResult mAuthResult;


    /* Boolean variable to ensure invocation of interactive sign-in only once in case of multiple  acquireTokenSilent call failures */
    private static AtomicBoolean sIntSignInInvoked = new AtomicBoolean();
    /* Constant to send message to the mAcquireTokenHandler to do acquire token with Prompt.Auto*/
    private static final int MSG_INTERACTIVE_SIGN_IN_PROMPT_AUTO = 1;
    /* Constant to send message to the mAcquireTokenHandler to do acquire token with Prompt.Always */
    private static final int MSG_INTERACTIVE_SIGN_IN_PROMPT_ALWAYS = 2;

    private static final int MSG_TECH_AUTH_ERROR = 3;

    /* Constant to store user id in shared preferences */
    private static final String USER_ID = "user_id";

    /* Telemetry variables */
    // Flag to turn event aggregation on/off
    private static final boolean sTelemetryAggregationIsRequired = true;





    /* get the cache */
    private DataCache mDataCache = DataCache.getSingleInstance();

    /* Telemetry dispatcher registration */
    static {
        Telemetry.getInstance().registerDispatcher(new IDispatcher() {
            @Override
            public void dispatchEvent(Map<String, String> events) {
                // Events from ADAL will be sent to this callback
                for(Map.Entry<String, String> entry: events.entrySet()) {
                    Log.d(TAG, entry.getKey() + ": " + entry.getValue());
                }
            }
        }, sTelemetryAggregationIsRequired);
    }
    /* Handler to do an interactive sign in and acquire token */
    private Handler mAcquireTokenHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==10000){
                AuthenticationResult obj= (AuthenticationResult) msg.obj;

                mAuthContext.acquireTokenSilentAsync(TECH_LOUNGE_RESOURCE_ID, CLIENT_ID, obj.getUserInfo().getUserId(), getTechLoungeAuthSilentCallback());
                return;
            }
            if(msg.what == 20000) {
                AuthenticationResult obj= (AuthenticationResult) msg.obj;
                mDataCache.saveITAppToken(new Token(obj.getAccessToken()));
                mDataCache.saveEmail(obj.getUserInfo().getDisplayableId());
                mDataCache.saveName(obj.getUserInfo().getGivenName() + " " +obj.getUserInfo().getFamilyName());
                Intent intent=new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }

            if( sIntSignInInvoked.compareAndSet(false, true)) {

                if (msg.what == MSG_INTERACTIVE_SIGN_IN_PROMPT_AUTO){
                    mAuthContext.acquireToken(getActivity(), RESOURCE_ID, CLIENT_ID, REDIRECT_URI, PromptBehavior.Auto, getAuthInteractiveCallback());
                }else if(msg.what == MSG_INTERACTIVE_SIGN_IN_PROMPT_ALWAYS){
                    mAuthContext.acquireToken(getActivity(), RESOURCE_ID, CLIENT_ID, REDIRECT_URI, PromptBehavior.Always, getAuthInteractiveCallback());
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mDataCache = new DataCache(getApplicationContext());

        callGraphButton = (Button) findViewById(R.id.callGraph);
        signOutButton = (Button) findViewById(R.id.clearCache);
        callGraphButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCallGraphClicked();
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSignOutClicked();
            }
        });

        mAuthContext = new AuthenticationContext(getApplicationContext(), AUTHORITY, false);

        /* Instantiate handler which can invoke interactive sign-in to get the Resource
         * sIntSignInInvoked ensures interactive sign-in is invoked one at a time */



        /* ADAL Logging callback setup */

        Logger.getInstance().setExternalLogger(new Logger.ILogger() {
            @Override
            public void Log(String tag, String message, String additionalMessage, Logger.LogLevel level, ADALError errorCode) {
                // You can filter the logs  depending on level or errorcode.
                Log.d(TAG, message + " " + additionalMessage);
            }
        });

        /*Attempt an acquireTokenSilent call to see if we're signed in*/
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        String userId = preferences.getString(USER_ID, "");
//        if(!TextUtils.isEmpty(userId)) {
//            mAuthContext.acquireTokenSilentAsync(RESOURCE_ID, CLIENT_ID, userId, getAuthSilentCallback());
//        }
    }

    //
    // Core Auth methods used by ADAL
    // ==================================
    // onActivityResult() - handles redirect from System browser
    // onCallGraphClicked() - attempts to get tokens for graph, if it succeeds calls graph & updates UI
    // callApi() - called on successful token acquisition which makes an HTTP request to graph
    // onSignOutClicked() - Signs user out of the app & updates UI
    //

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAuthContext.onActivityResult(requestCode, resultCode, data);
    }

    /*
     * End user clicked call Graph API button, time for Auth
     * Use ADAL to get an Access token for the Microsoft Graph API
     */
    private void onCallGraphClicked() {
        mAcquireTokenHandler.sendEmptyMessage(MSG_INTERACTIVE_SIGN_IN_PROMPT_AUTO);
    }

    private void callApi(AuthenticationResult result) {
        Message msg=new Message();
        msg.obj=result;
        msg.what=10000;
        mAcquireTokenHandler.sendMessage(msg);
    }

    private void callTechApi(AuthenticationResult result) {
        Message msg = new Message();
        msg.obj = result;
        msg.what=20000;
        mAcquireTokenHandler.sendMessage(msg);
    }

    private void onSignOutClicked() {
        // End user has clicked the Sign Out button
        // Kill the token cache
        // Optionally call the signout endpoint to fully sign out the user account
        mAuthContext.getCache().removeAll();
//        updateSignedOutUI();
    }

    //
    // UI Helper methods
    // ================================
    // updateGraphUI() - Sets graph response in UI
    // updateSuccessUI() - Updates UI when token acquisition succeeds
    // updateSignedOutUI() - Updates UI when app sign out succeeds
    //

//    private void updateGraphUI(JSONObject response) {
//        // Called on success from /me endpoint
//        // Writes graph data to the UI
//        TextView graphText = (TextView) findViewById(R.id.graphData);
//        graphText.setText(response.toString());
//    }
//
//    @SuppressLint("SetTextI18n")
//    private void updateSuccessUI() {
//        // Called on success from /me endpoint
//        // Removed call Graph API button and paint Sign out
//        callGraphButton.setVisibility(View.INVISIBLE);
//        signOutButton.setVisibility(View.VISIBLE);
//        findViewById(R.id.welcome).setVisibility(View.VISIBLE);
//        ((TextView) findViewById(R.id.welcome)).setText("Welcome, " +
//                mAuthResult.getUserInfo().getGivenName());
//
//    }
//
//    @SuppressLint("SetTextI18n")
//    private void updateSignedOutUI() {
//        callGraphButton.setVisibility(View.VISIBLE);
//        signOutButton.setVisibility(View.INVISIBLE);
//        findViewById(R.id.welcome).setVisibility(View.INVISIBLE);
//        ((TextView) findViewById(R.id.graphData)).setText("No Data");
//    }

    //
    // ADAL Callbacks
    // ======================
    // getActivity() - returns activity so we can acquireToken within a callback
    // getAuthSilentCallback() - callback defined to handle acquireTokenSilent() case
    // getAuthInteractiveCallback() - callback defined to handle acquireToken() case
    //

    public Activity getActivity() {
        return this;
    }

    /* Callback used in for silent acquireToken calls.
     * Looks if tokens are in the cache (refreshes if necessary and if we don't forceRefresh)
     * else errors that we need to do an interactive request.
     */
    private AuthenticationCallback<AuthenticationResult> getAuthSilentCallback() {
        return new AuthenticationCallback<AuthenticationResult>() {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult) {
                if(authenticationResult==null || TextUtils.isEmpty(authenticationResult.getAccessToken())
                        || authenticationResult.getStatus()!= AuthenticationResult.AuthenticationStatus.Succeeded){
                    Log.d(TAG, "Silent acquire token Authentication Result is invalid, retrying with interactive");
                    /* retry with interactive */
                    mAcquireTokenHandler.sendEmptyMessage(MSG_INTERACTIVE_SIGN_IN_PROMPT_AUTO);
                    return;
                }
                /* Successfully got a token, call graph now */
                Log.d(TAG, "Successfully authenticated");
                /* Store the mAuthResult */
                mAuthResult = authenticationResult;
                /* call API */
                callApi(mAuthResult);

                /* update the UI to post call graph state */
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        updateSuccessUI();
//                    }
//                });
            }

            @Override
            public void onError(Exception exception) {
                /* Failed to acquireToken */
                Log.e(TAG, "Authentication failed: " + exception.toString());
                if (exception instanceof AuthenticationException) {
                    AuthenticationException authException = ((AuthenticationException) exception);
                    ADALError error = authException.getCode();
                    logHttpErrors(authException);
                    /*  Tokens expired or no session, retry with interactive */
                    if (error == ADALError.AUTH_REFRESH_FAILED_PROMPT_NOT_ALLOWED ) {
                        mAcquireTokenHandler.sendEmptyMessage(MSG_INTERACTIVE_SIGN_IN_PROMPT_AUTO);
                    }else if(error == ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION){
                        /* Device is in Doze mode or App is in stand by mode.
                           Wake up the app or show an appropriate prompt for the user to take action
                           More information on this : https://github.com/AzureAD/azure-activedirectory-library-for-android/wiki/Handle-Doze-and-App-Standby */
                        Log.e(TAG, "Device is in doze mode or the app is in standby mode");
                    }
                    return;
                }
                /* Attempt an interactive on any other exception */
                mAcquireTokenHandler.sendEmptyMessage(MSG_INTERACTIVE_SIGN_IN_PROMPT_AUTO);
            }
        };
    }

    private AuthenticationCallback<AuthenticationResult> getTechLoungeAuthSilentCallback() {
        return new AuthenticationCallback<AuthenticationResult>() {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult) {
                if(authenticationResult==null || TextUtils.isEmpty(authenticationResult.getAccessToken())
                        || authenticationResult.getStatus()!= AuthenticationResult.AuthenticationStatus.Succeeded){
                    Log.d(TAG, "TechLounge: Silent acquire token Authentication Result is invalid, retrying with interactive");
                    /* retry with interactive */
                    mAcquireTokenHandler.sendEmptyMessage(MSG_INTERACTIVE_SIGN_IN_PROMPT_AUTO);
                    return;
                }
                /* Successfully got a token, call graph now */
                Log.d(TAG, "TechLounge: Successfully authenticated");
                /* Store the mAuthResult */
                mAuthResult = authenticationResult;
                /* call API */
                callTechApi(mAuthResult);

                /* update the UI to post call graph state */
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        updateSuccessUI();
//                    }
//                });
            }

            @Override
            public void onError(Exception exception) {
                /* Failed to acquireToken */
                Log.e(TAG, "Tech Authentication failed: " + exception.toString());
                if (exception instanceof AuthenticationException) {
                    AuthenticationException authException = ((AuthenticationException) exception);
                    ADALError error = authException.getCode();
                    logHttpErrors(authException);
                    /*  Tokens expired or no session, retry with interactive */
                    if (error == ADALError.AUTH_REFRESH_FAILED_PROMPT_NOT_ALLOWED ) {
                        mAcquireTokenHandler.sendEmptyMessage(MSG_INTERACTIVE_SIGN_IN_PROMPT_AUTO);
                    }else if(error == ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION){
                        /* Device is in Doze mode or App is in stand by mode.
                           Wake up the app or show an appropriate prompt for the user to take action
                           More information on this : https://github.com/AzureAD/azure-activedirectory-library-for-android/wiki/Handle-Doze-and-App-Standby */
                        Log.e(TAG, "Device is in doze mode or the app is in standby mode");
                    }
                    return;
                }
                /* Attempt an interactive on any other exception */
                mAcquireTokenHandler.sendEmptyMessage(MSG_INTERACTIVE_SIGN_IN_PROMPT_AUTO);
            }
        };
    }


    private void logHttpErrors(AuthenticationException authException){
        int httpResponseCode = authException.getServiceStatusCode();
        Log.d(TAG , "HTTP Response code: " + authException.getServiceStatusCode());
        if(httpResponseCode< 200 || httpResponseCode >300) {
            // logging http response headers in case of a http error.
            HashMap<String, List<String>> headers = authException.getHttpResponseHeaders();
            if (headers != null) {
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                    sb.append(entry.getKey());
                    sb.append(":");
                    sb.append(entry.getValue().toString());
                    sb.append("; ");
                }
                Log.e(TAG, "HTTP Response headers: " + sb.toString());
            }
        }
    }

    /* Callback used for interactive request.  If succeeds we use the access
     * token to call the Microsoft Graph. Does not check cache
     */
    private AuthenticationCallback<AuthenticationResult> getAuthInteractiveCallback() {
        return new AuthenticationCallback<AuthenticationResult>() {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult) {
                if(authenticationResult==null || TextUtils.isEmpty(authenticationResult.getAccessToken())
                        || authenticationResult.getStatus()!= AuthenticationResult.AuthenticationStatus.Succeeded){
                    Log.e(TAG, "Authentication Result is invalid");
                    return;
                }
                /* Successfully got a token, call graph now */
                Log.d(TAG, "Successfully authenticated");
                Log.d(TAG, "ID Token: " + authenticationResult.getIdToken());

                /* Store the auth result */
                mAuthResult = authenticationResult;

                /* Store User id to SharedPreferences to use it to acquire token silently later */
//                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                preferences.edit().putString(USER_ID, authenticationResult.getUserInfo().getUserId()).apply();

                /* Store User email to mDataCache */
//                mDataCache.saveEmail(authenticationResult.getUserInfo().getDisplayableId());


                /* update the UI to post call graph state */
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        updateSuccessUI();
//                    }
//                });
                /* set the sIntSignInInvoked boolean back to false  */
                sIntSignInInvoked.set(false);

                /* call API */
                callApi(mAuthResult);
            }

            @Override
            public void onError(Exception exception) {
                /* Failed to acquireToken */
                Log.e(TAG, "Authentication failed: " + exception.toString());
                if (exception instanceof AuthenticationException) {
                    ADALError  error = ((AuthenticationException)exception).getCode();
                    if(error==ADALError.AUTH_FAILED_CANCELLED){
                        Log.e(TAG, "The user cancelled the authorization request");
                    }else if(error== ADALError.AUTH_FAILED_NO_TOKEN){
                        // In this case ADAL has found a token in cache but failed to retrieve it.
                        // Retry interactive with Prompt.Always to ensure we do an interactive sign in
                        mAcquireTokenHandler.sendEmptyMessage(MSG_INTERACTIVE_SIGN_IN_PROMPT_ALWAYS);
                    }else if(error == ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION){
                        /* Device is in Doze mode or App is in stand by mode.
                           Wake up the app or show an appropriate prompt for the user to take action
                           More information on this : https://github.com/AzureAD/azure-activedirectory-library-for-android/wiki/Handle-Doze-and-App-Standby */
                        Log.e(TAG, "Device is in doze mode or the app is in standby mode");
                    }
                }
                /* set the sIntSignInInvoked boolean back to false  */
                sIntSignInInvoked.set(false);
            }
        };
    }

}
