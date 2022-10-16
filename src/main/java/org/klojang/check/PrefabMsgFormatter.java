package org.klojang.check;

import java.util.function.Function;

@FunctionalInterface
public interface PrefabMsgFormatter extends Function<MsgArgs, String> {}
