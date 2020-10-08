package ch.ost.rj.mge.mind_supporter;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ToDoStorage {
    private static ArrayList<ToDo> toDoArrayList;

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
        toDoArrayList = new ArrayList<ToDo>();


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
