package com.mscode.data.remoteconfig.datasource

import com.mscode.data.remoteconfig.model.datasUrl
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.mscode.data.remoteconfig.model.paths
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class RemoteConfigDataSource(
    private val remoteConfig: FirebaseRemoteConfig
) {

    suspend fun getRemoteConfig(): Map<String, String>? =
        suspendCancellableCoroutine { cont ->
            remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val configRes: MutableMap<String, String> = mutableMapOf()
                    datasUrl.map { dataUrl ->
                        configRes.put(dataUrl, remoteConfig.getString(dataUrl))
                    }
                    paths.map { path ->
                        configRes.put(path, remoteConfig.getString(path))
                    }
                    cont.resume(configRes.toMap())
                } else {
                    cont.resume(null)
                }
            }
        }

}