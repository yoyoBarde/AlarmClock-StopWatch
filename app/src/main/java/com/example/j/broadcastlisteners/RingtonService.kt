package com.example.j.broadcastlisteners

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.IBinder
import android.support.v7.app.NotificationCompat

/**
 * Created by Romeo on 2/11/2018.
 */
class RingtonService: Service(){
    companion object {
        lateinit var myRingtone:Ringtone
    }

    var id:Int= 0
    var isRunning:Boolean = false

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var state:String = intent!!.getStringExtra("extra")!!
        when(state)
        {
            "on" -> id = 1
            "off" -> id = 0

        }
        if(!this.isRunning&&id==1) {
            playAlarm()
            this.isRunning=true
            this.id=0
            setNotification()
        }
        else if(this.isRunning&&id==0)
        {
            myRingtone.stop()
            this.isRunning=false
            this.id=0
        }
        else if(!this.isRunning&&id==0)
        {
            this.isRunning=false
            this.id=0
        }
        else if(this.isRunning&&id==1)
        {
            this.isRunning=true
            this.id=1
        }
        else
        {

        }

        return START_NOT_STICKY
    }

    private fun setNotification() {
        var mainActivity_intent : Intent = Intent(this,MainActivity::class.java)
        var pi:PendingIntent = PendingIntent.getActivity(this,0,mainActivity_intent,0)
        val defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        var notify_manager:NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var notification = NotificationCompat.Builder(this)
                .setContentTitle("Alarm is ON")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setSound(defaultSoundUri)
                .setContentText("Tap me")
                .setAutoCancel(true)
                .build()


        notify_manager.notify(0,notification)

    }

    fun playAlarm(){
        var alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if(alarmUri==null)
        {
            alarmUri=RingtoneManager.getDefaultUri (RingtoneManager.TYPE_NOTIFICATION)

        }
        myRingtone= RingtoneManager.getRingtone(baseContext,alarmUri)
        myRingtone.play()


    }

    override fun onDestroy() {
        super.onDestroy()
        this.isRunning = false
    }
}