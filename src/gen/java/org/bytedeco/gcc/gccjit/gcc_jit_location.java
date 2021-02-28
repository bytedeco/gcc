// Targeted by JavaCPP version 1.5.5-SNAPSHOT: DO NOT EDIT THIS FILE

package org.bytedeco.gcc.gccjit;

import java.nio.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

import static org.bytedeco.gcc.global.gccjit.*;


/* A gcc_jit_location encapsulates a source code location, so that
   you can (optionally) associate locations in your language with
   statements in the JIT-compiled code, allowing the debugger to
   single-step through your language.

   Note that to do so, you also need to enable
     GCC_JIT_BOOL_OPTION_DEBUGINFO
   on the gcc_jit_context.

   gcc_jit_location instances are optional; you can always pass
   NULL.  */
@Opaque @Properties(inherit = org.bytedeco.gcc.presets.gccjit.class)
public class gcc_jit_location extends Pointer {
    /** Empty constructor. Calls {@code super((Pointer)null)}. */
    public gcc_jit_location() { super((Pointer)null); }
    /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */
    public gcc_jit_location(Pointer p) { super(p); }
}
