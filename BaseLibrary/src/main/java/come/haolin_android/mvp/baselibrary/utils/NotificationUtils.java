package come.haolin_android.mvp.baselibrary.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.util.Random;

import come.haolin_android.mvp.baselibrary.R;

/**
 * 作者：haoLin_Lee on 2019/10/25 20:59
 * 邮箱：Lhaolin0304@sina.com
 * class:
 */
public class NotificationUtils {
    private static final String CHANNED_ID = "alertas";
    private MediaPlayer mediaPlayer;

    /**
     * 适配8.0以上通知 并实现自定义推送声音
     */
    public void exibirNotificacao(Context context, String string) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNED_ID, "Push", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(string);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.MAGENTA);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{1000, 500, 2000});
            notificationChannel.setBypassDnd(true);
//            notificationChannel.setSound(null, null);
//            if (mediaPlayer == null) {
//                mediaPlayer = MediaPlayer.create(context, R.raw.default_push_sound);
//            }
//            if (!mediaPlayer.isPlaying()) {
//                mediaPlayer.start();
//            }
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(context, CHANNED_ID);
        notification.setContentTitle("sample");
        notification.setChannelId(CHANNED_ID);
        notification.setPriority(NotificationCompat.PRIORITY_MAX);
        notification.setContentText(string);
        notification.setVibrate(new long[]{1000, 500, 2000});
        notification.setWhen(System.currentTimeMillis());
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setDefaults(Notification.DEFAULT_ALL);
        notification.setAutoCancel(true);
//        notification.setContentIntent(
//                PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class),
//                        PendingIntent.FLAG_UPDATE_CURRENT)).setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.default_push_sound));
        assert notificationManager != null;
        notificationManager.notify(new Random().nextInt(10000), notification.build());
    }

}
