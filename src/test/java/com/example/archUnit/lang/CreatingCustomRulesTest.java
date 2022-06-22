package com.example.archUnit.lang;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.base.Predicate;
import com.tngtech.archunit.core.domain.JavaAccess;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreatingCustomRulesTest {

  JavaClasses importedClasses;

  DescribedPredicate<JavaClass> resideInPackageService = new DescribedPredicate<>("resides in package ..service..") {
    @Override
    public boolean apply(JavaClass javaClass) {
      return javaClass.getName().contains(".service.");
    }
  };

  ArchCondition<JavaClass> notDependOnClassesInController =
      new ArchCondition<>("should depend on classes in ..controller..") {
        @Override
        public void check(JavaClass javaClass, ConditionEvents conditionEvents) {
          for (JavaAccess accessFromSelf : javaClass.getAccessesFromSelf()) {
            String targetName = accessFromSelf.getTargetOwner().getName();
            if(targetName.contains(".controller.")) {
              String message = String.format("How dare you to access controller by %s", accessFromSelf.getOrigin().getFullName());
              conditionEvents.add(SimpleConditionEvent.violated(accessFromSelf, message));
            }
          }
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
  void servicesShouldNotDependOnControllers() {
    ArchRule rule = ArchRuleDefinition.classes().that(resideInPackageService).should(notDependOnClassesInController);

    rule.check(importedClasses);
  }

}
