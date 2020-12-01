package adventofcode

class Day1 {

    static List<Integer> search(int[] input, int expectedSum) {
        Arrays.sort(input)
        List<Integer> result = []
        input.eachWithIndex { int value, int index ->
            int siblingIndex = Arrays.binarySearch(input, expectedSum - value)
            if (siblingIndex >= 0) {
                if (siblingIndex != index ||
                    (index > 0 && input[index - 1] == value) ||
                    (index < (input.size() - 1) && input[index + 1] == value)) {
                    result.add(input[index])
                }
            }
        }
        result
    }
}
