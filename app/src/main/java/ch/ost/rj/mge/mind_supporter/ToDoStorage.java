package ch.ost.rj.mge.mind_supporter;

import android.widget.RatingBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ToDoStorage {
    private static ArrayList<ToDo> toDoArrayList;
    public static File persistFile;

    static{
        toDoArrayList = new ArrayList<>();
        persistFile = App.getFile();
        try {
            ToDoStorage.replaceToDoArrayListWithPersistedToDoArrayList();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

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

    public static ArrayList<ToDo> getToDoArrayList() {
        return toDoArrayList;
    }

    public static void addToToDoArrayList(ToDo currentToDo) throws IOException {
        toDoArrayList.add(new ToDo(currentToDo.getTitle(), currentToDo.getDueDateTime(), currentToDo.getDurationMinutes(), currentToDo.getPriority(), currentToDo.isFinished(), currentToDo.getImage(), currentToDo.getNote()));
        sortDueTime();
        persist();
    }

    public static void removeToDoFromArrayList(ToDo toDelete) throws IOException {
        toDoArrayList.remove(toDelete);
        sortDueTime();
        persist();
    }

    private static void sortDueTime(){
        toDoArrayList.sort((a,b)-> a.getDueDateTime().compareTo(b.getDueDateTime()));
    }
}
