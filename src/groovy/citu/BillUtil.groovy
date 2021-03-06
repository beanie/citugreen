package citu

class BillUtil {

	static TarrifList tarrifList = TarrifList.get(1)

	static Float calcTotal(ArrayList costs) {
		def tmpFloat = 0
		for (i in costs) {
			if (i != null) {
				tmpFloat += i
			}
		}
		return tmpFloat
	}

	static Float calcTotalHeatCost(ArrayList costs){
		return (calcTotal(costs)*tarrifList.heatTarrif)
	}

	static Float calcTotalElecCost(ArrayList costs){
		return (calcTotal(costs)*tarrifList.elecTarrif)
	}

	static Float calcTotalGreyWaterCost(ArrayList costs){
		return (calcTotal(costs)*tarrifList.greyWaterTarrif)
	}

	static Float calcTotalHotWaterCost(ArrayList costs){
		return (calcTotal(costs)*tarrifList.hotWaterTarrif)
	}

	static Float calcTotalColdWaterCost(ArrayList costs){
		return (calcTotal(costs)*tarrifList.coldWaterTarrif)
	}

	static Float aveHotWaterByRoom(ArrayList costs, int rooms) {
		return (calcTotalColdWaterCost(costs)/rooms)
	}
	
	static Float calcElecPriceByVolume(Double reading) {
		if (reading) {
		//	println("READING "+ reading)
			return (reading*tarrifList.elecTarrif)
		} else {
	//	println("ZERO")
			return 0
		}
	}
	
	
	static Float calcWaterPriceByVolume(Double reading) {
		return (reading*tarrifList.coldWaterTarrif)
	}
	static Float calcHotWaterPriceByVolume(Double reading) {
		return (reading*tarrifList.hotWaterTarrif)
	}
		static Float calcColdWaterPriceByVolume(Double reading) {
		return (reading*tarrifList.coldWaterTarrif)
	}
	
	static Float calcGreyWaterPriceByVolume(Double reading) {
		return (reading*tarrifList.greyWaterTarrif)
	}
	static Float calcHeatPriceByVolume(Double reading) {
		return (reading*tarrifList.heatTarrif)
	}

}
