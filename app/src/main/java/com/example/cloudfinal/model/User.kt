package com.example.cloudfinal.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(var username: String? = null, val email: String? = null, val uid: String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}