# rean-native-lsblocation

Getting location from gps or network without using google play service.



##Installing

#### Install npm package.
```
npm i --save react-native-lsblocation
```

#### Set Android project up.

- In android/settings.gradle
```
include ':LSBlocation',':app'
project(':LSBlocation').projectDir = new File('../node_modules/react-native-lsblocation/android/lsblocation')

```

- In android/app/build.gradle
```
dependencies {
	compile project(':LSBlocation')
}
```

- In MainApplication.java

```
protected List<ReactPackage> getPackages() {
  return Arrays.<ReactPackage>asList(
      new MainReactPackage(),
          new LSBLocationPackage()  <== add new line here
  );
}
```

- Add permission in AndroidManifest file
```
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

```

##Example

```
var MyLocation = require('react-native-lsblocation');

DeviceEventEmitter.addListener('updateLocation', function(e: Event) {

  const data = {
     coords: {
      latitude: e.Latitude,
      longitude: e.Longitude
    }
  }
  
  ${data.coords.latitude}:${data.coords.longitude}`);
  
}.bind(this));


MyLocation.getCurrentLocation();

```


** You also could mix geolocation for handling ios devices.**

```
getLocation(successCallback,errorCallback,options){
	if(Platform.OS == 'ios'){
	  console.log(`getLocation ios`);
	  navigator.geolocation.getCurrentPosition(
	    (position) => {

	      const data = {
	         coords: {
	          latitude: position.coords.latitude,
	          longitude: position.coords.longitude
	        }
	      }
	      console.log(` ios ${data.coords.latitude}:${data.coords.ongitude}`);
	      successCallback(data);
	    },
	    (error) => { errorCallback(error) },
	    options
	  );
	}else{
	  console.log(`getLocation Android`);
	  DeviceEventEmitter.addListener('updateLocation', function(e: Event) {

	      const data = {
	         coords: {
	          latitude: e.Latitude,
	          longitude: e.Longitude
	        }
	      }
	      console.log(`getLocation Android ${data.coords.latitude}:${data.coords.longitude}`);
	      successCallback(data);
	  }.bind(this));

	  MyLocation.getCurrentLocation();
	}

}




//Call getLocation function
getLocation(
    (position)=>{
        console.log(`ShopList_new updateGeoInfomation lat:${position.coords.latitude} lng:${position.coords.longitude}`);
        alert(`lat:${position.coords.latitude} lng:${position.coords.longitude}`);
      },
    (error)=>{alert(`${error.message} 請打開GPS`);},
    {enableHighAccuracy: false, timeout: 20000, maximumAge: 1000 });



```
