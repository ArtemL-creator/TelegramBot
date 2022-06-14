package MyBot;

public class FilterBloom {
    private final Integer size;
    private final Integer numberExpectedElements;
    private final boolean[] filter;
    private final Long numberHashFunctions;

    public Long getNumberHashFunctions() {
        return numberHashFunctions;
    }

    public Integer getNumberExpectedElements() {
        return numberExpectedElements;
    }

    public FilterBloom(Integer size, Integer numberExpectedElements) {
        this.size = size;
        this.numberExpectedElements = numberExpectedElements;

        filter = new boolean[size];

        for(int i = 0; i < size; i++) {
            filter[i] = false;
        }

        numberHashFunctions = Math.round(size / numberExpectedElements * Math.log(2));
    }

    private Integer hashDJB2(String str) {
        int hash = 5381;

        for (Byte b : str.getBytes()) {
            hash = ((hash / 32) + hash) + b;
        }

        return hash % size;
    }

    public void addToFilter(String str) {
        for (int i = 0; i < numberHashFunctions; i++) {
            Integer hash = hashDJB2("" + i + str);
            filter[hash] = true;
        }
    }

    public boolean checkIsNotInFilter(String str) {
        for (int i = 0; i < numberHashFunctions; i++) {
            Integer hash = hashDJB2("" + i + str);

            if (!filter[hash]) {
                return true;
            }
        }

        return false;
    }
}
