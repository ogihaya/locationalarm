package com.example.locationalarm.di;

import com.example.locationalarm.data.local.AlarmDao;
import com.example.locationalarm.data.local.AlarmDatabase;
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
public final class DatabaseModule_ProvideAlarmDaoFactory implements Factory<AlarmDao> {
  private final Provider<AlarmDatabase> databaseProvider;

  public DatabaseModule_ProvideAlarmDaoFactory(Provider<AlarmDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public AlarmDao get() {
    return provideAlarmDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideAlarmDaoFactory create(
      Provider<AlarmDatabase> databaseProvider) {
    return new DatabaseModule_ProvideAlarmDaoFactory(databaseProvider);
  }

  public static AlarmDao provideAlarmDao(AlarmDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAlarmDao(database));
  }
}
