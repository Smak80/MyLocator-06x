package ru.smak.mylocator.viewmodels

import android.app.Application
import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel

class MainViewModel(app: Application) : AndroidViewModel(app) {

    val location: String by mutableStateOf("Здесь будут ваши координаты")

    var showRequestDialog: Boolean by mutableStateOf(true)

}