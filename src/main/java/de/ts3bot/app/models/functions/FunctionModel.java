package de.ts3bot.app.models.functions;

import java.util.List;

/**
 * Created by Captain on 18.07.2016.
 *
 */
public class FunctionModel {
    private int functionId;
    private String functionKey;
    private List<Integer> gruppenIDs;
    private String ignoreOnly;

    public String getFunctionKey() {
        return functionKey;
    }

    public void setFunctionKey(String functionKey) {
        this.functionKey = functionKey;
    }

    public int getFunctionId() {
        return functionId;
    }

    public List<Integer> getGruppenIDs() {
        return gruppenIDs;
    }

    public String getIgnoreOnly() {
        return ignoreOnly;
    }

    public void setIgnoreOnly(String ignoreOnly) {
        this.ignoreOnly = ignoreOnly;
    }

    public void setGruppenIDs(List<Integer> gruppenIDs) {
        this.gruppenIDs = gruppenIDs;
    }

    public void setFunctionId(int functionId) {
        this.functionId = functionId;
    }
}
