# JavaCPP Presets for GCC

Build status: ![gcc](https://github.com/supergrecko/gcc/workflows/gcc/badge.svg)

## Introduction

This repository contains the JavaCPP Presets module for

- libgccjit 10.2.0: [https://gcc.gnu.org/wiki/JIT][libgccjit]

Please refer to the [JavaCPP Presets README.md][javacpp-presets] file for more 
detailed information about the JavaCPP Presets.

> This preset is in development, snapshot artifacts and javadocs are coming 
> soon.

## Documentation

Java API documentation is currently not available.

## Builds

The preset currently supports the following platforms (more platforms coming 
soon)

- Linux (x86-64)
- Linux (PowerPC 64-bit little endian) (experimental)

Experimental builds are builds run in a custom build environment, separated 
from the rest of the JavaCPP Presets CI envs. This is a temporary solution and 
primarily a proof-of-concept. Please open an issue/discussion if you're in need 
of these architectures or if an architecture you need is not available.

## Sample Usage

This is an example showing how to build and generate code via libgccjit and 
outputting it into a dynamic library.
[/samples/code-generation/](samples/code-generation)

> Sample showing how to call JIT-generated functions is work-in-progress.

We can use Gradle to install the required dependencies and native binaries 
built by JavaCPP.

The `build.gradle` build file

```groovy
plugins {
    id("application")
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url 'https://oss.sonatype.org/content/repositories/snapshots/' }
}

dependencies {
    implementation("org.bytedeco:gcc-platform:10.2.0-1.5.5-SNAPSHOT")
}

application {
    mainClass = 'DylibGenerator'
}
```

The `DylibGenerator.java` source file

```java
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
```

> The below explanation assumes a Linux environment. I tested this on Linux 
> x86_64

The above program should output something similar to the following to the 
terminal window on x86_64. Assembler output for other architectures will 
obviously be different.

```asm
	.file	"fake.c"
	.text
	.globl	greet
	.type	greet, @function
greet:
.LFB0:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$16, %rsp
	movq	%rdi, -8(%rbp)
.L2:
	movabsq	$139992918400400, %rdi
	call	printf@PLT
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE0:
	.size	greet, .-greet
	.ident	"GCC: (GNU) 10.2.0"
	.section	.note.GNU-stack,"",@progbits
```

The `$139992918400400` operand to the movabsq instruction is expected as 
it refers to our `"hello %s\n"` string we created.

If we look in the directory we invoked the program in we should have a 
`libhello.so` file which is dynamically linkable. Looking inside the shared 
library file with `nm libhello.so | grep "greet"` we can see that the greet 
function is indeed there.

[javacpp-presets]: https://github.com/bytedeco/javacpp-presets#readme
[libgccjit]: https://gcc.gnu.org/wiki/JIT

