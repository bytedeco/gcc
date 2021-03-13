import org.bytedeco.gcc.gccjit.*;
import org.bytedeco.javacpp.*;

import static org.bytedeco.gcc.global.gccjit.*;

public class JitExecution {
    public static void main(String[] unused) {
        gcc_jit_context ctxt = gcc_jit_context_acquire();

        gcc_jit_type int_type = gcc_jit_context_get_type(ctxt, GCC_JIT_TYPE_INT);
        BytePointer add_name = new BytePointer("add");
        PointerPointer<gcc_jit_param> args = new PointerPointer<>(
            gcc_jit_context_new_param(ctxt, null, int_type, "a"),
            gcc_jit_context_new_param(ctxt, null, int_type, "b")
        );
        gcc_jit_function add = gcc_jit_context_new_function(ctxt, null, GCC_JIT_FUNCTION_EXPORTED, int_type, add_name
            , 2, args, 0);

        gcc_jit_block entry = gcc_jit_function_new_block(add, "entry");
        gcc_jit_param a_param = gcc_jit_function_get_param(add, 0);
        gcc_jit_param b_param = gcc_jit_function_get_param(add, 1);
        gcc_jit_rvalue sum = gcc_jit_context_new_binary_op(
            ctxt, null, GCC_JIT_BINARY_OP_PLUS, int_type,
            gcc_jit_param_as_rvalue(a_param),
            gcc_jit_param_as_rvalue(b_param)
        );
        gcc_jit_block_end_with_return(entry, null, sum);

        gcc_jit_result result = gcc_jit_context_compile(ctxt);
        Pointer addr = gcc_jit_result_get_code(result, "add");

        com.sun.jna.Pointer address = new com.sun.jna.Pointer(addr.address());
        com.sun.jna.Function func = com.sun.jna.Function.getFunction(address);

        Object call_sum = func.invoke(Integer.class, new Object[]{ 10, 20 });

        System.out.println("JIT compiling call add(10, 20) result: " + call_sum);

        gcc_jit_result_release(result);
        gcc_jit_context_release(ctxt);

        args.deallocate();
        add_name.deallocate();
    }
}
