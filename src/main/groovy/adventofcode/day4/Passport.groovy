package adventofcode.day4

class Passport {

    Integer birtYear
    Integer issueYear
    Integer expirationYear
    String height
    String hairColor
    String eyeColor
    String passportId
    Integer countryId

    static final Passport newInstance(String input) {
        if ( this.isValidInput(input)) {
            Map keyValue = this.buildKeyValueMap(input)

            Passport passport = new Passport(
                keyValue[PassportKey.BIRTH_YEAR_KEY.label],
                keyValue[PassportKey.ISSUE_YEAR_KEY.label],
                keyValue[PassportKey.EXPIRATION_YEAR_KEY.label],
                keyValue[PassportKey.HEIGHT.label],
                keyValue[PassportKey.HAIR_COLOR_KEY.label],
                keyValue[PassportKey.EYE_COLOR_KEY.label],
                keyValue[PassportKey.PASSPORT_ID_KEY.label],
                keyValue[PassportKey.COUNTRY_ID_KEY.label],
            )
            if(!passport.validate()) {
                throw new InvalidInputException()
            }
            passport
        }
        else {
            throw new InvalidInputException()
        }
    }

    private Passport(final Integer birtYear,
                     final Integer issueYear,
                     final Integer expirationYear,
                     final String height,
                     final String hairColor,
                     final String eyeColor,
                     final String passportId,
                     final Integer countryId) {
        this.birtYear = birtYear
        this.issueYear = issueYear
        this.expirationYear = expirationYear
        this.height = height
        this.hairColor = hairColor
        this.eyeColor = eyeColor
        this.passportId = passportId
        this.countryId = countryId
    }

    Boolean validate() {
        ((int) Math.log10(this.birtYear) + 1 == 4 && this.birtYear >= 1920 && this.birtYear <= 2002) &&
            ((int) Math.log10(this.issueYear) + 1 == 4 && this.issueYear >= 2010 && this.issueYear <= 2020) &&
            ((int) Math.log10(this.expirationYear) + 1 == 4 && this.expirationYear >= 2020 && this.expirationYear <= 2030) &&
            (isValidHeight(this.height)) &&
            (this.hairColor.startsWith('#') && this.hairColor.substring(1).matches('^[a-zA-Z0-9]+$')) &&
            (['amb', 'blu', 'brn', 'gry', 'grn', 'hzl', 'oth'].contains(this.eyeColor)) &&
            (this.passportId.length() ==  9)
    }

    static final boolean isValidInput(String input) {
        (input.contains(PassportKey.BIRTH_YEAR_KEY.label) &&
            input.contains(PassportKey.ISSUE_YEAR_KEY.label) &&
            input.contains(PassportKey.EXPIRATION_YEAR_KEY.label) &&
            input.contains(PassportKey.HEIGHT.label) &&
            input.contains(PassportKey.HAIR_COLOR_KEY.label) &&
            input.contains(PassportKey.EYE_COLOR_KEY.label) &&
            input.contains(PassportKey.PASSPORT_ID_KEY.label)
        )
    }

    static final List<String> splitInputInKeyValue(String input) {
        input.tokenize(' ')
    }

    static final String getStringValueFromKey(List<String> input, String key) {
        input.find { it.contains(key) }.tokenize(':').last()
    }

    static final Integer getNumberValueFromKey(List<String> input, String key) {
        getStringValueFromKey(input, key).toInteger()
    }

    static final Map<String, Object> buildKeyValueMap(String input) {
        splitInputInKeyValue(input).collectEntries { keyValue ->
            String [] splited = keyValue.split(':')
            [(splited[0]): (splited[1].isNumber() && splited[0] !=  PassportKey.PASSPORT_ID_KEY.label) ?
                splited[1].toInteger() :
                splited[1]
            ]
        }
    }

    static final List<String> splitHeight(String height) {
        List<Object> result = []
        result << height.find(( /\d+/ )).toInteger()
        result << height.find("[a-z]+")
        result
    }

    static final boolean isValidHeight(String height) {
        List<Object> splittedHeight = this.splitHeight(height)
        (splittedHeight.last() == 'cm' && splittedHeight.first() >= 150 && splittedHeight.first() <= 193) ||
            (splittedHeight.last() == 'in' && splittedHeight.first() >= 59 && splittedHeight.first() <= 76)
    }

}
