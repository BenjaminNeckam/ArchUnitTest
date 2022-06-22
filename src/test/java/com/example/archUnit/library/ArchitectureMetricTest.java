package com.example.archUnit.library;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaPackage;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.library.metrics.ArchitectureMetrics;
import com.tngtech.archunit.library.metrics.ComponentDependencyMetrics;
import com.tngtech.archunit.library.metrics.LakosMetrics;
import com.tngtech.archunit.library.metrics.MetricsComponent;
import com.tngtech.archunit.library.metrics.MetricsComponents;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ArchitectureMetricTest {

  JavaClasses importedClasses;

  @BeforeEach
  void onInit() {
    importedClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("com.example.archUnit");
  }

  @Test
  void cumulativeDependencyMetric() {
    Set<JavaPackage> packages = importedClasses.getPackage("com.example.archUnit").getSubpackages();
    MetricsComponents<JavaClass> components = MetricsComponents.fromPackages(packages);

    LakosMetrics metrics = ArchitectureMetrics.lakosMetrics(components);

    System.out.println("CCD: " + metrics.getCumulativeComponentDependency());
    System.out.println("ACD: " + metrics.getAverageComponentDependency());
    System.out.println("RACD: " + metrics.getRelativeAverageComponentDependency());
    System.out.println("NCCD: " + metrics.getNormalizedCumulativeComponentDependency());
  }

  @Test
  void componentDependencyMetric() {
    Set<JavaPackage> packages = importedClasses.getPackage("com.example.archUnit").getSubpackages();
    MetricsComponents<JavaClass> components = MetricsComponents.fromPackages(packages);

    ComponentDependencyMetrics metrics = ArchitectureMetrics.componentDependencyMetrics(components);

    System.out.println("Ce: " + metrics.getEfferentCoupling("com.example.archUnit"));
    System.out.println("Ca: " + metrics.getAfferentCoupling("com.example.archUnit"));
    System.out.println("I: " + metrics.getInstability("ccom.example.archUnit"));
    System.out.println("A: " + metrics.getAbstractness("com.example.archUnit"));
    System.out.println("D: " + metrics.getNormalizedDistanceFromMainSequence("com.example.archUnit"));
  }
}
