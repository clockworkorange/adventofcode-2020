package adventofcode.day4

enum PassportKey {
    BIRTH_YEAR_KEY('byr'),
    ISSUE_YEAR_KEY('iyr'),
    EXPIRATION_YEAR_KEY('eyr'),
    HEIGHT('hgt'),
    HAIR_COLOR_KEY('hcl'),
    EYE_COLOR_KEY('ecl'),
    PASSPORT_ID_KEY('pid'),
    COUNTRY_ID_KEY('cid'),

    public final String label;

    private PassportKey(String label) {
        this.label = label;
    }

}
