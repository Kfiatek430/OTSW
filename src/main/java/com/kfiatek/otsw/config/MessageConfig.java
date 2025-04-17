package com.kfiatek.otsw.config;

import com.kfiatek.otsw.enums.TextTypes;

import java.util.LinkedHashSet;

public class MessageConfig {
    public int count;
    public int length;
    public LinkedHashSet<TextTypes> characters;
    public int frequencyMs;
}
