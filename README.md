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

[javacpp-presets]: https://github.com/bytedeco/javacpp-presets#readme
[libgccjit]: https://gcc.gnu.org/wiki/JIT