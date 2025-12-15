package good.space.runnershi.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle

object MapsApiKeyChecker {
    /**
     * AndroidManifest.xml에서 Google Maps API 키를 읽어옵니다.
     * @return API 키가 설정되어 있고 유효하면 true, 그렇지 않으면 false
     */
    fun isApiKeySet(context: Context): Boolean {
        return try {
            val ai: ApplicationInfo = context.packageManager
                .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            val bundle: Bundle = ai.metaData
            val apiKey = bundle.getString("com.google.android.geo.API_KEY")
            apiKey != null && apiKey.isNotBlank() && apiKey != "YOUR_API_KEY_HERE"
        } catch (_: Exception) {
            false
        }
    }
}

