package phrasegrammarcreator.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// A Wrapper to store List writes / deletes and apply them afterwards
public class ListModifier<T> extends ArrayList<ListModifier.ListChange<T>> {
    public void apply(List<T> list) {
        this.forEach(change -> change.applyOn(list));
        this.clear();
    }

    public void addition(T entry) {
        this.add(new Addition<>(entry));
    }

    public void addition(Collection<T> entries) {
        this.add(new BulkAddition<>(entries));
    }

    public void deletion(T entry) {
        this.add(new Deletion<>(entry));
    }

    public void deletion(Collection<T> entries) {
        this.add(new BulkDeletion<>(entries));
    }

    public static abstract class  ListChange<T> {
        public abstract void applyOn(List<T> list);
    }

    public static class Addition<T> extends ListChange<T> {
        private T entry;

        public Addition(T entry) {
            this.entry = entry;
        }

        @Override
        public void applyOn(List<T> list) {
            list.add(entry);
        }
    }

    public static class BulkAddition<T> extends ListChange<T> {
        private Collection<T> entries;

        public BulkAddition(Collection<T> entries) {
            this.entries = entries;
        }

        @Override
        public void applyOn(List<T> list) {
            list.addAll(entries);
        }
    }

    public static class Deletion<T> extends ListChange<T> {
        private T entry;

        public Deletion(T entry) {
            this.entry = entry;
        }

        @Override
        public void applyOn(List<T> list) {
            list.remove(entry);
        }
    }

    public static class BulkDeletion<T> extends ListChange<T> {
        private Collection<T> entries;

        public BulkDeletion(Collection<T> entries) {
            this.entries = entries;
        }

        @Override
        public void applyOn(List<T> list) {
            list.removeAll(entries);
        }
    }

}
