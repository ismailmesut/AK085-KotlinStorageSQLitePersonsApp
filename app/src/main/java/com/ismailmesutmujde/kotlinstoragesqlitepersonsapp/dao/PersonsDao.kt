package com.ismailmesutmujde.kotlinstoragesqlitepersonsapp.dao

import android.content.ContentValues
import com.ismailmesutmujde.kotlinstoragesqlitepersonsapp.database.DatabaseHelper
import com.ismailmesutmujde.kotlinstoragesqlitepersonsapp.model.Persons

class PersonsDao {

    fun addPerson(dbh: DatabaseHelper, person_name:String, person_phone:String) {
        val db = dbh.writableDatabase

        val values = ContentValues()
        values.put("person_name", person_name)
        values.put("person_phone", person_phone)

        db.insertOrThrow("persons", null, values)
        db.close()
    }

    fun allPersons(db:DatabaseHelper) : ArrayList<Persons> {
        val personsList = ArrayList<Persons>()
        val db = db.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM persons",null)

        while (cursor.moveToNext()) {
            val person = Persons(cursor.getInt(cursor.getColumnIndexOrThrow("person_id")),
                cursor.getString(cursor.getColumnIndexOrThrow("person_name")),
                cursor.getString(cursor.getColumnIndexOrThrow("person_phone")))
            personsList.add(person)
        }
        return personsList
    }

    fun updatePerson(db:DatabaseHelper, person_id:Int, person_name:String, person_phone:String) {
        val db = db.writableDatabase

        val values = ContentValues()
        values.put("person_name", person_name)
        values.put("person_phone", person_phone)

        db.update("persons", values,"person_id=?", arrayOf(person_id.toString()))
        db.close()
    }

    fun deletePerson(db:DatabaseHelper, person_id:Int) {
        val db = db.writableDatabase
        db.delete("persons", "person_id=?", arrayOf(person_id.toString()))
        db.close()
    }

    fun searchPerson(db:DatabaseHelper, searchWord:String) : ArrayList<Persons>{
        val personsList = ArrayList<Persons> ()
        val db = db.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM persons WHERE person_name like '%$searchWord%' ", null)

        while(cursor.moveToNext()) {
            val person = Persons (
                cursor.getInt(cursor.getColumnIndexOrThrow("person_id")),
                cursor.getString(cursor.getColumnIndexOrThrow("person_name")),
                cursor.getString(cursor.getColumnIndexOrThrow("person_phone"))
            )
            personsList.add(person)
        }
        return personsList
    }
}