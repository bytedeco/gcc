#!/bin/bash
set -e

GCC_VERSION=10.2.0

mkdir -p build/$PLATFORM
cd build/$PLATFORM

if [ ! -e "gcc-$GCC_VERSION.tar.gz" ]; then
  curl -L -o "gcc-$GCC_VERSION.tar.gz" "https://ftp.gnu.org/gnu/gcc/gcc-$GCC_VERSION/gcc-$GCC_VERSION.tar.gz"
fi

rm -rf build install
tar --totals -xf "gcc-$GCC_VERSION.tar.gz"
mkdir build install

GCC_INSTALL_PREFIX=$(pwd)/install

case $PLATFORM in
  linux-ppc64le)
    export CC="powerpc64le-linux-gnu-gcc-10 -m64 -fPIC"
    export CXX="powerpc64le-linux-gnu-g++-10 -m64 -fPIC"

    cd gcc-$GCC_VERSION
    ./contrib/download_prerequisites
    cd ../build

    ../gcc-$GCC_VERSION/configure \
      --host=powerpc64le-linux-gnu \
      --target=powerpc64le-linux-gnu \
      --prefix=$INSTALL_PREFIX \
      --enable-checking=release \
      --enable-languages=jit \
      --enable-host-shared \
      --disable-bootstrap \
      --disable-multilib \
      --disable-nls
    make -j $MAKEJ
    make install
    ;;
  linux-x86_64)
    export CC="gcc -m64 -fPIC"
    export CXX="g++ -m64 -fPIC"

    cd gcc-$GCC_VERSION
    ./contrib/download_prerequisites
    cd ../build

    ../gcc-$GCC_VERSION/configure \
      --prefix=$GCC_INSTALL_PREFIX \
      --enable-checking=release \
      --enable-languages=jit \
      --enable-host-shared \
      --disable-bootstrap \
      --disable-multilib \
      --disable-nls
    make -j $MAKEJ
    make install
    ;;
  *)
    echo "Platform \"$PLATFORM\" is not supported."
    exit 1
    ;;
esac

cd ../../..