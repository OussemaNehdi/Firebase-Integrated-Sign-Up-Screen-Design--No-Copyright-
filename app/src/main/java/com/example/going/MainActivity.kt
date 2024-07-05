package com.example.going



import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth // the handler of auth in firebase
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        val roundedCornerSize = 40

        setContent {
            val controller = LocalSoftwareKeyboardController.current
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient( //gradient background color
                            colors = listOf(
                                Color.White,
                                Color.Black,
                            ),
                            )
                    )){
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) { // to hide keyboard when clicking on box
                        detectTapGestures(onTap = {
                            controller?.hide()
                        })
                    },
            )
            {
                //Logo
                Image(
                    painter = painterResource(id = R.drawable.mylogo),
                    contentDescription = "my logo description"
                )
                //Sign Up
                //Email input
                var email by remember { mutableStateOf("") }
                var passwd by remember { mutableStateOf("") }
                var showPassword by remember { mutableStateOf(value = false) }

                TextField(
                    modifier = Modifier.padding(15.dp),
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text = resources.getString(R.string.email) ,
                        color = Color.Black) },
                    placeholder = { Text(resources.getString(R.string.putEmail)) },
                    shape = RoundedCornerShape(roundedCornerSize),

                    colors = TextFieldDefaults.textFieldColors( //tohide the underline
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor =  Color.Gray
                    )

                )


                //Password input


                TextField(
                    value = passwd,
                    onValueChange = { passwd = it },
                    label = { Text(text =resources.getString(R.string.passwd),
                        color = Color.Black ) },
                    placeholder = { Text(resources.getString(R.string.putPasswd)) },
                    shape = RoundedCornerShape(percent = roundedCornerSize),
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(), // hide the password conditional
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),//specify keyboard for password (like no numbers)
                    trailingIcon = { // the icon next to the body
                        if (showPassword) {
                            IconButton(onClick = { showPassword = false }) {
                                Icon(
                                    imageVector = Icons.Filled.Visibility,
                                    contentDescription = "hide_password"
                                )
                            }
                        } else {
                            IconButton(
                                onClick = { showPassword = true }) {

                                Icon(
                                    imageVector = Icons.Filled.VisibilityOff,
                                    contentDescription = "hide_password"
                                )
                            }
                        }
                    } ,
                    colors = TextFieldDefaults.textFieldColors( //TODO FIX EXPERIMENTAL
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = Color.Gray // cursor color
                        ),


                )
                //Line of seperation
                //Divider()
                //User agreement
                var signupState by remember { mutableStateOf(false) }
                Row (
                    modifier = Modifier.padding(top = 40.dp)
                )
                {//

                    // Checkbox

                    var checkedState by remember { mutableStateOf(false) }
                    Checkbox(
                        modifier = Modifier.offset(0.dp , -13.dp),
                        checked = checkedState,
                        onCheckedChange = {
                            checkedState = it
                            signupState = !(signupState)

                                          },
                        colors =  CheckboxDefaults.colors(
                            checkedColor = Color.Gray
                        )
                    )
                    //Text for Agreement
                    Text(text = resources.getString(R.string.iAgreeWith))
                    TextButton( modifier =  Modifier.offset(0.dp,-15.dp),
                        onClick = { /*TODO*/ }) {
                        Text(resources.getString(R.string.terms) , color = Color.Gray)
                    }
                    Text(resources.getString(R.string.and))
                    TextButton( modifier =  Modifier.offset(0.dp,-15.dp),
                        onClick = { /*TODO*/ }) {
                        Text(text = resources.getString(R.string.privacy), color = Color.Gray)
                    }

                }
                //SIGN UP Button
                Button(enabled = signupState , onClick = {
                    //to login a person task is returned as a response
                    auth.createUserWithEmailAndPassword(email, passwd).addOnCompleteListener(this@MainActivity ) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("taggingg", "createUserWithEmail:success")
                            val user = auth.currentUser
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("tagging", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()

                        }
                    }
                }  , colors = ButtonDefaults.buttonColors(containerColor = Color.White ,disabledContainerColor = Color.Gray ),


                ) {
                    Text(text = resources.getString(R.string.signUp) , color = Color.Black)
                }
                //clickable text button Already ? : Sign in TODO make the login activity and make differenet composes
                Row (modifier = Modifier.padding(top = 20.dp)){


                Text(text = resources.getString(R.string.haveAccount) , color  = Color.White)
                TextButton(modifier = Modifier.offset(0.dp , -15.dp) ,onClick = { /*TODO*/ }) {
                    Text(text = resources.getString(R.string.haveAccountClick) , color = Color.Gray)

                }
                }
            }
        }
        }
    }
}

