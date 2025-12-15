package good.space.runnershi.service

import android.content.Context
import android.content.Intent
import android.os.Build

class AndroidServiceController(private val context: Context) : ServiceController {
    
    private fun sendCommand(action: String) {
        Intent(context, RunningService::class.java).also {
            it.action = action
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(it)
            } else {
                context.startService(it)
            }
        }
    }

    override fun startService() = sendCommand(RunningService.ACTION_START)
    override fun pauseService() = sendCommand(RunningService.ACTION_PAUSE)
    override fun resumeService() = sendCommand(RunningService.ACTION_RESUME)
    override fun stopService() = sendCommand(RunningService.ACTION_STOP)
}

