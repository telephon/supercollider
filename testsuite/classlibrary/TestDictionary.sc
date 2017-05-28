TestDictionary : UnitTest {

	test_keysValuesArrayDo {

		this.assert(Dictionary[].keysValuesArrayDo([]) == Dictionary[], "keysValuesArrayDo on an empty dictionary should do nothing");

		this.assert(
			try { Dictionary[].keysValuesArrayDo(nil) } { |err|
				err.isKindOf(PrimitiveFailedError)
			},
			"calling keysValuesArrayDo on nil should throw a PrimitiveFailedError"
		)

	}


	test_insertParent {


		var x, y, u, v, a, b;
		var parentChain = { |dict|
			var chain;
			while { dict.notNil } {
				chain = chain.add(dict);
				dict = dict.parent;
			};
			chain
		};
		var makeEvents = {

			u = (u:0);
			x = (x:1);
			a = (a:2);

			x.parent = u;
			a.parent = x;

			v = (v:3);
			y = (y:4);
			b = (b:5);

			y.parent = v;
			b.parent = y;

		};
		var checkSpec = { |dict, order|
			var chain = parentChain.(dict);
			this.assert(chain == order,
				"insertParent order should be according to spec.\nIs: %\nShould be:%".format(chain, order)
			);
		};

		makeEvents.value;
		a[\k] = 2;
		x[\k] = 1;
		x[\x] = 1;
		this.assert(a[\x] == 1, "parent should be reachable if child has no key");
		this.assert(a[\k] == 2, "child should override parent key");
		this.assert(parentChain.(a) == [a, x, u], "parentChain self test");
		this.assert(parentChain.(b) == [b, y, v], "parentChain self test");

		// the specs are derived from the documentation of Event::insertParent

		makeEvents.value;
		a.insertParent(b, 0, inf);
		checkSpec.(a, [a, b, y, v, x, u]);

		makeEvents.value;
		a.insertParent(b, 1, inf);
		checkSpec.(a, [a, x, b, y, v, u]);

		makeEvents.value;
		a.insertParent(b, 0, 0);
		checkSpec.(a, [a, b, x, u, y, v]);


	}


}
