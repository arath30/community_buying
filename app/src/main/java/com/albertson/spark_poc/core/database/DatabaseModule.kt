package com.albertson.spark_poc.core.database

import android.content.Context
import com.albertson.spark_poc.data.local.dao.CommunityDao
import com.albertson.spark_poc.data.local.dao.MemberDao
import com.albertson.spark_poc.data.local.dao.ProductCommUserDao
import com.albertson.spark_poc.data.local.dao.RegistrationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun provideCommunityDao(albertsonRoomDatabase: AlbertsonRoomDatabase): CommunityDao{
        return albertsonRoomDatabase.communityDao()
    }

    @Provides
    fun provideMemberDao(albertsonRoomDatabase: AlbertsonRoomDatabase): MemberDao{
        return albertsonRoomDatabase.memberDao()
    }

    @Provides
    fun provideRegistrationDao(albertsonRoomDatabase: AlbertsonRoomDatabase): RegistrationDao {
        return albertsonRoomDatabase.registrationDao()
    }

    @Provides
    fun provideProductCommunityUserDao(albertsonRoomDatabase: AlbertsonRoomDatabase): ProductCommUserDao{
        return albertsonRoomDatabase.productCommunityUserDao()
    }

    @Provides
    fun provideAlbertSonDatabase(@ApplicationContext context: Context) : AlbertsonRoomDatabase{
        return AlbertsonRoomDatabase.getDatabase(context)
    }

}