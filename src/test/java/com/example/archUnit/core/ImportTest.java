package com.example.archUnit.core;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.archUnit.controller.UselessController;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ImportTest {

  JavaClasses importedClasses;

  @BeforeEach
  void onInit() {
    importedClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("com.example.archUnit");
  }

  @Test
  void whenClassesImportedCorrectly_thenTrue() {
    assertTrue(importedClasses.contain(UselessController.class));
  }

  ImportOption ignoreUselessController = new ImportOption() {
    @Override
    public boolean includes(Location location) {
      return !location.contains("/UselessController.class");
    }
  };

  ImportOption ignoreProductDao = new ImportOption() {
    @Override
    public boolean includes(Location location) {
      return !location.contains("/ProductDao.class");
    }
  };
}
