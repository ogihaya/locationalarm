package com.example.locationalarm.di;

import com.example.locationalarm.data.local.AlarmDao;
import com.example.locationalarm.domain.repository.AlarmRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideAlarmRepositoryFactory implements Factory<AlarmRepository> {
  private final Provider<AlarmDao> alarmDaoProvider;

  public DatabaseModule_ProvideAlarmRepositoryFactory(Provider<AlarmDao> alarmDaoProvider) {
    this.alarmDaoProvider = alarmDaoProvider;
  }

  @Override
  public AlarmRepository get() {
    return provideAlarmRepository(alarmDaoProvider.get());
  }

  public static DatabaseModule_ProvideAlarmRepositoryFactory create(
      Provider<AlarmDao> alarmDaoProvider) {
    return new DatabaseModule_ProvideAlarmRepositoryFactory(alarmDaoProvider);
  }

  public static AlarmRepository provideAlarmRepository(AlarmDao alarmDao) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAlarmRepository(alarmDao));
  }
}
