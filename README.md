
![logo](https://github.com/nisrulz/zentone/raw/master/app/src/main/res/mipmap-xhdpi/ic_launcher.png)

#ZenTone

Generating pure tone of an specific frequency was never that easy.
ZenTone does all the heavy lifting for you.

Checkout the app using the same in [Playstore](https://play.google.com/store/apps/details?id=in.excogitation.library_zentone)

#Integration
- Zentone is available in the MavenCentral, so getting it as simple as adding it as a dependency
```gradle
compile 'com.github.nisrulz:zentone:1.0.2'
```

#Usage
+ To play a tone of certain frequency and duration
```java
ZenTone.getInstance().generate(freq, duration, new ToneStoppedListener() {
      @Override
      public void onToneStopped() {
          // Do something when the tone has stopped playing
      }
  });
```
*where*

|argument|type|value in
|---|---|---|
|freq|`int`|Hz
|duration|`int`|seconds


+ To stop the tone
```java
ZenTone.getInstance().stop();
```


---
> *NOTE : Some variables used in build.gradle correspond to*

> **COMPILE_SDK_VERSION**=23

> **BUILDTOOLS_VERSION**=23.0.1

> **VERSION_NAME**=1.0.1

> **VERSION_CODE**=3

# License

 <a rel="license" href="http://www.apache.org/licenses/LICENSE-2.0.html" target="_blank">Apache License 2.0</a>
