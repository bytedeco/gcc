package org.bytedeco.gcc.presets;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(
    value = {
        @Platform(include = "<libgccjit.h>", link = "gccjit@.0")
    },
    target = "org.bytedeco.gcc",
    global = "org.bytedeco.gcc.global.gccjit"
)
public class gccjit implements InfoMapper {
    static {
        Loader.checkVersion("org.bytedeco", "gcc");
    }

    public void map(InfoMap infoMap) {
        infoMap.put(new Info("gcc_jit_block").pointerTypes("gcc_jit_block"))
            .put(new Info("gcc_jit_case").pointerTypes("gcc_jit_case"))
            .put(new Info("gcc_jit_context").pointerTypes("gcc_jit_context"))
            .put(new Info("gcc_jit_field").pointerTypes("gcc_jit_field"))
            .put(new Info("gcc_jit_function").pointerTypes("gcc_jit_function"))
            .put(new Info("gcc_jit_location").pointerTypes("gcc_jit_location"))
            .put(new Info("gcc_jit_lvalue").pointerTypes("gcc_jit_lvalue"))
            .put(new Info("gcc_jit_object").pointerTypes("gcc_jit_object"))
            .put(new Info("gcc_jit_param").pointerTypes("gcc_jit_param"))
            .put(new Info("gcc_jit_result").pointerTypes("gcc_jit_result"))
            .put(new Info("gcc_jit_rvalue").pointerTypes("gcc_jit_rvalue"))
            .put(new Info("gcc_jit_struct").pointerTypes("gcc_jit_struct"))
            .put(new Info("gcc_jit_timer").pointerTypes("gcc_jit_timer"))
            .put(new Info("gcc_jit_type").pointerTypes("gcc_jit_type"));
    }
}
