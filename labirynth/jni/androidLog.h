/*
 * andoridLog.h
 *
 *  Created on: 27.06.2012
 *      Author: пк
 */

#ifndef ANDROIDLOG_H_
#define ANDROIDLOG_H_

#include <android/log.h>

#define LOGI(tag, text) __android_log_print(ANDROID_LOG_INFO,	tag, text)
#define LOGE(tag, text) __android_log_print(ANDROID_LOG_ERROR,	tag, text)
#define LOGV(tag, text) __android_log_print(ANDROID_LOG_VERBOSE,tag, text)
#define LOGD(tag, text) __android_log_print(ANDROID_LOG_DEBUG,	tag, text)

#endif /* ANDROIDLOG_H_ */
