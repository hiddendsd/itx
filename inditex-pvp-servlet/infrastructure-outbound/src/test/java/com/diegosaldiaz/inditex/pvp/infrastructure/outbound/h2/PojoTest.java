package com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2;

import com.openpojo.reflection.filters.FilterPackageInfo;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import org.junit.jupiter.api.Test;

class PojoTest {
  private static final String PACKAGE = "com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2.entity";

  @Test
  void testPojoStructureAndBehavior() {
    Validator validator = ValidatorBuilder.create()
        .with(new SetterTester())
        .with(new GetterTester())
        .build();

    validator.validate(PACKAGE, new FilterPackageInfo());
  }
}
