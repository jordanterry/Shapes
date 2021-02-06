package uk.co.jordanterry.shapes.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PlayGamesAuthProvider
import dagger.android.AndroidInjection
import uk.co.jordanterry.shapes.R
import uk.co.jordanterry.shapes.StartGame
import javax.inject.Inject


class MenuActivity : AppCompatActivity() {

    @Inject
    lateinit var startGame: StartGame

    private lateinit var auth: FirebaseAuth

    private val bStartGame: Button by lazy {
        findViewById<Button>(R.id.bStartGame)
    }

//    private val bPlayServices: Button by lazy {
//        findViewById<Button>(R.id.bPlayServices)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
            .requestServerAuthCode(getString(R.string.default_web_client_id))
            .build()


//        bPlayServices.setOnClickListener {
//            val signInClient = GoogleSignIn.getClient(
//                this,
//                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN
//            )
//            val intent = signInClient.signInIntent
//            startActivityForResult(intent, RC_SIGN_IN)
//
//        }

        bStartGame.setOnClickListener {
            startGame.start(this)
        }
    }

    private fun firebaseAuthWithPlayGames(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithPlayGames:" + acct.id!!)

        val credential = PlayGamesAuthProvider.getCredential(acct.serverAuthCode!!)
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
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }

            }
    }

    private fun signInSilently() {
        val signInOptions = GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (GoogleSignIn.hasPermissions(account, *signInOptions.scopeArray)) {
            // Already signed in.
            // The signed in account is stored in the 'account' variable.
            val signedInAccount = account
            if (signedInAccount != null) {
                firebaseAuthWithPlayGames(signedInAccount)
            }

        } else {
            // Haven't been signed-in before. Try the silent sign-in first.
            val signInClient = GoogleSignIn.getClient(this, signInOptions)
            signInClient
                .silentSignIn()
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {
                        // The signed in account is stored in the task's result.
                        val signedInAccount = task.result
                        firebaseAuthWithPlayGames(signedInAccount)
                    } else {
                        // Player will need to sign-in explicitly using via UI.
                        // See [sign-in best practices](http://developers.google.com/games/services/checklist) for guidance on how and when to implement Interactive Sign-in,
                        // and [Performing Interactive Sign-in](http://developers.google.com/games/services/android/signin#performing_interactive_sign-in) for details on how to implement
                        // Interactive Sign-in.
                    }
                }
        }
    }

    override fun onResume() {
        super.onResume()
        signInSilently()
    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
//        if (firebaseUser != null) {
//            bPlayServices.visibility = View.GONE
//        } else {
//            bPlayServices.visibility = View.VISIBLE
//        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result?.isSuccess == true) {
                // The signed in account is stored in the result.
                val signedInAccount = result.signInAccount
                if (signedInAccount != null) {
                    firebaseAuthWithPlayGames(signedInAccount)
                }
            } else {
                var message = result?.status?.statusMessage
                if (message == null || message.isEmpty()) {
                    message = "Something went wrong whilst singing in with Google."
                }
                AlertDialog.Builder(this).setMessage(message)
                    .setNeutralButton("OK", null).show()
            }
        }
    }
}

private const val RC_SIGN_IN = 1001
private const val TAG: String = "AG"