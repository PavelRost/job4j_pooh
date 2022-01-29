package ru.job4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue =
            new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp rsl = null;
        if ("GET".equals(req.httpRequestType())) {
            String text = queue.get(req.getSourceName()).poll();
            String status = "200";
            if (text == null || "".equals(text)) {
                status = "204";
            }
            rsl = new Resp(text, status);
        } else if ("POST".equals(req.httpRequestType())) {
            queue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            queue.get(req.getSourceName()).add(req.getParam());
        } else {
            throw new IllegalArgumentException("Запрос некорректного типа.");
        }
        return rsl;
    }
}

