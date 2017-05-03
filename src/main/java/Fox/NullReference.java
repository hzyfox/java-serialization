package Fox;

/**
 * create with Serialization
 * USER: husterfox
 */
public final class NullReference {
    /**
     * Static singleton instance.
     */
    public static final NullReference INSTANCE = new NullReference();

    private NullReference() {
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object rhs) {
        return rhs == null || rhs instanceof NullReference;
    }

    @Override
    public String toString() {
        return "null";
    }
}
