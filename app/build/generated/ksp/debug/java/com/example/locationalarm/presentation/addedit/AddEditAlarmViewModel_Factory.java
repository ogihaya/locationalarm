package com.example.locationalarm.presentation.addedit;

import androidx.lifecycle.SavedStateHandle;
import com.example.locationalarm.alarm.AlarmScheduler;
import com.example.locationalarm.domain.usecase.AddAlarmUseCase;
import com.example.locationalarm.domain.usecase.GetAlarmByIdUseCase;
import com.example.locationalarm.domain.usecase.UpdateAlarmUseCase;
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
public final class AddEditAlarmViewModel_Factory implements Factory<AddEditAlarmViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<GetAlarmByIdUseCase> getAlarmByIdUseCaseProvider;

  private final Provider<AddAlarmUseCase> addAlarmUseCaseProvider;

  private final Provider<UpdateAlarmUseCase> updateAlarmUseCaseProvider;

  private final Provider<AlarmScheduler> alarmSchedulerProvider;

  public AddEditAlarmViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetAlarmByIdUseCase> getAlarmByIdUseCaseProvider,
      Provider<AddAlarmUseCase> addAlarmUseCaseProvider,
      Provider<UpdateAlarmUseCase> updateAlarmUseCaseProvider,
      Provider<AlarmScheduler> alarmSchedulerProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.getAlarmByIdUseCaseProvider = getAlarmByIdUseCaseProvider;
    this.addAlarmUseCaseProvider = addAlarmUseCaseProvider;
    this.updateAlarmUseCaseProvider = updateAlarmUseCaseProvider;
    this.alarmSchedulerProvider = alarmSchedulerProvider;
  }

  @Override
  public AddEditAlarmViewModel get() {
    return newInstance(savedStateHandleProvider.get(), getAlarmByIdUseCaseProvider.get(), addAlarmUseCaseProvider.get(), updateAlarmUseCaseProvider.get(), alarmSchedulerProvider.get());
  }

  public static AddEditAlarmViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetAlarmByIdUseCase> getAlarmByIdUseCaseProvider,
      Provider<AddAlarmUseCase> addAlarmUseCaseProvider,
      Provider<UpdateAlarmUseCase> updateAlarmUseCaseProvider,
      Provider<AlarmScheduler> alarmSchedulerProvider) {
    return new AddEditAlarmViewModel_Factory(savedStateHandleProvider, getAlarmByIdUseCaseProvider, addAlarmUseCaseProvider, updateAlarmUseCaseProvider, alarmSchedulerProvider);
  }

  public static AddEditAlarmViewModel newInstance(SavedStateHandle savedStateHandle,
      GetAlarmByIdUseCase getAlarmByIdUseCase, AddAlarmUseCase addAlarmUseCase,
      UpdateAlarmUseCase updateAlarmUseCase, AlarmScheduler alarmScheduler) {
    return new AddEditAlarmViewModel(savedStateHandle, getAlarmByIdUseCase, addAlarmUseCase, updateAlarmUseCase, alarmScheduler);
  }
}
