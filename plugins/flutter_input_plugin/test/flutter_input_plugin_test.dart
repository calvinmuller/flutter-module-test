import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_input_plugin/flutter_input_plugin.dart';
import 'package:flutter_input_plugin/flutter_input_plugin_platform_interface.dart';
import 'package:flutter_input_plugin/flutter_input_plugin_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFlutterInputPluginPlatform
    with MockPlatformInterfaceMixin
    implements FlutterInputPluginPlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final FlutterInputPluginPlatform initialPlatform = FlutterInputPluginPlatform.instance;

  test('$MethodChannelFlutterInputPlugin is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelFlutterInputPlugin>());
  });

  test('getPlatformVersion', () async {
    FlutterInputPlugin flutterInputPlugin = FlutterInputPlugin();
    MockFlutterInputPluginPlatform fakePlatform = MockFlutterInputPluginPlatform();
    FlutterInputPluginPlatform.instance = fakePlatform;

    expect(await flutterInputPlugin.getPlatformVersion(), '42');
  });
}
