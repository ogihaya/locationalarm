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
public final class GetAllAlarmsUseCase_Factory implements Factory<GetAllAlarmsUseCase> {
  private final Provider<AlarmRepository> repositoryProvider;

  public GetAllAlarmsUseCase_Factory(Provider<AlarmRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetAllAlarmsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetAllAlarmsUseCase_Factory create(Provider<AlarmRepository> repositoryProvider) {
    return new GetAllAlarmsUseCase_Factory(repositoryProvider);
  }

  public static GetAllAlarmsUseCase newInstance(AlarmRepository repository) {
    return new GetAllAlarmsUseCase(repository);
  }
}
