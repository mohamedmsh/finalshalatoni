package com.example.cloudfinal.sign


import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.cloudfinal.DoctorActivity
import com.example.cloudfinal.PatientActivity
import com.example.cloudfinal.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val userTypes = arrayOf("Doctor", "Patient")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, userTypes)
        binding.spinnerUserType.adapter = adapter

        binding.buttonLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.buttonRegister.setOnClickListener {
            val userType = binding.spinnerUserType.selectedItem.toString()
            val email = binding.editTextEmail.text.toString()
            val name = binding.editTextName.text.toString()
            val password = binding.editTextPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()&& userType.isNotEmpty()){

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    if (user != null) {
                        // حفظ نوع المستخدم في Firebase Realtime Database
                        val userId = user.uid
                        val userRef = database.reference.child("users").child(userId)
                        userRef.child("userType").setValue(userType)
                        val userName = database.reference.child("users").child(userId)
                        userRef.child("userName").setValue(name)

                        // قم بتوجيه المستخدم إلى الواجهة المناسبة بعد التسجيل
                        if (userType == "Doctor") {
                            val intent = Intent(this, DoctorActivity::class.java)
                            startActivity(intent)
                        } else if (userType == "Patient") {
                            val intent = Intent(this, PatientActivity::class.java)
                            startActivity(intent)
                        }
                    }
                } else {
                    // حدث خطأ أثناء التسجيل
                    Toast.makeText(this, "فشل في التسجيل!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        }
    }
}