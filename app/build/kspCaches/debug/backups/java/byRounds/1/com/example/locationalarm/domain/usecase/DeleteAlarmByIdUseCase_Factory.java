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
public final class DeleteAlarmByIdUseCase_Factory implements Factory<DeleteAlarmByIdUseCase> {
  private final Provider<AlarmRepository> repositoryProvider;

  public DeleteAlarmByIdUseCase_Factory(Provider<AlarmRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public DeleteAlarmByIdUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static DeleteAlarmByIdUseCase_Factory create(
      Provider<AlarmRepository> repositoryProvider) {
    return new DeleteAlarmByIdUseCase_Factory(repositoryProvider);
  }

  public static DeleteAlarmByIdUseCase newInstance(AlarmRepository repository) {
    return new DeleteAlarmByIdUseCase(repository);
  }
}
