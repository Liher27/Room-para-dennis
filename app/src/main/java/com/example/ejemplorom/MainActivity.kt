package com.example.ejemplorom

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pojo.MyDatabase
import pojo.User

class MainActivity : AppCompatActivity() {

    private lateinit var userAdapter: ArrayAdapter<String>
    private lateinit var userListView: ListView
    private lateinit var userInfo: List<String>

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userListView = findViewById(R.id.workoutList)
        userAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)

        //Creamos la instancia de la base de datos
        val db = MyDatabase(this)
        GlobalScope.launch(Dispatchers.IO) {
            //Primero, añadimos el usuario para que la db no este vacia, en este caso es siempre el mismo...
            User(0, "Juan", "Perez").let { db.userDao().insert(it) }
            //despues, recogemos el arrayList de usuarios de la base de datos
            val users = db.userDao().getAll()
            //añadimos la informacion al array de strings
            userInfo = users.map { "${it.name} ${it.surname}" }
            GlobalScope.launch(Dispatchers.Main) {
                //Y añadimos la informacion del array de strings al adapter
                userAdapter = ArrayAdapter(
                    this@MainActivity,
                    android.R.layout.simple_list_item_1,
                    userInfo
                )
                //Le metemos el adapter a la listview, y listo
                userListView.adapter = userAdapter
            }
        }
    }
}