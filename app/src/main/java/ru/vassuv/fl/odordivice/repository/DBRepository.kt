@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package ru.vassuv.fl.odordivice.repository

object DBRepository {
    val dbHelper = DBHelper()

//    fun saveUser(user: JsonObject) {
//        dbHelper.use {
//            val id = user[Field.ID].asString().toLong()
//            val values = ContentValues()
//            values.put(Column.ID, id)
//            insertWithOnConflict(Table.USER, "", values, CONFLICT_IGNORE)
//
//            values.put(Column.NAME, user[Field.USER_NAME].asString())
//            values.put(Column.FULL_NAME, user[Field.FULL_NAME].asString())
//            values.put(Column.PHOTO, user[Field.PROFILE_PICTURE].asString())
//            values.put(Column.BIO, user[Field.BIO].asString())
//            values.put(Column.WEB_SITE, user[Field.WEBSITE].asString())
//            values.put(Column.MEDIA, user[Field.COUNTS].asObject()[Field.MEDIA].asInt())
//            values.put(Column.FOLLOWS, user[Field.COUNTS].asObject()[Field.FOLLOWS].asInt())
//            values.put(Column.FOLLOWED_BY, user[Field.COUNTS].asObject()[Field.FOLLOWED_BY].asInt())
//            updateWithOnConflict(Table.USER, values, null, null, CONFLICT_REPLACE)
//        }
//    }
//
//    fun user(id: Long, func: (User?) -> Unit) {
//        async(UI, CoroutineStart.DEFAULT) {
//            func(bg {
//                dbHelper.readableDatabase.select(Table.USER).whereSimple("${Column.ID}=$id").exec {
//                    if (moveToFirst()) {
//                        User(id, getString(getColumnIndex(Column.NAME)),
//                                getString(getColumnIndex(Column.FULL_NAME)),
//                                getString(getColumnIndex(Column.PHOTO)),
//                                getString(getColumnIndex(Column.BIO)),
//                                getString(getColumnIndex(Column.WEB_SITE)),
//                                getInt(getColumnIndex(Column.MEDIA)),
//                                getInt(getColumnIndex(Column.FOLLOWS)),
//                                getInt(getColumnIndex(Column.FOLLOWED_BY)))
//                    } else null
//                }
//            }.await())
//        }
//    }
}
