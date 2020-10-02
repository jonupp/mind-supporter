package ch.ost.rj.mge.mind_supporter;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ToDoStorage {
    private static ArrayList<ToDo> toDoArrayList;
    private static ArrayList<ToDo> finishedToDoArrayList;
    private static ArrayList<ToDo> pendingToDoArrayList;

    public static ArrayList<ToDo> getFinishedToDoArrayList() {
        return finishedToDoArrayList;
    }

    public static void setFinishedToDoArrayList(ArrayList<ToDo> finishedToDoArrayList) {
        ToDoStorage.finishedToDoArrayList = finishedToDoArrayList;
    }

    public static ArrayList<ToDo> getPendingToDoArrayList() {
        return pendingToDoArrayList;
    }

    public static void setPendingToDoArrayList(ArrayList<ToDo> pendingToDoArrayList) {
        ToDoStorage.pendingToDoArrayList = pendingToDoArrayList;
    }

    static{
        toDoArrayList = new ArrayList<ToDo>();
        finishedToDoArrayList = new ArrayList<ToDo>();
        pendingToDoArrayList = new ArrayList<ToDo>();

        ToDo test1 = new ToDo("Shopping",LocalDateTime.now(),100,1,false, null);
        ToDo test2 = new ToDo("Learning", LocalDateTime.now(), 60, 2, false, null);
        ToDo test3 = new ToDo("Football", LocalDateTime.now(), 60, 2, true, null);

        for(int i = 0;i < 20;i++){
            toDoArrayList.add(new ToDo("Automatic", LocalDateTime.now(), 99, 5, false, null));
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
}
