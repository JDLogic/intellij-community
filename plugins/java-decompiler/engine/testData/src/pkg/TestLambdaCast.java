// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package pkg;

import java.util.ArrayList;
import java.util.List;

public class TestLambdaCast {
  void foo(TestLambdaCastInterface inf) {}

  void bar() {
    foo((TestLambdaCastDerivedInterface)() -> {
      return 0;
    });

    List<TestLambdaCastInterface> list = new ArrayList<>();

    list.add((TestLambdaCastInterface)() -> {
      return "test";
    });
  }
}

interface TestLambdaCastInterface {
  String get();
}

interface TestLambdaCastDerivedInterface extends TestLambdaCastInterface {
  default String get() {
    return "test";
  }

  int getNum();
}
