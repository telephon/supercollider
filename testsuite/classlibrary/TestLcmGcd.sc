
TestLcmGcd : UnitTest {


	lcm { |a, b|
		^lcm(a, b)
	}

	// test implementation
	gcd { |a, b|

		if(a == 0) { ^b };
		if(b == 0) { ^a };
		^gcd(a, b)

	}

	// lattice theoretic laws
	// https://en.wikipedia.org/wiki/Least_common_multiple#Lattice-theoretic

	callTest_commutative_lcm { |a, b|
		var x = this.lcm(a, b);
		var y = this.lcm(b, a);
		this.assertEquals(x, y, "lcm(%, %) = lcm(%, %) should be valid"
			.format(a, b, b, a))
	}
	callTest_commutative_gcd { |a, b|
		var x = this.gcd(a, b);
		var y = this.gcd(b, a);
		this.assertEquals(x, y, "gcd(%, %) = gcd(%, %) should be valid"
			.format(a, b, b, a))
	}

	callTest_associative_lcm { |a, b, c|
		var x = this.lcm(a, this.lcm(b, c));
		var y = this.lcm(this.lcm(a, b), c);
		this.assertEquals(x, y, "lcm(%, lcm(%, %)) = lcm(lcm(%, %), %) should be valid"
			.format(a, b, c, a, b, c))
	}

	callTest_associative_gcd { |a, b, c|
		var x = this.gcd(a, this.gcd(b, c));
		var y = this.gcd(this.gcd(a, b), c);
		this.assertEquals(x, y, "gcd(%, gcd(%, %)) = gcd(gcd(%, %), %) should be valid"
			.format(a, b, c, a, b, c))
	}

	callTest_absorption_lcm { |a, b|
		var x = this.lcm(a, this.gcd(a, b));
		this.assertEquals(a, x, "lcm(%, gcd(%, %)) should be valid"
			.format(a, a, b))
	}

	callTest_absorption_gcd { |a, b|
		var x = this.gcd(a, this.lcm(a, b));
		this.assertEquals(a, x, "gcd(%, lcm(%, %)) should be valid"
			.format(a, a, b))
	}

	callTest_idempotence_lcm { |a|
		var x = this.lcm(a, a);
		this.assertEquals(a, x, "% = lcm(%, %) should be valid"
			.format(a, a, a))
	}

	callTest_idempotence_gcd { |a|
		var x = this.gcd(a, a);
		this.assertEquals(a, x, "% = gcd(%, %) should be valid"
			.format(a, a, a))
	}

	callTest_distributive_lcm { |a, b, c|
		var x = this.lcm(a, this.gcd(b, c));
		var y = this.gcd(this.lcm(a, b), this.lcm(a, c));
		this.assertEquals(x, y, "lcm(%, gcd(%, %)) = gcd(lcm(%, %), lcm(%, %)) should be valid"
			.format(a, b, c, a, b, a, c))
	}

	callTest_distributive_gcd { |a, b, c|
		var x = this.gcd(a, this.lcm(b, c));
		var y = this.lcm(this.gcd(a, b), this.gcd(a, c));
		this.assertEquals(x, y, "gcd(%, lcm(%, %)) = lcm(gcd(%, %), gcd(%, %)) should be valid"
			.format(a, b, c, a, b, a, c))
	}

	callTest_selfDuality { |a, b, c|

		var x = this.gcd(this.gcd(this.lcm(a, b), this.lcm(b, c)), this.lcm(a, c));
		var y = this.lcm(this.lcm(this.gcd(a, b), this.gcd(b, c)), this.gcd(a, c));
		this.assertEquals(x, y,
			"gcd(gcd(lcm(%, %), lcm(%, %)), lcm(%, %)) = lcm(lcm(gcd(%, %), gcd(%, %)), gcd(%, %))"
			"should be valid".format(
				a, b, b, c, a, c,
				a, b, b, c, a, c
			)
		)
	}

	// see E.E. McDonnell, A Notation for the GCD and LCM Functions
	// http://www.jsoftware.com/papers/eem/gcd.htm

	callTest_booleanLattic_lcm_and { |a, b|
		var a_bool = booleanValue(a);
		var b_bool = booleanValue(b);
		var a_num = binaryValue(a);
		var b_num = binaryValue(b);
		var x = and(a_bool, b_bool);
		var y = this.lcm(a_num, b_num);
		this.assertEquals(x, booleanValue(y), "lcm(%, %) should be equivalent to and(%, %)".format(
			a_num, b_num, a_bool, b_bool
		));
	}

	callTest_booleanLattic_gcd_or { |a, b|
		var a_bool = booleanValue(a);
		var b_bool = booleanValue(b);
		var a_num = binaryValue(a);
		var b_num = binaryValue(b);
		var x = or(a_bool, b_bool);
		var y = this.gcd(a_num, b_num);
		this.assertEquals(x, booleanValue(y), "gcd(%, %) should be equivalent to or(%, %)".format(
			a_num, b_num, a_bool, b_bool
		));
	}


	// tests


	test_commutative {
		var operands = (-4..4).dup(2).allTuples;
		operands.do { |pair| this.callTest_commutative_lcm(*pair) };
		operands.do { |pair| this.callTest_commutative_gcd(*pair) };
	}

	test_associative {
		var operands = (-4..4).dup(3).allTuples;
		operands.do { |triple| this.callTest_associative_lcm(*triple) };
	}

	test_idempotence {
		var operands = (-4..4);
		operands.do { |x| this.callTest_idempotence_lcm(x) };
		operands.do { |x| this.callTest_idempotence_gcd(x) };
	}

	test_absorption_lcm {
		var operands = (-4..4).dup(2).allTuples;
		operands.do { |pair| this.callTest_absorption_lcm(*pair) };
	}

	test_absorption_gcd {
		var operands = (-4..4).dup(2).allTuples;
		operands.do { |pair| this.callTest_absorption_gcd(*pair) };
	}

	test_distributive {
		var operands = (-4..4).dup(3).allTuples;
		operands.do { |triple| this.callTest_distributive_lcm(*triple) };
		operands.do { |triple| this.callTest_distributive_gcd(*triple) };

	}

	test_selfDuality {
		var operands = (-4..4).dup(3).allTuples;
		operands.do { |triple| this.callTest_selfDuality(*triple) };
	}


	test_booleanLattice {
		var operands = [true, false].dup(2).allTuples;
		operands.do { |pair| this.callTest_booleanLattic_lcm_and(*pair) };
	}

}
