package com.example.archUnit.lang;

import com.example.archUnit.persistence.dao.ProductDao;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ComposingClassRulesTest {

  JavaClasses importedClasses;

  @BeforeEach
  void onInit() {
    importedClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("com.example.archUnit");
  }

  @Test
  void servicesShouldNotDependOnControllers() {
    ArchRule rule = ArchRuleDefinition.noClasses().that().resideInAPackage("..service..")
        .should().dependOnClassesThat().resideInAPackage("..controller..");

    rule.check(importedClasses);
  }

  @Test
  void classesInPackageControllerShouldHaveNameEndingWithController() {
    ArchRule rule = ArchRuleDefinition.classes().that().resideInAPackage("..controller..")
        .should().haveSimpleNameEndingWith("Controller");

    rule.check(importedClasses);
  }

  @Test
  void classesInPackageDaoShouldImplementProductDao() {
    ArchRule rule = ArchRuleDefinition.classes().that().resideInAPackage("..dao.impl..")
        .should().implement(ProductDao.class);

    rule.check(importedClasses);
  }
}
