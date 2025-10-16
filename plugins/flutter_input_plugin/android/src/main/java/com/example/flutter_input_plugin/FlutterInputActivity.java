package com.example.flutter_input_plugin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.android.FlutterActivityLaunchConfigs;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

public class FlutterInputActivity extends FlutterActivity {
    private static final String TAG = "FlutterInputActivity";
    private static final String CHANNEL = "com.example.flutter_module/data";
    private static final String KEY_RESULT_DATA = "result_data";

    private MethodChannel methodChannel;

    /**
     * Creates an Intent builder for launching FlutterInputActivity with a new engine.
     */
    @NonNull
    public static NewEngineIntentBuilder createBuilder() {
        return new NewEngineIntentBuilder(FlutterInputActivity.class);
    }

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        Log.d(TAG, "Configuring FlutterEngine");

        methodChannel = new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL);
        methodChannel.setMethodCallHandler((call, result) -> {
            Log.d(TAG, "Method called: " + call.method);
            if (call.method.equals("sendResult")) {
                String data = call.argument("data");
                Log.d(TAG, "Received data: " + data);

                // Create the result intent
                Intent resultIntent = new Intent();
                resultIntent.putExtra(KEY_RESULT_DATA, data);

                // Set the result and finish the activity
                setResult(RESULT_OK, resultIntent);
                finish();

                result.success(null);
            } else {
                result.notImplemented();
            }
        });

        Log.d(TAG, "MethodChannel handler registered on channel: " + CHANNEL);
    }

    @Override
    public void cleanUpFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        Log.d(TAG, "Cleaning up FlutterEngine");
        if (methodChannel != null) {
            methodChannel.setMethodCallHandler(null);
        }
        super.cleanUpFlutterEngine(flutterEngine);
    }

    /**
     * Custom builder for FlutterInputActivity
     */
    public static class NewEngineIntentBuilder {
        private final Class<? extends FlutterActivity> activityClass;
        private String initialRoute = "/";
        private String backgroundMode = "opaque";

        public NewEngineIntentBuilder(@NonNull Class<? extends FlutterActivity> activityClass) {
            this.activityClass = activityClass;
        }

        @NonNull
        public NewEngineIntentBuilder initialRoute(@NonNull String initialRoute) {
            this.initialRoute = initialRoute;
            return this;
        }

        @NonNull
        public NewEngineIntentBuilder backgroundMode(@NonNull String backgroundMode) {
            this.backgroundMode = backgroundMode;
            return this;
        }

        @NonNull
        public Intent build(@NonNull Context context) {
            Intent intent = new Intent(context, activityClass);
            intent.putExtra("route", initialRoute);
            intent.putExtra("background_mode", backgroundMode);
            intent.putExtra("destroy_engine_with_activity", true);
            return intent;
        }

        @NonNull
        public Intent build() {
            throw new IllegalStateException(
                "Cannot build Intent without a Context. Use build(Context) instead."
            );
        }
    }
}
