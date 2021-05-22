import org.bytedeco.gcc.gccjit.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.libffi.ffi_cif;
import org.bytedeco.libffi.ffi_type;
import org.bytedeco.libffi.global.ffi;

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

        ffi_cif cif = new ffi_cif();
        PointerPointer<ffi_type> arguments = new PointerPointer<>(2);
        PointerPointer<LongPointer> values = new PointerPointer<>(2);
        LongPointer res = new LongPointer(1);

        arguments.put(0, ffi.ffi_type_sint());
        arguments.put(1, ffi.ffi_type_sint());
        values.put(0, new LongPointer(1).put(10));
        values.put(1, new LongPointer(1).put(20));

        if (ffi.ffi_prep_cif(cif, ffi.FFI_DEFAULT_ABI(), 2, ffi.ffi_type_sint(), arguments) != ffi.FFI_OK) {
            throw new IllegalStateException("Failed to prepare libffi cif");
        }
        ffi.ffi_call(cif, addr, res, values);

        System.out.println("Evaluating add(10, 20) with JIT results in: " + res.get());

        gcc_jit_result_release(result);
        gcc_jit_context_release(ctxt);

        args.deallocate();
        add_name.deallocate();
    }
}
