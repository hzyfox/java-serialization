package Fox.Target;

/**
 * create with Fox.Target
 * USER: husterfox
 */
public class TargetKryo {
    public int targetInt;
    public String targetString;

    public String getTargetString() {
        return targetString;
    }

    public int getTargetInt() {

        return targetInt;
    }

    public TargetKryo(){
        targetInt = 1;
        targetString = "HelloSuccess";
    }
}
