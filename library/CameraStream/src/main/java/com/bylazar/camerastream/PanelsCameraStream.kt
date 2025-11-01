package com.bylazar.camerastream

import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource


object PanelsCameraStream {
    fun startStream(source: CameraStreamSource, maxFps: Int) {
        Plugin.startStream(source, maxFps)
    }

    fun stopStream() {
        Plugin.stopStream()
    }
}