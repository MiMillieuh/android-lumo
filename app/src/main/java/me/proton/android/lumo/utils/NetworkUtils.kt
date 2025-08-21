package me.proton.android.lumo.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

// --- Network Reachability Check ---
suspend fun isHostReachable(host: String, port: Int, timeoutMs: Int): Boolean {
    return withContext(Dispatchers.IO) { // Run on IO dispatcher
        try {
            Log.d(
                "NetworkCheck",
                "Attempting to connect to $host:$port with ${timeoutMs}ms timeout"
            )
            Socket().use { socket -> // Use try-with-resources
                val socketAddress = InetSocketAddress(host, port)
                socket.connect(socketAddress, timeoutMs)
                // Connection successful
                Log.d("NetworkCheck", "Connection to $host:$port successful.")
                true
            }
        } catch (e: IOException) {
            // Connection failed (timeout, host unknown, network unreachable, etc.)
            Log.w(
                "NetworkCheck",
                "Host $host:$port not reachable within ${timeoutMs}ms: ${e.message}"
            )
            false
        } catch (e: Exception) {
            // Other potential exceptions
            Log.e("NetworkCheck", "Error checking reachability for $host:$port", e)
            false
        }
    }
} 