import android.os.Handler
import android.os.Looper

class Task {
    private val handler = Handler(Looper.getMainLooper())
    private val interval: Long = 10000

    fun execute(idUser: String) {
        handler.postDelayed(object : Runnable {
            override fun run() {
                execute(idUser)
            }
        }, interval)
    }
}
