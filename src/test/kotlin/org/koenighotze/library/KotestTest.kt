package org.koenighotze.library

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class KotestTest : FunSpec({
    test("a test") {
        1 + 2 shouldBe 3
    }
})

//class OtherTest : StringSpec({
//    "something" {
//        "hello".length shouldBe 2
//    }
//})
