package com.example.locationalarm.di;

import android.content.Context;
import com.example.locationalarm.data.local.AlarmDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DatabaseModule_ProvideAlarmDatabaseFactory implements Factory<AlarmDatabase> {
  private final Provider<Context> contextProvider;

  public DatabaseModule_ProvideAlarmDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AlarmDatabase get() {
    return provideAlarmDatabase(contextProvider.get());
  }

  public static DatabaseModule_ProvideAlarmDatabaseFactory create(
      Provider<Context> contextProvider) {
    return new DatabaseModule_ProvideAlarmDatabaseFactory(contextProvider);
  }

  public static AlarmDatabase provideAlarmDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAlarmDatabase(context));
  }
}
