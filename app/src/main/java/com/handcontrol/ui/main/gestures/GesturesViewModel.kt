package com.handcontrol.ui.main.gestures

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.handcontrol.api.Api
import com.handcontrol.api.PerformGesture
import com.handcontrol.model.Gesture
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GesturesViewModel : ViewModel() {
    val listData = MutableLiveData<MutableList<Gesture>>(mutableListOf())
    val errorConnection = MutableLiveData(false)

    init {
        updateGestures()
    }

    fun performGesture(id: Int) {
        PerformGesture().invoke(id)
    }

    private fun updateGestures() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listData.postValue(Api.getApiHandler().getGestures())
                errorConnection.postValue(false)
            } catch (e: StatusRuntimeException) {
                e.printStackTrace()
                errorConnection.postValue(true)
            }
        }
    }

}