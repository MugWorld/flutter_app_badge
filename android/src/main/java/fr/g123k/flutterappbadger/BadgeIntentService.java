package fr.g123k.flutterappbadger;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import fr.g123k.flutterappbadge.flutterappbadger.R;
import me.leolin.shortcutbadger.ShortcutBadger;

public class BadgeIntentService extends IntentService {

    private static final String NOTIFICATION_CHANNEL = "me.leolin.shortcutbadger.example";

    private int notificationId = 0;

    public BadgeIntentService() {
        super("BadgeIntentService");
    }

    private NotificationManager mNotificationManager;
    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }
    
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            int badgeCount = intent.getIntExtra("badgeCount", 0);

            mNotificationManager.cancel(notificationId);
            notificationId++;
            Notification.Builder builder = new Notification.Builder(getApplicationContext())
                .setContentTitle("")
                .setContentText("")
                .setSmallIcon(R.mipmap.ic_launcher);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setupNotificationChannel();

                builder.setChannelId(NOTIFICATION_CHANNEL);
            }

            Notification notification = builder.build();
            ShortcutBadger.applyNotification(getApplicationContext(), notification, badgeCount);
            mNotificationManager.notify(notificationId, notification);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void setupNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL, "ShortcutBadger Sample",
                NotificationManager.IMPORTANCE_DEFAULT);

        mNotificationManager.createNotificationChannel(channel);
    }

    public Drawable parseApkIcon(Context context, String apkFilePath){
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(apkFilePath, 0);
        if(packageInfo == null){
            return null;
        }

        packageInfo.applicationInfo.sourceDir = apkFilePath;
        packageInfo.applicationInfo.publicSourceDir = apkFilePath;
        Drawable iconDrawable = packageInfo.applicationInfo.loadIcon(packageManager);
        if(iconDrawable == null){
            return null;
        }

        if(iconDrawable instanceof BitmapDrawable && ((BitmapDrawable) iconDrawable).getBitmap() == ((BitmapDrawable) packageManager.getDefaultActivityIcon()).getBitmap()){
            return null;
        }

        return iconDrawable;
    }
}
