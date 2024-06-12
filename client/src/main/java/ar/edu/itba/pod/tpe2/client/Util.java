package ar.edu.itba.pod.tpe2.client;

public class Util {
    public static <T> T requireArgument(T value, ClientArguments argument) {
        if (value == null)
            throw new IllegalArgumentException("Expected argument " + argument.getArgument() + " but found nothing.");
        return value;
    }
}
