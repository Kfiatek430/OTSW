package com.kfiatek.otsw.helpers;

import com.kfiatek.otsw.enums.TextTypes;

import java.util.EnumSet;
import java.util.LinkedHashSet;

public class Helper {
  public static EnumSet<TextTypes> convert(LinkedHashSet<TextTypes> hashSet) {
    return EnumSet.copyOf(hashSet);
  }
}
