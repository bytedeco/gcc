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

Below is an example that shows how you can do code generation with libgccjit 
and execute function calls via jit.

A full index of samples can be found in the [samples](samples) directory.

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
    // JNA required dependency for calling functions by address
    implementation("net.java.dev.jna:jna:5.5.0")
}

application {
    mainClass = 'JitExecution'
}
```

The `JitExecution.java` source file

```java
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
```

[javacpp-presets]: https://github.com/bytedeco/javacpp-presets#readme
[libgccjit]: https://gcc.gnu.org/wiki/JIT

