package ch.ost.rj.mge.mind_supporter;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class NotificationJobService extends JobService {
    NotificationManager mNotifyManager;

    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";

    public void createNotificationChannel() {

        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,"Mind Supporter",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notifications for pending todos.");

            mNotifyManager.createNotificationChannel(notificationChannel);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        createNotificationChannel();
        String title = null;
        try {
            FileInputStream fis = new FileInputStream(new File(this.getFilesDir(), "todos"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<ToDo> toDoArrayList = (ArrayList<ToDo>) ois.readObject();
            ois.close();
            fis.close();

            LocalDateTime now = LocalDateTime.now();
            for(ToDo t : toDoArrayList){
                long timeDelta = ChronoUnit.MINUTES.between(now, t.getDueDateTime());
                if(timeDelta > 0 &&  timeDelta < t.getDurationMinutes()+20 ){ //Dont send notification for past todos
                    title = t.getTitle();
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) { //No todos found
            return true;
        }

        if(title == null){
            return false;
        }

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (this, 0, new Intent(this, MainActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (this, PRIMARY_CHANNEL_ID)
                .setContentTitle("Mind Supporter")
                .setContentText("ToDo has to be done: " + title)
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.image_placeholder)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);

        mNotifyManager.notify(0, builder.build());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}