import org.bytedeco.gcc.gccjit.*;
import org.bytedeco.javacpp.*;

import static org.bytedeco.gcc.global.gccjit.*;

public class DylibGenerator {
    public static void main(String[] args) {
        gcc_jit_context ctxt = gcc_jit_context_acquire();
        gcc_jit_context_set_bool_option(ctxt, GCC_JIT_BOOL_OPTION_DUMP_GENERATED_CODE, 1);

        create_code(ctxt);

        gcc_jit_context_compile_to_file(ctxt, GCC_JIT_OUTPUT_KIND_DYNAMIC_LIBRARY, "libhello.so");
        gcc_jit_context_release(ctxt);
    }

    public static void create_code(gcc_jit_context ctxt) {
        gcc_jit_type void_type = gcc_jit_context_get_type(ctxt, GCC_JIT_TYPE_VOID);
        gcc_jit_type const_char_ptr_type = gcc_jit_context_get_type(ctxt, GCC_JIT_TYPE_CONST_CHAR_PTR);
        gcc_jit_type int_type = gcc_jit_context_get_type(ctxt, GCC_JIT_TYPE_INT);
        gcc_jit_param param_name = gcc_jit_context_new_param(ctxt, null, const_char_ptr_type, "name");
        gcc_jit_param[] func_params = { param_name };

        PointerPointer<gcc_jit_param> func_params_ptr = new PointerPointer<>(func_params);
        BytePointer func_name = new BytePointer("greet");
        gcc_jit_function greet_func = gcc_jit_context_new_function(ctxt, null, GCC_JIT_FUNCTION_EXPORTED, void_type,
            func_name, func_params.length, func_params_ptr, 0);

        gcc_jit_param param_format = gcc_jit_context_new_param(ctxt, null, const_char_ptr_type, "format");
        gcc_jit_param[] printf_params = { param_format };

        PointerPointer<gcc_jit_param> printf_params_ptr = new PointerPointer<>(printf_params);
        BytePointer printf_name = new BytePointer("printf");
        gcc_jit_function printf_func = gcc_jit_context_new_function(ctxt, null, GCC_JIT_FUNCTION_IMPORTED, int_type,
            printf_name, printf_params.length, printf_params_ptr, 0);

        gcc_jit_block block = gcc_jit_function_new_block(greet_func, "entry");
        BytePointer format = new BytePointer("hello %s\\n");
        gcc_jit_rvalue format_rvalue = gcc_jit_context_new_rvalue_from_ptr(ctxt, const_char_ptr_type, format);
        gcc_jit_rvalue call = gcc_jit_context_new_call(ctxt, null, printf_func, 1, format_rvalue);
        gcc_jit_block_add_eval(block, null, call);

        gcc_jit_block_end_with_void_return(block, null);

        func_name.deallocate();
        func_params_ptr.deallocate();
        printf_name.deallocate();
        printf_params_ptr.deallocate();
        format.deallocate();
    }
}