package com.albertson.spark_poc.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.albertson.spark_poc.data.local.dao.CommunityDao
import com.albertson.spark_poc.data.local.dao.MemberDao
import com.albertson.spark_poc.data.local.dao.ProductCommUserDao
import com.albertson.spark_poc.data.local.dao.RegistrationDao
import com.albertson.spark_poc.data.local.entity.Community
import com.albertson.spark_poc.data.local.entity.Member
import com.albertson.spark_poc.data.local.entity.ProductCommunityUser
import com.albertson.spark_poc.data.local.entity.Registration

@Database(entities = arrayOf(Community::class,Member::class,ProductCommunityUser::class,
    Registration::class), version = 1, exportSchema = false)
abstract class AlbertsonRoomDatabase: RoomDatabase() {
    abstract fun communityDao(): CommunityDao
    abstract fun memberDao(): MemberDao
    abstract fun productCommunityUserDao(): ProductCommUserDao
    abstract fun registrationDao(): RegistrationDao

    companion object{

        @Volatile
        private var INSTANCE: AlbertsonRoomDatabase? = null

        fun getDatabase(context: Context): AlbertsonRoomDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlbertsonRoomDatabase::class.java,
                    "albertson_database"
                ).build()
                INSTANCE = instance

                instance
            }
        }
    }
}