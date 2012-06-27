#include "NativeEngine.h"
/*
 * NativeEngine.cpp
 *
 *  Created on: 27.06.2012
 *      Author: пк
 */

NativeEngine::NativeEngine(){
	SLresult res;
	// API will be thread-safe
	SLEngineOption engineOption[2] = { (SLuint32) SL_ENGINEOPTION_THREADSAFE,
	                                   (SLuint32) SL_BOOLEAN_TRUE };
	// Create SLObjectItf
	res = slCreateEngine(&m_engineObj, 1, engineOption, 0, NULL, NULL);
	checkResult(res);

	// Realizing OpenSL Engine in synchronous mode
	res = (*m_engineObj)->Realize(m_engineObj, SL_BOOLEAN_FALSE);
	checkResult(res);

	// Get SL Engine interface
	res = (*m_engineObj)->GetInterface(m_engineObj, SL_IID_ENGINE, &m_engineIntf);
	checkResult(res);

	// Create Output Mix object to be used by player. SLVolumeItf on OutputMix is absent on Android :(
	res = (*m_engineIntf)->CreateOutputMix(m_engineIntf, &m_outputObj, 0, NULL, NULL);
	checkResult(res);

	// Realizing Output Mix
	res = (*m_outputObj)->Realize(m_outputObj, SL_BOOLEAN_FALSE);
	checkResult(res);

	m_engReady = true;
}

void NativeEngine::checkResult(SLresult & res)
{
    if (res != SL_RESULT_SUCCESS) {
        LOGE("NativeSoundEngine","Error result!!! SoundEngine not ready to use");
    }
}
