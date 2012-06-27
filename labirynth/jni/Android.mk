LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := com.flexymind.labirynth.FXs.NativeSound
LOCAL_LDLIBS 	+= libOpenSLES -llog
### Add all source file names to be included in lib separated by a whitespace
LOCAL_SRC_FILES := com.flexymind.labirynth.FXs.NativeSound.cpp

include $(BUILD_SHARED_LIBRARY)
