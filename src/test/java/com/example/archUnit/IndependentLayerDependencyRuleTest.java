package com.example.archUnit;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.Location;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import lombok.ToString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IndependentLayerDependencyRuleTest {

  JavaClasses javaClasses;

  ImportOption ignoreUselessController = new ImportOption() {
    @Override
    public boolean includes(Location location) {
      return !location.contains("/UselessController.class");
    }
  };

  @BeforeEach
  void onInit() {
    javaClasses = new ClassFileImporter()
        .withImportOption(ignoreUselessController)
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("com.example.archUnit");
  }

//  @Test
//  public void controllers_should_not_be_accessed_by_any_service() {
//    ArchRule rule = ArchRuleDefinition.noClasses()
//        .that().resideInAPackage("..controller..")
//        .should(new ArchCondition<JavaClass>() {
//          @Override
//          public void check(JavaClass javaClass, ConditionEvents conditionEvents) {
//            // TODO write check that filters if calls to controller happens
//          }
//        });
//  }

  @Test
  public void services_should_not_depend_on_controllers() {
    ArchRule rule = ArchRuleDefinition.noClasses()
        .that().resideInAPackage("..service..")
        .should().dependOnClassesThat().resideInAPackage("..controller..");

    rule.check(javaClasses);
  }


}
