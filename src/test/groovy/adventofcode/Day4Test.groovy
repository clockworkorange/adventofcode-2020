package adventofcode

import adventofcode.day4.InvalidInputException
import adventofcode.day4.Passport
import adventofcode.day4.PassportKey
import spock.lang.Specification
import spock.lang.Unroll

class Day4Test extends Specification {

    void 'Day 4 - get all passports'() {
        when:
        List<String> result = inputExample.split('\\n\\n').collect { it.replace('\n', ' ') }

        then:
        result == [
            'ecl:gry pid:860033327 eyr:2020 hcl:#fffffd byr:1937 iyr:2017 cid:147 hgt:183cm',
            'iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884 hcl:#cfa07d byr:1929',
            'hcl:#ae17e1 iyr:2013 eyr:2024 ecl:brn pid:760753108 byr:1931 hgt:179cm',
            'hcl:#cfa07d eyr:2025 pid:166559648 iyr:2011 ecl:brn hgt:59in'
        ]
    }

    void 'Day 4 - Check valid passports'() {
        when:
        List<String> passports = inputExample.split('\\n\\n').collect { it.replace('\n', ' ') }

        then:
        2 == passports.count { passport ->
            passport.contains(PassportKey.BIRTH_YEAR_KEY.label) &&
                passport.contains(PassportKey.ISSUE_YEAR_KEY.label) &&
                passport.contains(PassportKey.EXPIRATION_YEAR_KEY.label) &&
                passport.contains(PassportKey.HEIGHT.label) &&
                passport.contains(PassportKey.HAIR_COLOR_KEY.label) &&
                passport.contains(PassportKey.EYE_COLOR_KEY.label) &&
                passport.contains(PassportKey.PASSPORT_ID_KEY.label)
        }
    }

    void 'Day 4 - Split passport in key:value fields'() {
        when:
        List<String> passports = inputExample.split('\\n\\n').collect { it.replace('\n', ' ') }
        List<String> validPassports = passports.findAll { passport ->
            passport.contains(PassportKey.BIRTH_YEAR_KEY.label) &&
                passport.contains(PassportKey.ISSUE_YEAR_KEY.label) &&
                passport.contains(PassportKey.EXPIRATION_YEAR_KEY.label) &&
                passport.contains(PassportKey.HEIGHT.label) &&
                passport.contains(PassportKey.HAIR_COLOR_KEY.label) &&
                passport.contains(PassportKey.EYE_COLOR_KEY.label) &&
                passport.contains(PassportKey.PASSPORT_ID_KEY.label)
        }

        then:
        List<List<String>> result = validPassports.collect { validPassport ->
            validPassport.tokenize(' ')
        }

        expect:
        result == [
            ['ecl:gry', 'pid:860033327', 'eyr:2020', 'hcl:#fffffd', 'byr:1937', 'iyr:2017', 'cid:147', 'hgt:183cm'],
            ['hcl:#ae17e1', 'iyr:2013', 'eyr:2024', 'ecl:brn', 'pid:760753108', 'byr:1931', 'hgt:179cm'],
        ]
    }

    @Unroll
    void 'Day 4 - Valid a passport input'() {
        expect:
        Passport.isValidInput(validInput)

        where:
        validInput << [
            'ecl:gry pid:860033327 eyr:2020 hcl:#fffffd byr:1937 iyr:2017 cid:147 hgt:183cm',
            'hcl:#ae17e1 iyr:2013 eyr:2024 ecl:brn pid:760753108 byr:1931 hgt:179cm',
        ]
    }

    @Unroll
    void 'Day 4 - non valid a passport input'() {
        expect:
        !Passport.isValidInput(invalidInput)

        where:
        invalidInput << [
            'iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884 hcl:#cfa07d byr:1929',
            'hcl:#cfa07d eyr:2025 pid:166559648 iyr:2011 ecl:brn hgt:59in',
        ]
    }

    @Unroll
    void 'Day 4 - Split input in key value'() {
        expect:
        Passport.splitInputInKeyValue(validInput) == result

        where:
        validInput                                                                       | result
        'ecl:gry pid:860033327 eyr:2020 hcl:#fffffd byr:1937 iyr:2017 cid:147 hgt:183cm' | ['ecl:gry', 'pid:860033327', 'eyr:2020', 'hcl:#fffffd', 'byr:1937', 'iyr:2017', 'cid:147', 'hgt:183cm']
        'hcl:#ae17e1 iyr:2013 eyr:2024 ecl:brn pid:760753108 byr:1931 hgt:179cm'         | ['hcl:#ae17e1', 'iyr:2013', 'eyr:2024', 'ecl:brn', 'pid:760753108', 'byr:1931', 'hgt:179cm']
    }

    void 'Day 4 - get String value from key'() {
        expect:
        Passport.getStringValueFromKey(['ecl:gry', 'hcl:#fffffd', 'hgt:183cm'], 'hcl') == '#fffffd'
    }

    void 'Day 4 - get Number value from key'() {
        expect:
        Passport.getNumberValueFromKey(['pid:860033327', 'eyr:2020', 'byr:1937', 'iyr:2017', 'cid:147'], 'pid') == 860033327
    }

    void 'Day 4 - Create map with key values'() {
        given:
        String singleInput = 'ecl:gry pid:860033327 eyr:2020 hcl:#fffffd byr:1937 iyr:2017 cid:147 hgt:183cm'

        when:
        Map result = Passport.buildKeyValueMap(singleInput)

        then:
        result == [ecl: 'gry', pid: '860033327', eyr: 2020, hcl: '#fffffd', byr: 1937, iyr: 2017, cid: 147, hgt: '183cm']
    }

    void 'Day 4 - create a Passport '() {
        given:
        String singleInput = 'ecl:gry pid:086003332 eyr:2020 hcl:#fffffd byr:1937 iyr:2017 cid:147 hgt:183cm'

        when:
        Passport passport = Passport.newInstance(singleInput)

        then:
        passport.birtYear == 1937
        passport.issueYear == 2017
        passport.expirationYear == 2020
        passport.height == '183cm'
        passport.hairColor == '#fffffd'
        passport.eyeColor == 'gry'
        passport.passportId == '086003332'
        passport.countryId == 147
    }

    void 'Day 4 - create a invalid Passport'() {
        given:
        String singleInput = 'iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884 hcl:#cfa07d byr:1929'

        when:
        Passport.newInstance(singleInput)

        then:
        thrown(InvalidInputException)
    }

    void 'Day 4 - count valid passports'() {
        expect:
        4 == fourValidPassports.split('\\n\\n').collect { it.replace('\n', ' ') }.count {
            try {
                Passport.newInstance(it)
                true
            }
            catch (Exception exception) {
                println('Not valid passport')
                false
            }
        }
    }

    @Unroll
    void 'Day 4 - split height in number and unit'() {
        when:
        List<String> heightSplitted = Passport.splitHeight(height)

        then:
        heightSplitted == result

        where:
        height  | result
        '179cm' | [179, 'cm']
        '59in'  | [59, 'in']
    }

    @Unroll
    void 'Day 4 - check valid height'() {
        expect:
        Passport.isValidHeight(height) == isValid

        where:
        height  | isValid
        '149cm' | false
        '150cm' | true
        '193cm' | true
        '194cm' | false
        '58in'  | false
        '59in'  | true
        '68in'  | true
        '76in'  | true
        '77in'  | false
    }



    private static final String inputExample = 'ecl:gry pid:860033327 eyr:2020 hcl:#fffffd\n' +
        'byr:1937 iyr:2017 cid:147 hgt:183cm\n' +
        '\n' +
        'iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884\n' +
        'hcl:#cfa07d byr:1929\n' +
        '\n' +
        'hcl:#ae17e1 iyr:2013\n' +
        'eyr:2024\n' +
        'ecl:brn pid:760753108 byr:1931\n' +
        'hgt:179cm\n' +
        '\n' +
        'hcl:#cfa07d eyr:2025 pid:166559648\n' +
        'iyr:2011 ecl:brn hgt:59in'

