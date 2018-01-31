# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

aux_source_directory (src/main/cpp/ A_SRC)
aux_source_directory (src/main/cpp/customer/ B_SRC)
aux_source_directory (src/main/cpp/inc/ C_SRC)
aux_source_directory (src/main/cpp/sound/ D_SRC)


list(APPEND C_SRC ${D_SRC})
list(APPEND B_SRC ${C_SRC})
list(APPEND A_SRC ${B_SRC})

add_library( # Sets the name of the library.
             FunBox

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             ${A_SRC}
             )

#设置变量
set(libs "${CMAKE_SOURCE_DIR}/src/main/jni")
#添加预编译so库
add_library(fmod-lib SHARED IMPORTED )
#设置预编译so库文件
set_target_properties(  fmod-lib    PROPERTIES IMPORTED_LOCATION     "${libs}/${ANDROID_ABI}/libfmodL.so")

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

target_link_libraries( # Specifies the target library.
                       FunBox
                       fmod-lib
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )