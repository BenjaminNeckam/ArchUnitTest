package com.example.archUnit.lang;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

public class ComposingMemberRulesTest {

  JavaClasses importedClasses;

  @BeforeEach
  void onInit() {
    importedClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("com.example.archUnit");
  }

  @Test
  void privateFieldsShouldNotBeAnnotatedWithAutowired() {
    ArchRule rule = ArchRuleDefinition.fields().that().arePrivate().should()
        .notBeAnnotatedWith(Autowired.class);

    rule.check(importedClasses);
  }

  @Test
  void methodsInControllersShouldHaveAnnotations() {
    ArchRule rule = ArchRuleDefinition.methods().that().areDeclaredInClassesThat().resideInAPackage("..controller..")
        .should().beAnnotatedWith(GetMapping.class);

    rule.check(importedClasses);
  }
}
