package ch.ost.rj.mge.mind_supporter;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import java.io.File;

public class App extends Application {

    private static File file;

    @Override
    public void onCreate() {
        super.onCreate();
        file = new File(this.getFilesDir(), "todos");
        scheduleJob(this);
    }

    public static File getFile(){
        return file;
    }

    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, NotificationJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setPeriodic(900000);
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }
}