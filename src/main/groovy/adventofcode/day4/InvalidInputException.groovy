package adventofcode.day4

class InvalidInputException extends RuntimeException {

    InvalidInputException() {
    }

    InvalidInputException(final String message) {
        super(message)
    }
}
