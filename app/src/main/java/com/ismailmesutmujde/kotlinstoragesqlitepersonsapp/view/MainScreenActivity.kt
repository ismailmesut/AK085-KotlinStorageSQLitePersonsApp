package com.ismailmesutmujde.kotlinstoragesqlitepersonsapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.ismailmesutmujde.kotlinstoragesqlitepersonsapp.R
import com.ismailmesutmujde.kotlinstoragesqlitepersonsapp.adapter.PersonsRecyclerViewAdapter
import com.ismailmesutmujde.kotlinstoragesqlitepersonsapp.dao.PersonsDao
import com.ismailmesutmujde.kotlinstoragesqlitepersonsapp.database.DatabaseHelper
import com.ismailmesutmujde.kotlinstoragesqlitepersonsapp.databinding.ActivityMainScreenBinding
import com.ismailmesutmujde.kotlinstoragesqlitepersonsapp.model.Persons

class MainScreenActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var bindingMainScreen : ActivityMainScreenBinding

    private lateinit var personsList : ArrayList<Persons>
    private lateinit var adapterPersons : PersonsRecyclerViewAdapter
    private lateinit var dbh : DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMainScreen = ActivityMainScreenBinding.inflate(layoutInflater)
        val view = bindingMainScreen.root
        setContentView(view)

        bindingMainScreen.toolbar.title = "Persons Application"
        setSupportActionBar(bindingMainScreen.toolbar)

        bindingMainScreen.recyclerView.setHasFixedSize(true)
        bindingMainScreen.recyclerView.layoutManager = LinearLayoutManager(this)

        /*
        personsList = ArrayList()
        val p1 = Persons(1,"Ahmet", "888888")
        val p2 = Persons(2,"Zeynep", "666666")
        val p3 = Persons(3,"Ece", "333333")

        personsList.add(p1)
        personsList.add(p2)
        personsList.add(p3)
        */

        dbh = DatabaseHelper(this)

        getAllPersons()

        bindingMainScreen.fab.setOnClickListener {
            showAlert()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val item = menu?.findItem(R.id.action_search)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

    fun showAlert() {
        val design = LayoutInflater.from(this).inflate(R.layout.alert_design, null)
        val editTextPersonName = design.findViewById(R.id.editTextPersonName) as EditText
        val editTextPersonPhone = design.findViewById(R.id.editTextPersonPhone) as EditText

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Add Person")
        alertDialog.setView(design)
        alertDialog.setPositiveButton("Add") { dialogInterface, i ->
            val person_name = editTextPersonName.text.toString().trim()
            val person_phone = editTextPersonPhone.text.toString().trim()

            PersonsDao().addPerson(dbh, person_name, person_phone)
            getAllPersons()

            Toast.makeText(applicationContext, "${person_name} - ${person_phone}", Toast.LENGTH_SHORT).show()

        }
        alertDialog.setNegativeButton("Cancel") { dialogInterface, i ->


        }
        alertDialog.show()
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        search(query)
        Log.e("Sent Search", query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        search(newText)
        Log.e("As Letters Enter", newText)
        return true
    }

    fun getAllPersons() {
        personsList = PersonsDao().allPersons(dbh)

        adapterPersons = PersonsRecyclerViewAdapter(this, personsList, dbh)
        bindingMainScreen.recyclerView.adapter = adapterPersons
    }

    fun search(searchWord:String) {
        personsList = PersonsDao().searchPerson(dbh,searchWord)

        adapterPersons = PersonsRecyclerViewAdapter(this, personsList, dbh)
        bindingMainScreen.recyclerView.adapter = adapterPersons
    }

}