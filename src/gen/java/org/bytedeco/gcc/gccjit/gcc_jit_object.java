// Targeted by JavaCPP version 1.5.5-SNAPSHOT: DO NOT EDIT THIS FILE

package org.bytedeco.gcc.gccjit;

import java.nio.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

import static org.bytedeco.gcc.global.gccjit.*;


/* An object created within a context.  Such objects are automatically
   cleaned up when the context is released.

   The class hierarchy looks like this:

     +- gcc_jit_object
	 +- gcc_jit_location
	 +- gcc_jit_type
	    +- gcc_jit_struct
	 +- gcc_jit_field
	 +- gcc_jit_function
	 +- gcc_jit_block
	 +- gcc_jit_rvalue
	     +- gcc_jit_lvalue
		 +- gcc_jit_param
	 +- gcc_jit_case
*/
@Opaque @Properties(inherit = org.bytedeco.gcc.presets.gccjit.class)
public class gcc_jit_object extends Pointer {
    /** Empty constructor. Calls {@code super((Pointer)null)}. */
    public gcc_jit_object() { super((Pointer)null); }
    /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */
    public gcc_jit_object(Pointer p) { super(p); }
}