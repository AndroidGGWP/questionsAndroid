package hk.ust.cse.hunkim.questionroom;

/**
 * Created by Teman on 11/9/2015.
 */
class Holder<T> {
    private T value;

    Holder(T value) {
        setValue(value);
    }

    T getValue() {
        return value;
    }

    void setValue(T value) {
        this.value = value;
    }
}
