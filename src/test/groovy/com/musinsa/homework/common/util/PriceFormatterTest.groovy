package com.musinsa.homework.common.util

import spock.lang.Specification

class PriceFormatterTest extends Specification {
    def "format 호출시 양수가 정상처리된다."() {
        given:
        def price = 1000000

        when:
        def result = PriceFormatter.format(price)

        then:
        result == "1,000,000"
    }

    def "format 호출시 0이 정상처리된다."() {
        given:
        def price = 0

        when:
        def result = PriceFormatter.format(price)

        then:
        result == "0"
    }

    def "format 호출시 음수가 정상처리된다."() {
        given:
        def price = -5000

        when:
        def result = PriceFormatter.format(price)

        then:
        result == "-5,000"
    }

}
