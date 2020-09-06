package fr.g123k.flutterappbadger;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * FlutterAppBadgerPlugin
 */
public class FlutterAppBadgerPlugin implements MethodCallHandler {

  private final Context context;

  private FlutterAppBadgerPlugin(Context context) {
    this.context = context;
  }

  /**
   * Plugin registration.
   */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "g123k/flutter_app_badger");
    channel.setMethodCallHandler(new FlutterAppBadgerPlugin(registrar.activeContext()));
  }

  @Override
  public void onMethodCall(final MethodCall call, Result result) {
    if (call.method.equals("updateBadgeCount")) {
      if(Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")){
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            context.startService(
                    new Intent(context, BadgeIntentService.class).putExtra("badgeCount", Integer.valueOf(call.argument("count").toString()))
            );
          }
        },5000);
      } else {
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            ShortcutBadger.applyCount(context, Integer.valueOf(call.argument("count").toString()));
          }
        }, 5000);

      }
      result.success(null);
    } else if (call.method.equals("removeBadge")) {
      ShortcutBadger.removeCount(context);
      result.success(null);
    } else if (call.method.equals("isAppBadgeSupported")) {
      result.success(ShortcutBadger.isBadgeCounterSupported(context));
    } else {
      result.notImplemented();
    }
  }
}
