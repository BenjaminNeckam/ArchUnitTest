package com.example.archUnit;

import com.tngtech.archunit.junit.AnalyzeClasses;
import org.junit.jupiter.api.BeforeEach;

@AnalyzeClasses(packages = "com.example.archUnit")
public class LayerDependencyRuleTest {

  @BeforeEach
  void onInit() {

  }
}
