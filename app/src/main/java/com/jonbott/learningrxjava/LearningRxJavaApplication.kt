package com.jonbott.learningrxjava

import android.app.Application
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import com.google.gson.Gson
import com.jonbott.learningrxjava.Common.fromJson
import com.jonbott.learningrxjava.ModelLayer.PersistenceLayer.LocalDatabase
import com.jonbott.learningrxjava.ModelLayer.PersistenceLayer.PersistenceLayer
import com.jonbott.learningrxjava.ModelLayer.PersistenceLayer.PhotoDescription
import com.jonbott.learningrxjava.SimpleExamples.SimpleRx
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LearningRxJavaApplication : Application() {

    companion object {
        lateinit var database: LocalDatabase
    }

    override fun onCreate() {
        super.onCreate()

        println("Simple App being used.")

//        SimpleRx.simpleValues()
//        SimpleRx.subjects()
//        SimpleRx.basicObservable()

        setupDatabase()
    }

    //region Database Setup Methods
    //handle migration
    //https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE photo_descriptions "
                    + " ADD COLUMN created_at INTEGER")
        }
    }

    fun setupDatabase() {
        LearningRxJavaApplication.database = Room.databaseBuilder(this, LocalDatabase::class.java, "LearningRxJavaLocalDatabase")
//                .fallbackToDestructiveMigration()
                .addMigrations(MIGRATION_1_2)
                .build()

        GlobalScope.launch {
            val photoDescriptions = loadJson()
            PersistenceLayer.shared.prepareDb(photoDescriptions)
        }
    }

    fun loadJson(): List<PhotoDescription> {
        val json = loadDescriptionsFromFile()
        val photoDescriptions = Gson().fromJson<List<PhotoDescription>>(json)
        return photoDescriptions
    }

    private fun loadDescriptionsFromFile(): String {
        //ignoring IOExceptions
        val inputStream = assets.open("PhotoDescription.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        val json = String(buffer, Charsets.UTF_8)
        return json
    }

    //endregion
}

