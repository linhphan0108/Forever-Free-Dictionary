package com.example.foreverfreedictionary.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.foreverfreedictionary.data.local.*

/**
 * The Room database for this app
 */
@Database(entities = [TblDictionary::class, TblHistory::class, TblWordOfTheDay::class,
    TblFavorite::class, TblReminder::class, TblMyVocabularyGroup::class, TblMyVocabulary::class],
        version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dictionaryDao(): DictionaryDao
    abstract fun wordOfTheDayDao(): WordOfTheDayDao
    abstract fun historyDao(): HistoryDao
    abstract fun favoriteDAo(): FavoriteDao
    abstract fun reminderDao(): ReminderDao
    abstract fun myVocabularyGroupDao(): MyVocabularyGroupDao
    abstract fun myVocabularyDao(): MyVocabularyDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        //todo this function must be implement later on.
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "forever-free-dictionary-db")
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
//                            super.onCreate(db)
//                            val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
//                            WorkManager.getInstance(application).enqueue(request)
                        }
                    })
                    .build()
        }
    }
}
