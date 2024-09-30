package com.soarespt.bluetoothlescanner

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.soarespt.bluetoothlescanner.ui.theme.BluetoothLEScannerTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class BluetoothDevice(
    val name: String,
    val macAddress: String,
    val rssi: Int,
    val isConnectable: Boolean
)

class MainActivity : ComponentActivity() {
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothLeScanner: BluetoothLeScanner
    private val permissionsHelper = PermissionsHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        enableEdgeToEdge()

        setContent {
            BluetoothLEScannerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var hasPermissions by remember { mutableStateOf(false) }
                    var bluetoothEnabled by remember { mutableStateOf(bluetoothAdapter.isEnabled) }

                    val context = LocalContext.current
                    val requestPermissionsLauncher = rememberLauncherForActivityResult(
                        ActivityResultContracts.RequestMultiplePermissions()
                    ) { permissions ->
                        hasPermissions = permissions.values.all { it }
                    }

                    LaunchedEffect(Unit) {
                        val missingPermissions = permissionsHelper.checkPermissions(context)
                        if (missingPermissions.isNotEmpty()) {
                            requestPermissionsLauncher.launch(missingPermissions.toTypedArray())
                        } else {
                            hasPermissions = true
                        }
                    }

                    val bluetoothEnablerLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartActivityForResult()
                    ) { result ->
                        if (result.resultCode == RESULT_OK) {
                            bluetoothEnabled = bluetoothAdapter.isEnabled
                        }
                    }

                    if (!bluetoothEnabled) {
                        PromptEnableBluetooth(onEnableBluetooth = {
                            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                            bluetoothEnablerLauncher.launch(enableBtIntent)
                        })
                    } else if (hasPermissions) {
                        MainContent(bluetoothLeScanner, requestPermissionsLauncher)
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                PermissionMessage("Bluetooth and Location Permissions are required to use this app.")
                            } else {
                                PermissionMessage("Location Permissions are required to use this app.")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PromptEnableBluetooth(onEnableBluetooth: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Bluetooth is turned off. Please enable Bluetooth to use this app.",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .padding(30.dp)
                    .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)
                    .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface), shape = MaterialTheme.shapes.medium)
                    .padding(30.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onEnableBluetooth) {
                Text(text = "Enable Bluetooth")
            }
        }
    }
}

@Composable
fun PermissionMessage(message: String) {
    Text(
        text = message,
        style = TextStyle(
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        ),
        modifier = Modifier
            .padding(30.dp)
            .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface), shape = MaterialTheme.shapes.medium)
            .padding(30.dp)
            .fillMaxWidth()
    )
}

@Composable
fun MainContent(bluetoothLeScanner: BluetoothLeScanner, requestPermissionsLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>) {
    val coroutineScope = rememberCoroutineScope()
    val devices = remember { mutableStateListOf<BluetoothDevice>() }
    var isScanning by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val permissionsHelper = PermissionsHelper()

    val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                listOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            } else {
                listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }

            val permissionsGranted = permissions.all { permission ->
                ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
            }

            val name = if (permissionsGranted) {
                device.name ?: "Unknown"
            } else {
                "Unknown"
            }

            val macAddress = device.address
            val rssi = result.rssi
            val isConnectable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) result.isConnectable else false

            val newDevice = BluetoothDevice(name, macAddress, rssi, isConnectable)
            if (newDevice !in devices) {
                devices.add(newDevice)
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            for (result in results) {
                onScanResult(ScanSettings.CALLBACK_TYPE_ALL_MATCHES, result)
            }
        }

        override fun onScanFailed(errorCode: Int) {
            println("Scan failed with error: $errorCode")
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp) // General padding for the content
        ) {
            Button(
                onClick = {
                    val missingPermissions = permissionsHelper.checkPermissions(context)
                    if (missingPermissions.isEmpty()) {
                        isScanning = true
                        devices.clear()
                        try {
                            bluetoothLeScanner.startScan(scanCallback)
                        } catch (e: SecurityException) {
                            println("SecurityException: ${e.message}")
                        }
                        coroutineScope.launch {
                            delay(3000) // Scan for 3 seconds
                            bluetoothLeScanner.stopScan(scanCallback)
                            isScanning = false
                        }
                    } else {
                        // Request missing permissions
                        requestPermissionsLauncher.launch(missingPermissions.toTypedArray())
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp) // Increase padding for a thicker margin around the button
                    .height(64.dp), // Increase height for a larger button
                shape = RectangleShape // Set the button shape to a rectangle
            ) {
                Text(
                    text = if (isScanning) "Scanning..." else "Start Scanning",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp, // Bigger text
                    modifier = Modifier.padding(8.dp) // Padding within the button
                )
            }

            LazyColumn {
                items(devices) { device ->
                    val textStyle = if (device.isConnectable) {
                        TextStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    } else {
                        TextStyle(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    Text(
                        text = "${device.macAddress} ${device.name} ${device.rssi}dBm",
                        style = textStyle,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}