// Targeted by JavaCPP version 1.5.7-SNAPSHOT: DO NOT EDIT THIS FILE

package org.bytedeco.gcc.gccjit;

import java.nio.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

import static org.bytedeco.gcc.global.gccjit.*;


/* A gcc_jit_lvalue is a storage location within your code (e.g. a
   variable, a parameter, etc).  It is also a gcc_jit_rvalue; use
   gcc_jit_lvalue_as_rvalue to cast.  */
@Opaque @Properties(inherit = org.bytedeco.gcc.presets.gccjit.class)
public class gcc_jit_lvalue extends Pointer {
    /** Empty constructor. Calls {@code super((Pointer)null)}. */
    public gcc_jit_lvalue() { super((Pointer)null); }
    /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */
    public gcc_jit_lvalue(Pointer p) { super(p); }
}
