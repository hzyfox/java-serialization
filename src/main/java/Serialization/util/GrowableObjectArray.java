package Serialization.util;

/**
 * create with Serialization.util
 * USER: husterfox
 */
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public final class GrowableObjectArray<E> implements List<E> {
    static final int SMALL_CAPACITY = 1000000;

    private E[] array;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public GrowableObjectArray(Class<? super E> objectClass) {
        array = (E[]) Array.newInstance(objectClass, 10);
    }

    @SuppressWarnings("unchecked")
    public GrowableObjectArray(Class<? super E> objectClass, int initialCapacity) {
        array = (E[]) Array.newInstance(objectClass, initialCapacity);
    }

    public void increaseCapacity() {
        int capacity = array.length;
        if (capacity < SMALL_CAPACITY) {
            capacity += capacity;
        } else if (capacity > Integer.MAX_VALUE - capacity / 2) {
            capacity = Integer.MAX_VALUE;
        } else {
            capacity += capacity / 2;
        }

        capacity = capacity < size ? size : capacity;

        array = Arrays.copyOf(array, capacity);
    }

    public E[] trimToSize() {
        if (size < array.length) {
            array = Arrays.copyOf(array, size);
        }
        return array;
    }

    private void checkCapacity() {
        if (size > array.length - 1) {
            increaseCapacity();
        }
    }

    @Override
    public boolean add(E exception) {
        checkCapacity();
        array[size++] = exception;
        return true;
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public boolean contains(Object object) {
        for (int i = 0; i < size; i++) {
            if (array[i].equals(object)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object o : collection) {
            for (int i = 0; i < size; i++) {
                if (array[i].equals(o)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public E get(int index) {
        return array[index];
    }

    @Override
    public int indexOf(Object object) {
        for (int i = 0; i < size; i++) {
            if (array[i] == object) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public int lastIndexOf(Object object) {
        for (int i = size; i > 0; i--) {
            if (array[i - 1] == object) {
                return i - 1;
            }
        }

        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListItr(index);
    }

    @Override
    public boolean remove(Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E set(int index, E element) {
        if (index >= size) {
            size = index + 1;
        }
        checkCapacity();

        E previous = array[index];
        array[index] = element;
        return previous;
    }

    @Override
    public int size() {
        return size;
    }

    public int capacity() {
        return array.length;
    }

    public void capacity(int capacity) {
        if (capacity < size) {
            throw new IllegalArgumentException();
        }
        array = Arrays.copyOf(array, capacity);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, size);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] array) {
        if (size > array.length) {
            return (T[]) Arrays.copyOf(array, size, array.getClass());
        }
        System.arraycopy(this.array, 0, array, 0, size);
        if (size < array.length) {
            array[size] = null;
        }
        return array;
    }

    public E[] getArray() {
        return array;
    }

    public void addArray(E[] inputArray, int startIndex, int len) {
        while (array.length < len) {
            increaseCapacity();
        }

        System.arraycopy(inputArray, startIndex, array, size, len);
        size = len;
    }

    private class Itr implements Iterator<E> {
        int index = 0;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public E next() {
            return array[index++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class ListItr implements ListIterator<E> {
        int index;

        ListItr(int index) {
            this.index = index;
        }

        @Override
        public void add(E exception) {
            GrowableObjectArray.this.add(index, exception);
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public E next() {
            return array[index++];
        }

        @Override
        public int nextIndex() {
            return Math.min(size, index + 1);
        }

        @Override
        public E previous() {
            return array[--index];
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();

        }

        @Override
        public void set(E exception) {
            array[index] = exception;
        }

    }
}

