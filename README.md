# RKVideoConverter
[![](https://jitpack.io/v/RufenKhokhar/RKVideoConverter.svg)](https://jitpack.io/#RufenKhokhar/RKVideoConverter)

## Based on ffmpeg file formates
### Steps for quick staring 

## Step-1
 Configer project for java8, add the following code into app level build.gradle file
```
 android{
    .......
    compileOptions{
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
       }
 ```   
 ## step-2 
   add the following code into project level build.gradle file
 ```
 allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```  
## Step-3
add the following dependency into app level build.gradle file and sync project
```
dependencies {
	        implementation 'com.github.RufenKhokhar:RKVideoConverter:Tag'
	}
```
##  Now it's time to intialized the library. it's importent otherwise library don't works
 the variable (status) true means library sucssfully initialized, otherwise not.
```
VideoConverter.initialize(this, status -> Toast.makeText(this, ""+status, Toast.LENGTH_SHORT).show());
```
## now it's time to convert video (.mp4) to (.3gp). give the input as .mp4 file
```
VideoConverter.Builder builder = new VideoConverter.Builder(this)
                .setFastStart(true)
                .setOutputFormat("3gp");
        VideoConverter converter = builder.build();
        
        converter.convertVideo(sourceFile, targetFile, new EncoderProgressListener() {
            @Override
            public void onStartEncoding(MediaInfo info) {
                
            }

            @Override
            public void onUpdateProgress(int progress) {

            }

            @Override
            public void onSendMassage(String message) {

            }

            @Override
            public void onCompleteEncoding(int completionCode) {

            }

            @Override
            public void onReceivedError(Exception e) {

            }
        });
        
 ```       
