package com.example.myapplication
@Entity(tableName = "users")
 data class User {
     @primarykey(autoGenerate = true) val id: Int = 0
    val name: String
    val email: String
    val password: String
    val phoneNumber: String
    val faculty: String

}