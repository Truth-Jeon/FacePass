  #
# Copyright (c) 2016-2022 HYUNDAI TELECOM Co., Ltd. All rights reserved.
#
# This software is the confidential and proprietary information of
# HYUNDAI TELECOM ("Confidential Information"). You shall not disclose
# such Confidential Information and shall use it only in accordance with
# the terms of the license agreement you entered into with HYUNDAI TELECOM.
#

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

$(warning ###### PRODUCT_CONSTRUCTOR: $(PRODUCT_CONSTRUCTOR))
$(warning ###### DEVICE_NETWORK_SYSTEM: $(DEVICE_NETWORK_SYSTEM))

LOCAL_GRADLE_PROJECT_NAME := app

LOCAL_MODULE := FacePass
LOCAL_GRADLE_MODULE := app
LOCAL_MODULE_TAGS := optional
LOCAL_MODULE_SUFFIX := .apk
LOCAL_CERTIFICATE := platform
LOCAL_MODULE_CLASS := APPS
LOCAL_JAVA_LIBRARIES := hdtel_lib framework
include $(BUILD_GRADLE)

include $(CLEAR_VARS)
LOCAL_MODULE := permission_FacePass.xml
LOCAL_MODULE_CLASS := ETC
LOCAL_MODULE_TAGS := optional
LOCAL_MODULE_PATH := $(TARGET_OUT_ETC)/default-permissions
LOCAL_SRC_FILES := permission/permission_FacePass.xml
include $(BUILD_PREBUILT)