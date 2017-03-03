
TestBus : UnitTest {

	test_firstBusNumbers {
		var s, ba, bk;
		s = Server.default;
		s.newAllocators;
		ba = Bus.audio(s, 1);
		bk = Bus.control(s, 1);
		this.assert(ba.index == s.options.firstPrivateBus, "audio bus allocator should use first available");
		this.assert(bk.index == 0, "control bus allocator should use first available")
	}

	test_free {
		var s;
		s = Server.default;
		s.options.maxLogins = 32;
		s.options.maxLogins.do { |clientID|
			var busses, numBusses;

			s.clientID = clientID; // in the clientID setter, s.newAllocators happens

			numBusses = s.options.numPrivateAudioBusChannels div: s.options.maxLogins;
			"testing audio bus allocation with clientID %\n".postf(s.clientID);

			busses = Array.fill(numBusses, { Bus.audio(s, 1) });

			this.assert(busses.every(_.notNil),"should be able to allocate all busses");
			this.assertEquals(
				busses.select(_.notNil).size,
				numBusses,
				" should be numPrivateAudioBusChannels div: % busses (%)".format(s.options.maxLogins, numBusses)
			);

			busses.do({ |b| b.free });

			busses = Array.fill(numBusses, { Bus.audio(s, 1) });

			this.assert(
				busses.every(_.notNil),
				"after freeing, should be able to re-allocate all busses"
			);
			this.assertEquals(
				busses.select(_.notNil).size,
				numBusses,
				" should be numPrivateAudioBusChannels div: % busses (%)".format(s.options.maxLogins, numBusses)
			);
		};

	}

	test_controlFree {
		var s;
		s = Server.default;
		s.options.maxLogins = 32;
		s.options.maxLogins.do { |clientID|
			var busses, numBusses;

			numBusses = s.options.numControlBusChannels div: s.options.maxLogins;

			s.clientID = clientID; // in the clientID setter, s.newAllocators happens

			busses = Array.fill(numBusses, { Bus.control(s, 1) });

			"testing control bus allocation with clientID %\n".postf(s.clientID);
			this.assert(busses.every(_.notNil),"should be able to allocate all busses");
			this.assertEquals(
				busses.select(_.notNil).size,
				s.options.numControlBusChannels,
				" should be numControlBusChannels busses"
			);

			busses.do({ |b| b.free });

			busses = Array.fill(numBusses, { Bus.control(s,1) });
			this.assert(
				busses.every(_.notNil),
				"after freeing, should be able to re-allocate all busses"
			);
			this.assertEquals(
				busses.select(_.notNil).size,
				s.options.numControlBusChannels,
				" should be numControlBusChannels  div: % busses (%) able to allocate again after freeing all"
				.format(s.options.maxLogins, numBusses)
			);
		}
	}

		// note: server reboot does not de-allocate busses
		// and isn't supposed to

}
