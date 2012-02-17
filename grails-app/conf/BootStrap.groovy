import citu.*

class BootStrap {

    def init = { servletContext ->
		if (!EnergyFileRef.count()) {
			def xml1 = new EnergyFileRef(urlPath:'http://20.20.3.20/xmlwater3.xml', category:'Water').save()
			def xml2 = new EnergyFileRef(urlPath:'http://20.20.3.21/xmlwater4.xml', category:'Water').save()
			def xml3 = new EnergyFileRef(urlPath:'http://20.20.3.22/xmlwater5.xml', category:'Water').save()
			def xml4 = new EnergyFileRef(urlPath:'http://20.20.3.23/xmlwater6.xml', category:'Water').save()
			def xml5 = new EnergyFileRef(urlPath:'http://20.20.3.24/xmlwater7.xml', category:'Water').save()
			def xml6 = new EnergyFileRef(urlPath:'http://20.20.3.25/xmlwater8.xml', category:'Water').save()
			def xml7 = new EnergyFileRef(urlPath:'http://20.20.3.26/xmlwater9.xml', category:'Water').save()
			def xml8 = new EnergyFileRef(urlPath:'http://20.20.3.27/xmlwater10.xml', category:'Water').save()
			def xml9 = new EnergyFileRef(urlPath:'http://20.20.3.28/xmlwater11.xml', category:'Water').save()
			def xml10 = new EnergyFileRef(urlPath:'http://20.20.3.29/xmlwater12.xml', category:'Water').save()
			def xml11 = new EnergyFileRef(urlPath:'http://20.20.3.30/xmlwater13.xml', category:'Water').save()
			def xml12 = new EnergyFileRef(urlPath:'http://20.20.3.20/xmlelectricity3.xml', category:'Electricity').save()
			def xml13 = new EnergyFileRef(urlPath:'http://20.20.3.23/xmlelectricity6.xml', category:'Electricity').save()
			def xml14 = new EnergyFileRef(urlPath:'http://20.20.3.25/xmlelectricity8.xml', category:'Electricity').save()
			def xml15 = new EnergyFileRef(urlPath:'http://20.20.3.28/xmlelectricity11.xml', category:'Electricity').save()
		}
		if (!TarrifList.count()) {
			TarrifList tl = new TarrifList(coldWaterTarrif:0.003339, hotWaterTarrif:0.006249, greyWaterTarrif:0.001365, elecTarrif:0.108, heatTarrif:0.1163).save()
		}
		if(!User.count()) {
			User phil = new User(userType: 'admin', password:'password', userName:'psellick', firstName:'Phil', lastName:'Sellick', contactEmail:'sonic@sellick.org', vmUserId:'45465654').save()
			//User ben = new User(userType: 'admin', password:'password'.encodeAsHash(), userName:'bhanson', firstName:'Ben', lastName:'Hanson', contactEmail:'ben@chilling.co.uk', vmUserId:'12154852').save()
			User ben = new User(userType: 'admin', password:'password', userName:'bhanson', firstName:'Ben', lastName:'Hanson', contactEmail:'ben@chilling.co.uk', vmUserId:'12154852').save()
			User neil = new User(userType: 'user', userName:'nillingworth', firstName:'Neil', lastName:'Illingworth', contactEmail:'neil@virginmedia.com', vmUserId:'12154852').save()
			User helen = new User(userType: 'user', userName:'hsellick', firstName:'Helen', lastName:'Sellick', contactEmail:'helen@virginmedia.com', vmUserId:'12154852').save()
			User john = new User(userType: 'user', userName:'jhiggins', firstName:'John', lastName:'Higgins', contactEmail:'john@virginmedia.com', vmUserId:'12154852').save()
			Premise philsFlat = new Premise(rooms:1, flatNo:'308', addressLine1:'Sonic House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:phil).save()
			Premise bensFlat = new Premise(rooms:1, flatNo:'309', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW15 5AS', user:ben).save()
			Premise bobsFlat = new Premise(rooms:1, flatNo:'607', addressLine1:'Sonic House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:phil).save()
			Premise keithsFlat = new Premise(rooms:1, flatNo:'215',addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW15 5AS', user:ben).save()
			Premise johnsFlat = new Premise(rooms:2, flatNo:'606', addressLine1:'Sonic House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:john).save()
			Premise helenFlat = new Premise(rooms:2, flatNo:'608', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:helen).save()
			Premise jacksFlat = new Premise(rooms:2, flatNo:'106', addressLine1:'Sonic House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:phil).save()
			Premise neilsFlat = new Premise(rooms:2, flatNo:'610', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			SetTopBox stb1 = new SetTopBox(macAddress:'AA:FF:EE:00:EE', TSMid:'12315464', premise:philsFlat).save()
			SetTopBox stb2 = new SetTopBox(macAddress:'A4:FF:TT:60:EE', TSMid:'345345', premise:bensFlat).save()
			SetTopBox stb3 = new SetTopBox(macAddress:'6A:FF:CC:50:EE', TSMid:'345564', premise:bobsFlat).save()
			SetTopBox stb4 = new SetTopBox(macAddress:'56:FF:YY:06:EE', TSMid:'5467345', premise:keithsFlat).save()
			SetTopBox stb5 = new SetTopBox(macAddress:'AA:34:CC:70:EE', TSMid:'876345354', premise:johnsFlat).save()
			SetTopBox stb6 = new SetTopBox(macAddress:'5A:FF:CC:u0:EE', TSMid:'834234', premise:helenFlat).save()
			SetTopBox stb7 = new SetTopBox(macAddress:'AA:FF:65:06:EE', TSMid:'23546', premise:jacksFlat).save()
			SetTopBox stb8 = new SetTopBox(macAddress:'FA:BF:45:00:EE', TSMid:'36315464', premise:neilsFlat).save()
			SetTopBox stb9 = new SetTopBox(macAddress:'WR:BF:T5:00:EE', TSMid:'6345345', premise:neilsFlat).save()
		}

		if (!ElecReading.count()) {
			def now = (new Date() - 50)

			Premise p = Premise.findByFlatNo("215")
			Premise p1 = Premise.findByFlatNo("606")
			Premise p2 = Premise.findByFlatNo("608")

			def random = new Random()
			50.times {
				now = (now + 1)
				24.times{
					ElecReading tmpReading = new ElecReading(readingValueElec:random.nextInt(8), fileDate:now, premise:p1).save()
					ElecReading tmpReading1 = new ElecReading(readingValueElec:random.nextInt(8), fileDate:now, premise:p2).save()
					ElecReading tmpReading2 = new ElecReading(readingValueElec:random.nextInt(8), fileDate:now, premise:p).save()
					WaterReading tmpWater = new WaterReading(fileDate:now, readingValueCold:random.nextInt(30), readingValueHot:random.nextInt(30) + 2, readingValueGrey:random.nextInt(30) + 1, premise:p).save()
					//HeatReading tmpHeat = new HeatReading(heatReading:random.nextInt(60), heatCost:0, premise:p).save()
				}
			}
		}
    }
    def destroy = {
    }
}
