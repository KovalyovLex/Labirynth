/*
 * NativeEngine.h
 *
 *  Created on: 27.06.2012
 *      Author: пк
 */

#ifndef NATIVEENGINE_H_
#define NATIVEENGINE_H_

#ifdef __cplusplus
extern "C" {
#endif

#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
#include <SLES/OpenSLES_AndroidConfiguration.h>
#include "androidLog.h"

class NativeEngine{
public:
	NativeEngine();
	void loadSound();
	//~NativeEngine();
private:
	void checkResult(SLresult & res);

	SLObjectItf m_engineObj;
	SLEngineItf m_engineIntf;
	SLObjectItf m_outputObj;
	bool m_engReady;
};

#ifdef __cplusplus
}
#endif /* __cplusplus */

#endif /* NATIVEENGINE_H_ */
