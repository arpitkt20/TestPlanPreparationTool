package com.testPlanPreparationTool.service;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyLogger implements NativeKeyListener {

	@Override
	public void nativeKeyPressed(NativeKeyEvent key) {
		if(key.getKeyCode() == NativeKeyEvent.VC_PRINTSCREEN){
			CaptureShot shot = new CaptureShot();
			shot.takeScreenShot();
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent key) {
	}

}
