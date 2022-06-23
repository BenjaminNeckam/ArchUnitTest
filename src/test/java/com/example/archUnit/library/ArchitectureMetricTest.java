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
import com.tngtech.archunit.library.metrics.VisibilityMetrics;
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

  // Good for finding cycles within components
  @Test
  void cumulativeDependencyMetric() {
    Set<JavaPackage> packages = importedClasses.getPackage("com.example.archUnit").getSubpackages();
    MetricsComponents<JavaClass> components = MetricsComponents.fromPackages(packages);

    LakosMetrics metrics = ArchitectureMetrics.lakosMetrics(components);

    System.out.println("The sum of all DependsOn values of all components (CCD): " + metrics.getCumulativeComponentDependency());

    System.out.println("The CCD divided by the number of all components (ACD): " + metrics.getAverageComponentDependency());

    System.out.println(
        "The ACD divided by the number of all components (RACD): " + metrics.getRelativeAverageComponentDependency());

    System.out.println(
        "The CCD of the system divided by the CCD of a balanced binary tree with the same number of components (NCCD): "
            + metrics.getNormalizedCumulativeComponentDependency());
  }

  @Test
  void componentDependencyMetric() {
    Set<JavaPackage> packages = importedClasses.getPackage("com.example.archUnit").getSubpackages();
    MetricsComponents<JavaClass> components = MetricsComponents.fromPackages(packages);

    ComponentDependencyMetrics metrics = ArchitectureMetrics.componentDependencyMetrics(components);

    System.out.println("The number of outgoing dependencies to any other component (Ce): " + metrics.getEfferentCoupling(
        "com.example.archUnit.service"));

    System.out.println("The number of incoming dependencies from any other component (Ca): " + metrics.getAfferentCoupling(
        "com.example.archUnit.service"));

    // value closer to 1, have many outgoing and not many incoming dependencies is unstable due to possibility of easy changes to the packages
    // value closer to 0, have many incoming and not many outgoing dependencies are rather difficult to modify due to their responsibility
    System.out.println("The relationship of outgoing dependencies to all dependencies (I): " + metrics.getInstability(
        "com.example.archUnit.service"));

    System.out.println("num(abstract_classes) / num(all_classes) in the component (A): " + metrics.getAbstractness(
        "com.example.archUnit.service"));

    System.out.println("The normalized distance from the ideal line between (A=1, I=0) and (A=0, I=1) (D): "
        + metrics.getNormalizedDistanceFromMainSequence("com.example.archUnit.service"));
  }

  @Test
  void visibilityMetric() {
    Set<JavaPackage> packages = importedClasses.getPackage("com.example.archUnit").getSubpackages();
    MetricsComponents<JavaClass> components = MetricsComponents.fromPackages(packages);

    VisibilityMetrics metrics = ArchitectureMetrics.visibilityMetrics(components);

    System.out.println("num(visible_elements) / num(all_elements) for each component (RV) : " + metrics.getRelativeVisibility(
        "com.example.archUnit.service"));

    System.out.println("The average of all RV values (ARV): " + metrics.getAverageRelativeVisibility());

    System.out.println(
        "num(visible_elements) / num(all_elements) over all components (GRV): " + metrics.getGlobalRelativeVisibility());
  }
}
