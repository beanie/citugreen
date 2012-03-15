import citu.*

class BootStrap {

    def init = { servletContext ->
		if (!EnergyFileRef.count()) {
			
			def xml1 = new EnergyFileRef(urlPath:'http://office.citu.co.uk:9020/xmlwater3.xml', category:'Water').save()
			def xml2 = new EnergyFileRef(urlPath:'http://office.citu.co.uk:9021/xmlwater4.xml', category:'Water').save()
			def xml3 = new EnergyFileRef(urlPath:'http://office.citu.co.uk:9022/xmlwater5.xml', category:'Water').save()
			def xml4 = new EnergyFileRef(urlPath:'http://office.citu.co.uk:9023/xmlwater6.xml', category:'Water').save()
			def xml5 = new EnergyFileRef(urlPath:'http://office.citu.co.uk:9024/xmlwater7.xml', category:'Water').save()
			def xml6 = new EnergyFileRef(urlPath:'http://office.citu.co.uk:9025/xmlwater8.xml', category:'Water').save()
			def xml7 = new EnergyFileRef(urlPath:'http://office.citu.co.uk:9026/xmlwater9.xml', category:'Water').save()
			def xml8 = new EnergyFileRef(urlPath:'http://office.citu.co.uk:9027/xmlwater10.xml', category:'Water').save()
			def xml9 = new EnergyFileRef(urlPath:'http://office.citu.co.uk:9028/xmlwater11.xml', category:'Water').save()
			def xml10 = new EnergyFileRef(urlPath:'http://office.citu.co.uk:9029/xmlwater12.xml', category:'Water').save()
			def xml11 = new EnergyFileRef(urlPath:'http://office.citu.co.uk:9030/xmlwater13.xml', category:'Water').save()
			def xml12 = new EnergyFileRef(urlPath:'http://office.citu.co.uk:9020/xmlelectricity3.xml', category:'Electricity').save()
			def xml13 = new EnergyFileRef(urlPath:'http://office.citu.co.uk:9023/xmlelectricity6.xml', category:'Electricity').save()
			def xml14 = new EnergyFileRef(urlPath:'http://office.citu.co.uk:9025/xmlelectricity8.xml', category:'Electricity').save()
			def xml15 = new EnergyFileRef(urlPath:'http://office.citu.co.uk:9028/xmlelectricity11.xml', category:'Electricity').save()
			
			
//			def xml1 = new EnergyFileRef(urlPath:'http://20.20.3.20/xmlwater3.xml', category:'Water').save()
//			def xml2 = new EnergyFileRef(urlPath:'http://20.20.3.21/xmlwater4.xml', category:'Water').save()
//			def xml3 = new EnergyFileRef(urlPath:'http://20.20.3.22/xmlwater5.xml', category:'Water').save()
//			def xml4 = new EnergyFileRef(urlPath:'http://20.20.3.23/xmlwater6.xml', category:'Water').save()
//			def xml5 = new EnergyFileRef(urlPath:'http://20.20.3.24/xmlwater7.xml', category:'Water').save()
//			def xml6 = new EnergyFileRef(urlPath:'http://20.20.3.25/xmlwater8.xml', category:'Water').save()
//			def xml7 = new EnergyFileRef(urlPath:'http://20.20.3.26/xmlwater9.xml', category:'Water').save()
//			def xml8 = new EnergyFileRef(urlPath:'http://20.20.3.27/xmlwater10.xml', category:'Water').save()
//			def xml9 = new EnergyFileRef(urlPath:'http://20.20.3.28/xmlwater11.xml', category:'Water').save()
//			def xml10 = new EnergyFileRef(urlPath:'http://20.20.3.29/xmlwater12.xml', category:'Water').save()
//			def xml11 = new EnergyFileRef(urlPath:'http://20.20.3.30/xmlwater13.xml', category:'Water').save()
//			def xml12 = new EnergyFileRef(urlPath:'http://20.20.3.20/xmlelectricity3.xml', category:'Electricity').save()
//			def xml13 = new EnergyFileRef(urlPath:'http://20.20.3.23/xmlelectricity6.xml', category:'Electricity').save()
//			def xml14 = new EnergyFileRef(urlPath:'http://20.20.3.25/xmlelectricity8.xml', category:'Electricity').save()
//			def xml15 = new EnergyFileRef(urlPath:'http://20.20.3.28/xmlelectricity11.xml', category:'Electricity').save()
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
			User kath = new User(userType: 'user', userName:'ksmith', firstName:'Katherine', lastName:'Smith', contactEmail:'info@catherinesmithonline.com', vmUserId:'12154852').save()
			User steve = new User(userType: 'user', userName:'ksmith', firstName:'Katherine', lastName:'Smith', contactEmail:'Stephen.Perry2@virginmedia.co.uk', vmUserId:'12154854').save()
			User ryan = new User(userType: 'user', userName:'ksmith', firstName:'Katherine', lastName:'Smith', contactEmail:'Ryan.Gilmore@virginmedia.co.uk', vmUserId:'12154855').save()
			
			Premise Flat1 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:40, flatNo:'1', addressLine1:'Sonic House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:kath).save()
			Premise Flat2 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:3, squareArea:76,flatNo:'2', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW15 5AS', user:steve).save()
			Premise Flat3 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:46,flatNo:'3', addressLine1:'Sonic House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:phil).save()
			Premise Flat4 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:2, squareArea:54,flatNo:'4',addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW15 5AS', user:ben).save()
			Premise Flat5 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:40,flatNo:'5', addressLine1:'Sonic House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:john).save()
			Premise Flat6 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:2, squareArea:54,flatNo:'6', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:helen).save()
			Premise Flat7 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:38,flatNo:'7', addressLine1:'Sonic House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:phil).save()
			Premise Flat8 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:3, squareArea:76,flatNo:'8', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat9 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:40,flatNo:'9', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat12 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:40,flatNo:'12', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat14 = new Premise(bathrooms:2, premiseType:'Flat', core:'None', bedrooms:3, squareArea:76, flatNo:'14', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat15 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:43,flatNo:'15', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat16 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:33,flatNo:'16', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat17 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:33,flatNo:'17', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat18 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:33,flatNo:'18', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat19 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:33,flatNo:'19', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat20 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:33,flatNo:'20', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat21 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:33,flatNo:'21', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat22 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:33,flatNo:'22', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat23 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:43,flatNo:'23', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat24 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:3, squareArea:76,flatNo:'24', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat25 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:40,flatNo:'25', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat26 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:2, squareArea:56,flatNo:'26', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat101 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:2, squareArea:59,flatNo:'101', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat102 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:2, squareArea:42,flatNo:'102', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat103 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:3, squareArea:79,flatNo:'103', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat104 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:47,flatNo:'104', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat105 = new Premise(bathrooms:2, premiseType:'Flat', core:'None', bedrooms:2, squareArea:59,flatNo:'105', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat106 = new Premise(bathrooms:2, premiseType:'Flat', core:'None', bedrooms:2, squareArea:59,flatNo:'106', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat107 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:2, squareArea:57,flatNo:'107', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat108 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:40,flatNo:'108', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat109 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:3, squareArea:79,flatNo:'109', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat110 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:42,flatNo:'110', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat113 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:42,flatNo:'111', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat114 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:3, squareArea:79,flatNo:'112', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat115 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:44,flatNo:'113', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat116 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:2, squareArea:60,flatNo:'114', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat117 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:42,flatNo:'115', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat118 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:60,flatNo:'116', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat119 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:37,flatNo:'117', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat120 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:3, squareArea:79,flatNo:'118', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat121 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:42,flatNo:'119', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat122 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:2, squareArea:59,flatNo:'120', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat201 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:2, squareArea:59,flatNo:'201', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat202 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:42,flatNo:'202', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat203 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:79,flatNo:'203', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat204 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:47,flatNo:'204', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat205 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:57,flatNo:'205', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat206 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:42,flatNo:'206', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat207 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:2, squareArea:77,flatNo:'207', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat208 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:40,flatNo:'208', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat209 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:40,flatNo:'209', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat210 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:42,flatNo:'210', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat212 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:42,flatNo:'212', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat213 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:42,flatNo:'213', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat214 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:42,flatNo:'214', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat215 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:44,flatNo:'215', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat216 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:2, squareArea:42,flatNo:'216', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat217 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:42,flatNo:'217', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat218 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:2, squareArea:60,flatNo:'218', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat219 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:37,flatNo:'219', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat220 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:3, squareArea:79,flatNo:'220', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat221 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:42,flatNo:'221', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat222 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:2, squareArea:42,flatNo:'222', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			Premise Flat223 = new Premise(bathrooms:1, premiseType:'Flat', core:'None', bedrooms:1, squareArea:42,flatNo:'223', addressLine1:'Beanie House', addressLine2:'CituGreen Est.', postCode:'SW5 3AP', user:neil).save()
			
						
			SetTopBox stb1 = new SetTopBox(macAddress:'44:58:29:17:2A:EC', TSMid:'12315464', premise:Flat114).save()
			SetTopBox stb2 = new SetTopBox(macAddress:'44:58:29:17:23:D2', TSMid:'345345', premise:Flat2).save()
			SetTopBox stb3 = new SetTopBox(macAddress:'44:58:29:17:C1:52', TSMid:'345564', premise:Flat3).save()
			SetTopBox stb4 = new SetTopBox(macAddress:'44:58:29:17:C1:53', TSMid:'5467345', premise:Flat4).save()
			SetTopBox stb5 = new SetTopBox(macAddress:'44:58:29:17:C1:54', TSMid:'876345354', premise:Flat5).save()
			SetTopBox stb6 = new SetTopBox(macAddress:'44:58:29:17:C1:55', TSMid:'834234', premise:Flat6).save()
			SetTopBox stb7 = new SetTopBox(macAddress:'44:58:29:17:C1:56', TSMid:'23546', premise:Flat7).save()
			SetTopBox stb8 = new SetTopBox(macAddress:'44:58:29:17:C1:57', TSMid:'36315464', premise:Flat8).save()
			SetTopBox stb9 = new SetTopBox(macAddress:'44:58:29:17:C1:58', TSMid:'6345345', premise:Flat9).save()
			SetTopBox stb12 = new SetTopBox(macAddress:'44:58:29:17:C1:59', TSMid:'6345345', premise:Flat12).save()
		}

		
	//	if (!ElecReading.count()) {
	//		def now = (new Date() - 50)

	//		Premise p = Premise.findByFlatNo("215")
	//		Premise p1 = Premise.findByFlatNo("606")
	//		Premise p2 = Premise.findByFlatNo("106")
	//		Premise p3 = Premise.findByFlatNo("608")
	// 	Premise p4 = Premise.findByFlatNo("610")
			
	//		def random = new Random()
			
	//		50.times {
			//	now = (now + 1)
		//		24.times{
				//	ElecReading tmpReading = new ElecReading(readingValueElec:random.nextInt(8), fileDate:now, premise:p1).save()
				//	ElecReading tmpReading1 = new ElecReading(readingValueElec:random.nextInt(8), fileDate:now, premise:p2).save()
				//	ElecReading tmpReading2 = new ElecReading(readingValueElec:random.nextInt(8), fileDate:now, premise:p).save()
				//	ElecReading tmpReading3 = new ElecReading(readingValueElec:random.nextInt(8), fileDate:now, premise:p3).save()
				//	ElecReading tmpReading4 = new ElecReading(readingValueElec:random.nextInt(8), fileDate:now, premise:p4).save()
				//	WaterReading tmpWater = new WaterReading(fileDate:now, readingValueCold:random.nextInt(30), readingValueHot:random.nextInt(30) + 2, readingValueGrey:random.nextInt(30) + 1, premise:p1).save()
				//	WaterReading tmpWater1 = new WaterReading(fileDate:now, readingValueCold:random.nextInt(30), readingValueHot:random.nextInt(30) + 2, readingValueGrey:random.nextInt(30) + 1, premise:p2).save()
				//	WaterReading tmpWater2 = new WaterReading(fileDate:now, readingValueCold:random.nextInt(30), readingValueHot:random.nextInt(30) + 2, readingValueGrey:random.nextInt(30) + 1, premise:p3).save()
				//	WaterReading tmpWater3 = new WaterReading(fileDate:now, readingValueCold:random.nextInt(30), readingValueHot:random.nextInt(30) + 2, readingValueGrey:random.nextInt(30) + 1, premise:p4).save()
				//	WaterReading tmpWater4 = new WaterReading(fileDate:now, readingValueCold:random.nextInt(30), readingValueHot:random.nextInt(30) + 2, readingValueGrey:random.nextInt(30) + 1, premise:p).save()
					//HeatReading tmpHeat = new HeatReading(heatReading:random.nextInt(60), heatCost:0, premise:p).save()
			//	}
		//	}
			
		//}
    }
    def destroy = {
    }
}
