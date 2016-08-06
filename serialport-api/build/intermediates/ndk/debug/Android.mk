LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := serial_port
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	D:\android\EasyPay\serialport-api\src\main\jni\empty.c \
	D:\android\EasyPay\serialport-api\src\main\jni\SerialPort.c \

LOCAL_C_INCLUDES += D:\android\EasyPay\serialport-api\src\main\jni
LOCAL_C_INCLUDES += D:\android\EasyPay\serialport-api\src\debug\jni

include $(BUILD_SHARED_LIBRARY)
