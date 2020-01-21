package aldrigos.mc.alchemy;

import java.lang.reflect.Array;
import java.util.List;

public abstract class Utils {
    public static <T> T[] toArray(List<T> list){
        return (T[])list.toArray();
    }
}
