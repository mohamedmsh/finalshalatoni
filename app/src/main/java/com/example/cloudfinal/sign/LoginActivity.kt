package com.example.cloudfinal.sign


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cloudfinal.DoctorActivity
import com.example.cloudfinal.PatientActivity
import com.example.cloudfinal.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
   // private lateinit var database: FirebaseDatabase
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.buttonRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()){
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        val userId = user.uid
                        // التحقق من نوع المستخدم في Firebase Realtime Database
                        database = Firebase.database.reference
                        val userRef = database.child("users").child(userId)
                        userRef.child("userType").get().addOnSuccessListener { userType ->
                            if (userType != null && userType.value != null) {
                                val userTypeValue = userType.value.toString()
                                if (userTypeValue == "Doctor") {
                                    // توجيه المستخدم إلى واجهة الطبيب
                                    val intent = Intent(this, DoctorActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else if (userTypeValue == "Patient") {
                                    // توجيه المستخدم إلى واجهة المريض
                                    val intent = Intent(this, PatientActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                    }
                } else {
                    // حدث خطأ أثناء تسجيل الدخول
                    Toast.makeText(this, "فشل في تسجيل الدخول!", Toast.LENGTH_SHORT).show()
                }
            }
        }
      }
    }
}