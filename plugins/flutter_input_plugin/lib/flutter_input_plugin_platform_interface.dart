import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'flutter_input_plugin_method_channel.dart';

abstract class FlutterInputPluginPlatform extends PlatformInterface {
  /// Constructs a FlutterInputPluginPlatform.
  FlutterInputPluginPlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterInputPluginPlatform _instance = MethodChannelFlutterInputPlugin();

  /// The default instance of [FlutterInputPluginPlatform] to use.
  ///
  /// Defaults to [MethodChannelFlutterInputPlugin].
  static FlutterInputPluginPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FlutterInputPluginPlatform] when
  /// they register themselves.
  static set instance(FlutterInputPluginPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
