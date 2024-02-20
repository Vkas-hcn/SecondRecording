package com.flie.best.tato.secondrecording

import android.app.Application
import com.tencent.mmkv.MMKV
import java.util.UUID

class App:Application() {
    companion object {
        lateinit var instance: App
            private set
        val mmkvRecord by lazy { MMKV.mmkvWithID("record")  }
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        MMKV.initialize(this)
        if(SjUtils.uuid_record.isEmpty()){
            mmkvRecord.encode("uuid_record",UUID.randomUUID().toString())
        }
    }
}