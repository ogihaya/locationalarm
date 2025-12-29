package com.example.locationalarm.data.repository;

import com.example.locationalarm.data.local.AlarmDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class AlarmRepositoryImpl_Factory implements Factory<AlarmRepositoryImpl> {
  private final Provider<AlarmDao> alarmDaoProvider;

  public AlarmRepositoryImpl_Factory(Provider<AlarmDao> alarmDaoProvider) {
    this.alarmDaoProvider = alarmDaoProvider;
  }

  @Override
  public AlarmRepositoryImpl get() {
    return newInstance(alarmDaoProvider.get());
  }

  public static AlarmRepositoryImpl_Factory create(Provider<AlarmDao> alarmDaoProvider) {
    return new AlarmRepositoryImpl_Factory(alarmDaoProvider);
  }

  public static AlarmRepositoryImpl newInstance(AlarmDao alarmDao) {
    return new AlarmRepositoryImpl(alarmDao);
  }
}
