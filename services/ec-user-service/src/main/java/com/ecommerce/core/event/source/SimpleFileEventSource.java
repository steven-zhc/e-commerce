package com.ecommerce.core.event.source;

import com.ecommerce.core.event.Event;
import com.ecommerce.core.event.EventSource;
import com.ecommerce.core.event.EventStream;
import com.ecommerce.core.event.io.SimpleEventIO;
import com.ecommerce.core.event.stream.SimpleEventStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SimpleFileEventSource implements EventSource {

    private final Path eventSourcePath;
    private final SimpleEventIO eventIO;

    public SimpleFileEventSource(String filePath) {
        this.eventSourcePath = Paths.get(filePath);
        this.eventIO = new SimpleEventIO();
        initializeEventSource();
    }

    private void initializeEventSource() {
        try {
            if (!Files.exists(eventSourcePath)) {
                if (eventSourcePath.getParent() != null) {
                    Files.createDirectories(eventSourcePath.getParent());
                }
                Files.createFile(eventSourcePath);
            }
        } catch (IOException e) {
            throw new EventSourceException("Failed to initialize file event source", e);
        }
    }

    @Override
    public <T> void save(Event<T> event) {
        try {
            String serialized = eventIO.serialize(event);
            Files.writeString(
                eventSourcePath,
                serialized + System.lineSeparator(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new EventSourceException("Failed to save event to file", e);
        }
    }

    @Override
    public void save(EventStream eventStream) {
        while (eventStream.hasNext()) {
            Event<?> event = eventStream.next();
            save(event);
        }
    }

    @Override
    public EventStream loadEventStream(String aggregateId) {
        return loadEventStream(aggregateId, 0);
    }

    @Override
    public EventStream loadEventStream(String aggregateId, int fromVersion) {
        List<Event<?>> events = new ArrayList<>();

        try (Stream<String> lines = Files.lines(eventSourcePath)) {
            lines.forEach(line -> {
                if (!line.trim().isEmpty()) {
                    Event<?> event = eventIO.deserialize(line);
                    if (event != null &&
                        event.getAggregateId().equals(aggregateId) &&
                        event.getVersion() >= fromVersion) {
                        events.add(event);
                    }
                }
            });
        } catch (IOException e) {
            throw new EventSourceException("Failed to load event stream from file", e);
        }

        return new SimpleEventStream(events);
    }


    public static class EventSourceException extends RuntimeException {
        public EventSourceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
