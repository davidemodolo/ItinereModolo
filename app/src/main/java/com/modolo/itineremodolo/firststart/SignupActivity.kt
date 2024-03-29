package com.modolo.itineremodolo.firststart

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.modolo.itineremodolo.MainActivity
import com.modolo.itineremodolo.R
import com.modolo.itineremodolo.data.user.User
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignupActivity : AppCompatActivity() {
    var caid = ""
    var cama = ""
    var cate = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginViewModel.userList.observe(this, Observer { it ->
            it.forEach {
                if (it.sess == 1) {
                    txtTitle.text = "I TUOI DATI"
                    txtSignupEmail.setText(it.mail)
                    txtSignupEmail.isEnabled = false
                    txtSignupPsw1.isVisible = false
                    txtSignupPsw1.setText(it.pass)
                    txtSignupPsw2.isVisible = false
                    txtName.setText((it.name))
                    txtSurname.setText((it.surn))
                    txtBirthDate.text = it.data
                    favCar.setText(it.fcar)
                    favNumber.setText(it.numb)
                    favCircuit.setText(it.fcir)
                    hateCircuit.setText(it.hcir)
                    btnSignup.text = "SALVA MODIFICHE"
                    txtNoSnitch.text = "LOGOUT"
                    caid =it.caid
                    cama =it.cama
                    cate =it.cate
                    return@forEach
                }
            }
        })

        txtBirthDate.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    txtBirthDate.text = "$dayOfMonth/${month + 1}/$year"
                },
                1970,
                1,
                1
            ).show()
        }
        btnSignup.setOnClickListener {
            if (btnSignup.text == "SALVA MODIFICHE") {
                val mod = User(
                    txtSignupEmail.text.toString(),
                    1,
                    txtSignupPsw1.text.toString(),
                    txtName.text.toString(),
                    txtSurname.text.toString(),
                    txtBirthDate.text.toString(),
                    favNumber.text.toString(),
                    favCircuit.text.toString(),
                    hateCircuit.text.toString(),
                    favCar.text.toString(),
                    caid,
                    cama,
                    cate
                )
                loginViewModel.updateUser(mod)
                this.finish()
            } else {
                txtSignupEmail.background =
                    ResourcesCompat.getDrawable(this.resources, R.drawable.little_box, null)
                txtSignupPsw1.background = txtSignupEmail.background
                txtSignupPsw2.background = txtSignupEmail.background


                var signup = true
                if (txtSignupEmail.text.toString() == "" || !txtSignupEmail.text.toString().isEmailValid()) {
                    signup = false
                    Toast.makeText(this, "FORMATO MAIL ERRATO", Toast.LENGTH_SHORT)
                        .show()
                    txtSignupEmail.background = ResourcesCompat.getDrawable(
                        this.resources,
                        R.drawable.little_box_error,
                        null
                    )
                }
                if (txtSignupPsw1.text.toString() != txtSignupPsw2.text.toString() || txtSignupPsw1.text.toString() == "") {
                    txtSignupPsw1.background =
                        ResourcesCompat.getDrawable(
                            this.resources,
                            R.drawable.little_box_error,
                            null
                        )
                    txtSignupPsw2.background = txtSignupPsw1.background
                    if (txtSignupPsw1.text.toString() != "")
                        Toast.makeText(this, "LE PASSWORD NON COINCIDONO", Toast.LENGTH_SHORT)
                            .show()
                    signup = false
                }
                loginViewModel.userList.observe(this, Observer { it ->
                    it.forEach {
                        if (it.mail == txtSignupEmail.text.toString()) {
                            signup = false
                            Toast.makeText(this, "MAIL GIA' PRESENTE", Toast.LENGTH_SHORT).show()
                            txtSignupEmail.background = ResourcesCompat.getDrawable(
                                this.resources,
                                R.drawable.little_box_error,
                                null
                            )
                            return@forEach
                        }
                    }
                })
                if (signup) {
                    val new = User(
                        txtSignupEmail.text.toString(),
                        1,
                        txtSignupPsw1.text.toString(),
                        txtName.text.toString(),
                        txtSurname.text.toString(),
                        txtBirthDate.text.toString(),
                        favNumber.text.toString(),
                        favCircuit.text.toString(),
                        hateCircuit.text.toString(),
                        favCar.text.toString(),
                        caid,
                        cama,
                        cate
                    )
                    loginViewModel.insertUser(new)
                    val intent = Intent(baseContext, MainActivity::class.java)
                    intent.putExtra("utente", new)
                    this.finish()
                    startActivity(intent)
                } else {
                    scrollView.smoothScrollTo(0, 0)
                }
            }
        }

        txtNoSnitch.setOnClickListener {
            if (txtNoSnitch.text == "LOGOUT") {
                loginViewModel.logout()
                val intent = Intent(this, LoginActivity::class.java)
                this.startActivity(intent)
                finishAffinity()
            } else {
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse("http://www.simcareer.org/")
                startActivity(openURL)
            }
        }
    }
    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

}
