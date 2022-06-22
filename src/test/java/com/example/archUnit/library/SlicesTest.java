package com.example.archUnit.library;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.dependencies.SliceAssignment;
import com.tngtech.archunit.library.dependencies.SliceIdentifier;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SlicesTest {

  JavaClasses importedClasses;

  SliceAssignment controllerPersistenceSlice = new SliceAssignment() {
    @Override
    public SliceIdentifier getIdentifierOf(JavaClass javaClass) {
      String packageName = javaClass.getPackageName();
      if (packageName.contains("com.example.archUnit.controller") || packageName.contains("com.example.archUnit.persistence")) {
        return SliceIdentifier.of("ControllerPersistenceSlice");
      }

      return SliceIdentifier.ignore();
    }

    @Override
    public String getDescription() {
      return "Controller and Persistence structure";
    }
  };

  @BeforeEach
  void onInit() {
    importedClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("com.example.archUnit");
  }

  @Test
  void allPackagesShouldBeFreeOfCycles() {
    ArchRule rule = SlicesRuleDefinition.slices().matching("..archUnit.(*)..").should().beFreeOfCycles();

    rule.check(importedClasses);
  }

  @Test
  void allClassesOfControllerAndPersistenceShouldNotDependOnEachOther() {
    ArchRule rule = SlicesRuleDefinition.slices().assignedFrom(controllerPersistenceSlice).should().notDependOnEachOther();

    rule.check(importedClasses);
  }

}
