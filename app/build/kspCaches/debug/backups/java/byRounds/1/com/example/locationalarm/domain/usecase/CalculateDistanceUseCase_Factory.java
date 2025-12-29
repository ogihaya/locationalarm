package com.example.locationalarm.domain.usecase;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class CalculateDistanceUseCase_Factory implements Factory<CalculateDistanceUseCase> {
  @Override
  public CalculateDistanceUseCase get() {
    return newInstance();
  }

  public static CalculateDistanceUseCase_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CalculateDistanceUseCase newInstance() {
    return new CalculateDistanceUseCase();
  }

  private static final class InstanceHolder {
    private static final CalculateDistanceUseCase_Factory INSTANCE = new CalculateDistanceUseCase_Factory();
  }
}
