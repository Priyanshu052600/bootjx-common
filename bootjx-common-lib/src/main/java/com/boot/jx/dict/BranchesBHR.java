package com.boot.jx.dict;

public enum BranchesBHR {

	ALL_GOLDEN_SANDS("26.235821","50.588763","Golden Sands , Shop B 259 - Road 1805 - Block 318 Manama – Al Hoora , Jibin Sivanandan : 33627340"),
	ALL_GUDAYBIYAH("26.227112","50.590792","Gudaybiyah , Shop 197 - Road 333 - Block 321 Manama – Al Gudaybiyah , Sari Rajesh : 33627341"),
	ALL_FILIPINO_GARDEN("26.227012","50.585836","Filipino Garden , Shop 100 G - Building 100 - Road 342 - Block 308 Manama – Al Gudaybiyah , Rosita Cruz : 33627342"),
	ALL_MUHARRAQ("26.2572841","50.6119196","Muharraq , Shop 243/245 - Road 51 - Shaikh Isa - Block 206 Muharraq , Antony Praveen : 33627343"),
	ALL_RIFFA("26.122623","50.566184","Riffa , Shop 101 - Road 82 - Block 905 Riffa Soqe , Mubarak Ali : 33627344"),
	ALL_BAB_AL_BAHRAIN("26.232999","50.574308","Bab Al Bahrain , Shop 181 – Government Avenue – Block 304 Manama Centre , Sidhina Bineesh : 33627345"),
	ALL_ASKAR("26.081468","50.625861","Askar , Shop 1880A – Road 5233 – Ras Zuwayed – Block 952 Askar , Johnson Rajkumar : 33627346"),
	ALL_BANGALI_ROAD("26.227443","50.572356","Bangali Road , Shop 2278B – Road 237 – Manama Center - Block 302 Manama Centre , Waleed Hassan : 33627347"),
	ALL_HAMAD_TOWN("26.141677","50.494447","Hamad Town , Shop 76 – Road 303 – Hamad Town - Block 1203 Hamad Town , Naveed : 33627348"),
	ALL_UM_AL_HASSAM("26.203005","50.596203","Um Al Hassam , Shop A322 – Sheikh Ateyatallah Bin Abdulrahman Avenue – Manama- Umm AlHassam 337 , Hasan Jawadi : 33627349"),
	ALL_HIDD("26.210557","50.678111","Hidd , Shop 5 – Building 2178 – Road 1529 Hidd 115 , Rahul Raj : 33627350"),
	ALL_AKER("26.143196","50.602496","Aker , Shop 145A – West Aker Avenue 6003 Al – Aker Al-Gharbi 626 , Prakash Derasari : 33627351"),
	ALL_ASKER2("26.086214","50.612231","Asker2 , Shop A1146 – Road 5223 - Ras Zuwayed – Block 952 Askar , Aditya Prakash : 33627352"),
	ALL_SALAMABAD("26.184357","50.531686","Salamabad , Shop B0524-Road 412-Block 704 Salmabad , Alen Joseph : 33627353"),
	ALL_TUBLI("26.190249","50.556425","Tubli , CR: 91824-17 Shop 82-Road 11-Block 713 Tubli , Mohammed Sohel : 33627355"),
	ALL_BUDAIYAH("26.212746","50.450595","Budaiyah , CR: 91824-18 Shop 138-Road 5505-Block 555 Budaiyah , Sayed Majeed : 33627356"),
	ALL_GALALI("26.272546","50.650642","Galali , CR: 91824-19 Shop 538-Road 5215-Block 252 Galali , Arjun Mahesan : 33627354");

	private String latitude;
	private String longitude;
	private String address;

	BranchesBHR(String latitude, String longitude, String address) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
