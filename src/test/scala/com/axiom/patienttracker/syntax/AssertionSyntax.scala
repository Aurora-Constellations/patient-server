package com.axiom.patienttracker.syntax

import zio.* 
import zio.test.* 

extension[R,E,A](zio: ZIO[R,E,A])
    def assert(assertion: Assertion[A]): ZIO[R, E, TestResult] =
        assertZIO(zio)(assertion): ZIO[R, E, TestResult]

    def assert(predicate: (=> A) => Boolean): ZIO[R, E, TestResult] =
        //syntax of assertion take by-name argument
        // by-name argument is prepend with `=>`
        // the advantage is that a function argument won’t be evaluated until the corresponding value is used inside the function body
        assert(Assertion.assertion("test assertion")(predicate))