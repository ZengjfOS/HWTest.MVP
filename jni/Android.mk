LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_SRC_FILES:= \
    AplexBuzz.c \
    i2c_data.c \
    I2CController.c \
    AplexGPIO.c \
    SerialPort.c \
    COM3Mode.c \
    stability.c 
    
LOCAL_C_INCLUDES += \
    $(JNI_H_INCLUDE) \
    
LOCAL_SHARED_LIBRARIES := \
    libandroid_runtime \
    libnativehelper \
    libcutils \
    libutils \
    liblog 


LOCAL_MODULE := libaplex
LOCAL_MODULE_TAGS := optional
include $(BUILD_SHARED_LIBRARY)