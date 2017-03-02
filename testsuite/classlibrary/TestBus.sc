
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

		s.options.maxLogins.do { |clientID|
			var busses, numBusses;

			s.clientID = clientID; // in clientID setter, s.newAllocators happens;

			numBusses = s.options.numAudioBusChannels - (s.options.numOutputBusChannels + s.options.numInputBusChannels);

			busses = Array.fill(numBusses, { Bus.audio(s, 1) });

			this.assert(busses.every(_.notNil),"should be able to allocate all busses");
			this.assertEquals( busses.select(_.notNil).size, numBusses," should be numAudioBusChannels busses");

			busses.do({ |b| b.free });

			busses = Array.fill(numBusses, { Bus.audio(s, 1) });

			this.assert(
				busses.every(_.notNil),
				"after freeing, should be able to re-allocate all busses (clientID: %)".format(clientID)
			);
			this.assertEquals(
				busses.select(_.notNil).size,
				numBusses,
				" should be numAudioBusChannels busses (clientID: %)".format(clientID)
			);
		};

	}

	test_controlFree {
		var s,busses;
		s = Server.default;
		s.newAllocators;

		busses = Array.fill(s.options.numControlBusChannels, { Bus.control(s, 1) });
		this.assertEquals(
			busses.select(_.notNil).size,
			s.options.numControlBusChannels,
			" should be numControlBusChannels busses"
		);

		busses.do({ |b| b.free });

		busses = Array.fill(s.options.numControlBusChannels, { Bus.control(s,1) });

		this.assertEquals(
			busses.select(_.notNil).size,
			s.options.numControlBusChannels,
			" should be numControlBusChannels busses able to allocate again after freeing all"
		);
	}

	// note: server reboot does not de-allocate busses
	// and isn't supposed to

}