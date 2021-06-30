package vn.edu.hust.simulation.reader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class SimpleAbstractReader<S, T> implements IReader<T>{

    private int maxLines;
    private final Function<S, T> createFuntion;

    public SimpleAbstractReader(Function<S, T> createFuntion) {
        this.createFuntion = createFuntion;
    }

    @Override
    public void setMaxLines(int maxLines) {
        if (maxLines < 0) {
            this.maxLines = -1;
        } else this.maxLines = maxLines;
    }

    @Override
    public List<T> read(String dataPath) {
        List<S> events = this.readInfo(dataPath);
        List<T> entities = new ArrayList<>();

        for (int i = 0; i < events.size(); ++i) {
            entities.add(this.createFuntion.apply(events.get(i)));
        }

        return entities;
    }

    protected abstract List<S> readInfo(String dataPath);
}
