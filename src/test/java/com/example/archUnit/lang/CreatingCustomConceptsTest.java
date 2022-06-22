package com.example.archUnit.lang;

import com.example.archUnit.domain.Product;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.AbstractClassesTransformer;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ClassesTransformer;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreatingCustomConceptsTest {

  JavaClasses importedClasses;

  @BeforeEach
  void onInit() {
    importedClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("com.example.archUnit");
  }

  ClassesTransformer<JavaClass> implClasses = new AbstractClassesTransformer<JavaClass>("classes that implements an interface") {
    @Override
    public Iterable<JavaClass> doTransform(JavaClasses javaClasses) {
      Set<JavaClass> result = new HashSet<>();
      for (JavaClass javaClass : javaClasses) {
        if (javaClass.getAllRawInterfaces().size() > 0) {
          result.add(javaClass);
        }
      }
      return result;
    }
  };

  ArchCondition<JavaClass> haveNameSuffixImpl = new ArchCondition<JavaClass>("have name ending with \"Impl\" ") {
    @Override
    public void check(JavaClass javaClass, ConditionEvents conditionEvents) {
      boolean endsWithImpl = javaClass.getSimpleName().endsWith("Impl");

      if (!endsWithImpl) {
        String message = String.format("%s does not have name ending with \"Impl\"", javaClass.getName());
        conditionEvents.add(SimpleConditionEvent.violated(javaClass, message));
      }
    }
  };


  @Test
  void allClassesThatImplementsAnInterfaceShouldHaveSuffixImpl() {
    ArchRule rule = ArchRuleDefinition.all(implClasses).should(haveNameSuffixImpl);

    rule.check(importedClasses);
  }
}
