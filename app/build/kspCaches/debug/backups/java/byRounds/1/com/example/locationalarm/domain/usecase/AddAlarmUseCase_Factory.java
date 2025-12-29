package com.example.locationalarm.domain.usecase;

import com.example.locationalarm.domain.repository.AlarmRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class AddAlarmUseCase_Factory implements Factory<AddAlarmUseCase> {
  private final Provider<AlarmRepository> repositoryProvider;

  public AddAlarmUseCase_Factory(Provider<AlarmRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AddAlarmUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static AddAlarmUseCase_Factory create(Provider<AlarmRepository> repositoryProvider) {
    return new AddAlarmUseCase_Factory(repositoryProvider);
  }

  public static AddAlarmUseCase newInstance(AlarmRepository repository) {
    return new AddAlarmUseCase(repository);
  }
}
