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
public final class DeleteAlarmUseCase_Factory implements Factory<DeleteAlarmUseCase> {
  private final Provider<AlarmRepository> repositoryProvider;

  public DeleteAlarmUseCase_Factory(Provider<AlarmRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public DeleteAlarmUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static DeleteAlarmUseCase_Factory create(Provider<AlarmRepository> repositoryProvider) {
    return new DeleteAlarmUseCase_Factory(repositoryProvider);
  }

  public static DeleteAlarmUseCase newInstance(AlarmRepository repository) {
    return new DeleteAlarmUseCase(repository);
  }
}
