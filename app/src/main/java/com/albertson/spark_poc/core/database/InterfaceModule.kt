package com.albertson.spark_poc.core.database

import com.albertson.spark_poc.data.repository.CommunityRepositoryImpl
import com.albertson.spark_poc.data.repository.MemberRepositoryImpl
import com.albertson.spark_poc.data.repository.ProductCommUserImpl
import com.albertson.spark_poc.data.repository.RegistrationImpl
import com.albertson.spark_poc.domain.repository.ICommunityRepository
import com.albertson.spark_poc.domain.repository.IMemberRepository
import com.albertson.spark_poc.domain.repository.IProductCommUserRepository
import com.albertson.spark_poc.domain.repository.IRegistration
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class InterfaceModule {
    @Binds
    abstract fun bindRepository(communityRepositoryImpl: CommunityRepositoryImpl) : ICommunityRepository

    @Binds
    abstract fun bindMemberRepository(memberRepositoryImpl: MemberRepositoryImpl) : IMemberRepository

    @Binds
    abstract fun bindRegistrationRepository(registrationImpl: RegistrationImpl) : IRegistration

    @Binds
    abstract fun bindProductCommUserRepository(productCommunityUserImpl: ProductCommUserImpl) : IProductCommUserRepository
}