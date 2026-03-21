package Generics;

public class MyContainer<T extends Number> implements MyContainerInterface<T>{
    private T bucket;

    public MyContainer(T bucketValue) {
        this.bucket = bucketValue;
    }

    public T getBucket() {
        return this.bucket;
    }

    @Override
    public void set(T value) {}

    @Override
    public T get() {
        return this.bucket;
    }
}
