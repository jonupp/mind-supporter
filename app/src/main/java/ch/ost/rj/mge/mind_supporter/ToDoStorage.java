package ch.ost.rj.mge.mind_supporter;

import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ToDoStorage {
    private static ArrayList<ToDo> toDoArrayList;
    public static File persistFile;

    public static void persist() throws IOException {
        FileOutputStream fos = new FileOutputStream(persistFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(toDoArrayList);
        oos.close();
        fos.close();
    }

    public static void replaceToDoArrayListWithPersistedToDoArrayList() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(persistFile);
        ObjectInputStream ois = new ObjectInputStream(fis);
        toDoArrayList = (ArrayList<ToDo>) ois.readObject();
        ois.close();
        fis.close();
    }

    public static ArrayList<ToDo> getFinished(){
        ArrayList<ToDo> out = new ArrayList<>();
        for(ToDo tmp : toDoArrayList){
            if(tmp.isFinished()){
                out.add(tmp);
            }
        }
        return out;
    }

    public static ArrayList<ToDo> getPending(){
        ArrayList<ToDo> out = new ArrayList<>();
        for(ToDo tmp : toDoArrayList){
            if(!tmp.isFinished()){
                out.add(tmp);
            }
        }
        return out;
    }

    static{
        toDoArrayList = new ArrayList<>();
        persistFile = new File(App.getContext().getFilesDir(), "todos");
        try {
            ToDoStorage.replaceToDoArrayListWithPersistedToDoArrayList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ToDo> getToDoArrayList() {
        return toDoArrayList;
    }

    public static void setToDoArrayList(ArrayList<ToDo> toDoArrayList) {
        ToDoStorage.toDoArrayList = toDoArrayList;
    }

    public static void addToToDoArrayList(String title, LocalDateTime dueDateTime, int durationMinutes, int priority, boolean finished, Uri image, String note){
        toDoArrayList.add(new ToDo(title, dueDateTime, durationMinutes, priority, finished, image, note));
    }
}
