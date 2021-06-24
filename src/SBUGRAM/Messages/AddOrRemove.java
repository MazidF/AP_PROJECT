package SBUGRAM.Messages;

import java.io.Serializable;

public enum AddOrRemove implements Serializable {
    ADD,
    REMOVE;

    @Override
    public String toString() {
        if (this == ADD) return "add";
        return "remove";
    }
}

