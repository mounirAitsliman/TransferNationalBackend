package org.national.transfer.backoffice.service.utils;

public final class LoggingUtils {

    public static final String SPACE = " ";
    public static final String START = "start";
    public static final String END = "end";

    private LoggingUtils() {
        throw new IllegalAccessError("Utility class");
    }

    public static String getEndMessage(Object... inputs) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        return buildStringObjects(methodName, LoggingUtils.END, inputs).toString();
    }

    public static String getMessage(Object... inputs) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        return buildStringObjects(methodName, "", inputs).toString();
    }

    public static String getStartMessage(Object... inputs) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        return buildStringObjects(methodName, LoggingUtils.START, inputs).toString();
    }

    private static StringBuilder buildStringObjects(String methodName, String phase, Object... inputs) {
        StringBuilder builder = new StringBuilder(phase).append(LoggingUtils.SPACE);
        builder.append(methodName).append(LoggingUtils.SPACE).append("(");
        boolean hasArgs = false;
        for (Object o : inputs) {
            hasArgs = true;
            try {
                if (o != null) {
                    builder.append("<").append(o.getClass().getSimpleName()).append(">");
                    builder.append(o.toString());
                    builder.append(", ");
                } else {
                    builder.append("null, ");
                }
            } catch (Exception e) {
                builder.append("null, ");
            }
        }
        if (hasArgs) {
            builder.delete(builder.length() - 2, builder.length());
        }
        builder.append(")");
        return builder;
    }
}
