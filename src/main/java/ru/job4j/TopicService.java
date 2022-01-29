package ru.job4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topics =
            new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp rsl = null;
        if ("GET".equals(req.httpRequestType())) {
            String text = "";
            String status = "204";
            if (topics.get(req.getSourceName()) == null) {
                topics.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
                topics.get(req.getSourceName()).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
            } else if (topics.get(req.getSourceName()).get(req.getParam()) == null) {
                topics.get(req.getSourceName()).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
            } else {
                text = topics.get(req.getSourceName()).get(req.getParam()).poll();
                if (!"".equals(text)) {
                    status = "200";
                }
            }
            rsl = new Resp(text, status);
        } else if ("POST".equals(req.httpRequestType())) {
            topics.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> temp = topics.get(req.getSourceName());
            for (var key : temp.keySet()) {
                temp.get(key).add(req.getParam());
            }
        } else {
            throw new IllegalArgumentException("Запрос некорректного типа.");
        }
        return rsl;
    }
}
