package com.example.archUnit.core;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.Location;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.example.archUnit")
public class Junit5ImportTest {

  @ArchTest
  ArchRule classes_in_controller_should_not_have_name_uselesscontroller = ArchRuleDefinition.noClasses().that()
      .resideInAPackage("..controller..").should().haveNameMatching(".*UselessController");

  static final class DoNotIncludeUselessController implements ImportOption {

    @Override
    public boolean includes(Location location) {
      return !location.contains("/UselessController.class");
    }
  }
}
