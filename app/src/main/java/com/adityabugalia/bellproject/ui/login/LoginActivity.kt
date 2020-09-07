package com.adityabugalia.bellproject.ui.login

import android.Manifest
import android.accounts.AccountManager
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.adityabugalia.bellproject.R
import com.adityabugalia.bellproject.ui.main.activities.DisplayActivity

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.YouTubeScopes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.IOException
import java.util.*


class LoginActivity : AppCompatActivity() {

    var mCredential: GoogleAccountCredential? = null
    var mProgress: ProgressDialog? = null
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    val RC_SIGN_IN: Int = 109
    val TAG: String = "aditya "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)


        val loading = findViewById<ProgressBar>(R.id.loading)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1071552502369-dl7chcr345hd6hbgrguge11j6pcpp97r.apps.googleusercontent.com")
            .requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        auth = Firebase.auth
        val googleSignInButton = findViewById<SignInButton>(R.id.sign_in_button)
        googleSignInButton.setSize(SignInButton.SIZE_STANDARD)

        googleSignInButton.setOnClickListener(View.OnClickListener {
            signIn()
        })
        mProgress = ProgressDialog(this)
        mProgress!!.setMessage("Calling YouTube Data API ...")
        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
            applicationContext,
            Arrays.asList(*LoginActivity.SCOPES)
        )
            .setBackOff(ExponentialBackOff())


    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_GOOGLE_PLAY_SERVICES -> if (resultCode != Activity.RESULT_OK) {
//                mOutputText!!.text = "This app requires Google Play Services. Please install " +
//                        "Google Play Services on your device and relaunch this app."
            } else {
                getResultsFromApi()
            }
            REQUEST_ACCOUNT_PICKER -> if (resultCode == Activity.RESULT_OK && data != null && data.extras != null
            ) {
                val accountName =
                    data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
                if (accountName != null) {
                    val settings =
                        getPreferences(Context.MODE_PRIVATE)
                    val editor = settings.edit()
                    editor.putString(LoginActivity.PREF_ACCOUNT_NAME, accountName)
                    editor.apply()
                    mCredential!!.selectedAccountName = accountName
                    getResultsFromApi()
                }
            }
            REQUEST_AUTHORIZATION -> if (resultCode == Activity.RESULT_OK) {
                getResultsFromApi()
            }
            RC_SIGN_IN-> if (resultCode==Activity.RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    if (account.idToken != null)
                        firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e)
                    // [START_EXCLUDE]
                    updateUI(null)
                    // [END_EXCLUDE]
                }
            }
        }
    }

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);





    fun getResultsFromApi() {
        if (!isGooglePlayServicesAvailable) {
            acquireGooglePlayServices()
        } else if (mCredential!!.selectedAccountName == null) {
            chooseAccount()
        } else if (!isDeviceOnline) {
           // mOutputText!!.text = "No network connection available."
        } else {
            MakeRequestTask(mCredential)
                .execute()
        }
    }


    /**
     * An asynchronous task that handles the YouTube Data API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private inner class MakeRequestTask internal constructor(credential: GoogleAccountCredential?) :
        AsyncTask<Void?, Void?, List<String?>?>() {
        private var mService: YouTube? = null
        private var mLastError: Exception? = null

        /**
         * Background task to call YouTube Data API.
         * @param params no parameters needed for this task.
         */
        // Get a list of up to 10 files.

        /**
         * Fetch information about the "GoogleDevelopers" YouTube channel.
         * @return List of Strings containing information about the channel.
         * @throws IOException
         */
        @get:Throws(IOException::class)
        private val dataFromApi: List<String?>
            private get() {
                // Get a list of up to 10 files.
                val channelInfo: MutableList<String?> =
                    ArrayList()
                val result =
                    mService!!.channels().list("snippet,contentDetails,statistics")
                        .setForUsername("GoogleDevelopers")
                        .execute()
                val channels =
                    result.items
                if (channels != null) {
                    val channel = channels[0]
                    channelInfo.add(
                       channel.id
                    )
//                    channelInfo.add(
//                        "This channel's ID is " + channel.id + ". " +
//                                "Its title is '" + channel.snippet.title + ", " +
//                                "and it has " + channel.statistics.viewCount + " views."
//                    )
                }
                return channelInfo
            }

        override fun onPreExecute() {
          //  mOutputText!!.text = ""
            mProgress!!.show()
        }

        override fun onPostExecute(result: List<String?>?) {
            mProgress!!.hide()
            if (result == null || result.size == 0) {
           //     mOutputText!!.text = "No results returned."
            } else {
                Log.e(TAG,  TextUtils.join("\n", result))


                val intent = Intent(this@LoginActivity, DisplayActivity::class.java)

intent.putExtra("channel",result.get(0) );
                startActivity(intent)
                this@LoginActivity.finish()

                // result.add(0, "Data retrieved using the YouTube Data API:")
             //   mOutputText!!.text = TextUtils.join("\n", result)
            }
        }

        override fun onCancelled() {
            mProgress!!.hide()
            if (mLastError != null) {
                if (mLastError is GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                        (mLastError as GooglePlayServicesAvailabilityIOException)
                            .connectionStatusCode
                    )
                } else if (mLastError is UserRecoverableAuthIOException) {
                    startActivityForResult(
                        (mLastError as UserRecoverableAuthIOException).intent,
                        REQUEST_AUTHORIZATION
                    )
                } else {
//                    mOutputText!!.text = """
//                        The following error occurred:
//                        ${mLastError!!.message}
//                        """.trimIndent()
                }
            } else {
               // mOutputText!!.text = "Request cancelled."
            }
        }

        init {
            val transport = AndroidHttp.newCompatibleTransport()
            val jsonFactory: JsonFactory =
                JacksonFactory.getDefaultInstance()
            mService = YouTube.Builder(
                transport, jsonFactory, credential
            )
                .setApplicationName("YouTube Data API Android Quickstart")
                .build()
        }

        override fun doInBackground(vararg p0: Void?): List<String?>? {
            return try {
                dataFromApi
            } catch (e: Exception) {
                mLastError = e
                cancel(true)
                null
            }
        }


    }
    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private val isDeviceOnline: Boolean
        private get() {
            val connMgr =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connMgr.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private fun chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS
            )
        ) {
            val accountName = getPreferences(Context.MODE_PRIVATE)
                .getString(LoginActivity.PREF_ACCOUNT_NAME, null)
            if (accountName != null) {
                mCredential!!.selectedAccountName = accountName
                getResultsFromApi()
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                    mCredential!!.newChooseAccountIntent(),
                    REQUEST_ACCOUNT_PICKER
                )
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                this,
                "This app needs to access your Google account (via Contacts).",
                REQUEST_PERMISSION_GET_ACCOUNTS,
                Manifest.permission.GET_ACCOUNTS
            )
        }
    }
    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     * date on this device; false otherwise.
     */
    private val isGooglePlayServicesAvailable: Boolean
        private get() {
            val apiAvailability = GoogleApiAvailability.getInstance()
            val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this)
            return connectionStatusCode == ConnectionResult.SUCCESS
        }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private fun acquireGooglePlayServices() {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode)
        }
    }
    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     * Google Play Services on this device.
     */
    fun showGooglePlayServicesAvailabilityErrorDialog(
        connectionStatusCode: Int
    ) {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val dialog = apiAvailability.getErrorDialog(
            this@LoginActivity,
            connectionStatusCode,
            REQUEST_GOOGLE_PLAY_SERVICES
        )
        dialog.show()
    }

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        // [START_EXCLUDE silent]
        // showProgressBar()
        // [END_EXCLUDE]
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    // [START_EXCLUDE]
                    //   val view = binding.mainLayout
                    //  // [END_EXCLUDE]
                    //  Snackbar.make(view, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    updateUI(null)
                }

                // [START_EXCLUDE]
                // hideProgressBar()
                // [END_EXCLUDE]
            }
    }
    // [END auth_with_google]


    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun updateUI(account: FirebaseUser?) {
        //account.displayName;
        if(account?.email!=null) {
            var email =""
            mCredential!!.selectedAccountName = account.email
        }
        getResultsFromApi()
        Log.e(TAG, account?.email + "")
    }


    companion object {
        const val REQUEST_ACCOUNT_PICKER = 1000
        const val REQUEST_AUTHORIZATION = 1001
        const val REQUEST_GOOGLE_PLAY_SERVICES = 1002
        const val REQUEST_PERMISSION_GET_ACCOUNTS = 1003
        private const val BUTTON_TEXT = "Call YouTube Data API"
        private const val PREF_ACCOUNT_NAME = "accountName"
        private val SCOPES =
            arrayOf(YouTubeScopes.YOUTUBE_READONLY)
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}