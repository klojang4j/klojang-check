package org.klojang.check;

import java.util.function.Function;

@FunctionalInterface
interface PrefabMsgFormatter extends Function<MsgArgs, String> {}
