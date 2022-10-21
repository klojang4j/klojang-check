package org.klojang.check.x.msg;

import java.util.function.Function;

@FunctionalInterface
public interface PrefabMsgFormatter extends Function<MsgArgs, String> {}
