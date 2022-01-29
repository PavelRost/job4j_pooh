package ru.job4j;

public class Req {
    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String ls = System.lineSeparator();
        String typeModeNameParam = content.split(ls)[0];
        String[] temp = typeModeNameParam.split(" ");
        String type = temp[0];
        String[] modeNameParam = temp[1].split("/");
        String mode = modeNameParam[1];
        String name = modeNameParam[2];
        String param = "";
        if ("GET".equals(type)) {
            if (modeNameParam.length >= 4) {
                param = modeNameParam[3];
            }
        } else if ("POST".equals(type)) {
            param = content.split(ls)[content.split(ls).length - 1];
        }
        return new Req(type, mode, name, param);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}
