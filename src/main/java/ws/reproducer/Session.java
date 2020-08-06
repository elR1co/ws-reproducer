package ws.reproducer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Session {

    private final List<String> messages;
    private final String id;

    public Session() {
        messages = new ArrayList<>();
        id = UUID.randomUUID().toString();
    }

    public String id() {
        return id;
    }

    public Session addMessage(String message) {
        messages.add(message);
        return this;
    }

    public Session clear() {
        messages.clear();
        return this;
    }
}
