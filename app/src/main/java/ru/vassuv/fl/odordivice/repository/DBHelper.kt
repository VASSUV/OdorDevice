package ru.vassuv.fl.odordivice.repository

import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import ru.vassuv.fl.odordivice.App
import ru.vassuv.fl.odordivice.repository.constant.Column
import ru.vassuv.fl.odordivice.repository.constant.Table

class DBHelper : ManagedSQLiteOpenHelper(App.context, "MyInstagram", null, DBHelper.CURRENT_VERSION) {

    companion object {
        val CURRENT_VERSION = 20170617
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(Table.USER, true,
                Column.ID to INTEGER + PRIMARY_KEY,
                Column.NAME to TEXT,
                Column.FULL_NAME to TEXT,
                Column.PHOTO to TEXT,
                Column.BIO to TEXT,
                Column.WEB_SITE to TEXT,
                Column.MEDIA to INTEGER,
                Column.FOLLOWS to INTEGER,
                Column.FOLLOWED_BY to INTEGER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
}