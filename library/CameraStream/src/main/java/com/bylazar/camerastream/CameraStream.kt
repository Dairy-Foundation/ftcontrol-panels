package com.bylazar.camerastream

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import com.bylazar.panels.Panels
import com.bylazar.panels.plugins.BasePluginConfig
import com.bylazar.panels.plugins.Plugin
import com.bylazar.panels.server.Socket
import com.qualcomm.ftccommon.FtcEventLoop
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource
import java.io.ByteArrayOutputStream
import org.firstinspires.ftc.robotcore.external.function.Consumer
import org.firstinspires.ftc.robotcore.external.function.Continuation
import kotlin.coroutines.resume

open class CameraStreamPluginConfig : BasePluginConfig() {
    var imageQuality = 50
    var defaultFPS = 15
}

object Plugin : Plugin<CameraStreamPluginConfig>(CameraStreamPluginConfig()) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var streamJob: Job? = null

    private lateinit var getClientsCount: () -> Int

    override fun onNewClient(client: Socket.ClientSocket) {
    }

    fun startStream(source: CameraStreamSource, maxFps: Int = config.defaultFPS) {
        stopStream()

        streamJob = scope.launch {
            while (isActive) {
                try {
                    val currentTimestamp = System.currentTimeMillis()

                    if (getClientsCount() == 0) {
                        delay(500)
                        continue
                    }

                    val bitmap = source.awaitFrame()

                    if(bitmap != null){
                        val jpegString = withContext(Dispatchers.Default) {
                            bitmapToJpegString(bitmap, config.imageQuality)
                        }

                        send("CAM_STREAM", jpegString)
                    }

                    if (maxFps > 0) {
                        val frameTimeMs = (1000.0 / maxFps).toLong()
                        val elapsedTime = System.currentTimeMillis() - currentTimestamp
                        val sleepTime = maxOf(0L, frameTimeMs - elapsedTime)
                        delay(sleepTime)
                    }

                } catch (e: Exception) {
                    if (e is kotlinx.coroutines.CancellationException) {
                        log("Stream job cancelled.")
                        break
                    }
                    log("Error in camera stream loop: ${e.message}")
                    delay(100)
                }
            }
        }
    }

    fun stopStream() {
        streamJob?.cancel()
        streamJob = null
    }

    private suspend fun CameraStreamSource.awaitFrame(): Bitmap? = suspendCancellableCoroutine { continuation ->
        this.getFrameBitmap(
            Continuation.createTrivial(
                Consumer { bitmap ->
                    if (continuation.isActive) {
                        continuation.resume(bitmap)
                    }
                }
            )
        )
    }

    private fun bitmapToJpegString(bitmap: Bitmap, quality: Int): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    override fun onMessage(client: Socket.ClientSocket, type: String, data: Any?) {
        log("Got message of type $type with data $data")
    }

    override fun onRegister(
        panelsInstance: Panels,
        context: Context
    ) {
        getClientsCount = { panelsInstance.clientsCount }
    }

    override fun onAttachEventLoop(eventLoop: FtcEventLoop) {
    }

    override fun onOpModeManager(o: OpModeManagerImpl) {
    }

    override fun onOpModePreInit(opMode: OpMode) {
    }

    override fun onOpModePreStart(opMode: OpMode) {
    }

    override fun onOpModePostStop(opMode: OpMode) {
        stopStream()
    }

    override fun onEnablePanels() {
    }

    override fun onDisablePanels() {
        stopStream()
    }
}