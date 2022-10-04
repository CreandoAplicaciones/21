package com.venago.en21.dias.save

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

class Prefs (val context: Context) {

    val SHARED_NAME="Mydbt"
    val SHARED_USER_NAME="username"
    val SHARED_VIP="vip"
    val SHARED_DAY="day"
    val SHARED_MEAL="meal"


    val storage=context.getSharedPreferences(SHARED_NAME,0)





    fun saveDay(dayNumber: Int){
        storage.edit().putInt(SHARED_DAY,dayNumber).apply()
    }

    fun getDay():Int{
        return  storage.getInt(SHARED_DAY,1)
    }

    fun saveMeat(meatName: String){
        storage.edit().putString(SHARED_MEAL,meatName).apply()
    }

    fun getMeat():String{
        return  storage.getString(SHARED_MEAL,"")!!
    }

    fun saveVIP(vip:Boolean){
        storage.edit().putBoolean(SHARED_VIP, vip).apply()
    }

    fun getVIP():Boolean{
        return  storage.getBoolean(SHARED_VIP,false)
    }


fun contextImagen(urlImagen: String, imageView: ImageView) {
    context?.let {
        Glide.with(it)
            .load(urlImagen)
            .centerCrop().into(imageView)
    }

}


}