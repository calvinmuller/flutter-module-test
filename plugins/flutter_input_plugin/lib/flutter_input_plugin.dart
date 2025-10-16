
import 'flutter_input_plugin_platform_interface.dart';

class FlutterInputPlugin {
  Future<String?> getPlatformVersion() {
    return FlutterInputPluginPlatform.instance.getPlatformVersion();
  }
}
