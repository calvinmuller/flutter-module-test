# Flutter Input Module

A Flutter module that provides a text input screen and returns the entered value to the host Android app using `startActivityForResult`. The platform code is embedded in the AAR, so no additional code is required in the host app.

## Features

- Simple text input interface
- Returns data to host app using Android Activity result mechanism
- Self-contained AAR with all platform code included
- No configuration required in host app

## Building the Module

To build the AAR files:

```bash
flutter build aar
```

This will generate AAR files for debug, profile, and release builds in `build/host/outputs/repo`.

## Integration into Host Android App

### Step 1: Add Repositories

In your host app's `app/build.gradle`, add the repositories:

```gradle
repositories {
    maven {
        url '/Users/calvinmuller/Sites/flutter_module_test/build/host/outputs/repo'
    }
    maven {
        url 'https://storage.googleapis.com/download.flutter.io'
    }
}
```

### Step 2: Add Dependencies

Add the Flutter module dependencies:

```gradle
dependencies {
    debugImplementation 'com.example.flutter_module_test:flutter_debug:1.0'
    profileImplementation 'com.example.flutter_module_test:flutter_profile:1.0'
    releaseImplementation 'com.example.flutter_module_test:flutter_release:1.0'
}
```

### Step 3: Add Profile Build Type

In your app's `build.gradle`, add the profile build type if it doesn't exist:

```gradle
android {
    buildTypes {
        profile {
            initWith debug
        }
    }
}
```

### Step 4: Launch the Flutter Activity

In your Android Activity, use `startActivityForResult` to launch the Flutter input screen:

```java
import android.content.Intent;
import com.example.flutter_input_plugin.FlutterInputActivity;

public class MainActivity extends AppCompatActivity {
    private static final int FLUTTER_INPUT_REQUEST = 1001;

    private void launchFlutterInput() {
        Intent intent = new Intent(this, FlutterInputActivity.class);
        startActivityForResult(intent, FLUTTER_INPUT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FLUTTER_INPUT_REQUEST && resultCode == RESULT_OK) {
            String resultData = data.getStringExtra("result_data");
            // Use the returned data
            Log.d("MainActivity", "Received data: " + resultData);
        }
    }
}
```

For Kotlin:

```kotlin
import android.content.Intent
import com.example.flutter_input_plugin.FlutterInputActivity

class MainActivity : AppCompatActivity() {
    companion object {
        private const val FLUTTER_INPUT_REQUEST = 1001
    }

    private fun launchFlutterInput() {
        val intent = Intent(this, FlutterInputActivity::class.java)
        startActivityForResult(intent, FLUTTER_INPUT_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FLUTTER_INPUT_REQUEST && resultCode == RESULT_OK) {
            val resultData = data?.getStringExtra("result_data")
            // Use the returned data
            Log.d("MainActivity", "Received data: $resultData")
        }
    }
}
```

## How It Works

1. The host app launches `FlutterInputActivity` using `startActivityForResult`
2. The Flutter module displays a text input screen
3. User enters text and clicks Submit
4. The Flutter code communicates with the native Android code via MethodChannel
5. The Activity sets the result with the entered text and finishes
6. The host app receives the data in `onActivityResult`

## Architecture

This module uses a **plugin-based architecture** for better code organization and proper AAR packaging:

### Key Components

- **lib/main.dart**: Flutter UI with text input and submit button
- **plugins/flutter_input_plugin/**: Flutter plugin containing all platform-specific code
  - **android/src/main/kotlin/.../FlutterInputActivity.kt**: Custom FlutterActivity that handles result passing
  - **android/src/main/AndroidManifest.xml**: Declares the Activity (automatically included in AAR)
- The plugin is included as a path dependency and automatically bundled with the module AAR

## Result Data

The text entered by the user is returned with the key `"result_data"` in the result Intent.

## Minimum Requirements

- Flutter SDK
- Android API level 21 or higher
- Java 8 or higher / Kotlin support

## Notes

- The AAR includes all necessary platform code, so the host app only needs to launch the Activity
- The Activity is properly configured with `android:exported="true"` to allow external apps to launch it
- All Flutter engine initialization and MethodChannel setup is handled internally
