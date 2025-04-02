package eu.tutorials.myrecipeapp


import android.app.Application
import eu.tutorials.myrecipeapp.worker.NewsUpdateWorker

class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // This schedules our periodic news updates when the app starts
        NewsUpdateWorker.scheduleNewsUpdates(this)
    }
}