    private static final String fourValidPassports = 'pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980\n' +
        'hcl:#623a2f\n' +
        '\n' +
        'eyr:2029 ecl:blu cid:129 byr:1989\n' +
        'iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm\n' +
        '\n' +
        'hcl:#888785\n' +
        'hgt:164cm byr:2001 iyr:2015 cid:88\n' +
        'pid:545766238 ecl:hzl\n' +
        'eyr:2022\n' +
        '\n' +
        'iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719'

    private static final String input = 'ecl:hzl byr:1926 iyr:2010\n' +
        'pid:221225902 cid:61 hgt:186cm eyr:2021 hcl:#7d3b0c\n' +
        '\n' +
        'hcl:#efcc98 hgt:178 pid:433543520\n' +
        'eyr:2020 byr:1926\n' +
        'ecl:blu cid:92\n' +
        'iyr:2010\n' +
        '\n' +
        'iyr:2018\n' +
        'eyr:2026\n' +
        'byr:1946 ecl:brn\n' +
        'hcl:#b6652a hgt:158cm\n' +
        'pid:822320101\n' +
        '\n' +
        'iyr:2010\n' +
        'hgt:138 ecl:grn pid:21019503 eyr:1937 byr:2008 hcl:z\n' +
        '\n' +
        'byr:2018 hcl:z eyr:1990 ecl:#d06796 iyr:2019\n' +
        'hgt:176in cid:75 pid:153cm\n' +
        '\n' +
        'byr:1994\n' +
        'hcl:#ceb3a1 hgt:176cm cid:80 pid:665071929 eyr:2024 iyr:2020 ecl:grn\n' +
        '\n' +
        'cid:280 byr:1955 ecl:blu hgt:155cm hcl:#733820\n' +
        'eyr:2013 iyr:2011 pid:2346820632\n' +
        '\n' +
        'hcl:#4a5917 hgt:61cm\n' +
        'pid:4772651050\n' +
        'iyr:2026 ecl:brn byr:2015 eyr:2026\n' +
        '\n' +
        'iyr:2019 hcl:#a97842 hgt:182cm eyr:2024 ecl:gry pid:917294399 byr:1974\n' +
        '\n' +
        'ecl:#9c635c pid:830491851 hgt:175cm cid:141\n' +
        'iyr:2010\n' +
        'hcl:z\n' +
        'byr:2026 eyr:1998\n' +
        '\n' +
        'byr:1927 iyr:2011 pid:055176954 ecl:gry hcl:#7d3b0c eyr:2025 hgt:166cm\n' +
        '\n' +
        'hcl:#733820 byr:2008 ecl:utc eyr:1920 pid:159cm hgt:66cm iyr:2030\n' +
        '\n' +
        'pid:027609878\n' +
        'eyr:2022 iyr:2012\n' +
        'byr:1960 hgt:157cm\n' +
        'hcl:#b6652a\n' +
        'cid:117\n' +
        'ecl:grn\n' +
        '\n' +
        'iyr:2025 pid:7190749793 ecl:grn byr:1984 hgt:71in hcl:c41681\n' +
        'cid:259 eyr:1928\n' +
        '\n' +
        'eyr:2029 pid:141655389 cid:52 hcl:#cfa07d iyr:2019\n' +
        'ecl:blu hgt:69in byr:1938\n' +
        '\n' +
        'eyr:2020 hgt:166cm\n' +
        'ecl:gry\n' +
        'pid:611660309 iyr:2011\n' +
        'hcl:#623a2f byr:1943\n' +
        '\n' +
        'hgt:190cm eyr:2022 byr:2000 cid:210 pid:728418346 hcl:#a97842 ecl:xry iyr:2015\n' +
        '\n' +
        'byr:1973 eyr:2028 iyr:2012\n' +
        'hcl:#ff0ec8 pid:740554599 ecl:amb cid:58 hgt:155cm\n' +
        '\n' +
        'iyr:2016 pid:922938570 ecl:oth hcl:#fffffd hgt:154cm eyr:2021 byr:1966\n' +
        '\n' +
        'ecl:amb\n' +
        'byr:1929\n' +
        'hcl:#c3bbea pid:511876219\n' +
        'iyr:2019\n' +
        'hgt:191cm\n' +
        'eyr:2026\n' +
        '\n' +
        'ecl:utc hgt:155cm pid:#9f0a41 iyr:2012 hcl:#bd4141\n' +
        'byr:1998 eyr:2020\n' +
        '\n' +
        'ecl:grn hgt:173cm cid:321 pid:851120816 byr:1968 hcl:#a97842 eyr:2027\n' +
        'iyr:2014\n' +
        '\n' +
        'hgt:155cm hcl:#f40d77 pid:038224056 byr:1953 ecl:brn iyr:2014\n' +
        'eyr:2022\n' +
        '\n' +
        'pid:181869721\n' +
        'iyr:2011 hgt:151cm hcl:#733820 cid:110 ecl:blu\n' +
        'byr:1931 eyr:2024\n' +
        '\n' +
        'byr:1948\n' +
        'hcl:#888785\n' +
        'hgt:74in\n' +
        'cid:112 ecl:hzl pid:921761213 eyr:2028\n' +
        'iyr:2015\n' +
        '\n' +
        'ecl:gry\n' +
        'byr:1931\n' +
        'pid:600127430 hcl:#341e13 eyr:2027\n' +
        'iyr:2013 hgt:173cm\n' +
        '\n' +
        'hgt:178cm pid:530791289 hcl:#6b5442\n' +
        'eyr:2022 byr:1979 iyr:2014 ecl:hzl\n' +
        '\n' +
        'pid:412193170 hcl:#cfa07d hgt:186cm iyr:2012 cid:284 eyr:2020 byr:1967\n' +
        'ecl:grn\n' +
        '\n' +
        'hcl:#6b5442\n' +
        'iyr:2015 pid:808448466 ecl:blu eyr:2022 hgt:159cm byr:1969\n' +
        '\n' +
        'eyr:2020\n' +
        'iyr:2019 hgt:170cm pid:8964201562 hcl:#6b5442 byr:1947 ecl:amb\n' +
        '\n' +
        'eyr:2029 ecl:hzl hcl:#866857 byr:1961\n' +
        'iyr:2017\n' +
        '\n' +
        'ecl:#3456ba eyr:2013 iyr:2020 pid:378280953\n' +
        'hcl:z hgt:174cm\n' +
        '\n' +
        'hgt:172cm\n' +
        'cid:202 ecl:oth eyr:2021 byr:1980\n' +
        'iyr:2012\n' +
        'hcl:#cfa07d pid:605707698\n' +
        '\n' +
        'cid:281 hgt:161cm iyr:2017 pid:122936432 hcl:#602927 byr:1981 ecl:gry eyr:2021\n' +
        '\n' +
        'byr:1959 hgt:193cm pid:083900241 iyr:2020 eyr:2037 hcl:#623a2f\n' +
        'ecl:hzl\n' +
        '\n' +
        'iyr:2030 hgt:153cm eyr:2022 hcl:#efcc98 cid:131\n' +
        'byr:2016 ecl:hzl pid:64053944\n' +
        '\n' +
        'hgt:172cm eyr:2025\n' +
        'hcl:#866857\n' +
        'byr:1938 ecl:dne\n' +
        'pid:192cm iyr:2014\n' +
        '\n' +
        'pid:016297574 cid:152 iyr:2015\n' +
        'eyr:2024 hcl:#341e13 byr:1965 hgt:175cm\n' +
        'ecl:oth\n' +
        '\n' +
        'pid:604330171 cid:125 byr:1974 hgt:160cm iyr:2014\n' +
        'eyr:2022 ecl:oth hcl:#6b5442\n' +
        '\n' +
        'pid:59747275\n' +
        'byr:2027\n' +
        'hgt:145\n' +
        'hcl:1fd71f iyr:1944 eyr:2037 ecl:brn\n' +
        '\n' +
        'iyr:2010\n' +
        'eyr:2021 byr:1953\n' +
        'pid:7098774146 ecl:brn hcl:98737d hgt:158cm\n' +
        '\n' +
        'hcl:#602927 eyr:2039 pid:#81a5a1 iyr:2012 cid:67 byr:1951\n' +
        'ecl:#6551f5 hgt:76cm\n' +
        '\n' +
        'hgt:170cm ecl:oth\n' +
        'cid:235 eyr:2022\n' +
        'byr:1929 iyr:2019\n' +
        'hcl:#341e13 pid:797557745\n' +
        '\n' +
        'iyr:2011\n' +
        'hcl:#733820\n' +
        'eyr:2022 pid:830183476 ecl:blu byr:1976 cid:157 hgt:75in\n' +
        '\n' +
        'hgt:164cm ecl:amb pid:653425455 hcl:#623a2f byr:1977 eyr:2020\n' +
        'iyr:2013\n' +
        '\n' +
        'byr:2009 eyr:1953 hgt:178cm pid:#5d02f0\n' +
        'hcl:#a97842 iyr:2016\n' +
        'ecl:amb\n' +
        '\n' +
        'pid:009643210 eyr:2036 ecl:zzz\n' +
        'cid:97 hcl:32e540 byr:2005 hgt:187cm iyr:2021\n' +
        '\n' +
        'pid:155cm\n' +
        'iyr:2022 byr:2024 eyr:2031 ecl:amb cid:79\n' +
        'hcl:#cfa07d hgt:69cm\n' +
        '\n' +
        'cid:176 ecl:oth\n' +
        'pid:688645779 byr:1933 eyr:2026 hgt:69cm\n' +
        'iyr:2016 hcl:#888785\n' +
        '\n' +
        'hcl:#888785\n' +
        'eyr:2027\n' +
        'iyr:2020 pid:802243213 ecl:brn\n' +
        'hgt:179cm byr:1976\n' +
        '\n' +
        'hcl:#6cad3e hgt:164cm byr:1982 iyr:2020\n' +
        'ecl:gry\n' +
        'pid:142160687 eyr:2023\n' +
        '\n' +
        'hcl:#18171d\n' +
        'hgt:153cm\n' +
        'iyr:2014 ecl:hzl cid:231 pid:167809118 byr:1997 eyr:2028\n' +
        '\n' +
        'byr:1940\n' +
        'ecl:hzl iyr:2016 cid:67 hcl:#c800da\n' +
        'pid:563956960 eyr:2021\n' +
        'hgt:189cm\n' +
        '\n' +
        'pid:133094996 eyr:2032 hgt:60cm hcl:#623a2f byr:2030 ecl:dne iyr:2023\n' +
        '\n' +
        'pid:65195409 hcl:d0d492\n' +
        'iyr:1956\n' +
        'byr:2019 ecl:#bb043f eyr:2031 hgt:167in\n' +
        '\n' +
        'iyr:2016 byr:2006 ecl:#35d62f eyr:2029\n' +
        'hgt:186cm\n' +
        'hcl:1d8307\n' +
        '\n' +
        'eyr:1935 iyr:1960 pid:346667344 ecl:grn hgt:170cm hcl:cfcc36\n' +
        '\n' +
        'ecl:oth byr:1979 pid:165581192\n' +
        'hgt:177cm\n' +
        'hcl:#c0946f\n' +
        'iyr:2011\n' +
        '\n' +
        'iyr:2011 eyr:2030 pid:250840477\n' +
        'byr:1934 cid:174 hgt:179cm hcl:#866857\n' +
        'ecl:blu\n' +
        '\n' +
        'hgt:157cm hcl:#7d3b0c eyr:2027 pid:979510046\n' +
        'ecl:oth\n' +
        '\n' +
        'iyr:2025\n' +
        'hgt:69\n' +
        'ecl:grt byr:1935\n' +
        'eyr:1928 pid:168cm\n' +
        'cid:271 hcl:z\n' +
        '\n' +
        'pid:998166233\n' +
        'iyr:2020 hgt:166cm ecl:amb byr:1995 hcl:#fffffd\n' +
        '\n' +
        'hcl:#ceb3a1 ecl:amb\n' +
        'iyr:2019\n' +
        'eyr:2024 hgt:184cm byr:1980 pid:839215481\n' +
        'cid:146\n' +
        '\n' +
        'byr:1967\n' +
        'pid:444303019 ecl:oth hgt:150cm eyr:2024\n' +
        '\n' +
        'eyr:2023 byr:1960 iyr:2010\n' +
        'cid:236 hcl:#733820 pid:900635506\n' +
        'hgt:69in\n' +
        'ecl:hzl\n' +
        '\n' +
        'eyr:2029 pid:969574247\n' +
        'hgt:150cm byr:1967\n' +
        'iyr:2010 ecl:blu\n' +
        '\n' +
        'pid:575879605 iyr:2010\n' +
        'ecl:hzl\n' +
        'byr:1963\n' +
        'hgt:151cm\n' +
        'hcl:#c0946f cid:277\n' +
        '\n' +
        'byr:1998 pid:621374275\n' +
        'ecl:brn hcl:z iyr:2029\n' +
        'eyr:2024\n' +
        'hgt:68cm\n' +
        '\n' +
        'pid:365407169 ecl:amb hcl:#87f433 iyr:2011 eyr:2021 byr:1987\n' +
        'hgt:175cm cid:201\n' +
        '\n' +
        'hgt:175cm iyr:2020\n' +
        'ecl:gry\n' +
        'eyr:2029 pid:806927384 cid:59\n' +
        'byr:1932 hcl:#888785\n' +
        '\n' +
        'pid:589898274 cid:113 hcl:z hgt:184cm eyr:2000\n' +
        'ecl:lzr iyr:2016 byr:2016\n' +
        '\n' +
        'ecl:#2bafbb\n' +
        'eyr:2038 iyr:2027\n' +
        'hcl:#fffffd\n' +
        'hgt:174 byr:2007\n' +
        'pid:093750113\n' +
        '\n' +
        'eyr:2022 hgt:59in\n' +
        'hcl:#ceb3a1\n' +
        'pid:159921662 ecl:gry\n' +
        'byr:1948 iyr:2014\n' +
        'cid:50\n' +
        '\n' +
        'hgt:190cm\n' +
        'iyr:2014 pid:480507618 hcl:#fffffd byr:1945 eyr:2029\n' +
        '\n' +
        'byr:1951 hgt:152cm ecl:brn iyr:2016 eyr:2029 cid:179 pid:027575942\n' +
        'hcl:#fffffd\n' +
        '\n' +
        'cid:198 pid:728480773 eyr:2028 hgt:153cm iyr:2018\n' +
        'hcl:#888785 ecl:amb byr:1983\n' +
        '\n' +
        'byr:1968 hcl:#c0946f ecl:grn eyr:2027\n' +
        'iyr:2013 pid:269749807\n' +
        'cid:227\n' +
        'hgt:178cm\n' +
        '\n' +
        'eyr:2024 hgt:185cm ecl:oth\n' +
        'hcl:#448ace byr:1987 iyr:2018 pid:454243136\n' +
        '\n' +
        'byr:1930 ecl:grn iyr:2018 hgt:158cm\n' +
        'hcl:#341e13 eyr:2021\n' +
        '\n' +
        'eyr:2024 cid:194 pid:425431271\n' +
        'hgt:169cm ecl:grn byr:1973\n' +
        'iyr:2014 hcl:#fffffd\n' +
        '\n' +
        'ecl:grn cid:110 iyr:2013 hcl:#18171d\n' +
        'hgt:155cm eyr:2024 byr:1962 pid:522435225\n' +
        '\n' +
        'byr:1934 ecl:hzl hgt:152cm iyr:2018\n' +
        'eyr:2024 pid:079740520\n' +
        '\n' +
        'ecl:grn eyr:2023 hcl:c3f119 pid:468039715 iyr:2013 hgt:150cm byr:1955\n' +
        '\n' +
        'pid:809357582 eyr:2025 byr:1958\n' +
        'hcl:#6b5442 iyr:2013\n' +
        'hgt:161cm ecl:hzl\n' +
        '\n' +
        'hcl:#b6652a pid:068979430 byr:1960 iyr:2010 ecl:grn hgt:159cm eyr:2021\n' +
        '\n' +
        'cid:105 pid:495292692 byr:1965\n' +
        'hcl:#ceb3a1 hgt:160cm ecl:amb\n' +
        'iyr:2020\n' +
        '\n' +
        'iyr:2010\n' +
        'eyr:2024 byr:1941 ecl:grn hcl:#b35770 hgt:171cm cid:132 pid:975699036\n' +
        '\n' +
        'pid:767448421 hgt:186cm hcl:#733820\n' +
        'byr:1972 iyr:2020 eyr:2026 ecl:grn\n' +
        '\n' +
        'pid:036236909 iyr:2012\n' +
        'hgt:181cm hcl:#888785\n' +
        'eyr:2026\n' +
        'ecl:hzl byr:1936\n' +
        '\n' +
        'hgt:173cm\n' +
        'byr:1923 ecl:blu\n' +
        'eyr:2026 pid:570818321\n' +
        'hcl:#733820 iyr:2016\n' +
        'cid:59\n' +
        '\n' +
        'pid:2711059768\n' +
        'byr:2024\n' +
        'cid:139 ecl:blu hcl:z hgt:60cm\n' +
        '\n' +
        'eyr:2025\n' +
        'pid:671193016\n' +
        'byr:1950 hcl:#6b4b25 iyr:2017 hgt:158cm ecl:blu\n' +
        '\n' +
        'hgt:175cm iyr:2015 ecl:amb\n' +
        'byr:1984 eyr:2026 pid:342782894\n' +
        'cid:140\n' +
        '\n' +
        'iyr:2019 eyr:2027 byr:1972\n' +
        'pid:196266458\n' +
        'hgt:158cm hcl:#7d3b0c cid:69\n' +
        '\n' +
        'pid:604018034 iyr:2016 ecl:brn eyr:2028 hgt:172cm hcl:#6b5442 byr:1922\n' +
        'cid:238\n' +
        '\n' +
        'eyr:2024 ecl:gry byr:1970 pid:356551266 cid:340 hgt:162cm iyr:2013\n' +
        '\n' +
        'ecl:amb\n' +
        'hgt:151cm hcl:#18171d byr:1921 pid:187276410 eyr:2030 iyr:2015\n' +
        '\n' +
        'eyr:2030 pid:056372924 hcl:#d236d9 hgt:156cm\n' +
        'iyr:2014 ecl:blu\n' +
        '\n' +
        'iyr:2014 eyr:2028 byr:1991\n' +
        'hcl:#b6652a pid:119231378 hgt:155cm ecl:blu\n' +
        'cid:77\n' +
        '\n' +
        'hcl:#341e13\n' +
        'eyr:2027\n' +
        'iyr:2012 ecl:grn hgt:152cm pid:405955710 byr:1970\n' +
        '\n' +
        'iyr:2013 hgt:180cm eyr:1978 ecl:amb byr:1929 pid:3198111997 hcl:z\n' +
        '\n' +
        'pid:32872520 ecl:#8a0dd4 iyr:1955 eyr:2036\n' +
        'byr:2027 cid:133 hcl:z hgt:184in\n' +
        '\n' +
        'hgt:152cm pid:402361044\n' +
        'hcl:#efcc98 eyr:2029 ecl:grn iyr:2014\n' +
        'byr:1960\n' +
        '\n' +
        'byr:1972 eyr:2026 pid:411187543 iyr:2014\n' +
        'hgt:184cm cid:211 hcl:#866857 ecl:brn\n' +
        '\n' +
        'ecl:brn\n' +
        'hcl:#efcc98\n' +
        'pid:311916712\n' +
        'byr:1957 hgt:151cm eyr:2020 iyr:2020\n' +
        '\n' +
        'iyr:1968\n' +
        'hcl:a28220\n' +
        'pid:#ed250d cid:240 eyr:2031\n' +
        'hgt:181cm ecl:xry\n' +
        '\n' +
        'ecl:grn byr:1946 hgt:172cm iyr:2010 hcl:#b6652a pid:372011640 eyr:2026\n' +
        '\n' +
        'ecl:brn\n' +
        'eyr:2026 byr:1980 hcl:#c0946f\n' +
        'hgt:151cm pid:153076317 iyr:2012\n' +
        '\n' +
        'byr:1966 pid:852999809 ecl:oth\n' +
        'hgt:163cm\n' +
        'iyr:2014 eyr:2029 hcl:#341e13\n' +
        '\n' +
        'ecl:blu\n' +
        'byr:1959 hgt:191cm pid:195095631 iyr:2016 hcl:#ceb3a1 eyr:2028\n' +
        '\n' +
        'byr:2001 ecl:gry hcl:#888785 iyr:2018 hgt:177cm pid:576714115\n' +
        '\n' +
        'iyr:2017\n' +
        'byr:1949\n' +
        'ecl:blu hgt:186cm cid:289 pid:859016371\n' +
        'hcl:#ceb3a1 eyr:2021\n' +
        '\n' +
        'byr:1999 hcl:#b6652a eyr:2023\n' +
        'hgt:175cm\n' +
        'ecl:gry iyr:2013 cid:165 pid:194927609\n' +
        '\n' +
        'hgt:70in eyr:2027 ecl:brn iyr:2012 pid:162238378 hcl:#ceb3a1 byr:1986\n' +
        '\n' +
        'hgt:63in ecl:xry\n' +
        'byr:2011 iyr:2024\n' +
        'hcl:5337b0\n' +
        '\n' +
        'hcl:#341e13 eyr:2029\n' +
        'hgt:184cm ecl:amb iyr:2012\n' +
        'byr:1970\n' +
        '\n' +
        'byr:1920 pid:472914751\n' +
        'eyr:2028\n' +
        'hgt:187cm hcl:#cfa07d cid:290 ecl:gry\n' +
        '\n' +
        'byr:1948 ecl:gry eyr:2025 hgt:151cm cid:276 hcl:#6b5442 pid:937979267\n' +
        'iyr:2016\n' +
        '\n' +
        'byr:1934\n' +
        'pid:626915978 hcl:#623a2f hgt:167cm ecl:gry\n' +
        'iyr:2020 eyr:2023\n' +
        '\n' +
        'byr:1949\n' +
        'hgt:68in eyr:2027 iyr:2019 hcl:#733820 ecl:brn cid:237\n' +
        'pid:057797826\n' +
        '\n' +
        'pid:155cm\n' +
        'hgt:68cm ecl:lzr hcl:z cid:344 eyr:2028 iyr:2020 byr:2017\n' +
        '\n' +
        'byr:1959\n' +
        'hcl:#341e13 eyr:2022\n' +
        'iyr:2019 pid:728703569\n' +
        'hgt:167cm\n' +
        'ecl:oth\n' +
        '\n' +
        'ecl:grn\n' +
        'eyr:2024 byr:1999\n' +
        'pid:566956828\n' +
        'iyr:2015 cid:293 hcl:#602927 hgt:192cm\n' +
        '\n' +
        'byr:1939\n' +
        'ecl:xry pid:929512270 hgt:66in iyr:1939 eyr:2030 hcl:#efcc98\n' +
        '\n' +
        'eyr:2026\n' +
        'iyr:2014\n' +
        'pid:176cm hcl:#fffffd\n' +
        'ecl:gry\n' +
        'hgt:151cm byr:1933\n' +
        'cid:256\n' +
        '\n' +
        'ecl:oth eyr:2025 iyr:2017 hgt:159cm pid:055267863 cid:55 byr:2001 hcl:#cfa07d\n' +
        '\n' +
        'eyr:2029 byr:1954 ecl:hzl cid:123 iyr:2020 hgt:192cm hcl:#866857\n' +
        'pid:225593536\n' +
        '\n' +
        'pid:320274514 cid:289 byr:1963\n' +
        'eyr:1942\n' +
        'ecl:gmt hcl:z hgt:167in iyr:2022\n' +
        '\n' +
        'byr:2013\n' +
        'ecl:gmt\n' +
        'iyr:2011\n' +
        'hcl:#733820 pid:#e7962f\n' +
        'hgt:178cm eyr:2029\n' +
        '\n' +
        'pid:154cm ecl:hzl\n' +
        'eyr:2035 byr:2023 cid:104 iyr:2026\n' +
        '\n' +
        'eyr:2024 ecl:hzl hcl:#7d3b0c iyr:2010\n' +
        'pid:105864164\n' +
        'byr:1955\n' +
        'hgt:163cm\n' +
        '\n' +
        'eyr:2021 hgt:151cm\n' +
        'iyr:2017 hcl:#c0946f\n' +
        'ecl:amb\n' +
        'cid:150\n' +
        'pid:296798563\n' +
        'byr:1953\n' +
        '\n' +
        'iyr:2012\n' +
        'byr:1990 hcl:#341e13\n' +
        'pid:189449931 eyr:2024 hgt:64in\n' +
        '\n' +
        'hcl:z cid:79 byr:2028\n' +
        'eyr:2028 pid:886152432\n' +
        'ecl:#ce0596 hgt:178cm\n' +
        'iyr:2029\n' +
        '\n' +
        'ecl:brn\n' +
        'iyr:2019 hgt:151cm\n' +
        'hcl:#341e13\n' +
        'byr:1969\n' +
        'pid:468846056\n' +
        'eyr:2022\n' +
        '\n' +
        'ecl:grn hgt:157cm iyr:2012\n' +
        'eyr:2020\n' +
        'hcl:#b6652a cid:338\n' +
        'byr:1954 pid:153867580\n' +
        '\n' +
        'iyr:2011\n' +
        'eyr:2027\n' +
        'byr:1935\n' +
        'hgt:151cm\n' +
        'ecl:blu pid:802665934 cid:276 hcl:#623a2f\n' +
        '\n' +
        'hcl:#efcc98 eyr:2026 ecl:amb\n' +
        'iyr:2014 pid:320160032\n' +
        'hgt:157cm\n' +
        'byr:1976\n' +
        '\n' +
        'eyr:2021 cid:172\n' +
        'iyr:2012 ecl:oth hgt:187cm\n' +
        'pid:432856831 byr:2001 hcl:#733820\n' +
        '\n' +
        'eyr:2028 ecl:amb hcl:#efcc98\n' +
        'iyr:2020 byr:1954 hgt:153cm\n' +
        '\n' +
        'byr:1930 ecl:brn hcl:#fffffd\n' +
        'pid:458840035 hgt:178cm eyr:2021\n' +
        'iyr:2011 cid:336\n' +
        '\n' +
        'pid:216876576 hcl:#341e13\n' +
        'eyr:2028 iyr:2018 hgt:177cm byr:1938\n' +
        'ecl:brn cid:214\n' +
        '\n' +
        'byr:2029 eyr:1987\n' +
        'hgt:75cm pid:193cm hcl:#b6652a cid:246 iyr:2028\n' +
        '\n' +
        'ecl:hzl hgt:151cm hcl:#7d3b0c\n' +
        'eyr:2030 pid:910999919\n' +
        'iyr:2019 byr:1956\n' +
        '\n' +
        'byr:1950\n' +
        'cid:95 iyr:2013 ecl:grn\n' +
        'eyr:2020 hcl:#623a2f\n' +
        'pid:603817559 hgt:159cm\n' +
        '\n' +
        'pid:913791667\n' +
        'iyr:2018 byr:1959 hcl:#a97842 hgt:179cm eyr:2029 ecl:gry\n' +
        '\n' +
        'hgt:71in\n' +
        'ecl:blu eyr:2028\n' +
        'hcl:#18171d byr:1937 iyr:2011 pid:951572571\n' +
        '\n' +
        'hcl:#b6652a iyr:2015 hgt:170cm ecl:blu cid:292\n' +
        'byr:1977 pid:475457579 eyr:2020\n' +
        '\n' +
        'ecl:amb eyr:2029\n' +
        'pid:530769382 iyr:2018 cid:53\n' +
        'hgt:63in\n' +
        'byr:1954 hcl:#07de91\n' +
        '\n' +
        'hcl:#cfa07d hgt:185cm\n' +
        'byr:1929 iyr:2011\n' +
        'eyr:2027\n' +
        '\n' +
        'iyr:2019 ecl:oth byr:2023 hcl:#341e13 pid:879919037\n' +
        'eyr:2030 hgt:174cm\n' +
        '\n' +
        'hcl:z hgt:182cm ecl:grn iyr:2010 eyr:2020 pid:2063425865\n' +
        'cid:182\n' +
        'byr:2019\n' +
        '\n' +
        'byr:1930 hgt:185cm pid:412694897 eyr:2025 ecl:brn iyr:2020\n' +
        'hcl:#a97842\n' +
        '\n' +
        'hgt:150cm byr:1955 eyr:2020 cid:149 pid:597600808\n' +
        'hcl:#ceb3a1\n' +
        'ecl:hzl\n' +
        '\n' +
        'pid:209568495\n' +
        'eyr:2026 byr:1928 hcl:#341e13 hgt:183cm ecl:brn iyr:2011\n' +
        '\n' +
        'pid:723789670 ecl:blu iyr:2013 byr:1933\n' +
        'cid:239 hcl:#7d3b0c eyr:2026 hgt:151cm\n' +
        '\n' +
        'byr:1978 eyr:2027 hgt:164cm\n' +
        'pid:009071063\n' +
        'hcl:#602927 iyr:2014 ecl:blu\n' +
        '\n' +
        'hcl:#18171d ecl:grn hgt:154cm cid:154 iyr:2016\n' +
        'byr:1952 pid:730027149 eyr:2024\n' +
        '\n' +
        'eyr:2025 hcl:#888785 iyr:2013 cid:90\n' +
        'byr:1975 ecl:grn\n' +
        'pid:619198428 hgt:161cm\n' +
        '\n' +
        'ecl:gry iyr:2013 pid:795604673 cid:198 byr:1962\n' +
        'hcl:#6b5442 hgt:64in eyr:2021\n' +
        '\n' +
        'hcl:#ceb3a1 ecl:oth iyr:2015\n' +
        'eyr:2021 pid:920586799 cid:302 hgt:60in\n' +
        'byr:1964\n' +
        '\n' +
        'eyr:2021 ecl:gry iyr:2019\n' +
        'hcl:#6b5442 hgt:192cm\n' +
        'byr:1996\n' +
        'pid:692698177\n' +
        '\n' +
        'ecl:grn pid:141369492 byr:1956 eyr:2028 hcl:#6b5442 hgt:190cm iyr:2014\n' +
        '\n' +
        'hcl:#6b5442\n' +
        'ecl:grn iyr:2020 hgt:153cm\n' +
        'pid:312738382 eyr:2028\n' +
        'byr:1985\n' +
        '\n' +
        'byr:1979\n' +
        'eyr:2021 ecl:gry hgt:175cm pid:787676021 cid:81 hcl:#b6652a iyr:2012\n' +
        '\n' +
        'cid:80 hgt:188cm byr:1964 pid:105773060 iyr:2014 hcl:#733820 ecl:gry eyr:2028\n' +
        '\n' +
        'byr:1960 pid:251870522 iyr:2018 hgt:168cm ecl:blu hcl:#c0946f eyr:2026\n' +
        '\n' +
        'cid:270\n' +
        'pid:#5661f0 hgt:182in\n' +
        'ecl:dne\n' +
        'byr:1930\n' +
        'hcl:z iyr:2026\n' +
        '\n' +
        'hcl:#888785 byr:1954 pid:170544716 eyr:2028 hgt:162cm cid:244\n' +
        'iyr:2014\n' +
        'ecl:grn\n' +
        '\n' +
        'iyr:2017\n' +
        'hgt:69in\n' +
        'ecl:hzl\n' +
        'pid:544135985 hcl:#ceb3a1 eyr:2020\n' +
        '\n' +
        'hcl:92d4a1 iyr:2018 pid:178cm\n' +
        'cid:347\n' +
        'hgt:97 eyr:2017\n' +
        'ecl:gmt byr:2004\n' +
        '\n' +
        'ecl:oth iyr:2018 hcl:#fffffd byr:1999 pid:853396129\n' +
        'cid:119 eyr:2026 hgt:178cm\n' +
        '\n' +
        'hgt:69in\n' +
        'hcl:#fffffd eyr:2026 byr:1922\n' +
        'iyr:2010 ecl:oth pid:664840386\n' +
        '\n' +
        'hgt:178cm\n' +
        'byr:2000\n' +
        'iyr:2013 hcl:#cfa07d\n' +
        'eyr:2028 pid:842454291\n' +
        'ecl:amb\n' +
        '\n' +
        'ecl:hzl\n' +
        'hcl:#733820 pid:316835287 byr:1998\n' +
        'eyr:2024\n' +
        'iyr:2015 hgt:165cm\n' +
        '\n' +
        'pid:684064750 byr:1928 ecl:gry iyr:2015 cid:343\n' +
        'hgt:189cm\n' +
        'hcl:#4c6cb4 eyr:2020\n' +
        '\n' +
        'byr:1923 hcl:#a97842 eyr:2024 ecl:gry\n' +
        'pid:095911913\n' +
        'hgt:185cm iyr:2010\n' +
        '\n' +
        'ecl:hzl\n' +
        'byr:1996\n' +
        'eyr:2023\n' +
        'hgt:177cm\n' +
        'hcl:#b6652a pid:011541746\n' +
        'iyr:2011\n' +
        '\n' +
        'hcl:#efcc98\n' +
        'iyr:2014 ecl:oth byr:1942 pid:730960830\n' +
        'hgt:183cm\n' +
        'eyr:2025\n' +
        '\n' +
        'byr:1939 eyr:2029 ecl:amb hcl:#fffffd\n' +
        'hgt:188cm pid:732730418 iyr:2013 cid:313\n' +
        '\n' +
        'hgt:164cm cid:217 byr:1985 hcl:#888785 eyr:2020\n' +
        'iyr:2014 ecl:oth\n' +
        'pid:071172789\n' +
        '\n' +
        'eyr:2024 pid:215897274 ecl:#c67898\n' +
        'byr:1972 hcl:#866857 iyr:2010 hgt:170cm cid:310\n' +
        '\n' +
        'ecl:hzl pid:030118892 byr:1941 hgt:158cm hcl:#b6652a\n' +
        'eyr:2029 iyr:2012\n' +
        '\n' +
        'ecl:gry hcl:#c0946f hgt:166cm pid:604313781\n' +
        'byr:1924 eyr:2023 iyr:2020\n' +
        '\n' +
        'hcl:#602927 hgt:168cm eyr:2027 ecl:brn\n' +
        'pid:764635418 byr:1968 iyr:2010\n' +
        '\n' +
        'pid:157933284\n' +
        'ecl:grn\n' +
        'eyr:2030 byr:2000\n' +
        'hgt:81 hcl:z\n' +
        '\n' +
        'hcl:#ec24d1\n' +
        'pid:647881680 byr:1922\n' +
        'hgt:178cm iyr:2020 ecl:amb eyr:2021 cid:94\n' +
        '\n' +
        'ecl:hzl byr:1971 iyr:2018 pid:975690657 eyr:2027\n' +
        'hgt:192in\n' +
        'cid:202 hcl:#c0946f\n' +
        '\n' +
        'pid:678999378\n' +
        'hgt:61in\n' +
        'byr:1981 hcl:#cfa07d eyr:2029 iyr:2014\n' +
        'ecl:oth\n' +
        '\n' +
        'eyr:2022 iyr:2012 ecl:grn pid:883419125\n' +
        'hcl:#ceb3a1\n' +
        'cid:136 hgt:75in\n' +
        'byr:1952\n' +
        '\n' +
        'iyr:2018 hgt:185cm\n' +
        'byr:1985 pid:119464380 eyr:2028 hcl:#623a2f ecl:gry\n' +
        '\n' +
        'eyr:2025 hcl:#ceb3a1 byr:1953\n' +
        'cid:277 hgt:164cm iyr:2010 pid:574253234\n' +
        '\n' +
        'cid:252 ecl:amb pid:594663323\n' +
        'hgt:75in hcl:#cfa07d iyr:2019\n' +
        'eyr:2026 byr:1964\n' +
        '\n' +
        'iyr:2026 hcl:z pid:60117235 ecl:lzr\n' +
        'byr:2016 hgt:156in eyr:1994\n' +
        '\n' +
        'pid:448392350\n' +
        'eyr:2022 hcl:#a97842\n' +
        'hgt:157cm\n' +
        'ecl:hzl\n' +
        'iyr:2018 byr:1973\n' +
        '\n' +
        'ecl:brn\n' +
        'byr:1951\n' +
        'eyr:2028\n' +
        'hcl:#7d3b0c iyr:2018 hgt:164cm\n' +
        '\n' +
        'hgt:156cm\n' +
        'byr:1963\n' +
        'iyr:2014 eyr:2020 ecl:blu hcl:#ceb3a1\n' +
        'pid:#a87d16\n' +
        '\n' +
        'pid:447170366 ecl:blu hcl:#888785\n' +
        'iyr:2012 cid:236\n' +
        'hgt:167cm\n' +
        'eyr:2022 byr:1942\n' +
        '\n' +
        'hcl:#623a2f\n' +
        'eyr:2020 iyr:2017 cid:128 ecl:amb pid:279550425\n' +
        'byr:1983 hgt:154cm\n' +
        '\n' +
        'byr:2014 eyr:2034 hgt:176in hcl:z\n' +
        'ecl:#d4e521\n' +
        'pid:3629053477 cid:177\n' +
        'iyr:1970\n' +
        '\n' +
        'pid:30370825 byr:1966 eyr:2026\n' +
        'iyr:2026 hcl:#866857\n' +
        'cid:346 ecl:#f7c189\n' +
        '\n' +
        'iyr:2010 pid:271066119 eyr:2023 hcl:#efcc98 hgt:179cm byr:1956\n' +
        '\n' +
        'byr:1966 hgt:156cm pid:977897485 cid:287 iyr:2011 hcl:#b6652a ecl:amb eyr:2029\n' +
        '\n' +
        'cid:211 ecl:gmt byr:2017\n' +
        'hcl:z eyr:2029 hgt:180in iyr:2021 pid:81920053\n' +
        '\n' +
        'byr:2019\n' +
        'pid:5229927737 hcl:75b4f1 hgt:146 iyr:2026 ecl:#92cf7d eyr:2032\n' +
        '\n' +
        'eyr:2027 pid:604671573\n' +
        'ecl:hzl\n' +
        'hgt:189cm byr:1979\n' +
        'hcl:#efcc98 iyr:2020\n' +
        '\n' +
        'iyr:2018 cid:192\n' +
        'eyr:2029 ecl:grn\n' +
        'pid:653764645 hgt:179cm\n' +
        'hcl:#341e13 byr:1927\n' +
        '\n' +
        'byr:2012\n' +
        'iyr:2015\n' +
        'hcl:#b6652a\n' +
        'pid:168500059 eyr:2038 cid:234 hgt:191cm ecl:zzz\n' +
        '\n' +
        'ecl:gry hcl:#623a2f byr:1925\n' +
        'iyr:2016\n' +
        'eyr:2028 cid:157\n' +
        'hgt:154cm\n' +
        'pid:196280865\n' +
        '\n' +
        'cid:319 pid:928322396 ecl:gry\n' +
        'byr:1949\n' +
        'eyr:2028\n' +
        'hcl:#341e13 hgt:171cm\n' +
        'iyr:2018\n' +
        '\n' +
        'byr:2023\n' +
        'iyr:1953 hgt:154cm ecl:dne\n' +
        'hcl:#888785\n' +
        'pid:066246061 eyr:1983\n' +
        '\n' +
        'hcl:z\n' +
        'iyr:2016 byr:1986 ecl:utc\n' +
        'hgt:179cm eyr:2019 pid:583251408\n' +
        '\n' +
        'ecl:amb iyr:2014 pid:499004360\n' +
        'byr:1927 eyr:2021 hgt:193cm hcl:#ceb3a1\n' +
        '\n' +
        'pid:631303194 ecl:gry\n' +
        'hcl:#18171d cid:216 iyr:2019\n' +
        'eyr:2024 hgt:178cm\n' +
        '\n' +
        'hcl:#341e13 cid:201\n' +
        'byr:1949 iyr:2019 ecl:gry pid:372356205\n' +
        'eyr:2024\n' +
        '\n' +
        'hcl:#18171d\n' +
        'pid:867489359\n' +
        'hgt:185cm\n' +
        'iyr:2020 ecl:amb\n' +
        'eyr:2030\n' +
        'byr:1955\n' +
        '\n' +
        'byr:1991\n' +
        'ecl:brn eyr:2025 hgt:184cm iyr:2016 pid:202216365\n' +
        '\n' +
        'ecl:xry pid:#524139 hgt:151cm hcl:z eyr:2031 byr:2030 iyr:2005\n' +
        '\n' +
        'byr:1971 hgt:178cm ecl:amb hcl:#ceb3a1\n' +
        'iyr:2010\n' +
        'eyr:2026 pid:396974525\n' +
        '\n' +
        'iyr:2014\n' +
        'hgt:177cm pid:928522073\n' +
        'eyr:2022\n' +
        'ecl:hzl\n' +
        'hcl:#c0946f byr:1983\n' +
        '\n' +
        'hgt:167cm hcl:#ceb3a1 iyr:2014\n' +
        'pid:172415447\n' +
        'eyr:2020 byr:1956\n' +
        '\n' +
        'iyr:2011 hgt:188cm byr:1947 eyr:2020 pid:667108134 ecl:amb hcl:#44a86b\n' +
        '\n' +
        'cid:302 ecl:brn pid:292483175 hgt:154cm\n' +
        'byr:1997\n' +
        'eyr:2026\n' +
        'iyr:2014 hcl:#623a2f\n' +
        '\n' +
        'hgt:171cm\n' +
        'iyr:2014 hcl:z ecl:hzl pid:321513523 eyr:2027 cid:146\n' +
        'byr:2001\n' +
        '\n' +
        'eyr:1956 ecl:dne hgt:75cm hcl:82e1fa\n' +
        'iyr:2030 byr:2027\n' +
        '\n' +
        'eyr:2020\n' +
        'iyr:2011 pid:656669479 ecl:oth hgt:151cm hcl:#efcc98 byr:1981\n' +
        '\n' +
        'iyr:2013\n' +
        'byr:1934\n' +
        'pid:142890410 hgt:62in\n' +
        'eyr:2022\n' +
        'hcl:#87cca4\n' +
        'ecl:hzl\n' +
        '\n' +
        'pid:006232726\n' +
        'hgt:173cm ecl:hzl cid:110\n' +
        'eyr:2026 hcl:#866857 iyr:2017 byr:1992\n' +
        '\n' +
        'cid:208\n' +
        'iyr:2014 ecl:brn eyr:2024 byr:1935 hgt:187cm\n' +
        'hcl:#b6652a\n' +
        'pid:770836724\n' +
        '\n' +
        'iyr:2014 cid:144 hgt:169cm\n' +
        'eyr:2022\n' +
        'ecl:oth\n' +
        'pid:117575716 hcl:#fffffd byr:1926\n' +
        '\n' +
        'byr:1971 ecl:brn\n' +
        'hcl:#733820 eyr:1942 iyr:2013\n' +
        'pid:606274259 hgt:163cm cid:196\n' +
        '\n' +
        'byr:1964\n' +
        'pid:997828217 eyr:2029 iyr:2017 ecl:blu hcl:#341e13\n' +
        'hgt:158cm\n' +
        '\n' +
        'pid:568202531 hcl:#efcc98 hgt:154cm eyr:2029 iyr:2010\n' +
        'byr:1946\n' +
        'ecl:blu\n' +
        '\n' +
        'iyr:2011\n' +
        'pid:619355919\n' +
        'byr:1955\n' +
        'ecl:brn hcl:#888785 eyr:2030 hgt:155cm\n' +
        '\n' +
        'ecl:hzl pid:367152545\n' +
        'hgt:162cm\n' +
        'cid:221 hcl:#866857\n' +
        'eyr:2024\n' +
        'byr:1997 iyr:2019\n' +
        '\n' +
        'hgt:157in\n' +
        'cid:268 hcl:32371d byr:2020\n' +
        'ecl:zzz pid:1081234390\n' +
        '\n' +
        'ecl:hzl eyr:2026\n' +
        'byr:1969 pid:850482906 cid:166 hcl:#602927 hgt:60in\n' +
        'iyr:2019\n' +
        '\n' +
        'hcl:#c0946f\n' +
        'hgt:176cm\n' +
        'ecl:brn eyr:2026 iyr:2018 cid:172 byr:1986 pid:172963254\n' +
        '\n' +
        'ecl:grn iyr:2016\n' +
        'hgt:187cm\n' +
        'byr:1983\n' +
        'hcl:#efcc98\n' +
        'pid:722084344 eyr:2025\n' +
        '\n' +
        'ecl:oth hcl:#341e13 pid:130312766 hgt:171cm iyr:2018 byr:1927 eyr:2024\n' +
        '\n' +
        'byr:2021 hgt:152cm hcl:74dda6\n' +
        'eyr:1984 cid:216\n' +
        'iyr:2018 pid:95283942\n' +
        '\n' +
        'hcl:#b6652a pid:924778815 iyr:2017 ecl:gry\n' +
        'eyr:2035\n' +
        'hgt:68cm\n' +
        '\n' +
        'iyr:2010\n' +
        'hcl:#efcc98 ecl:brn eyr:2020 pid:801894599 hgt:163cm byr:1959\n' +
        '\n' +
        'pid:798701070 eyr:2030\n' +
        'hcl:#866857 ecl:hzl hgt:169cm byr:1994 cid:219 iyr:2010\n' +
        '\n' +
        'pid:#e9b41b\n' +
        'hcl:#341e13 byr:1970\n' +
        'iyr:2014\n' +
        'ecl:oth cid:266 hgt:68cm eyr:2023\n' +
        '\n' +
        'byr:1931 pid:929960843 hgt:187cm hcl:#6b5442 cid:52 iyr:2010 eyr:2024 ecl:brn\n' +
        '\n' +
        'iyr:2017 byr:1974\n' +
        'ecl:hzl cid:243 pid:66053995 hgt:147 eyr:1920 hcl:z\n' +
        '\n' +
        'iyr:2012 byr:1962 ecl:brn pid:773399437 hcl:#341e13\n' +
        'eyr:2026\n' +
        '\n' +
        'pid:738442771 hgt:186cm eyr:2027 hcl:#efcc98 iyr:2013\n' +
        'ecl:brn byr:1928\n' +
        '\n' +
        'pid:855794198\n' +
        'ecl:oth\n' +
        'hgt:67in\n' +
        'cid:81\n' +
        'iyr:2011 hcl:#b6652a eyr:2020\n' +
        'byr:1921\n' +
        '\n' +
        'hcl:176abf hgt:161in\n' +
        'byr:2002 iyr:2016 eyr:2027 pid:639047770 ecl:brn\n' +
        'cid:178\n' +
        '\n' +
        'pid:335686451\n' +
        'hcl:#86c240 iyr:2017 hgt:190cm byr:1968 ecl:amb\n' +
        '\n' +
        'hgt:150cm\n' +
        'hcl:094a87 ecl:#09c463 eyr:1926 pid:537511570 byr:2009\n' +
        'iyr:1998\n' +
        '\n' +
        'hgt:74in\n' +
        'pid:927963411\n' +
        'eyr:2026 ecl:gry cid:323 iyr:2012 hcl:#fffffd byr:1959\n' +
        '\n' +
        'iyr:2018 byr:1978\n' +
        'hcl:#ff1829 eyr:2023\n' +
        'pid:823129853 ecl:hzl\n' +
        'hgt:65in\n' +
        '\n' +
        'pid:189cm\n' +
        'ecl:#00391e hgt:72cm hcl:11050f\n' +
        'byr:2029\n' +
        'eyr:1994\n' +
        'iyr:1935\n' +
        'cid:186\n' +
        '\n' +
        'ecl:grn byr:1942 pid:217290710 hgt:181cm eyr:2021 hcl:#7d3b0c iyr:2019 cid:320\n' +
        '\n' +
        'byr:1983 iyr:2013 cid:122 hcl:#ceb3a1 eyr:2030 hgt:59in ecl:grn pid:946451564\n' +
        '\n' +
        'ecl:amb\n' +
        'cid:236 hgt:184cm\n' +
        'hcl:#cfa07d iyr:2017 pid:934730535 eyr:2021 byr:2002\n' +
        '\n' +
        'byr:1950 ecl:hzl eyr:2030 hcl:#623a2f pid:742249321\n' +
        'hgt:158cm iyr:2018\n' +
        '\n' +
        'byr:1946 eyr:2021 hcl:#a97842 pid:204671558 ecl:grn\n' +
        'iyr:2010 hgt:187cm\n' +
        '\n' +
        'hcl:#b6652a pid:528124882 hgt:162cm byr:1924 ecl:amb iyr:2027 cid:157\n' +
        'eyr:2028\n' +
        '\n' +
        'hgt:180cm iyr:2013 byr:1926 pid:232265934 hcl:#602927 ecl:oth\n' +
        '\n' +
        'byr:1984 ecl:brn\n' +
        'iyr:2016 pid:756596443 eyr:2030 hcl:#7d3b0c hgt:183cm\n' +
        '\n' +
        'hgt:185cm\n' +
        'hcl:#fffffd byr:1991 eyr:2023 iyr:2014\n' +
        'ecl:amb\n' +
        'pid:759105859\n' +
        '\n' +
        'cid:82 iyr:2012 hgt:160cm eyr:2022 pid:593798464 ecl:gry hcl:#4e7571 byr:1983\n' +
        '\n' +
        'pid:478427550\n' +
        'iyr:2010\n' +
        'ecl:amb byr:1969 hgt:68in cid:94 eyr:2021 hcl:#866857\n' +
        '\n' +
        'ecl:amb iyr:2019 byr:1986 hgt:170cm\n' +
        'hcl:#c0946f\n' +
        'pid:779205106 eyr:2027\n' +
        '\n' +
        'ecl:brn eyr:2025 byr:1925\n' +
        'hcl:#7d3b0c hgt:76in pid:576353079 iyr:2010\n' +
        '\n' +
        'hgt:175cm hcl:4bf5ae ecl:amb\n' +
        'eyr:2029 pid:173cm cid:329\n' +
        'iyr:1952 byr:1972\n' +
        '\n' +
        'ecl:grn\n' +
        'eyr:2030\n' +
        'iyr:2015 hcl:#c0946f\n' +
        'byr:1989\n' +
        'hgt:178cm\n' +
        'pid:287209519\n' +
        '\n' +
        'pid:834505198 byr:1985 ecl:gry eyr:2024\n' +
        'cid:295 hgt:169cm iyr:2017\n' +
        '\n' +
        'hgt:170cm\n' +
        'pid:054644831 eyr:2023 iyr:1949 ecl:amb\n' +
        'hcl:#888785\n' +
        'byr:1955\n' +
        '\n' +
        'hgt:171cm\n' +
        'pid:947263309 iyr:2015 byr:1944 eyr:2027 ecl:grn cid:79 hcl:#341e13\n' +
        '\n' +
        'eyr:1982\n' +
        'cid:147\n' +
        'iyr:2015\n' +
        'hgt:70cm hcl:a77c10 ecl:zzz byr:2007\n' +
        'pid:161cm\n' +
        '\n' +
        'ecl:gry byr:1933\n' +
        'hcl:#c0946f pid:483275512 iyr:2012 eyr:2025 hgt:161cm\n' +
        '\n' +
        'eyr:1985 hgt:176cm hcl:7b6ddc iyr:2012 cid:326 byr:1973 pid:929418396 ecl:gmt\n' +
        '\n' +
        'ecl:gry\n' +
        'byr:1971\n' +
        'hgt:184cm\n' +
        'eyr:2027 hcl:#3adf2c iyr:2017 cid:210\n' +
        'pid:693561862\n' +
        '\n' +
        'eyr:2021 pid:779298835 byr:1921 hgt:193cm ecl:amb\n' +
        'iyr:2016 hcl:#ceb3a1\n' +
        '\n' +
        'hcl:4a1444\n' +
        'byr:2019 iyr:2024 hgt:182in\n' +
        'cid:87 ecl:#122264\n' +
        'pid:181cm\n' +
        'eyr:1927\n' +
        '\n' +
        'cid:267 ecl:amb eyr:2020 byr:2000\n' +
        'hcl:#18171d iyr:2012 hgt:190cm pid:18525759\n' +
        '\n' +
        'ecl:oth byr:1988\n' +
        'iyr:2019 pid:660570833\n' +
        'hcl:#866857 hgt:176cm\n' +
        '\n' +
        'eyr:2030 hcl:#866857\n' +
        'byr:1967 cid:316 pid:560346474 iyr:2015\n' +
        'hgt:160cm\n' +
        'ecl:gry\n' +
        '\n' +
        'ecl:hzl\n' +
        'iyr:2014 hgt:164cm hcl:#733820 eyr:2025\n' +
        'pid:106302413 byr:1920\n' +
        '\n' +
        'iyr:2016 pid:515066491\n' +
        'ecl:grn eyr:2026 hgt:179cm hcl:#b6652a byr:1982\n' +
        '\n' +
        'ecl:#7de6a0\n' +
        'iyr:2004 eyr:1955 hgt:154cm cid:138 byr:2004\n' +
        'pid:758934555\n' +
        'hcl:a21980\n' +
        '\n' +
        'pid:#2a21e0 ecl:#1b9b27 hgt:165in\n' +
        'byr:1998 iyr:2014 eyr:2032\n' +
        '\n' +
        'eyr:2021 hgt:184cm pid:431054313 hcl:#ceb3a1 cid:109 byr:1977 ecl:blu\n' +
        'iyr:2011\n' +
        '\n' +
        'pid:006339126 hgt:177cm\n' +
        'cid:188 hcl:#a97842\n' +
        'iyr:1959\n' +
        'ecl:xry\n' +
        '\n' +
        'byr:2000\n' +
        'ecl:hzl eyr:2029\n' +
        'iyr:2011 hcl:#866857 hgt:74in\n'


}
