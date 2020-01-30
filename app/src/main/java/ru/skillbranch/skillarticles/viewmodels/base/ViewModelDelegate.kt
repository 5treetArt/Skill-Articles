package ru.skillbranch.skillarticles.viewmodels.base

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewModelDelegate<T : ViewModel>(private val clazz: Class<T>, private val arg: Any?)
    : ReadOnlyProperty<FragmentActivity, T> {

    //private var value: Int? = null
    override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T {
        val vmFactory = ViewModelFactory(arg)
        return ViewModelProviders.of(thisRef, vmFactory).get(clazz)
        //if (value == null) {
        //    val tv = TypedValue()
        //    if (thisRef.theme.resolveAttribute(res, tv, true)) value = tv.data
        //    else throw Resources.NotFoundException("Resource with id $res not found")
        //}
        //return value!!
    }

}