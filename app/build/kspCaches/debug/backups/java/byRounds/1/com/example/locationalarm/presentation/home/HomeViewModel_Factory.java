package com.example.locationalarm.presentation.home;

import android.content.Context;
import com.example.locationalarm.alarm.AlarmScheduler;
import com.example.locationalarm.domain.usecase.CalculateDistanceUseCase;
import com.example.locationalarm.domain.usecase.DeleteAlarmByIdUseCase;
import com.example.locationalarm.domain.usecase.GetAllAlarmsUseCase;
import com.example.locationalarm.domain.usecase.UpdateAlarmUseCase;
import com.google.android.gms.location.FusedLocationProviderClient;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<GetAllAlarmsUseCase> getAllAlarmsUseCaseProvider;

  private final Provider<UpdateAlarmUseCase> updateAlarmUseCaseProvider;

  private final Provider<DeleteAlarmByIdUseCase> deleteAlarmByIdUseCaseProvider;

  private final Provider<CalculateDistanceUseCase> calculateDistanceUseCaseProvider;

  private final Provider<AlarmScheduler> alarmSchedulerProvider;

  private final Provider<FusedLocationProviderClient> fusedLocationClientProvider;

  private final Provider<Context> contextProvider;

  public HomeViewModel_Factory(Provider<GetAllAlarmsUseCase> getAllAlarmsUseCaseProvider,
      Provider<UpdateAlarmUseCase> updateAlarmUseCaseProvider,
      Provider<DeleteAlarmByIdUseCase> deleteAlarmByIdUseCaseProvider,
      Provider<CalculateDistanceUseCase> calculateDistanceUseCaseProvider,
      Provider<AlarmScheduler> alarmSchedulerProvider,
      Provider<FusedLocationProviderClient> fusedLocationClientProvider,
      Provider<Context> contextProvider) {
    this.getAllAlarmsUseCaseProvider = getAllAlarmsUseCaseProvider;
    this.updateAlarmUseCaseProvider = updateAlarmUseCaseProvider;
    this.deleteAlarmByIdUseCaseProvider = deleteAlarmByIdUseCaseProvider;
    this.calculateDistanceUseCaseProvider = calculateDistanceUseCaseProvider;
    this.alarmSchedulerProvider = alarmSchedulerProvider;
    this.fusedLocationClientProvider = fusedLocationClientProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(getAllAlarmsUseCaseProvider.get(), updateAlarmUseCaseProvider.get(), deleteAlarmByIdUseCaseProvider.get(), calculateDistanceUseCaseProvider.get(), alarmSchedulerProvider.get(), fusedLocationClientProvider.get(), contextProvider.get());
  }

  public static HomeViewModel_Factory create(
      Provider<GetAllAlarmsUseCase> getAllAlarmsUseCaseProvider,
      Provider<UpdateAlarmUseCase> updateAlarmUseCaseProvider,
      Provider<DeleteAlarmByIdUseCase> deleteAlarmByIdUseCaseProvider,
      Provider<CalculateDistanceUseCase> calculateDistanceUseCaseProvider,
      Provider<AlarmScheduler> alarmSchedulerProvider,
      Provider<FusedLocationProviderClient> fusedLocationClientProvider,
      Provider<Context> contextProvider) {
    return new HomeViewModel_Factory(getAllAlarmsUseCaseProvider, updateAlarmUseCaseProvider, deleteAlarmByIdUseCaseProvider, calculateDistanceUseCaseProvider, alarmSchedulerProvider, fusedLocationClientProvider, contextProvider);
  }

  public static HomeViewModel newInstance(GetAllAlarmsUseCase getAllAlarmsUseCase,
      UpdateAlarmUseCase updateAlarmUseCase, DeleteAlarmByIdUseCase deleteAlarmByIdUseCase,
      CalculateDistanceUseCase calculateDistanceUseCase, AlarmScheduler alarmScheduler,
      FusedLocationProviderClient fusedLocationClient, Context context) {
    return new HomeViewModel(getAllAlarmsUseCase, updateAlarmUseCase, deleteAlarmByIdUseCase, calculateDistanceUseCase, alarmScheduler, fusedLocationClient, context);
  }
}
