package ch.ost.rj.mge.mind_supporter;

import android.net.Uri;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ToDoStorage {
    private static ArrayList<ToDo> toDoArrayList;

    public static void persist() throws IOException {
        FileOutputStream fos = new FileOutputStream("todos");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(toDoArrayList);
        oos.close();
        fos.close();
    }

    public static void replaceToDoArrayListWithPersistedToDoArrayList() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("todos");
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
        try {
            ToDoStorage.replaceToDoArrayListWithPersistedToDoArrayList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ToDo test1 = new ToDo("Shopping",LocalDateTime.now(),100,1,true, null, "");
        ToDo test2 = new ToDo("Learning", LocalDateTime.now(), 60, 2, false, null, "");
        ToDo test3 = new ToDo("Football", LocalDateTime.now(), 60, 2, true, null, "");

        for(int i = 0;i < 20;i++){
            toDoArrayList.add(new ToDo("Automatic", LocalDateTime.now(), 99, 5, false, null,""));
        }

        toDoArrayList.add(test1);
        toDoArrayList.add(test2);
        toDoArrayList.add(test3);
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
